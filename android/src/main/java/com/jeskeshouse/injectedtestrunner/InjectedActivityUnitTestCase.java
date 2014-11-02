package com.jeskeshouse.injectedtestrunner;

import android.app.Activity;
import android.app.Application;
import android.test.ActivityUnitTestCase;

import com.google.inject.Module;
import com.google.inject.util.Modules;

import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import roboguice.RoboGuice;

public class InjectedActivityUnitTestCase<T extends Activity> extends ActivityUnitTestCase<T> {

    public InjectedActivityUnitTestCase(Class<T> activityClass) {
        super(activityClass);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getAbsolutePath());
        MockitoAnnotations.initMocks(this);

        Application application = (Application) getInstrumentation().getTargetContext().getApplicationContext();

        List<Dependency> dependencies = new ArrayList<Dependency>();
        for(DependencyProvider provider : Arrays.asList(new MockitoFieldDependencyProvider(), new ProvidesMethodDependencyProvider())) {
            dependencies.addAll(provider.getDependencies(this));
        }

        List<Module> modules = ModuleFactory.instantiateModulesForTest(this, getInstrumentation().getTargetContext(), dependencies);
        RoboGuice.overrideApplicationInjector(application,
                Modules.override(RoboGuice.newDefaultRoboModule(application)).with(modules.toArray(new Module[modules.size()])));
    }
}
