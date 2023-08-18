package com.strategyobject.substrateclient.rpc.metadata;


import com.strategyobject.substrateclient.crypto.ss58.SS58AddressFormat;

public interface MetadataProvider {
    SS58AddressFormat getSS58AddressFormat();

    PalletCollection getPallets();


}
