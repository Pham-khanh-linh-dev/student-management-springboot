package com.pklinh.student_management.controller;

import com.nimbusds.jose.JOSEException;
import com.pklinh.student_management.dto.request.AuthenticationRequest;
import com.pklinh.student_management.dto.request.IntrospectRequest;
import com.pklinh.student_management.dto.request.LogoutRequest;
import com.pklinh.student_management.dto.response.ApiResponse;
import com.pklinh.student_management.dto.response.AuthenticationResponse;
import com.pklinh.student_management.dto.response.IntrospectResponse;
import com.pklinh.student_management.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.core.JacksonException;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        var authenticationResult = authenticationService.authenticate(request);

        ApiResponse<AuthenticationResponse> apiResponse = ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationResult)
                .build();
        return apiResponse;
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> Introspect(@RequestBody IntrospectRequest request) throws JOSEException, ParseException {
        var introspectResult = authenticationService.introspect(request);

        ApiResponse<IntrospectResponse> apiResponse = ApiResponse.<IntrospectResponse>builder()
                .result(introspectResult)
                .build();
        return apiResponse;
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws JOSEException, ParseException {
        authenticationService.logout(request);

        return ApiResponse.<Void>builder()
                .build();
    }
}
