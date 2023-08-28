package com.logicia.cards.service;


import com.logicia.cards.dto.LoginDto;
import com.logicia.cards.dto.SignUpRequest;
import com.logicia.cards.model.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    String login(LoginDto loginDto);

    ResponseEntity<?> signup(SignUpRequest signUpRequest);
    ResponseEntity<?> register(SignUpRequest signUpRequest);
    public ResponseEntity<?> updateUser(Long userId,SignUpRequest signUpRequest);
    public ResponseEntity<?> findUserById(long id);
    public ResponseEntity<?> findAll();
    public ResponseEntity<?> deleteById(long id);
}
