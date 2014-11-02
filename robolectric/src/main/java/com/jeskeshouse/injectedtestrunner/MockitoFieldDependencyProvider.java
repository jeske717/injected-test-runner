package main.java.com.jeskeshouse.injectedtestrunner;

import org.mockito.Mock;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class MockitoFieldDependencyProvider implements DependencyProvider {
    @Override
    public List<Dependency> getDependencies(Object test) throws Exception {
        final Field[] declaredFields = test.getClass().getDeclaredFields();
        List<Dependency> objects = new ArrayList<Dependency>();
        for (Field field : declaredFields) {
            Mock mockAnnotation = field.getAnnotation(Mock.class);
            if (mockAnnotation != null) {
                field.setAccessible(true);
                Annotation annotationToBindWith = AnnotationLocator.findFirstAnnotationNotOfType(field, Mock.class);
                objects.add(new MockitoFieldDependency(field, field.get(test), annotationToBindWith));
            }
        }
        return objects;
    }
}
