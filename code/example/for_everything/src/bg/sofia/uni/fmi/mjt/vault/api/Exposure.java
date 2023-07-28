package bg.sofia.uni.fmi.mjt.vault.api;

import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class Exposure {
    private static final String apiKey = "be3458d568884128a7d60ab734a77768";
    private static final String apiSecret = "&WGs%=HW33h+HDN1w7%95D-TVBZ%-?M%";
    private static final String sha1ForHash = "SHA-1";
    private static final String sha256ForHash = "SHA-256";
    private static final String md5ForHash = "MD5";
    private static final int lengthOfMd5AndSha1 = 32;
    private static final int lengthOfSha256 = 64;
    private static final String API_ENDPOINT_SCHEME = "http";
    private static final String API_ENDPOINT_HOST = "api.enzoic.com";
    private static final String API_ENDPOINT_PATH = "/passwords";
    private static final String API_ENDPOINT_QUERY_SHA1 = "**sha1**=";
    private static final String API_ENDPOINT_QUERY_MD5 = "**md5**=";
    private static final String API_ENDPOINT_QUERY_SHA256 = "**sha256**=";
    private static final Gson GSON = new Gson();
    private static final HttpClient newsHttpClient = HttpClient.newBuilder().build();;


    public static InformationForThePassword findResults(URI uri) {
        HttpResponse<String> response;
        try {
            String keyAndSecret =  apiKey + ":" + apiSecret;
            String encodedString = Base64.getEncoder().encodeToString(keyAndSecret.getBytes());
            String hhhh = "Base64Encode({" + apiKey + "}:{" + apiSecret + "})";
            HttpRequest request = HttpRequest.newBuilder().header("authorization", " basic " + encodedString
                    ).uri(uri).build();
            response = newsHttpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not retrieve response!", e);
        }
        if (response == null) {
            throw new IllegalArgumentException("Could not retrieve response, because it is null!");
        }
        int statusCode = response.statusCode();
        if (response.statusCode() == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new IllegalArgumentException("Could not find password in the data base!" +
                    System.lineSeparator());
        } else if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            return GSON.fromJson(response.body(), InformationForThePassword.class);
        } else {
            throw new IllegalArgumentException("Status code of response in undefined : " + response.statusCode() +
                    System.lineSeparator() + response.body());
        }
    }

    public static InformationForThePassword getNewsByParameters(String password) {

        StringBuilder create = new StringBuilder();
        create.append(API_ENDPOINT_QUERY_SHA1)
                .append(Hash.makeHash(password, sha1ForHash, lengthOfMd5AndSha1)).append('&');
        create.append(API_ENDPOINT_QUERY_MD5)
                .append(Hash.makeHash(password, md5ForHash, lengthOfMd5AndSha1)).append('&');
        create.append(API_ENDPOINT_QUERY_SHA256)
                .append(Hash.makeHash(password, sha256ForHash, lengthOfSha256));
        URI uri;
        try {
            uri = new URI(API_ENDPOINT_SCHEME, API_ENDPOINT_HOST, API_ENDPOINT_PATH,
                    create.toString(), null);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating uri!", e);
        }
        return findResults(uri);
    }
}