package com.jeskeshouse.injectedtestrunner.dagger;

import org.mockito.Mock;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("com.jeskeshouse.injectedtestrunner.dagger.MockModule")
public class MockModuleAnnotationProcessor extends AbstractProcessor {
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element rootElement : roundEnv.getRootElements()) {
            if (rootElement.getAnnotation(MockModule.class) != null) {
                TypeElement type = (TypeElement) rootElement;
                PackageElement packageElement = (PackageElement) type.getEnclosingElement();
                PendingModule module = new PendingModule(type.getSimpleName() + "MockModule", packageElement.getQualifiedName().toString());

                for (Element element : type.getEnclosedElements()) {
                    if (element.getAnnotation(Mock.class) != null) {
                        module.addMock(new MockField(element.getSimpleName().toString(), element.getAnnotationMirrors()));
                    }
                }

                writeModuleSource(module);
            }
        }
        return true;
    }

    private void writeModuleSource(PendingModule module) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, module.toString());
        try {
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(module.getClassName());
            BufferedWriter writer = new BufferedWriter(sourceFile.openWriter());

            writer.append("package ").append(module.getPackageName()).append(";");
            writer.newLine();
            writer.newLine();

            writer.append("public class ").append(module.getClassName()).append("{}");
            writer.newLine();
            writer.newLine();

            writer.flush();
            writer.close();
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Unable to generate source file for " + module.getClassName());
        }
    }

    private static class PendingModule {
        private final String className;
        private final String packageName;
        private final List<MockField> mocks = new ArrayList<MockField>();

        private PendingModule(String className, String packageName) {
            this.className = className;
            this.packageName = packageName;
        }

        public String getClassName() {
            return className;
        }

        public List<MockField> getMocks() {
            return mocks;
        }

        public String getPackageName() {
            return packageName;
        }

        public void addMock(MockField mock) {
            mocks.add(mock);
        }

        @Override
        public String toString() {
            return "PendingModule{" +
                    "className='" + className + '\'' +
                    ", packageName='" + packageName + '\'' +
                    ", mocks=" + mocks +
                    '}';
        }
    }

    private static class MockField {
        private final String name;
        private final List<AnnotationMirror> annotations = new ArrayList<AnnotationMirror>();

        private MockField(String name, List<? extends AnnotationMirror> annotations) {
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
}
