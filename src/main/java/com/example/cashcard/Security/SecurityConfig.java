package com.example.cashcard.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// Annotates the class as a configuration class that will be used by Spring Framework for configuration
@Configuration
public class SecurityConfig {

    // This bean configures the security filter chain, which determines how security checks are performed
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Sets authorization rules. Specifically, only users with the "CARD-OWNER" role can access "/cashcards/**"
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/giftcards/**")
                        .hasRole("CARD-OWNER"))
                // Disables Cross-Site Request Forgery (CSRF) checks for simplicity.
                // Note: This is not recommended for production use as it opens up vulnerabilities.
                .csrf(AbstractHttpConfigurer::disable)
                // Enables HTTP Basic authentication using the default configuration
                .httpBasic(Customizer.withDefaults());

        // Builds and returns the configured security filter chain
        return http.build();
    }

    // This bean provides a user details service that is backed by in-memory data. Used for testing.
    @Bean
    public UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
        User.UserBuilder users = User.builder(); // Builder for creating user details

        // Defines a user named 'sarah1' with password 'abc123' and role 'CARD-OWNER'
        UserDetails sarah = users
                .username("sarah1")
                .password(passwordEncoder.encode("abc123")) // Password is encoded for security
                .roles("CARD-OWNER")
                .build();

        // Defines another user named 'hank-owns-no-cards' with a different password and role 'NON-OWNER'
        UserDetails hankOwnsNoCards = users
                .username("hank-owns-no-cards")
                .password(passwordEncoder.encode("qrs456"))
                .roles("NON-OWNER")
                .build();

        // Defines a user named 'kumar2' with password 'xyz789' and role 'CARD-OWNER'
        UserDetails kumar = users
                .username("kumar2")
                .password(passwordEncoder.encode("xyz789"))
                .roles("CARD-OWNER")
                .build();

        // Returns an in-memory user details manager initialized with the three users defined above
        return new InMemoryUserDetailsManager(sarah, hankOwnsNoCards, kumar);
    }

    // This bean provides a password encoder which uses BCrypt hashing algorithm
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
