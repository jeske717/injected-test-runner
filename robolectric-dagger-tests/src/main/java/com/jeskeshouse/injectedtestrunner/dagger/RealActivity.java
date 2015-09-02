package com.jeskeshouse.injectedtestrunner.dagger;

import android.app.Activity;
import android.os.Bundle;

import com.jeskeshouse.daggermodules.Modules;

import javax.inject.Inject;

public class RealActivity extends Activity {

    @Inject
    public InjectableThing injectableThing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Modules.install(new ProductionModule());
        Modules.asObjectGraph().inject(this);
    }
}
