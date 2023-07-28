package bg.sofia.uni.fmi.mjt.vault.crypting;

import bg.sofia.uni.fmi.mjt.vault.exception.EncryptDecryptException;

import java.io.Serializable;

public record PasswordAndDecryptingKey(String password, String decryptKey) implements Serializable {
    @Override
    public String toString() {
        try {
            return "password='" + EncryptAndDecrypt.decrypt(this);
        } catch (Exception e) {
            throw new EncryptDecryptException("Error while decrypting your password!");
        }
    }
}
