import com.strategyobject.substrateclient.rpc.core.annotations.RpcDecoder;

@RpcDecoder
public interface RpcDecodableInterface {
    int getA();
    void setA(int value);
}
