package com.softdesign.livetest.applicationservice.venda;

import com.softdesign.livetest.domain.cliente.ClienteService;
import com.softdesign.livetest.domain.venda.Venda;
import com.softdesign.livetest.domain.venda.VendaService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CreateVendaApplicationService {

    private final VendaService vendaService;

    private final ClienteService clienteService;

    public CreateVendaApplicationService(VendaService vendaService, ClienteService clienteService) {
        this.vendaService = vendaService;
        this.clienteService = clienteService;
    }

    public Venda execute(String clientId) {

        var cliente = clienteService.getById(clientId);

        var venda = new Venda(null, cliente.id(), 0.0d, Collections.emptyList());

        return vendaService.create(venda);
    }

}
