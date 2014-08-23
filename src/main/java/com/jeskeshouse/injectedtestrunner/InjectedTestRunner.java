package com.jeskeshouse.injectedtestrunner;

import android.content.Context;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Modules;
import org.junit.runners.model.InitializationError;
import org.mockito.MockitoAnnotations;
import org.robolectric.DefaultTestLifecycle;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestLifecycle;
import roboguice.RoboGuice;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

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
                for(DependencyProvider provider : Arrays.asList(new MockitoFieldDependencyProvider(), new ProvidesMethodDependencyProvider())) {
                    dependencies.addAll(provider.getDependencies(test));
                }
                setupRoboguice(test, dependencies);
            } catch (Exception e) {
                throw new RuntimeException("Failed to provide dependencies", e);
            }
        }

        @Override
        public void afterTest(Method method) {
            Robolectric.reset();
            if (Robolectric.application != null) {
                Robolectric.runBackgroundTasks();
                Robolectric.runUiThreadTasksIncludingDelayedTasks();
            }
            RoboGuice.util.reset();
            super.afterTest(method);
        }

        private void setupRoboguice(Object test, List<Dependency> objects) {
            List<Module> modules = new ArrayList<Module>();
            modules.add(new TestModule(objects));
            if (test.getClass().isAnnotationPresent(RequiredModules.class)) {
                try {
                    for (Class<? extends Module> moduleClass : test.getClass().getAnnotation(RequiredModules.class).value()) {
                        try {
                            modules.add(moduleClass.getDeclaredConstructor(Context.class).newInstance(Robolectric.application));
                        } catch (NoSuchMethodException ignored) {
                            modules.add(moduleClass.newInstance());
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Unable to instantiate configured modules!", e);
                }
            }
            RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE,
                    Modules.override(RoboGuice.newDefaultRoboModule(Robolectric.application))
                            .with(modules.toArray(new Module[modules.size()])));

            RoboGuice.injectMembers(Robolectric.application, test);
        }
    }

    private static class TestModule extends AbstractModule {

        private List<Dependency> mocksToInject;

        public TestModule(List<Dependency> mocksToInject) {
            this.mocksToInject = mocksToInject;
        }

        @Override
        protected void configure() {
            List<TypeLiteral<?>> boundClasses = new ArrayList<TypeLiteral<?>>();
            for (Dependency dependency : mocksToInject) {
                TypeLiteral type = dependency.getTypeToBindTo();
                if (boundClasses.contains(type)) {
                    Logger.getLogger(InjectedTestRunner.class.getName()).warning("Unsupported configuration for " + type + ":\n" +
                            "\tmultiple implementations are bound.  Use @Named if you need multiple instances of a single type for injection.");
                } else {
                    if (dependency.shouldBindWithAnnotation()) {
                        bind(type).annotatedWith(dependency.getBindingAnnotation()).toInstance(dependency.getInstance());
                    } else {
                        bind(type).toInstance(dependency.getInstance());
                        boundClasses.add(type);
                    }
                }
            }
        }
    }
}
