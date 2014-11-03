package com.jeskeshouse.injectedtestrunner;

import android.app.Application;
import android.content.Context;
import android.test.AndroidTestCase;

public class AndroidTestCaseInitializationStrategy implements InitializationStrategy {

    private final AndroidTestCase testCase;

    public AndroidTestCaseInitializationStrategy(AndroidTestCase testCase) {
        this.testCase = testCase;
    }

    @Override
    public Context getUsableContext() {
        return testCase.getContext();
    }

    @Override
    public Application getApplication() {
        return (Application) testCase.getContext().getApplicationContext();
    }
}
