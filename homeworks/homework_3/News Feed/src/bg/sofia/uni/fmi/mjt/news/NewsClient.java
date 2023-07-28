package bg.sofia.uni.fmi.mjt.news;


import bg.sofia.uni.fmi.mjt.news.exception.NewsClientException;
import bg.sofia.uni.fmi.mjt.news.exception.NoNewsWithThisCriteriaException;
import bg.sofia.uni.fmi.mjt.news.exception.errors.BadRequestException;
import bg.sofia.uni.fmi.mjt.news.exception.errors.ServerErrorException;
import bg.sofia.uni.fmi.mjt.news.exception.errors.TooManyRequestsException;
import bg.sofia.uni.fmi.mjt.news.exception.errors.UnauthorizedException;
import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class NewsClient {

    private static final String API_KEY = "903ac21252774ab0aa0a2a7086e3fb6e";

    private static final String API_ENDPOINT_SCHEME = "http";
    private static final String API_ENDPOINT_HOST = "newsapi.org";
    private static final String API_ENDPOINT_PATH = "/v2/top-headlines";
    private static final String API_ENDPOINT_QUERY_Q = "q=";
    private static final String API_ENDPOINT_QUERY_KEY = "apiKey=";
    private static final String API_ENDPOINT_QUERY_CATEGORY = "category=";
    private static final String API_ENDPOINT_QUERY_COUNTRY = "country=";
    private static final String API_ENDPOINT_QUERY_PAGE_SIZE = "pageSize=";
    private static final String API_ENDPOINT_QUERY_PAGE = "page=";
    private static final Gson GSON = new Gson();

    private final HttpClient newsHttpClient;
    private final String apiKey;

    public NewsClient(HttpClient client) {
        this(client, API_KEY);
    }
    public NewsClient(HttpClient client, String key) {
        this.newsHttpClient = client;
        this.apiKey = key;
    }

    private News findResults(URI uri) throws BadRequestException, UnauthorizedException,
            TooManyRequestsException, ServerErrorException {
        HttpResponse<String> response;

        try {
            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
            response = newsHttpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new NewsClientException("Could not retrieve news", e);
        }
        if (response == null) {
            throw new NewsClientException("Could not retrieve news");
        }
        int a = response.statusCode();
        if (response.statusCode() == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new NoNewsWithThisCriteriaException("Could not find new with these criteria");
        }
        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            return GSON.fromJson(response.body(), News.class);
        }

        final int error1 = 400;
        final int error2 = 401;
        final int error3 = 429;
        final int error4 = 500;
        switch (response.statusCode()) {
            case error1: throw new BadRequestException("400 : " + "The request was unacceptable, often due to a" +
                    " missing or misconfigured parameter.");
            case error2: throw new UnauthorizedException("401 : " + "Your API key was missing from the request," +
                    " or wasn't correct.");
            case error3: throw new TooManyRequestsException("429 : " + "You made too many requests within a window of" +
                    " time and have been rate limited. Back off for a while.");
            case error4: throw new ServerErrorException("500 : " + "Something went wrong on our side.");
            default: throw new NewsClientException("Some kind of server/request error" );
        }

    }

    public News getNewsByParameters(Parameters parameters) throws NewsClientException, TooManyRequestsException,
            UnauthorizedException, BadRequestException, ServerErrorException {
        if (parameters == null || parameters.getKeyWords() == null || parameters.getKeyWords().length == 0 ) {
            throw new IllegalArgumentException("Invalid criteria");
        }
        StringBuilder create = new StringBuilder();
        String allKeyWords = String.join(",", parameters.getKeyWords());
        create.append(API_ENDPOINT_QUERY_Q);
        create.append(allKeyWords).append('&');
        URI uri;
        if (parameters.getCountry() != null) {
            create.append(API_ENDPOINT_QUERY_COUNTRY).append(parameters.getCountry()).append('&');
        }
        if (parameters.getCategory() != null) {
            create.append(API_ENDPOINT_QUERY_CATEGORY).append(parameters.getCategory()).append('&');
        }
        if (parameters.getPageSize() > 0) {
            create.append(API_ENDPOINT_QUERY_PAGE_SIZE).append(parameters.getPageSize()).append('&');
        }
        if (parameters.getPage() > 0) {
            create.append(API_ENDPOINT_QUERY_PAGE).append(parameters.getPage()).append('&');
        }
        create.append(API_ENDPOINT_QUERY_KEY).append(apiKey);
        try {
            uri = new URI(API_ENDPOINT_SCHEME, API_ENDPOINT_HOST, API_ENDPOINT_PATH,
                    create.toString(), null);
        } catch (Exception e) {
            throw new NewsClientException("Error creating uri", e);
        }
        return findResults(uri);
    }
}
