package br.com.george.commerce.entity;

import br.com.george.commerce.enums.Role;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false, unique = true)
    private String cpf;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private Boolean active;

    @Enumerated(EnumType.STRING)
    private Role role;
}
