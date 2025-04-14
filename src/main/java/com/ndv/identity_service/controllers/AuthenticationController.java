package com.ndv.identity_service.controllers;

import com.ndv.identity_service.Services.AuthenticationService;
import com.ndv.identity_service.domain.dtos.request.ApiResponse;
import com.ndv.identity_service.domain.dtos.request.AuthenticationRequest;
import com.ndv.identity_service.domain.dtos.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        boolean rs = authenticationService.authenticate(request);
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .authenticated(rs)
                .build();
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationResponse)
                .build();
    }
}
