package main.java.com.jeskeshouse.injectedtestrunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

class ProvidedMethodDependency extends AbstractDependency {

    public ProvidedMethodDependency(Method method, Object instance, Annotation annotation) {
        super(instance, annotation, method.getGenericReturnType());
    }

    @Override
    protected Class<?> getUserClass() {
        return instance.getClass();
    }
}
