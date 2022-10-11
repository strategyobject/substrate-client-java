package com.strategyobject.substrateclient.api.pallet.scheduler;

import com.strategyobject.substrateclient.common.types.Result;
import com.strategyobject.substrateclient.common.types.tuple.Pair;
import com.strategyobject.substrateclient.pallet.annotation.*;
import com.strategyobject.substrateclient.rpc.api.runtime.DispatchError;
import com.strategyobject.substrateclient.rpc.api.runtime.LookupError;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleGeneric;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

import javax.lang.model.type.NullType;
import java.util.List;
import java.util.Optional;

@Pallet("Scheduler")
public interface Scheduler {
    /**
     * Scheduled some task.
     */
    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class Scheduled {
        @Scale(ScaleType.U32.class)
        private Long when;
        @Scale(ScaleType.U32.class)
        private Long index;
    }

    /**
     * Canceled some task.
     */
    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class Canceled {
        @Scale(ScaleType.U32.class)
        private Long when;
        @Scale(ScaleType.U32.class)
        private Long index;
    }

    /**
     * Dispatched some task.
     */
    @Event(index = 2)
    @Getter
    @Setter
    @ScaleReader
    class Dispatched {
        @ScaleGeneric(
                template = "Pair<U32,U32>",
                types = {
                        @Scale(ScaleType.U32.class)
                })
        private Pair<Long,Long> task;
        @ScaleGeneric(
                template = "Option<Vec<U8>>",
                types = {
                        @Scale(ScaleType.Option.class),
                        @Scale(ScaleType.Vec.class),
                        @Scale(ScaleType.U8.class)
                })
        private Optional<List<Short>> id;
        private Result<Void, DispatchError> result;
    }

    /**
     * The call for the provided hash was not found so the task has been aborted.
     */
    @Event(index = 3)
    @Getter
    @Setter
    @ScaleReader
    class CallLookupFailed {
        private Pair<Long,Long> task;
        @ScaleGeneric(
                template = "Option<Vec<U8>>",
                types = {
                        @Scale(ScaleType.Option.class),
                        @Scale(ScaleType.Vec.class),
                        @Scale(ScaleType.U8.class)
                })
        private Optional<List<Short>> id;
        private LookupError error;
    }
}