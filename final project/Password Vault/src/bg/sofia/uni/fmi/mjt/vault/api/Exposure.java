package bg.sofia.uni.fmi.mjt.vault.api;

import bg.sofia.uni.fmi.mjt.vault.exception.ErrorCreatingUrlException;
import bg.sofia.uni.fmi.mjt.vault.exception.ErrorsInRequestOrResponseException;
import bg.sofia.uni.fmi.mjt.vault.exception.HttpResponseException;
import bg.sofia.uni.fmi.mjt.vault.exception.PasswordIsNotInTheDataBaseExseption;
import bg.sofia.uni.fmi.mjt.vault.hash.Hash;
import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class Exposure {
    private static final String API_KEY = "";
    private static final String API_SECRET = "";
    private static final String SHA1_FOR_HASH = "SHA-1";
    private static final String SHA256_FOR_HASH = "SHA-256";
    private static final String MD5_FOR_HASH = "MD5";
    private static final int LENGTH_OF_MD5_AND_SHA1 = 32;
    private static final int LENGTH_OF_SHA256 = 64;
    private static final String API_ENDPOINT_SCHEME = "https";
    private static final String API_ENDPOINT_HOST = "api.enzoic.com";
    private static final String API_ENDPOINT_PATH = "/passwords";
    private static final String API_ENDPOINT_QUERY_SHA1 = "sha1=";
    private static final String API_ENDPOINT_QUERY_MD5 = "md5=";
    private static final String API_ENDPOINT_QUERY_SHA256 = "sha256=";
    private static final Gson GSON = new Gson();
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().build();

    private static InformationForThePassword findResults(URI uri) {
        HttpResponse<String> response;
        try {
            String keyAndSecret = API_KEY + ":" + API_SECRET;
            String encodedString = Base64.getEncoder().encodeToString(keyAndSecret.getBytes());
            HttpRequest request = HttpRequest.newBuilder().header("authorization",
                    " basic " + encodedString).uri(uri).build();
            response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new ErrorsInRequestOrResponseException("Could not retrieve response!", e);
        }
        if (response == null) {
            throw new ErrorsInRequestOrResponseException("Could not retrieve response, because it is null!");
        }

        if (response.statusCode() == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new PasswordIsNotInTheDataBaseExseption("Could not find password in the data base!" +
                    System.lineSeparator());
        } else if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            return GSON.fromJson(response.body(), InformationForThePassword.class);
        } else {
            throw new HttpResponseException("Status code of response in undefined : " + response.statusCode() +
                    System.lineSeparator());
        }
    }

    public static InformationForThePassword getNewsByParameters(String password) {

        StringBuilder create = new StringBuilder();
        create.append(API_ENDPOINT_QUERY_SHA1)
                .append(Hash.makeHash(password, SHA1_FOR_HASH, LENGTH_OF_MD5_AND_SHA1)).append('&');
        create.append(API_ENDPOINT_QUERY_MD5)
                .append(Hash.makeHash(password, MD5_FOR_HASH, LENGTH_OF_MD5_AND_SHA1)).append('&');
        create.append(API_ENDPOINT_QUERY_SHA256)
                .append(Hash.makeHash(password, SHA256_FOR_HASH, LENGTH_OF_SHA256));
        URI uri;
        try {
            uri = new URI(API_ENDPOINT_SCHEME, API_ENDPOINT_HOST, API_ENDPOINT_PATH,
                    create.toString(), null);
        } catch (Exception e) {
            throw new ErrorCreatingUrlException("Error creating uri!", e);
        }
        return findResults(uri);
    }
}
