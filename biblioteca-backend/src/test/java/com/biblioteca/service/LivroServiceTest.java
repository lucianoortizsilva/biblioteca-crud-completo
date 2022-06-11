package com.biblioteca.service;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.biblioteca.livro.dto.LivroDTO;
import com.biblioteca.livro.entity.Livro;
import com.biblioteca.livro.exception.LivroComIsbnDuplicadoException;
import com.biblioteca.livro.exception.LivroNaoEncontradoException;
import com.biblioteca.livro.repository.LivroRepository;
import com.biblioteca.livro.service.LivroService;

/**
 * 
 * @see https://docs.spring.io/spring-security/site/docs/5.5.2/reference/html5/
 * @see https://junit.org/junit5/docs/current/user-guide/
 * 
 */
//@formatter:off
@Disabled
@RunWith(JUnitPlatform.class)
@ExtendWith(MockitoExtension.class)
class LivroServiceTest {
	
	static final Long ID_RANDOMICO =  nextLong();
	static final Long ID_RANDOMICO_OUTRO =  nextLong();
	static final String ISBN_ALEATORIO =  randomNumeric(11);
	static final LivroDTO DTO_LIVRO_ALEATORIO = LivroDTO.builder().id(ID_RANDOMICO).isbn(ISBN_ALEATORIO).build();
	static final Livro ENTITY_LIVRO_ALEATORIO = new Livro(ID_RANDOMICO, ISBN_ALEATORIO);
	static final Livro ENTITY_LIVRO_ALEATORIO_MESMO_ISBN = new Livro(ID_RANDOMICO_OUTRO, ENTITY_LIVRO_ALEATORIO.getIsbn());
			

	
	@Nested
	@DisplayName("Testando Cenario Perfeito")
	 class CenarioPerfeitoTest {

		@InjectMocks
		LivroService livroService;
		
		@Mock
		LivroRepository livroRepository;
		
		@Mock
		ModelMapper modelMapper;
		
		@Test
		@DisplayName("QUANDO tento inserir um livro que nao existe ENTAO deveria ser cadastrado com sucesso")
		void inserir_deveInserirLivroComSucesso() {
			when(livroRepository.findByIsbn(ENTITY_LIVRO_ALEATORIO.getIsbn())).thenReturn(Optional.empty());
			this.livroService.insert(ENTITY_LIVRO_ALEATORIO);
			Mockito.verify(this.livroRepository, Mockito.atLeast(1)).save(any());
		}
		
		@Test
		@DisplayName("QUANDO tento deletar um livro ENTAO deveria ser deletado com sucesso")
		void deletar_deveDeletarLivroComSucesso() {
			when(livroRepository.findById(ENTITY_LIVRO_ALEATORIO.getId())).thenReturn(Optional.of(ENTITY_LIVRO_ALEATORIO));
			this.livroService.delete(ENTITY_LIVRO_ALEATORIO.getId());
			Mockito.verify(this.livroRepository, Mockito.atLeast(1)).deleteById(anyLong());
		}
		
		@Test
		@DisplayName("QUANDO pesquiso por um livro que existe ENTAO deveria retornar com sucesso")
		void findById_deveRetornarLivroComSucesso() {
			when(livroRepository.findById(ENTITY_LIVRO_ALEATORIO.getId())).thenReturn(Optional.of(ENTITY_LIVRO_ALEATORIO));
			final Livro livro = this.livroService.findById(ENTITY_LIVRO_ALEATORIO.getId());
			assertThat(livro.getId()).isEqualTo(ENTITY_LIVRO_ALEATORIO.getId());
			assertThat(livro.getIsbn()).isEqualTo(ENTITY_LIVRO_ALEATORIO.getIsbn());
			assertThat(livro.getDescricao()).isEqualTo(ENTITY_LIVRO_ALEATORIO.getDescricao());
			assertThat(livro.getDtLancamento()).isEqualTo(ENTITY_LIVRO_ALEATORIO.getDtLancamento());
		}
		
