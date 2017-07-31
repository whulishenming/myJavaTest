package lsm.designMode.decorator;

/**
 * Created by lishenming on 2017/3/11.
 * 抽象装饰角色“七十二变”
 */
public class Change implements TheGreatestSage {
    private TheGreatestSage sage;

    public Change(TheGreatestSage sage){
        this.sage = sage;
    }
    @Override
    public void move() {
        // 代码
        sage.move();
    }

}
