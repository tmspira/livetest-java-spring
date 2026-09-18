package com.softdesign.livetest.fixture;

import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.softdesign.livetest.api.produto.ProdutoDTO;

import static br.com.six2six.fixturefactory.Fixture.of;

public class ProdutoDTOFixture implements TemplateLoader {

    public static final String PRODUTO_DTO_MOCK = "PRODUTO_DTO_MOCK";

    @Override
    public void load() {
        of(ProdutoDTO.class).addTemplate(PRODUTO_DTO_MOCK, new Rule() {
            {
                add("id", "123");
                add("nome", "Macarrão");
                add("descricao", "Lamen");
                add("valor", 1.50);
            }
        });
    }
}