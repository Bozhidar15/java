package bg.sofia.uni.fmi.mjt.vault.crypting;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptAndDecrypt {
    static Cipher cipher;

    public static PasswordAndDecryptingKey encrypt(String plainText)
            throws Exception {
        final int keySize = 128;
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(keySize);
        SecretKey secretKey = keyGenerator.generateKey();
        cipher = Cipher.getInstance("AES");
        byte[] plainTextByte = plainText.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(plainTextByte);
        Base64.Encoder encoder = Base64.getEncoder();
        String encryptedText = encoder.encodeToString(encryptedByte);
        return new PasswordAndDecryptingKey(encryptedText, Base64.getEncoder().encodeToString(secretKey.getEncoded()));
    }

    public static String decrypt(PasswordAndDecryptingKey passwordAndDecryptingKey)
            throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(passwordAndDecryptingKey.decryptKey());
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        cipher = Cipher.getInstance("AES");
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(passwordAndDecryptingKey.password());
        cipher.init(Cipher.DECRYPT_MODE, originalKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        return new String(decryptedByte);
    }
}
