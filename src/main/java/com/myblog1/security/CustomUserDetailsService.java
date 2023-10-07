package com.myblog1.security;

//This class is responsible for loading user details from the database.

import com.myblog1.entity.Role;
import com.myblog1.entity.User;
import com.myblog1.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //When a user attempts to log in, the loadUserByUsername method is called with the provided username or email as an argument.
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // It uses the UserRepository to query the database for a user with the provided username or email.
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email:" + usernameOrEmail));

        //If a user is found, it creates a UserDetails object using the user's email as the username, the user's password, and a list of authorities (roles) obtained by calling the mapRolesToAuthorities method.
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    //The mapRolesToAuthorities method takes a set of roles associated with the user and converts them into a collection of GrantedAuthority objects. These authorities are typically used for authorization purposes.
    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }



}
