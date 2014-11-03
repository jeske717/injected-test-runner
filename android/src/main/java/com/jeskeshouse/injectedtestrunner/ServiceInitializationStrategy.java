package com.jeskeshouse.injectedtestrunner;

import android.app.Application;
import android.content.Context;

class ServiceInitializationStrategy implements InitializationStrategy {

    private final InjectedServiceTestCase<?> testCase;

    public ServiceInitializationStrategy(InjectedServiceTestCase<?> testCase) {
        this.testCase = testCase;
    }

    @Override
    public Context getUsableContext() {
        return testCase.getSystemContext();
    }

    @Override
    public Application getApplication() {
        return (Application) testCase.getSystemContext().getApplicationContext();
    }
}
