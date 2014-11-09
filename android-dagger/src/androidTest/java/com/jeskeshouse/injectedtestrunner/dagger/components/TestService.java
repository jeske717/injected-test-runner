package com.jeskeshouse.injectedtestrunner.dagger.components;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.jeskeshouse.injectedtestrunner.dagger.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.dagger.injectables.InjectableThing;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.ObjectGraph;

public class TestService extends Service {

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

    @Override
    public void onCreate() {
        super.onCreate();
        ObjectGraph.create().inject(this);
    }
}
