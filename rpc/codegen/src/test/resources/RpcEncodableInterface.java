import com.strategyobject.substrateclient.rpc.core.annotations.RpcEncoder;

@RpcEncoder
public interface RpcEncodableInterface {
    int getA();
    void setA(int value);
}
