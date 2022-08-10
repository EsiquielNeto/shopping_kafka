package br.com.studies.eventshop.infra.client.kafka;

import br.com.studies.eventshop.application.dto.ShopDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaClient {

    private final KafkaTemplate<String, ShopDTO> kafkaTemplate;
    private static final String TOPIC = "SHOP_TOPIC";

    public void sendMessage(ShopDTO shopDTO) {
        kafkaTemplate.send(TOPIC, String.valueOf(shopDTO.getBuyerIdentifier()), shopDTO);
    }
}
