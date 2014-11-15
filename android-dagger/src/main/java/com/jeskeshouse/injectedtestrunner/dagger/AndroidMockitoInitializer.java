package com.jeskeshouse.injectedtestrunner.dagger;

import android.content.Context;

import org.mockito.MockitoAnnotations;

class AndroidMockitoInitializer {

    public static void setupMockito(Object test, Context context) {
        System.setProperty("dexmaker.dexcache", context.getCacheDir().getAbsolutePath());
        MockitoAnnotations.initMocks(test);
    }

}
