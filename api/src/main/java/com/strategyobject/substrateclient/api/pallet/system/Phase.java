package com.strategyobject.substrateclient.api.pallet.system;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.types.union.Union;

/**
 * A phase of a block's execution.
 */
public class Phase extends Union {

    private Phase() {
    }

    /**
     * @return true if applying an extrinsic
     */
    public boolean isApplyExtrinsic() {
        return index == 0;
    }

    /**
     * @return true if finalizing the block
     */
    public boolean isFinalization() {
        return index == 1;
    }

    /**
     * @return true if initializing the block
     */
    public boolean isInitialization() {
        return index == 2;
    }

    /**
     * @return index of the extrinsic applied
     */
    public long getApplyExtrinsicIndex() {
        Preconditions.checkState(index == 0);
        return (long) value;
    }

    public static Phase ofApplyExtrinsic(long extrinsicIndex) {
        Phase result = new Phase();
        result.value = extrinsicIndex;
        result.index = 0;
        return result;
    }

    public static Phase ofFinalization() {
        Phase result = new Phase();
        result.index = 1;
        return result;
    }

    public static Phase ofInitialization() {
        Phase result = new Phase();
        result.index = 2;
        return result;
    }
}
