package com.lucianoortizsilva.crud.cliente;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

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
import com.lucianoortizsilva.crud.cliente.controller.ClienteController;
import com.lucianoortizsilva.crud.cliente.model.Cliente;
import com.lucianoortizsilva.crud.cliente.service.ClienteService;
import com.lucianoortizsilva.crud.cliente.util.ClienteStub;
import com.lucianoortizsilva.crud.seguranca.autenticacao.UserDetailsCustom;
import com.lucianoortizsilva.crud.seguranca.token.TokenJWT;

@WebMvcTest(controllers = ClienteController.class)
@ExtendWith(value = SpringExtension.class)
class ClienteControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private TokenJWT jwtUtil;

	@MockBean
	private ClienteService clienteService;

	@Test
	@WithMockUser
	@DisplayName("DADO QUE estou logado, QUANDO pesquiso um cliente por id, Então ele deverá ser retornado com status 200")
	void test_1() throws Exception {
		final Optional<Cliente> clienteEsperado = ClienteStub.getCliente();
		Mockito.when(this.clienteService.findById(1L, new UserDetailsCustom())).thenReturn(clienteEsperado);
		final MvcResult mvcResult = this.mockMvc.perform(get("/clientes/1").contentType("application/json")).andExpect(status().isOk()).andReturn();
		final String clienteRetornado = mvcResult.getResponse().getContentAsString();
		assertThat(clienteRetornado).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(clienteEsperado));
	}
	
	@Test
	@WithMockUser
	@DisplayName("DADO QUE estou logado, QUANDO pesquiso um cliente por id, E o mesmo não existe, Então deverá ser retornado o status 404")
	void test_2() throws Exception {
		Mockito.when(this.clienteService.findById(1L, new UserDetailsCustom())).thenReturn(Optional.ofNullable(null));
		this.mockMvc.perform(get("/clientes/1").contentType("application/json")).andExpect(status().isNotFound()).andReturn();
	}
	
	@Test
	@DisplayName("DADO QUE não estou logado, QUANDO pesquiso um cliente por id, Então deverá ser retornado o status 403")
	void test_3() throws Exception {
		this.mockMvc.perform(get("/clientes/1").contentType("application/json")).andExpect(status().isForbidden());
	}

}