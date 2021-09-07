package com.strategyobject.substrateclient.transport.ws;

import lombok.NonNull;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

class WebSocket extends WebSocketClient {
    private final Runnable onOpen;
    private final Consumer<String> onMessage;
    private final BiConsumer<Integer, String> onClose;
    private final Consumer<Exception> onError;

    private WebSocket(URI serverUri,
                      Map<String, String> httpHeaders,
                      Runnable onOpen,
                      Consumer<String> onMessage,
                      BiConsumer<Integer, String> onClose,
                      Consumer<Exception> onError) {
        super(serverUri, httpHeaders);
        this.onOpen = onOpen;
        this.onMessage = onMessage;
        this.onClose = onClose;
        this.onError = onError;
    }

    @Override
    public void onOpen(ServerHandshake _handshakeData) {
        onOpen.run();
    }

    @Override
    public void onMessage(String message) {
        onMessage.accept(message);
    }

    @Override
    public void onClose(int code, String reason, boolean _remote) {
        onClose.accept(code, reason);
    }

    @Override
    public void onError(Exception ex) {
        onError.accept(ex);
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private URI serverUri;
        private Map<String, String> httpHeaders;
        private Runnable onOpen;
        private Consumer<String> onMessage;
        private BiConsumer<Integer, String> onClose;
        private Consumer<Exception> onError;

        Builder setServerUri(@NonNull URI serverUri) {
            this.serverUri = serverUri;
            return this;
        }

        Builder setHttpHeaders(Map<String, String> httpHeaders) {
            this.httpHeaders = httpHeaders;
            return this;
        }

        Builder onOpen(@NonNull Runnable onOpen) {
            this.onOpen = onOpen;
            return this;
        }

        Builder onMessage(@NonNull Consumer<String> onMessage) {
            this.onMessage = onMessage;
            return this;
        }

        Builder onClose(@NonNull BiConsumer<Integer, String> onClose) {
            this.onClose = onClose;
            return this;
        }

        Builder onError(@NonNull Consumer<Exception> onError) {
            this.onError = onError;
            return this;
        }

        public WebSocketClient build() {
            checkRequiredFieldsSet();

            return new WebSocket(
                    this.serverUri,
                    this.httpHeaders,
                    this.onOpen,
                    this.onMessage,
                    this.onClose,
                    this.onError);
        }

        private void checkRequiredFieldsSet() {
            String undefinedFields = "";
            undefinedFields = serverUri == null ? undefinedFields + " serverUri" : undefinedFields;
            undefinedFields = onOpen == null ? undefinedFields + " onOpen" : undefinedFields;
            undefinedFields = onMessage == null ? undefinedFields + " onMessage" : undefinedFields;
            undefinedFields = onClose == null ? undefinedFields + " onClose" : undefinedFields;
            undefinedFields = onError == null ? undefinedFields + " onError" : undefinedFields;
            if (!undefinedFields.isEmpty()) {
                throw new IllegalStateException(
                        "Following field(s) are required but weren't set: " + undefinedFields);
            }
        }
    }
}