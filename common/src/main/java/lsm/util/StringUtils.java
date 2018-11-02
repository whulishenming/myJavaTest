package lsm.util;

/**
 * @author lishenming
 * @create 2017-11-09
 **/

public class StringUtils {

    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0){
            return true;
        }
        return false;
    }
}
