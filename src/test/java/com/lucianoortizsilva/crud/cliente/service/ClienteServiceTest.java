package com.lucianoortizsilva.crud.cliente.service;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

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

import com.lucianoortizsilva.crud.cliente.dto.ClienteDTO;
import com.lucianoortizsilva.crud.cliente.entity.Cliente;
import com.lucianoortizsilva.crud.cliente.exception.ClienteComCpfDuplicadoException;
import com.lucianoortizsilva.crud.cliente.exception.ClienteNaoEncontradoException;
import com.lucianoortizsilva.crud.cliente.repository.ClienteRepository;

/**
 * 
 * @see https://docs.spring.io/spring-security/site/docs/5.5.2/reference/html5/
 * @see https://junit.org/junit5/docs/current/user-guide/
 * 
 */
//@formatter:off
@RunWith(JUnitPlatform.class)
@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {
	
	static final Long ID_RANDOMICO =  nextLong();
	static final Long ID_RANDOMICO_OUTRO =  nextLong();
	static final String CPF_ALEATORIO =  randomNumeric(11);
	static final ClienteDTO DTO_CLIENTE_ALEATORIO = new ClienteDTO(ID_RANDOMICO, CPF_ALEATORIO);
	static final Cliente ENTITY_CLIENTE_ALEATORIO = new Cliente(ID_RANDOMICO, CPF_ALEATORIO);
	static final Cliente ENTITY_CLIENTE_ALEATORIO_MESMO_CPF = new Cliente(ID_RANDOMICO_OUTRO, ENTITY_CLIENTE_ALEATORIO.getCpf());
			

	
	@Nested
	@DisplayName("Testando Cenário Perfeito")
	 class CenarioPerfeitoTest {

		@InjectMocks
		ClienteService clienteService;
		
		@Mock
		ClienteRepository clienteRepository;
		
		@Mock
		ModelMapper modelMapper;
		
		@Test
		@DisplayName("QUANDO tento inserir um cliente que não existe ENTÃO deverá ser cadastrado com sucesso")
		void inserir_deveInserirClienteComSucesso() {
			when(clienteRepository.findByCpf(ENTITY_CLIENTE_ALEATORIO.getCpf())).thenReturn(Optional.empty());
			this.clienteService.insert(ENTITY_CLIENTE_ALEATORIO);
			Mockito.verify(this.clienteRepository, Mockito.atLeast(1)).save(any());
		}
		
		@Test
		@DisplayName("QUANDO tento deletar um cliente ENTÃO deverá ser deletado com sucesso")
		void deletar_deveDeletarClienteComSucesso() {
			when(clienteRepository.findById(ENTITY_CLIENTE_ALEATORIO.getId())).thenReturn(Optional.of(ENTITY_CLIENTE_ALEATORIO));
			this.clienteService.delete(ENTITY_CLIENTE_ALEATORIO.getId());
			Mockito.verify(this.clienteRepository, Mockito.atLeast(1)).deleteById(anyLong());
		}
		
		@Test
		@DisplayName("QUANDO pesquiso por um cliente que existe ENTÃO deverá retornar com sucesso")
		void findById_deveRetornarClienteComSucesso() {
			when(clienteRepository.findById(ENTITY_CLIENTE_ALEATORIO.getId())).thenReturn(Optional.of(ENTITY_CLIENTE_ALEATORIO));
			final Cliente cliente = this.clienteService.findById(ENTITY_CLIENTE_ALEATORIO.getId());
			assertThat(cliente.getId()).isEqualTo(ENTITY_CLIENTE_ALEATORIO.getId());
			assertThat(cliente.getCpf()).isEqualTo(ENTITY_CLIENTE_ALEATORIO.getCpf());
			assertThat(cliente.getNome()).isEqualTo(ENTITY_CLIENTE_ALEATORIO.getNome());
			assertThat(cliente.getDtNascimento()).isEqualTo(ENTITY_CLIENTE_ALEATORIO.getDtNascimento());
		}
		
		@Test
		@DisplayName("QUANDO tento atualizar dados de um ENTÃO deverá ser atualizado com sucesso")
		void update_deveAtualizarClienteComSucesso() {
			when(clienteRepository.findById(ENTITY_CLIENTE_ALEATORIO.getId())).thenReturn(Optional.of(ENTITY_CLIENTE_ALEATORIO));
			when(clienteRepository.findByCpf(ENTITY_CLIENTE_ALEATORIO.getCpf())).thenReturn(Optional.of(ENTITY_CLIENTE_ALEATORIO));
			this.clienteService.update(DTO_CLIENTE_ALEATORIO);
			Mockito.verify(this.clienteRepository, Mockito.atLeast(1)).save(any());
		}
	}
	
	
	
	@Nested
	@DisplayName("Testando Exceptions")
	class ExceptionTest {
		
		@InjectMocks
		ClienteService clienteService;
		
		@Mock
		ClienteRepository clienteRepository;
		
		@Test
		@DisplayName("QUANDO pesquiso por um cliente que não existe ENTÃO deverá lançar ClienteNaoEncontradoException")
		void findById_deveRetornarClienteNaoEncontradoException() {
			when(clienteRepository.findById(ID_RANDOMICO)).thenReturn(Optional.empty());
			ClienteNaoEncontradoException exception = assertThrows(ClienteNaoEncontradoException.class, () -> this.clienteService.findById(ID_RANDOMICO));
			assertEquals("Cliente não encontrado", exception.getMessage());
		}
		
		@Test
		@DisplayName("QUANDO tento deletar um cliente que não existe ENTÃO deverá lançar ClienteNaoEncontradoException")
		void delete_deveRetornarClienteNaoEncontradoException() {
			when(clienteRepository.findById(ID_RANDOMICO)).thenReturn(Optional.empty());
			ClienteNaoEncontradoException exception = assertThrows(ClienteNaoEncontradoException.class, () -> this.clienteService.delete(ID_RANDOMICO));
			assertEquals("Cliente não encontrado", exception.getMessage());
		}
		
		@Test
		@DisplayName("QUANDO tento atualizar um cliente que não existe ENTÃO deverá lançar ClienteNaoEncontradoException")
		void update_deveRetornarClienteNaoEncontradoException() {
			when(clienteRepository.findById(DTO_CLIENTE_ALEATORIO.getId())).thenReturn(Optional.empty());
			ClienteNaoEncontradoException exception = assertThrows(ClienteNaoEncontradoException.class, () -> this.clienteService.update(DTO_CLIENTE_ALEATORIO));
			assertEquals("Cliente não encontrado", exception.getMessage());
		}
		
		@Test
		@DisplayName("QUANDO tento atualizar um cliente com CPF de outro cliente cadastrado ENTÃO deverá lançar ClienteComCpfDuplicadoException")
		void update_deveRetornarCpfDuplicadoException() {
			when(clienteRepository.findById(ENTITY_CLIENTE_ALEATORIO.getId())).thenReturn(Optional.of(ENTITY_CLIENTE_ALEATORIO));
			when(clienteRepository.findByCpf(ENTITY_CLIENTE_ALEATORIO.getCpf())).thenReturn(Optional.of(ENTITY_CLIENTE_ALEATORIO_MESMO_CPF));
			ClienteComCpfDuplicadoException exception = assertThrows(ClienteComCpfDuplicadoException.class, () -> this.clienteService.update(DTO_CLIENTE_ALEATORIO));
			assertEquals("O CPF informado já foi cadastrado", exception.getMessage());
		}
		
		@Test
		@DisplayName("QUANDO tento inserir um cliente com CPF que já cadastrado ENTÃO deverá lançar ClienteComCpfDuplicadoException")
		void inserir_deveRetornarClienteNaoEncontradoException() {
			when(clienteRepository.findByCpf(ENTITY_CLIENTE_ALEATORIO.getCpf())).thenReturn(Optional.of(ENTITY_CLIENTE_ALEATORIO));
			ClienteComCpfDuplicadoException exception = assertThrows(ClienteComCpfDuplicadoException.class, () -> this.clienteService.insert(ENTITY_CLIENTE_ALEATORIO));
			assertEquals("O CPF informado já foi cadastrado", exception.getMessage());
		}
	}
	
}