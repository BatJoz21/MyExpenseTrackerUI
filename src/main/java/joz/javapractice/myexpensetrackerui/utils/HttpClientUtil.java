package joz.javapractice.myexpensetrackerui.utils;

import com.google.gson.Gson;
import joz.javapractice.myexpensetrackerui.security.AuthenticationException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientUtil {
    private static final HttpClient client = HttpClient.newBuilder().build();
    private static final Gson gson = new Gson();
    private static final String BASE_URL = "http://localhost:8080";

    public static String sendPostRequest(String url, String jsonBody)
            throws IOException, InterruptedException, AuthenticationException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 403){
            throw new AuthenticationException("Session has expired. Please log in again.");
        }

        if (response.statusCode() == 200){
            return response.body();
        }
        else{
            throw new IOException("Failed to fetch data: " + response.statusCode());
        }
    }

    public static String sendGetRequestWithToken(String path, String token)
            throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200){
            return response.body();
        }
        else{
            throw new IOException("Failed to fetch data: " + response.statusCode());
        }
    }
}
