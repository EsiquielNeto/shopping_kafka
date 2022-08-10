package br.com.studies.shopreport.infrastructure.client.kafka;

import br.com.studies.shopreport.application.dto.ShopDTO;
import br.com.studies.shopreport.infrastructure.repository.ReportRepository;
import com.sun.jdi.PrimitiveValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReceiveKafkaMessage {

    private final static String SHOP_TOPIC_EVENT = "SHOP_TOPIC_EVENT";
    private final ReportRepository reportRepository;

    @Transactional
    @KafkaListener(topics = SHOP_TOPIC_EVENT, groupId = "group_report")
    public void listenShopTopic(ShopDTO shopDTO) {
        try {
            log.info("Compra recebida no tópico: {}", shopDTO.getIdentifier());
            reportRepository.incrementShopStatus(shopDTO.getStatus());

        } catch (Exception e) {
            log.error("Erro no processamento da mensagem", e);
        }
    }
}
