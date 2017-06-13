package lsm.concurrent.callAble;

import java.util.concurrent.Callable;

/**
 * Created by lishenming on 2017/3/12.
 */
public class MyTask implements Callable<String> {

    private int flag = 0;

    public MyTask(int flag){

        this.flag = flag;

    }

    public String call() throws Exception{

        if (this.flag == 0){

            return "flag = 0";

        }

        if (this.flag == 1){

            try {

                while (true) {

                    System.out.println("looping.");

                    Thread.sleep(2000);

                }

            } catch (InterruptedException e) {

                System.out.println("Interrupted");

            }

            return "false";

        } else {

            throw new Exception("Bad flag value!");

        }

    }
}
