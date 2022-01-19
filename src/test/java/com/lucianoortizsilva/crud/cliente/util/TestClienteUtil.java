package com.lucianoortizsilva.crud.cliente.util;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.apache.commons.lang3.RandomUtils.nextLong;

import java.time.LocalDate;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestClienteUtil {

	public static Long qualquerID() {
		return nextLong();
	}

	public static String qualquerCpf() {
		return randomNumeric(11);
	}

	public static String cpfMaxCaracterExcedido() {
		return randomNumeric(12);
	}

	public static String qualquerNome() {
		return randomAlphanumeric(120);
	}

	public static String nomeMaxCaracterExcedido() {
		return randomAlphanumeric(121);
	}

	public static LocalDate qualquerDate() {
		return LocalDate.now();
	}

}