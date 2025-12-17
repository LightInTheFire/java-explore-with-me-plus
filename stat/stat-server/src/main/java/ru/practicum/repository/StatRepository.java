package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {
    @Query("""
            SELECT new ru.practicum.dto.ViewStatsDto(eh.app, eh.uri, COUNT(DISTINCT eh.ip))
            FROM EndpointHit AS eh
            WHERE eh.created BETWEEN :start AND :end AND eh.uri IN :uris
            GROUP BY eh.uri, eh.app
            ORDER BY COUNT(DISTINCT eh.ip) DESC
            """)
    List<ViewStatsDto> getUniqueStatsWithUris(@Param("start")LocalDateTime start,
                                      @Param("end") LocalDateTime end,
                                      @Param("uris") List<String> uris);

    @Query("""
            SELECT new ru.practicum.dto.ViewStatsDto(eh.app, eh.uri, COUNT(eh.ip))
            FROM EndpointHit AS eh
            WHERE eh.created BETWEEN :start AND :end AND eh.uri IN :uris
            GROUP BY eh.uri, eh.app
            ORDER BY COUNT(eh.ip) DESC
            """)
    List<ViewStatsDto> getNotUniqueStatsWithUris(@Param("start")LocalDateTime start,
                                      @Param("end") LocalDateTime end,
                                      @Param("uris") List<String> uris);

    @Query("""
            SELECT new ru.practicum.dto.ViewStatsDto(eh.app, eh.uri, COUNT(DISTINCT eh.ip))
            FROM EndpointHit AS eh
            WHERE eh.created BETWEEN :start AND :end
            GROUP BY eh.uri, eh.app
            ORDER BY COUNT(DISTINCT eh.ip) DESC
            """)
    List<ViewStatsDto> getUniqueStatsWithoutUris(@Param("start")LocalDateTime start,
                                              @Param("end") LocalDateTime end);

    @Query("""
            SELECT new ru.practicum.dto.ViewStatsDto(eh.app, eh.uri, COUNT(eh.ip))
            FROM EndpointHit AS eh
            WHERE eh.created BETWEEN :start AND :end
            GROUP BY eh.uri, eh.app
            ORDER BY COUNT(eh.ip) DESC
            """)
    List<ViewStatsDto> getNotUniqueStatsWithoutUris(@Param("start")LocalDateTime start,
                                                 @Param("end") LocalDateTime end);
}
