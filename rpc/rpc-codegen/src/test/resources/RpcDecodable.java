import com.strategyobject.substrateclient.rpc.annotation.RpcDecoder;
import com.strategyobject.substrateclient.scale.annotation.Scale;

@RpcDecoder
public class RpcDecodable<T> {
    public int a;

    @Scale
    public int b;

    public T c;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public T getC() {
        return c;
    }

    public void setC(T c) {
        this.c = c;
    }
}
