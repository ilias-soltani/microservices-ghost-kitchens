package com.esi.msformation;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class ApiController {

    @RequestMapping("test")
    public String test(){
        return "HI";
    }
}
