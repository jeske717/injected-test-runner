package com.jeskeshouse.injectedtestrunner.dagger;

import android.app.Activity;
import android.test.ActivityUnitTestCase;

public class InjectedActivityUnitTestCase<T extends Activity> extends ActivityUnitTestCase<T> {
    public InjectedActivityUnitTestCase(Class<T> activityClass) {
        super(activityClass);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        AndroidMockitoInitializer.setupMockito(this, getInstrumentation().getTargetContext());
        DaggerTestInitializer.addMockModuleToObjectGraph(this);
    }

    @Override
    public void tearDown() throws Exception {
        DaggerTestInitializer.resetModules();
        super.tearDown();
    }
}
