package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcDecoder;
import com.strategyobject.substrateclient.scale.annotations.Scale;
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
        Future,
        Ready,
        Broadcast,
        InBlock,
        Retracted,
        FinalityTimeout,
        Finalized,
        Usurped,
        Dropped,
        Invalid
    }

    class Future implements ExtrinsicStatus {
        @Override
        public Status getStatus() {
            return Status.Future;
        }
    }

    class Ready implements ExtrinsicStatus {
        @Override
        public Status getStatus() {
            return Status.Ready;
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
            return Status.Broadcast;
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
            return Status.InBlock;
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
            return Status.Retracted;
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
            return Status.FinalityTimeout;
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
            return Status.Finalized;
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
            return Status.Usurped;
        }
    }

    class Dropped implements ExtrinsicStatus {
        @Override
        public Status getStatus() {
            return Status.Dropped;
        }
    }

    class Invalid implements ExtrinsicStatus {
        @Override
        public Status getStatus() {
            return Status.Invalid;
        }
    }
}
