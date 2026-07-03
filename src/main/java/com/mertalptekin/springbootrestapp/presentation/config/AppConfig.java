package com.mertalptekin.springbootrestapp.presentation.config;


import com.mertalptekin.springbootrestapp.domain.service.CustomUserDetailService;
import com.mertalptekin.springbootrestapp.infra.repository.IUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Configuration sınıfında manuel bean tanımlamaları yapılabilir.
@Configuration
public class AppConfig {


    private final CustomUserDetailService customUserDetailService;

    public AppConfig(IUserRepository userRepository, CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    @Bean(name = "getAppName1")
    public String getAppName() {
        return "Spring Boot Rest App 01";
    }


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailService;
    }

    // Password encoder bean tanımı, şifreleri güvenli bir şekilde saklamak için kullanılır.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Burada kullanıcı bilgisi veritabanından çekilrirken hangi şifreleme bean ve hangi servis kullanılacağını belirtiyoruz.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // AuthenticationManager bean tanımı, kimlik doğrulama işlemlerini yönetir.
    // APIda UsernamePasswordAuthenticationToken bazlı kimlik doğrulama işlemleri için kullanacağız.
    // AuthenticationManager, Spring Securityde birden fazla kimlik doğrulama yönetini yöneten sınıftır.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
    }

//    @Bean
//    public AuthenticationManager authenticationManager() {
//        return authentication -> {
//            String username = authentication.getName();
//            String password = authentication.getCredentials().toString();
//
//            UserDetails userDetails = userDetailsService().loadUserByUsername(username);
//
//            if (passwordEncoder().matches(password, userDetails.getPassword())) {
//                return new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
//                        username, password, userDetails.getAuthorities());
//            } else {
//                throw new UsernameNotFoundException("Invalid username or password");
//            }
//        };
//    }


}
