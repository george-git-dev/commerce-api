package br.com.george.commerce.repository;

import br.com.george.commerce.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    /** Endereços da conta: principal primeiro, depois na ordem de cadastro. */
    List<Address> findByUserIdAndActiveTrueOrderByPrimaryAddressDescIdAsc(Long userId);

    /** Só acha endereço ativo e do próprio usuário (de outra conta = "não encontrado"). */
    Optional<Address> findByIdAndUserIdAndActiveTrue(Long id, Long userId);
}
