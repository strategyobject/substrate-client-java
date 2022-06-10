package com.strategyobject.substrateclient.scale.codegen.writer;

import com.google.auto.service.AutoService;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.scale.annotation.ScaleWriter;
import lombok.val;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Set;

import static com.strategyobject.substrateclient.scale.codegen.ScaleProcessorHelper.SCALE_SELF_WRITABLE;

@SupportedAnnotationTypes("com.strategyobject.substrateclient.scale.annotation.ScaleWriter")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class ScaleWriterProcessor extends AbstractProcessor {
    private ProcessorContext context;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        context = new ProcessorContext(processingEnv.getTypeUtils(),
                processingEnv.getElementUtils(),
                processingEnv.getFiler(),
                processingEnv.getMessager());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(ScaleWriter.class)) {
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                context.error(
                        annotatedElement,
                        "Only classes can be annotated with `@%s`.",
                        ScaleWriter.class.getSimpleName());

                return true;
            }

            val typeElement = (TypeElement) annotatedElement;
            if (!validateScaleSelfWritable(typeElement)) {
                context.error(
                        typeElement,
                        "Classes implementing `%1$s` and annotated with `@%2$s` have to be either non generic or all its parameters have to implement `%1$s`",
                        SCALE_SELF_WRITABLE.getCanonicalName(),
                        ScaleWriter.class.getSimpleName());
            }

            try {
                new ScaleWriterAnnotatedClass(typeElement).generateWriter(context);
            } catch (ProcessingException e) {
                context.error(typeElement, e);
                return true;
            } catch (IOException e) {
                context.error(e);
                return true;
            }
        }

        return true;
    }

    private boolean validateScaleSelfWritable(TypeElement typeElement) {
        val selfWritable = context.erasure(context.getType(SCALE_SELF_WRITABLE));
        if (!context.isAssignable(typeElement.asType(), selfWritable)) {
            return true;
        }

        val typeParameters = typeElement.getTypeParameters();
        return typeParameters.size() == 0 ||
                typeParameters.stream().allMatch(x -> context.isAssignable(x.asType(), selfWritable));
    }
}
