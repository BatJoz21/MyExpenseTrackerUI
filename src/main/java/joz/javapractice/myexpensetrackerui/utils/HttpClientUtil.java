package joz.javapractice.myexpensetrackerui.utils;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientUtil {
    private static final HttpClient client = HttpClient.newBuilder().build();
    private static final Gson gson = new Gson();

    public static HttpResponse<String> sendPostRequest(String url, String jsonBody)
            throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> sendGetRequest(String url, String token){
        return null;
    }
}
