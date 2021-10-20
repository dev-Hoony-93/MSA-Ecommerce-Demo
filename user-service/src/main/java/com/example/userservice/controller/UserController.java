package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.ServerRequest;

import java.net.URL;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-service")
public class UserController {

    private final Environment env;
    private final UserService userService;
    private final Greeting greeting;


    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in User Service Port : {%s}",env.getProperty("local.server.port"));
    }

    @GetMapping("/welcome")
    public String welcome(){
//        return env.getProperty("greeting.message");
        return greeting.getMeesage();
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user){

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto , ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);

    }

}
