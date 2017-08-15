package lsm.nio;

import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RandomAccessFileTest {

    @Test
    public void testRead() {
        RandomAccessFile randomAccessFile = null;
        int point = 10;
        try {
            randomAccessFile = new RandomAccessFile("D:\\nio\\read.txt","rw");
            //获取RandomAccessFile对象文件指针的位置，初始位置是0
            System.out.println("RandomAccessFile文件指针的初始位置:"+randomAccessFile.getFilePointer());
            randomAccessFile.seek(point);//移动文件指针位置
            byte[]  buff=new byte[1024];
            int hasRead=0;
            //循环读取
            while((hasRead = randomAccessFile.read(buff)) != -1){
                //打印读取的内容,并将字节转为字符串输入
                System.out.print(new String(buff,0,hasRead));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWrite() {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile("D:\\nio\\write.txt","rw");
            //将记录指针移动到文件最后
            randomAccessFile.seek(randomAccessFile.length());
            randomAccessFile.write((System.getProperty("line.separator") + "append to end").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                if(randomAccessFile != null){
                    randomAccessFile.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testInsert() {
        int point = 10;
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile("D:\\nio\\write.txt","rw");
            //会替换掉原来位置的内容，如果想在中间插入，只能用另外一个留记录后面的内容之后再写入
            randomAccessFile.seek(point);
            randomAccessFile.write((System.getProperty("line.separator") + "insert into").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                if(randomAccessFile != null){
                    randomAccessFile.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testMThreadWrite() {
        // 预分配文件所占的磁盘空间，磁盘中会创建一个指定大小的文件
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile("D:\\nio\\mThreadWrite.txt","rw");
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(() ->write(1024 * 1, "第一个字符串"));
        executorService.execute(() ->write(1024 * 2, "第二个字符串"));
        executorService.execute(() ->write(1024 * 3, "第三个字符串"));
        executorService.execute(() ->write(1024 * 4, "第四个字符串"));
        executorService.execute(() ->write(1024 * 5, "第五个字符串"));
        executorService.execute(() ->write(1024 * 6, "第六个字符串"));
        executorService.execute(() ->write(1024 * 7, "第七个字符串"));
        executorService.execute(() ->write(1024 * 8, "第八个字符串"));
        executorService.execute(() ->write(1024 * 9, "第九个字符串"));
        executorService.execute(() ->write(1024 * 10, "第十个字符串"));

    }

    private void write(int skip, String content) {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile("D:\\nio\\mThreadWrite.txt","rw");
            //将记录指针移动到文件最后
            randomAccessFile.seek(skip);
            randomAccessFile.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                if(randomAccessFile != null){
                    randomAccessFile.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
