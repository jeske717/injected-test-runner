package main.java.com.jeskeshouse.injectedtestrunner;

import com.google.inject.Provides;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
            dependencies.add(new ProvidedMethodDependency(providedMethod, providedMethod.invoke(test), annotationToBindWith));
        }
        return dependencies;
    }

}
