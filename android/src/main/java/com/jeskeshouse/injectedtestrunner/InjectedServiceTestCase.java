package com.jeskeshouse.injectedtestrunner;

import android.app.Service;
import android.test.ServiceTestCase;

public abstract class InjectedServiceTestCase<T extends Service> extends ServiceTestCase<T> {

    public InjectedServiceTestCase(Class<T> serviceClass) {
        super(serviceClass);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        GuiceInitializer.initialize(this, new ServiceInitializationStrategy(this));
    }
}
