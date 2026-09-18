package com.softdesign.livetest.domain.venda.adiciolnaritemvenda;

import com.softdesign.livetest.applicationservice.venda.AdicionarItemVendaApplicationService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = {
                "adicionar-item-venda",
                "adicionar-item-venda-dlt"
        }
)
class AdicionarItemVendaKafkaRetryIntegrationTest {

    @Autowired
    private KafkaTemplate<String, AdicionarItemVendaMessage> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @MockitoSpyBean
    private AdicionarItemVendaApplicationService adicionarItemVendaApplicationService;

    @Test
    void deveTentarTresVezesEEnviarParaDlt() throws Exception {

        var message = new AdicionarItemVendaMessage(
                "123",
                "123",
                2
        );

        doThrow(new RuntimeException("Erro ao adicionar item"))
                .when(adicionarItemVendaApplicationService)
                .executar(
                        message.vendId(),
                        message.produtoId(),
                        message.quantidade()
                );

        var consumer = criarConsumerDlt();

        try {
            embeddedKafka.consumeFromAnEmbeddedTopic(
                    consumer,
                    "adicionar-item-venda-dlt"
            );

            kafkaTemplate.send(
                    "adicionar-item-venda",
                    message
            ).get();

            ConsumerRecord<String, AdicionarItemVendaMessage> dltRecord =
                    KafkaTestUtils.getSingleRecord(
                            consumer,
                            "adicionar-item-venda-dlt",
                            Duration.ofSeconds(15)
                    );

            assertEquals(message, dltRecord.value());

            verify(adicionarItemVendaApplicationService, times(3))
                    .executar(
                            message.vendId(),
                            message.produtoId(),
                            message.quantidade()
                    );

        } finally {
            consumer.close();
        }
    }

    private Consumer<String, AdicionarItemVendaMessage> criarConsumerDlt() {

        var props = KafkaTestUtils.consumerProps(
                "dlt-test-group",
                "false",
                embeddedKafka
        );

        props.put(
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest"
        );

        var jsonDeserializer =
                new JsonDeserializer<AdicionarItemVendaMessage>(
                        AdicionarItemVendaMessage.class
                );

        jsonDeserializer.addTrustedPackages("*");

        ConsumerFactory<String, AdicionarItemVendaMessage> consumerFactory =
                new DefaultKafkaConsumerFactory<>(
                        props,
                        new StringDeserializer(),
                        jsonDeserializer
                );

        return consumerFactory.createConsumer();
    }
}