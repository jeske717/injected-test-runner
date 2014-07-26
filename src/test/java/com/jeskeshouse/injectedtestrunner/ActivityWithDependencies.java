package com.jeskeshouse.injectedtestrunner;

import javax.inject.Inject;
import javax.inject.Named;

import roboguice.activity.RoboActivity;

public class ActivityWithDependencies extends RoboActivity {

    @Inject
    public InjectableThing dependency;

    @Inject
    @Named("objectOne")
    public InjectableThing objectOne;

    @Inject
    @Named("objectTwo")
    public InjectableThing objectTwo;

}
