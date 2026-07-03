package com.mertalptekin.springbootrestapp.presentation.controller;


import com.mertalptekin.springbootrestapp.application.auth.RegisterRequest;
import com.mertalptekin.springbootrestapp.application.auth.TokenRequest;
import com.mertalptekin.springbootrestapp.application.auth.TokenResponse;
import com.mertalptekin.springbootrestapp.domain.entity.User;
import com.mertalptekin.springbootrestapp.infra.jwt.IJwtService;
import com.mertalptekin.springbootrestapp.infra.repository.IUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

// Kullanıcı oluşturma token üretme, token refresh etme ve benzeri işlemler burada yapılacak.
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {


    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;
    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(PasswordEncoder passwordEncoder, IUserRepository userRepository, IJwtService jwtService, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping
    public String test(){
        return "Auth Controller is working...";
    }


    @PostMapping("token")
    public ResponseEntity<TokenResponse> token(@RequestBody TokenRequest request){

        // Böyle bir kullanıcı bilgisi User tablosunda var mı? UserDetailsService ile kontrol et.
        // Username ve Password -> kontrolü yapar.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );

            // Security Config dosyasında .anyRequest().authenticated()); koddan geçebilmek için. Uygulama üzerinde anlık oturum açmamız gerekiyor.
            SecurityContextHolder.getContext().setAuthentication(authentication);

            if(authentication.isAuthenticated()){
                // oturum açan kullanının sahip olduğu username,password, grantAuthority bilgileri.
                // authentication.getPrincipal() -> Oturum açan kullanıcının detayları
                String token = jwtService.generateToken((UserDetails) authentication.getPrincipal());
                return ResponseEntity.ok(new TokenResponse(token));
            }

            return ResponseEntity.status(401).build();

    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody RegisterRequest request){

        User usr = new User();
        usr.setUsername(request.username());
        usr.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(usr);

        // Kullanıcı veritabanın kaydı

        return ResponseEntity.ok().build();
    }

}
