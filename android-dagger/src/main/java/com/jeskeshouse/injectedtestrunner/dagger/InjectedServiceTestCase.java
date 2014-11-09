package com.jeskeshouse.injectedtestrunner.dagger;

import android.app.Service;
import android.content.Intent;
import android.test.ServiceTestCase;

import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import dagger.ObjectGraph;

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

    @Override
    protected void startService(Intent intent) {
        super.startService(intent);
        List<?> customModules = getCustomModules();
        ObjectGraph.create(customModules.toArray(new Object[customModules.size()])).inject(getService());
    }

    protected List<?> getCustomModules() {
        return Collections.emptyList();
    }
}
