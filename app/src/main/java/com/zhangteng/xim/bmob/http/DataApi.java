package com.zhangteng.xim.bmob.http;

import android.os.Environment;
import android.support.annotation.Nullable;

import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.Friend;
import com.zhangteng.xim.bmob.entity.Like;
import com.zhangteng.xim.bmob.entity.Photo;
import com.zhangteng.xim.bmob.entity.Remark;
import com.zhangteng.xim.bmob.entity.Story;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.db.DBManager;
import com.zhangteng.xim.db.bean.LocalUser;
import com.zhangteng.xim.utils.StringUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteBatchListener;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
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
     * 查询动态默认取一百条
     * 可以设定从第m+1个元素开始，例如从第 11 个元素（包含）开始往后取 10 个：select * from GameScore limit 10,10
     */
    public void queryStorys(User selfUser, final int start, final int limit, final BmobCallBack<List<Story>> bmobCallBack) {
        final List<String> users = new ArrayList<>();
        BmobQuery<Friend> userBmobQuery = new BmobQuery<>();
        String userBql = "select * from Friend where user = pointer('_User', '" + selfUser.getObjectId() + "')";
        userBmobQuery.doSQLQuery(userBql, new SQLQueryListener<Friend>() {
            @Override
            public void done(BmobQueryResult<Friend> bmobQueryResult, BmobException e) {
                if (e == null) {
                    List<Friend> list = (List<Friend>) bmobQueryResult.getResults();
                    for (Friend friend : list) {
                        users.add(friend.getFriendUser().getObjectId());
                    }
                    if (!users.isEmpty()) {
                        BmobQuery<Story> query = new BmobQuery<>();
                        StringBuffer buffer = new StringBuffer();
                        buffer.append("select * from Story ")
                                .append("limit ")
                                .append(start)
                                .append(",")
                                .append(limit)
                                .append(" where user in (");
                        for (int i = 0; i < users.size(); i++) {
                            buffer.append("'")
                                    .append(users.get(i))
                                    .append("'");
                            if (i != users.size() - 1) {
                                buffer.append(",");
                            }
                        }
                        buffer.append(")");
                        query.doSQLQuery(buffer.toString(), new SQLQueryListener<Story>() {
                            @Override
                            public void done(BmobQueryResult<Story> bmobQueryResult, BmobException e) {
                                if (e == null) {
                                    List<Story> list = (List<Story>) bmobQueryResult.getResults();
                                    bmobCallBack.onResponse(list, null);
                                } else {
                                    bmobCallBack.onResponse(null, e);
                                }
                            }
                        });
                    } else {
                        bmobCallBack.onResponse(null, new BmobException());
                    }
                } else {
                    bmobCallBack.onResponse(null, e);
                }
            }
        });
    }

    /**
     * 查询前三条有图片的动态
     */
    public void queryStorys(final User user, final BmobCallBack<List<Story>> bmobCallBack) {
        BmobQuery<Story> query = new BmobQuery<>();
        query.addWhereEqualTo("user", new BmobPointer(user));
        query.setLimit(3);
        query.addWhereExists("iconPaths");
        query.order("-updatedAt");
        query.findObjects(new FindListener<Story>() {
            @Override
            public void done(List<Story> list, BmobException e) {
                bmobCallBack.onResponse(list, e);
            }
        });
    }

    /**
     * 按照时间查询
     */
    public void queryStorys(String start, String end, final BmobCallBack<List<Story>> bmobCallBack) {
        final List<LocalUser> list = DBManager.instance(DBManager.USERNAME).queryUsers(0, Integer.MAX_VALUE);
        final List<Story> stories = new ArrayList<>();
        for (final LocalUser localUser : list) {
            Story story = new Story();
            story.setUser(User.getUser(localUser));
            queryStory(start, end, story, new BmobCallBack<List<Story>>(bmobCallBack.getContext(), false) {
                @Override
                public void onSuccess(@Nullable List<Story> bmobObject) {
                    if (bmobCallBack != null) {
                        stories.addAll(bmobObject);
                    }
                    if (localUser == list.get(list.size() - 1)) {
                        bmobCallBack.onResponse(stories, null);
                    }
                }

                @Override
                public void onFailure(BmobException bmobException) {
                    super.onFailure(bmobException);
                    if (localUser == list.get(list.size() - 1)) {
                        bmobCallBack.onResponse(stories, bmobException);
                    }
                }
            });
        }
    }

    /**
     * 查询数据
     *
     * @param bmobCallBack
     */
    public void queryStory(Story data, int limit, final BmobCallBack<List<Story>> bmobCallBack) {
        BmobQuery<Story> query = new BmobQuery<>();
        query.addWhereEqualTo("user", new BmobPointer(data.getUser()));
        query.setLimit(limit);
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

    public void queryStory(String start, String end, Story data, final BmobCallBack<List<Story>> bmobCallBack) {
        BmobQuery<Story> query = new BmobQuery<>();
        List<BmobQuery<Story>> and = new ArrayList<BmobQuery<Story>>();
        //大于00：00：00
        BmobQuery<Story> q1 = new BmobQuery<Story>();
//        String start = "2015-05-01 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q1.addWhereEqualTo("user", data.getUser());
        q1.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
        and.add(q1);
        //小于23：59：59
        BmobQuery<Story> q2 = new BmobQuery<Story>();
//        String end = "2015-05-01 23:59:59";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        try {
            date1 = sdf1.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q2.addWhereEqualTo("user", data.getUser());
        q2.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(date1));
        and.add(q2);
        //添加复合与查询
        query.and(and);
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
        query.addWhereEqualTo("story", new BmobPointer(data.getStory()));
        query.order("-updatedAt");
        query.include("user,story,remark");
        query.findObjects(new FindListener<Remark>() {
            @Override
            public void done(List<Remark> list, BmobException e) {
                bmobCallBack.onResponse(list, e);
            }
        });
    }

    public void queryRemark(String objectId, final BmobCallBack<Remark> bmobCallBack) {
        BmobQuery<Remark> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", objectId);
        query.order("-updatedAt");
        query.include("user,story,remark");
        query.findObjects(new FindListener<Remark>() {
            @Override
            public void done(List<Remark> list, BmobException e) {
                if (list.size() > 0)
                    bmobCallBack.onResponse(list.get(0), e);
                else {
                    bmobCallBack.onResponse(null, e);
                }
            }
        });
    }

    public void queryLike(Like data, final BmobCallBack<List<Like>> bmobCallBack) {
        BmobQuery<Like> query = new BmobQuery<>();
        query.addWhereEqualTo("story", new BmobPointer(data.getStory()));
        query.order("-updatedAt");
        query.include("user,story");
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
        query.include("user");
        query.findObjects(new FindListener<Photo>() {
            @Override
            public void done(List<Photo> list, BmobException e) {
                bmobCallBack.onResponse(list, e);
            }
        });
    }

    public void queryThemePhoto(User user, final BmobCallBack<Photo> bmobCallBack) {
        BmobQuery<Photo> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user);
        query.addWhereEqualTo("mark", "theme");
        query.order("-updatedAt");
        query.findObjects(new FindListener<Photo>() {
            @Override
            public void done(List<Photo> list, BmobException e) {
                bmobCallBack.onResponse(list.isEmpty() ? null : list.get(0), e);
            }
        });
    }


    public void queryCirclePhoto(User user, final BmobCallBack<Photo> bmobCallBack) {
        BmobQuery<Photo> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user);
        query.addWhereEqualTo("mark", "circle");
        query.order("-updatedAt");
        query.findObjects(new FindListener<Photo>() {
            @Override
            public void done(List<Photo> list, BmobException e) {
                bmobCallBack.onResponse(list.isEmpty() ? null : list.get(0), e);
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
    public <T> void add(final T data, final BmobCallBack<String> bmobCallBack) {
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
            if (((Photo) data).getMark().equals("circle")) {
                queryCirclePhoto(((Photo) data).getUser(), new BmobCallBack<Photo>(bmobCallBack.getContext(), false) {
                    @Override
                    public void onSuccess(@Nullable Photo bmobObject) {
                        if (bmobObject == null) {
                            ((Photo) data).save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    bmobCallBack.onResponse(s, e);
                                }
                            });
                        } else {
                            ((Photo) data).setObjectId(bmobObject.getObjectId());
                            ((Photo) data).update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    bmobCallBack.onResponse(((Photo) data).getObjectId(), e);
                                }
                            });
                        }
                    }
                });
            } else if (((Photo) data).getMark().equals("theme")) {
                queryThemePhoto(((Photo) data).getUser(), new BmobCallBack<Photo>(bmobCallBack.getContext(), false) {
                    @Override
                    public void onSuccess(@Nullable Photo bmobObject) {
                        if (bmobObject == null) {
                            ((Photo) data).save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    bmobCallBack.onResponse(s, e);
                                }
                            });
                        } else {
                            ((Photo) data).setObjectId(bmobObject.getObjectId());
                            ((Photo) data).update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    bmobCallBack.onResponse(((Photo) data).getObjectId(), e);
                                }
                            });
                        }
                    }
                });
            } else {
                ((Photo) data).save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        bmobCallBack.onResponse(s, e);
                    }
                });
            }
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
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                bmobCallBack.onResponse(bmobFile.getFileUrl(), e);
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
