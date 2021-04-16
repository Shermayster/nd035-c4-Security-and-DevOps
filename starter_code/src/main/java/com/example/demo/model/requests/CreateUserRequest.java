package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public class CreateUserRequest {

	@JsonProperty
	private String username;

	@JsonProperty
	private String password;

	@JsonProperty
	private String confirmPassword;

	public String getUsername() {
		return Optional.ofNullable(username).orElseGet(String::new);
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public String getPassword() {
		return Optional.ofNullable(password).orElseGet(String::new);
	}

	public String getConfirmPassword() {
		return Optional.ofNullable(confirmPassword).orElseGet(String::new);
	}
}
