package com.pklinh.student_management.controller;

import com.pklinh.student_management.dto.request.AuthenticationRequest;
import com.pklinh.student_management.dto.response.ApiResponse;
import com.pklinh.student_management.dto.response.AuthenticationResponse;
import com.pklinh.student_management.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        boolean authenticated = authenticationService.authenticate(request);
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .authenticated(authenticated)
                .build();
        ApiResponse<AuthenticationResponse> apiResponse = ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationResponse)
                .build();
        return apiResponse;
    }
}
