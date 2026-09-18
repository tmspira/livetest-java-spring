package com.softdesign.livetest.applicationservice.venda;

import com.softdesign.livetest.api.produto.ProdutoDTO;
import com.softdesign.livetest.domain.cliente.ClienteService;
import com.softdesign.livetest.domain.produto.Produto;
import com.softdesign.livetest.domain.produto.ProdutoNotFound;
import com.softdesign.livetest.domain.produto.ProdutoService;
import com.softdesign.livetest.domain.venda.ItemVenda;
import com.softdesign.livetest.domain.venda.Venda;
import com.softdesign.livetest.domain.venda.VendaService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


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

        var venda = new Venda(null, cliente.id(), BigDecimal.ZERO, Collections.emptyList());

        return vendaService.create(venda);
    }

    public Venda execute(String clientId, List<ProdutoDTO> produtos) {

        produtoValidator(produtos);

        var cliente = clienteService.getById(clientId);

        var items = produtos.stream()
                .map(ProdutoDTO::toItemVenda)
                .toList();

        var valorTotal = items.stream()
                .map(ItemVenda::valorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var venda = new Venda(
                null,
                cliente.id(),
                valorTotal,
                items
        );

        return vendaService.create(venda);
    }

    private void produtoValidator(List<ProdutoDTO> produtos) {

        var produtoIds = produtos.stream()
                .map(ProdutoDTO::id)
                .toList();

        var produtosExistentes = produtoService.findAllByIds(produtoIds);

        var idsExistentes = produtosExistentes.stream()
                .map(Produto::id)
                .collect(Collectors.toSet());

        var produtosInexistentes = produtoIds.stream()
                .filter(id -> !idsExistentes.contains(id))
                .toList();

        if (!produtosInexistentes.isEmpty()) {
            throw new ProdutoNotFound(
                    "Produtos não encontrados: " + produtosInexistentes
            );
        }
    }
}
