package lsm.designMode.decorator;

/**
 * Created by lishenming on 2017/3/11.
 * 具体装饰角色“鱼儿”
 */
public class Fish extends Change {

    public Fish(TheGreatestSage sage) {
        super(sage);
    }

    @Override
    public void move() {
        // 代码
        super.move();
        System.out.println("Fish Move");
    }
}
