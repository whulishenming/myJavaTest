package lsm.encoder;

import org.junit.Test;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by shenming.li on 2017/6/15.
 */
public class MD5Test {

    @Test
    public void EncoderByMd5() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String str = "admin";
        //确定计算方法
        MessageDigest md5=MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
        String encoderStr=base64en.encode(md5.digest(str.getBytes("utf-8")));
        System.out.println(encoderStr);
    }
}
