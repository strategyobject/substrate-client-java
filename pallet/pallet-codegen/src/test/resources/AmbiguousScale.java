import com.strategyobject.substrateclient.pallet.annotations.Pallet;
import com.strategyobject.substrateclient.pallet.annotations.Storage;
import com.strategyobject.substrateclient.pallet.annotations.StorageHasher;
import com.strategyobject.substrateclient.pallet.annotations.StorageKey;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotations.Scale;
import com.strategyobject.substrateclient.scale.annotations.ScaleGeneric;
import com.strategyobject.substrateclient.storage.StorageNMap;

@Pallet("Test")
public interface AmbiguousScale {
    @Storage(value = "Test", keys = {
            @StorageKey(
                    hasher = StorageHasher.Blake2B128Concat,
                    type = @Scale(ScaleType.I32.class),
                    generic = @ScaleGeneric(
                            template = "Option<I32>",
                            types = {
                                    @Scale(ScaleType.Option.class),
                                    @Scale(ScaleType.I32.class)
                            }
                    )
            )
    })
    StorageNMap<String> test();
}
