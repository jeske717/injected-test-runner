package com.jeskeshouse.injectedtestrunner.dagger;

import android.app.Activity;
import android.os.Bundle;

import com.jeskeshouse.daggermodules.Modules;
import com.jeskeshouse.injectedtestrunner.dagger.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.dagger.injectables.BasicServiceConnection;
import com.jeskeshouse.injectedtestrunner.dagger.injectables.EmployeeService;
import com.jeskeshouse.injectedtestrunner.dagger.injectables.GenericThing;
import com.jeskeshouse.injectedtestrunner.dagger.injectables.InjectableThing;

import javax.inject.Inject;
import javax.inject.Named;

public class ActivityThatDoesWork extends Activity {

    @Inject
    public InjectableThing injectableThing;

    @Inject
    public GenericThing<String> genericThing;

    @Inject
    public EmployeeService employeeService;

    @Inject
    public BasicServiceConnection basicServiceConnection;

    @Inject
    @Named("named")
    public AnotherInjectableThing namedThing;

    @Inject
    @Named("provided")
    public AnotherInjectableThing providedThing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Modules.install(new ProductionModule());
        Modules.asObjectGraph().inject(this);
    }
}
