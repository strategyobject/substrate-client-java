import com.strategyobject.substrateclient.rpc.annotation.RpcEncoder;
import com.strategyobject.substrateclient.scale.annotation.Scale;

@RpcEncoder
public class RpcEncodable<T> {
    public long a;

    @Scale
    public int b;

    public T c;

    public long getA() {
        return a;
    }

    public void setA(long a) {
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
