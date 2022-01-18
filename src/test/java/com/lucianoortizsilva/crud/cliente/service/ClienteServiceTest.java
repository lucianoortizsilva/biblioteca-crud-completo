package com.lucianoortizsilva.crud.cliente.service;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import com.lucianoortizsilva.crud.cliente.dto.ClienteDTO;
import com.lucianoortizsilva.crud.cliente.entity.Cliente;
import com.lucianoortizsilva.crud.cliente.exception.ClienteNaoEncontradoException;
import com.lucianoortizsilva.crud.cliente.exception.CpfDuplicadoException;
import com.lucianoortizsilva.crud.cliente.repository.ClienteRepository;

//@formatter:off
@SpringBootTest
@AutoConfigureMockMvc
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
	@DisplayName("Testando Exceptions")
	class ExceptionTest {
		
		@InjectMocks
		ClienteService clienteService;
		
		@Mock
		ClienteRepository clienteRepository;
		
		@Test
		@DisplayName("QUANDO pesquiso por um cliente que não existe ENTÃO deverá lançar ClienteNaoEncontradoException")
		@WithMockUser(roles = {"ADMIN"})
		void findById_deveRetornarClienteNaoEncontradoException() {
			when(clienteRepository.findById(ID_RANDOMICO)).thenReturn(Optional.empty());
			ClienteNaoEncontradoException exception = assertThrows(ClienteNaoEncontradoException.class, () -> this.clienteService.findById(ID_RANDOMICO));
			assertEquals("Cliente não encontrado", exception.getMessage());
		}
		
		@Test
		@DisplayName("QUANDO tento deletar um cliente que não existe ENTÃO deverá lançar ClienteNaoEncontradoException")
		@WithMockUser(roles = {"ADMIN"})
		void delete_deveRetornarClienteNaoEncontradoException() {
			when(clienteRepository.findById(ID_RANDOMICO)).thenReturn(Optional.empty());
			ClienteNaoEncontradoException exception = assertThrows(ClienteNaoEncontradoException.class, () -> this.clienteService.delete(ID_RANDOMICO));
			assertEquals("Cliente não encontrado", exception.getMessage());
		}
		
		@Test
		@DisplayName("QUANDO tento atualizar um cliente que não existe ENTÃO deverá lançar ClienteNaoEncontradoException")
		@WithMockUser(roles = {"ADMIN"})
		void update_deveRetornarClienteNaoEncontradoException() {
			when(clienteRepository.findById(ID_RANDOMICO)).thenReturn(Optional.empty());
			ClienteNaoEncontradoException exception = assertThrows(ClienteNaoEncontradoException.class, () -> this.clienteService.update(DTO_CLIENTE_ALEATORIO));
			assertEquals("Cliente não encontrado", exception.getMessage());
		}
		
		@Test
		@DisplayName("QUANDO tento atualizar um cliente com CPF de outro cliente cadastrado ENTÃO deverá lançar CpfDuplicadoException")
		@WithMockUser(roles = {"ADMIN"})
		void update_deveRetornarCpfDuplicadoException() {
			when(clienteRepository.findById(ID_RANDOMICO)).thenReturn(Optional.of(ENTITY_CLIENTE_ALEATORIO));
			when(clienteRepository.findByCpf(CPF_ALEATORIO)).thenReturn(Optional.of(ENTITY_CLIENTE_ALEATORIO_MESMO_CPF));
			CpfDuplicadoException exception = assertThrows(CpfDuplicadoException.class, () -> this.clienteService.update(DTO_CLIENTE_ALEATORIO));
			assertEquals("O CPF informado já foi cadastrado", exception.getMessage());
		}
		
		@Test
		@DisplayName("QUANDO tento inserir um cliente com CPF que já cadastrado ENTÃO deverá lançar CpfDuplicadoException")
		@WithMockUser(roles = {"ADMIN"})
		void inserir_deveRetornarClienteNaoEncontradoException() {
			when(clienteRepository.findByCpf(CPF_ALEATORIO)).thenReturn(Optional.of(ENTITY_CLIENTE_ALEATORIO));
			CpfDuplicadoException exception = assertThrows(CpfDuplicadoException.class, () -> this.clienteService.insert(ENTITY_CLIENTE_ALEATORIO));
			assertEquals("O CPF informado já foi cadastrado", exception.getMessage());
		}
	}
	
}