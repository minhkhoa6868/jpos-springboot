package com.example.jpos_springboot.server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "channel")
@Getter
@Setter
public class ChannelRoute {
    @Id
    @Column(columnDefinition = "INT(11)")
    private Integer id;

    @Column(columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(name = "status_code", columnDefinition = "INT(11)")
    private Integer statusCode;

    @Column(name = "to_channel_id", columnDefinition = "INT(11)")
    private Integer toChannelId;

    @Column(name = "queue_type", columnDefinition = "VARCHAR(255)")
    private String queueType;

    @Column(name = "belong_app_name", columnDefinition = "VARCHAR(255)")
    private String belongAppName;

    @Column(name = "queue_name", columnDefinition = "VARCHAR(255)")
    private String queueName;
}
