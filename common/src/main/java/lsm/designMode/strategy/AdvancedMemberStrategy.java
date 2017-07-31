package lsm.designMode.strategy;

/**
 * Created by lishenming on 2017/3/11.
 * 高级会员折扣类
 */
public class AdvancedMemberStrategy implements MemberStrategy {

    @Override
    public double calcPrice(double booksPrice) {

        System.out.println("对于高级会员的折扣为20%");
        return booksPrice * 0.8;
    }
}
