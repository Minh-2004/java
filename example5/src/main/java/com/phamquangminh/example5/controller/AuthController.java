package com.phamquangminh.example5.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phamquangminh.example5.exceptions.UserNotFoundException;
import com.phamquangminh.example5.payloads.LoginCredentials;
import com.phamquangminh.example5.payloads.UserDTO;
import com.phamquangminh.example5.security.JWTUtil;
import com.phamquangminh.example5.service.UserService;
import com.phamquangminh.example5.repository.UserRepo;
import com.phamquangminh.example5.entity.User;
import com.phamquangminh.example5.entity.Role;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class AuthController {

        @Autowired
        private UserService userService;

        @Autowired
        private JWTUtil jwtUtil;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private UserRepo userRepo;

        @PostMapping("/register")
        public ResponseEntity<Map<String, Object>> registerHandler(@Valid @RequestBody UserDTO user)
                        throws UserNotFoundException {
                String encodedPass = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPass);

                UserDTO userDTO = userService.registerUser(user);
                String token = jwtUtil.generateToken(userDTO.getEmail());

                return new ResponseEntity<>(
                                Collections.singletonMap("jwt-token", token),
                                HttpStatus.CREATED);
        }

        @PostMapping("/login")
        public Map<String, Object> loginHandler(@Valid @RequestBody LoginCredentials credentials) {
                UsernamePasswordAuthenticationToken authCredentials = new UsernamePasswordAuthenticationToken(
                                credentials.getEmail(),
                                credentials.getPassword());

                authenticationManager.authenticate(authCredentials);

                // ✅ Tạo JWT
                String token = jwtUtil.generateToken(credentials.getEmail());

                // ✅ Lấy user từ email
                User user = userRepo.findByEmail(credentials.getEmail())
                                .orElseThrow(
                                                () -> new UsernameNotFoundException("User not found with email: "
                                                                + credentials.getEmail()));

                // ✅ Chuẩn bị response
                Map<String, Object> response = new HashMap<>();
                response.put("jwt-token", token);
                response.put("userId", user.getUserId());
                response.put("email", user.getEmail());
                response.put("fullName", user.getFirstName() + " " + user.getLastName());
                response.put("role", user.getRoles().stream().map(Role::getRoleName).toList());

                return response;
        }

}
