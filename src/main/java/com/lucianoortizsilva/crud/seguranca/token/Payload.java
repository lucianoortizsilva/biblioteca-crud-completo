package com.lucianoortizsilva.crud.seguranca.token;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Payload {

	@SerializedName("sub")
	private String login;

	@SerializedName(value = "exp")
	private Integer expiration;

}