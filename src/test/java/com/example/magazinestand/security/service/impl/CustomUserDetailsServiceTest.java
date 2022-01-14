package com.example.magazinestand.security.service.impl;

import com.example.magazinestand.security.dto.UserDto;
import com.example.magazinestand.security.entity.UserEntity;
import com.example.magazinestand.security.enums.Role;
import com.example.magazinestand.security.exception.AuthenticationException;
import com.example.magazinestand.security.exception.UserExistsException;
import com.example.magazinestand.security.repository.UserRepository;
import com.example.magazinestand.security.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

	private static final String USERNAME = "USER";
	private static final String PASSWORD = "PASS";
	private static final Role ROLE = Role.USER;

	private final UserRepository repository = mock(UserRepository.class);
	private final PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
	private final CustomUserDetailsService service = new CustomUserDetailsService(repository, encoder);

	@Test
	void loadUserByUserNameTestWithExistingUsername(){
		when(repository.findById(USERNAME)).thenReturn(Optional.of(new UserEntity(USERNAME, PASSWORD, ROLE)));
		var loadedUser = service.loadUserByUsername(USERNAME);

		assertEquals(USERNAME, loadedUser.getUsername());
		assertEquals(PASSWORD, loadedUser.getPassword());
		assertTrue(loadedUser.getAuthorities().contains(new SimpleGrantedAuthority(Role.USER.toString())));
	}

	@Test
	void loadUserByUserNameTestWithNOtExistingUsername(){
		when(repository.findById(USERNAME)).thenReturn(Optional.empty());
		assertThrows(AuthenticationException.class, () -> service.loadUserByUsername(USERNAME));
	}

	@Test
	void createUserWithValidData(){
		var userDto = new UserDto(USERNAME, PASSWORD, null);

		when(repository.findById(USERNAME)).thenReturn(Optional.empty());
		when(repository.save(any(UserEntity.class))).then(returnsFirstArg());
		var createdUser = service.createUser(userDto);

		assertEquals(USERNAME, createdUser.getUsername());
		assertEquals(PASSWORD, createdUser.getPassword());
		assertTrue(createdUser.getAuthorities().contains(new SimpleGrantedAuthority(Role.USER.toString())));
	}

	@Test
	void createUserWithExistingUsername(){
		var userDto = new UserDto(USERNAME, PASSWORD, null);
		when(repository.findById(USERNAME)).thenReturn(Optional.of(new UserEntity(USERNAME, PASSWORD, null)));

		assertThrows(UserExistsException.class, () -> service.createUser(userDto));
	}
}
