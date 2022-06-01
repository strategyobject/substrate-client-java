import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.pallet.annotation.Storage;
import com.strategyobject.substrateclient.pallet.annotation.StorageHasher;
import com.strategyobject.substrateclient.pallet.annotation.StorageKey;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleGeneric;
import com.strategyobject.substrateclient.pallet.storage.StorageNMap;

@Pallet("Test")
public interface AmbiguousScale {
    @Storage(value = "Test", keys = {
            @StorageKey(
                    hasher = StorageHasher.BLAKE2_128_CONCAT,
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
