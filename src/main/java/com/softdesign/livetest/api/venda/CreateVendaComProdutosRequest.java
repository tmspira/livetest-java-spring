package com.softdesign.livetest.api.venda;

import com.softdesign.livetest.api.produto.ProdutoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateVendaComProdutosRequest(

        @NotBlank
        String clienteId,

        @NotEmpty(message = "A lista de produtos não pode ser vazia")
        List<@Valid ProdutoDTO> produtos

) {}
