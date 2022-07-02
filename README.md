### Tech Stack
- Java 11 +
- Spring Boot
- Spring Security
- JWT

### O que é ?
- CRUD completo de "Livros"
- Geração de token JWT
- Refresh token

### Como rodar ?
- Execute na pasta "livros-backend" **`mvn clean package spring-boot:run`**
- Com isso, irá também ser cadastrado automaticamente alguns dados na base H2, conforme configurado na LoadDatabaseDefault.java

### Como executar endpoints ?
No client de sua preferência, estou utilizando o [insomnia](https://insomnia.rest/)

#### Endpoints:

*1º Cadastrar um livro:*
> **POST** **`http://localhost:8080/livros`**
```json
{
	"isbn" : "6456-ADAD-S-ADDSD3",
	"descricao" : "Livro XPTO",
	"autor" : "Eu mesmo",
	"dtLancamento" : "2010-01-31"
}
```

<hr>

*2º Buscar Authorization para algum usuário:*
> **POST** **`http://localhost:8080/login`**
```json
{
	"username" : "admin@biblioteca.com",
	"password" : "12345"
}
```
Copie do HEADER a Authorization retornada, e adicione nos outros endpoints.

<hr>

*3º Buscar Livros:* (Necessário adicionar 'Authorization: Bearer ...' no header)
> **GET** **`http://localhost:8080/livros/pageable`**

<hr>

*4º Buscar Livro:* (Necessário adicionar 'Authorization: Bearer ...' no header)
> **GET** **`http://localhost:8080/livros/1`**

<hr>

*5º Atualizar Livro:* (Necessário adicionar 'Authorization: Bearer ...' no header)
> **PUT** **`http://localhost:8080/Livro/1`**
```json
{
	"isbn" : "6456-ADAD-S-ADDSD3",
	"descricao" : "Livro XPTO",
	"autor" : "Eu mesmo",
	"dtLancamento" : "2010-01-31"
}
```

<hr>

*6º Deletar Livro:* (Necessário adicionar 'Authorization: Bearer ...' no header)
> **DELETE** **`http://localhost:8080/livros/1`**

<hr>
