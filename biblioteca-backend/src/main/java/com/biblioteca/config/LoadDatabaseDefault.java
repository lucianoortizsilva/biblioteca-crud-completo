package com.biblioteca.config;

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

import com.biblioteca.livro.entity.Livro;
import com.biblioteca.livro.repository.LivroRepository;
import com.biblioteca.security.authentication.user.Permission;
import com.biblioteca.security.authentication.user.PermissionEnum;
import com.biblioteca.security.authentication.user.PermissionRepository;
import com.biblioteca.security.authentication.user.Role;
import com.biblioteca.security.authentication.user.RoleEnum;
import com.biblioteca.security.authentication.user.RoleRepository;
import com.biblioteca.security.authentication.user.User;
import com.biblioteca.security.authentication.user.UserRepository;

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
	private LivroRepository livroRepository;

	@Override
	@Transactional
	public void onApplicationEvent(final ContextRefreshedEvent event) {

		final Permission create = createPermissionIfNotFound(PermissionEnum.CREATE.name());
		final Permission read = createPermissionIfNotFound(PermissionEnum.READ.name());
		final Permission update = createPermissionIfNotFound(PermissionEnum.UPDATE.name());
		final Permission delete = createPermissionIfNotFound(PermissionEnum.DELETE.name());

		final Role roleAdmin = createRoleIfNotFound(RoleEnum.ROLE_ADMIN, Arrays.asList(create, read, update, delete));
		final Role roleCliente = createRoleIfNotFound(RoleEnum.ROLE_FUNCIONARIO, Arrays.asList(read, update));
		final Role roleSuporte = createRoleIfNotFound(RoleEnum.ROLE_SUPORTE, Arrays.asList(read));

		createUserIfNotFound("luciano@fakeMail.com", roleAdmin);
		createUserIfNotFound("mariana@fakeMail.com", roleCliente);
		createUserIfNotFound("vanessa@fakeMail.com", roleSuporte);

		createLivroIfNotFound("978-1-119-61762-4", "Oracle Certified Professional Java SE 11 Programmer II", "Scott Selikoff", LocalDate.of(2020, 01, 01));
		createLivroIfNotFound("978-85-7608-325-2", "EJB 3 em acao", "Debu Panda", LocalDate.of(2009, 01, 01));
		createLivroIfNotFound("978-85-7608-224-8", "Codigo Limpo", "Robert C. Martin", LocalDate.of(2005, 01, 01));
		createLivroIfNotFound("978-85-7522-621-6", "Microsservices Pronto Para Produção", "Susan J. Fowler", LocalDate.of(2022, 03, 14));
		createLivroIfNotFound("978-85-254-3218-6", "Sapiens", "Yuval Noah Harari", LocalDate.of(1999, 04, 9));
		createLivroIfNotFound("978-85-7608-303-0", "Certificação Sun Java 6", "Kathy Sierra", LocalDate.of(2010, 11, 12));
		createLivroIfNotFound("978-85-7608-294-1", "Use a Cabeça Servlets & JSP", "Bryan Basham", LocalDate.of(2000, 07, 14));
		
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
	private void createLivroIfNotFound(final String isbn, final String descricao, final String autor, final LocalDate dtLancamento) {
		Optional<Livro> livro = livroRepository.findByIsbn(isbn);
		if (livro.isEmpty()) {
			livroRepository.save(new Livro(isbn, descricao, autor, dtLancamento));
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