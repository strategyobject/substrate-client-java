package com.strategyobject.substrateclient.crypto.ss58;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
@EqualsAndHashCode
public class SS58AddressFormat {
    private final short prefix;

    /**
     * Polkadot Relay-chain, standard account (*25519).
     */
    public static final SS58AddressFormat POLKADOT_ACCOUNT = new SS58AddressFormat((short) 0);

    /**
     * Bare 32-bit Schnorr/Ristretto 25519 (S/R 25519) key.
     */
    public static final SS58AddressFormat BARE_SR_25519 = new SS58AddressFormat((short) 1);

    /**
     * Kusama Relay-chain, standard account (*25519).
     */
    public static final SS58AddressFormat KUSAMA_ACCOUNT = new SS58AddressFormat((short) 2);

    /**
     * Bare 32-bit Edwards Ed25519 key.
     */
    public static final SS58AddressFormat BARE_ED_25519 = new SS58AddressFormat((short) 3);

    /**
     * Any Substrate network, standard account (*25519).
     */
    public static final SS58AddressFormat SUBSTRATE_ACCOUNT = new SS58AddressFormat((short) 42);
}
