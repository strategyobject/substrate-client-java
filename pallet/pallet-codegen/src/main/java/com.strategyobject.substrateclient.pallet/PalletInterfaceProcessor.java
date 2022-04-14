package com.strategyobject.substrateclient.pallet;

import com.google.auto.service.AutoService;
import com.strategyobject.substrateclient.common.codegen.ProcessingException;
import com.strategyobject.substrateclient.common.codegen.ProcessorContext;
import com.strategyobject.substrateclient.pallet.annotations.Pallet;
import lombok.val;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@SupportedAnnotationTypes("com.strategyobject.substrateclient.pallet.annotations.Pallet")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class PalletInterfaceProcessor extends AbstractProcessor {
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

        for (val annotatedElement : roundEnv.getElementsAnnotatedWith(Pallet.class)) {
            if (annotatedElement.getKind() != ElementKind.INTERFACE) {
                context.error(
                        annotatedElement,
                        "Only interfaces can be annotated with `@%s`.",
                        Pallet.class.getSimpleName());

                return true;
            }

            val typeElement = (TypeElement) annotatedElement;
            try {
                val annotatedInterface = new PalletAnnotatedInterface(
                        typeElement,
                        new CompoundMethodProcessor(typeElement,
                                Collections.singletonList(
                                        new StorageProcessor(typeElement)
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
