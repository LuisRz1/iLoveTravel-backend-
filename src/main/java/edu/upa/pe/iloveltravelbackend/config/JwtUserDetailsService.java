package edu.upa.pe.iloveltravelbackend.config;

import edu.upa.pe.iloveltravelbackend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class JwtUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(JwtUserDetailsService.class);
    private final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("---loadUserByUsername called.---");
        Optional<edu.upa.pe.iloveltravelbackend.models.User> user = userRepository.findByEmail(username);
        if(user.isPresent()) {
            Set<SimpleGrantedAuthority> authorities = new HashSet<>(1);
            authorities.add(new SimpleGrantedAuthority("ADMIN"));

            return new User(user.get().getEmail(), user.get().getPassword(),true,true,true,true, authorities);
        } else {
            throw new UsernameNotFoundException("User "+username+" not found.");
        }
    }
}