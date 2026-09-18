package com.softdesign.livetest.fixture;

import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.softdesign.livetest.domain.produto.Produto;

import java.math.BigDecimal;

import static br.com.six2six.fixturefactory.Fixture.of;

public class ProdutoFixture implements TemplateLoader {
    public static final String PRODUTO_MOCK = "PRODUTO_MOCK";

    @Override
    public void load() {
        of(Produto.class).addTemplate(PRODUTO_MOCK, new Rule() {
            {
                add("id", "123");
                add("nome", "Macarrão");
                add("descricao", "Lamen");
                add("valor", new BigDecimal("1.50"));
            }
        });
    }
}
