package lsm.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author lishenming
 * @date 2018/11/2 10:12
 * RSA加密解密
 **/

@Slf4j
public class RSAUtils {

    /**
     * 使用私钥加密
     *
     * @param content
     * @param privateKey
     * @return
     */
    public static String sign(String content, String privateKey) {

        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            PrivateKey priKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            Signature signature = Signature.getInstance("SHA1WithRSA");

            signature.initSign(priKey);

            signature.update(content.getBytes("utf-8"));

            byte[] signed = signature.sign();

            return Base64.getEncoder().encodeToString(signed);
        } catch (Exception e) {

            log.error("[[title={}]] RSAUtils sign error, content={}", "RSAUtils", content, e);
        }

        return "";
    }

    /**
     * 使用公钥验签
     *
     * @param content
     * @param sign
     * @param publicKey
     * @return
     */
    public static boolean verify(String content, String sign, String publicKey) {
        try {

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            byte[] encodedKey = Base64.getDecoder().decode(publicKey);

            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            Signature signature = Signature.getInstance("SHA1WithRSA");

            signature.initVerify(pubKey);

            signature.update(content.getBytes("utf-8"));

            return signature.verify(Base64.getDecoder().decode(sign));

        } catch (Exception e) {
            log.error("[[title={}]] RSAUtils verify error, content={}, sign={}", "RSAUtils", content, sign, e);
        }

        return false;
    }

    /**
     * 获取密钥
     *
     * @param key
     * @return
     * @throws Exception
     */
    private static PrivateKey getPrivateKey(String key) throws Exception {

        try {
            byte[] keyBytes;

            keyBytes = Base64.getDecoder().decode(key);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {

            log.error("[[title={}]] RSAUtils getPrivateKey error, key={}", "RSAUtils", key, e);

            throw e;
        }
    }

    /**
     * 使用私钥解密
     *
     * @param content
     * @param privateKey
     * @return
     */
    public static String decrypt(String content, String privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));

            InputStream ins = new ByteArrayInputStream(Base64.getDecoder().decode(content));

            ByteArrayOutputStream writer = new ByteArrayOutputStream();

            // rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
            byte[] buf = new byte[128];
            int bufl;

            while ((bufl = ins.read(buf)) != -1) {
                byte[] block = null;

                if (buf.length == bufl) {
                    block = buf;
                } else {
                    block = new byte[bufl];

                    System.arraycopy(buf, 0, block, 0, bufl);
                }

                writer.write(cipher.doFinal(block));
            }

            return new String(writer.toByteArray(), "utf-8");
        } catch (Exception e) {

            log.error("[[title={}]] RSAUtils decrypt error, content={}", "RSAUtils", content, e);
        }

        return "";
    }
}
