package com.jeskeshouse.injectedtestrunner;

import roboguice.activity.RoboActivity;

import javax.inject.Inject;
import javax.inject.Named;

public class ActivityWithDependencies extends RoboActivity {

    @Inject
    public InjectableThing dependency;

    @Inject
    @Named("objectOne")
    public InjectableThing objectOne;

    @Inject
    @Named("objectTwo")
    public InjectableThing objectTwo;

    @Inject
    @Named("providedByTest1")
    public AnotherInjectableThing anotherInjectableThing;

    @Inject
    @Named("providedByTest2")
    public AnotherInjectableThing anotherInjectableThingWithName;
}
