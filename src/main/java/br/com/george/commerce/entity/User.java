package br.com.george.commerce.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Perfil do cliente/equipe na loja. Login, senha, 2 etapas e papéis ficam no Keycloak;
 * aqui fica o que é da loja (CPF, endereços, pedidos, bloqueio), ligado pelo {@code sub} do token.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** {@code sub} do token do Keycloak: identidade estável (o e-mail pode mudar). */
    @Column(name = "keycloak_id", unique = true)
    private String keycloakId;

    /** Pedido no checkout (nota fiscal e pagamento); conta recém-criada ainda não tem. */
    @Column(unique = true)
    private String cpf;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    /** Bloqueio pela loja (backoffice). Bloqueado continua entrando no Keycloak, mas a API recusa (403). */
    private Boolean active;
}
