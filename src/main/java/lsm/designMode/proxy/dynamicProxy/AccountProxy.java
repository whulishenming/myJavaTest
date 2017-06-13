package lsm.designMode.proxy.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by lishenming on 2017/3/11.
 */
public class AccountProxy implements InvocationHandler {
    private Object target;
    /**
     * 绑定委托对象并返回一个代理类
     * @return
     * @param target
     */
    public Object getInstance(Object target) {
        this.target = target;
        //取得代理对象
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }


    /**
     * 调用方法
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        Object result=null;
        System.out.println("before-各种代理操作");
        //执行方法
        result=method.invoke(target, args);
        System.out.println("after");
        return result;
    }
}
