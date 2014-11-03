package com.jeskeshouse.injectedtestrunner;

import android.app.Activity;
import android.test.ActivityUnitTestCase;

public class InjectedActivityUnitTestCase<T extends Activity> extends ActivityUnitTestCase<T> {

    public InjectedActivityUnitTestCase(Class<T> activityClass) {
        super(activityClass);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        GuiceInitializer.initialize(this, new ActivityInitializationStrategy(this));
    }
}
