package com.jeskeshouse.injectedtestrunner.dagger;

import com.jeskeshouse.injectedtestrunner.dagger.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.dagger.injectables.InjectableThing;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module(injects = {ServiceThatDoesWork.class, ActivityThatDoesWork.class})
public class ProductionModule {

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