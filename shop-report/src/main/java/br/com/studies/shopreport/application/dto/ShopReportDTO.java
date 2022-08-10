package br.com.studies.shopreport.application.dto;

import br.com.studies.shopreport.domain.model.ShopReport;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopReportDTO {
    private String identifier;
    private Integer amount;

    public static ShopReportDTO convert(ShopReport shopReport) {
        ShopReportDTO shopDTO = new ShopReportDTO();
        shopDTO.setIdentifier(shopReport.getStatus());
        shopDTO.setAmount(shopReport.getAmount());
        return shopDTO;
    }
}
