package com.example.demo;

import com.example.demo.models.MyUser;
import com.example.demo.models.MyUserDetailsService;
import com.example.demo.models.MyUserRepository;
import com.example.demo.webtoken.JwtService;
import com.example.demo.webtoken.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;
    @Autowired
    MyUserDetailsService myUserDetailsService;
    @Autowired
    MyUserRepository myUserRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @PostMapping("/register/user")
    public MyUser register(@RequestBody MyUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return myUserRepository.save(user);
    }
    @PostMapping("/authenticate")
    public String authenticateAndToken(@RequestBody LoginForm loginForm){
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.username(),loginForm.password()));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(myUserDetailsService.loadUserByUsername(loginForm.username()));
        }
        else{
            throw new UsernameNotFoundException("Invalid Credentials");
        }
    }
}
