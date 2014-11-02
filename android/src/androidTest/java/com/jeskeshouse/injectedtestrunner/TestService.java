package com.jeskeshouse.injectedtestrunner;

import android.content.Intent;
import android.os.IBinder;

import javax.inject.Inject;

import roboguice.service.RoboService;

public class TestService extends RoboService {

    @Inject
    public InjectableThing injectableThing;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
