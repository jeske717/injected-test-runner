package com.jeskeshouse.injectedtestrunner.dagger;

import org.junit.runners.model.InitializationError;
import org.mockito.MockitoAnnotations;
import org.robolectric.DefaultTestLifecycle;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestLifecycle;

import java.lang.reflect.Method;
import java.util.Collections;

public class DaggerInjectedTestRunner extends RobolectricTestRunner {
    public DaggerInjectedTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected Class<? extends TestLifecycle> getTestLifecycleClass() {
        return TestLifecycleWithMockito.class;
    }

    public static void injectMocks(Object test, Object target) {
        DaggerTestInitializer.injectTestSubject(test, target, Collections.emptyList());
    }

    public static class TestLifecycleWithMockito extends DefaultTestLifecycle {

        @Override
        public void prepareTest(Object test) {
            super.prepareTest(test);
            MockitoAnnotations.initMocks(test);
        }

        @Override
        public void afterTest(Method method) {
            Robolectric.reset();
            if (Robolectric.application != null) {
                Robolectric.runBackgroundTasks();
                Robolectric.runUiThreadTasksIncludingDelayedTasks();
            }
            super.afterTest(method);
        }
    }
}