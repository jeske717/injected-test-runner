package com.jeskeshouse.injectedtestrunner.dagger;

import android.app.Activity;
import android.test.ActivityUnitTestCase;

import java.util.Collections;
import java.util.List;

public class InjectedActivityUnitTestCase<T extends Activity> extends ActivityUnitTestCase<T> {
    public InjectedActivityUnitTestCase(Class<T> activityClass) {
        super(activityClass);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        AndroidMockitoInitializer.setupMockito(this, getInstrumentation().getTargetContext());
        DaggerTestInitializer.addModulesToObjectGraph(this, getCustomModules());
    }

    @Override
    public void tearDown() throws Exception {
        DaggerTestInitializer.resetModules();
        super.tearDown();
    }

    protected List<?> getCustomModules() {
        return Collections.emptyList();
    }

}
