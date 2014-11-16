package com.jeskeshouse.injectedtestrunner.dagger;

import android.app.Activity;
import android.os.Bundle;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class RealActivity extends Activity {

    @Inject
    public InjectableThing injectableThing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ObjectGraph.create(new ProductionModule()).inject(this);
    }
}
