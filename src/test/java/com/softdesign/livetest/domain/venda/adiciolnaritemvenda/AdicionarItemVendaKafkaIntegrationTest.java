package com.softdesign.livetest.domain.venda.adiciolnaritemvenda;

import com.softdesign.livetest.applicationservice.venda.AdicionarItemVendaApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = {"adicionar-item-venda"}
)
class AdicionarItemVendaKafkaIntegrationTest {

    @Autowired
    private KafkaTemplate<String, AdicionarItemVendaMessage> kafkaTemplate;

    @MockitoSpyBean
    private AdicionarItemVendaApplicationService adicionarItemVendaApplicationService;

    @Test
    void deveConsumirMensagemDeAdicionarItemVenda() throws Exception {
        var message = new AdicionarItemVendaMessage(
                "123",
                "123",
                2
        );

        kafkaTemplate.send(
                "adicionar-item-venda",
                message
        ).get();

        verify(adicionarItemVendaApplicationService, timeout(5000))
                .executar(
                        message.vendId(),
                        message.produtoId(),
                        message.quantidade()
                );
    }
}