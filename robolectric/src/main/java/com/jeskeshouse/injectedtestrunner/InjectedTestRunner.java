package com.jeskeshouse.injectedtestrunner;

import com.google.inject.Module;
import com.google.inject.util.Modules;

import org.junit.runners.model.InitializationError;
import org.mockito.MockitoAnnotations;
import org.robolectric.DefaultTestLifecycle;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestLifecycle;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import roboguice.RoboGuice;

public class InjectedTestRunner extends RobolectricTestRunner {

    public InjectedTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected Class<? extends TestLifecycle> getTestLifecycleClass() {
        return TestLifecycleWithMockito.class;
    }

    public static class TestLifecycleWithMockito extends DefaultTestLifecycle {

        @Override
        public void prepareTest(Object test) {
            super.prepareTest(test);
            MockitoAnnotations.initMocks(test);
            try {
                List<Dependency> dependencies = new ArrayList<Dependency>();
                for (DependencyProvider provider : Arrays.asList(new MockitoFieldDependencyProvider(new CglibClassExtractor()), new ProvidesMethodDependencyProvider())) {
                    dependencies.addAll(provider.getDependencies(test));
                }
                setupRoboguice(test, dependencies);
            } catch (Exception e) {
                throw new RuntimeException("Failed to provide dependencies", e);
            }
        }

        @Override
        public void afterTest(Method method) {
            Robolectric.reset(null);
            if (Robolectric.application != null) {
                Robolectric.runBackgroundTasks();
                Robolectric.runUiThreadTasksIncludingDelayedTasks();
            }
            super.afterTest(method);
        }

        private void setupRoboguice(Object test, List<Dependency> objects) {
            List<Module> modules = ModuleFactory.instantiateModulesForTest(test, Robolectric.application, objects);
            RoboGuice.overrideApplicationInjector(Robolectric.application, Modules.override(RoboGuice.newDefaultRoboModule(Robolectric.application))
                    .with(modules.toArray(new Module[modules.size()])));

            RoboGuice.injectMembers(Robolectric.application, test);
        }
    }
}
