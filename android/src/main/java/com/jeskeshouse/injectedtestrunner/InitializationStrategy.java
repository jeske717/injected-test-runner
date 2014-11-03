package com.jeskeshouse.injectedtestrunner;

import android.app.Application;
import android.content.Context;

interface InitializationStrategy {
    Context getUsableContext();

    Application getApplication();
}
