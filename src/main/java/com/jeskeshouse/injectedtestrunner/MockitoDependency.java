package com.jeskeshouse.injectedtestrunner;

import java.lang.annotation.Annotation;

public class MockitoDependency {

    private final Object mockInstance;
    private final Annotation annotation;

    public MockitoDependency(Object mockInstance, Annotation annotation) {
        this.mockInstance = mockInstance;
        this.annotation = annotation;
    }

    public boolean shouldBindWithAnnotation() {
        return this.annotation != null;
    }

    public Object getMockInstance() {
        return mockInstance;
    }

    public Annotation getAnnotation() {
        return annotation;
    }
}
