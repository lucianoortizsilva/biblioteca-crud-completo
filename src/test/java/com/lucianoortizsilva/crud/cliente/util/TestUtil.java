package com.lucianoortizsilva.crud.cliente.util;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.apache.commons.lang3.RandomUtils.nextLong;

import java.time.LocalDate;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtil {

	public static Long qualquerLong() {
		return nextLong();
	}

	public static String qualquerCPF() {
		return randomNumeric(11);
	}

	public static String cpfMaxExcedido() {
		return randomNumeric(12);
	}

	public static String qualquerString() {
		return randomAlphanumeric(10);
	}

	public static String nomeMaxExcedido() {
		return randomAlphanumeric(121);
	}

	public static LocalDate qualquerDate() {
		return LocalDate.now();
	}

}