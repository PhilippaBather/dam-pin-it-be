package com.batherphilippa.pin_it_app_be.config;

import com.batherphilippa.pin_it_app_be.security.jwt.JwtAuthEntryPoint;
import com.batherphilippa.pin_it_app_be.security.jwt.JwtAuthFilter;
import com.batherphilippa.pin_it_app_be.service.UserAuthService;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.servlet.Filter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * AppConfig - application configuration.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public ModelMapper ModelMapper() {
        return new ModelMapper();
    }

    // Spring Security Configuration
    @Value("${key.public}")
    RSAPublicKey key;

    @Value("${key.private}")
    RSAPrivateKey priv;

    @Autowired
    private UserAuthService userDetailsService;

    @Autowired
    private JwtAuthEntryPoint unauthorisedHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        logger.info("AppConfig_authenticationManager");
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public JwtAuthFilter authenticationJwtTokenFilter() {
        logger.info("AppConfig_authenticationJwtTokenFilter");
        return new JwtAuthFilter();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        logger.info("start: AppConfig_authenticationProvider");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        logger.info("end: AppConfig_authenticationProvider");
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("start: AppConfig_filterChain");
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorisedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/users/auth/login").permitAll()
                                .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());

        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        http.addFilterBefore((Filter) authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        logger.info("end: AppConfig_filterChain");
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        logger.info("AppConfig_webSecurityCustomizer");
        return (web) -> web.ignoring().requestMatchers(
                "/users", "/users/auth/signup");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("AppConfig_passwordEncoder");
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        logger.info("AppConfig_jwtDecoder");
        return NimbusJwtDecoder.withPublicKey(this.key).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        logger.info("start: AppConfig_jwtEncoder");
        JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        logger.info("end: AppConfig_jwtEncoder");
        return new NimbusJwtEncoder(jwks);
    }


}