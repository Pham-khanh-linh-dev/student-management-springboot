package com.pklinh.student_management.controller;

import com.pklinh.student_management.dto.request.UserCreationRequest;
import com.pklinh.student_management.dto.request.UserUpdateRequest;
import com.pklinh.student_management.dto.response.ApiResponse;
import com.pklinh.student_management.dto.response.UserResponse;
import com.pklinh.student_management.entity.User;
import com.pklinh.student_management.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ApiResponse<User> createUser(@Valid @RequestBody UserCreationRequest request){
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
    }
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable String id){
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable String id, @RequestBody UserUpdateRequest request){
        return userService.updateUser(id,request);
    }
}
