package com.jeskeshouse.injectedtestrunner.dagger.components;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.jeskeshouse.injectedtestrunner.dagger.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.dagger.injectables.InjectableThing;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;

public class TestService extends Service {

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
        ObjectGraph.create(new TestServiceModule()).inject(this);
    }

    @Module(injects = TestService.class)
    public static class TestServiceModule {

        @Provides
        public InjectableThing injectableThing() {
            return new InjectableThing();
        }

        @Provides
        @Named("named")
        public AnotherInjectableThing namedThing() {
            return new AnotherInjectableThing();
        }

        @Provides
        @Named("provided")
        public AnotherInjectableThing providedThing() {
            return new AnotherInjectableThing();
        }

    }
}
