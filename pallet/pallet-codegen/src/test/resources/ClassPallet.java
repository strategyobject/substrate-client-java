import com.strategyobject.substrateclient.pallet.annotations.Pallet;
import com.strategyobject.substrateclient.pallet.annotations.Storage;
import com.strategyobject.substrateclient.storage.StorageNMap;

@Pallet("Class")
public abstract class ClassPallet {
    @Storage("Test")
    public abstract StorageNMap<Integer> test();
}
