package com.strategyobject.substrateclient.scale.codegen.writer;

import com.google.auto.service.AutoService;
import com.strategyobject.substrateclient.scale.annotations.ScaleWriter;
import com.strategyobject.substrateclient.scale.codegen.ProcessingException;
import com.strategyobject.substrateclient.scale.codegen.ProcessorContext;
import lombok.val;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Set;

@SupportedAnnotationTypes("com.strategyobject.substrateclient.scale.annotations.ScaleWriter")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class ScaleWriterProcessor extends AbstractProcessor {
    private ProcessorContext context;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        context = new ProcessorContext(
                processingEnv.getTypeUtils(),
                processingEnv.getElementUtils());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }

        for (val annotatedElement : roundEnv.getElementsAnnotatedWith(ScaleWriter.class)) {
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                error(
                        annotatedElement,
                        "Only classes can be annotated with `@%s`.",
                        ScaleWriter.class.getSimpleName());

                return true;
            }

            val typeElement = (TypeElement) annotatedElement;
            if (!validateScaleSelfWritable(typeElement)) {
                error(
                        typeElement,
                        "Classes implementing `%1$s` and annotated with `@%2$s` have to be either nongeneric or all its parameters have to implement `%1$s`",
                        ProcessorContext.SCALE_SELF_WRITABLE,
                        ScaleWriter.class.getSimpleName());
            }

            try {
                new ScaleWriterAnnotatedClass(typeElement).generateWriter(context, filer);
            } catch (ProcessingException e) {
                error(typeElement, e.getMessage());
                return true;
            } catch (IOException e) {
                error(null, e.getMessage());
                return true;
            }
        }

        return true;
    }

    private boolean validateScaleSelfWritable(TypeElement typeElement) {
        if (!context.isSubtypeOfScaleSelfWritable(typeElement.asType())) {
            return true;
        }

        val typeParameters = typeElement.getTypeParameters();
        return typeParameters.size() == 0 ||
                typeParameters.stream().allMatch(x -> context.isSubtypeOfScaleSelfWritable(x.asType()));
    }

    private void error(Element e, String message, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(message, args),
                e
        );
    }
}
