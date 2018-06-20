package com.zhangteng.xim.bmob.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.zhangteng.updateversionlibrary.UpdateVersion;
import com.zhangteng.updateversionlibrary.asynctask.AsyncDownloadForeground;
import com.zhangteng.updateversionlibrary.callback.DownloadCallback;
import com.zhangteng.updateversionlibrary.callback.VersionInfoCallback;
import com.zhangteng.updateversionlibrary.entity.VersionEntity;
import com.zhangteng.updateversionlibrary.http.HttpClient;
import com.zhangteng.updateversionlibrary.utils.JSONHandler;
import com.zhangteng.updateversionlibrary.utils.URLUtils;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.Version;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by swing on 2018/6/20.
 */
public class UpdateVersionClient implements HttpClient {
    private Context mContext;
    private FragmentManager mFragmentManager;

    public UpdateVersionClient(Context mContext, FragmentManager mFragmentManager) {
        this.mContext = mContext;
        this.mFragmentManager = mFragmentManager;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getVersionInfo(String versionInfoUrl, final VersionInfoCallback versionInfoCallback) {
        new AsyncTask<String, Integer, VersionEntity>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                versionInfoCallback.onPreExecute(mContext, mFragmentManager, UpdateVersionClient.this);
            }

            @Override
            protected VersionEntity doInBackground(String... params) {
                final VersionEntity[] versionEntity = {null};
                if (params.length == 0) {
                    Log.e("NullPointerException",
                            " Url parameter must not be null.");
                    return null;
                }
                String url = params[0];
                if (!URLUtils.isNetworkUrl(url)) {
                    return null;
                }
                try {
                    if (UpdateVersion.isUpdateTest()) {
                        versionEntity[0] = JSONHandler.toVersionEntity(VersionInfoCallback.nativeAssertGet("versionInfo.json"));
                    } else {
                        versionEntity[0] = new VersionEntity();
                        Version version = DataApi.getInstance().queryVersion();
                        versionEntity[0].setId(version.getObjectId());
                        versionEntity[0].setAppId(version.getAppId());
                        versionEntity[0].setName(version.getName());
                        if (UpdateVersion.isUpdateDownloadWithBrowser()) {
                            versionEntity[0].setUrl(UpdateVersion.getCheckUpdateCommonUrl());
                        } else {
                            versionEntity[0].setUrl(version.getUrl());
                        }
                        versionEntity[0].setVersionCode(version.getVersionCode());
                        versionEntity[0].setVersionNo(version.getVersionNo());
                    }

                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                versionInfoCallback.doInBackground(versionEntity[0]);
                return versionEntity[0];
            }

            @Override
            protected void onPostExecute(VersionEntity versionEntity) {
                super.onPostExecute(versionEntity);
                versionInfoCallback.onPostExecute();
            }
        }.execute(versionInfoUrl);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void downloadApk(VersionEntity versionEntity, final DownloadCallback downloadCallback) {
        AsyncDownloadForeground asyncDownloadForeground = new AsyncDownloadForeground() {
            @Override
            public void doOnPreExecute() {
                downloadCallback.onPreExecute(mContext);
            }

            @Override
            public void doOnPostExecute(Boolean flag) {
                downloadCallback.onPostExecute(flag);
            }

            @Override
            public void doDoInBackground(long total, File apkFile) {
                downloadCallback.doInBackground(total, apkFile);
            }

            @Override
            public void doOnProgressUpdate(Integer... values) {
                downloadCallback.onProgressUpdate(values);
            }
        };
        asyncDownloadForeground.execute(versionEntity);
    }
}
