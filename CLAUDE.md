# CLAUDE.md

Este arquivo orienta o Claude Code (claude.ai/code) ao trabalhar com o código deste repositório.

## Contexto do projeto

Backend da **Nani Perfumes** ("Essência do Oriente"), e-commerce de perfumes começando por perfumes árabes. Meta: MVP funcional até o fim de 2026. O frontend fica em outro repositório (`commerce-web`, Angular 22) e hoje consome dados mockados; a integração com esta API vem depois que o visual fechar.

Cerca de 90% dos clientes vão acessar pelo celular: respostas enxutas (sem campos desnecessários) e endpoints de listagem pensados para rede móvel.

## Estratégia: front primeiro

**O frontend conduz o projeto.** Primeiro todas as telas e features são construídas no `commerce-web` com dados mockados. Só depois, com o front validado, este backend é **adaptado para sustentar o que o front precisa**.

- Enquanto o front está em construção, este repositório fica em espera: não criar features novas aqui por antecipação.
- Na fase de adaptação, a referência é o front: os models (`core/models`) e os mocks (`core/data`) do `commerce-web` definem os dados e formatos esperados. Divergências entre a API atual e o front são resolvidas a favor do front, a menos que haja um motivo técnico ou de segurança (nesse caso, sinalizar e discutir).
- A adaptação é o momento certo para as mudanças que alteram o contrato da API (formato de erro, paginação, nomes de campos), porque ainda não há cliente consumindo.

## Comandos

Ambiente: Windows com Git Bash (MINGW64). Usar o Maven Wrapper.

- `docker compose up -d`: sobe o Postgres de desenvolvimento (porta 5435; senhas no `.env`, copiado de `.env.example`). **Precisa estar no ar** para rodar a API e para o `verify`.
- `./mvnw spring-boot:run`: sobe a API em `http://localhost:8080` com o profile `dev` (padrão)
- `./mvnw test`: roda os testes (Postgres num container via Testcontainers: **Docker aberto**)
- `./mvnw test -Dtest=NomeDaClasseTest`: roda uma classe de teste específica
- `./mvnw verify`: build completo. Na fase de integration-test, sobe a aplicação na porta 8085 e **regenera `docs/swagger.yaml`**. Rodar sempre que mudar algum endpoint ou DTO.
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Stack

Java 21, Spring Boot 4.1 (Web, Data JPA, Security, Validation), OAuth2 Resource Server (token do Keycloak), Lombok, MapStruct 1.6, springdoc-openapi, PostgreSQL 17 (dev, testes e produção) e Flyway.

## Profiles e banco

