package com.biblioteca.controller;

import static com.biblioteca.util.TestLivroUtil.isbnMaxCaracterExcedido;
import static com.biblioteca.util.TestLivroUtil.descricaoMaxCaracterExcedido;
import static com.biblioteca.util.TestLivroUtil.qualquerISBN;
import static com.biblioteca.util.TestLivroUtil.qualquerDate;
import static com.biblioteca.util.TestLivroUtil.qualquerID;
import static com.biblioteca.util.TestLivroUtil.qualquerDescricao;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import javax.servlet.ServletContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
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

import com.biblioteca.annotation.WithMockUserAdmin;
import com.biblioteca.annotation.WithMockUserCliente;
import com.biblioteca.annotation.WithMockUserSemPermissao;
import com.biblioteca.annotation.WithMockUserSuporte;
import com.biblioteca.livro.dto.LivroDTO;
import com.biblioteca.livro.entity.Livro;
import com.biblioteca.livro.repository.LivroRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @see https://docs.spring.io/spring-security/site/docs/5.5.2/reference/html5/#test-mockmvc
 * @see https://www.baeldung.com/integration-testing-in-spring
 * @see https://dzone.com/articles/rest-endpoint-testing-with-mockmvc
 *
 */
@Disabled
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("/livros")
@TestInstance(Lifecycle.PER_CLASS)
class LivroControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private LivroRepository livroRepository;

	private MockMvc mvc;

	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
	}

	@Test
	@DisplayName("#Testando load livroController")
	void testServletContextLivroController() {
		ServletContext servletContext = webApplicationContext.getServletContext();
		Assertions.assertNotNull(servletContext);
		Assertions.assertTrue(servletContext instanceof MockServletContext);
		Assertions.assertNotNull(webApplicationContext.getBean("livroController"));
	}

	@Nested
	@DisplayName("#Testando > GET /livros/{id}")
	class Getlivros {

		@Test
		@WithMockUserSemPermissao
		@DisplayName("DADO QUE estou autenticado sem permissões de acesso QUANDO acesso GET /livros/{id}, ENTAO deveria lancar erro 403")
		void livrosById_deveGerarForbidden() throws Exception {
			mvc.perform(get("/livros/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
		}

		@Test
		@WithMockUserAdmin
		@DisplayName("DADO QUE estou autenticado com permissão de acesso, E livro pesquisado nao existe, QUANDO acesso GET /livros/{id}, ENTAO deveria lancar erro 404")
		void livrosById_deveGerarNotFound() throws Exception {
			mvc.perform(get("/livros/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
		}

		@Test
		@WithMockUserAdmin
		@DisplayName("DADO QUE estou autenticado com permissão de acesso, E livro pesquisado existe, QUANDO acesso GET /livros/{id}, ENTAO deveria retornar 200")
		void livrosById_deveRetornarComSucesso() throws Exception {
			when(livroRepository.findById(1L)).thenReturn(Optional.of(new Livro()));
			mvc.perform(get("/livros/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		}

	}

	@Nested
	@DisplayName("#Testando > DELETE /livros/{id}")
	class Deletelivros {

		@Test
		@WithMockUserSemPermissao
		@DisplayName("DADO QUE estou autenticado sem permissoes de acesso, QUANDO acesso DELETE /livros/{id}, ENTAO deveria lancar erro 403")
		void deleteById_deveGerarForbidden() throws Exception {
			mvc.perform(delete("/livros/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
		}

		@Test
		@WithMockUserCliente
		@DisplayName("DADO QUE estou autenticado, E estou com usuario com permissao de livro, QUANDO acesso DELETE /livros/{id}, ENTAO deveria lancar erro 403")
		void deleteById_comUsuarioCliente_deveGerarForbidden() throws Exception {
			mvc.perform(delete("/livros/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
		}

		@Test
		@WithMockUserSuporte
		@DisplayName("DADO QUE estou autenticado, E estou com usuario com permissao de suporte, QUANDO acesso DELETE /livros/{id}, ENTAO deveria lancar erro 403")
		void deleteById_comUsuarioSuporte_deveGerarForbidden() throws Exception {
			mvc.perform(delete("/livros/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
		}

		@Test
		@WithMockUserAdmin
		@DisplayName("DADO QUE estou autenticado com permissao de acesso, E livro pesquisado nao existe, QUANDO acesso DELETE /livros/{id}, ENTAO deveria lancar erro 404")
		void deleteById_deveGerarNotFound() throws Exception {
			mvc.perform(delete("/livros/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
		}

		@Test
		@WithMockUserAdmin
		@DisplayName("DADO QUE estou autenticado com permissao de acesso, E livro pesquisado existe, QUANDO acesso DELETE /livros/{id}, ENTAO deveria lancar 204")
		void deleteById_deveRetornarComSucesso() throws Exception {
			when(livroRepository.findById(1L)).thenReturn(Optional.of(new Livro()));
			mvc.perform(delete("/livros/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
		}

	}

	@Nested
	@DisplayName("#Testando > POST /livros")
	class Postlivros {

		@Autowired
		private ObjectMapper objectMapper;

		@Autowired
		private ModelMapper modelMapper;

		@Test
		@DisplayName("DADO QUE não estou autenticado, QUANDO acesso POST /livros, E informo todos campos corretamente, ENTAO deveria retornar 201")
		void insert_deveRetornarCriadoComSucesso() throws Exception {
			final LivroDTO dto = LivroDTO.builder().id(1L).isbn(qualquerISBN()).descricao(qualquerDescricao()).dtLancamento(qualquerDate()).build();
			when(livroRepository.save(any())).thenReturn(modelMapper.map(dto, Livro.class));
			final String content = objectMapper.writeValueAsString(dto);
			mvc.perform(post("/livros").content(content).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andExpect(header().string("Location", "http://localhost/livros/1"));
		}
		
		@Test
		@DisplayName("DADO QUE informo isbn null, QUANDO acesso POST /livros, ENTAO deveria retornar 422")
		void insert_comIsbnNull_deveRetornarUnprocessableEntity() throws Exception {
			final LivroDTO dto = LivroDTO.builder().id(qualquerID()).isbn(null).descricao(qualquerDescricao()).dtLancamento(qualquerDate()).build();
			when(livroRepository.save(any())).thenReturn(modelMapper.map(dto, Livro.class));
			final String content = objectMapper.writeValueAsString(dto);
			mvc.perform(post("/livros").content(content).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
		}
		
		@Test
		@DisplayName("DADO QUE informo isbn com MAX caracter excedido, QUANDO acesso POST /livros, ENTAO deveria retornar 422")
		void insert_comIsbnMaxCaractersExcedido_deveRetornarUnprocessableEntity() throws Exception {
			final LivroDTO dto = LivroDTO.builder().id(qualquerID()).isbn(isbnMaxCaracterExcedido()).descricao(qualquerDescricao()).dtLancamento(qualquerDate()).build();
			when(livroRepository.save(any())).thenReturn(modelMapper.map(dto, Livro.class));
			final String content = objectMapper.writeValueAsString(dto);
			mvc.perform(post("/livros").content(content).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
		}
		
		@Test
		@DisplayName("DADO QUE informo nome null, QUANDO acesso POST /livros, ENTAO deveria retornar 422")
		void insert_comNomeNull_deveRetornarUnprocessableEntity() throws Exception {
			final LivroDTO dto = LivroDTO.builder().id(qualquerID()).isbn(qualquerISBN()).descricao(null).dtLancamento(qualquerDate()).build();
			when(livroRepository.save(any())).thenReturn(modelMapper.map(dto, Livro.class));
			final String content = objectMapper.writeValueAsString(dto);
			mvc.perform(post("/livros").content(content).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
		}
		
		@Test
		@DisplayName("DADO QUE informo nome com MAX caracter excedido, QUANDO acesso POST /livros, ENTAO deveria retornar 422")
		void insert_comNomeMaxCaractersExcedido_deveRetornarUnprocessableEntity() throws Exception {
			final LivroDTO dto = LivroDTO.builder().id(qualquerID()).isbn(qualquerISBN()).descricao(descricaoMaxCaracterExcedido()).dtLancamento(qualquerDate()).build();
			when(livroRepository.save(any())).thenReturn(modelMapper.map(dto, Livro.class));
			final String content = objectMapper.writeValueAsString(dto);
			mvc.perform(post("/livros").content(content).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
		}
		
		@Test
		@DisplayName("DADO QUE informo dtNascimento null, QUANDO acesso POST /livros, ENTAO deveria retornar 422")
		void insert_comNomeDtNascimentoNull_deveRetornarUnprocessableEntity() throws Exception {
			final LivroDTO dto = LivroDTO.builder().id(qualquerID()).isbn(qualquerISBN()).descricao(qualquerDescricao()).dtLancamento(null).build();
			when(livroRepository.save(any())).thenReturn(modelMapper.map(dto, Livro.class));
			final String content = objectMapper.writeValueAsString(dto);
			mvc.perform(post("/livros").content(content).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
		}
	}

	@Nested
	@DisplayName("#Testando > PUT /livros")
	class Putlivros {

		@Autowired
		private ObjectMapper objectMapper;

		@Test
		@WithMockUserAdmin
		@DisplayName("DADO QUE estou autenticado com permissao de acesso, QUANDO acesso PUT /livros, E informo todos campos corretamente, ENTAO deveria lancar retornar 204")
		void update_comUsuarioComPermissao_deveRetornarAtualizadoComSucesso() throws Exception {
			final LivroDTO dto = LivroDTO.builder().id(1L).isbn(qualquerISBN()).descricao(qualquerDescricao()).dtLancamento(qualquerDate()).build();
			final Livro livroCadastrado = new Livro(dto.getIsbn(), dto.getDescricao(), dto.getAutor(), dto.getDtLancamento());
			when(livroRepository.findById(1L)).thenReturn(Optional.of(livroCadastrado));
			final String content = objectMapper.writeValueAsString(dto);
			mvc.perform(put("/livros/1").content(content).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
		}
		
		@Test
		@WithMockUserSuporte
		@DisplayName("DADO QUE estou autenticado, E estou com usuario com permissao de suporte, QUANDO acesso PUT /livros/{id}, ENTAO deveria lancar erro 403")
		void update_comUsuarioSuporte_deveGerarForbidden() throws Exception {
			final LivroDTO dto = LivroDTO.builder().id(1L).isbn(qualquerISBN()).descricao(qualquerDescricao()).dtLancamento(qualquerDate()).build();
			final String content = objectMapper.writeValueAsString(dto);
			mvc.perform(put("/livros/1").content(content).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
		}
	}
	
}