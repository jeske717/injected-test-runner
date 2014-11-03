package com.jeskeshouse.injectedtestrunner;

import android.app.Application;
import android.content.Context;

class ActivityInitializationStrategy implements InitializationStrategy {

    private final InjectedActivityUnitTestCase<?> testCase;

    public ActivityInitializationStrategy(InjectedActivityUnitTestCase<?> testCase) {
        this.testCase = testCase;
    }

    @Override
    public Context getUsableContext() {
        return testCase.getInstrumentation().getTargetContext();
    }

    @Override
    public Application getApplication() {
        return (Application) testCase.getInstrumentation().getTargetContext().getApplicationContext();
    }
}
