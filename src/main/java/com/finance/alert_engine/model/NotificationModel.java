package com.finance.alert_engine.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "notifications")
@Data
public class NotificationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title; //Volatility Detected

    @Column(name = "value", columnDefinition = "TEXT")
    private String value; //

    @Column(name = "extra", columnDefinition = "TEXT")
    private String extra; //

    @Column(name = "has_read", nullable = false)
    private Boolean hasRead = false;

    @Column(name = "sent_at")
    private Date sentAt;

    @Column(name = "last_read_at")
    private Date lastReadAt;
}
