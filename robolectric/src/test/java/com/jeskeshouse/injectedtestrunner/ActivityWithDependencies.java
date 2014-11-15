package com.jeskeshouse.injectedtestrunner;

import javax.inject.Inject;
import javax.inject.Named;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;

public class ActivityWithDependencies extends RoboActivity {

    public static final String EXTRA_NAME = "Extra";

    @InjectExtra(value = EXTRA_NAME, optional = true)
    public String injectedExtra;

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

    @Inject
    public GenericThing<String> genericThing;

    @Inject
    public GenericThing<InjectableThing> providedGenericThing;
}
