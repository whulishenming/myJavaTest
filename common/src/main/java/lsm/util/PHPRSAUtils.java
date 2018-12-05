package lsm.util;

import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

/**
 * @author lishenming
 * @date 2018/11/19 17:36
 **/
public class PHPRSAUtils {

    public static String encrypt(String publicKey, String content) throws Exception{

        KeyFactory keyFactory= KeyFactory.getInstance("RSA");

        X509EncodedKeySpec keySpec= new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));

        Cipher cipher= Cipher.getInstance("RSA/ECB/PKCS1Padding");

        cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePublic(keySpec));

        byte[] output= cipher.doFinal(content.getBytes());

        return Base64.getEncoder().encodeToString(output);
    }

    public static String decrypt(String privateKey, String content) throws Exception{

        PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));

        KeyFactory keyFactory= KeyFactory.getInstance("RSA");

        Cipher cipher= Cipher.getInstance("RSA/ECB/PKCS1Padding");

        cipher.init(Cipher.DECRYPT_MODE, keyFactory.generatePrivate(keySpec));

        byte[] output= cipher.doFinal(Base64.getDecoder().decode(content));

        return new String(output);

    }

    public static void main(String[] args) throws Exception{

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBqnzIvzr4HlEyAeAEVJNapG8n5jJGtTQrQomPtpTvjp5jo5wJn8lKgz8a6u4poWafhFYI9joau2PtuvmCa6QGNN27ag2a4cdnT+nbgSLKK8tyuslEpVaeJVB2/+za1iCoWphv4YAY2VY4I++Q2dMqppldAgx8ua3+px718hMauQIDAQAB";

        String encode = encrypt(publicKey,"ctrip-release&xTllaoQPxaiH");

        System.out.println("加密后结果："+encode);

        String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDGkwduUuPBrkjNT7FO82anqWh6Qb4efdDquSRVhpTN+g+9pFnp20YD/mSx+N4UpYbqWpW0t90f8Ej+wbpn9ryC4jKfrhQpb8mxMZUdK7lQt3mxykHERbRp0C57W5tJeBggVFZq93hinCmRYMScMy1t3nSsEbZG9q+y4YrUnrxRrHBEVdascZrMUrcMaCFYt3OMEa3zpsasGOa7IpO5RPG0vqas420BXfNkUOqArLLIXJx/KFyS20AkxL9vaem8WsF5kapY8a3G0tOVMBZTJiDDh/JdiGhcAcHdM/jrE2qM+Ip7nemoIrhjPrpiqnzcfEqGprm7OCRG4r5+3yWhEcj3AgMBAAECggEACK5Kx97lyATHd/pA5ROs7C1yvM188zZK2MgJN5fvj2WcJI03xQpp8CjB3CnmA38M+9fD7aeQCiJl/oUXkg3uRcZ/sg1ZIkqZxZ5GR4Uuz8GFl2khIORvM6T/MzNZAYg3fcpSENEKuTzccFeLoMx38vKjpRYNIuML4UUM3aim0OcmNmsisUOhT6XKlMEeuYIxrBzw9DIcSAlbMbLmi3f6DEB7zUrwOB0pPcJav90OfeNy/ocJ1Ioe61Rv3QrgsduWtTaBD2MN+8j/E7VQsOOa7kKAV438RypfHxPNBz3R6nE0uiD2eIqVX3FZIaceDFErMwbZ2mB0Ruki/d9DjSCNYQKBgQDj47BlaUbBgH0LKofFXpJpAbqsw5afagxkmhOrpbdSOYoaISJmu3y66Gz2JW2Q2/Ko3++eMN7J96VS+9fWsH5kQIAs8FNBr9/unQUaVdF4dRc3u0JTKBYikw8zNClZul40IirTWDHW9Da/5IX4/TAUFX/bbZXKJUr2+LZaDd9JBwKBgQDfEaA//hFU2S8YOGXmsCqngidfcbuU4e+gU3IRahdFkghQV1OFD+A0vPn6xTz0zfMIFg9XN15aWMkAM1kXAGXrUF7vhhhycreqM1/vjRNEJv8bIanoEL8CQQlUxFS9nEO85fEzqi8UL0HUKOBdUl3b0b1DKfRSJWXtrXPQohw0kQKBgQC3xDPKk0prX6F2D6XMkqad5rcR+ebMRkgYF+ck000xERU8XNvQ2/+I+URkeWoRFkxnwa0Ic4A0fW7jrBIxHcHONfUmR+XbYmfBkv06b1mrOUCvwGGLjRfMaaooMDfoE1FD2QmPw97s56H+u9kNH55Vk8C2wXe4urnozka999HGoQKBgQCriyOAcsMjTA3yg1SiDjIPNYFP3efSe9nKosFi8QJKhx4Losxg1K+n/vgCyHGzFTpjqotLmBHgSszwcYx+qti2GM2ZR7c/NEdYBUAuuX/qdk7Sr5W5GD+73QXvATjlw+k3uGFASQ0BacInBPRd7/ysEmSI5Re1oHr9R9l7xV8JcQKBgQCBbjcfxVFooisnPfdBhhvm0W/0tdqTPr7YRCAgIujY8Z/qVU/l5MuI1QLQzGkxlJ0svf4j4u7fsRDn7lPgq9JW1dUvcQh8jlxeKsaVP1qtBVbYMsfY/14NqlBtr1fCL6l4jg5ldQooOMDee0MjSNqYbezlYK3fYPkmCkww4ZnRdg==";

        String plainText = decrypt(privateKey,encode);

        System.out.println("解密后结果："+plainText);
    }
}
