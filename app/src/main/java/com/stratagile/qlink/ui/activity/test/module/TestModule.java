package com.stratagile.qlink.ui.activity.test.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.test.TestActivity;
import com.stratagile.qlink.ui.activity.test.contract.TestContract;
import com.stratagile.qlink.ui.activity.test.presenter.TestPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.test
 * @Description: The moduele of TestActivity, provide field for TestActivity
 * @date 2018/08/31 17:23:42
 */
@Module
public class TestModule {
    private final TestContract.View mView;


    public TestModule(TestContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public TestPresenter provideTestPresenter(HttpAPIWrapper httpAPIWrapper, TestActivity mActivity) {
        return new TestPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public TestActivity provideTestActivity() {
        return (TestActivity) mView;
    }
}