package com.jeskeshouse.injectedtestrunner;

import com.google.inject.TypeLiteral;

import java.lang.annotation.Annotation;

import javax.inject.Provider;

public interface Dependency {
    boolean shouldBindWithAnnotation();
    Annotation getBindingAnnotation();
    Provider<Object> getInstanceProvider();
    TypeLiteral getTypeToBindTo();
}
