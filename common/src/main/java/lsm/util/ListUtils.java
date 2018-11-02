package lsm.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lishenming
 * @create 2017-11-09
 **/

public class ListUtils {

    public static <T>  List<T> removeDuplicate(List<T> list){
        HashSet<T> h = new HashSet<>(list);

        list.clear();
        list.addAll(h);

        return list;
    }

    public static <T>  List<T> removeDuplicate2(List<T> list){

        return new ArrayList<>(new HashSet<>(list));
    }
}
