import com.strategyobject.substrateclient.pallet.annotations.Pallet;
import com.strategyobject.substrateclient.pallet.annotations.Storage;
import com.strategyobject.substrateclient.pallet.annotations.StorageHasher;
import com.strategyobject.substrateclient.pallet.annotations.StorageKey;
import com.strategyobject.substrateclient.rpc.types.AccountId;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotations.Scale;
import com.strategyobject.substrateclient.scale.annotations.ScaleGeneric;
import com.strategyobject.substrateclient.storage.StorageNMap;
import com.strategyobject.substrateclient.types.Result;

@Pallet("Test")
public interface TestPallet {
    @Storage("Value")
    StorageNMap<Integer> value();

    @Storage(
            value = "Map",
            keys = {
                    @StorageKey(type = @Scale(ScaleType.I32.class),
                            hasher = StorageHasher.TwoX64Concat)
            })
    StorageNMap<Integer> map();

    @Storage(
            value = "DoubleMap",
            keys = {
                    @StorageKey(type = @Scale(ScaleType.I32.class),
                            hasher = StorageHasher.TwoX64Concat),
                    @StorageKey(
                            generic = @ScaleGeneric(
                                    template = "Result<Bool, String>",
                                    types = {
                                            @Scale(Result.class),
                                            @Scale(ScaleType.Bool.class),
                                            @Scale(ScaleType.String.class)
                                    }
                            ),
                            hasher = StorageHasher.Blake2B128Concat
                    )
            })
    StorageNMap<AccountId> doubleMap();

    @Storage(
            value = "TripleMap",
            keys = {
                    @StorageKey(type = @Scale(String.class),
                            hasher = StorageHasher.Blake2B128Concat),
                    @StorageKey(type = @Scale(ScaleType.I32.class),
                            hasher = StorageHasher.TwoX64Concat),
                    @StorageKey(type = @Scale(AccountId.class),
                            hasher = StorageHasher.Identity)
            })
    StorageNMap<Integer> tripleMap();
}
