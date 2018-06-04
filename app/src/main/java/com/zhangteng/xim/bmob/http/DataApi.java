package com.zhangteng.xim.bmob.http;

import android.os.Environment;

import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.Like;
import com.zhangteng.xim.bmob.entity.Photo;
import com.zhangteng.xim.bmob.entity.Remark;
import com.zhangteng.xim.bmob.entity.Story;
import com.zhangteng.xim.utils.StringUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteBatchListener;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by swing on 2018/6/4.
 */
public class DataApi {
    private static DataApi instance;

    private DataApi() {
    }

    public static DataApi getInstance() {
        if (instance == null) {
            synchronized (DataApi.class) {
                instance = new DataApi();
            }
        }
        return instance;
    }


    /**
     * 查询数据
     *
     * @param bmobCallBack
     */
    public void queryStory(Story data, final BmobCallBack<List<Story>> bmobCallBack) {
        BmobQuery<Story> query = new BmobQuery<>();
        query.addWhereEqualTo("user", ((Story) data).getUser());
        query.setLimit(1);
        if (StringUtils.isNotEmpty(data.getCreatedAt())) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = sdf1.parse(data.getCreatedAt());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            query.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
        }
        query.order("-updatedAt");
        query.findObjects(new FindListener<Story>() {
            @Override
            public void done(List<Story> list, BmobException e) {
                bmobCallBack.onResponse(list, e);
            }
        });
    }

    public void queryRemark(Remark data, final BmobCallBack<List<Remark>> bmobCallBack) {
        BmobQuery<Remark> query = new BmobQuery<>();
        query.addWhereEqualTo("story", ((Remark) data).getStory());
        query.order("-updatedAt");
        query.findObjects(new FindListener<Remark>() {
            @Override
            public void done(List<Remark> list, BmobException e) {
                bmobCallBack.onResponse(list, e);
            }
        });
    }

    public void queryLike(Like data, final BmobCallBack<List<Like>> bmobCallBack) {
        BmobQuery<Like> query = new BmobQuery<>();
        query.addWhereEqualTo("story", ((Like) data).getStory());
        query.order("-updatedAt");
        query.findObjects(new FindListener<Like>() {
            @Override
            public void done(List<Like> list, BmobException e) {
                bmobCallBack.onResponse(list, e);
            }
        });
    }

    public void queryPhoto(Photo data, final BmobCallBack<List<Photo>> bmobCallBack) {
        BmobQuery<Photo> query = new BmobQuery<>();
        query.addWhereEqualTo("user", data.getUser());
        query.order("-updatedAt");
        query.findObjects(new FindListener<Photo>() {
            @Override
            public void done(List<Photo> list, BmobException e) {
                bmobCallBack.onResponse(list, e);
            }
        });
    }


    /**
     * 删除数据
     *
     * @param data
     * @param bmobCallBack
     */
    public <T> void delete(T data, final BmobCallBack bmobCallBack) {
        if (data instanceof Story) {
            ((Story) data).delete(((Story) data).getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    bmobCallBack.onResponse(null, e);
                }
            });
        } else if (data instanceof Remark) {
            ((Remark) data).delete(((Remark) data).getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    bmobCallBack.onResponse(null, e);
                }
            });
        } else if (data instanceof Like) {
            ((Like) data).delete(((Like) data).getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    bmobCallBack.onResponse(null, e);
                }
            });
        } else if (data instanceof Photo) {
            ((Photo) data).delete(((Photo) data).getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    bmobCallBack.onResponse(null, e);
                }
            });
        }
    }

    /**
     * 添加数据
     *
     * @param data
     * @param bmobCallBack
     */
    public <T> void add(T data, final BmobCallBack<String> bmobCallBack) {
        if (data instanceof Story) {
            ((Story) data).save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    bmobCallBack.onResponse(s, e);
                }
            });
        } else if (data instanceof Remark) {
            ((Remark) data).save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    bmobCallBack.onResponse(s, e);
                }
            });
        } else if (data instanceof Like) {
            ((Like) data).save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    bmobCallBack.onResponse(s, e);
                }
            });
        } else if (data instanceof Photo) {
            ((Photo) data).save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    bmobCallBack.onResponse(s, e);
                }
            });
        }
    }

    /**
     * 修改数据
     *
     * @param data
     * @param bmobCallBack
     */
    public <T> void update(T data, final BmobCallBack<String> bmobCallBack) {
        if (data instanceof Story) {
            ((Story) data).update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    bmobCallBack.onResponse(null, e);
                }
            });
        } else if (data instanceof Remark) {
            ((Remark) data).update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    bmobCallBack.onResponse(null, e);
                }
            });
        } else if (data instanceof Like) {
            ((Like) data).update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    bmobCallBack.onResponse(null, e);
                }
            });
        } else if (data instanceof Photo) {
            ((Photo) data).update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    bmobCallBack.onResponse(null, e);
                }
            });
        }
    }

    /**
     * 上传文件
     */
    public void uploadFile(String path, final BmobCallBack<String> bmobCallBack) {
        BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                bmobCallBack.onResponse(null, e);
            }

            @Override
            public void onProgress(Integer value) {
                bmobCallBack.onProgress(value);
            }
        });
    }

    /**
     * 批量上传
     */
    public void uploadBatch(final String[] filePaths, final BmobCallBack<List<String>> bmobCallBack) {
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                    //do something
                    bmobCallBack.onResponse(urls, null);
                } else {
                    bmobCallBack.onResponse(urls, new BmobException("未完全上传"));
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                bmobCallBack.onResponse(null, new BmobException(statuscode, errormsg));
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
                bmobCallBack.onProgress(totalPercent);
            }
        });
    }

    /**
     * 下载文件
     */
    private void downloadFile(BmobFile file) {
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        File saveFile = new File(Environment.getExternalStorageDirectory(), file.getFilename());
        file.download(saveFile, new DownloadFileListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void done(String savePath, BmobException e) {
                if (e == null) {

                } else {

                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {

            }

        });
    }

    /**
     * 删除文件
     */
    public void deleteFile(String url) {
        BmobFile file = new BmobFile();
        file.setUrl(url);//此url是上传文件成功之后通过bmobFile.getUrl()方法获取的。
        file.delete(new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {

                } else {

                }
            }
        });

    }

    /**
     * 批量删除
     */
    public void deleteBatch(String[] urls) {
        //此url必须是上传文件成功之后通过bmobFile.getUrl()方法获取的。
        BmobFile.deleteBatch(urls, new DeleteBatchListener() {

            @Override
            public void done(String[] failUrls, BmobException e) {
                if (e == null) {

                } else {
                    if (failUrls != null) {

                    } else {

                    }
                }
            }
        });

    }
}
