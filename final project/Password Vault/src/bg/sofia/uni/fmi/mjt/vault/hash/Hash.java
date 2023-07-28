package bg.sofia.uni.fmi.mjt.vault.hash;

import bg.sofia.uni.fmi.mjt.vault.exception.HashCanNotBeCreatedException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    public static String makeHash(String input, String type, int length) {
        final int radix = 16;
        try {
            MessageDigest typeOfHash = MessageDigest.getInstance(type);
            byte[] messageDigest = typeOfHash.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hexHash = new StringBuilder(no.toString(radix));
            while (hexHash.length() < length) {
                hexHash.insert(0, '0');
            }
            return hexHash.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new HashCanNotBeCreatedException("Error while creating hash!");
        }
    }
}
