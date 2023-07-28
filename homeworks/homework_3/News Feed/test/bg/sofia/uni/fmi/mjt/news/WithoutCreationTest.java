package bg.sofia.uni.fmi.mjt.news;

import bg.sofia.uni.fmi.mjt.news.exception.NewsClientException;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class WithoutCreationTest {

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
                "http://realMadrid.com/image","2023-01-21T14:49:51Z", "Last matches : " +
                "W(2:3), L(1:3), W(2:1), L(2:1), W(0:1), W(0:2), W(2:1)");
        Article[] articles = {article};
        newsSports = new News("ok", 1, articles);
        newsSportsJson = new Gson().toJson(newsSports);
    }

    @BeforeEach
    public void createClient() {
        client = new NewsClient(httpClientMock);
    }

    @Order(1)
    @Test
    public void testThrowExceptionAfterCreatingClient() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenThrow(NewsClientException.class);
        String[] arr = {"Madrid"};
        Parameters parameters = Parameters.builder(arr).build();
        assertThrows(NewsClientException.class, () -> client.getNewsByParameters(parameters),
                "Creating client was not successful");
    }

    @Order(2)
    @Test
    public void testWithInvalidParameterNull() {
        assertThrows(IllegalArgumentException.class, () -> client.getNewsByParameters(null),
                "Invalid argument");
    }

    @Order(4)
    @Test
    public void testAddingNewsNotNull() {
        Set<News> set = new HashSet<>();
        Article art1 = new Article(new Source("4", "gong.bg"),
                "Pavel", "", "little def", "http//google.bg", "", "",
                "Play football");
        Article[] articles1 = {art1};
        News one = new News("ok", 1, articles1);
        set.add(one);
        Article art2 = new Article(new Source("3", "gong.bg"),
                "Gosho", "", "Arsenal win over MU", "http//google.bg", "",
                "", "Play football");
        Article[] articles2 = {art2};
        News two = new News("ok", 1, articles2);
        set.add(two);

        for (var current:set) {
            assertNotNull(current, "There is a null element");
        }
        set.add(null);
    }

    @Order(5)
    @Test
    public void testWithCRespondNull() {
        String[] arr = {"Madrid"};
        Parameters parameters = Parameters.builder(arr).build();
        assertThrows(NewsClientException.class, () -> client.getNewsByParameters(parameters),
                "Invalid argument");
    }
}
