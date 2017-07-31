package lsm.interview.test;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lishenming on 2017/3/25.
 */
public class TestNo17 {
    public static void main(String[] args) {
        /**
         *  public FileOutputStream(lsm.String name);创建一个具有指定名称的文件中写入数据的输出文件流
         *  public FileOutputStream(lsm.String name,boolean append)：创建一个向具有指定name的文件中写入数据的输出文件流。如果第二个参数为true，则将字节写入文件末尾处，而不是写入文件开始处
         *  public FileOutputStream(File file)：创建一个向指定File对象表示的文件中写入数据的文件输出流
         *  public FileOutputStream(File file,boolean appended)：创建一个向指定File对象表示的文件中写入数据的文件文件输出流。如果第二个参数为true，则将字节写入文件末尾处，而不是写入文件开始处。
         */
        FileOutputStream fileOutputStream = null;
        try {
            String s = "ABCDE";
            byte[] b = s.getBytes();
             fileOutputStream = new FileOutputStream("text.txt", true);
            fileOutputStream.write(b);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
