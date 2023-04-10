package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

@AutoRegister(types = {
    HeterogeneousVecWriter.HeterogeneousVec.class
})
public class HeterogeneousVecWriter implements ScaleWriter<HeterogeneousVecWriter.HeterogeneousVec<?>> {
  private final ScaleWriterRegistry scaleWriterRegistry;

  public HeterogeneousVecWriter(ScaleWriterRegistry scaleWriterRegistry) {
    this.scaleWriterRegistry = scaleWriterRegistry;
  }

  @Override
  public void write(HeterogeneousVec<?> value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
    CompactIntegerWriter.writeInternal(value.size(), stream);
    for (val item : value) {
      ScaleWriter<Object> scaleWriter = (ScaleWriter<Object>) scaleWriterRegistry.resolve(item.getClass());
      scaleWriter.write(item, stream, writers);
    }
  }

  public static class HeterogeneousVec<T> extends LinkedList<T> {

  }
}
