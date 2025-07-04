package com.example.jpos_springboot.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.jpos_springboot.server.model.ChannelRoute;

@Repository
public interface ChannelRouteRepository extends JpaRepository<ChannelRoute, Long> {
    @Query(value = """
            SELECT a.name
            FROM channel a
            JOIN channel b ON a.id = b.to_channel_id
            WHERE b.id = :id
            """, nativeQuery = true)
    String findDestinationName(@Param("id") Integer id);

    @Query(value = """
            SELECT a.queue_type
            FROM channel a
            JOIN channel b ON a.id = b.to_channel_id
            WHERE b.id = :id
            """, nativeQuery = true)
    String findQueueType(@Param("id") Integer id);
}
