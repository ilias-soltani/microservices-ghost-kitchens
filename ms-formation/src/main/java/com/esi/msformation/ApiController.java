package com.esi.msformation;


import com.esi.msformation.authntication.AuthProxy;
import com.esi.msformation.authntication.VerifyTokenRequest;
import com.esi.msformation.authntication.VerifyTokenResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class ApiController {

    @Autowired
    AuthProxy authProxy;



    @GetMapping("/verify-token")
    public ResponseEntity<Object> verifyToken(@RequestHeader("Authorization") String token) {
        VerifyTokenResponse response = authProxy.verifyToken(token, new VerifyTokenRequest(new String[]{"client"}));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.valueToTree(response.getUser());

// Access the attributes using the keys
        String userId = responseBody.get("_id").asText();
        String name = responseBody.get("name").asText();
        String email = responseBody.get("email").asText();
        String role = responseBody.get("role").asText();

        return ResponseEntity.ok(userId);
    }
}
