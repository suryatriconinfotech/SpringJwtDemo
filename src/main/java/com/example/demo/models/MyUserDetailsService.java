package com.example.demo.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    MyUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> user=userRepository.findByUsername(username);
        if(user.isPresent()){
            var UserObj=user.get();
            return User.builder()
                    .username(UserObj.getUsername())
                    .password(UserObj.getPassword())
                    .roles(getrole(UserObj))
                    .build();
        }
        else{
            throw new UsernameNotFoundException(username);
        }
    }

    private String[] getrole(MyUser userObj) {
        if(userObj.getRole()==null)return new String[]{"USER"};
        return userObj.getRole().split(",");
    }

}
