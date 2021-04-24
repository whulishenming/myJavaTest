package lsm.jvm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019-07-22 23:02
 **/

public class OOMTest {

    public void stackOverFlowMethod(){
        stackOverFlowMethod();
    }

    @Test
    public void testStackOverFlowMethod() {
        stackOverFlowMethod();
    }

    @Test
    public void testPermGenSpace() {
        List<String> list = new ArrayList<>();
        while(true){
            list.add(UUID.randomUUID().toString().intern());
        }
    }

}
