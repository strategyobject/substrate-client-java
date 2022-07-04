import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.pallet.annotation.Storage;
import com.strategyobject.substrateclient.pallet.storage.StorageNMap;

@Pallet("Test")
public interface UnnamedStorage {
    @Storage(name = "")
    StorageNMap<Integer> unnamed();
}
