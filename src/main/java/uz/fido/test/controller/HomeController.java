package uz.fido.test.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("/get")
    public HttpEntity<?> getHome(){
        return ResponseEntity.ok("Welcome to Home page");
    }

}
