package lsm.designMode.decorator;

/**
 * Created by lishenming on 2017/3/11.
 * 具体装饰角色“鸟儿”
 */
public class Bird extends Change {

    public Bird(TheGreatestSage sage) {
        super(sage);
    }

    @Override
    public void move() {
        // 代码
        super.move();
        System.out.println("Bird Move");
    }
}
