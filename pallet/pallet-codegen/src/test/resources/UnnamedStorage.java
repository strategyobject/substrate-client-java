import com.strategyobject.substrateclient.pallet.annotations.Pallet;
import com.strategyobject.substrateclient.pallet.annotations.Storage;
import com.strategyobject.substrateclient.storage.StorageNMap;

@Pallet("Test")
public interface UnnamedStorage {
    @Storage("")
    StorageNMap<Integer> unnamed();
}