		@Test
		@DisplayName("QUANDO tento atualizar dados de um ENTAO deveria ser atualizado com sucesso")
		void update_deveAtualizarLivroComSucesso() {
			when(livroRepository.findById(ENTITY_LIVRO_ALEATORIO.getId())).thenReturn(Optional.of(ENTITY_LIVRO_ALEATORIO));
			when(livroRepository.findByIsbn(ENTITY_LIVRO_ALEATORIO.getIsbn())).thenReturn(Optional.of(ENTITY_LIVRO_ALEATORIO));
			this.livroService.update(DTO_LIVRO_ALEATORIO);
			Mockito.verify(this.livroRepository, Mockito.atLeast(1)).save(any());
		}
	}
	
	
	
	@Nested
	@DisplayName("Testando Exceptions")
	class ExceptionTest {
		
		@InjectMocks
		LivroService livroService;
		
		@Mock
		LivroRepository livroRepository;
		
		@Test
		@DisplayName("QUANDO pesquiso por um livro que nao existe ENTAO deveria lancar LivroNaoEncontradoException")
		void findById_deveRetornarLivroNaoEncontradoException() {
			when(livroRepository.findById(ID_RANDOMICO)).thenReturn(Optional.empty());
			LivroNaoEncontradoException exception = assertThrows(LivroNaoEncontradoException.class, () -> this.livroService.findById(ID_RANDOMICO));
			assertEquals("Livro nao encontrado", exception.getMessage());
		}
		
		@Test
		@DisplayName("QUANDO tento deletar um livro que nao existe ENTAO deveria lancar LivroNaoEncontradoException")
		void delete_deveRetornarLivroNaoEncontradoException() {
			when(livroRepository.findById(ID_RANDOMICO)).thenReturn(Optional.empty());
			LivroNaoEncontradoException exception = assertThrows(LivroNaoEncontradoException.class, () -> this.livroService.delete(ID_RANDOMICO));
			assertEquals("Livro nao encontrado", exception.getMessage());
		}
		
		@Test
		@DisplayName("QUANDO tento atualizar um livro que nao existe ENTAO deveria lancar LivroNaoEncontradoException")
		void update_deveRetornarLivroNaoEncontradoException() {
			when(livroRepository.findById(DTO_LIVRO_ALEATORIO.getId())).thenReturn(Optional.empty());
			LivroNaoEncontradoException exception = assertThrows(LivroNaoEncontradoException.class, () -> this.livroService.update(DTO_LIVRO_ALEATORIO));
			assertEquals("Livro nao encontrado", exception.getMessage());
		}
		
		@Test
		@DisplayName("QUANDO tento atualizar um livro com ISBN de outro livro cadastrado ENTAO deveria lancar LivroComCpfDuplicadoException")
		void update_deveRetornarIsbnDuplicadoException() {
			when(livroRepository.findById(ENTITY_LIVRO_ALEATORIO.getId())).thenReturn(Optional.of(ENTITY_LIVRO_ALEATORIO));
			when(livroRepository.findByIsbn(ENTITY_LIVRO_ALEATORIO.getIsbn())).thenReturn(Optional.of(ENTITY_LIVRO_ALEATORIO_MESMO_ISBN));
			LivroComIsbnDuplicadoException exception = assertThrows(LivroComIsbnDuplicadoException.class, () -> this.livroService.update(DTO_LIVRO_ALEATORIO));
			assertEquals("O ISBN informado ja foi cadastrado", exception.getMessage());
		}
		
		@Test
		@DisplayName("QUANDO tento inserir um livro com ISBN que ja cadastrado ENTAO deveria lancar LivroComIsbnDuplicadoException")
		void inserir_deveRetornarClienteNaoEncontradoException() {
			when(livroRepository.findByIsbn(ENTITY_LIVRO_ALEATORIO.getIsbn())).thenReturn(Optional.of(ENTITY_LIVRO_ALEATORIO));
			LivroComIsbnDuplicadoException exception = assertThrows(LivroComIsbnDuplicadoException.class, () -> this.livroService.insert(ENTITY_LIVRO_ALEATORIO));
			assertEquals("O ISBN informado ja foi cadastrado", exception.getMessage());
		}
	}
	
}