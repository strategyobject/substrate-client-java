package com.strategyobject.substrateclient.rpc.api.section;

import com.strategyobject.substrateclient.rpc.annotation.RpcCall;
import com.strategyobject.substrateclient.rpc.annotation.RpcInterface;
import com.strategyobject.substrateclient.rpc.annotation.RpcSubscription;
import com.strategyobject.substrateclient.rpc.api.BlockHash;
import com.strategyobject.substrateclient.rpc.api.BlockNumber;
import com.strategyobject.substrateclient.rpc.api.Header;
import com.strategyobject.substrateclient.rpc.api.SignedBlock;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@RpcInterface("chain")
public interface Chain {

    /**
     * Get header and body of a relay chain block.
     *
     * @return current signed block
     */
    @RpcCall("getBlock")
    CompletableFuture<SignedBlock> getBlock();

    /**
     * Get header and body of a relay chain block.
     *
     * @param hash hash of the wanted block, or empty for the latest block
     * @return signed block
     */
    @RpcCall("getBlock")
    CompletableFuture<SignedBlock> getBlock(BlockHash hash);

    /**
     * Get hash of the n-th block in the canon chain.
     *
     * @return block hash
     */
    @RpcCall("getBlockHash")
    CompletableFuture<BlockHash> getBlockHash();

    /**
     * Get hash of the n-th block in the canon chain.
     *
     * @param blockNumber number of wanted block
     * @return block hash
     */
    @RpcCall("getBlockHash")
    CompletableFuture<BlockHash> getBlockHash(BlockNumber blockNumber);

    /**
     * Get hash of the last finalized block in the canon chain.
     *
     * @return hash of the last finalized block
     */
    @RpcCall("getFinalizedHead")
    CompletableFuture<BlockHash> getFinalizedHead();

    /**
     * Retrieves the best header via subscription.
     *
     * @param callback callback handler
     * @return unsubscribe delegate
     */
    @RpcSubscription(type = "newHead", subscribeMethod = "subscribeNewHead", unsubscribeMethod = "unsubscribeNewHead")
    CompletableFuture<Supplier<CompletableFuture<Boolean>>> subscribeNewHeads(BiConsumer<Exception, Header> callback);
}
