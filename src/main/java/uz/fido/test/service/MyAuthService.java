package uz.fido.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MyAuthService implements UserDetailsService {

@Autowired
PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<User> users = new ArrayList<>(Arrays.asList(
                new User("jakhongir",passwordEncoder.encode("jakhongir1011"), new ArrayList<>()),
                new User("almas",passwordEncoder.encode("almas1011"), new ArrayList<>()),
                new User("kul",passwordEncoder.encode("kul1011"), new ArrayList<>()),
                new User("fido",passwordEncoder.encode("fido1011") , new ArrayList<>())
        ));

        for (User user : users) {
            boolean equals = user.getUsername().equals(username);
            if (equals)
                return user;
        }
        throw new UsernameNotFoundException("error user");
    }
}
