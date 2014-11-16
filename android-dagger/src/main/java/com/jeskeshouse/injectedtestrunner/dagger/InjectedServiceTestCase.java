package com.jeskeshouse.injectedtestrunner.dagger;

import android.app.Service;
import android.test.ServiceTestCase;

import java.util.Collections;
import java.util.List;

public class InjectedServiceTestCase<T extends Service> extends ServiceTestCase<T> {

    public InjectedServiceTestCase(Class<T> serviceClass) {
        super(serviceClass);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        AndroidMockitoInitializer.setupMockito(this, getSystemContext());
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
