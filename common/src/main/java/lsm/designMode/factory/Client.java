package lsm.designMode.factory;

/**
 * Created by lishenming on 2017/3/12.
 */
public class Client {
    public static void main(String[]args){
        //创建装机工程师对象
        ComputerEngineer cf = new ComputerEngineer();
        //客户选择并创建需要使用的产品对象
        AbstractFactory af = new AmdFactory();
        //告诉装机工程师自己选择的产品，让装机工程师组装电脑
        cf.makeComputer(af);
    }
}
