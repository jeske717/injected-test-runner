package com.jeskeshouse.injectedtestrunner;

import org.mockito.Mock;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Provider;

class MockitoFieldDependencyProvider implements DependencyProvider {

    private final ClassExtractor classExtractor;

    public MockitoFieldDependencyProvider(ClassExtractor classExtractor) {
        this.classExtractor = classExtractor;
    }

    @Override
    public List<Dependency> getDependencies(Object test) throws Exception {
        final List<Field> declaredFields = getDeclaredFields(test);
        List<Dependency> objects = new ArrayList<Dependency>();
        for (Field field : declaredFields) {
            Mock mockAnnotation = field.getAnnotation(Mock.class);
            if (mockAnnotation != null) {
                field.setAccessible(true);
                Annotation annotationToBindWith = AnnotationLocator.findFirstAnnotationNotOfType(field, Mock.class);
                objects.add(new MockitoFieldDependency(field, new FieldReadingProvider(field), annotationToBindWith, classExtractor));
            }
        }
        return objects;
    }

    private List<Field> getDeclaredFields(Object test) {
        List<Field> fields = new ArrayList<Field>();
        for (Class daClass : getAllClasses(test)) {
            fields.addAll(Arrays.asList(daClass.getDeclaredFields()));
        }
        return fields;
    }

    private List<Class> getAllClasses(Object test) {
        List<Class> classes = new ArrayList<Class>();
        Class currentClass = test.getClass();
        while (!currentClass.equals(Object.class)) {
            classes.add(currentClass);
            currentClass = currentClass.getSuperclass();
        }
        return classes;
    }

    private static class FieldReadingProvider implements Provider<Object> {

        private final Field field;

        public FieldReadingProvider(Field field) {
            this.field = field;
        }

        @Override
        public Object get() {
            try {
                return field.get(InitializedTestInstance.get());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
