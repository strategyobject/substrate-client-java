package com.strategyobject.substrateclient.rpc.codegen.decoder;

import com.google.auto.service.AutoService;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.rpc.annotation.RpcDecoder;
import lombok.val;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Set;

@SupportedAnnotationTypes("com.strategyobject.substrateclient.rpc.annotation.RpcDecoder")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class RpcDecoderProcessor extends AbstractProcessor {
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

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(RpcDecoder.class)) {
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                context.error(
                        annotatedElement,
                        "Only classes can be annotated with `@%s`.",
                        RpcDecoder.class.getSimpleName());

                return true;
            }

            val typeElement = (TypeElement) annotatedElement;
            try {
                new RpcDecoderAnnotatedClass(typeElement).generateDecoder(context);
            } catch (ProcessingException e) {
                context.error(typeElement, e);
                return true;
            } catch (IOException e) {
                context.error(e);
                return true;
            }
        }

        return false;
    }
}
