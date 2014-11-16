package com.jeskeshouse.injectedtestrunner.dagger;

import org.mockito.Mock;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@SupportedAnnotationTypes("com.jeskeshouse.injectedtestrunner.dagger.MockModule")
public class MockModuleAnnotationProcessor extends AbstractProcessor {

    private Configuration config;

    public MockModuleAnnotationProcessor() {
        config = new Configuration(Configuration.VERSION_2_3_21);
        config.setClassForTemplateLoading(MockModuleAnnotationProcessor.class, "/templates/");
    }

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
                PendingModule module = new PendingModule(type.getSimpleName() + "MockModule", packageElement.getQualifiedName().toString(), type.getQualifiedName().toString());

                for (Element element : type.getEnclosedElements()) {
                    if (element.getAnnotation(Mock.class) != null) {
                        TypeMirror fieldType = element.asType();
                        module.addMock(new MockField(fieldType.toString(), element.getSimpleName().toString(), element.getAnnotationMirrors()));
                    }
                }

                writeModuleSource(module);
            }
        }
        return true;
    }

    private void writeModuleSource(PendingModule module) {
        try {
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(module.getClassName());

            Writer writer = sourceFile.openWriter();

            Template template = config.getTemplate("ModuleTemplate.ftl");
            template.process(module, writer);

            writer.flush();
            writer.close();
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Unable to generate source file for " + module.getClassName());
        } catch (TemplateException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Unable to generate source file for " + module.getClassName());
        }
    }
}
