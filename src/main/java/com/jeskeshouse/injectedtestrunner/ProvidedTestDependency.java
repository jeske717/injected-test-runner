package com.jeskeshouse.injectedtestrunner;

import java.lang.annotation.Annotation;

public class ProvidedTestDependency extends TestDependency {
    public ProvidedTestDependency(Object mockInstance, Annotation annotation) {
        super(mockInstance, annotation);
    }

    @Override
    public Class<?> getBindableClass() {
        return getDependency().getClass();
    }
}
