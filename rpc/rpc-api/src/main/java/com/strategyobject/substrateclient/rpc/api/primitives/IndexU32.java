package com.strategyobject.substrateclient.rpc.api.primitives;

import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;

/**
 * Sadly it seems thew Index that is sent in an extrinsic needs to be a CompactBigInteger, however, the SCALE encoding
 * for an Index coming from the AccountNonceApi_account_nonce is a U32 hence the need for 2 different class depending on
 * context
 *
 * @see Index
 */
@ScaleReader
public class IndexU32 {
  @Scale(ScaleType.U32.class)
  private Long value;

  public IndexU32(){

  }

  public IndexU32(Long value){
    this.value = value;
  }

  public Long getValue() {
    return value;
  }

  public void setValue(Long value) {
    this.value = value;
  }

  public static IndexU32 of(Long value){
    return new IndexU32(value);
  }

  public Index toIndex(){
    return Index.of(value);
  }

  public static IndexU32 fromIndex(Index index){
    return IndexU32.of(index.getValue().longValue());
  }
}
