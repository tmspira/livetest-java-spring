package com.softdesign.livetest.repository.venda;

import com.softdesign.livetest.domain.produto.Produto;
import com.softdesign.livetest.domain.venda.Venda;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VendaMongoRepository extends MongoRepository<Venda, String> {
}
