package com.zhangteng.xim.callback;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangteng.xim.MyApplication;
import com.zhangteng.xim.R;
import com.zhangteng.xim.common.config.NetConfig;
import com.zhangteng.xim.common.tools.Encryption;
import com.zhangteng.xim.common.tools.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lanxumit on 2017/12/29.
 */

public abstract class ResponseRetrofitCallBack implements Callback<String> {

    private static final String DEFAULT_LOADING_TEXT = "正在加载...";
    private static Dialog mProgressDialog;
    private final String NETWORK_REQUEST_FAIL_STR = "请求失败";
    private Context mContext;
    private boolean isShow;
    private boolean isLongTimeDialog = false;
    private String mLoadingText;
    private boolean isNeedDecrypt;


    public ResponseRetrofitCallBack(Context context, boolean isShowLoadingDialog) {
        this(context, isShowLoadingDialog, DEFAULT_LOADING_TEXT);
    }

    public ResponseRetrofitCallBack(Context context, boolean isShowLoadingDialog, String dialogText) {
        this(context, true, isShowLoadingDialog, dialogText, false);
    }

    public ResponseRetrofitCallBack(Context context, boolean isShowLoadingDialog, boolean isLongTimeDialog) {
        this(context, true, isShowLoadingDialog, DEFAULT_LOADING_TEXT, isLongTimeDialog);
    }

    public ResponseRetrofitCallBack(Context context, boolean isNeedDecrypt, boolean isShowLoadingDialog, String dialogText) {
        this(context, isNeedDecrypt, isShowLoadingDialog, dialogText, false);
    }

    public ResponseRetrofitCallBack(Context context, boolean isNeedDecrypt, boolean isShowLoadingDialog, String dialogText, boolean isLongTimeDialog) {
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

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) {
        if (response.isSuccessful()) {
            String body = (String) response.body();
            if (body.equals("") || body == null) {
                if (!isNeedDecrypt) {
                    onSuccess(body);
                } else {
                    String json = Encryption.desEncrypt(body);
                    String content = decodeUnicode(json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(content.trim());
                        String code = jsonObject.getString(NetConfig.ResponseKeyConfig.CODE);
                        String message = jsonObject.getString(NetConfig.ResponseKeyConfig.MESSAGE);
                        if (!code.equals("") || code != null) {
                            switch (Integer.parseInt(code)) {
                                case NetConfig.ResponseCodeConfig.OK:
                                case NetConfig.ResponseCodeConfig.SUCCESS:
                                    if (jsonObject.has("data")) {
                                        String data = jsonObject.getString(NetConfig.ResponseKeyConfig.DATA);
                                        onSuccess(data);
                                    } else {
                                        onSuccess(message);
                                    }
                                    break;
                                default:
                                    onFailure(code, message);
                                    break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onFailure();
                    }
                }
            } else {
                toast(NETWORK_REQUEST_FAIL_STR);
                onFailure();
            }
        } else {
            toast(NETWORK_REQUEST_FAIL_STR);
            onFailure();
        }
        dismissProgressDialog();
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull Throwable t) {
        dismissProgressDialog();
        if (t instanceof UnknownHostException) {
            Toast.makeText(mContext, "请检查网络连接", Toast.LENGTH_SHORT).show();
        } else if (t instanceof SocketTimeoutException) {
            Toast.makeText(mContext, "当前网络链接过慢，请稍后再试", Toast.LENGTH_SHORT).show();
        } else if (t instanceof ConnectException) {
            Toast.makeText(mContext, "网络不可用", Toast.LENGTH_SHORT).show();
        } else {
            onFailure();
        }
    }

    public abstract void onSuccess(String json);

    public void onFailure() {
        dismissProgressDialog();
    }

    public void onFailure(String code) {
        onFailure();
    }

    public void onFailure(String code, String message) {
        toast(message);
        onFailure(code);
    }

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

    private String decodeUnicode(String asciicode) {
        String[] asciis = asciicode.split("\\\\u");
        String nativeValue = asciis[0];
        try {
            for (int i = 1; i < asciis.length; i++) {
                String code = asciis[i];
                nativeValue += (char) Integer.parseInt(code.substring(0, 4), 16);
                if (code.length() > 4) {
                    nativeValue += code.substring(4, code.length());
                }
            }
        } catch (NumberFormatException e) {
            return asciicode;
        }
        return nativeValue;
    }
}
