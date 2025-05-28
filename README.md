Aplicação Java para armazenamento seguro de credenciais com criptografia e autenticação em dois fatores.

## 🚀 Começando

### Pré-requisitos
- Java JDK 20+
- Maven 3.8+
- Conta no [SendGrid](https://sendgrid.com/) (para e-mails)
- Git 2.30+

## Ferramentas utilizadas
- Visual Studio Code
- DB Browser v3.13.1
- Git

### Instalação
``` git clone https://github.com/joaogldn/pwdmanager_java.git ```
- Acesse o local que você clonou a aplicação e abra seu bash

### Compilar e executar a aplicação
``` mvn clean compile exec:java ```

### ⚙️ Configuração
- Crie um arquivo .env na raiz do projeto:
SENDGRID_API_KEY=sua_chave_aqui

#### Exemplo  (nunca comite este arquivo, o que está no código é possui chave inexistente!)

### Fluxo Principal
1. Registre um novo usuário para acessar a aplicação
2. Faça login com 2FA
3. Adicione/gerencie senhas
4. Visualize credenciais com verificação de token

## 🔧 Tecnologias

| Componente     | Tecnologia     |
|----------------|----------------|
| Backend        | Java 20        |
| Criptografia   | SHA-256        |
| Armazenamento  | SQLite         |
| 2FA            | SendGrid API   |
| Verificação    | HIBP API       |

## 📁 Estrutura do Projeto

```text
src/
├── main/
│   ├── java/
│   │   └── com/pwdmanager/pwdmanager/
│   │       ├── config/      # Configurações da aplicação 
│   │       ├── dao/         # Camada de acesso a dados 
│   │       ├── model/       # Entidades e modelos de dados 
│   │       ├── service/     # Lógica de negócio 
│   │       ├── util/        # Utilitários 
│   │       └── Main.java    # Classe principal que inicia a aplicação
│   └── resources/           # Recursos estáticos ou arquivos de configuração
├── pom.xml                  # Gerenciador de dependências Maven
├── .gitignore               # Arquivos/pastas ignorados pelo Git
├── pwd_manager.db           # Banco de dados SQLite local
````

## Componentes Principais
### 1. Configuração do Banco de Dados
**DatabaseConfig.java**

- Estabelece conexão com banco SQLite

- Padrão Singleton para conexões

- URL do banco: jdbc:sqlite:pwd_manager.db

- Carrega driver JDBC do SQLite

### 2. Modelos de Dados
**PasswordEntry.java**

- Representa uma entrada de senha no sistema

- Atributos: id, userId, title, username, password, service

- Métodos getters/setters e toString()

**User.java**

- Modelo de usuário do sistema

- Atributos: id, name, email, salt, hashedPassword

- Métodos getters/setters

**Credential.java**

- Representa credenciais criptografadas

- Atributos: service, username, encryptedPassword

**TwoFactorToken.java**

- Modelo para tokens de autenticação de dois fatores

- Contém token e data de expiração

- Método isExpired() para verificar validade

### 3. Camada de Acesso a Dados (DAO)
*PasswordDAO.java*

- Operações CRUD para senhas

Métodos:

- save(): Salva nova senha (com criptografia)

- findById(): Busca senha por ID

- findAllByUserId(): Lista todas senhas de um usuário

- update(): Atualiza entrada de senha

- delete(): Remove entrada de senha

**UserDAO.java**

- Operações CRUD para usuários

Métodos:

- save(): Registra novo usuário

- emailExists(): Verifica se email já está cadastrado

- findById()/findByEmail(): Busca usuários

- findAll(): Lista todos usuários

- update()/delete(): Atualiza/remove usuários

### 4. Serviços de Aplicação
**AuthService.java**

- Gerencia autenticação de usuários

Funcionalidades:

- registerUser(): Cadastra novos usuários

- login(): Autentica usuários existentes

- generateStrongPassword(): Gera senhas seguras

**EncryptionService.java**

- Implementa criptografia

Métodos:

- criptografar()/descriptografar()

- PasswordBreachService.java

- Verifica se senha foi comprometida usando API Have I Been Pwned

- Método checkPasswordBreach()

**TwoFactorAuthService.java**

- Envia tokens 2FA por email usando SendGrid

Método enviarToken()

** TokenService.java**

- Gera e valida tokens temporários

- Tokens válidos por 5 minutos

### 5. Utilitários
**CryptoUtils.java**

- Utilitário de criptografia com chave fixa

- Métodos encrypt()/decrypt()

**HashUtils.java**

- Geração de hashes SHA-256 para senhas

**PasswordGenerator.java**

- Gera senhas seguras com critérios:

- Mínimo 12 caracteres

- Mistura de maiúsculas, minúsculas, números e símbolos

- Método calculatePasswordStrength() avalia força da senha

**SaltUtils.java**

- Gera salts aleatórios para hashing de senhas

### 6. Ponto de Entrada
**Main.java**

- Interface de linha de comando (CLI)

Fluxos principais:

- Cadastro de usuários

- Login com 2FA

Gerenciamento de senhas:

- Cadastro

- Listagem

- Visualização (com verificação 2FA)

## Fluxos Principais
### 1. Cadastro de Usuário
- Coleta nome, email e senha

- Gera salt aleatório

- Cria hash da senha com salt

- Salva usuário no banco

### 2. Autenticação
- Verifica credenciais (email/senha)

- Gera token 2FA

- Envia token por email

- Valida token inserido pelo usuário

### 3. Gerenciamento de Senhas
- Cadastro:

- Criptografa senha antes de armazenar

Listagem:

- Mostra apenas metadados (não mostra senhas)

Visualização:

- Requer novo token 2FA

- Descriptografa senha para exibição

## Considerações de Segurança
**Criptografia:**

- Senhas armazenadas no SQlite já criptografadas

- Chave de criptografia fixa **(deve ser externalizada em produção)**

**Hashing:**

- Senhas de usuários armazenadas como hashes (SHA-256) com salt

**Autenticação de Dois Fatores:**

- Token temporário enviado por email

- Validade de 5 minutos

**Verificação de Senhas Comprometidas:**

- Integração com Have I Been Pwned API

**Geração de Senhas Seguras:**

- Algoritmo que garante complexidade mínima

##Dependências
- SQLite JDBC Driver

- SendGrid (para envio de emails)

- Apache HttpClient (para integração com HIBP API)

## Variáveis de Ambiente

| SENDGRID_API_KEY   | Chave para API SendGrid    |
|--------------------|----------------------------|

## 🔒 Política de Senhas
- Todas as senhas são verificadas contra bancos de dados de vazamentos
- Senhas com histórico de vazamento são rejeitadas





