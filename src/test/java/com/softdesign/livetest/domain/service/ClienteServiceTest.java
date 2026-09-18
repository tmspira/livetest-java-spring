package com.softdesign.livetest.domain.service;

import br.com.six2six.fixturefactory.Fixture;
import com.softdesign.livetest.BaseTestSuite;
import com.softdesign.livetest.domain.cliente.Cliente;
import com.softdesign.livetest.domain.cliente.ClienteNotFound;
import com.softdesign.livetest.domain.cliente.ClienteService;
import com.softdesign.livetest.fixture.ClienteFixture;
import com.softdesign.livetest.repository.cliente.ClienteMongoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest extends BaseTestSuite {

    @Mock
    private ClienteMongoRepository clienteMongoRepository;

    private ClienteService clienteService;

    @Test
    void deveCriarCliente() {
        clienteService = new ClienteService(clienteMongoRepository);
        Cliente cliente = Fixture.from(Cliente.class).gimme(ClienteFixture.CLIENTE_MOCK);

        when(clienteMongoRepository.insert(cliente))
                .thenReturn(cliente);

        var resultado = clienteService.create(cliente);

        assertEquals(cliente, resultado);

        verify(clienteMongoRepository).insert(cliente);
    }

    @Test
    void deveAtualizarCliente() {
        clienteService = new ClienteService(clienteMongoRepository);
        Cliente cliente = Fixture.from(Cliente.class).gimme(ClienteFixture.CLIENTE_MOCK);

        when(clienteMongoRepository.findById(cliente.id()))
                .thenReturn(Optional.of(cliente));

        when(clienteMongoRepository.save(cliente)).thenReturn(cliente);

        var resultado = clienteService.update(cliente);

        assertEquals(cliente, resultado);

        verify(clienteMongoRepository).findById(cliente.id());
        verify(clienteMongoRepository).save(cliente);
    }

    @Test
    void deveLancarExcecaoAoAtualizarClienteInexistente() {
        clienteService = new ClienteService(clienteMongoRepository);
        Cliente cliente = Fixture.from(Cliente.class).gimme(ClienteFixture.CLIENTE_MOCK);

        when(clienteMongoRepository.findById(cliente.id()))
                .thenReturn(Optional.empty());

        var exception = assertThrows(ClienteNotFound.class,
                () -> clienteService.update(cliente));

        assertEquals("cliente não encontrado - " + cliente.id(), exception.getMessage());

        verify(clienteMongoRepository).findById(cliente.id());
        verify(clienteMongoRepository, never()).save(any());
    }

    @Test
    void deveBuscarClientePorId() {
        clienteService = new ClienteService(clienteMongoRepository);
        Cliente cliente = Fixture.from(Cliente.class).gimme(ClienteFixture.CLIENTE_MOCK);

        when(clienteMongoRepository.findById(cliente.id()))
                .thenReturn(Optional.of(cliente));

        var resultado = clienteService.getById(cliente.id());

        assertEquals(cliente, resultado);

        verify(clienteMongoRepository).findById(cliente.id());
    }

    @Test
    void deveLancarExcecaoAoBuscarClienteInexistente() {
        clienteService = new ClienteService(clienteMongoRepository);
        Cliente cliente = Fixture.from(Cliente.class).gimme(ClienteFixture.CLIENTE_MOCK);

        when(clienteMongoRepository.findById(cliente.id()))
                .thenReturn(Optional.empty());

        var exception = assertThrows(ClienteNotFound.class,
                () -> clienteService.getById(cliente.id()));

        assertEquals("cliente não encontrado - " + cliente.id(), exception.getMessage());

        verify(clienteMongoRepository).findById(cliente.id());
    }

    @Test
    void deveListarTodosOsClientes() {
        clienteService = new ClienteService(clienteMongoRepository);
        Cliente cliente = Fixture.from(Cliente.class).gimme(ClienteFixture.CLIENTE_MOCK);

        when(clienteMongoRepository.findAll())
                .thenReturn(List.of(cliente));

        var resultado = clienteService.listAll();

        assertEquals(1, resultado.size());
        assertEquals(cliente, resultado.getFirst());

        verify(clienteMongoRepository).findAll();
    }
}
