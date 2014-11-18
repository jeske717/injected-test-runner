package com.jeskeshouse.injectedtestrunner.components;

import com.jeskeshouse.injectedtestrunner.injectables.AnInterface;
import com.jeskeshouse.injectedtestrunner.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.injectables.InjectableThing;

import javax.inject.Inject;
import javax.inject.Named;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;

public class TestActivity extends RoboActivity {

    public static final String EXTRA_NAME = "Extra";

    @InjectExtra(value = EXTRA_NAME, optional = true)
    public String injectedExtra;

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

    @Inject
    public AnInterface anInterface;
}
