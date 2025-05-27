Aplicação Java para armazenamento seguro de credenciais com criptografia e autenticação em dois fatores.

## ✨ Funcionalidades

- ✅ Armazenamento criptografado de senhas (SHA-256)
- ✅ Autenticação com 2FA via SendGrid
- ✅ Verificação contra vazamentos (Have I Been Pwned)
- ✅ Gerenciamento por usuário
- ✅ CLI intuitivo

## 🚀 Começando

### Pré-requisitos
- Java JDK 20+
- Maven 3.8+
- Conta no [SendGrid](https://sendgrid.com/) (para e-mails)
- Git 2.30+

### Instalação
``` git clone https://github.com/joaogldn/pwdmanager_java.git ```
- Acesse o local que você clonou a aplicação e abra seu bash

### Compilar e executar a aplicação
``` mvn clean compile exec:java ```

### ⚙️ Configuração
- Crie um arquivo .env na raiz do projeto:
SENDGRID_API_KEY=sua_chave_aqui
JWT_SECRET=seu_segredo_aqui
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
## Variáveis de Ambiente

| SENDGRID_API_KEY   | Chave para API SendGrid    |
|--------------------|----------------------------|

## 🔒 Política de Senhas
- Todas as senhas são verificadas contra bancos de dados de vazamentos
- Senhas com histórico de vazamento são rejeitadas

## 🤝 Como Contribuir

1. Faça um fork do projeto

2. Crie uma branch (git checkout -b feature/nova-funcionalidade)

3. Commit (git commit -m 'Add feature')

4. Push (git push origin feature)

5. Abra um Pull Request




