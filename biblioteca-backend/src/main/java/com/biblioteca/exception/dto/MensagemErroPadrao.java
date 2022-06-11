package com.biblioteca.exception.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MensagemErroPadrao implements Serializable {

	private static final long serialVersionUID = -5291236777168402790L;
	private final String datetime = LocalDateTime.now().toString();
	private final Long timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	private Integer status;
	private String erro;
	private String mensagem;
	private String path;

}