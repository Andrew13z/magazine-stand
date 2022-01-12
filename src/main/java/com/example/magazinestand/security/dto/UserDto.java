package com.example.magazinestand.security.dto;

import com.example.magazinestand.security.enums.Role;

public class UserDto {

	private String username;

	private String password;
	private Role role;

	public UserDto() {
	}

	public UserDto(String username, String password, Role role) {
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
