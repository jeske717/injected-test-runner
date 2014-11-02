package com.jeskeshouse.injectedtestrunner;

import javax.inject.Inject;

import roboguice.activity.RoboActivity;

public class TestActivity extends RoboActivity {

    @Inject
    public InjectableThing injectableThing;

}
