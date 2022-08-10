package br.com.studies.eventshop.infrastructure.repository;

import br.com.studies.eventshop.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdentifier(String identifier);
}
