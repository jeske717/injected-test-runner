package com.jeskeshouse.injectedtestrunner.dagger;

import android.app.Service;
import android.test.ServiceTestCase;

public class InjectedServiceTestCase<T extends Service> extends ServiceTestCase<T> {

    public InjectedServiceTestCase(Class<T> serviceClass) {
        super(serviceClass);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        AndroidMockitoInitializer.setupMockito(this, getSystemContext());
        DaggerTestInitializer.addMockModuleToObjectGraph(this);
    }

    @Override
    public void tearDown() throws Exception {
        DaggerTestInitializer.resetModules();
        super.tearDown();
    }

}
