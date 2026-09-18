package com.softdesign.livetest.api.produto;

import com.softdesign.livetest.domain.produto.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.List;

@RestController
public class ProdutosController {

    private final ProdutoService produtoService;

    public ProdutosController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/produtos")
    public ResponseEntity<List<ProdutoDTO>> listAll() {
        var produtos = produtoService.listAll();

        return ResponseEntity.ok(ProdutoDTO.from(produtos));
    }

    @GetMapping("/produtos/{id}")
    public ResponseEntity<ProdutoDTO> get(@PathVariable("id") String id) {
        var produto = produtoService.getById(id);

        return ResponseEntity.ok(ProdutoDTO.from(produto));
    }

    @PutMapping("/produtos/{id}")
    public ResponseEntity<ProdutoDTO> update(
            @PathVariable("id") String id,
            @RequestBody UpdateProdutoRequest request) {

        var produto = produtoService.update(request.toProduto(id));

        return ResponseEntity.ok(ProdutoDTO.from(produto));
    }

    @PostMapping("/produtos")
    public ResponseEntity<ProdutoDTO> create(
            @RequestBody CreateProdutoRequest request) {

        var produto = produtoService.create(request.toProduto());

        var createdUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(produto.id())
                .toUri();

        return ResponseEntity
                .created(createdUri)
                .body(ProdutoDTO.from(produto));
    }
}
