package com.jeskeshouse.injectedtestrunner.dagger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.jeskeshouse.injectedtestrunner.dagger.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.dagger.injectables.InjectableThing;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.ObjectGraph;

public class ServiceThatDoesWork extends Service {

    @Inject
    public InjectableThing injectableThing;

    @Inject
    @Named("named")
    public AnotherInjectableThing namedThing;

    @Inject
    @Named("provided")
    public AnotherInjectableThing providedThing;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ObjectGraph.create(new ProductionModule()).inject(this);
    }
}
