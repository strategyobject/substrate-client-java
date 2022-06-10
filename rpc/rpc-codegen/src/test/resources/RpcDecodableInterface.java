import com.strategyobject.substrateclient.rpc.annotation.RpcDecoder;

@RpcDecoder
public interface RpcDecodableInterface {
    int getA();
    void setA(int value);
}
