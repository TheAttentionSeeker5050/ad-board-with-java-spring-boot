package com.kaijoo.demo.service;

import com.kaijoo.demo.model.User;
import com.kaijoo.demo.model.UserInfoDetails;
import com.kaijoo.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userDetail = repository.findByEmail(username);

        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }

    public boolean addUser(User userInfo) {

        // attempt to get user by email
        Optional<User> user = repository.findByEmail(userInfo.getEmail());
        if (user.isPresent()) {
            return false;
        }

        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return true;
    }


}