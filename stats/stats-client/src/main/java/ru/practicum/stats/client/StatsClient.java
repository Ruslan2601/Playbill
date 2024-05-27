package ru.practicum.stats.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.stats.dto.EventRequest;
import ru.practicum.stats.dto.StatisticResponse;
import ru.practicum.stats.exception.ServerResponseException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class StatsClient {

    @Value("${stats-server.url}")
    private String url;
    private final RestTemplate rest;

    public StatsClient() {
        this.rest = new RestTemplate();
    }

    public List<StatisticResponse> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        StringBuilder urlRequest = new StringBuilder();

        urlRequest.append(url)
                .append("/stats?")
                .append("start=")
                .append(start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .append("&end=")
                .append(end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        if (uris != null && !uris.isEmpty()) {
            urlRequest.append("&uris=");

            for (String uri : uris) {
                urlRequest.append(uri).append(",");
            }

            urlRequest.deleteCharAt(urlRequest.length() - 1);
        }

        urlRequest.append("&unique=").append(unique);

        try {
            return rest.exchange(urlRequest.toString(),
                            HttpMethod.GET,
                            new HttpEntity<>(defaultHeaders()),
                            new ParameterizedTypeReference<List<StatisticResponse>>() {
                            })
                    .getBody();
        } catch (HttpStatusCodeException exception) {
            throw new ServerResponseException("Запрос на получение статистики от сервера не прошёл: " + exception.getMessage());
        }
    }

    public void addEvent(EventRequest event) {
        try {
            rest.exchange(url + "/hit",
                    HttpMethod.POST,
                    new HttpEntity<>(event, defaultHeaders()),
                    Object.class);
        } catch (HttpStatusCodeException exception) {
            throw new ServerResponseException("Запрос на добавление просмотра не прошёл для сервера: " + exception.getMessage());
        }
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
