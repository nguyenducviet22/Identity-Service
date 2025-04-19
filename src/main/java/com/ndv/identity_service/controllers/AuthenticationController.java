package com.ndv.identity_service.controllers;

import com.ndv.identity_service.domain.dtos.request.*;
import com.ndv.identity_service.services.AuthenticationService;
import com.ndv.identity_service.domain.dtos.response.AuthenticationResponse;
import com.ndv.identity_service.domain.dtos.response.IntrospectResponse;
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
        var rs = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(rs)
                .build();
    }

    @PostMapping("introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws Exception {
        var rs = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(rs)
                .build();
    }

    @PostMapping("logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws Exception {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("refresh")
    ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshRequest request) throws Exception {
        var rs = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(rs)
                .build();
    }
}
