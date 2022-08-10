package br.com.studies.shopreport.infrastructure.repository;

import br.com.studies.shopreport.domain.model.ShopReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<ShopReport, Long> {

    @Modifying
    @Query(nativeQuery = true,
            value = "update shop_report s set s.amount = s.amount + 1 where status = :shopStatus")
    void incrementShopStatus(String shopStatus);
}
