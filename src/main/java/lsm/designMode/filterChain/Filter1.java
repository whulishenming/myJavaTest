package lsm.designMode.filterChain;

/**
 * Created by lishenming on 2017/3/11.
 */
public class Filter1 implements Filter {
    @Override
    public void handleString(String string) {

        System.out.println("filter1 do something");
    }
}
