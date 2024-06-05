package com.be.controller;

import com.be.config.jwt.JwtTokenProvider;
import com.be.constant.RoleConstant;
import com.be.dto.AuthResponseDto;
import com.be.dto.LoginDto;
import com.be.dto.RegisterDto;
import com.be.entity.Roles;
import com.be.entity.Users;
import com.be.repository.RoleRepository;
import com.be.repository.UserRepository;
import com.be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class JwtDemoController {
    private final JwtTokenProvider provider;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword()));

            Users users = userService.getUserByUserName(loginDto.getUsername());
            if (users.isStatus()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                List<Roles> authorities = roleRepository.findAll();
                String rolesString = users.getRoles().stream()
                        .map(Roles::getName)
                        .collect(Collectors.joining(", "));
                String token = provider.generateToken(authentication, authorities);
                return new ResponseEntity<>(new AuthResponseDto(token, rolesString), HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("pending_approval", HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Username or password is incorrect", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        if (userService.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username is taken", HttpStatus.NOT_FOUND);
        }
        List<Roles> authorities = roleRepository.findAll();
        if (authorities.isEmpty()) {
            String[] rolesArray = {"ROLE_ADMIN", "ROLE_USER"};

            for (String roleName : rolesArray) {
                Roles role = new Roles();
                String newGuid = UUID.randomUUID().toString();
                role.setId(newGuid);
                role.setName(roleName);
                role.setDateCreated(new Date());
                role.setDateUpdated(new Date());
                roleRepository.save(role);
            }
        }
        // Kiểm tra nếu cơ sở dữ liệu người dùng trống
        if (userRepository.count() == 0) {
            Users defaultAdmin = new Users();
            String newGuid = UUID.randomUUID().toString();
            defaultAdmin.setId(newGuid);
            defaultAdmin.setFullName("Admin");
            defaultAdmin.setEmail("admin@example.com");
            defaultAdmin.setUsername("admin");
            defaultAdmin.setStatus(true);
            defaultAdmin.setPassword(encoder.encode("admin")); // Mật khẩu mặc định
            defaultAdmin.setDateCreated(new Date());
            defaultAdmin.setDateUpdated(new Date());
            defaultAdmin.setRoles(roleRepository.findByName(RoleConstant.ROLE_ADMIN));
            userRepository.save(defaultAdmin);
        }

        // Tạo người dùng mới
        Users user = new Users();
        String newGuid = UUID.randomUUID().toString();
        user.setId(newGuid);
        user.setFullName(registerDto.getFullName());
        user.setEmail(registerDto.getEmail());
        user.setUsername(registerDto.getUsername());
        user.setPassword(encoder.encode(registerDto.getPassword()));
        user.setDateCreated(new Date());
        user.setDateUpdated(new Date());
        user.setRoles(roleRepository.findByName(RoleConstant.ROLE_USER));
        userRepository.save(user);

        return new ResponseEntity<>("Created Successfully", HttpStatus.CREATED);
    }

    @PostMapping("/request-reset-password")
    public ResponseEntity<?> requestResetPassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        Users user = userRepository.findByUsername(username);

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        // Tạo token và lưu nó vào database
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        userRepository.save(user);

        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        Users user = userRepository.findByUsername(username);

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        // Xác minh token
        if (!token.equals(user.getResetPasswordToken())) {
            return new ResponseEntity<>("Invalid token", HttpStatus.BAD_REQUEST);
        }

        // Đặt lại mật khẩu
        user.setPassword(encoder.encode(newPassword));
        user.setResetPasswordToken(null); // Xóa token sau khi đặt lại mật khẩu
        userRepository.save(user);

        return new ResponseEntity<>("Password has been reset", HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }
}
