package com.lucianoortizsilva.crud.cliente.controller;

import static com.lucianoortizsilva.crud.cliente.util.TestUtil.cpfMaxExcedido;
import static com.lucianoortizsilva.crud.cliente.util.TestUtil.nomeMaxExcedido;
import static com.lucianoortizsilva.crud.cliente.util.TestUtil.qualquerCPF;
import static com.lucianoortizsilva.crud.cliente.util.TestUtil.qualquerDate;
import static com.lucianoortizsilva.crud.cliente.util.TestUtil.qualquerString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import javax.servlet.ServletContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucianoortizsilva.crud.cliente.annotation.WithMockUserAdmin;
import com.lucianoortizsilva.crud.cliente.annotation.WithMockUserCliente;
import com.lucianoortizsilva.crud.cliente.annotation.WithMockUserSemPermissao;
import com.lucianoortizsilva.crud.cliente.annotation.WithMockUserSuporte;
import com.lucianoortizsilva.crud.cliente.dto.ClienteDTO;
import com.lucianoortizsilva.crud.cliente.entity.Cliente;
import com.lucianoortizsilva.crud.cliente.repository.ClienteRepository;

/**
 * 
 * @see https://docs.spring.io/spring-security/site/docs/5.5.2/reference/html5/#test-mockmvc
 * @see https://www.baeldung.com/integration-testing-in-spring
 * @see https://dzone.com/articles/rest-endpoint-testing-with-mockmvc
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("/clientes")
@TestInstance(Lifecycle.PER_CLASS)
class ClienteControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private ClienteRepository clienteRepository;

	private MockMvc mvc;

	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
	}

	@Test
	@DisplayName("#Testando load clienteController")
	void testServletContextClienteController() {
		ServletContext servletContext = webApplicationContext.getServletContext();
		Assertions.assertNotNull(servletContext);
		Assertions.assertTrue(servletContext instanceof MockServletContext);
		Assertions.assertNotNull(webApplicationContext.getBean("clienteController"));
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
		void deleteById_deveGerarForbidden() throws Exception {
			mvc.perform(delete("/clientes/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
		}

		@Test
		@WithMockUserCliente
		@DisplayName("DADO QUE estou autenticado, E estou com usuário com permissão de cliente, QUANDO acesso DELETE /clientes/{id}, ENTÃO deverá lançar erro 403")
		void deleteById_comUsuarioCliente_deveGerarForbidden() throws Exception {
			mvc.perform(delete("/clientes/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
		}

		@Test
		@WithMockUserSuporte
		@DisplayName("DADO QUE estou autenticado, E estou com usuário com permissão de suporte, QUANDO acesso DELETE /clientes/{id}, ENTÃO deverá lançar erro 403")
		void deleteById_comUsuarioSuporte_deveGerarForbidden() throws Exception {
			mvc.perform(delete("/clientes/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
		}

		@Test
		@WithMockUserAdmin
		@DisplayName("DADO QUE estou autenticado com permissão de acesso, E cliente pesquisado não existe, QUANDO acesso DELETE /clientes/{id}, ENTÃO deverá lançar erro 404")
		void deleteById_deveGerarNotFound() throws Exception {
			mvc.perform(delete("/clientes/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
		}

		@Test
		@WithMockUserAdmin
		@DisplayName("DADO QUE estou autenticado com permissão de acesso, E cliente pesquisado existe, QUANDO acesso DELETE /clientes/{id}, ENTÃO deverá retornar 200")
		void deleteById_deveRetornarComSucesso() throws Exception {
			Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(new Cliente()));
			mvc.perform(delete("/clientes/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
		}

	}

	@Nested
	@DisplayName("#Testando > POST /clientes")
	class PostClientes {

		@Autowired
		private ObjectMapper objectMapper;

		@Autowired
		private ModelMapper modelMapper;

		@Test
		@DisplayName("DADO QUE não estou autenticado, QUANDO acesso POST /clientes, E informo todos campos corretamente, ENTÃO deverá retornar 201")
		void insert_deveRetornarCriadoComSucesso() throws Exception {
			final ClienteDTO dto = ClienteDTO.builder().id(1L).cpf(qualquerCPF()).nome(qualquerString()).dtNascimento(qualquerDate()).build();
			when(clienteRepository.save(any())).thenReturn(modelMapper.map(dto, Cliente.class));
			final String content = objectMapper.writeValueAsString(dto);
			mvc.perform(post("/clientes").content(content).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andExpect(header().string("Location", "http://localhost/clientes/1"));
		}
		
		@Test
		@DisplayName("DADO QUE informo cpf null, QUANDO acesso POST /clientes, ENTÃO deverá retornar 422")
		void insert_comCpfNull_deveRetornarUnprocessableEntity() throws Exception {
			final ClienteDTO dto = ClienteDTO.builder().id(1L).cpf(null).nome(qualquerString()).dtNascimento(qualquerDate()).build();
			when(clienteRepository.save(any())).thenReturn(modelMapper.map(dto, Cliente.class));
			final String content = objectMapper.writeValueAsString(dto);
			mvc.perform(post("/clientes").content(content).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
		}
		
		@Test
		@DisplayName("DADO QUE informo cpf com MAX caracter excedido, QUANDO acesso POST /clientes, ENTÃO deverá retornar 422")
		void insert_comCpfMaxCaractersExcedido_deveRetornarUnprocessableEntity() throws Exception {
			final ClienteDTO dto = ClienteDTO.builder().id(1L).cpf(cpfMaxExcedido()).nome(qualquerString()).dtNascimento(qualquerDate()).build();
			when(clienteRepository.save(any())).thenReturn(modelMapper.map(dto, Cliente.class));
			final String content = objectMapper.writeValueAsString(dto);
			mvc.perform(post("/clientes").content(content).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
		}
		
		@Test
		@DisplayName("DADO QUE informo nome null, QUANDO acesso POST /clientes, ENTÃO deverá retornar 422")
		void insert_comNomeNull_deveRetornarUnprocessableEntity() throws Exception {
			final ClienteDTO dto = ClienteDTO.builder().id(1L).cpf(qualquerCPF()).nome(null).dtNascimento(qualquerDate()).build();
			when(clienteRepository.save(any())).thenReturn(modelMapper.map(dto, Cliente.class));
			final String content = objectMapper.writeValueAsString(dto);
			mvc.perform(post("/clientes").content(content).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
		}
		
		@Test
		@DisplayName("DADO QUE informo nome com MAX caracter excedido, QUANDO acesso POST /clientes, ENTÃO deverá retornar 422")
		void insert_comNomeMaxCaractersExcedido_deveRetornarUnprocessableEntity() throws Exception {
			final ClienteDTO dto = ClienteDTO.builder().id(1L).cpf(qualquerCPF()).nome(nomeMaxExcedido()).dtNascimento(qualquerDate()).build();
			when(clienteRepository.save(any())).thenReturn(modelMapper.map(dto, Cliente.class));
			final String content = objectMapper.writeValueAsString(dto);
			mvc.perform(post("/clientes").content(content).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
		}
		
		@Test
		@DisplayName("DADO QUE informo dtNascimento null, QUANDO acesso POST /clientes, ENTÃO deverá retornar 422")
		void insert_comNomeDtNascimentoNull_deveRetornarUnprocessableEntity() throws Exception {
			final ClienteDTO dto = ClienteDTO.builder().id(1L).cpf(qualquerCPF()).nome(qualquerString()).dtNascimento(null).build();
			when(clienteRepository.save(any())).thenReturn(modelMapper.map(dto, Cliente.class));
			final String content = objectMapper.writeValueAsString(dto);
			mvc.perform(post("/clientes").content(content).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
		}
	}

}