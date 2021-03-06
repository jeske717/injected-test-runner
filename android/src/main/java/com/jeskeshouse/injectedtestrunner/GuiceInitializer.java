package com.jeskeshouse.injectedtestrunner;

import android.app.Application;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import roboguice.RoboGuice;

class GuiceInitializer {

    static void initialize(Object test, InitializationStrategy strategy) throws Exception {
        RoboGuice.setUseAnnotationDatabases(false);
        System.setProperty("dexmaker.dexcache", strategy.getUsableContext().getCacheDir().getAbsolutePath());
        MockitoAnnotations.initMocks(test);

        Application application = strategy.getApplication();

        Injector injector;
        if (InitializedTestInstance.get() != null && InitializedTestInstance.get().getClass().equals(test.getClass())) {
            InitializedTestInstance.set(test);
            injector = RoboGuice.getInjector(application);
        } else {
            RoboGuice.Util.reset();
            InitializedTestInstance.set(test);

            List<Dependency> dependencies = new ArrayList<>();
            for (DependencyProvider provider : Arrays.asList(new MockitoFieldDependencyProvider(new DexmakerClassExtractor()), new ProvidesMethodDependencyProvider())) {
                dependencies.addAll(provider.getDependencies(test));
            }

            List<Module> modules = ModuleFactory.instantiateModulesForTest(test, strategy.getUsableContext(), dependencies);
            injector = RoboGuice.overrideApplicationInjector(application,
                    Modules.override(RoboGuice.newDefaultRoboModule(application)).with(modules.toArray(new Module[modules.size()])));
        }
        injector.injectMembers(test);
    }

}
