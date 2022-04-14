import com.strategyobject.substrateclient.pallet.annotations.Pallet;
import com.strategyobject.substrateclient.pallet.annotations.Storage;
import com.strategyobject.substrateclient.storage.StorageNMap;

@Pallet("")
public interface UnnamedPallet {
    @Storage("Test")
    StorageNMap<Integer> test();
}
