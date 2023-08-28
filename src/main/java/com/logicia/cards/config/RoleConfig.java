package com.logicia.cards.config;

import com.logicia.cards.model.Role;
import com.logicia.cards.model.User;
import com.logicia.cards.repository.RoleRepository;
import com.logicia.cards.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Component
@Slf4j
public class RoleConfig implements CommandLineRunner {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info(":::::::::::checking if roles exist, if it doesn't add them::::::::::::");
        String admin = "ADMIN";
        String member = "MEMBER";
        if (!roleRepository.findByName(admin).isPresent()) {
            Role role = new Role();
            role.setName(admin);
            roleRepository.save(role);
        }
        if (!roleRepository.findByName(member).isPresent()) {
            Role role = new Role();
            role.setName(member);
            roleRepository.save(role);
        }
        if(userRepository.findAll().size()<2){
            userRepository.deleteAll();
            log.info(":::::::::::adding admin and member user::::::::::::");
            User adminUser= new User();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setName("Administrator");
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            Role userRole = roleRepository.findById(1L).get();
            adminUser.setRoles(Collections.singleton(userRole));
            adminUser.setRoleId(0);
            userRepository.save(adminUser);

            User memberUser= new User();
            memberUser.setEmail("member@gmail.com");
            memberUser.setName("Member");
            memberUser.setUsername("member");
            memberUser.setPassword(passwordEncoder.encode("member123"));
            Role memberRole = roleRepository.findById(2L).get();
            memberUser.setRoles(Collections.singleton(memberRole));
            memberUser.setRoleId(0);
            userRepository.save(memberUser);

        }




    }
}