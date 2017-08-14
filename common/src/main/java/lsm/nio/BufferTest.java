package lsm.nio;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 1. Buffer的分配
 *      要想获得一个Buffer对象首先要进行分配, 每一个Buffer类都有一个allocate方法, ByteBuffer buf = ByteBuffer.allocate(1024);
 * 2. 向Buffer中写数据
 *      从Channel写到Buffer: int bytesRead = fileChannel.read(buf);
 *      通过Buffer的put()方法写到Buffer里: buf.put("xixixi".getBytes());
 * 3. flip()方法
 *      flip方法将Buffer从写模式切换到读模式。调用flip()方法会将position设回0，并将limit设置成之前position的值
 * 4. 从Buffer中读取数据
 *      从Buffer读取数据到Channel: int bytesWritten = inChannel.write(buf);
 *      使用get()方法从Buffer中读取数据: char c = (char)buf.get();
 * 5. clear()与compact()方法
 *      a. 一旦读完Buffer中的数据，需要让Buffer准备好再次被写入。可以通过clear()或compact()方法来完成
 *          如果调用的是clear()方法，position将被设回0，limit被设置成 capacity的值。换句话说，Buffer 被清空了。Buffer中的数据并未清除，只是这些标记告诉我们可以从哪里开始往Buffer里写数据
 *          如果Buffer中有一些未读的数据，调用clear()方法，数据将“被遗忘”，意味着不再有任何标记会告诉你哪些数据被读过，哪些还没有
 *      b. 如果Buffer中仍有未读的数据，且后续还需要这些数据，但是此时想要先先写些数据，那么使用compact()方法
 *          compact()方法将所有未读的数据拷贝到Buffer起始处。然后将position设到最后一个未读元素正后面。
 *          limit属性依然像clear()方法一样，设置成capacity。现在Buffer准备好写数据了，但是不会覆盖未读的数据。
 */
@Slf4j
public class BufferTest {

    @Test
    public void testByteBuffer() {
        RandomAccessFile aFile = null;
        try{
            aFile = new RandomAccessFile("C:\\Users\\lishe\\Desktop\\1111.txt","rw");
            FileChannel fileChannel = aFile.getChannel();
            ByteBuffer buf = ByteBuffer.allocate(1024);

            buf.put("xixixi".getBytes());
            // read into buffer, every time 1024 bytes
            int bytesRead = fileChannel.read(buf);
            System.out.println(bytesRead);

            while(bytesRead != -1)
            {
                // make buffer ready for read
                buf.flip();
                while(buf.hasRemaining())
                {
                    // read 1 byte at a time
                    System.out.print((char)buf.get());
                }

                //
                /**
                 * 清空缓冲区，让它可以再次被写入
                 * 有两种方式能清空缓冲区
                 *  1. buf.compact(); compact()方法只会清除已经读过的数据
                 *  2. buf.clear(); clear()方法会清空整个缓冲区
                 */
                buf.compact();
//                buf.clear();
                bytesRead = fileChannel.read(buf);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            try{
                if(aFile != null){
                    aFile.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
