package com.example.taskmanager.user;

import com.example.taskmanager.common.NotFoundException;
import com.example.taskmanager.user.dto.UserCreateDto;
import com.example.taskmanager.user.dto.UserReadDto;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserReadDto create(UserCreateDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User u = new User();
        u.setEmail(dto.getEmail());
        u.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        u.setFullName(dto.getFullName());
        u.setRoles(Set.of(Role.USER));
        u.setCreatedAt(OffsetDateTime.now());
        u = userRepository.save(u);
        return toReadDto(u);
    }

    public UserReadDto get(Long id) {
        User u = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return toReadDto(u);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) throw new NotFoundException("User not found");
        userRepository.deleteById(id);
    }

    private UserReadDto toReadDto(User u) {
        UserReadDto d = new UserReadDto();
        d.setId(u.getId());
        d.setEmail(u.getEmail());
        d.setFullName(u.getFullName());
        d.setRoles(u.getRoles().stream().map(Enum::name).collect(Collectors.toSet()));
        d.setCreatedAt(u.getCreatedAt());
        return d;
    }
}

