import com.strategyobject.substrateclient.pallet.annotations.Pallet;
import com.strategyobject.substrateclient.pallet.annotations.Storage;
import com.strategyobject.substrateclient.pallet.annotations.StorageHasher;
import com.strategyobject.substrateclient.pallet.annotations.StorageKey;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotations.Scale;

@Pallet("Test")
public interface NotAStorage {
    @Storage(value = "Test", keys = {
            @StorageKey(
                    hasher = StorageHasher.Blake2B128Concat,
                    type = @Scale(ScaleType.I32.class)
            )
    })
    String test();
}
