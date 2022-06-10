import com.strategyobject.substrateclient.rpc.annotation.RpcEncoder;

@RpcEncoder
public interface RpcEncodableInterface {
    int getA();
    void setA(int value);
}
