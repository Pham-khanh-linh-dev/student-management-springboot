package com.pklinh.student_management.controller;

import com.pklinh.student_management.dto.request.UserCreationRequest;
import com.pklinh.student_management.dto.request.UserUpdateRequest;
import com.pklinh.student_management.dto.response.ApiResponse;
import com.pklinh.student_management.dto.response.UserResponse;
import com.pklinh.student_management.entity.User;
import com.pklinh.student_management.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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
    public ApiResponse<List<UserResponse>> getUsers(){
//    Lấy thông tin user đang trong request, context
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info("Role: {}", grantedAuthority.getAuthority()));


        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build()
                ;
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable String id){
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable String id, @RequestBody UserUpdateRequest request){
        return userService.updateUser(id,request);
    }

    @GetMapping("/my-info")
    public ApiResponse<UserResponse> getMyInfo(){

        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build()
                ;
    }
}
