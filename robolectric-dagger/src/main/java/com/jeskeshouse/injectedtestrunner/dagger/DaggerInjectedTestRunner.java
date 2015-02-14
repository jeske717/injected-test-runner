package com.jeskeshouse.injectedtestrunner.dagger;

import org.junit.runners.model.InitializationError;
import org.mockito.MockitoAnnotations;
import org.robolectric.DefaultTestLifecycle;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestLifecycle;

import java.lang.reflect.Method;

public class DaggerInjectedTestRunner extends RobolectricTestRunner {
    public DaggerInjectedTestRunner(Class<?> testClass) throws InitializationError {
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
            DaggerTestInitializer.addMockModuleToObjectGraph(test);
        }

        @Override
        public void afterTest(Method method) {
            Robolectric.reset(null);
            if (Robolectric.application != null) {
                Robolectric.runBackgroundTasks();
                Robolectric.runUiThreadTasksIncludingDelayedTasks();
            }
            DaggerTestInitializer.resetModules();
            super.afterTest(method);
        }
    }
}