package lsm.designMode.proxy.staticProxy;

/**
 * Created by lishenming on 2017/3/11.
 * 静态代理模式
 */
public class Client {
    public static void main(String[] args) {
        AccountImpl accountImpl = new AccountImpl();
        //在这里传入要调用的业务对象
        AccountProxy accountProxy = new AccountProxy(accountImpl);
        //开始调用业务对象的方法，这两个方法都被增强了。
        accountProxy.updateAccount();
        accountProxy.queryAccount();
    }
}
