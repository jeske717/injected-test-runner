package com.jeskeshouse.injectedtestrunner;

import android.content.Context;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.util.Modules;
import org.junit.runners.model.InitializationError;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.DefaultTestLifecycle;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestLifecycle;
import roboguice.RoboGuice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
                List<TestDependency> mocks = getMocks(test);
                List<TestDependency> providedObjects = getProvidedObjects(test);
                mocks.addAll(providedObjects);
                setupRoboguice(test, mocks);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to get instance of object", e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Failed to get instance of object", e);
            }
        }

        private List<TestDependency> getProvidedObjects(Object test) throws InvocationTargetException, IllegalAccessException {
            List<TestDependency> dependencies = new ArrayList<TestDependency>();
            List<Method> providedMethods = new ArrayList<Method>();
            for(Method method : test.getClass().getDeclaredMethods()) {
                if(method.isAnnotationPresent(Provides.class)) {
                    providedMethods.add(method);
                }
            }
            for (Method providedMethod : providedMethods) {
                Provides providesAnnotation = providedMethod.getAnnotation(Provides.class);
                Annotation annotationToBindWith = null;
                for(Annotation annotation : providedMethod.getDeclaredAnnotations()) {
                    if(!providesAnnotation.equals(annotation)) {

                        annotationToBindWith = annotation;
                        break;
                    }
                }
                dependencies.add(new ProvidedTestDependency(providedMethod.invoke(test), annotationToBindWith));
            }
            return dependencies;
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

        private List<TestDependency> getMocks(Object test) throws IllegalAccessException {
            final Field[] declaredFields = test.getClass().getDeclaredFields();
            List<TestDependency> objects = new ArrayList<TestDependency>();
            for (Field field : declaredFields) {
                Mock mockAnnotation = field.getAnnotation(Mock.class);
                if (mockAnnotation != null) {
                    field.setAccessible(true);
                    Annotation annotationToBindWith = null;
                    for (Annotation annotation : field.getAnnotations()) {
                        if (!mockAnnotation.equals(annotation)) {
                            annotationToBindWith = annotation;
                            break;
                        }
                    }
                    objects.add(new TestDependency(field.get(test), annotationToBindWith));
                }
            }
            return objects;
        }

        private void setupRoboguice(Object test, List<TestDependency> objects) {
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

        private List<TestDependency> mocksToInject;

        public TestModule(List<TestDependency> mocksToInject) {
            this.mocksToInject = mocksToInject;
        }

        @Override
        protected void configure() {
            List<Class<?>> boundClasses = new ArrayList<Class<?>>();
            for (TestDependency dependency : mocksToInject) {
                Object mock = dependency.getDependency();
                Class clazz = dependency.getBindableClass();
                if (boundClasses.contains(clazz)) {
                    Logger.getLogger(InjectedTestRunner.class.getName()).warning("Unsupported configuration for " + clazz + ":\n" +
                            "\tmultiple implementations are bound.  Use @Named if you need multiple instances of a single type for injection.");
                } else {
                    if (dependency.shouldBindWithAnnotation()) {
                        bind(clazz).annotatedWith(dependency.getAnnotation()).toInstance(mock);
                    } else {
                        bind(clazz).toInstance(mock);
                        boundClasses.add(clazz);
                    }
                }
            }
        }
    }
}
