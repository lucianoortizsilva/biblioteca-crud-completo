package com.lucianoortizsilva.crud.security.config;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.lucianoortizsilva.crud.security.authentication.user.Permission;
import com.lucianoortizsilva.crud.security.authentication.user.PermissionEnum;
import com.lucianoortizsilva.crud.security.authentication.user.PermissionRepository;
import com.lucianoortizsilva.crud.security.authentication.user.Role;
import com.lucianoortizsilva.crud.security.authentication.user.RoleEnum;
import com.lucianoortizsilva.crud.security.authentication.user.RoleRepository;
import com.lucianoortizsilva.crud.security.authentication.user.User;
import com.lucianoortizsilva.crud.security.authentication.user.UserRepository;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private boolean alreadySetup = false;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (alreadySetup)
			return;

		final Permission create = createPermissionIfNotFound(PermissionEnum.CREATE.name());
		final Permission read = createPermissionIfNotFound(PermissionEnum.READ.name());
		final Permission update = createPermissionIfNotFound(PermissionEnum.UPDATE.name());
		final Permission delete = createPermissionIfNotFound(PermissionEnum.DELETE.name());

		final Role roleAdmin = createRoleIfNotFound(RoleEnum.ROLE_ADMIN, Arrays.asList(create, read, update, delete));
		final Role roleCliente = createRoleIfNotFound(RoleEnum.ROLE_CLIENTE, Arrays.asList(read, update));
		final Role roleSuporte = createRoleIfNotFound(RoleEnum.ROLE_SUPORTE, Arrays.asList(read));

		final String senha = "12345";
		final User luciano = new User("luciano@email.com", bCryptPasswordEncoder.encode(senha), Boolean.TRUE, Arrays.asList(roleAdmin));
		final User mariana = new User("mariana@email.com", bCryptPasswordEncoder.encode(senha), Boolean.TRUE, Arrays.asList(roleCliente));
		final User vanessa = new User("vanessa@email.com", bCryptPasswordEncoder.encode(senha), Boolean.TRUE, Arrays.asList(roleSuporte));

		final List<User> usuarios = List.of(luciano, mariana, vanessa);
		
		userRepository.saveAll(usuarios);
		
		alreadySetup = true;
	}

	@Transactional
	Permission createPermissionIfNotFound(final String name) {
		Optional<Permission> permission = permissionRepository.findByName(name);
		if (permission.isEmpty()) {
			return permissionRepository.save(new Permission(name));
		} else {
			return null;
		}
	}

	@Transactional
	Role createRoleIfNotFound(final RoleEnum roleEnum, final List<Permission> permissions) {
		Optional<Role> role = roleRepository.findByName(roleEnum.name());
		if (role.isEmpty()) {
			return roleRepository.save(new Role(roleEnum.name(), permissions));
		} else {
			return null;
		}
	}

}