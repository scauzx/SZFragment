/*
package com.scauzx.utils;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

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




public class UserInfoHelperV3 {

    public static final int MAX_CACHE_SIZE = 10;

    private static String KEY_CACHE = "key_search_user_at";
    public static final String TAG = UserInfoHelperV3.class.getSimpleName();
    private boolean mHasLoadUserIngo = false;
    public static UserInfoHelperV3 sInstance;
    public List<UserInfoStruct> mUserInfos;

    public interface UserInfoListener {
        void onSuccess(List<UserInfoStruct> userInfoStructs);
        void onFail();
    }

    public static UserInfoHelperV3 getInstance() {
        if (sInstance == null) {
            sInstance = new UserInfoHelperV3();
        }
        return sInstance;
    }

    public void loadCache(final UserInfoListener listener, ApiCacheHelper.Validator validator) {
        if (listener == null) {
            return;
        }
        if (mHasLoadUserIngo) {
            listener.onSuccess(mUserInfos);
        } else {
            Type type = new TypeToken<ArrayList<UserInfoStruct>>() {
            }.getType();
            ApiCacheHelper.getApiCache(KEY_CACHE, validator, type, new Consumer<ArrayList<UserInfoStruct>>() {
                @Override
                public void accept(ArrayList<UserInfoStruct> items) {
                    mHasLoadUserIngo = true;
                    if (listener != null) {
                        listener.onSuccess(items);
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) {
                    mHasLoadUserIngo = true;
                    throwable.printStackTrace();
                    // 清理异常的缓存
                    ApiCacheHelper.removeCache(KEY_CACHE);
                    if (listener != null) {
                        listener.onFail();
                    }
                }
            });
        }
    }



    */
/**
     * 存储最近@过的数据
     * @param items
     *//*

    public void saveCache(List<UserInfoStruct> items) {
        mHasLoadUserIngo = true;
        mUserInfos = items;
        if (isEmpty(mUserInfos)) {
            clearCache();
        }
        for (UserInfoStruct item :items) {
            item.lineTitle = "";
        }
        ApiCacheHelper.saveApiCache(KEY_CACHE, items.subList(0, Math.min(MAX_CACHE_SIZE, mUserInfos.size())));
    }



    */
/**
     * 清除缓存的@过的人的数据
     *//*

    public static void clearCache() {
        ApiCacheHelper.removeCache(KEY_CACHE);
    }


    */
/**
     * 删除指定用户
     * @param uids
     *//*

    public void deleteUserInfoByUids(final List<Integer> uids) {
        if (isEmpty(uids)) {
            return;
        }
        if (mHasLoadUserIngo) {
            if (isEmpty(mUserInfos)) {
                return;
            }
            removeElementByUids(mUserInfos, uids);
            saveCache(mUserInfos);
        } else {
            loadCache(new UserInfoListener() {
                @Override
                public void onSuccess(List<UserInfoStruct> items) {
                    Log.d(TAG, "removeInvalidAtUser items.size() = " + items.size() + " delete uid = " + uids.toString());
                    if (isEmpty(items)) {
                        return;
                    }
                    removeElementByUids(mUserInfos, uids);
                    saveCache(mUserInfos);
                }

                @Override
                public void onFail() {
                    Log.d(TAG, "removeInvalidAtUser onFail ");
                }
            }, getDefaultValidator());

        }
    }



    public void removeElementByUids(List<UserInfoStruct> userInfos, List<Integer> uids) {
        if (isEmpty(userInfos) || isEmpty(uids)) {
            return;
        }
        Iterator<UserInfoStruct> iter = userInfos.iterator();
        while (iter != null && iter.hasNext()) {
            UserInfoStruct user = iter.next();
            if (user != null) {
                for (Integer uid : uids) {
                    if (user.uid == uid) {
                        //重复的数据，排除掉
                        iter.remove();
                    }
                }
            }
        }

    }


    public static ApiCacheHelper.Validator getDefaultValidator() {
        return new ApiCacheHelper.Validator() {
            @Override
            public boolean validate(ApiCacheEntry entry) {
                return true;
            }
        };
    }


    */
/**
     * 增加新数据
     *//*

    public void addUserInfo(final List<UserInfoStruct> users) {
        if (isEmpty(users)) {
            return;
        }
        if (mHasLoadUserIngo) {
            addElementToUserInfo(users);
            saveCache(mUserInfos);
        } else {
            loadCache(new UserInfoListener() {
                @Override
                public void onSuccess(List<UserInfoStruct> items) {
                    addElementToUserInfo(users);
                    saveCache(mUserInfos);
                }

                @Override
                public void onFail() {
                    Log.d(TAG, "removeInvalidAtUser onFail ");
                    addElementToUserInfo(users);
                    saveCache(mUserInfos);
                }
            }, getDefaultValidator());
        }
    }



    public void addElementToUserInfo(List<UserInfoStruct> newUserInfo) {
        if (isEmpty(newUserInfo)) {
            return;
        }
        if (mUserInfos == null) {
            mUserInfos = new ArrayList<>();
        }
        if (isEmpty(mUserInfos)) {
            Collections.reverse(newUserInfo);
            mUserInfos = newUserInfo;
        } else {
            HashSet<Integer> itemUids = new HashSet<>();

            for (UserInfoStruct item : newUserInfo) {
                if (item != null) {
                    itemUids.add(item.uid);
                }
            }

            //本地存储结果先去除这次新加进来的旧有数据
            Iterator<UserInfoStruct> iterator = mUserInfos.iterator();
            while(iterator != null && iterator.hasNext()) {
                UserInfoStruct userInfoStruct = iterator.next();
                if (userInfoStruct != null && itemUids.contains(userInfoStruct.uid)) {
                    iterator.remove();
                }
            }
            for (UserInfoStruct userInfo : newUserInfo) {
                if (userInfo != null) {
                    mUserInfos.add(0,userInfo);
                }
            }
        }
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

}
*/
