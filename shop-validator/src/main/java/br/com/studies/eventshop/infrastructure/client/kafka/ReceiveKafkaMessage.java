package br.com.studies.eventshop.infrastructure.client.kafka;

import br.com.studies.eventshop.application.dto.ShopDTO;
import br.com.studies.eventshop.application.dto.ShopItemDTO;
import br.com.studies.eventshop.domain.model.Product;
import br.com.studies.eventshop.infrastructure.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_MESSAGE_KEY;
import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_PARTITION_ID;
import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_TIMESTAMP;
import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReceiveKafkaMessage {

    private static final String SHOP_TOPIC = "SHOP_TOPIC";
    private static final String SHOP_TOPIC_EVENT_NAME = "SHOP_TOPIC_EVENT";
    private final ProductRepository productRepository;
    private final KafkaTemplate<String, ShopDTO> kafkaTemplate;

    @KafkaListener(topics = SHOP_TOPIC, groupId = "group")
    public void listenShopTopic(@Header(RECEIVED_TOPIC) String topic,
                                @Header(RECEIVED_PARTITION_ID) String partitionId,
                                @Header(RECEIVED_MESSAGE_KEY) String key,
                                @Header(RECEIVED_TIMESTAMP) String timestamp,
                                ShopDTO shopDTO) {

        try {
            log.info("Compra recebida: {} | tópico: {} | chave {} | partição {} | hora {}.",
                    shopDTO.getIdentifier(), topic, key, partitionId, timestamp);

            boolean success = true;

            for (ShopItemDTO item : shopDTO.getItems()) {
                Optional<Product> product = productRepository.findByIdentifier(item.getProductIdentifier());

                if (!isValidShop(item, product)) {
                    shopError(shopDTO);
                    success = false;
                    break;
                }
            }

            if (success) {
                shopSuccess(shopDTO);
            }
        } catch (Exception e) {
            log.error("Erro no processamento da compra {}", shopDTO.getIdentifier());
        }
    }

    private boolean isValidShop(ShopItemDTO item, Optional<Product> product) {
        return product.filter(value -> value.getAmount() >= item.getAmount()).isPresent();
    }

    // Envia uma mensagem para o Kafka indicando erro na compra
    private void shopError(ShopDTO shopDTO) {
        log.info("Erro no processamento da compra {}.", shopDTO.getIdentifier());
        shopDTO.setStatus("ERROR");
        kafkaTemplate.send(SHOP_TOPIC_EVENT_NAME, shopDTO);
    }

    // Envia uma mensagem para o Kafka indicando sucesso na compra
    private void shopSuccess(ShopDTO shopDTO) {
        log.info("Compra {} efetuada com sucesso", shopDTO.getIdentifier());
        shopDTO.setStatus("SUCCESS");
        kafkaTemplate.send(SHOP_TOPIC_EVENT_NAME, shopDTO);
    }


}
