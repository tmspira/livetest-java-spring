package com.softdesign.livetest.fixture;

import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.softdesign.livetest.api.venda.ItemVendaDTO;
import com.softdesign.livetest.api.venda.VendaDTO;

import java.math.BigDecimal;
import java.util.List;

import static br.com.six2six.fixturefactory.Fixture.of;

public class VendaDTOFixture implements TemplateLoader {

    public static final String VENDA_DTO_MOCK = "VENDA_DTO_MOCK";
    public static final String VENDA_DTO_COM_DOIS_ITENS_MOCK = "VENDA_DTO_COM_DOIS_ITENS_MOCK";

    @Override
    public void load() {
        of(VendaDTO.class).addTemplate(VENDA_DTO_MOCK, new Rule() {
            {
                add("id", "123");
                add("clienteId", "123");
                add("valorTotal", new BigDecimal("3.00"));
                add("items", List.of(
                        one(ItemVendaDTO.class, ItemVendaDTOFixture.ITEM_VENDA_DTO_MOCK)
                ));
            }
        });

        of(VendaDTO.class).addTemplate(VENDA_DTO_COM_DOIS_ITENS_MOCK, new Rule() {
            {
                add("id", "123");
                add("clienteId", "123");
                add("valorTotal", new BigDecimal("6.00"));
                add("items", List.of(
                        one(ItemVendaDTO.class, ItemVendaDTOFixture.ITEM_VENDA_DTO_MOCK),
                        one(ItemVendaDTO.class, ItemVendaDTOFixture.ITEM_VENDA_DTO_MOCK)
                ));
            }
        });
    }
}
