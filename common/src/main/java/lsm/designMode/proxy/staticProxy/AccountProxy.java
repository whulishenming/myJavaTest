package lsm.designMode.proxy.staticProxy;

/**
 * Created by lishenming on 2017/3/11.
 * 代理类（增强AccountImpl的功能）
 */
public class AccountProxy implements IAccount {
    private AccountImpl accountImpl;

    /**
     * 重写默认构造函数
     * @param accountImpl :真正要执行业务的对象
     */
    public AccountProxy(AccountImpl accountImpl) {
        this.accountImpl =accountImpl;
    }

    @Override
    public void queryAccount() {
        System.out.println("业务处理之前需要做的如：事务管理 记录log...");
        // 调用委托类的方法，这是具体的业务方法
        accountImpl.queryAccount();
        System.out.println("业务处理之后...");
    }

    @Override
    public void updateAccount() {
        System.out.println("业务处理之前需要做的如：事务管理 记录log...");
        // 调用委托类的方法;
        accountImpl.updateAccount();
        System.out.println("业务处理之后...");
    }
}
