package com.jeskeshouse.injectedtestrunner;

class DexmakerClassExtractor implements ClassExtractor {
    @Override
    public Class<?> extract(Object instance) {
        Class<?> clazz = instance.getClass();
        Class<?> superclass = clazz.getSuperclass();
        if (clazz.getInterfaces().length > 0) {
            superclass = clazz.getInterfaces()[0];
        }

        return superclass;
    }
}
