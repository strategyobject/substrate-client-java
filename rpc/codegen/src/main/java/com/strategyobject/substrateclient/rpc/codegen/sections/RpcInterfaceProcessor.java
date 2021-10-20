package com.strategyobject.substrateclient.rpc.codegen.sections;

import com.google.auto.service.AutoService;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcInterface;
import lombok.val;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@SupportedAnnotationTypes("com.strategyobject.substrateclient.rpc.core.annotations.RpcInterface")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class RpcInterfaceProcessor extends AbstractProcessor {
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

        for (val annotatedElement : roundEnv.getElementsAnnotatedWith(RpcInterface.class)) {
            if (annotatedElement.getKind() != ElementKind.INTERFACE) {
                context.error(
                        annotatedElement,
                        "Only interfaces can be annotated with `@%s`.",
                        RpcInterface.class.getSimpleName());

                return true;
            }

            val typeElement = (TypeElement) annotatedElement;
            try {
                val annotatedInterface = new RpcAnnotatedInterface(
                        typeElement,
                        new CompoundRpcMethodProcessor(
                                typeElement,
                                Arrays.asList(
                                        new RpcInterfaceMethodValidatingProcessor(typeElement),
                                        new RpcCallProcessor(typeElement),
                                        new RpcSubscriptionProcessor(typeElement)
                                )));

                annotatedInterface.generateClass(context);
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
}
