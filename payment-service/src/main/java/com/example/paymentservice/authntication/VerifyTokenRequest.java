package com.example.paymentservice.authntication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @AllArgsConstructor @NoArgsConstructor
public class VerifyTokenRequest {
    private String[] roles;

}
