package lsm.designMode.filterChain;

/**
 * Created by lishenming on 2017/3/11.
 */
public class Filter3 implements Filter {
    @Override
    public void handleString(String string) {

        System.out.println("filter3 do something");
    }
}
