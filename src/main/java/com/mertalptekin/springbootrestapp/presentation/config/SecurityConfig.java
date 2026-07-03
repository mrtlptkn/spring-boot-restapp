package com.mertalptekin.springbootrestapp.presentation.config;

import com.mertalptekin.springbootrestapp.infra.jwt.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // Kimlik Doğrulama için UserDetails Servisten kullanıcı bilgilerini sorgulayacağım altyapı katmanı
    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationFilter authenticationFilter;

    public SecurityConfig(AuthenticationProvider authenticationProvider, AuthenticationFilter authenticationFilter) {
        this.authenticationProvider = authenticationProvider;
        this.authenticationFilter = authenticationFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {
        // web uygulamasında hangi endpointlere login oladan gireceğimiz yöneteceğiz.
        http.csrf(AbstractHttpConfigurer::disable); // Form istekleri www.urlencoded değil application/json bundan dolayı bu ayarı kapattık. Gelenek web uygulamalrındaki güvenlik ayarı.
        // Bunu kaldırınca Post isteklerinde 403 hatası alırız. uygulama keser.

        http.cors(cors -> cors.configurationSource(corsConfigurationSource())); // React gibi farklı origin'den (örn. localhost:3000) gelen isteklere izin ver.

        // H2 Console için frame options'ı devre dışı bırak
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        // URL seviyesinde genel bir yetkilendirme politikası belirlemek için hasRole veya hasAuthority kullanabiliriz.


        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Rest servislerde kullanılan session modeli
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/api/v1/categories/**").permitAll()
                        .requestMatchers("/api/v1/courses/**").permitAll()
                        .requestMatchers("/api/v1/demo/**").hasAuthority("ROLE_MANAGER")
                        .requestMatchers(("/api/v1/auth/**")).permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        .anyRequest().authenticated());
        http.authenticationProvider(authenticationProvider); // Kimlik doğrulama sağlayıcıyı ekliyoruz.

        // JWT Filter ile jwt doğrulama yapacağız.
        http.addFilterBefore(authenticationFilter,
                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(exceptionHandler ->
                exceptionHandler.authenticationEntryPoint(new AuthEntryPoint(jwtService))
        ); // Yetkilendirme hatalarında özel giriş noktası kullanımı

        return http.build();

    }

    // CORS: farklı origin'den (ör. React dev sunucusu) gelen tarayıcı isteklerinin
    // hangi koşullarda kabul edileceğini tanımlar.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
