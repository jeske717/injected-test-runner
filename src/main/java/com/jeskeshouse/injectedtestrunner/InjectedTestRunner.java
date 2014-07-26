package com.jeskeshouse.injectedtestrunner;

import android.content.Context;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.util.Modules;

import org.junit.After;
import org.junit.runners.model.InitializationError;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.DefaultTestLifecycle;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestLifecycle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import roboguice.RoboGuice;

public class InjectedTestRunner extends RobolectricTestRunner {

    public InjectedTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected Class<? extends TestLifecycle> getTestLifecycleClass() {
        return TestLifecycleWithMockito.class;
    }

    @After
    public void tearDown() {
        RoboGuice.util.reset();
        Robolectric.reset();
    }

    public static class TestLifecycleWithMockito extends DefaultTestLifecycle {

        @Override
        public void prepareTest(Object test) {
            super.prepareTest(test);
            MockitoAnnotations.initMocks(test);
            try {
                setupRoboguice(test, getListOfMocks(test));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to get instance of object", e);
            }
        }

        private List<MockitoDependency> getListOfMocks(Object test) throws IllegalAccessException {
            final Field[] declaredFields = test.getClass().getDeclaredFields();
            List<MockitoDependency> objects = new ArrayList<MockitoDependency>();
            for (Field field : declaredFields) {
                Mock mockAnnotation = field.getAnnotation(Mock.class);
                if (mockAnnotation != null) {
                    field.setAccessible(true);
                    Annotation annotationToBindWith = null;
                    for(Annotation annotation : field.getAnnotations()) {
                        if(!mockAnnotation.equals(annotation)) {
                            annotationToBindWith = annotation;
                            break;
                        }
                    }
                    objects.add(new MockitoDependency(field.get(test), annotationToBindWith));
                }
            }
            return objects;
        }

        private void setupRoboguice(Object test, List<MockitoDependency> objects) {
            List<Module> modules = new ArrayList<Module>();
            modules.add(new TestModule(objects));
            if(test.getClass().isAnnotationPresent(RequiredModules.class)) {
                try {
                    for (Class<? extends Module> moduleClass : test.getClass().getAnnotation(RequiredModules.class).value()) {
                        try {
                            modules.add(moduleClass.getDeclaredConstructor(Context.class).newInstance(Robolectric.application));
                        } catch (NoSuchMethodException ignored) {
                            modules.add(moduleClass.newInstance());
                        }
                    }
                } catch(Exception e) {
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

        private List<MockitoDependency> mocksToInject;

        public TestModule(List<MockitoDependency> mocksToInject) {
            this.mocksToInject = mocksToInject;
        }

        @Override
        protected void configure() {
            List<Class<?>> boundClasses = new ArrayList<Class<?>>();
            for (MockitoDependency dependency : mocksToInject) {
                Object mock = dependency.getMockInstance();
                Class clazz = mock.getClass();
                Class superclass = clazz.getSuperclass();
                if(superclass.equals(Object.class) && clazz.getInterfaces().length > 0) {
                    superclass = clazz.getInterfaces()[0];
                }
                if(boundClasses.contains(superclass)) {
                    Logger.getLogger(InjectedTestRunner.class.getName()).warning("Unsupported configuration for " + superclass + ":\n" +
                            "\tmultiple implementations are bound.  Use @Named if you need multiple instances of a single type for injection.");
                } else {
                    if(dependency.shouldBindWithAnnotation()) {
                        bind(superclass).annotatedWith(dependency.getAnnotation()).toInstance(mock);
                    } else {
                        bind(superclass).toInstance(mock);
                        boundClasses.add(superclass);
                    }
                }
            }
        }
    }
}
