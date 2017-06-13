package lsm.designMode.proxy.cglibProxy;

/**
 * Created by lishenming on 2017/3/11.
 * 接口实现类(包含业务逻辑) 即：委托类
 */
public class Account{

    public void queryAccount() {
        System.out.println("查询方法...");
    }

    public void updateAccount() {
        System.out.println("修改方法...");
    }
}
