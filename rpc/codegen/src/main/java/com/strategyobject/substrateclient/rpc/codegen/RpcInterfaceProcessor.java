package com.strategyobject.substrateclient.rpc.codegen;

import com.google.auto.service.AutoService;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcInterface;
import lombok.val;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@SupportedAnnotationTypes("com.strategyobject.substrateclient.rpc.core.annotations.RpcInterface")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class RpcInterfaceProcessor extends AbstractProcessor {
    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }

        for (val annotatedElement : roundEnv.getElementsAnnotatedWith(RpcInterface.class)) {
            if (annotatedElement.getKind() != ElementKind.INTERFACE) {
                error(
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

                annotatedInterface.generateClass(typeUtils, elementUtils, filer);
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

    private void error(Element e, String message, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(message, args),
                e
        );
    }
}
