package com.swastik.URL_Shortener.repository;

import com.swastik.URL_Shortener.model.Url;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UrlRepository extends JpaRepository<Url, String> {

    Url findByShortKey(String shortKey);

    @Transactional
    @Modifying
    @Query("DELETE FROM Url u WHERE u.expirationTime < CURRENT_TIMESTAMP")
    void deleteExpiredUrls();
}
