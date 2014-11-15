package com.jeskeshouse.injectedtestrunner.dagger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    }

    @Override
    protected T startActivity(Intent intent, Bundle savedInstanceState, Object lastNonConfigurationInstance) {
        T result = super.startActivity(intent, savedInstanceState, lastNonConfigurationInstance);
        DaggerTestInitializer.injectTestSubject(this, result, getCustomModules());
        return result;
    }

    protected List<?> getCustomModules() {
        return Collections.emptyList();
    }

}
