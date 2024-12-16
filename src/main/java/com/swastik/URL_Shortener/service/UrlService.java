package com.swastik.URL_Shortener.service;

import com.swastik.URL_Shortener.model.Url;
import com.swastik.URL_Shortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class UrlService {

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int URL_KEY_LENGTH = 6;

    @Value("${url.expiration.time}")
    private long expirationTimeMs;

    @Autowired
    private UrlRepository urlRepository;

    public String createShortUrl(String originalUrl) {
        String shortKey;
        do {
            shortKey = generateShortKey();
        } while (urlRepository.findByShortKey(shortKey) != null);

        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortKey(shortKey);
        url.setCreatedAt(new Date());
        url.setExpirationTime(new Date(System.currentTimeMillis() + expirationTimeMs));

        urlRepository.save(url);
        return shortKey;
    }

    private String generateShortKey() {
        Random random = new Random();
        StringBuilder shortKey = new StringBuilder();
        for (int i = 0; i < URL_KEY_LENGTH; i++) {
            shortKey.append(BASE62.charAt(random.nextInt(BASE62.length())));
        }
        return shortKey.toString();
    }

    public String getOriginalUrl(String shortKey) {
        Url url = urlRepository.findByShortKey(shortKey);
        if (url == null) {
            throw new RuntimeException("URL not found!");
        }
        if (url.getExpirationTime().getTime() < System.currentTimeMillis()) {
            urlRepository.delete(url);
            throw new RuntimeException("URL has expired and was deleted!");
        }
        return url.getOriginalUrl();
    }

    @Scheduled(fixedRate = 60000)
    public void deleteExpiredUrls() {
        long currentTime = System.currentTimeMillis();
        urlRepository.deleteAllByExpirationTimeBefore(new Date(currentTime));
        System.out.println("Expired URLs deleted at: " + new Date());
    }
}