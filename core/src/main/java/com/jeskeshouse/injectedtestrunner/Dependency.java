package com.jeskeshouse.injectedtestrunner;

import com.google.inject.TypeLiteral;

import java.lang.annotation.Annotation;

public interface Dependency {
    boolean shouldBindWithAnnotation();
    Annotation getBindingAnnotation();
    Object getInstance();
    TypeLiteral getTypeToBindTo();
}
