import com.strategyobject.substrateclient.pallet.annotations.Pallet;
import com.strategyobject.substrateclient.pallet.annotations.Storage;
import com.strategyobject.substrateclient.pallet.annotations.StorageHasher;
import com.strategyobject.substrateclient.pallet.annotations.StorageKey;
import com.strategyobject.substrateclient.storage.StorageNMap;

@Pallet("Test")
public interface WithoutScale {
    @Storage(value = "Test", keys = {
            @StorageKey(hasher = StorageHasher.Blake2B128Concat)
    })
    StorageNMap<String> test();
}
