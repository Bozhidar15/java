package bg.sofia.uni.fmi.mjt.vault.api;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    public static void main(String [] args) throws URISyntaxException {
//        String type1 = "MD5";
//        String type2 = "SHA-1";
//        int length1 = 32;
//        System.out.println(makeHash("hello world", type1, length1));
//        System.out.println(makeHash("hello world", type2, length1));
//        String type3 = "SHA-256";
//        int length2 = 64;
//        System.out.println(makeHash("hello world", type3, length2));

        System.out.printf(Exposure.findResults(new URI("https://api.enzoic.com/passwords?sha1=2aae6c35c94fcfb415dbe95f408b9ce91ee846ed&md5=5eb63bbbe01eeed093cb22bb8f5acdc3&sha256=b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9")).toString());

    }

    public static String makeHash(String input, String type, int length)
    {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance(type);

            // digest() method is called to calculate message digest
            // of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hexString = new StringBuilder(no.toString(16));
            // Convert message digest into hex value
            while (hexString.length() < length) {
                hexString.insert(0, '0');
            }
            return hexString.toString();
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
