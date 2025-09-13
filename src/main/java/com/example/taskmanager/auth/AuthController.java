package com.example.taskmanager.auth;

import com.example.taskmanager.auth.dto.*;
import com.example.taskmanager.security.JwtService;
import com.example.taskmanager.user.Role;
import com.example.taskmanager.user.User;
import com.example.taskmanager.user.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokens;

    public AuthController(UserRepository users,
                          PasswordEncoder encoder,
                          AuthenticationManager authManager,
                          JwtService jwtService,
                          RefreshTokenService refreshTokens) {
        this.users = users;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.refreshTokens = refreshTokens;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest req) {
        if (users.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User u = new User();
        u.setEmail(req.getEmail());
        u.setPasswordHash(encoder.encode(req.getPassword()));
        u.setFullName(req.getFullName());
        u.setRoles(Set.of(Role.USER));
        u.setCreatedAt(OffsetDateTime.now());
        users.save(u);

        String access = jwtService.generateAccessToken(u.getEmail(), Map.of("roles", u.getRoles()));
        String refresh = jwtService.generateRefreshToken(u.getEmail());
        refreshTokens.store(refresh, u.getEmail());
        return new AuthResponse(access, refresh);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        String email = auth.getName();
        var user = users.findByEmail(email).orElseThrow();
        String access = jwtService.generateAccessToken(email, Map.of("roles", user.getRoles()));
        String refresh = jwtService.generateRefreshToken(email);
        refreshTokens.store(refresh, email);
        return new AuthResponse(access, refresh);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshRequest req) {
        // Проверка подписи/срока годности + что токен ещё живёт в Redis
        String email = jwtService.getSubject(req.getRefreshToken());
        if (!refreshTokens.exists(req.getRefreshToken())) {
            throw new BadCredentialsException("Refresh token is invalid or already used");
        }
        // одноразовый refresh
        refreshTokens.consume(req.getRefreshToken());

        var user = users.findByEmail(email).orElseThrow();
        String access = jwtService.generateAccessToken(email, Map.of("roles", user.getRoles()));
        String newRefresh = jwtService.generateRefreshToken(email);
        refreshTokens.store(newRefresh, email);
        return new AuthResponse(access, newRefresh);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@Valid @RequestBody RefreshRequest req) {
        refreshTokens.revoke(req.getRefreshToken());
    }
}
