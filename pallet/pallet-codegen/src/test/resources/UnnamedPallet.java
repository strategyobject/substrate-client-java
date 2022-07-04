import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.pallet.annotation.Storage;
import com.strategyobject.substrateclient.pallet.storage.StorageNMap;

@Pallet("")
public interface UnnamedPallet {
    @Storage(name = "Test")
    StorageNMap<Integer> test();
}
