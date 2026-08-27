package br.com.george.commerce.repository;

import br.com.george.commerce.entity.AffiliateSale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AffiliateSaleRepository extends JpaRepository<AffiliateSale, Long> {

    List<AffiliateSale> findByAffiliateId(Long affiliateId);

}

