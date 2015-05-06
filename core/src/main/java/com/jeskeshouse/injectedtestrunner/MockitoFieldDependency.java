package com.jeskeshouse.injectedtestrunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.inject.Provider;

class MockitoFieldDependency extends AbstractDependency {

    private final ClassExtractor classExtractor;

    public MockitoFieldDependency(Field field, Provider<Object> provider, Annotation annotation, ClassExtractor classExtractor) {
        super(provider, annotation, field.getGenericType());
        this.classExtractor = classExtractor;
    }

    @Override
    protected Class<?> getUserClass() {
        return classExtractor.extract(provider.get());
    }
}
