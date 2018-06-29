package com.zhangteng.xim.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.johnnygq.treerecyclerview.adapter.TreeRecyclerViewAdapter;
import com.johnnygq.treerecyclerview.tree.Node;
import com.zhangteng.xim.R;
import com.zhangteng.xim.adapter.AreaTreeAdapter;
import com.zhangteng.xim.base.BaseActivity;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.bmob.params.UpdateUserParams;
import com.zhangteng.xim.db.DBManager;
import com.zhangteng.xim.db.bean.CityNo;
import com.zhangteng.xim.db.bean.LocalUser;
import com.zhangteng.xim.event.UserRefreshEvent;
import com.zhangteng.xim.utils.AppManager;
import com.zhangteng.xim.utils.AssetsUtils;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.exception.BmobException;

public class AreaSelectActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private AreaTreeAdapter adapter;
    private List<CityNo> mData = new ArrayList<>();
    private TreeRecyclerViewAdapter.OnTreeNodeClickListener mOnTreeNodeClickListener = new TreeRecyclerViewAdapter.OnTreeNodeClickListener() {
        @Override
        public void onClick(Node node, int position) {
            if (node.isLeaf()) {//最后一层
                final UpdateUserParams updateUserParams = new UpdateUserParams();
                updateUserParams.setAreaId(node.getId());
                updateUserParams.setCityId(node.getParent().getId());
                updateUserParams.setProvinceId(node.getParent().getParent().getId());
                UserApi.getInstance().updateUser(updateUserParams, new BmobCallBack(AreaSelectActivity.this, false) {
                    @Override
                    public void onSuccess(@Nullable Object bmobObject) {
                        LocalUser localUser = DBManager.instance(DBManager.USERNAME).queryUser(UserApi.getInstance().getUserInfo().getObjectId());
                        localUser.setAreaId(updateUserParams.getAreaId());
                        localUser.setCityId(updateUserParams.getCityId());
                        localUser.setProvinceId(updateUserParams.getProvinceId());
                        Intent intent = new Intent();
                        intent.putExtra("user", User.getUser(localUser));
                        AreaSelectActivity.this.setResult(2002, intent);
                        EventBus.getDefault().post(new UserRefreshEvent(User.getUser(localUser)));
                        AppManager.finishActivity(AreaSelectActivity.this);
                    }

                    @Override
                    public void onFailure(BmobException bmobException) {
                        super.onFailure(bmobException);
                        AreaSelectActivity.this.showToast("修改失败");
                    }
                });
            }
        }
    };

    @Override
    protected int getResourceId() {
        return R.layout.activity_area_select;
    }

    @Override
    protected void initInject() {

    }

    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.getItemAnimator().setChangeDuration(300);
        recyclerView.getItemAnimator().setMoveDuration(300);
        String json = AssetsUtils.getJson("cityno.json", this);
        Type listType = new TypeToken<List<CityNo>>() {
        }.getType();
        //这里的json是字符串类型 = jsonArray.toString();
        List<CityNo> list = new Gson().fromJson(json, listType);
        forTreeList(list);
        try {
            adapter = new AreaTreeAdapter(recyclerView, this, mData, mData.size());
            recyclerView.setAdapter(adapter);
            adapter.setOnTreeNodeClickListener(mOnTreeNodeClickListener);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {
    }

    private void forTreeList(List<CityNo> list) {
        mData.addAll(list);
        for (CityNo cityNo : list) {
            if (cityNo.getRegionEntitys() != null)
                forTreeList(cityNo.getRegionEntitys());
        }
    }
}
