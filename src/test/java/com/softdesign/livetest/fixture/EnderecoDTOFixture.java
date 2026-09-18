package com.softdesign.livetest.fixture;

import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.softdesign.livetest.api.cliente.EnderecoDTO;

import static br.com.six2six.fixturefactory.Fixture.of;

public class EnderecoDTOFixture implements TemplateLoader {

    public static final String ENDERECO_DTO_MOCK = "ENDERECO_DTO_MOCK";

    @Override
    public void load() {
        of(EnderecoDTO.class).addTemplate(ENDERECO_DTO_MOCK, new Rule() {
            {
                add("logradouro", "Rua da esperança");
                add("numero", "33");
                add("cep", "123");
            }
        });
    }
}
