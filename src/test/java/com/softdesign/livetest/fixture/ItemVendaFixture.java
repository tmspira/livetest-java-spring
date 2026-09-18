package com.softdesign.livetest.fixture;

import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.softdesign.livetest.domain.venda.ItemVenda;

import java.math.BigDecimal;

import static br.com.six2six.fixturefactory.Fixture.of;

public class ItemVendaFixture implements TemplateLoader {

    public static final String ITEM_VENDA_MOCK = "ITEM_VENDA_MOCK";

    @Override
    public void load() {
        of(ItemVenda.class).addTemplate(ITEM_VENDA_MOCK, new Rule() {
            {
                add("produtoId", "123");
                add("quantidade", 2);
                add("valorTotal", new BigDecimal("3.00"));
            }
        });
    }
}
