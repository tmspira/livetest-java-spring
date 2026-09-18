package com.softdesign.livetest.applicationservice.venda;

import com.softdesign.livetest.api.produto.ProdutoDTO;
import com.softdesign.livetest.domain.cliente.ClienteService;
import com.softdesign.livetest.domain.produto.Produto;
import com.softdesign.livetest.domain.produto.ProdutoNotFound;
import com.softdesign.livetest.domain.produto.ProdutoService;
import com.softdesign.livetest.domain.venda.Venda;
import com.softdesign.livetest.domain.venda.VendaService;
import com.softdesign.livetest.mocks.ClienteMock;
import com.softdesign.livetest.mocks.ProdutoMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateVendaApplicationServiceTest {

    @Mock
    private VendaService vendaService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private CreateVendaApplicationService createVendaApplicationService;

    @Test
    void deveCriarVendaSemProdutos() {
        var cliente = ClienteMock.criar();

        when(clienteService.getById(cliente.id()))
                .thenReturn(cliente);

        var venda = new Venda(
                "venda-1",
                cliente.id(),
                BigDecimal.ZERO,
                List.of()
        );

        when(vendaService.create(org.mockito.ArgumentMatchers.any(Venda.class)))
                .thenReturn(venda);

        var result = createVendaApplicationService.execute(cliente.id());

        assertEquals(venda, result);

        verify(clienteService)
                .getById(cliente.id());

        verify(vendaService)
                .create(org.mockito.ArgumentMatchers.any(Venda.class));
    }

    @Test
    void deveCriarVendaComProdutos() {
        var cliente = ClienteMock.criar();
        var produto1 = ProdutoMock.criar();
        var produto2 = ProdutoMock.criar();

        var produtoDTO1 = ProdutoDTO.from(produto1);
        var produtoDTO2 = ProdutoDTO.from(produto2);

        var produtos = List.of(
                produtoDTO1,
                produtoDTO2
        );

        when(produtoService.findAllByIds(
                List.of(produtoDTO1.id(), produtoDTO2.id())
        )).thenReturn(List.of(produto1, produto2));

        when(clienteService.getById(cliente.id()))
                .thenReturn(cliente);

        var venda = new Venda(
                "venda-1",
                cliente.id(),
                produto1.valor().add(produto2.valor()),
                List.of(
                        produtoDTO1.toItemVenda(),
                        produtoDTO2.toItemVenda()
                )
        );

        when(vendaService.create(org.mockito.ArgumentMatchers.any(Venda.class)))
                .thenReturn(venda);

        var result = createVendaApplicationService.execute(
                cliente.id(),
                produtos
        );

        assertEquals(venda, result);

        verify(produtoService)
                .findAllByIds(List.of(
                        produtoDTO1.id(),
                        produtoDTO2.id()
                ));

        verify(clienteService)
                .getById(cliente.id());

        verify(vendaService)
                .create(org.mockito.ArgumentMatchers.any(Venda.class));
    }

    @Test
    void deveCalcularValorTotalDaVenda() {
        var cliente = ClienteMock.criar();
        var produto1 = ProdutoMock.criar();
        var produto2 = ProdutoMock.criar();

        var produtoDTO1 = ProdutoDTO.from(produto1);
        var produtoDTO2 = ProdutoDTO.from(produto2);

        var produtos = List.of(
                produtoDTO1,
                produtoDTO2
        );

        when(produtoService.findAllByIds(
                List.of(produtoDTO1.id(), produtoDTO2.id())
        )).thenReturn(List.of(produto1, produto2));

        when(clienteService.getById(cliente.id()))
                .thenReturn(cliente);

        createVendaApplicationService.execute(
                cliente.id(),
                produtos
        );

        var captor = ArgumentCaptor.forClass(Venda.class);

        verify(vendaService).create(captor.capture());

        var vendaCriada = captor.getValue();

        var valorEsperado = produtoDTO1.valor()
                .add(produtoDTO2.valor());

        assertEquals(valorEsperado, vendaCriada.valorTotal());
        assertEquals(2, vendaCriada.items().size());
    }

    @Test
    void deveLancarProdutoNotFoundQuandoProdutoNaoExistir() {
        var cliente = ClienteMock.criar();
        var produto = ProdutoMock.criar();

        var produtoDTO = ProdutoDTO.from(produto);

        when(produtoService.findAllByIds(
                List.of(produtoDTO.id())
        )).thenReturn(List.of());

        assertThrows(
                ProdutoNotFound.class,
                () -> createVendaApplicationService.execute(
                        cliente.id(),
                        List.of(produtoDTO)
                )
        );

        verify(produtoService)
                .findAllByIds(List.of(produtoDTO.id()));

        verify(vendaService, org.mockito.Mockito.never())
                .create(org.mockito.ArgumentMatchers.any(Venda.class));
    }

    @Test
    void deveLancarProdutoNotFoundQuandoParteDosProdutosNaoExistir() {
        var cliente = ClienteMock.criar();

        var produto1Original = ProdutoMock.criar();
        var produto2Original = ProdutoMock.criar();

        var produto1 = new Produto(
                "produto-1",
                produto1Original.nome(),
                produto1Original.descricao(),
                produto1Original.valor()
        );

        var produto2 = new Produto(
                "produto-2",
                produto2Original.nome(),
                produto2Original.descricao(),
                produto2Original.valor()
        );

        var produtoDTO1 = ProdutoDTO.from(produto1);
        var produtoDTO2 = ProdutoDTO.from(produto2);

        when(produtoService.findAllByIds(
                List.of(produtoDTO1.id(), produtoDTO2.id())
        )).thenReturn(List.of(produto1));

        var exception = assertThrows(
                ProdutoNotFound.class,
                () -> createVendaApplicationService.execute(
                        cliente.id(),
                        List.of(produtoDTO1, produtoDTO2)
                )
        );

        assertEquals(
                "Produtos não encontrados: [" + produtoDTO2.id() + "]",
                exception.getMessage()
        );

        verify(vendaService, org.mockito.Mockito.never())
                .create(org.mockito.ArgumentMatchers.any(Venda.class));
    }

    @Test
    void deveBuscarClienteAntesDeCriarVenda() {
        var cliente = ClienteMock.criar();

        when(clienteService.getById(cliente.id()))
                .thenReturn(cliente);

        var venda = new Venda(
                "venda-1",
                cliente.id(),
                BigDecimal.ZERO,
                List.of()
        );

        when(vendaService.create(org.mockito.ArgumentMatchers.any(Venda.class)))
                .thenReturn(venda);

        createVendaApplicationService.execute(cliente.id());

        verify(clienteService)
                .getById(cliente.id());

        verify(vendaService)
                .create(org.mockito.ArgumentMatchers.any(Venda.class));
    }
}