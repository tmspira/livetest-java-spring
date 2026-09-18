package com.softdesign.livetest.api.venda;

import com.softdesign.livetest.api.produto.ProdutoDTO;
import java.util.List;

public record CreateVendaComProdutosRequest(
        String clienteId,
        List<ProdutoDTO> produtos
) {}