- **Postgres em todo lugar** (dev pelo `docker-compose.yml`, testes pelo Testcontainers, produção). Usuário `commerce_api` só com o schema `commerce_api` (A7).
- **O banco só muda por migração do Flyway** (`src/main/resources/db/migration`, `V1__...`, `V2__...`). O Hibernate só valida (`ddl-auto=validate`): entidade diferente da tabela = a API nem sobe. **Migração que já rodou nunca é editada** — mudança é sempre uma migração nova. Entidade nova ou campo novo = migração nova no mesmo commit.
- `dev` (padrão): também roda `db/dev/R__dados_de_desenvolvimento.sql` (dados de teste; repetível: roda de novo quando o arquivo muda e começa com `TRUNCATE`). Ao criar uma tabela nova, incluir no `TRUNCATE` e nos inserts.
- `prod` (`SPRING_PROFILES_ACTIVE=prod`): só as migrações. Conexão por `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.

## Arquitetura

Pacote base `br.com.george.commerce`, em camadas:

- `controller/`: separado por público-alvo e depois por domínio:
  - `publico/`: sem autenticação (catálogo, cadastro, login)
  - `me/`: área do cliente autenticado (`/me/...`: carrinho, pedidos, endereços, conta, afiliado)
  - `admin/`: backoffice (catálogo, pedidos, financeiro, inventário, relatórios, usuários, vendas manuais)
- `service/`: interfaces; implementações em `service/impl/`. Usuário do token (`CurrentUserService`) e conversor de papéis do Keycloak em `service/security/`.
- `repository/`: Spring Data JPA.
- `entity/`: entidades JPA. `enums/`: enums de domínio.
- `dto/`: records de request e response, agrupados por domínio (`dto/product`, `dto/cart`…).
- `mapper/`: MapStruct (entidade → response).
- `exception/`: exceções de domínio + `GlobalExceptionHandler` (`@RestControllerAdvice`).
- `config/`: Security, CORS, Swagger, `PasswordEncoder`.

Controllers só recebem, validam e delegam. **Toda regra de negócio fica nos services.** Controllers nunca acessam repositories.

## Convenções

Seguir o padrão do código existente:

- **SOLID.** Service sempre como interface + `Impl`. Injeção por construtor com `@RequiredArgsConstructor` e campos `private final`. Nunca usar `@Autowired` em campo.
- **Nomes:** código (entidades, DTOs, services, métodos) em inglês. Controllers e pacotes de controller em português (`MeuCarrinhoController`, `controller/publico/catalogo`). Rotas REST em inglês, no plural e em kebab-case (`/products`, `/manual-sales`, `/me/cart/items`).
- **DTOs:** sempre `record`. Requests com nome `CreateXRequest` / `UpdateXRequest` e Bean Validation. Respostas com nome `XResponse`. **Nunca expor entidade JPA em um controller.**
- **Mapper:** MapStruct com `componentModel = "spring"`. Não fazer mapeamento manual campo a campo.
- **Entidades:** Lombok `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor` e `@AllArgsConstructor`. **Não usar `@Data`** em entidade. Tabelas no plural em snake_case (`@Table(name = "products")`).
- **Dinheiro:** sempre `BigDecimal`, nunca `double` ou `float`.
- **Transações:** `@Transactional` em todo método de service que escreve em mais de uma tabela ou depende de consistência (pedido, pagamento, estoque). Baixa de estoque é atômica no banco (`productRepository.decreaseStock`, que verifica as linhas afetadas); manter esse padrão.
- **Erros:** toda exceção de regra **estende `BusinessException`** passando status, `code` estável em inglês maiúsculo (ex.: `OUT_OF_STOCK`) e a frase para o cliente em português: `super(HttpStatus.UNPROCESSABLE_CONTENT, "OUT_OF_STOCK", "Não temos mais unidades de ...")`. O `GlobalExceptionHandler` transforma tudo em `ProblemDetail` (RFC 9457): `{ status, code, detail }` (+ `errors` campo → mensagem na validação). **Não precisa registrar handler novo.** Status: 404 não encontrado (também para recurso de outro usuário, sem revelar que existe), 409 conflito, 422 regra recusada, 400 dado inválido, 401/403 sessão e permissão. Erro inesperado vira 500 genérico e vai para o log. Nada de `try-catch` espalhado; nunca stack trace, SQL ou nome de classe na resposta.
- **Paginação obrigatória:** todo endpoint que lista uma coleção que cresce (produtos, pedidos, usuários, relatórios) recebe `Pageable` e devolve página, com tamanho máximo limitado. Listas pequenas e fixas (categorias, marcas) podem ser devolvidas sem paginação.
- **Consultas e N+1:** relacionamentos novos com `fetch = FetchType.LAZY` (inclusive `@ManyToOne`, que é EAGER por padrão). Quando a resposta precisa do relacionamento, buscar na mesma consulta com `@EntityGraph` ou `JOIN FETCH`. Ao criar ou alterar uma listagem, conferir no log (`show-sql`) que ela não dispara uma consulta por item.
- **Mensagens para o usuário** (exceções e validação) em **português**: o front exibe essas mensagens diretamente.
- **Swagger:** todo controller com `@Tag` (nome e descrição em português). Depois de mudar endpoint ou DTO, rodar `./mvnw verify` para atualizar o `docs/swagger.yaml`.

## Segurança

- API stateless, **resource server do Keycloak** (commerce-auth): aceita só token com emissor = realm da loja e `aud = commerce-api` (`SecurityConfig`). Papéis do realm (`ROLE_*`) e permissões do cliente `commerce-api` viram authorities (`KeycloakJwtAuthenticationConverter`). Login, cadastro, senha, 2 etapas e papéis são do Keycloak — **nada disso existe aqui**. Quem chama é o BFF (sem CORS).
- Rotas públicas ficam no `SecurityConfig`: `GET` do catálogo (`/products`, `/categories`, `/brands`, `/promotions`) e Swagger. **O resto exige autenticação.**
- Autorização por método com `@PreAuthorize` **em todo endpoint** não público:
  - cliente: `isAuthenticated()`
  - leitura do backoffice: `hasAnyRole('VIEWER','ADMIN','SUPER_ADMIN')`
  - escrita do backoffice: `hasAnyRole('ADMIN','SUPER_ADMIN')`
  - gestão de usuários e ações críticas: `hasRole('SUPER_ADMIN')`
- Endpoints `/me/**` identificam o usuário pelo token (`currentUserService.get()`, ligado pelo `sub` em `users.keycloak_id`), **nunca** por um id recebido na requisição. No primeiro acesso o perfil é ligado pelo e-mail confirmado ou criado sem CPF.
- Senhas sempre com hash BCrypt (`PasswordConfig`). Nunca logar nem devolver senha, token ou hash.
- Toda entrada validada com Bean Validation nos DTOs e `@Valid` no parâmetro do controller.
- Nunca colocar segredo (senha de banco, segredo de cliente) no código ou nos `.properties` versionados. Usar variável de ambiente.

## Testes

Hoje só existe o teste de contexto (`CommerceApiApplicationTests`). Para código novo:

- Testes unitários de service com JUnit 5 + Mockito, cobrindo as regras de negócio e os caminhos de erro.
- Testes de controller com `@WebMvcTest` + `spring-security-test` quando houver regra de autorização relevante.
- Testes de integração com `@SpringBootTest` + `@Import(TestcontainersConfiguration.class)`: Postgres real num container, com as migrações e os dados de teste.
- Nome dos testes descrevendo o comportamento (`shouldThrowWhenStockIsInsufficient`).

## Dívidas técnicas conhecidas

**Na fase de adaptação ao front (mudam o contrato da API, então entram antes da integração):**

- Adicionar paginação às listagens que hoje devolvem lista completa (produtos, pedidos, usuários, relatórios).
- Revisar os relacionamentos existentes: `@ManyToOne` estão EAGER (padrão) e a listagem de produtos pode ter N+1 (categoria, marca e atributos).

**Antes de ir para produção (bloqueiam o deploy):**

- As credenciais do Postgres estão em `application-postgres.properties`: mover para variáveis de ambiente.
- O CORS está fixo em `http://localhost:4200` (`SecurityConfig`): tornar configurável por ambiente.
- Não há health check: adicionar o Spring Boot Actuator expondo só `/actuator/health` (usado pelo load balancer da AWS).
- Pool de conexões (HikariCP) com o tamanho padrão: ajustar `maximum-pool-size` ao limite de conexões do banco de produção.

**Revisão pós-MVP (não fazer durante as etapas):**

- Os services repetem a resolução do usuário logado (email → `User`). Extrair para um componente.
- `UserServiceImpl` injeta `BCryptPasswordEncoder` (classe concreta); trocar pela interface `PasswordEncoder` (DIP).
- A cobertura de testes é baixa.
- Cache (Caffeine) para o catálogo público, métricas com Micrometer/Prometheus e Virtual Threads (`spring.threads.virtual.enabled=true`): avaliar com base no tráfego real.

## Fluxo de trabalho

- O dono do projeto é dev backend Java/Spring pleno: não precisa simplificar as explicações de backend.
- Trabalhar em passos pequenos: propor o plano da sessão, esperar aprovação e então dizer exatamente o que fazer (comando, arquivo, conteúdo). Ele aplica e valida antes do próximo passo. Não rodar comandos nem editar arquivos sem ele pedir.
- Cada sessão tem um objetivo claro combinado no início (ex.: ajustar uma tela, uma seção ou um comportamento) e termina com ele funcionando e validado. O roadmap (`roadmap-mvp.md`, na área Contexto do projeto) é um guia do que falta, não uma ordem obrigatória.
- Refatorações de arquitetura ficam para a revisão pós-MVP. Se um atalho for inevitável por causa do prazo, sinalizar explicitamente como **dívida técnica**.

## Regras de Git

- **O Claude nunca executa `git add`, `git commit` nem `git push`** (nem outros comandos que alterem o histórico ou o remoto, como `merge`, `rebase`, `reset` ou `tag`). Essas ações são sempre feitas manualmente pelo dono do projeto. O Claude pode sugerir o comando e a mensagem de commit, mas não executa.
- Trabalhar sempre na branch `develop`.
- Ao sugerir comandos git, entregar em uma linha só, encadeados com `&&`, e sempre com `git push origin develop` (nunca só `git push`). Ex.: `git add <arquivos> && git commit -m "mensagem" && git push origin develop`.
- Nunca adicionar linha de coautoria (`Co-Authored-By: Claude`) nem "Generated with Claude Code" em mensagens de commit ou descrições de PR.
