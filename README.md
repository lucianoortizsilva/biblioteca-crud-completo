### Stack
- Java 8+
- Spring Boot
- Spring Security
- JWT

### O que é ?
CRUD completo de "Clientes", utilizando JWT, e testes de integração com @WebMvcTest

### Como rodar ?
- Execute **`mvn clean package -Pprod spring-boot:run`**

### Como executar endpoints ?
Como client, estou utilizando o [insomnia](https://insomnia.rest/)

#### Endpoints:

> **POST** **`http://localhost:8080/clientes`**
```json
{
	"nome" : "Luciano",
	"email" : "luciano@gmail.com",
	"senha" : "123456",
	"cpf" : "12345178113",
	"nascimento" : "1984-01-31",
	"perfis" : [2]
}
```
