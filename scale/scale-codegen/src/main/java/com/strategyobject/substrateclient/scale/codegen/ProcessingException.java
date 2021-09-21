package com.strategyobject.substrateclient.scale.codegen;

import javax.lang.model.element.Element;

public class ProcessingException extends Exception {
    protected final Element element;

    public ProcessingException(Element element, String message, Object... args) {
        super(String.format(message, args));
        this.element = element;
    }

    public ProcessingException(Throwable cause, Element element, String message, Object... args) {
        super(String.format(message, args), cause);
        this.element = element;
    }
}
