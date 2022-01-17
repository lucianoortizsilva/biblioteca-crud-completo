### Tech Stack
- Java 8+
- Spring Boot
- Spring Security
- JWT

### O que é ?
CRUD completo de "Clientes", utilizando JWT, e testes de integração com @WebMvcTest

### Como rodar ?
- Execute **`mvn clean package -Pdev spring-boot:run`**

### Como executar endpoints ?
No client de sua preferência, estou utilizando o [insomnia](https://insomnia.rest/)

#### Endpoints:

*1º Cadastrar um cliente:*
> **POST** **`http://localhost:8080/clientes`**
```json
{
	"nome" : "Luciano Ortiz",
	"cpf" : "12345678987",
	"nascimento" : "1984-01-31"
}
```

<hr>

*2º Buscar Authorization:*
> **POST** **`http://localhost:8080/login`**
```json
{
	"email" : "luciano@email.com",
	"senha" : "12345"
}
```

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
	"nascimento" : "1984-01-31"
}
```

<hr>

*5º Deletar Cliente:* (Necessário adicionar 'Authorization: Bearer ...' no header)
> **DELETE** **`http://localhost:8080/clientes/1`**

<hr>
