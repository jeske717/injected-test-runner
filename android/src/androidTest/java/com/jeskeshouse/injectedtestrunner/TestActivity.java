package com.jeskeshouse.injectedtestrunner;

import com.jeskeshouse.injectedtestrunner.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.injectables.InjectableThing;

import javax.inject.Inject;
import javax.inject.Named;

import roboguice.activity.RoboActivity;

public class TestActivity extends RoboActivity {

    @Inject
    public InjectableThing injectableThing;

    @Inject
    @Named("named")
    public AnotherInjectableThing namedThing;
}
