### Tech Stack
- Java 11 +
- Spring Boot
- Spring Security
- JWT

### O que é ?
- CRUD completo de "Clientes"
- Geração de token JWT
- Refresh token

### Como rodar ?
- Execute **`mvn clean package spring-boot:run`**
- Com isso, irá também ser cadastrado automaticamente alguns dados na base H2, conforme configurado na LoadDatabaseDefault.java

### Como executar endpoints ?
No client de sua preferência, estou utilizando o [insomnia](https://insomnia.rest/)

#### Endpoints:

*1º Cadastrar um cliente:*
> **POST** **`http://localhost:8080/clientes`**
```json
{
	"nome" : "Luciano Ortiz",
	"cpf" : "12345678987",
	"dtNascimento" : "1984-01-31"
}
```

<hr>

*2º Buscar Authorization para algum usuário:*
> **POST** **`http://localhost:8080/login`**
```json
{
	"username" : "luciano@fakeMail.com",
	"password" : "12345"
}
```
Copie do HEADER a Authorization retornada, para utilizar em outros endpoints.

<hr>

*3º Buscar Cliente:* (Necessário adicionar 'Authorization: Bearer ...' no header)
> **GET** **`http://localhost:8080/clientes/1`**

<hr>

*4º Atualizar Cliente:* (Necessário adicionar 'Authorization: Bearer ...' no header)
> **PUT** **`http://localhost:8080/clientes/1`**
```json
{
	"nome" : "Luciano Ortiz - teste",
	"cpf" : "12345678987",
	"dtNascimento" : "1984-01-31"
}
```

<hr>

*5º Deletar Cliente:* (Necessário adicionar 'Authorization: Bearer ...' no header)
> **DELETE** **`http://localhost:8080/clientes/1`**

<hr>
