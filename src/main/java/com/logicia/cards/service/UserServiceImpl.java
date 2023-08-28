package com.logicia.cards.service;


import com.logicia.cards.dto.LoginDto;
import com.logicia.cards.dto.ResponseDto;
import com.logicia.cards.dto.SignUpRequest;
import com.logicia.cards.model.Role;
import com.logicia.cards.model.User;
import com.logicia.cards.repository.RoleRepository;
import com.logicia.cards.repository.UserRepository;
import com.logicia.cards.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private ResponseDto responseDto;


    public UserServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }

    @Override
    public ResponseEntity<?> signup(SignUpRequest signUpRequest) {
        responseDto=new ResponseDto();
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setDescription("Email Address already in use!");
            return new ResponseEntity(responseDto,
                    responseDto.getStatus());
        }
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {

            responseDto.setDescription("Username is already taken!");
            return new ResponseEntity(responseDto,
                    responseDto.getStatus());
        }

        // Creating user's account
        User user = setUpUser(signUpRequest);
       // by default give user member role
        // to create admin user we shall have to manually do it on db for now
        Role userRole = roleRepository.findByName("MEMBER").get();
        user.setRoles(Collections.singleton(userRole));
        user.setRoleId(signUpRequest.getRoleId());

        userRepository.save(user);

        responseDto.setStatus(HttpStatus.CREATED);
        responseDto.setDescription("User Created successfully");
        return new ResponseEntity(responseDto,
                responseDto.getStatus());

    }
    public ResponseEntity<?> register(@RequestBody SignUpRequest signUpRequest) {
        responseDto=new ResponseDto();
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setDescription("Email Address already in use!");
            return new ResponseEntity(responseDto,
                    responseDto.getStatus());
        }
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {

            responseDto.setDescription("Username is already taken!");
            return new ResponseEntity(responseDto,
                    responseDto.getStatus());
        }

        // Creating user's account
        User user = setUpUser(signUpRequest);

        Role userRole = roleRepository.findById(signUpRequest.getRoleId()).get();
        user.setRoles(Collections.singleton(userRole));
        user.setRoleId(signUpRequest.getRoleId());

        userRepository.save(user);

        responseDto.setStatus(HttpStatus.CREATED);
        responseDto.setDescription("User Created successfully");
        return new ResponseEntity(responseDto,
                responseDto.getStatus());
    }
    public ResponseEntity<?> updateUser(Long userId,SignUpRequest signUpRequest){
        responseDto=new ResponseDto();
        try{
            User user= userRepository.findById(userId).get();
            user=setUpUser(signUpRequest);
            Role userRole = roleRepository.findById(signUpRequest.getRoleId()).get();
            user.setRoles(Collections.singleton(userRole));
            user.setRoleId(signUpRequest.getRoleId());
            userRepository.save(user);
            responseDto.setStatus(HttpStatus.ACCEPTED);
            responseDto.setDescription("User Updated Successfully");

            return new ResponseEntity<>(responseDto, HttpStatus.OK);

        }catch(Exception e){
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setDescription("User not Updated");

            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        }

    }
    private User setUpUser(SignUpRequest signUpRequest){
        User user=new User();
        user.setEmail(signUpRequest.getEmail());
        user.setName(signUpRequest.getName());
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        return user;

    }

    public ResponseEntity<?> findUserById(long id){
        responseDto =new ResponseDto();

        try{

            return new ResponseEntity<>( userRepository.findById(id).get(),HttpStatus.OK);

        }catch(Exception e){
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setDescription("User Not Found");
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<?> findAll(){

        try{

            return new ResponseEntity<>( userRepository.findAll(),HttpStatus.OK);

        }catch(Exception e){
            responseDto =new ResponseDto();
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setDescription("No User Found");
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> deleteById(long id){
        responseDto =new ResponseDto();
        try{
            responseDto.setStatus(HttpStatus.ACCEPTED);
            responseDto.setDescription("User deleted successfully");
            userRepository.deleteById(id);
            return new ResponseEntity<>(responseDto,HttpStatus.OK);

        }catch(Exception e){
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setDescription("User with that id not found");
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }
    }
}
