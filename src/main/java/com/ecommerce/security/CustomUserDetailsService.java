package com.ecommerce.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {

        // Chercher l'utilisateur en BDD
        User user = userRepository.findByLogin(login)
            .orElseThrow(() ->
                new UsernameNotFoundException("Utilisateur non trouvé : " + login)
            );

        // Convertir notre User en UserDetails Spring Security
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getLogin())
            .password(user.getPassword())
            // Utilise le rôle stocké en base (ROLE_USER ou ROLE_ADMIN)
            .authorities(user.getRole())
            .build();
    }
}
