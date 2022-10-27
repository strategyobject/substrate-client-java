package com.strategyobject.substrateclient.api.pallet.msa;

import com.strategyobject.substrateclient.pallet.annotation.*;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import java.math.BigInteger;

@Pallet("Msa")
public interface Msa {
    /**
     * A new Message Service Account was created with a new MessageSourceId
     */
    @Event(index = 0)
    @ScaleReader
    class MsaCreated {
        @Scale(ScaleType.U64.class)
        private BigInteger msaId;
        private AccountId key;

        public BigInteger getMsaId() {
            return msaId;
        }

        public AccountId getKey() {
            return key;
        }

        public void setMsaId(BigInteger msaId) {
            this.msaId = msaId;
        }

        public void setKey(AccountId key) {
            this.key = key;
        }
    }

    /**
     * An AccountId has been associated with a MessageSourceId
     */
    @Event(index = 1)
    @ScaleReader
    class PublicKeyAdded {
        @Scale(ScaleType.U64.class)
        private BigInteger msaId;
        private AccountId key;

        public BigInteger getMsaId() {
            return msaId;
        }

        public AccountId getKey() {
            return key;
        }

        public void setMsaId(BigInteger msaId) {
            this.msaId = msaId;
        }

        public void setKey(AccountId key) {
            this.key = key;
        }
    }

    /**
     * An AccountId had all permissions revoked from its MessageSourceId
     */
    @Event(index = 2)
    @ScaleReader
    class PublicKeyDeleted {
        private AccountId key;

        public AccountId getKey() {
            return key;
        }

        public void setKey(AccountId key) {
            this.key = key;
        }
    }

    /**
     * A delegation relationship was added with the given provider and delegator
     */
    @Event(index = 3)
    @ScaleReader
    class DelegationGranted {
        @Scale(ScaleType.U64.class)
        private BigInteger provider;
        @Scale(ScaleType.U64.class)
        private BigInteger delegator;

        public BigInteger getProvider() {
            return provider;
        }

        public BigInteger getDelegator() {
            return delegator;
        }

        public void setProvider(BigInteger provider) {
            this.provider = provider;
        }

        public void setDelegator(BigInteger delegator) {
            this.delegator = delegator;
        }
    }

    /**
     * A Provider-MSA relationship was registered
     */
    @Event(index = 4)
    @ScaleReader
    class ProviderCreated {
        @Scale(ScaleType.U64.class)
        private BigInteger providerMsaId;

        public BigInteger getProviderMsaId() {
            return providerMsaId;
        }

        public void setProviderMsaId(BigInteger providerMsaId) {
            this.providerMsaId = providerMsaId;
        }
    }

    /**
     * The Delegator revoked its delegation to the Provider
     */
    @Event(index = 5)
    @ScaleReader
    class DelegationRevoked {
        @Scale(ScaleType.U64.class)
        private BigInteger provider;
        @Scale(ScaleType.U64.class)
        private BigInteger delegator;

        public BigInteger getProvider() {
            return provider;
        }

        public BigInteger getDelegator() {
            return delegator;
        }

        public void setProvider(BigInteger provider) {
            this.provider = provider;
        }

        public void setDelegator(BigInteger delegator) {
            this.delegator = delegator;
        }
    }

    /**
     * The MSA has been retired.
     */
    @Event(index = 6)
    @ScaleReader
    class MsaRetired {
        @Scale(ScaleType.U64.class)
        private BigInteger msaId;

        public BigInteger getMsaId() {
            return msaId;
        }

        public void setMsaId(BigInteger msaId) {
            this.msaId = msaId;
        }
    }
}
