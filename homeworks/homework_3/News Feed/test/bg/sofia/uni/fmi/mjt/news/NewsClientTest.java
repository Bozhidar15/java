package bg.sofia.uni.fmi.mjt.news;
import bg.sofia.uni.fmi.mjt.news.exception.NewsClientException;
import bg.sofia.uni.fmi.mjt.news.exception.NoNewsWithThisCriteriaException;
import bg.sofia.uni.fmi.mjt.news.exception.errors.BadRequestException;
import bg.sofia.uni.fmi.mjt.news.exception.errors.ServerErrorException;
import bg.sofia.uni.fmi.mjt.news.exception.errors.TooManyRequestsException;
import bg.sofia.uni.fmi.mjt.news.exception.errors.UnauthorizedException;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NewsClientTest {
    private static News newsSports;
    private static String newsSportsJson;

    @Mock
    private HttpClient httpClientMock;

    @Mock
    private HttpResponse<String> responseMock;

    private NewsClient client;

    @BeforeAll
    public static void beforeTests() {
        Source source = new Source(null, "gong");
        Article article = new Article(source, "gong.bg", "Are Real Madrid gonna win LaLiga?",
                "Judging by Carlo they have their chances.", "http://realMadrid.com",
                "http://realMadrid.com/image", "2023-01-21T14:49:51Z", "Last matches : " +
                "W(2:3), L(1:3), W(2:1), L(2:1), W(0:1), W(0:2), W(2:1)");
        Article[] articles = {article};
        newsSports = new News("ok", 1, articles);
        newsSportsJson = new Gson().toJson(newsSports);
    }

    @BeforeEach
    public void createClient() throws IOException, InterruptedException {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers
                .<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(responseMock);

        client = new NewsClient(httpClientMock);
    }

    @Test
    public void testWithValidWordMadrid() throws NewsClientException, TooManyRequestsException, UnauthorizedException,
            BadRequestException, ServerErrorException {
        when(responseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(responseMock.body()).thenReturn(newsSportsJson);
        String[] arr = {"Madrid"};
        Parameters parameters = Parameters.builder(arr).build();

        var result = client.getNewsByParameters(parameters);

        assertEquals(newsSports, result, "Incorrect key words");
    }

    @Test
    public void testWithAllParameters() throws NewsClientException, TooManyRequestsException, UnauthorizedException,
            BadRequestException, ServerErrorException {
        when(responseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(responseMock.body()).thenReturn(newsSportsJson);
        String[] arr = {"Madrid"};
        Parameters parameters = Parameters.builder(arr).setCategory("general")
                .setCountry("bg").setPage(3).setPageSize(1).build();

        var result = client.getNewsByParameters(parameters);

        assertEquals(newsSports, result, "Incorrect key words");
    }

    @Test
    public void testWithInvalidWord() throws RuntimeException {
        when(responseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);
        String[] arr = {"sadgasfgdfbdsfgdfgregvdfvbdsfgeqrgqergdfvsad"};
        Parameters parameters = Parameters.builder(arr).build();
        assertThrows(NoNewsWithThisCriteriaException.class, () -> client.getNewsByParameters(parameters),
                "No news with this key word");
    }

    @Test
    public void testWithUnavailableHttp() {
        when(responseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_UNAVAILABLE);
        String[] arr = {"Madrid"};
        Parameters parameters = Parameters.builder(arr).build();
        assertThrows(NewsClientException.class, () -> client.getNewsByParameters(parameters),
                "Http is unavailable");
    }

    @Test
    public void testWithInvalidParameter() {
        String[] arr = {"Madrid"};
        Parameters parameters = Parameters.builder(arr).setCategory("nothing")
                .build();
        assertThrows(NewsClientException.class, () -> client.getNewsByParameters(parameters),
                "Invalid argument");
    }

    @Test
    public void testWithInvalidParameterBadRequest() {
        when(responseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
        String[] arr = {"Madrid"};
        Parameters parameters = Parameters.builder(arr).setCategory("")
                .build();
        assertThrows(BadRequestException.class, () -> client.getNewsByParameters(parameters),
                "Expected bad request exception due to response from http");
    }

    @Test
    public void testWithInvalidParameterUnauthorized() {
        when(responseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_UNAUTHORIZED);
        String[] arr = {"Madrid"};
        Parameters parameters = Parameters.builder(arr).setCategory("")
                .build();
        assertThrows(UnauthorizedException.class, () -> client.getNewsByParameters(parameters),
                "Expected unauthorized exception due to response from http");
    }

    @Test
    public void testWithInvalidParameterTooManyRequests() {
        final int error = 429;
        when(responseMock.statusCode()).thenReturn(error);
        String[] arr = {"Madrid"};
        Parameters parameters = Parameters.builder(arr).setCategory("")
                .build();
        assertThrows(TooManyRequestsException.class, () -> client.getNewsByParameters(parameters),
                "Expected too many request exception due to response from http");
    }

    @Test
    public void testWithInvalidParameterServerError() {
        when(responseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_SERVER_ERROR);
        String[] arr = {"Madrid"};
        Parameters parameters = Parameters.builder(arr).setCategory("")
                .build();
        assertThrows(ServerErrorException.class, () -> client.getNewsByParameters(parameters),
                "Expected server error exception due to response from http");
    }

    @Test
    public void testWithInvalidParameterRandomError() {
        final int error = 411;
        when(responseMock.statusCode()).thenReturn(error);
        String[] arr = {"Madrid"};
        Parameters parameters = Parameters.builder(arr).setCategory("")
                .build();
        assertThrows(NewsClientException.class, () -> client.getNewsByParameters(parameters),
                "Expected news client exception due to response from http - random");
    }


}
