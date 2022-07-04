import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.pallet.annotation.Storage;
import com.strategyobject.substrateclient.pallet.annotation.StorageHasher;
import com.strategyobject.substrateclient.pallet.annotation.StorageKey;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;

@Pallet("Test")
public interface NotAStorage {
    @Storage(name = "Test", keys = {
            @StorageKey(
                    hasher = StorageHasher.BLAKE2_128_CONCAT,
                    type = @Scale(ScaleType.I32.class)
            )
    })
    String test();
}
