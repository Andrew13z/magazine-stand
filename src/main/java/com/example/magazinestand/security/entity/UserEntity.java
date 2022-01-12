package com.example.magazinestand.security.entity;


import com.example.magazinestand.security.enums.Role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class UserEntity {

	@Id
	@Column(name = "USERNAME")
	private String username;

	@Column(name = "PASS", nullable = false)
	private String password;

	@Column(name = "ROLE", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	public UserEntity() {
	}

	public UserEntity(String username, String password, Role role) {
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
