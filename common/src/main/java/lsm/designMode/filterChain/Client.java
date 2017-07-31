package lsm.designMode.filterChain;

import java.util.ArrayList;

/**
 * Created by lishenming on 2017/3/11.
 * 责任链模式
 */
public class Client {
    public static void main(String[] args) {

        FilterChain filterChain = new FilterChain(new ArrayList<>());
        filterChain.addFilter(new Filter1());
        filterChain.addFilter(new Filter2());
        filterChain.addFilter(new Filter3());
        filterChain.addFilter(new Filter2());
        filterChain.doFilter("");
    }
}
