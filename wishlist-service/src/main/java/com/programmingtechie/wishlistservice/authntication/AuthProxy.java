package com.programmingtechie.wishlistservice.authntication;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface AuthProxy {
    @PostMapping("/api/v1/jwt/verify-token")
    VerifyTokenResponse verifyToken(@RequestHeader("Authorization") String token, @RequestBody VerifyTokenRequest request);
}
