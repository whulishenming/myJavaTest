package lsm.designMode.filterChain;

/**
 * Created by lishenming on 2017/3/11.
 */
public class Filter2 implements Filter {
    @Override
    public void handleString(String string) {

        System.out.println("filter2 do something");
    }
}
