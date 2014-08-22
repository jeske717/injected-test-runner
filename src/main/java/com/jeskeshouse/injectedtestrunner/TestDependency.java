package com.jeskeshouse.injectedtestrunner;

import java.lang.annotation.Annotation;

public class TestDependency {

    private final Object mockInstance;
    private final Annotation annotation;

    public TestDependency(Object mockInstance, Annotation annotation) {
        this.mockInstance = mockInstance;
        this.annotation = annotation;
    }

    public boolean shouldBindWithAnnotation() {
        return this.annotation != null;
    }

    public Object getDependency() {
        return mockInstance;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public Class<?> getBindableClass() {
        Class clazz = getDependency().getClass();
        Class superclass = clazz.getSuperclass();
        if (superclass.equals(Object.class) && clazz.getInterfaces().length > 0) {
            superclass = clazz.getInterfaces()[0];
        }
        return superclass;
    }
}
