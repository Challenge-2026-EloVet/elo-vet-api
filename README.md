# 🐾 Elo Vet — API (Spring Boot)

> **FIAP Challenge 2026 — Clyvo Vet** | **Disciplina:** Java Advanced

O momento de maior risco para a saúde do pet e para o negócio da clínica é a **saída da consulta**: o tutor vai para casa com muita orientação falada e pouca coisa por escrito. Isso gera descontinuidade no tratamento, ansiedade e evasão de clientes.

O **Elo Vet** conecta o pós-consulta ao dia a dia do tutor. O veterinário registra um **plano de cuidado** com checklist; o tutor acompanha e marca cada item pelo app; a clínica mantém o histórico e as notificações de cada etapa.

Esta é a **API backend** do ecossistema. Ela concentra as regras de negócio, a autenticação e a persistência, e é consumida pelo **app mobile do tutor**.

---

## 👥 Integrantes

| Nome | RM |
| --- | --- |
| Arthur Graciani | RM561728 |
| Gustavo Oliveira | RM566358 |
| João Pedro Scarpin | RM565421 |
| Lucas Hideki | RM565355 |
| Wesley Andrade | RM563593 |

## 🔗 Links

- 📹 **Vídeo da apresentação:** https://www.youtube.com/watch?v=U0qIjOQwTyY
- 📱 **Repositório do app mobile:** https://github.com/Challenge-2026-EloVet/EloPet-Mobile
- 🤖 **Bira AI (microsserviço de IA):** https://github.com/Challenge-2026-EloVet/bira-ai-api

---

## 🏗️ Arquitetura

A API é organizada **por domínio**, com as camadas separadas em cada um:

```
src/main/java/com/br/elovetapi
├── security     # autenticação JWT, filtro de requisições, cadastro e login
├── user         # entidade de login e perfis (UserRole)
├── responsible  # tutores e vínculo tutor-pet
├── pet          # cadastro de pets
└── care         # plano de cuidado, checklist, notificações e lembretes
    ├── controller / service / repository / model
    ├── dtos / mapper          # entidades nunca são expostas diretamente
    ├── validation             # validações de regra e tratamento de erros
    └── security               # autorização por dono do plano
```

---

## 🛠️ Tecnologias

| Camada | Tecnologia |
| --- | --- |
| Linguagem e framework | Java 21, Spring Boot 4 (Web MVC, Data JPA, Validation) |
| Segurança | Spring Security, JWT (`com.auth0:java-jwt`), BCrypt, `@PreAuthorize` |
| Banco de dados | PostgreSQL |
| Versionamento do schema | Flyway |
| Build | Gradle |
| Produtividade | Lombok |
| CI/CD | GitHub Actions → Azure Web App |

---

## 🔐 Segurança e perfis

A autenticação é **stateless**: o login devolve um token JWT (válido por 2 horas) que deve ser enviado no header `Authorization: Bearer <token>`. As senhas são armazenadas com **BCrypt**.

| Perfil | Quem é | O que pode fazer |
| --- | --- | --- |
| `ADMIN` | Administração da clínica | Tudo, inclusive cadastrar veterinários e outros administradores |
| `VETERINARIO` | Veterinário da clínica | Criar planos de cuidado e consultar os planos dos tutores |
| `RESPONSAVEL` | Tutor do pet | Gerenciar seus dados e pets e acompanhar o checklist do plano |

Regras de destaque:

- **Cadastro público só cria tutores.** `POST /auth/register` aceita o perfil `RESPONSAVEL` sem token; qualquer outro perfil exige o token de um `ADMIN` (sem ele, **403**).
- **Marcar item do checklist** é permitido apenas ao tutor dono do plano, ao veterinário que o criou ou a um `ADMIN` (`CarePlanAuthorizationService`).
- **Respostas sempre em DTO.** Entidades JPA não são serializadas, então dados internos como o hash da senha nunca saem da API.

---

## 📡 Endpoints principais

### Autenticação

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| `POST` | `/auth/register` | Público (RESPONSAVEL) / ADMIN (demais perfis) | Cria o login; para tutores, cria também o registro de responsável |
| `POST` | `/auth/login` | Público | Devolve o token JWT e, para tutores, o `idResponsavel` |

### Tutores e pets

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| `GET` | `/responsible/{id}` | Autenticado | Dados do tutor e do login (sem senha) |
| `PUT` / `DELETE` | `/responsible/{id}` | ADMIN, RESPONSAVEL | Atualiza ou remove o tutor |
| `POST` | `/responsible/pets` | ADMIN, RESPONSAVEL | Vincula um pet ao tutor (pet existente ou criado na mesma chamada) |
| `GET` | `/responsible/pets/{idResponsavel}` | Autenticado | Pets vinculados ao tutor |
| `DELETE` | `/responsible/pets/{idPetResponsavel}` | ADMIN, RESPONSAVEL | Desfaz o vínculo |
| `GET` | `/pet` · `/pet/{id}` | Autenticado | Consulta de pets |
| `POST` / `PUT` / `DELETE` | `/pet` · `/pet/{id}` | ADMIN, RESPONSAVEL | Cadastro, edição e remoção de pets |

