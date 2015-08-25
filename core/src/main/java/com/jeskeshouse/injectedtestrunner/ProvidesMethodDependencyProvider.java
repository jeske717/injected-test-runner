package com.jeskeshouse.injectedtestrunner;

import com.google.inject.Provides;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Provider;

class ProvidesMethodDependencyProvider implements DependencyProvider {
    @Override
    public List<Dependency> getDependencies(Object test) throws Exception {
        List<Dependency> dependencies = new ArrayList<Dependency>();
        List<Method> providedMethods = new ArrayList<Method>();
        for (Method method : test.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Provides.class)) {
                providedMethods.add(method);
            }
        }
        for (Method providedMethod : providedMethods) {
            Annotation annotationToBindWith = AnnotationLocator.findFirstAnnotationNotOfType(providedMethod, Provides.class);
            dependencies.add(new ProvidedMethodDependency(providedMethod, new ProvidesMethodInvoker(providedMethod), annotationToBindWith));
        }
        return dependencies;
    }

    private static class ProvidesMethodInvoker implements Provider<Object> {

        private final Method method;

        public ProvidesMethodInvoker(Method method) {
            this.method = method;
        }

        @Override
        public Object get() {
            try {
                return method.invoke(InitializedTestInstance.get());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
