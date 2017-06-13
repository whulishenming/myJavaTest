package lsm.designMode.strategy;

/**
 * Created by lishenming on 2017/3/11.
 * 抽象折扣类
 */
public interface MemberStrategy {
    /**
     * 计算图书的价格
     * @param booksPrice    图书的原价
     * @return    计算出打折后的价格
     */
    double calcPrice(double booksPrice);
}
