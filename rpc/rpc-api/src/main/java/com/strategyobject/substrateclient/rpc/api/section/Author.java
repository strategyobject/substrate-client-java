package com.strategyobject.substrateclient.rpc.api.section;

import com.strategyobject.substrateclient.common.types.Bytes;
import com.strategyobject.substrateclient.rpc.annotation.RpcCall;
import com.strategyobject.substrateclient.rpc.annotation.RpcInterface;
import com.strategyobject.substrateclient.rpc.annotation.RpcSubscription;
import com.strategyobject.substrateclient.rpc.api.Extrinsic;
import com.strategyobject.substrateclient.rpc.api.ExtrinsicStatus;
import com.strategyobject.substrateclient.rpc.api.Hash;
import com.strategyobject.substrateclient.scale.annotation.Scale;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@RpcInterface("author")
public interface Author {

    /**
     * Checks if the keystore has private keys for the given public key and key type.
     *
     * @param publicKey public key
     * @param keyType   type of key
     * @return `true` if a private key could be found
     */
    @RpcCall("hasKey")
    CompletableFuture<Boolean> hasKey(Bytes publicKey, String keyType);

    /**
     * Insert a key into the keystore.
     *
     * @param keyType   type of key
     * @param secretUri secret uri
     * @param publicKey public key
     * @return void
     */
    @RpcCall("insertKey")
    CompletableFuture<Void> insertKey(String keyType, String secretUri, Bytes publicKey);

    /**
     * Submit an extrinsic to watch.
     *
     * @param extrinsic extrinsic to submit
     * @param callback  callback handler
     * @return unsubscribe delegate
     */
    @RpcSubscription(type = "extrinsicUpdate", subscribeMethod = "submitAndWatchExtrinsic", unsubscribeMethod = "unwatchExtrinsic")
    CompletableFuture<Supplier<CompletableFuture<Boolean>>> submitAndWatchExtrinsic(@Scale Extrinsic<?, ?, ?, ?> extrinsic,
                                                                                    BiConsumer<Exception, ExtrinsicStatus> callback);

    /**
     * Submit hex-encoded extrinsic for inclusion in block.
     *
     * @param extrinsic extrinsic to submit
     * @return hash of extrinsic
     */
    @RpcCall("submitExtrinsic")
    CompletableFuture<Hash> submitExtrinsic(@Scale Extrinsic<?, ?, ?, ?> extrinsic);
}
