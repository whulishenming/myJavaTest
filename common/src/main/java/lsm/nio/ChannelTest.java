package lsm.nio;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
public class ChannelTest {

    @Test
    public void testFileChannel() {
        RandomAccessFile fromFile = null;
        RandomAccessFile toFile = null;

        try {
            // 打开FileChannel
            fromFile = new RandomAccessFile("D:\\nio\\from.txt","rw");
            FileChannel fromChannel = fromFile.getChannel();
            // 从FileChannel读取数据
            ByteBuffer readBuffer = ByteBuffer.allocate(24);
            StringBuilder stringBuilder = new StringBuilder();

            while (fromChannel.read(readBuffer) != -1){
                readBuffer.flip();
                while(readBuffer.hasRemaining()) {
                    stringBuilder.append((char)readBuffer.get());
                }
                readBuffer.compact();
            }

            byte[] bytes = stringBuilder.toString().getBytes();

            int channelSize = (int) fromChannel.size();
            int stringLength = stringBuilder.length();
            // channelSize, stringLength, bytes.length 中由于字符编码的问题，比如汉字等等， bytes.length是真正需要的BufferSize
            log.info("channelSize:{}, stringLength:{}, bytesLength:{}", channelSize, stringLength, bytes.length);

            // 向FileChannel写数据
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.clear();
            writeBuffer.put(bytes);
            writeBuffer.flip();
            toFile = new RandomAccessFile("D:\\nio\\to.txt","rw");
            FileChannel toChannel = toFile.getChannel();
            while(writeBuffer.hasRemaining()) {
                toChannel.write(writeBuffer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                if(fromFile != null){
                    fromFile.close();
                }
                if(toFile != null){
                    toFile.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    /**
     * 在Java NIO中，如果两个通道中有一个是FileChannel，那你可以直接将数据从一个 channel 传输到另外一个channel
     */
    @Test
    public void testFileChannelTransfer() {
        RandomAccessFile fromFile = null;
        RandomAccessFile toFile = null;

        try {
            // 打开FileChannel
            fromFile = new RandomAccessFile("D:\\nio\\from.txt", "rw");
            FileChannel fromChannel = fromFile.getChannel();

            toFile = new RandomAccessFile("D:\\nio\\to.txt", "rw");
            FileChannel toChannel = toFile.getChannel();
            fromChannel.transferTo(0L, fromChannel.size(), toChannel);
//            toChannel.transferFrom(fromChannel, 0L, fromChannel.size());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fromFile != null) {
                    fromFile.close();
                }
                if (toFile != null) {
                    toFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
