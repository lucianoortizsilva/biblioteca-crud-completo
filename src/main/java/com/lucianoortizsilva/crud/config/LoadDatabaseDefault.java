package com.lucianoortizsilva.crud.config;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.lucianoortizsilva.crud.cliente.entity.Cliente;
import com.lucianoortizsilva.crud.cliente.repository.ClienteRepository;
import com.lucianoortizsilva.crud.security.authentication.user.Permission;
import com.lucianoortizsilva.crud.security.authentication.user.PermissionEnum;
import com.lucianoortizsilva.crud.security.authentication.user.PermissionRepository;
import com.lucianoortizsilva.crud.security.authentication.user.Role;
import com.lucianoortizsilva.crud.security.authentication.user.RoleEnum;
import com.lucianoortizsilva.crud.security.authentication.user.RoleRepository;
import com.lucianoortizsilva.crud.security.authentication.user.User;
import com.lucianoortizsilva.crud.security.authentication.user.UserRepository;

@Component
public class LoadDatabaseDefault implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	@Transactional
	public void onApplicationEvent(final ContextRefreshedEvent event) {

		final Permission create = createPermissionIfNotFound(PermissionEnum.CREATE.name());
		final Permission read = createPermissionIfNotFound(PermissionEnum.READ.name());
		final Permission update = createPermissionIfNotFound(PermissionEnum.UPDATE.name());
		final Permission delete = createPermissionIfNotFound(PermissionEnum.DELETE.name());

		final Role roleAdmin = createRoleIfNotFound(RoleEnum.ROLE_ADMIN, Arrays.asList(create, read, update, delete));
		final Role roleCliente = createRoleIfNotFound(RoleEnum.ROLE_CLIENTE, Arrays.asList(read, update));
		final Role roleSuporte = createRoleIfNotFound(RoleEnum.ROLE_SUPORTE, Arrays.asList(read));

		createUserIfNotFound("luciano@fakeMail.com", roleAdmin);
		createUserIfNotFound("mariana@fakeMail.com", roleCliente);
		createUserIfNotFound("vanessa@fakeMail.com", roleSuporte);

		createClienteIfNotFound("Batman", "52678324052", LocalDate.of(1990, 05, 17));
		createClienteIfNotFound("Super Homem", "54671702010", LocalDate.of(1988, 01, 21));
		createClienteIfNotFound("Homem Aranha", "54096739057", LocalDate.of(1984, 01, 31));
		createClienteIfNotFound("Mulher Maravilha", "70585164053", LocalDate.of(1981, 10, 07));
	}

	@Transactional
	private Permission createPermissionIfNotFound(final String name) {
		Optional<Permission> permission = permissionRepository.findByName(name);
		if (permission.isEmpty()) {
			return permissionRepository.save(new Permission(name));
		} else {
			return null;
		}
	}

	@Transactional
	private Role createRoleIfNotFound(final RoleEnum roleEnum, final List<Permission> permissions) {
		Optional<Role> role = roleRepository.findByName(roleEnum.name());
		if (role.isEmpty()) {
			return roleRepository.save(new Role(roleEnum.name(), permissions));
		} else {
			return null;
		}
	}

	@Transactional
	private void createClienteIfNotFound(final String nome, final String cpf, final LocalDate dtNascimento) {
		Optional<Cliente> cliente = clienteRepository.findByCpf(cpf);
		if (cliente.isEmpty()) {
			clienteRepository.save(new Cliente(nome, cpf, dtNascimento));
		}
	}

	@Transactional
	private void createUserIfNotFound(final String username, final Role role) {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isEmpty()) {
			final String password = bCryptPasswordEncoder.encode("12345");
			userRepository.save(new User(username, password, true, Arrays.asList(role)));
		}
	}

}