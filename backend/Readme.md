# Tasklist API

Este projeto é uma API para gerenciamento de **usuários** e **tarefas (tasks)**.  
Foi desenvolvida em **Java com Spring Boot**, e possui autenticação JWT, documentação automática com **Swagger** e persistência de dados via **H2 (banco em memória)**.

---

## 1. Requisitos

Antes de iniciar, verifique se você possui instalado:

- **Java 17** ou superior
- **Maven 3.8+**
- Um editor de código (recomendado: IntelliJ IDEA ou VS Code)

Verifique as versões executando no terminal:

```bash
java -version
mvn -version
```
## 2. Clonar o repositório

No terminal, execute:

```bash
git clone [https://github.com/seu-usuario/tasklist.git](https://github.com/seu-usuario/tasklist.git)
cd tasklist
```

## 3. Instalar as dependências

Use o Maven para baixar todas as dependências:

```bash
mvn clean install
```

## 4. Configuração do ambiente

O projeto já vem com H2 configurado e uma chave JWT de exemplo 
no arquivo src/main/resources/application.yml. Se quiser alterar a chave JWT, edite:

```bash
security:
  jwt:
    secret: "sua_chave_segura_com_mais_de_32_caracteres"
    expiration: 86400000
```

## 5. Executar o projeto

Para iniciar o servidor:

```bash
mvn spring-boot:run
```

A API ficará disponível em:
http://localhost:8080

## 6. Acessar o banco H2 (opcional)

URL de acesso:

http://localhost:8080/h2-console

Credenciais:

JDBC URL: jdbc:h2:mem:tasklist

User Name: sa

Password: (deixe em branco)

## 7. Documentação da API (Swagger)

Acesse o Swagger em:

http://localhost:8080/swagger-ui/index.html

## 8. Autenticação e uso do token JWT

Crie um usuário com POST /users:

```bash
{
  "name": "Usuário Teste",
  "email": "teste@email.com",
  "password": "123456"
}
```

Faça login em POST /auth/login:

```bash
{
  "email": "teste@email.com",
  "password": "123456"
}
```

O retorno será um token JWT:

```bash
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

Copie o token e clique em Authorize no Swagger.

Digite:

```bash
Bearer SEU_TOKEN_AQUI
```

9. Estrutura do projeto

```bash
src/
 └── main/
     ├── java/br/com/jtech/tasklist/
     │   ├── adapters/
     │   │   ├── input/controllers/       # Controllers
     │   │   └── output/repositories/     # Repositórios
     │   ├── application/                 # Lógica de negócio
     │   ├── config/security/             # JWT e filtros
     │   └── domain/entities/             # Entidades
     └── resources/
         ├── application.yml              # Configurações
         └── data.sql                     # Dados iniciais (opcional)
```