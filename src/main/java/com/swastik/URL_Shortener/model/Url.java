package com.swastik.URL_Shortener.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "urls")
public class Url {
    @Id
    private String id;

    private String shortKey;

    private String originalUrl;

    private Date createdAt;

    @Indexed(name = "expirationTime")
    private Date expirationTime;
}