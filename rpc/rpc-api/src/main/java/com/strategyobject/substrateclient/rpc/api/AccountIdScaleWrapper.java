package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.scale.annotation.ScaleWriter;
import lombok.Getter;
import lombok.NonNull;

/**
 * The reality is this should just be an AccountId but in the spirit of Jenga code I'd rather wrap it. I would have just
 * called this AddressId but that exists and THAT class SHOULD be called MultiAddress but I don't want to mess up
 * downstream consumers by just refactoring in place
 */
@Getter
@ScaleWriter
public class AccountIdScaleWrapper {
  private final AccountId address;

  private AccountIdScaleWrapper(@NonNull AccountId address) {
    this.address = address;
  }

  public static AccountIdScaleWrapper fromBytes(byte @NonNull [] accountId) {
    return new AccountIdScaleWrapper(AccountId.fromBytes(accountId));
  }
}
