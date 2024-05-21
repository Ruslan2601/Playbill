package ru.practicum.stats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.stats.dto.EventRequest;
import ru.practicum.stats.dto.StatisticResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatsClient extends BaseClient {
    private static final String API_PREFIX = "/";

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public EventRequest addEvent(@RequestBody EventRequest endpoint) {
        ResponseEntity<EventRequest> responseEntity = this.post("/hit", endpoint, new EventRequest());
        return responseEntity.getBody();
    }

    public List<StatisticResponse> getStatistics(LocalDateTime start, LocalDateTime end, String[] uris, String unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> map = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter),
                "uris", uris,
                "unique", unique

        );
        ResponseEntity<ArrayList<StatisticResponse>> responseEntity =
                this.get("/stats?start={start}&end={end}&uris={uris}&uniq={unique}", map, new ArrayList<>());
        return responseEntity.getBody();
    }
}
