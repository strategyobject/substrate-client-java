package com.strategyobject.substrateclient.api.pallet.scheduler;

import com.strategyobject.substrateclient.common.types.tuple.Pair;
import com.strategyobject.substrateclient.pallet.annotation.*;
import com.strategyobject.substrateclient.rpc.api.primitives.Hash;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

import javax.sound.midi.Sequence;

@Pallet("Scheduler")
public interface Scheduler {
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

    @Event(index = 2)
    @Getter
    @Setter
    @ScaleReader
    class Dispatched {
        private Pair<Long,Long> hash;
        private enum id {
            
        }
        private ? result;
    }

    @Event(index = 3)
    @Getter
    @Setter
    @ScaleReader
    class CallLookupFailed {
        private Pair<Long,Long> hash;
        private ? id;
        private ? error;
    }
}


fields: [
        {
        name: task
        type: 39
        typeName: TaskAddress&lt;T::BlockNumber&gt;
        docs: []
        }
        {
        name: id
        type: 40
        typeName: Option&lt;Vec&lt;u8&gt;&gt;
        docs: []
        }
        {
        name: result
        type: 29
        typeName: DispatchResult
        docs: []
        }
        ]
        index: 2
        docs: [
        Dispatched some task.
        ]
        }
        {
        name: CallLookupFailed
        fields: [
        {
        name: task
        type: 39
        typeName: TaskAddress&lt;T::BlockNumber&gt;
        docs: []
        }
        {
        name: id
        type: 40
        typeName: Option&lt;Vec&lt;u8&gt;&gt;
        docs: []
        }
        {
        name: error
        type: 41
        typeName: LookupError
        docs: []
        }