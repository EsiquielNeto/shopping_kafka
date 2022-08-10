package br.com.studies.eventshop.infra.repository;

import br.com.studies.eventshop.domain.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    Shop findByIdentifier(String identifier);
}
