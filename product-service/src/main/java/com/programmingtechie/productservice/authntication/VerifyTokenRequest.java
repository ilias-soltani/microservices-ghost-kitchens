package com.programmingtechie.productservice.authntication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @AllArgsConstructor @NoArgsConstructor
public class VerifyTokenRequest {
    private String[] roles;

}
