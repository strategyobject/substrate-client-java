import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.pallet.annotation.Storage;
import com.strategyobject.substrateclient.pallet.annotation.StorageHasher;
import com.strategyobject.substrateclient.pallet.annotation.StorageKey;
import com.strategyobject.substrateclient.pallet.storage.StorageNMap;

@Pallet("Test")
public interface WithoutScale {
    @Storage(name = "Test", keys = {
            @StorageKey(hasher = StorageHasher.BLAKE2_128_CONCAT)
    })
    StorageNMap<String> test();
}
