package com.jeskeshouse.injectedtestrunner.components;

import com.jeskeshouse.injectedtestrunner.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.injectables.InjectableThing;

import javax.inject.Inject;
import javax.inject.Named;

import roboguice.activity.RoboActivity;

public class TestSuperclassActivity extends RoboActivity {

    @Inject
    @Named("injectableThingFromSuperclass")
    public InjectableThing injectableThingFromSuperclass;

    @Inject
    @Named("injectableThingFromSubclass")
    public InjectableThing injectableThingFromSubclass;

    @Inject
    public AnotherInjectableThing anotherInjectableThing;
}
