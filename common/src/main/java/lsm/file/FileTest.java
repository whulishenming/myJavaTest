package lsm.file;

import org.junit.Test;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * Created by za-lishenming on 2017/5/25.
 */
public class FileTest {

    public String file2byte(String filePath) {
        String buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            BASE64Encoder encoder = new BASE64Encoder();
            buffer = encoder.encode(bos.toByteArray());// 返回Base64编码过的字节数组字符串

        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


    public void byte2File(String buffer, String filePath, String fileName) {
        BASE64Decoder decoder = new BASE64Decoder();
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && !dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            byte[] buf = decoder.decodeBuffer(buffer);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void test(){
        String buffer = file2byte("C:\\Users\\za-lishenming\\Downloads\\P441704001774.tiff");

        byte2File(buffer, "d:\\Users\\za-lishenming\\Downloads", "33.tiff");
    }

    public void test2() {

    }
}
