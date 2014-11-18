package com.jeskeshouse.injectedtestrunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

class MockitoFieldDependency extends AbstractDependency {

    private final ClassExtractor classExtractor;

    public MockitoFieldDependency(Field field, Object instance, Annotation annotation, ClassExtractor classExtractor) {
        super(instance, annotation, field.getGenericType());
        this.classExtractor = classExtractor;
    }

    @Override
    protected Class<?> getUserClass() {
        return classExtractor.extract(instance);
    }
}
