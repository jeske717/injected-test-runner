package com.jeskeshouse.injectedtestrunner;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MockitoModule extends AbstractModule {

    private List<Dependency> mocksToInject;

    public MockitoModule(List<Dependency> mocksToInject) {
        this.mocksToInject = mocksToInject;
    }

    @Override
    protected void configure() {
        List<TypeLiteral<?>> boundClasses = new ArrayList<TypeLiteral<?>>();
        for (Dependency dependency : mocksToInject) {
            TypeLiteral type = dependency.getTypeToBindTo();
            if (boundClasses.contains(type)) {
                Logger.getLogger(MockitoModule.class.getName()).warning("Unsupported configuration for " + type + ":\n" +
                        "\tmultiple implementations are bound.  Use @Named if you need multiple instances of a single type for injection.");
            } else {
                if (dependency.shouldBindWithAnnotation()) {
                    bind(type).annotatedWith(dependency.getBindingAnnotation()).toProvider(dependency.getInstanceProvider());
                } else {
                    bind(type).toProvider(dependency.getInstanceProvider());
                    boundClasses.add(type);
                }
            }
        }
    }
}