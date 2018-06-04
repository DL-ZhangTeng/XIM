package com.zhangteng.xim.bmob.callback;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangteng.xim.MyApplication;
import com.zhangteng.xim.R;
import com.zhangteng.xim.bmob.tools.NetUtil;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by swing on 2018/2/11.
 */

public abstract class BmobCallBack<T> {
    private static final String DEFAULT_LOADING_TEXT = "正在加载...";
    private static Dialog mProgressDialog;
    private final String NETWORK_REQUEST_FAIL_STR = "请求失败";
    private Context mContext;
    private boolean isShow;
    private boolean isLongTimeDialog = false;
    private String mLoadingText;
    private boolean isNeedDecrypt;


    public BmobCallBack(Context context, boolean isShowLoadingDialog) {
        this(context, isShowLoadingDialog, DEFAULT_LOADING_TEXT);
    }

    public BmobCallBack(Context context, boolean isShowLoadingDialog, String dialogText) {
        this(context, true, isShowLoadingDialog, dialogText, false);
    }

    public BmobCallBack(Context context, boolean isShowLoadingDialog, boolean isLongTimeDialog) {
        this(context, true, isShowLoadingDialog, DEFAULT_LOADING_TEXT, isLongTimeDialog);
    }

    public BmobCallBack(Context context, boolean isNeedDecrypt, boolean isShowLoadingDialog, String dialogText) {
        this(context, isNeedDecrypt, isShowLoadingDialog, dialogText, false);
    }

    public BmobCallBack(Context context, boolean isNeedDecrypt, boolean isShowLoadingDialog, String dialogText, boolean isLongTimeDialog) {
        this.mContext = context;
        this.isShow = isShowLoadingDialog;
        this.mLoadingText = dialogText;
        this.isNeedDecrypt = isNeedDecrypt;
        this.isLongTimeDialog = isLongTimeDialog;
        checkIsNoNetwork();
    }

    private void checkIsNoNetwork() {
        if (mContext == null) {
            return;
        }
        if (NetUtil.getNetWorkConnectState(mContext)) {
            Toast.makeText(mContext, "网络不可用", Toast.LENGTH_SHORT).show();
        }
    }

    public void onStart() {
        if (isShow) {
            showProgressDialog();
        }
    }

    public void onResponse(@Nullable T bmobObject, BmobException bmobException) {
        if (bmobException == null) {
            onSuccess(bmobObject);
            dismissProgressDialog();
        } else {
            onFailure(bmobException);
        }
    }

    public void onFailure(BmobException bmobException) {
        dismissProgressDialog();
    }

    public abstract void onSuccess(@Nullable T bmobObject);


    private void showProgressDialog() {
        if (mProgressDialog == null || !isLongTimeDialog) {
            dismissProgressDialog();
            mProgressDialog = new Dialog(mContext, R.style.progress_dialog);
            View view = View.inflate(mContext, R.layout.layout_dialog_progress, null);
            TextView loadView = (TextView) view.findViewById(R.id.loadView);
            if (mLoadingText != null) {
                loadView.setText(mLoadingText);
            }
            mProgressDialog.setContentView(view);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        } else if (mProgressDialog != null && !mProgressDialog.isShowing() && isLongTimeDialog) {
            mProgressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing() && !isLongTimeDialog) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     * 手动关闭长时间dialog
     */
    public void dismissLongTimeProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing() && isLongTimeDialog) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    private void toast(String message) {
        if (message.equals("") || message == null) {
            Toast.makeText(MyApplication.getInstance(), message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 文件上传下载时的进度
     */
    public void onProgress(Integer value) {

    }
}
