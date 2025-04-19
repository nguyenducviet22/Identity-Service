package com.ndv.identity_service.services;

import com.ndv.identity_service.domain.dtos.request.AuthenticationRequest;
import com.ndv.identity_service.domain.dtos.request.IntrospectRequest;
import com.ndv.identity_service.domain.dtos.request.LogoutRequest;
import com.ndv.identity_service.domain.dtos.response.AuthenticationResponse;
import com.ndv.identity_service.domain.dtos.response.IntrospectResponse;
import com.ndv.identity_service.domain.entities.InvalidatedToken;
import com.ndv.identity_service.domain.entities.User;
import com.ndv.identity_service.repositories.InvalidatedTokenRepository;
import com.ndv.identity_service.repositories.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    private final Long jwtExpiryMs = 86400000L;

    @Value("${jwt.secret}")
    private String secretKey;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Incorrect Username or Password!"));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) throw new BadCredentialsException("Incorrect Username or Password!");

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    private String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("ndv.com")
                .issueTime(new Date(System.currentTimeMillis()))
                .expirationTime(new Date(System.currentTimeMillis() + jwtExpiryMs))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token!", e);
            throw new RuntimeException(e);
        }
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws Exception {
        boolean isValid = true;
        try {
            verifyToken(request.getToken());
        } catch (Exception ex) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }
        return stringJoiner.toString();
    }

    public void logout(LogoutRequest request) throws Exception {
        var signToken = verifyToken(request.getToken());
        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }

    private SignedJWT verifyToken(String token) throws Exception {
        JWSVerifier verifier = new MACVerifier(secretKey.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date(System.currentTimeMillis()))))
            throw new Exception();

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new Exception();

        return signedJWT;
    }
}
