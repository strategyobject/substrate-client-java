import com.strategyobject.substrateclient.rpc.annotation.RpcDecoder;
import com.strategyobject.substrateclient.rpc.annotation.Scale;

@RpcDecoder
public class RpcDecodableWithoutSetter<T> {
    public int a;

    @Scale
    public int b;

    public T c;

    public void setB(int b) {
        this.b = b;
    }

    public void setC(T c) {
        this.c = c;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public T getC() {
        return c;
    }
}
