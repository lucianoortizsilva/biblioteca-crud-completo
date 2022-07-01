package com.biblioteca.security.token;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Payload {

	@SerializedName(value = "dhExpiration")
	private Long expiration;

	@SerializedName(value = "authorities")
	private List<String> authorities;

	@SerializedName("username")
	private String login;

	@SerializedName("firstName")
	private String firstName;

	@SerializedName("lastName")
	private String lastName;

}