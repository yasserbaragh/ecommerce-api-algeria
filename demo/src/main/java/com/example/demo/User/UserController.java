package com.example.demo.User;

import com.example.demo.Config.JwtHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;

    @PostMapping("/register")
    public ResponseEntity<Void> signup(@Valid @RequestBody RegisterRecord request) {
        userService.signup(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        String token = jwtHelper.generateToken(request.email());
        return ResponseEntity.ok(new LoginResponse(request.email(), token));
    }
}
