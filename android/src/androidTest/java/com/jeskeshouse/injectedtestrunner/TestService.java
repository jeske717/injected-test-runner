package com.jeskeshouse.injectedtestrunner;

import android.content.Intent;
import android.os.IBinder;

import com.jeskeshouse.injectedtestrunner.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.injectables.InjectableThing;

import javax.inject.Inject;
import javax.inject.Named;

import roboguice.service.RoboService;

public class TestService extends RoboService {

    @Inject
    public InjectableThing injectableThing;

    @Inject
    @Named("named")
    public AnotherInjectableThing namedThing;

    @Inject
    @Named("provided")
    public AnotherInjectableThing providedThing;

    @Inject
    @Named("providedByModule")
    public AnotherInjectableThing providedByModuleThing;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
