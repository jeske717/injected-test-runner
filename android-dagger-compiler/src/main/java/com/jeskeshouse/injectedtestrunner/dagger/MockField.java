package com.jeskeshouse.injectedtestrunner.dagger;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;

public class MockField {
    private final String name;
    private final List<AnnotationMirror> annotations = new ArrayList<AnnotationMirror>();

    public MockField(String name, List<? extends AnnotationMirror> annotations) {
        this.name = name;
        this.annotations.addAll(annotations);
    }

    public String getName() {
        return name;
    }

    public List<AnnotationMirror> getAnnotations() {
        return annotations;
    }

    @Override
    public String toString() {
        return "MockField{" +
                "name='" + name + '\'' +
                ", annotations=" + annotations +
                '}';
    }
}