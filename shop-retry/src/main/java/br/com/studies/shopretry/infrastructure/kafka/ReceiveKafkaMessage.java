package br.com.studies.shopretry.infrastructure.kafka;

import br.com.studies.shopretry.application.dto.ShopDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReceiveKafkaMessage {

    private static final String SHOP_TOPIC = "SHOP_TOPIC";
    private static final String SHOP_TOPIC_RETRY = "SHOP_TOPIC_RETRY";
    private final KafkaTemplate<String, ShopDTO> kafkaTemplate;

    @KafkaListener(topics = SHOP_TOPIC, groupId = "group_retry")
    public void listenShopTopic(ShopDTO shopDTO) {
        try {
            log.info("Compra recebida no tópico: {}.", shopDTO.getIdentifier());

            if (shopDTO.getItems() == null || shopDTO.getItems().isEmpty()) {
                log.error("Compra sem items.");
                throw new Exception();
            }

        } catch (Exception e) {
            log.info("Erro na aplicação");
            kafkaTemplate.send(SHOP_TOPIC_RETRY, shopDTO);
        }
    }

    @KafkaListener(topics = SHOP_TOPIC_RETRY, groupId = "group_retry")
    public void listenShopTopicRetry(ShopDTO shopDTO) throws Exception {
        log.info("Retentativa de processamento: {}.", shopDTO.getIdentifier());
    }
}
