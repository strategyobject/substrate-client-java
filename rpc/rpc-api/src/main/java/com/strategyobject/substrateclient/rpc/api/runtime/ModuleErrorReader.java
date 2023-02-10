package com.strategyobject.substrateclient.rpc.api.runtime;

import com.google.common.collect.Lists;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;

import java.io.IOException;
import java.io.InputStream;

@AutoRegister(types = ModuleError.class)
public class ModuleErrorReader implements ScaleReader<ModuleError>{
  private final ScaleReaderRegistry registry;

  public ModuleErrorReader(ScaleReaderRegistry registry) {
    if (registry == null) {
      throw new IllegalArgumentException("registry can't be null.");
    }
    this.registry = registry;
  }

  @Override
  @SuppressWarnings({"unchecked"})
  public ModuleError read(InputStream stream, ScaleReader<?>... readers) throws IOException {
    if (stream == null) throw new IllegalArgumentException("stream is null");
    if (readers != null && readers.length > 0) throw new IllegalArgumentException();
    ModuleError result = new ModuleError();
    try {
      final ScaleReader<Integer> u8Reader = (ScaleReader<Integer>) registry.resolve(ScaleType.U8.class);
      result.setIndex(u8Reader.read(stream));
      final Integer index0 = u8Reader.read(stream);
      final Integer index1 = u8Reader.read(stream);
      final Integer index2 = u8Reader.read(stream);
      final Integer index3 = u8Reader.read(stream);
      result.setError(Lists.newArrayList(index0, index1, index2, index3));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return result;
  }
}
