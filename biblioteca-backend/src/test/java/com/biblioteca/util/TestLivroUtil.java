package com.biblioteca.util;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.apache.commons.lang3.RandomUtils.nextLong;

import java.time.LocalDate;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestLivroUtil {

	public static Long qualquerID() {
		return nextLong();
	}

	public static String qualquerISBN() {
		return randomNumeric(11);
	}

	public static String isbnMaxCaracterExcedido() {
		return randomNumeric(12);
	}

	public static String qualquerDescricao() {
		return randomAlphanumeric(120);
	}

	public static String descricaoMaxCaracterExcedido() {
		return randomAlphanumeric(121);
	}

	public static LocalDate qualquerDate() {
		return LocalDate.now();
	}

}