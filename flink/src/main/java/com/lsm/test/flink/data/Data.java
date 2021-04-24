package com.lsm.test.flink.data;

import java.util.Arrays;
import java.util.List;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/15 14:24
 **/

public interface Data {

    List<Integer> dataList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    List<String> wordList = Arrays.asList("hello word", "hello java", "hello bigdata", "flink for java");

}
