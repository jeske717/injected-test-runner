package com.jeskeshouse.injectedtestrunner.dagger;

import android.app.Service;
import android.test.ServiceTestCase;

import org.mockito.MockitoAnnotations;

public class InjectedServiceTestCase<T extends Service> extends ServiceTestCase<T> {

    public InjectedServiceTestCase(Class<T> serviceClass) {
        super(serviceClass);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getSystemContext().getCacheDir().getAbsolutePath());
        MockitoAnnotations.initMocks(this);
    }
}
