package com.be.controller;

import com.be.config.jwt.JwtTokenProvider;
import com.be.dto.ChangePasswordDto;
import com.be.entity.Users;
import com.be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping()
    public ResponseEntity<?> getAll() {
        List<Users> lstTest = userService.getAllUser();
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }

    @GetMapping("/allUserActivated")
    public ResponseEntity<?> getAllUserActivated() {
        List<Users> users = userService.getAllUserActivated();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/approve/{userId}")
    public ResponseEntity<?> approveUser(@PathVariable String userId) {
        Users userToUpdate = userService.getUserById(userId);
        if (userToUpdate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userToUpdate.setStatus(true);
        userService.saveUser(userToUpdate);
        return new ResponseEntity<>(userToUpdate, HttpStatus.OK);
    }

    @PutMapping("/unapprove/{userId}")
    public ResponseEntity<?> unapproveUser(@PathVariable String userId) {
        Users userToUpdate = userService.getUserById(userId);
        if (userToUpdate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userToUpdate.setStatus(false);
        userService.saveUser(userToUpdate);
        return new ResponseEntity<>(userToUpdate, HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestHeader("Authorization") String jwt, @RequestBody ChangePasswordDto changePasswordDto) {
        String username = jwtTokenProvider.getUsernameFromJwt(jwt.substring(7));
        String oldPassword = changePasswordDto.getOldPassword();
        String newPassword = changePasswordDto.getNewPassword();
        Users existingUser = userService.getUserByUserName(username);

        if (existingUser == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        if (!passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
            return new ResponseEntity<>("Old password is incorrect", HttpStatus.BAD_REQUEST);
        }

        existingUser.setPassword(passwordEncoder.encode(newPassword));
        userService.saveUser(existingUser);
        return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Long>> getUserStatistics() {
        Map<String, Long> stats = userService.getUserStatistics();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
