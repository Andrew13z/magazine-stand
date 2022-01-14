package com.example.magazinestand.security.service;

import com.example.magazinestand.security.dto.UserDto;
import com.example.magazinestand.security.entity.UserEntity;
import com.example.magazinestand.security.enums.Role;
import com.example.magazinestand.security.exception.AuthenticationException;
import com.example.magazinestand.security.exception.UserExistsException;
import com.example.magazinestand.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var userEntity = userRepository.findById(username)
				.orElseThrow(() -> new AuthenticationException("User not found by username: " + username));
		var authority = new SimpleGrantedAuthority(userEntity.getRole().toString());
		return new User(userEntity.getUsername(), userEntity.getPassword(), List.of(authority));
	}

	public UserDetails createUser(UserDto userDto) {
		if (userRepository.findById(userDto.getUsername()).isPresent()) {
			throw new UserExistsException("User already exists with username: " + userDto.getUsername());
		}
		var userEntity = new UserEntity(userDto.getUsername(),
										passwordEncoder.encode(userDto.getPassword()),
										Role.USER);
		var savedUser = userRepository.save(userEntity);
		var authority = new SimpleGrantedAuthority(savedUser.getRole().toString());
		return new User(savedUser.getUsername(), savedUser.getPassword(), List.of(authority));
	}

}
