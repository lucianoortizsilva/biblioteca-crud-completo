package com.lucianoortizsilva.crud.pessoa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucianoortizsilva.crud.pessoa.controller.PessoaController;
import com.lucianoortizsilva.crud.pessoa.model.Pessoa;
import com.lucianoortizsilva.crud.pessoa.service.PessoaService;
import com.lucianoortizsilva.crud.pessoa.util.PessoaStub;
import com.lucianoortizsilva.crud.seguranca.token.TokenJWT;

@Disabled
@WebMvcTest(controllers = PessoaController.class)
@ExtendWith(value = SpringExtension.class)
class PessoaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private TokenJWT jwtUtil;

	@MockBean
	private PessoaService pessoaService;

	@Test
	@WithMockUser
	@DisplayName("DADO QUE estou logado, QUANDO pesquiso uma pessoa por id, Então ele deverá ser retornado com status 200")
	void test_1() throws Exception {
		final Optional<Pessoa> clienteEsperado = PessoaStub.getPessoa();
		Mockito.when(this.pessoaService.findById(1L)).thenReturn(clienteEsperado.get());
		final MvcResult mvcResult = this.mockMvc.perform(get("/pessoas/1").contentType("application/json")).andExpect(status().isOk()).andReturn();
		final String clienteRetornado = mvcResult.getResponse().getContentAsString();
		assertThat(clienteRetornado).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(clienteEsperado));
	}
	
	@Test
	@WithMockUser
	@DisplayName("DADO QUE estou logado, QUANDO pesquiso uma pessoa por id, E o mesmo não existe, Então deverá ser retornado o status 404")
	void test_2() throws Exception {
		Mockito.when(this.pessoaService.findById(1L)).thenReturn(null);
		this.mockMvc.perform(get("/pessoas/1").contentType("application/json")).andExpect(status().isNotFound()).andReturn();
	}
	
	@Test
	@DisplayName("DADO QUE não estou logado, QUANDO pesquiso um pessoa por id, Então deverá ser retornado o status 403")
	void test_3() throws Exception {
		this.mockMvc.perform(get("/pessoas/1").contentType("application/json")).andExpect(status().isForbidden());
	}

}