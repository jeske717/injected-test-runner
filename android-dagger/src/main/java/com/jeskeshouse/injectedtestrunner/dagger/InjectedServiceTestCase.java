package com.jeskeshouse.injectedtestrunner.dagger;

import android.app.Service;
import android.test.ServiceTestCase;

public class InjectedServiceTestCase<T extends Service> extends ServiceTestCase<T> {

    public InjectedServiceTestCase(Class<T> serviceClass) {
        super(serviceClass);
    }


}
