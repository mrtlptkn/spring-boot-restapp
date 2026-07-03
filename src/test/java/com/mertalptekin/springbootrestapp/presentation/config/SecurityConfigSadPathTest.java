package com.mertalptekin.springbootrestapp.presentation.config;

import com.mertalptekin.springbootrestapp._demo.springContext.circular.ServiceA;
import com.mertalptekin.springbootrestapp._demo.springContext.circular.ServiceB;
import com.mertalptekin.springbootrestapp._demo.springContext.custom.WebRequestBasedBean;
import com.mertalptekin.springbootrestapp.domain.service.AspectService;
import com.mertalptekin.springbootrestapp.infra.jwt.JwtService;
import com.mertalptekin.springbootrestapp.infra.repository.IUserRepository;
import com.mertalptekin.springbootrestapp.presentation.controller.AuthController;
import com.mertalptekin.springbootrestapp.presentation.controller.DemoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {DemoController.class, AuthController.class})
@Import({SecurityConfig.class, AuthenticationFilter.class, AuthEntryPoint.class})
class SecurityConfigSadPathTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WebRequestBasedBean webRequestBasedBean;

    @MockBean
    private AspectService aspectService;

    @MockBean
    private ServiceA serviceA;

    @MockBean
    private ServiceB serviceB;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private IUserRepository userRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @MockBean
    private JwtService jwtService;

    @Test
    void deniesAnonymousAccessToProtectedDemoEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/demo"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("""
                        {
                          "error": "Unauthorized",
                          "message": "Kimlik doğrulama gerekli"
                        }
                        """));
    }

    @Test
    void allowsAnonymousAccessToAuthEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/auth"))
                .andExpect(status().isOk())
                .andExpect(content().string("Auth Controller is working..."));
    }
}
