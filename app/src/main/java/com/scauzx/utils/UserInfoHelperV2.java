/*
package com.scauzx.utils;

import android.util.SparseArray;
import com.google.gson.reflect.TypeToken;
import com.yy.iheima.outlets.ConfigLet;
import com.yy.iheima.outlets.YYServiceUnboundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import sg.bigo.common.function.Consumer;
import sg.bigo.core.apicache.ApiCacheEntry;
import sg.bigo.core.apicache.ApiCacheHelper;
import sg.bigo.live.aidl.UserInfoStruct;

*/
/**
 * Created by Administrator on 2017/11/28.
 *//*


public class UserInfoHelperV2 {

    public static final int MAX_CACHE_SIZE = 10;

    private static String KEY_CACHE = "key_search_user_at";

    public static final String TAG = UserInfoHelperV2.class.getSimpleName();



    public interface UserInfoListener {
        void onSuccess(List<UserInfoStruct> userInfoStructs);
        void onFail();
    }


    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }


    public static void loadCache(final UserInfoListener listener, ApiCacheHelper.Validator validator) {
        int myUid = 0;
        try {
            myUid = ConfigLet.myUid();
        } catch (YYServiceUnboundException e) {
            e.printStackTrace();
        }
        if (myUid != 0) {
            loadCache(listener, validator, myUid);
        } else {

        }
    }

    public static RecentAtBean getItem(List<RecentAtBean> lists, int uid) {
        if (isEmpty(lists) || uid == 0) {
            return null;
        }
        RecentAtBean result = null;
        for (RecentAtBean bean : lists) {
            if (bean != null && bean.uid == uid) {
                result = bean;
                break;
            }
        }
        return result;
    }

    public static void removeItem(List<RecentAtBean> lists, int uid) {
        if (isEmpty(lists) || uid == 0) {
            return;
        }
        Iterator<RecentAtBean> iterator = lists.iterator();
        while (iterator != null && iterator.hasNext()) {
            RecentAtBean bean = iterator.next();
            if (bean != null && bean.uid == uid) {
                iterator.remove();
            }
        }
    }




    public static void loadCache(final UserInfoListener listener, ApiCacheHelper.Validator validator, final int uid) {
        Type type = new TypeToken<List<RecentAtBean>>() {
        }.getType();
        ApiCacheHelper.getApiCache(KEY_CACHE, validator, type, new Consumer<List<RecentAtBean>>() {
            @Override
            public void accept(List<RecentAtBean> items) {

                if (listener != null) {
                    if (items != null && items.size() != 0) {
                        RecentAtBean bean = getItem(items, uid);
                        if (bean!= null) {
                            listener.onSuccess(bean.userInfoStructs);
                        } else {
                            listener.onFail();
                        }
                    } else {
                        listener.onSuccess(null);
                    }
                }
                if (items != null) {
                    items.clear();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                throwable.printStackTrace();
                // 清理异常的缓存
                ApiCacheHelper.removeCache(KEY_CACHE);
                if (listener != null) {
                    listener.onFail();
                }
            }
        });
    }

    public static boolean isEmpty(SparseArray sparseArray) {
        return sparseArray == null || sparseArray.size() == 0;

    }


    public static void saveCache(final List<UserInfoStruct> userInfoStructs){
        int myUid = 0;
        try {
            myUid = ConfigLet.myUid();
        } catch (YYServiceUnboundException e) {
            e.printStackTrace();
        }
        if (myUid != 0) {
            saveCache(userInfoStructs, myUid);
        }
    }



    */
/**
     * 存储最近@过的数据
     *
     *//*

    public static void saveCache(final List<UserInfoStruct> userInfoStructs, final int myUid) {
        resetUserInfo(userInfoStructs);
        Type type = new TypeToken<List<RecentAtBean>>() {
        }.getType();
        ApiCacheHelper.getApiCache(KEY_CACHE, getDefaultVaildator(), type, new Consumer<List<RecentAtBean>>() {
            @Override
            public void accept(List<RecentAtBean> items) {
                if (items == null) {
                    items = new ArrayList<>();
                }

                RecentAtBean bean = getItem(items, myUid);
                if (bean == null) {
                    if (!isEmpty(userInfoStructs)) {
                        bean = new RecentAtBean(myUid,userInfoStructs);
                        items.add(bean);
                    }
                } else {
                    if (isEmpty(userInfoStructs)) {
                        removeItem(items, myUid);
                    } else {
                        bean.userInfoStructs = userInfoStructs;
                    }
                }

                if (bean != null && !isEmpty(bean.userInfoStructs)) {
                    bean.userInfoStructs = bean.userInfoStructs.subList(0, Math.min(MAX_CACHE_SIZE, bean.userInfoStructs.size()));
                }

                ApiCacheHelper.saveApiCache(KEY_CACHE, items);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                throwable.printStackTrace();
                // 清理异常的缓存
                ApiCacheHelper.removeCache(KEY_CACHE);

            }
        });
    }





    public static void removeInvalidAtUser(final List<Integer> uids) {
        int myUid = 0;
        try {
            myUid = ConfigLet.myUid();
        } catch (YYServiceUnboundException e) {
            e.printStackTrace();
        }
        if (myUid != 0) {
            removeInvalidAtUser(uids, myUid);
        }
    }



    */
