package com.softdesign.livetest.applicationservice.venda;

import com.softdesign.livetest.api.produto.ProdutoDTO;
import com.softdesign.livetest.domain.cliente.ClienteService;
import com.softdesign.livetest.domain.produto.ProdutoNotFound;
import com.softdesign.livetest.domain.produto.ProdutoService;
import com.softdesign.livetest.domain.venda.ItemVenda;
import com.softdesign.livetest.domain.venda.Venda;
import com.softdesign.livetest.domain.venda.VendaService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class CreateVendaApplicationService {

    private final VendaService vendaService;

    private final ClienteService clienteService;

    private final ProdutoService produtoService;

    public CreateVendaApplicationService(VendaService vendaService, ClienteService clienteService, ProdutoService produtoService) {
        this.vendaService = vendaService;
        this.clienteService = clienteService;
        this.produtoService = produtoService;
    }

    public Venda execute(String clientId) {

        var cliente = clienteService.getById(clientId);

        var venda = new Venda(null, cliente.id(), 0.0d, Collections.emptyList());

        return vendaService.create(venda);
    }

    public Venda execute(String clientId, List<ProdutoDTO> produtos) {

        produtoValidator(produtos);

        var cliente = clienteService.getById(clientId);

        var items = produtos.stream().map(ProdutoDTO::toItemVenda).toList();
        var valorTotal = items.stream().mapToDouble(ItemVenda::valorTotal).sum();
        var venda = new Venda(null, cliente.id(), valorTotal, items);

        return vendaService.create(venda);
    }

    public void produtoValidator(List<ProdutoDTO> produtos) {
        var items = produtos.stream()
                .map(produto -> {
                    if (produto.id() == null || produto.id().isBlank()) {
                        throw new ProdutoNotFound("ID do produto não informado");
                    }

                    produtoService.getById(produto.id());

                    return produto.toItemVenda();
                })
                .toList();
    }
}
