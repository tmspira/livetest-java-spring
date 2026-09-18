package com.softdesign.livetest.fixture;

import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.softdesign.livetest.domain.venda.ItemVenda;
import com.softdesign.livetest.domain.venda.Venda;

import java.math.BigDecimal;
import java.util.List;

import static br.com.six2six.fixturefactory.Fixture.of;

public class VendaFixture implements TemplateLoader {

    public static final String VENDA_MOCK = "VENDA_MOCK";
    public static final String VENDA_COM_DOIS_ITENS_MOCK = "VENDA_COM_DOIS_ITENS_MOCK";

    @Override
    public void load() {
        of(Venda.class).addTemplate(VENDA_MOCK, new Rule() {
            {
                add("id", "123");
                add("clienteId", "123");
                add("valorTotal", new BigDecimal("3.00"));
                add("items", List.of(
                        one(ItemVenda.class, ItemVendaFixture.ITEM_VENDA_MOCK)
                ));
            }
        });

        of(Venda.class).addTemplate(VENDA_COM_DOIS_ITENS_MOCK, new Rule() {
            {
                add("id", "123");
                add("clienteId", "123");
                add("valorTotal", new BigDecimal("6.00"));
                add("items", List.of(
                        one(ItemVenda.class, ItemVendaFixture.ITEM_VENDA_MOCK),
                        one(ItemVenda.class, ItemVendaFixture.ITEM_VENDA_MOCK)
                ));
            }
        });
    }
}

