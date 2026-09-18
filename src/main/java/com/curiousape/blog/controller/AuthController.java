package com.curiousape.blog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status(Authentication authentication) {
        boolean loggedIn = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);

        if (loggedIn) {
            return ResponseEntity.ok(Map.of("loggedIn", true, "username", authentication.getName()));
        }
        return ResponseEntity.ok(Map.of("loggedIn", false));
    }
}
