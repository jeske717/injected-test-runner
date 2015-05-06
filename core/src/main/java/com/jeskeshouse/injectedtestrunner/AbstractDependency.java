package com.jeskeshouse.injectedtestrunner;

import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.inject.Provider;

abstract class AbstractDependency implements Dependency {

    protected final Provider<Object> provider;
    private final Annotation annotation;
    private final Type genericType;

    public AbstractDependency(Provider<Object> provider, Annotation annotation, Type genericType) {
        this.provider = provider;
        this.annotation = annotation;
        this.genericType = genericType;
    }

    @Override
    public boolean shouldBindWithAnnotation() {
        return annotation != null;
    }

    @Override
    public Annotation getBindingAnnotation() {
        return annotation;
    }

    @Override
    public Provider<Object> getInstanceProvider() {
        return provider;
    }

    @Override
    public TypeLiteral getTypeToBindTo() {
        Class<?> clazz = getUserClass();
        if(genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            return TypeLiteral.get(Types.newParameterizedType(parameterizedType.getRawType(), parameterizedType.getActualTypeArguments()));
        }
        return TypeLiteral.get(clazz);
    }

    protected abstract Class<?> getUserClass();
}
