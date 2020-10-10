package com.lucianoortizsilva.crud.cliente;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.lucianoortizsilva.crud.seguranca.autenticacao.JWTUtil;

@WebMvcTest(controllers = ClienteController.class)
@ExtendWith(value = SpringExtension.class)
public class ClienteControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private JWTUtil jwtUtil;

	@MockBean
	private ClienteService clienteService;

	@Test
	@DisplayName("Quando pesquiso um cliente por id, E não estou autenticado na aplicação, Então deverá retornar o status 403")
	void test_1() throws Exception {
		this.mockMvc.perform(get("/clientes/1").contentType("application/json")).andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser
	@DisplayName("Quando pesquiso um cliente por id, E o mesmo existe, Então ele deverá ser retornado com status 200")
	void test_2() throws Exception {
		final Cliente clienteEsperado = ClienteStub.getCliente();
		Mockito.when(this.clienteService.findById(1L)).thenReturn(clienteEsperado);
		final MvcResult mvcResult = this.mockMvc.perform(get("/clientes/1").contentType("application/json")).andExpect(status().isOk()).andReturn();
		final String clienteRetornado = mvcResult.getResponse().getContentAsString();
		assertThat(clienteRetornado).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(clienteEsperado));
	}
	
	@Test
	@WithMockUser
	@DisplayName("Quando pesquiso um cliente por id, E o mesmo existe, Então ele deverá ser retornado com status 404")
	void test_3() throws Exception {
		Mockito.when(this.clienteService.findById(1L)).thenReturn(null);
		this.mockMvc.perform(get("/clientes/1").contentType("application/json")).andExpect(status().isNotFound()).andReturn();
	}
	
}