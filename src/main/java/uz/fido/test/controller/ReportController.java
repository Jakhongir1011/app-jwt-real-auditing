package uz.fido.test.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.fido.test.poyload.AuthDto;

@RestController
@RequestMapping("/api")
public class ReportController {

    @GetMapping("/report")
    public HttpEntity<?> getHome(){
        return ResponseEntity.ok("Report sent");
    }

    @PostMapping("/add")
    public HttpEntity<?> add(@RequestBody AuthDto authDto){
        return ResponseEntity.ok(authDto);
    }
}
