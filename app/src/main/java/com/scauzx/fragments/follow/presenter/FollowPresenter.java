package com.scauzx.fragments.follow.presenter;

import android.support.annotation.Nullable;
import com.scauzx.base.presenter.BasePresenterImp;
import com.scauzx.fragments.follow.model.IFollowMode;
import com.scauzx.fragments.follow.model.FollowModeImp;
import com.scauzx.fragments.follow.view.IFollowView;
import java.util.List;

/**
 * 使用mvp模式的例子:
 * m 代表model,负责处理数据来源
 * v 代表view,负责处理视图交互，可以认为是view, activity, fragment等
 * p 中间业务层，负责m和v的交互,v需要的数据从p中拿，p调用m的方法获取，成功/失败回调p的方法，p再回调v中的成功/失败方法,这就是整个mvp的流程
 * 其中p暴露给v,v暴露给p,p暴露给m, m暴露给p的方法，要通过接口的方式，不然v可以使用p的所有方法，m可以使用p的使用公开方法，业务，逻辑不分离
 * 这里举个例子,获取个人信息:view -> presenter : fetchPersonInfo() -> model :fetchPersonInfo() -> presenter : handleFetchPersonInfo() -> view ; handleFetchPersonInfo();
 * mvp中v持有p引用，p持有v,m引用，m持有p引用,m中获取数据方法一般在子线程，那么view销毁的时候,m还持有p的引用，在进行任务，导致p释放不了，p释放不了导致v无法释放，造成内存泄漏
 * 所以在view销毁的时候要清除p中对view的引用,同样，还得清除p对m的引用，m对p的引用 {@link #onDestroy()}
 * @author scauzx
 * @date 2017/11/15
 */

public class FollowPresenter extends BasePresenterImp<IFollowView,IFollowMode> implements IFollowPresenter{
    IFollowMode mFollowModeImp;

    public FollowPresenter(@Nullable IFollowView view) {
        super(view);
        mProxy = new FollowModeImp(this);
    }

    @Override
    public void fetchPersonInfo() {
        if (mFollowModeImp != null) {
            mFollowModeImp.fetchPersonInfo();
        }
    }

    @Override
    public void handleFetchPersonInfo(List<Object> info, boolean success) {
        //要判断mView不为空是因为,mView一般的UI层，而model那边请求数据一般都是在子线程处理，子线程可能还没结束，但是View已经销毁了，所以要判断mView是否已经被销毁
        if (mView != null) {
            mView.handleFetchPersonInfo(info, success);
        }
    }
}
