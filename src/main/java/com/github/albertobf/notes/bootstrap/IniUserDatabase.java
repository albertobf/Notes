package com.github.albertobf.notes.bootstrap;

import com.github.albertobf.notes.model.Role;
import com.github.albertobf.notes.model.User;
import com.github.albertobf.notes.repository.RoleRepository;
import com.github.albertobf.notes.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class IniUserDatabase implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public IniUserDatabase(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Role role = new Role();
        role.setRole("USER");
        roleRepository.save(role);

        User user = new User();
        user.setUsername("albertobf");
        user.setPassword(passwordEncoder.encode("alberto"));
        user.setRoles(new HashSet<>(Arrays.asList(role)));
        userRepository.save(user);
    }
}
