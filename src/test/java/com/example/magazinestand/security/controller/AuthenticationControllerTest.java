package com.example.magazinestand.security.controller;

import com.example.magazinestand.security.dto.UserDto;
import com.example.magazinestand.security.exception.AuthenticationException;
import com.example.magazinestand.security.exception.UserExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.prometheus.client.CollectorRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest( properties = {"spring.main.allow-bean-definition-overriding=true"})
@ActiveProfiles("test")
@Sql(value = {"classpath:drop-auth.sql", "classpath:init-auth.sql"})
class AuthenticationControllerTest {

	@TestConfiguration
	static class TestConfig {
		@Bean
		public PasswordEncoder passwordEncoder() {
			return NoOpPasswordEncoder.getInstance();
		}

		@Bean
		public CollectorRegistry collectorRegistry() {
			return new CollectorRegistry();
		}
	}

	private static final UserDto USER_DTO = new UserDto("user", "pass", null);

	private final ObjectMapper objectMapper = new ObjectMapper();
	private MockMvc mockMvc;

	@BeforeEach
	void setUp(WebApplicationContext wac) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	void testLoginWithValidCredentials() throws Exception {
		mockMvc.perform(post("/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(USER_DTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jwt").isNotEmpty());
	}

	@Test
	void testLoginWithInvalidUsername() throws Exception {
		mockMvc.perform(post("/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new UserDto("not a username", "pass", null))))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void testLoginWithInvalidPassword() throws Exception {
		mockMvc.perform(post("/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new UserDto("user", "not a pass", null))))
				.andExpect(status().isUnauthorized())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof AuthenticationException));
	}

	@Test
	void testCreateUser() throws Exception {
		mockMvc.perform(post("/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new UserDto("newUser", "pass", null))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jwt").isNotEmpty());
	}

	@Test
	void testCreateUserWithExistingUsername() throws Exception {
		var mvcResult = mockMvc.perform(post("/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(USER_DTO)))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof UserExistsException));
	}
}