/**
     * 移除无效的数据
     * @param uids
     *//*

    public static void removeInvalidAtUser(final List<Integer> uids, final int myUid) {
        if (isEmpty(uids) || myUid == 0) {
            return;
        }

        Type type = new TypeToken<List<RecentAtBean>>() {
        }.getType();
        ApiCacheHelper.getApiCache(KEY_CACHE, getDefaultVaildator(), type, new Consumer<List<RecentAtBean>>() {
            @Override
            public void accept(List<RecentAtBean> items) {
                if (items == null) {
                    items = new ArrayList<>();
                }
                RecentAtBean bean = getItem(items, myUid);
                boolean needRestore = false;
                if (bean != null && !isEmpty(bean.userInfoStructs)) {
                    Iterator<UserInfoStruct> iter = bean.userInfoStructs.iterator();
                    while (iter != null && iter.hasNext()) {
                        UserInfoStruct user = iter.next();
                        if (user != null) {
                            for (Integer uid : uids) {
                                if (user.uid == uid) {
                                    //重复的数据，排除掉
                                    needRestore = true;
                                    iter.remove();
                                }
                            }
                        }
                    }
                    if (needRestore) {
                        ApiCacheHelper.saveApiCache(KEY_CACHE, items);
                    }
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                throwable.printStackTrace();
                // 清理异常的缓存
                ApiCacheHelper.removeCache(KEY_CACHE);

            }
        });

    }


    public static ApiCacheHelper.Validator getDefaultVaildator() {
        return new ApiCacheHelper.Validator() {
            @Override
            public boolean validate(ApiCacheEntry entry) {
                return true;
            }
        };
    }


    */
/**
     *
     *//*

    public static void addUsers(final List<UserInfoStruct> users) {
        int myUid = 0;
        try {
            myUid = ConfigLet.myUid();
        } catch (YYServiceUnboundException e) {
            e.printStackTrace();
        }
        if (myUid != 0) {
            addUsers(users, myUid);
        }
    }


    public static void resetUserInfo(List<UserInfoStruct> users) {
        if (isEmpty(users)) {
            return;
        }
        for (UserInfoStruct user : users) {
            if (user != null) {
                user.lineTitle = "";
            }
        }
    }



    */
/**
     * 插入新数据
     *//*

    public static void addUsers(final List<UserInfoStruct> users, final int myUid) {
        if (isEmpty(users)) {
            return;
        }
        resetUserInfo(users);

        Type type = new TypeToken<List<RecentAtBean>>() {
        }.getType();
        ApiCacheHelper.getApiCache(KEY_CACHE, getDefaultVaildator(), type, new Consumer<List<RecentAtBean>>() {
            @Override
            public void accept(List<RecentAtBean> items) {
                if (items == null) {
                    items = new ArrayList<>();
                }
                RecentAtBean bean = getItem(items, myUid);
                if (bean == null) {
                    Collections.reverse(users);
                    bean = new RecentAtBean(myUid, users);
                    items.add(bean);
                } else {
                    if (isEmpty(bean.userInfoStructs)) {
                        Collections.reverse(users);
                        bean.userInfoStructs = users;
                    } else {

                        HashSet<Integer> itemUids = new HashSet<>();

                        for (UserInfoStruct item : users) {
                            if (item != null) {
                                itemUids.add(item.uid);
                            }
                        }

                        //本地存储结果先去除这次新加进来的旧有数据
                        Iterator<UserInfoStruct> iterator = bean.userInfoStructs.iterator();
                        while(iterator != null && iterator.hasNext()) {
                            UserInfoStruct userInfoStruct = iterator.next();
                            if (userInfoStruct != null && itemUids.contains(userInfoStruct.uid)) {
                                iterator.remove();
                            }
                        }
                        for (UserInfoStruct userInfo : users) {
                            if (userInfo != null) {
                                bean.userInfoStructs.add(0,userInfo);
                            }
                        }
                    }
                }
                if (bean !=null && !isEmpty(bean.userInfoStructs)) {
                    bean.userInfoStructs = bean.userInfoStructs.subList(0, Math.min(MAX_CACHE_SIZE, bean.userInfoStructs.size()));
                }
                ApiCacheHelper.saveApiCache(KEY_CACHE, items);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                throwable.printStackTrace();
                // 清理异常的缓存
                ApiCacheHelper.removeCache(KEY_CACHE);

            }
        });


    }

    public static boolean isCollectionEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

}
*/
