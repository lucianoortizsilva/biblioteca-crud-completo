package com.lucianoortizsilva.crud.cliente.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.lucianoortizsilva.crud.cliente.annotation.WithMockUserAdmin;
import com.lucianoortizsilva.crud.cliente.annotation.WithMockUserCliente;
import com.lucianoortizsilva.crud.cliente.annotation.WithMockUserSemPermissao;
import com.lucianoortizsilva.crud.cliente.annotation.WithMockUserSuporte;
import com.lucianoortizsilva.crud.cliente.entity.Cliente;
import com.lucianoortizsilva.crud.cliente.repository.ClienteRepository;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("/clientes")
@TestInstance(Lifecycle.PER_CLASS)
class ClienteControllerTest {

	@Autowired
	private WebApplicationContext context;

	@MockBean
	private ClienteRepository clienteRepository;

	private MockMvc mvc;

	@BeforeAll
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	@Nested
	@DisplayName("#Testando > GET /clientes/{id}")
	class GetClientes {

		@Test
		@WithMockUserSemPermissao
		@DisplayName("DADO QUE estou autenticado sem permissões de acesso QUANDO acesso GET /clientes/{id}, ENTÃO deverá lançar erro 403")
		void clientesById_deveGerarForbidden() throws Exception {
			mvc.perform(get("/clientes/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
		}

		@Test
		@WithMockUserAdmin
		@DisplayName("DADO QUE estou autenticado com permissão de acesso, E cliente pesquisado não existe, QUANDO acesso GET /clientes/{id}, ENTÃO deverá lançar erro 404")
		void clientesById_deveGerarNotFound() throws Exception {
			mvc.perform(get("/clientes/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
		}

		@Test
		@WithMockUserAdmin
		@DisplayName("DADO QUE estou autenticado com permissão de acesso, E cliente pesquisado existe, QUANDO acesso GET /clientes/{id}, ENTÃO deverá retornar 200")
		void clientesById_deveRetornarComSucesso() throws Exception {
			Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(new Cliente()));
			mvc.perform(get("/clientes/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		}

	}

	
	@Nested
	@DisplayName("#Testando > DELETE /clientes/{id}")
	class DeleteClientes {

		@Test
		@WithMockUserSemPermissao
		@DisplayName("DADO QUE estou autenticado sem permissões de acesso, QUANDO acesso DELETE /clientes/{id}, ENTÃO deverá lançar erro 403")
		void clientesById_deveGerarForbidden() throws Exception {
			mvc.perform(delete("/clientes/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
		}

		@Test
		@WithMockUserCliente
		@DisplayName("DADO QUE estou autenticado, E estou com usuário com permissão de cliente, QUANDO acesso DELETE /clientes/{id}, ENTÃO deverá lançar erro 403")
		void clientesById_comUsuarioCliente_deveGerarForbidden() throws Exception {
			mvc.perform(delete("/clientes/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
		}
		
		@Test
		@WithMockUserSuporte
		@DisplayName("DADO QUE estou autenticado, E estou com usuário com permissão de suporte, QUANDO acesso DELETE /clientes/{id}, ENTÃO deverá lançar erro 403")
		void clientesById_comUsuarioSuporte_deveGerarForbidden() throws Exception {
			mvc.perform(delete("/clientes/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
		}
		
		@Test
		@WithMockUserAdmin
		@DisplayName("DADO QUE estou autenticado com permissão de acesso, E cliente pesquisado não existe, QUANDO acesso DELETE /clientes/{id}, ENTÃO deverá lançar erro 404")
		void clientesById_deveGerarNotFound() throws Exception {
			mvc.perform(delete("/clientes/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
		}

		@Test
		@WithMockUserAdmin
		@DisplayName("DADO QUE estou autenticado com permissão de acesso, E cliente pesquisado existe, QUANDO acesso DELETE /clientes/{id}, ENTÃO deverá retornar 200")
		void clientesById_deveRetornarComSucesso() throws Exception {
			Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(new Cliente()));
			mvc.perform(delete("/clientes/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
		}

	}

}