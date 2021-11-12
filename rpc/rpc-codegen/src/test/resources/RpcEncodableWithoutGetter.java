import com.strategyobject.substrateclient.rpc.core.annotations.RpcEncoder;
import com.strategyobject.substrateclient.rpc.core.annotations.Scale;

@RpcEncoder
public class RpcEncodableWithoutGetter<T> {
    public int a;

    @Scale
    public int b;

    public T c;

    public int getB() {
        return b;
    }

    public T getC() {
        return c;
    }

    public void setA(int a) {
        this.a = a;
    }

    public void setB(int b) {
        this.b = b;
    }

    public void setC(T c) {
        this.c = c;
    }
}
