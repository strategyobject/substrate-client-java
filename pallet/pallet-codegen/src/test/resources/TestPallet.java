import com.strategyobject.substrateclient.common.types.Result;
import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.pallet.annotation.Storage;
import com.strategyobject.substrateclient.pallet.annotation.StorageHasher;
import com.strategyobject.substrateclient.pallet.annotation.StorageKey;
import com.strategyobject.substrateclient.pallet.storage.StorageNMap;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleGeneric;

@Pallet("Test")
public interface TestPallet {
    @Storage("Value")
    StorageNMap<Integer> value();

    @Storage(
            value = "Map",
            keys = {
                    @StorageKey(type = @Scale(ScaleType.I32.class),
                            hasher = StorageHasher.TWOX_64_CONCAT)
            })
    StorageNMap<Integer> map();

    @Storage(
            value = "DoubleMap",
            keys = {
                    @StorageKey(type = @Scale(ScaleType.I32.class),
                            hasher = StorageHasher.TWOX_64_CONCAT),
                    @StorageKey(
                            generic = @ScaleGeneric(
                                    template = "Result<Bool, String>",
                                    types = {
                                            @Scale(Result.class),
                                            @Scale(ScaleType.Bool.class),
                                            @Scale(ScaleType.String.class)
                                    }
                            ),
                            hasher = StorageHasher.BLAKE2_128_CONCAT
                    )
            })
    StorageNMap<AccountId> doubleMap();

    @Storage(
            value = "TripleMap",
            keys = {
                    @StorageKey(type = @Scale(String.class),
                            hasher = StorageHasher.BLAKE2_128_CONCAT),
                    @StorageKey(type = @Scale(ScaleType.I32.class),
                            hasher = StorageHasher.TWOX_64_CONCAT),
                    @StorageKey(type = @Scale(AccountId.class),
                            hasher = StorageHasher.IDENTITY)
            })
    StorageNMap<Integer> tripleMap();
}
