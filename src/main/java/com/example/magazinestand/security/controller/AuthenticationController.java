package com.example.magazinestand.security.controller;

import com.example.magazinestand.security.dto.AuthenticationResponse;
import com.example.magazinestand.security.dto.UserDto;
import com.example.magazinestand.security.service.CustomUserDetailsService;
import com.example.magazinestand.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

	private final CustomUserDetailsService userDetailsService;

	private final AuthenticationManager authenticationManager;

	private final JwtUtil jwtUtil;

	@Autowired
	public AuthenticationController(CustomUserDetailsService userDetailsService,
									AuthenticationManager authenticationManager,
									JwtUtil jwtUtil) {
		this.userDetailsService = userDetailsService;
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/login")
	public AuthenticationResponse createAuthenticationToken(@RequestBody UserDto userDto) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
		var userDetails = userDetailsService.loadUserByUsername(userDto.getUsername());
		return new AuthenticationResponse(jwtUtil.generateToken(userDetails));
	}

	@PostMapping("/register")
	public AuthenticationResponse registerUserAndCreateAuthenticationToken(@RequestBody UserDto userDto) {
		var userDetails = userDetailsService.createUser(userDto);
		return new AuthenticationResponse(jwtUtil.generateToken(userDetails));
	}

}
