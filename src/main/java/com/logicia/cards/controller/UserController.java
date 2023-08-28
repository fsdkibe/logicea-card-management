package com.logicia.cards.controller;

import com.logicia.cards.dto.JWTAuthResponse;
import com.logicia.cards.dto.LoginDto;
import com.logicia.cards.dto.SignUpRequest;
import com.logicia.cards.model.User;
import com.logicia.cards.repository.RoleRepository;
import com.logicia.cards.repository.UserRepository;
import com.logicia.cards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cards/users")
public class UserController {
    private UserService userService;

        @Autowired
         UserRepository userRepository;
         @Autowired
         RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Build Login REST API
    @PostMapping(value = {"/signin"})
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto){
        String token = userService.login(loginDto);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }

    // Build Register REST API
    @PostMapping(value = {"/signup"})
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest){
        return userService.signup(signUpRequest);
    }
    @PostMapping(value = {"/register"})
    public ResponseEntity<?> register(@RequestBody SignUpRequest signUpRequest){
        return userService.register(signUpRequest);
    }


    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable long userId, SignUpRequest signUpRequest){
        return userService.updateUser(userId, signUpRequest);

    }
    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> findAll(){
        return userService.findAll();

    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> findUserById(@PathVariable long id){
        return userService.findUserById(id);

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable long id){
        return userService.deleteById(id);

    }
}
