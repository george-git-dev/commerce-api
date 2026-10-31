package br.com.george.commerce;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/** Sobe a API inteira: migrações do Flyway rodam e o Hibernate confere as entidades contra as tabelas. */
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class CommerceApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