### Plano de cuidado (fluxo principal)

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| `POST` | `/veterinaries/{vetId}/care-plans` | ADMIN, VETERINARIO | Cria o plano com checklist e registra a notificação de handoff ao tutor |
| `GET` | `/users/{userId}/care-plans` | ADMIN, VETERINARIO | Lista os planos de um tutor com o status de cada item |
| `POST` | `/care-plans/{carePlanId}/items/{itemId}/mark` | Dono do plano, veterinário do plano, ADMIN | Marca um item como `PENDING`, `DONE` ou `CANCELLED` (com auditoria) |
| `POST` | `/admin/process-reminders` | ADMIN | Gera lembretes para os itens pendentes que vencem hoje |

---

## 🔄 Fluxo principal

1. O **tutor** se cadastra pelo app (`/auth/register`) e faz login.
2. O tutor cadastra o **pet** já vinculado a ele (`/responsible/pets`).
3. Após a consulta, o **veterinário** cria o **plano de cuidado** com os itens do checklist. A API registra a notificação de handoff para o tutor e um registro de auditoria.
4. O tutor acompanha o plano e **marca cada item** conforme cumpre o tratamento; cada marcação gera auditoria.
5. A clínica pode disparar **lembretes** para itens pendentes que vencem no dia.

> O arquivo [`api.http`](./api.http) executa esse fluxo de ponta a ponta no HTTP Client do IntelliJ, salvando automaticamente tokens e IDs entre as requisições. Ele também demonstra as regras de segurança (respostas **403** esperadas).

---

## 🗄️ Banco de dados e migrations (Flyway)

O schema é versionado exclusivamente pelo Flyway (`src/main/resources/db/migration`). O Hibernate não altera o banco (`ddl-auto=none` por padrão).

| Versão | Conteúdo |
| --- | --- |
| `V1__create_tables.sql` | Login, responsável, pet e vínculo pet-responsável, com chaves, unicidade e restrições de domínio (CPF, sexo, castrado) |
| `V2__inserts.sql` | Dados iniciais de pets e usuários |
| `V3__create_care_tables.sql` | Plano de cuidado, itens do checklist, notificações e log de acompanhamento, com índices para as consultas do fluxo |

---

## 🚀 Como executar

### Pré-requisitos

- Java 21
- PostgreSQL acessível (local ou na nuvem)

### Variáveis de ambiente

| Variável | Obrigatória | Padrão | Descrição |
| --- | --- | --- | --- |
| `SPRING_DATASOURCE_URL` | Sim | — | Ex.: `jdbc:postgresql://localhost:5432/elovet` |
| `SPRING_DATASOURCE_USERNAME` | Sim | — | Usuário do banco |
| `SPRING_DATASOURCE_PASSWORD` | Sim | — | Senha do banco |
| `JWT_SECRET` | Recomendado | `my-secret-key` | Segredo de assinatura do JWT (**defina um valor próprio em produção**) |
| `CORS_ALLOWED_ORIGINS` | Não | `http://localhost:5127,http://localhost:8081` | Origens liberadas no CORS, separadas por vírgula |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Não | `none` | Mantenha `none`: o schema é responsabilidade do Flyway |

### Executando

```powershell
# Windows (PowerShell)
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/elovet"
$env:SPRING_DATASOURCE_USERNAME="postgres"
$env:SPRING_DATASOURCE_PASSWORD="postgres"
.\gradlew.bat bootRun
```

```bash
# Linux / macOS
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/elovet
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=postgres
./gradlew bootRun
```

A API sobe em `http://localhost:8080` e o Flyway aplica as migrations automaticamente.

---

## ☁️ Deploy

O workflow [`.github/workflows/main_api-java-seurm.yml`](./.github/workflows/main_api-java-seurm.yml) é disparado a cada push na `main`:

1. **Build:** compila com Gradle e gera o `.jar` (`bootJar`).
2. **Deploy:** autentica na Azure via OIDC (segredos no GitHub) e publica o `.jar` no **Azure Web App** `api-java-seurm`.

As credenciais do banco e o segredo do JWT ficam nas configurações do App Service, fora do repositório.

---

## 🧩 Integração com as demais disciplinas

| Disciplina | Como se conecta à API |
| --- | --- |
| Mobile Application Development | O app do tutor consome os endpoints de cadastro, login, pets e plano de cuidado |
| DevOps Tools & Cloud Computing | Build e deploy automatizados para o Azure Web App, com banco PostgreSQL na nuvem |
| Mastering Relational and Non-Relational Database | Modelagem relacional versionada nas migrations Flyway |
| Disruptive Architectures: IoT, IoB & Generative AI | O Bira AI gera o plano de cuidado do tutor a partir das anotações clínicas da consulta |

---

_Elo Vet — Cuidar de quem amamos: é isso que nos une._ 🐾
