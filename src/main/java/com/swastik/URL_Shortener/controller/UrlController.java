package com.swastik.URL_Shortener.controller;

import com.swastik.URL_Shortener.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@Controller
public class UrlController {

    @Value("${app.base.url}")
    private String baseUrl;

    @Autowired
    private UrlService urlService;

    @GetMapping("/")
    public String home(Model model) {
        return "shorty";
    }

    @PostMapping("/shorten")
    public String shortenUrl(@RequestParam("longUrl") String longUrl, Model model) {
        try {
            String shortKey = urlService.createShortUrl(longUrl);
            String shortUrl = "http://localhost:8080/" + shortKey;
            model.addAttribute("shortUrl", shortUrl);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to generate a short URL. Please try again.");
        }
        return "shorty";
    }

    @GetMapping("/{shortKey}")
    public void getOriginalUrl(@PathVariable String shortKey, HttpServletResponse response) throws IOException {
        String originalUrl = urlService.getOriginalUrl(shortKey);
        response.sendRedirect(originalUrl);
    }
}