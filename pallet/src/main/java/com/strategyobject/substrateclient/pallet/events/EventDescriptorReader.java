package com.strategyobject.substrateclient.pallet.events;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.rpc.metadata.MetadataProvider;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class EventDescriptorReader implements ScaleReader<EventDescriptor> {
    private final @NonNull ScaleReaderRegistry scaleReaderRegistry;
    private final @NonNull MetadataProvider metadataProvider;
    private final @NonNull EventRegistry eventRegistry;

    @Override
    public EventDescriptor read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        val palletIndex = Streamer.readByte(stream);
        val pallet = metadataProvider.getPallets().get(palletIndex);
        val eventIndex = Streamer.readByte(stream);

        val eventClass = eventRegistry.resolve(pallet.getName(), eventIndex);
        if (eventClass == null) {
            throw new RuntimeException(
                    String.format("Unknown event with index %d in pallet %s", eventIndex, pallet.getName()));
        }

        val eventReader = scaleReaderRegistry.resolve(eventClass);
        val event = eventReader.read(stream);
        return new EventDescriptor(pallet, eventIndex, event);
    }
}
