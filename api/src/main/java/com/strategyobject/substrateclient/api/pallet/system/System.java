package com.strategyobject.substrateclient.api.pallet.system;

import com.strategyobject.substrateclient.common.types.Into;
import com.strategyobject.substrateclient.pallet.annotation.*;
import com.strategyobject.substrateclient.pallet.storage.StorageNMap;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.primitives.Hash;
import com.strategyobject.substrateclient.rpc.api.runtime.DispatchError;
import com.strategyobject.substrateclient.rpc.api.weights.DispatchInfo;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The System pallet provides low-level access to core types and cross-cutting utilities.
 * It acts as the base layer for other pallets to interact with the Substrate framework components.
 */
@Pallet("System")
public interface System {
    interface AccountData extends Into {
    }

    /**
     * @return The full account information for a particular account ID.
     */
    @Storage(
            name = "Account",
            keys = {
                    @StorageKey(
                            type = @Scale(AccountId.class),
                            hasher = StorageHasher.BLAKE2_128_CONCAT
                    )
            })
    StorageNMap<AccountInfo> account();

    /**
     * @return Events deposited for the current block.
     */
    @Storage(name = "Events")
    StorageNMap<List<EventRecord>> events();

    /**
     * An extrinsic completed successfully.
     */
    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class ExtrinsicSuccess {
        private DispatchInfo dispatchInfo;
    }

    /**
     * An extrinsic failed.
     */
    @Event(index = 1)
    @Getter
    @Setter
    @ScaleReader
    class ExtrinsicFailed {
        private DispatchError dispatchError;
        private DispatchInfo dispatchInfo;
    }

    /**
     * `:code` was updated.
     */
    @Event(index = 2)
    @ScaleReader
    class CodeUpdated {
    }

    /**
     * A new account was created.
     */
    @Event(index = 3)
    @Getter
    @Setter
    @ScaleReader
    class NewAccount {
        private AccountId account;
    }

    /**
     * An account was reaped.
     */
    @Event(index = 4)
    @Getter
    @Setter
    @ScaleReader
    class KilledAccount {
        private AccountId account;
    }

    /**
     * On on-chain remark happened.
     */
    @Event(index = 5)
    @Getter
    @Setter
    @ScaleReader
    class Remarked {
        private AccountId sender;
        private Hash hash;
    }
}
