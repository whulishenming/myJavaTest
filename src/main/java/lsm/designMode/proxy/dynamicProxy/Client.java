package lsm.designMode.proxy.dynamicProxy;

/**
 * Created by lishenming on 2017/3/11.
 * 动态代理  使用java反射
 */
public class Client {
    public static void main(String[] args) {
        AccountProxy proxy = new AccountProxy();
        // 在这里进行真正的对象传入
        IAccount account= (IAccount )proxy.getInstance(new AccountImpl());
        account.queryAccount();
    }
}
