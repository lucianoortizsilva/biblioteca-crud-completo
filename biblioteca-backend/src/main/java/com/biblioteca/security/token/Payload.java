package com.biblioteca.security.token;

import java.util.List;

import com.biblioteca.security.authentication.user.Role;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Payload {

	@SerializedName("username")
	private String login;

	@SerializedName(value = "exp")
	private Long expiration;

	@SerializedName(value = "roles")
	private List<Role> roles;

}