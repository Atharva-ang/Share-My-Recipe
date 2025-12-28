package com.recipe.recipe_platform.controller;

import com.recipe.recipe_platform.dto.*;
import com.recipe.recipe_platform.model.Role;
import com.recipe.recipe_platform.model.User;
import com.recipe.recipe_platform.repository.UserRepository;
import com.recipe.recipe_platform.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/signup")
    public String registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.email())) {
            return "Error: Email is already in use!";
        }

        // Create new user's account
        User user = new User();
        user.setEmail(signUpRequest.email());
        user.setHandle(signUpRequest.handle());
        user.setPassword(encoder.encode(signUpRequest.password())); // HASH THE PASSWORD!

        // Convert string role to Enum
        user.setRole(Role.valueOf(signUpRequest.role().toUpperCase()));

        userRepository.save(user);
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public JwtResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
        // 1. Authenticate the user (Spring checks the hashed password automatically)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 2. Generate the Token
        String jwt = jwtUtils.generateTokenFromUsername(loginRequest.email());

        // 3. Return the token details
        org.springframework.security.core.userdetails.User userDetails =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        return new JwtResponse(jwt, userDetails.getUsername(), role);
    }
}