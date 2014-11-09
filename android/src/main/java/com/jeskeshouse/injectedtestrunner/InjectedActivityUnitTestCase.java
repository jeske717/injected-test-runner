package com.jeskeshouse.injectedtestrunner;

import android.app.Activity;
import android.test.ActivityUnitTestCase;

import roboguice.RoboGuice;

public class InjectedActivityUnitTestCase<T extends Activity> extends ActivityUnitTestCase<T> {

    public InjectedActivityUnitTestCase(Class<T> activityClass) {
        super(activityClass);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        GuiceInitializer.initialize(this, new ActivityInitializationStrategy(this));
    }

    @Override
    public void tearDown() throws Exception {
        RoboGuice.Util.reset();
        super.tearDown();
    }
}
