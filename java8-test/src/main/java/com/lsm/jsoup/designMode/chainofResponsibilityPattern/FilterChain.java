package com.lsm.jsoup.designMode.chainofResponsibilityPattern;

import java.util.List;

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
