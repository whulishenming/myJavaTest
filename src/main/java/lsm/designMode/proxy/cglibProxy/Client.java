package lsm.designMode.proxy.cglibProxy;

/**
 * Created by lishenming on 2017/3/11.
 * 指定的目标类生成一个子类，并覆盖其中方法实现增强。但是也有一点点不足
 * 因为采用的是继承，所以不能对final修饰的类进行代理
 */
public class Client {
    public static void main(String[] args) {
        //实例化代理
        AccountProxy cglib=new AccountProxy();
        //通过代理拿到对象
        Account account = (Account)cglib.getInstance(new Account());
        account.queryAccount();
    }
}
