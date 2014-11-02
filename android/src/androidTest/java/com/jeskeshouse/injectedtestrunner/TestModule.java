package com.jeskeshouse.injectedtestrunner;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jeskeshouse.injectedtestrunner.injectables.AnotherInjectableThing;

import javax.inject.Named;

public class TestModule extends AbstractModule {

    public static AnotherInjectableThing provided = new AnotherInjectableThing();

    @Override
    protected void configure() {
    }

    @Provides
    @Named("providedByModule")
    public AnotherInjectableThing anotherInjectableThing() {
        return provided;
    }
}
