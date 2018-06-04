package com.zhangteng.xim.bmob.http;

import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.Like;
import com.zhangteng.xim.bmob.entity.Photo;
import com.zhangteng.xim.bmob.entity.Remark;
import com.zhangteng.xim.bmob.entity.Story;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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
    public <T> void query(T data, final BmobCallBack<List<T>> bmobCallBack) {
        BmobQuery<T> query = new BmobQuery<>();
        if (data instanceof Story) {
            query.addWhereEqualTo("user", ((Story) data).getUser());
        } else if (data instanceof Remark) {
            query.addWhereEqualTo("story", ((Remark) data).getStory());
        } else if (data instanceof Like) {
            query.addWhereEqualTo("story", ((Like) data).getStory());
        } else if (data instanceof Photo) {
            query.addWhereEqualTo("user", ((Photo) data).getUser());
        }
        query.order("-updatedAt");
        query.findObjects(new FindListener<T>() {
            @Override
            public void done(List<T> list, BmobException e) {
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
}
