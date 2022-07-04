import com.strategyobject.substrateclient.pallet.annotation.Pallet;
import com.strategyobject.substrateclient.pallet.annotation.Storage;
import com.strategyobject.substrateclient.pallet.storage.StorageNMap;

@Pallet("Class")
public abstract class ClassPallet {
    @Storage(name = "Test")
    public abstract StorageNMap<Integer> test();
}
