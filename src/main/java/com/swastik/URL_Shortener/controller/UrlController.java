package com.swastik.URL_Shortener.controller;

import com.swastik.URL_Shortener.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class UrlController {

    @Value("${app.base.url}")
    private String baseUrl;

    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    public String shortenUrl(@RequestParam String originalUrl) {
        String shortKey = urlService.createShortUrl(originalUrl);
        return baseUrl + "/api/" + shortKey;
    }

    @GetMapping("/{shortKey}")
    public void redirectToOriginalUrl(@PathVariable String shortKey, HttpServletResponse response) throws IOException {
        String originalUrl = urlService.getOriginalUrl(shortKey);
        response.sendRedirect(originalUrl);
    }
}