package lsm.designMode.filterChain;

import java.util.List;

/**
 * Created by lishenming on 2017/3/11.
 */
public class FilterChain {

    private List<Filter> filters;

    public FilterChain(List<Filter> filters) {
        this.filters = filters;
    }

    public void addFilter(Filter filter){
        filters.add(filter);
    }

    public void doFilter(String string){
        for (Filter filter : filters) {
            filter.handleString(string);
        }
    }

}
