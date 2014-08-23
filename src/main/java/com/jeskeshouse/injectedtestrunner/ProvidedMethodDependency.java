package com.jeskeshouse.injectedtestrunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ProvidedMethodDependency extends AbstractDependency {
    public ProvidedMethodDependency(Object instance, Annotation annotation, Method method) {
        super(instance, annotation, method.getGenericReturnType());
    }

    @Override
    protected Class<?> getUserClass() {
        return instance.getClass();
    }
}
