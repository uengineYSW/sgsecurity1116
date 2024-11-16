package com.example.template.service;

import com.example.template.entity.User;
import com.example.template.repository.UserRepository;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private WebApplicationContext applicationContext;

    private UserRepository repository;

    @PostConstruct
    public void completeSetup() {
        repository = applicationContext.getBean(UserRepository.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException {
        return repository
            .findByUsername(username)
            .map(user -> {
                user.setAuthorities(
                    AuthorityUtils.createAuthorityList(
                        String.valueOf(user.getRole())
                    )
                );
                return user;
            })
            .orElseThrow(() ->
                new UsernameNotFoundException(
                    "Invalid resource owner, please check resource owner info !"
                )
            );
    }
}
