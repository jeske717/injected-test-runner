package com.jeskeshouse.injectedtestrunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

class MockitoFieldDependency extends AbstractDependency {


    public MockitoFieldDependency(Field field, Object instance, Annotation annotation) {
        super(instance, annotation, field.getGenericType());
    }

    @Override
    protected Class<?> getUserClass() {
        Class<?> clazz = instance.getClass();
        Class<?> superclass = clazz.getSuperclass();
        if (superclass.equals(Object.class) && clazz.getInterfaces().length > 0) {
            superclass = clazz.getInterfaces()[0];
        }

        return superclass;
    }
}
