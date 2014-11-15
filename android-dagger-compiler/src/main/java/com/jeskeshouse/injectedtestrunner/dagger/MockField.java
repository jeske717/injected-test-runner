package com.jeskeshouse.injectedtestrunner.dagger;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;

public class MockField {
    private final String qualifiedName;
    private final String name;
    private final List<AnnotationMirror> annotations = new ArrayList<AnnotationMirror>();

    public MockField(String qualifiedName, String name, List<? extends AnnotationMirror> annotations) {
        this.qualifiedName = qualifiedName;
        this.name = name;
        for (AnnotationMirror annotation : annotations) {
            if (!annotation.toString().contains("@org.mockito.Mock")) {
                this.annotations.add(annotation);
            }
        }
    }

    public String getName() {
        return name;
    }

    public List<AnnotationMirror> getAnnotations() {
        return annotations;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    @Override
    public String toString() {
        return "MockField{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", name='" + name + '\'' +
                ", annotations=" + annotations +
                '}';
    }
}