package com.softdesign.livetest.api.venda;

import com.softdesign.livetest.applicationservice.venda.CreateVendaApplicationService;
import com.softdesign.livetest.domain.venda.VendaService;
import com.softdesign.livetest.domain.venda.adiciolnaritemvenda.AdicionarItemVendaMessage;
import com.softdesign.livetest.domain.venda.adiciolnaritemvenda.AdicionarItemVendaProducer;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
public class VendasController {

    private final VendaService vendaService;
    private final CreateVendaApplicationService createVendaApplicationService;
    private final AdicionarItemVendaProducer adicionarItemVendaProducer;

    public VendasController(
            VendaService vendaService,
            CreateVendaApplicationService createVendaApplicationService,
            AdicionarItemVendaProducer adicionarItemVendaProducer) {
        this.vendaService = vendaService;
        this.createVendaApplicationService = createVendaApplicationService;
        this.adicionarItemVendaProducer = adicionarItemVendaProducer;
    }

    @GetMapping("/vendas")
    public ResponseEntity<List<VendaDTO>> listAll() {
        var vendas = vendaService.listAll();
        return ResponseEntity.accepted().body(VendaDTO.from(vendas));
    }

    @GetMapping("/vendas/{id}")
    public ResponseEntity<VendaDTO> get(@PathVariable("id") String id) {
        var venda = vendaService.getById(id);
        return ResponseEntity.ok(VendaDTO.from(venda));
    }

    @PostMapping("/vendas")
    public ResponseEntity<VendaDTO> createa(
            @RequestBody CreateVendaRequest createVendaRequest) {

        var venda = createVendaApplicationService.execute(
                createVendaRequest.clienteId());

        var createdUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(venda.id())
                .toUri();

        return ResponseEntity.created(createdUri)
                .body(VendaDTO.from(venda));
    }

    @PostMapping("/vendas-produtos")
    public ResponseEntity<VendaDTO> createComProdutos(
            @RequestBody @Valid CreateVendaComProdutosRequest createVendaRequest) {

        var venda = createVendaApplicationService.execute(
                createVendaRequest.clienteId(),
                createVendaRequest.produtos());

        var createdUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(venda.id())
                .toUri();

        return ResponseEntity.created(createdUri)
                .body(VendaDTO.from(venda));
    }

    @PostMapping("/vendas/{id}/item")
    public ResponseEntity<Void> addItemVenda(
            @PathVariable("id") String id,
            @RequestBody AdicionarItemVendaRequest request) {

        adicionarItemVendaProducer.send(
                new AdicionarItemVendaMessage(
                        id,
                        request.produtoId(),
                        request.quantidade()));

        return ResponseEntity.accepted().build();
    }
}