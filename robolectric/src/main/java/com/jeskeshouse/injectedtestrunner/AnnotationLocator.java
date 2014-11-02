package main.java.com.jeskeshouse.injectedtestrunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;

class AnnotationLocator {
    public static Annotation findFirstAnnotationNotOfType(AccessibleObject accessibleObject, Class<? extends Annotation> skippedAnnotationClass) {
        Annotation annotationToSkip = accessibleObject.getAnnotation(skippedAnnotationClass);
        Annotation annotationToBindWith = null;
        for (Annotation annotation : accessibleObject.getDeclaredAnnotations()) {
            if (!annotationToSkip.equals(annotation)) {
                annotationToBindWith = annotation;
                break;
            }
        }
        return annotationToBindWith;
    }
}
