package com.softdesign.livetest.api.cliente;

import com.softdesign.livetest.domain.cliente.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
public class ClientesController {

    private final ClienteService clienteService;

    public ClientesController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/clientes")
    public ResponseEntity<List<ClienteDTO>> listAll() {
        var clientes = clienteService.listAll();

        return ResponseEntity.ok(ClienteDTO.from(clientes));
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<ClienteDTO> get(@PathVariable("id") String id) {
        var cliente = clienteService.getById(id);

        return ResponseEntity.ok(ClienteDTO.from(cliente));
    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<ClienteDTO> update(
            @PathVariable("id") String id,
            @RequestBody UpdateClienteRequest request) {

        var cliente = clienteService.update(request.toCliente(id));

        return ResponseEntity.ok(ClienteDTO.from(cliente));
    }

    @PostMapping("/clientes")
    public ResponseEntity<ClienteDTO> create(
            @RequestBody CreateClienteRequest request) {

        var cliente = clienteService.create(request.toCliente());

        var createdUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cliente.id())
                .toUri();

        return ResponseEntity
                .created(createdUri)
                .body(ClienteDTO.from(cliente));
    }
}