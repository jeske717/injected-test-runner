package com.jeskeshouse.injectedtestrunner.dagger;

import android.app.Activity;
import android.os.Bundle;

import com.jeskeshouse.injectedtestrunner.dagger.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.dagger.injectables.InjectableThing;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.ObjectGraph;

public class ActivityThatDoesWork extends Activity {

    @Inject
    public InjectableThing injectableThing;

    @Inject
    @Named("named")
    public AnotherInjectableThing namedThing;

    @Inject
    @Named("provided")
    public AnotherInjectableThing providedThing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ObjectGraph.create(new ProductionModule()).inject(this);
    }
}
