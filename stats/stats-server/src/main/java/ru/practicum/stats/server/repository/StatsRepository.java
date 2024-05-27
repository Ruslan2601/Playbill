package ru.practicum.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.stats.server.model.Event;
import ru.practicum.stats.server.model.Statistic;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT new ru.practicum.stats.server.model.Statistic(app, uri, COUNT(ip)) \n" +
            "FROM Event \n" +
            "WHERE uri IN (:uris) \n" +
            "  AND created BETWEEN :start \n" +
            "                  AND :end \n" +
            "GROUP BY app, uri \n" +
            "ORDER BY COUNT(ip) DESC")
    List<Statistic> getStatisticsAnyIp(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

    @Query(value = "SELECT new ru.practicum.stats.server.model.Statistic(app, uri, COUNT(DISTINCT(ip))) \n" +
            "FROM Event \n" +
            "WHERE uri IN (:uris) \n" +
            "  AND created BETWEEN :start \n" +
            "                  AND :end \n" +
            "GROUP BY app, uri \n" +
            "ORDER BY COUNT(DISTINCT(ip)) DESC")
    List<Statistic> getStatisticsUniqueIp(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

    @Query(value = "SELECT new ru.practicum.stats.server.model.Statistic(app, uri, COUNT(ip)) \n" +
            "FROM Event \n" +
            "WHERE created BETWEEN :start \n" +
            "                  AND :end \n" +
            "GROUP BY app, uri \n" +
            "ORDER BY COUNT(ip) DESC")
    List<Statistic> getAllStatisticsAnyIp(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.stats.server.model.Statistic(app, uri, COUNT(DISTINCT(ip))) \n" +
            "FROM Event \n" +
            "WHERE created BETWEEN :start \n" +
            "                  AND :end \n" +
            "GROUP BY app, uri \n" +
            "ORDER BY COUNT(DISTINCT(ip)) DESC")
    List<Statistic> getAllStatisticsUniqueIp(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
