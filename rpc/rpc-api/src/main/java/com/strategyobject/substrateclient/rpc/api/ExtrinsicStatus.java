package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.rpc.annotation.RpcDecoder;
import com.strategyobject.substrateclient.rpc.api.primitives.BlockHash;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public interface ExtrinsicStatus {
    ExtrinsicStatus Ready = new ExtrinsicStatus.Ready();
    ExtrinsicStatus Future = new ExtrinsicStatus.Future();
    ExtrinsicStatus Dropped = new ExtrinsicStatus.Dropped();
    ExtrinsicStatus Invalid = new ExtrinsicStatus.Invalid();

    Status getStatus();

    enum Status {
        FUTURE,
        READY,
        BROADCAST,
        IN_BLOCK,
        RETRACTED,
        FINALITY_TIMEOUT,
        FINALIZED,
        USURPED,
        DROPPED,
        INVALID
    }

    class Future implements ExtrinsicStatus {
        @Override
        public Status getStatus() {
            return Status.FUTURE;
        }
    }

    class Ready implements ExtrinsicStatus {
        @Override
        public Status getStatus() {
            return Status.READY;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @RpcDecoder
    class Broadcast implements ExtrinsicStatus {
        @Scale
        private List<String> broadcast;

        @Override
        public Status getStatus() {
            return Status.BROADCAST;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @RpcDecoder
    class InBlock implements ExtrinsicStatus {
        @Scale
        private BlockHash inBlock;

        @Override
        public Status getStatus() {
            return Status.IN_BLOCK;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @RpcDecoder
    class Retracted implements ExtrinsicStatus {
        @Scale
        private BlockHash retracted;

        @Override
        public Status getStatus() {
            return Status.RETRACTED;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @RpcDecoder
    class FinalityTimeout implements ExtrinsicStatus {
        @Scale
        private BlockHash finalityTimeout;

        @Override
        public Status getStatus() {
            return Status.FINALITY_TIMEOUT;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @RpcDecoder
    class Finalized implements ExtrinsicStatus {
        @Scale
        private BlockHash finalized;

        @Override
        public Status getStatus() {
            return Status.FINALIZED;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @RpcDecoder
    class Usurped implements ExtrinsicStatus {
        @Scale
        private BlockHash usurped;

        @Override
        public Status getStatus() {
            return Status.USURPED;
        }
    }

    class Dropped implements ExtrinsicStatus {
        @Override
        public Status getStatus() {
            return Status.DROPPED;
        }
    }

    class Invalid implements ExtrinsicStatus {
        @Override
        public Status getStatus() {
            return Status.INVALID;
        }
    }
}
