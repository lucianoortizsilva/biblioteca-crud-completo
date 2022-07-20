### Tecnologias
- <img src="https://badges.aleen42.com/src/java.svg" alt="badge"/>
- <img src="https://badges.aleen42.com/src/react.svg" alt="badge"/>
- <img src="https://badges.aleen42.com/src/docker.svg" alt="badge"/>

### O que é ?
É um CRUD completo em um contexto de uma biblioteca.\
No projeto backend:
  - Utilizei `spring-security` para trabalhar com permissões de usuário.
  - É cadastrado automaticamente alguns dados na base H2, conforme configurado na LoadDatabaseDefault.java
  - Também estou utilizando JWT.

### Como rodar ?
- Na raiz do repositório, execute `docker-compose up`
- Acesse http://localhost:3000
- Faça login com algum usuário pré-cadastrado, conforme login/senha:\
-- admin@biblioteca.com   / 12345 (Role admin)\
-- funcionario@biblioteca.com / 12345 (Role funcionário)\
-- suporte@biblioteca.com / 12345 (Role suporte)
