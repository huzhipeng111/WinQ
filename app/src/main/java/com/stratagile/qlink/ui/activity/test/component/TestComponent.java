package com.stratagile.qlink.ui.activity.test.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.test.TestActivity;
import com.stratagile.qlink.ui.activity.test.module.TestModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.test
 * @Description: The component for TestActivity
 * @date 2018/08/31 17:23:42
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = TestModule.class)
public interface TestComponent {
    TestActivity inject(TestActivity Activity);
}