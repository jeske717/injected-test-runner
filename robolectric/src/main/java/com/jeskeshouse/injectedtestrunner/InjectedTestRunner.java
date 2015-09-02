package com.jeskeshouse.injectedtestrunner;

import com.google.inject.Module;
import com.google.inject.util.Modules;

import org.junit.runners.model.InitializationError;
import org.mockito.MockitoAnnotations;
import org.robolectric.DefaultTestLifecycle;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.TestLifecycle;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import roboguice.RoboGuice;

public class InjectedTestRunner extends RobolectricGradleTestRunner {

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
            Object previousTestInstance = InitializedTestInstance.get();
            InitializedTestInstance.set(test);
            MockitoAnnotations.initMocks(test);
            if(previousTestInstance == null || previousTestInstance.getClass().equals(test.getClass())) {
                RoboGuice.Util.reset();
                try {
                    List<Dependency> dependencies = new ArrayList<Dependency>();
                    for (DependencyProvider provider : Arrays.asList(new MockitoFieldDependencyProvider(new CglibClassExtractor()), new ProvidesMethodDependencyProvider())) {
                        dependencies.addAll(provider.getDependencies(test));
                    }
                    setupRoboguice(test, dependencies);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to provide dependencies", e);
                }
            } else {
                RoboGuice.injectMembers(RuntimeEnvironment.application, test);
            }
        }

        @Override
        public void afterTest(Method method) {
            Robolectric.reset();
            if (RuntimeEnvironment.application != null) {
                Robolectric.flushBackgroundThreadScheduler();
                Robolectric.flushForegroundThreadScheduler();
            }
            super.afterTest(method);
        }

        private void setupRoboguice(Object test, List<Dependency> objects) {
            List<Module> modules = ModuleFactory.instantiateModulesForTest(test, RuntimeEnvironment.application, objects);
            RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application, Modules.override(RoboGuice.newDefaultRoboModule(RuntimeEnvironment.application))
                    .with(modules.toArray(new Module[modules.size()])));

            RoboGuice.injectMembers(RuntimeEnvironment.application, test);
        }
    }
}
