package com.jeskeshouse.injectedtestrunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.inject.Provider;

class ProvidedMethodDependency extends AbstractDependency {

    public ProvidedMethodDependency(Method method, Provider<Object> provider, Annotation annotation) {
        super(provider, annotation, method.getGenericReturnType());
    }

    @Override
    protected Class<?> getUserClass() {
        return provider.get().getClass();
    }
}
