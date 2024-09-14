package com.momand.docauthor.jwt.controller;

import com.momand.docauthor.jwt.model.AuthenticationRequest;
import com.momand.docauthor.jwt.model.AuthenticationResponse;
import com.momand.docauthor.jwt.service.ApplicationUserDetailsService;
import com.momand.docauthor.jwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    @Autowired
    private ApplicationUserDetailsService applicationUserDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;


    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, ApplicationUserDetailsService applicationUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.applicationUserDetailsService = applicationUserDetailsService;
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return new AuthenticationResponse(jwt);
    }
}
