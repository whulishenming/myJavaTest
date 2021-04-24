package lsm.file;

import org.junit.Test;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Base64;

/**
 * Created by za-lishenming on 2017/5/25.
 */
public class FileTest {

    public String file2byte(String filePath) {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            return Base64.getEncoder().encodeToString(bos.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }

        return null;
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

    public String readFromTextFile(String pathname){

        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(new File(pathname)));
            BufferedReader br = new BufferedReader(reader);
            String line;

            StringBuilder stringBuilder = new StringBuilder();

            while((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }

            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Test
    public void testReadFromTextFile() {
        String s = readFromTextFile("/Users/lishenming/Downloads/app接口文档.txt");
        System.out.println( s);
    }

    @Test
    public void test() throws IOException {
        String buffer = file2byte("/Users/lishenming/Downloads/app接口文档.txt");

        byte2File(buffer, "/Users/lishenming/Downloads", "app接口文档2.txt");
    }

}
