package com.zhangteng.xim.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhangteng.loadingview.LoadingView;
import com.zhangteng.swiperecyclerview.adapter.HeaderOrFooterAdapter;
import com.zhangteng.swiperecyclerview.widget.PullDownLoadingRecyclerView;
import com.zhangteng.xim.R;
import com.zhangteng.xim.adapter.SendAdapter;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.http.IMApi;
import com.zhangteng.xim.db.DBManager;
import com.zhangteng.xim.db.bean.LocalUser;
import com.zhangteng.xim.event.RefreshEvent;
import com.zhangteng.xim.utils.ActivityHelper;
import com.zhangteng.xim.utils.AppManager;
import com.zhangteng.xim.utils.StringUtils;
import com.zhangteng.xim.utils.SystemBarTintManager;
import com.zhangteng.xim.utils.ToastUtils;
import com.zhangteng.xim.widget.TitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.v3.exception.BmobException;

public class SendActivity extends AppCompatActivity implements MessageListHandler,
        PullDownLoadingRecyclerView.PullDownListener<List<BmobIMMessage>>,
        View.OnClickListener {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    PullDownLoadingRecyclerView recyclerView;
    @BindView(R.id.send)
    ConstraintLayout send;
    @BindView(R.id.edit_msg)
    EditText editText;
    @BindView(R.id.btn_send)
    Button button;
    private LocalUser user;
    private String objectId;
    private List<BmobIMConversation> list;
    private List<BmobIMMessage> data;
    private SendAdapter sendAdapter;
    private HeaderOrFooterAdapter headerOrFooterAdapter;
    private BmobIMConversation bmobIMConversation;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 经测试在代码里直接声明透明状态栏更有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏
            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint 激活导航栏
            tintManager.setNavigationBarTintEnabled(true);
            //给状态栏设置颜色
            tintManager.setStatusBarTintResource(R.color.theme);
        }
        super.setContentView(getResourceId());
        EventBus.getDefault().register(this);
        AppManager.addActivity(this);
        setContentView(getResourceId());
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(RefreshEvent event) {

    }

    protected int getResourceId() {
        return R.layout.activity_send;
    }


    protected void initView() {
        data = new ArrayList<>();
        sendAdapter = new SendAdapter(this, data);
        headerOrFooterAdapter = new HeaderOrFooterAdapter(sendAdapter) {
            @Override
            public RecyclerView.ViewHolder createHeaderOrFooterViewHolder(ViewGroup parent, Integer viewInt) {
                LoadingView loadingView = (LoadingView) LayoutInflater.from(SendActivity.this).inflate(viewInt, parent, false);
                return new LoadingViewHolder(loadingView);
            }

            @Override
            public void onBindHeaderOrFooterViewHolder(@NonNull RecyclerView.ViewHolder holder, int viewType) {

            }

            class LoadingViewHolder extends RecyclerView.ViewHolder {
                private LoadingView loadingView;

                public LoadingViewHolder(View itemView) {
                    super(itemView);
                    loadingView = (LoadingView) itemView;
                }
            }
        };
        headerOrFooterAdapter.addHeaderView(R.layout.loading_item);
        recyclerView.setAdapter(headerOrFooterAdapter);
        recyclerView.setListener(this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        Intent intent = getIntent();
        if (intent.hasExtra("objectId")) {
            objectId = getIntent().getStringExtra("objectId");
        }
        Bundle bundle = getBundle();
        BmobIMConversation conversationEntrance = null;
        if (bundle != null) {
            conversationEntrance = (BmobIMConversation) getBundle().getSerializable("c");
        }
        if (conversationEntrance != null) {
            bmobIMConversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
            queryMessage(conversationEntrance, true, 10);
        } else {
            list = IMApi.ConversationManager.getInstance().loadAllConversation();
            for (BmobIMConversation bmobIMConversation : list) {
                if (bmobIMConversation.getConversationId().equals(objectId) || bmobIMConversation.getConversationTitle().equals(objectId)) {
                    this.bmobIMConversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), bmobIMConversation);
                    queryMessage(bmobIMConversation, true, 10);
                }
            }
        }
        button.setOnClickListener(this);
    }


    protected void initData() {
        if (StringUtils.isNotEmpty(objectId)) {
            user = DBManager.instance().queryUser(objectId);
            titleBar.setTitleText(user.getUsername());
        } else if (bmobIMConversation != null) {
            titleBar.setTitleText(bmobIMConversation.getConversationTitle());
        }
    }

    private void queryMessage(BmobIMConversation bmobIMConversation, final boolean isFistLoad, int limit) {
        IMApi.MessageManager.getInstance().queryMessages(bmobIMConversation, null, limit, new BmobCallBack<List<BmobIMMessage>>(this, false) {
            @Override
            public void onSuccess(@Nullable List<BmobIMMessage> bmobObject) {
                if (isFistLoad) {
                    data.clear();
                    data.addAll(bmobObject);
                    sendAdapter.setData(data);
                    sendAdapter.notifyDataSetChanged();
                    headerOrFooterAdapter.notifyDataSetChanged();
                    linearLayoutManager.scrollToPositionWithOffset(data.size() - 1, 0);
                } else {
                    recyclerView.stopPullDown(bmobObject);
                }
            }

            @Override
            public void onFailure(BmobException bmobException) {
                super.onFailure(bmobException);
                if (!isFistLoad) {
                    recyclerView.stopPullDown(null);
                }
            }
        });
    }


    private void sendMessage() {
        IMApi.MassageSender.getInstance().sendMessage(editText.getText().toString(), bmobIMConversation, new BmobCallBack<BmobIMMessage>(SendActivity.this, false) {
            @Override
            public void onSuccess(@Nullable BmobIMMessage bmobObject) {
                Log.e("message", "success");
                editText.setText("");
                sendAdapter.addMessage(bmobObject);
                linearLayoutManager.scrollToPositionWithOffset(data.size() - 1, 0);
                EventBus.getDefault().post(new RefreshEvent());
                bmobIMConversation.updateReceiveStatus(bmobObject);
            }

            @Override
            public void onFailure(BmobException bmobException) {
                super.onFailure(bmobException);
                editText.setText("");
                Log.e("message", "failure");
                ToastUtils.showShort(SendActivity.this, "send failed");
            }
        });
    }

    public Bundle getBundle() {
        if (getIntent() != null && getIntent().hasExtra(getPackageName())) {
            return getIntent().getBundleExtra(getPackageName());
        } else {
            return null;
        }
    }

    public void buttonClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_left_back:
                goBack();
                break;
            default:
                break;
        }
    }


    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int mesageId) {
        Toast.makeText(this, mesageId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startActivity(Intent intent) {
        try {
            super.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            showToast("未找到相应应用");
        }
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        try {
            super.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            showToast("未找到相应应用");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void goBack() {
        ActivityHelper.setActivityAnimClose(this);
        AppManager.finishActivity(this);
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        for (int i = 0; i < list.size(); i++) {
            MessageEvent event = list.get(i);
            if (event != null && bmobIMConversation != null && event.getConversation().getConversationId().equals(bmobIMConversation.getConversationId()))
                sendAdapter.addMessage(event.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BmobIM.getInstance().addMessageListHandler(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BmobIM.getInstance().removeMessageListHandler(this);
    }

    @Override
    public void onLoading() {
        if (bmobIMConversation != null) {
            queryMessage(bmobIMConversation, false, 10 + data.size());
        } else {
            recyclerView.stopPullDown(data);
        }
    }

    @Override
    public void onFinish(List<BmobIMMessage> bmobIMMessages) {
        int index;
        if (data.size() == bmobIMMessages.size()) {
            index = 0;
        } else if (bmobIMMessages.size() % 10 == 0) {
            index = 10;
        } else {
            index = bmobIMMessages.size() % 10;
        }
        data.clear();
        data.addAll(bmobIMMessages);
        sendAdapter.setData(data);
        sendAdapter.notifyDataSetChanged();
        headerOrFooterAdapter.notifyDataSetChanged();
        linearLayoutManager.scrollToPositionWithOffset(index, 0);
    }

    @Override
    public void onClick(View view) {
        if (StringUtils.isNotEmpty(editText.getText().toString())) {
            if (bmobIMConversation == null) {
                BmobIMUserInfo bmobIMUserInfo = new BmobIMUserInfo();
                bmobIMUserInfo.setUserId(user.getObjectId());
                bmobIMUserInfo.setAvatar(user.getIcoPath());
                bmobIMUserInfo.setName(user.getUsername());
                IMApi.ConversationManager.getInstance().startPrivateConversation(bmobIMUserInfo, false, new BmobCallBack<BmobIMConversation>(SendActivity.this, false) {
                    @Override
                    public void onSuccess(@Nullable final BmobIMConversation bmobIMConversation) {
                        SendActivity.this.bmobIMConversation = bmobIMConversation;
                        sendMessage();
                    }
                });
            } else {
                sendMessage();
            }
        }
    }
}
