package com.strategyobject.substrateclient.api.pallet.msa;

import com.strategyobject.substrateclient.pallet.annotation.Event;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;


@Pallet("Msa")
public interface Msa {
/*
		MsaCreated {
			/// The MSA for the Event
			msa_id: MessageSourceId,
			/// The key added to the MSA
			key: T::AccountId,
		},
		*/
    @Event(index = 0)
    @Getter
    @Setter
    @ScaleReader
    class MsaCreated {
        @Scale(ScaleType.U64.class)
        private BigInteger msaId;
        private AccountId accountId;
    }
/*
		/// An AccountId has been associated with a MessageSourceId
		KeyAdded {
			/// The MSA for the Event
			msa_id: MessageSourceId,
			/// The key added to the MSA
			key: T::AccountId,
		},
		/// An AccountId had all permissions revoked from its MessageSourceId
		KeyRemoved {
			/// The key no longer approved for the associated MSA
			key: T::AccountId,
		},
		/// A delegation relationship was added with the given provider and delegator
		ProviderAdded {
			/// The Provider MSA Id
			provider: Provider,
			/// The Delegator MSA Id
			delegator: Delegator,
		},
		/// A Provider-MSA relationship was registered
		ProviderRegistered {
			/// The MSA id associated with the provider
			provider_msa_id: MessageSourceId,
		},
		/// The Delegator revoked its delegation to the Provider
		DelegatorRevokedDelegation {
			/// The Provider MSA Id
			provider: Provider,
			/// The Delegator MSA Id
			delegator: Delegator,
		},
		/// The Provider revoked itself as delegate for the Delegator
		ProviderRevokedDelegation {
			/// The Provider MSA Id
			provider: Provider,
			/// The Delegator MSA Id
			delegator: Delegator,
		},
 */
}
