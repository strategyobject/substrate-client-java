package com.strategyobject.substrateclient.rpc.api.weights;

import com.strategyobject.substrateclient.scale.annotation.ScaleReader;

/**
 * A generalized group of dispatch types.
 */
@ScaleReader
public enum DispatchClass {
    /**
     * A normal dispatch.
     */
    NORMAL,

    /**
     * An operational dispatch.
     */
    OPERATIONAL,

    /**
     * A mandatory dispatch. These kinds of dispatch are always included regardless of their
     * weight, therefore it is critical that they are separately validated to ensure that a
     * malicious validator cannot craft a valid but impossibly heavy block. Usually this just means
     * ensuring that the extrinsic can only be included once and that it is always very light.
     * <p>
     * The only real use case for this is inherent extrinsics that are required to execute in a
     * block for the block to be valid, and it solves the issue in the case that the block
     * initialization is sufficiently heavy to mean that those inherents do not fit into the
     * block. Essentially, we assume that in these exceptional circumstances, it is better to
     * allow an overweight block to be created than to not allow any block at all to be created.
     */
    MANDATORY
}
