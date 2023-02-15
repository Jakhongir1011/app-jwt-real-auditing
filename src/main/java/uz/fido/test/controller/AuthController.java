package uz.fido.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.fido.test.poyload.AuthDto;
import uz.fido.test.secury.JwtProvider;
import uz.fido.test.service.MyAuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    MyAuthService myAuthService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthenticationManager authenticationManager; // shuni ozi tekshirib beradi 2 usulda

    @PostMapping("/login")
    public HttpEntity<?> loginToSystem(@RequestBody AuthDto authDto){

        // 1-usul
        UserDetails loadUserByUsername = myAuthService.loadUserByUsername(authDto.getUsername());// username solishtiradi bu

//1.       boolean equals = loadUserByUsername.getPassword().equals(authDto.getPassword());

        /**
         * 2. passwordla EnCoder bo'lishi kk. <br>
         * Matches qavus ichiga <br>
         * birinchisiga cleantdan kelgan password <br>
         * 2 chisiga bazadagi password yozishimiz kerak
         */

        // password == solishtirsmiz
        boolean matches = passwordEncoder.matches(authDto.getPassword(), loadUserByUsername.getPassword());
        if (matches)
            return ResponseEntity.ok(JwtProvider.generateToken(authDto.getUsername()));

        return ResponseEntity.status(407).body("username or password error!");

        /**
            //         2-usul
            try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            authDto.getPassword(),
            authDto.getUsername()));
            String token = JwtProvider.generateToken(authDto.getUsername());
            return ResponseEntity.ok(token);

            }catch (BadCredentialsException exception){
            return ResponseEntity.status(407).body("username or password error!");
            }
            */


    }
}
