package com.jeskeshouse.injectedtestrunner;

import android.app.Application;
import android.app.Service;
import android.test.ServiceTestCase;

import com.google.inject.Module;
import com.google.inject.util.Modules;

import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import roboguice.RoboGuice;

public class InjectedServiceUnitTestCase<T extends Service> extends ServiceTestCase<T> {

    public InjectedServiceUnitTestCase(Class<T> serviceClass) {
        super(serviceClass);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getSystemContext().getCacheDir().getAbsolutePath());
        MockitoAnnotations.initMocks(this);

        Application application = (Application) getSystemContext().getApplicationContext();

        List<Dependency> dependencies = new ArrayList<Dependency>();
        for(DependencyProvider provider : Arrays.asList(new MockitoFieldDependencyProvider(), new ProvidesMethodDependencyProvider())) {
            dependencies.addAll(provider.getDependencies(this));
        }

        List<Module> modules = ModuleFactory.instantiateModulesForTest(this, getSystemContext(), dependencies);
        RoboGuice.overrideApplicationInjector(application,
                Modules.override(RoboGuice.newDefaultRoboModule(application)).with(modules.toArray(new Module[modules.size()])));
    }
}
