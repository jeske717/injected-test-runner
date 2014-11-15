package com.jeskeshouse.injectedtestrunner.dagger;

import android.app.Service;
import android.content.Intent;
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
    }

    @Override
    protected void startService(Intent intent) {
        super.startService(intent);
        DaggerTestInitializer.injectTestSubject(this, getService(), getCustomModules());
    }

    protected List<?> getCustomModules() {
        return Collections.emptyList();
    }

}
