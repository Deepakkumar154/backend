package org.tragoit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tragoit.config.UserAuthenticationProvider;
import org.tragoit.dto.AgentRequestDto;
import org.tragoit.dto.LoginRequestDto;
import org.tragoit.dto.UserDto;
import org.tragoit.service.UserService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @PostMapping("/register")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto) {
        UserDto createdUser = userService.saveUser(userDto);
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }

    @PostMapping("/agent/register")
    public ResponseEntity<UserDto> saveAgentUser(@RequestBody AgentRequestDto userDto) {
        UserDto createdUser = userService.saveAgentUser(userDto);
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        UserDto userDto = userService.login(loginRequestDto);
        userDto.setToken(userAuthenticationProvider.createToken(userDto.getEmail()));
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/agent/login")
    public ResponseEntity<UserDto> agentLogin(@RequestBody LoginRequestDto loginRequestDto) {
        UserDto userDto = userService.agentLogin(loginRequestDto);
        userDto.setToken(userAuthenticationProvider.createToken(userDto.getEmail()));

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/all")
    List<UserDto> getAllUsers() {
        return userService.findAllUsers();
    }
}
