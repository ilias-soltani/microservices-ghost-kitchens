package com.programmingtechie.productservice.authntication;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class VerifyTokenResponse {
    private String error;
    private Object user;

}
