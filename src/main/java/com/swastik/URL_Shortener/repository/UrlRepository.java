package com.swastik.URL_Shortener.repository;

import com.swastik.URL_Shortener.model.Url;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UrlRepository extends MongoRepository<Url, String> {

    Url findByShortKey(String shortKey);

    void deleteAllByExpirationTimeBefore(Date date);
}
