package com.softdesign.livetest.fixture;

import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.softdesign.livetest.domain.cliente.Endereco;

import static br.com.six2six.fixturefactory.Fixture.of;

public class EnderecoFixture implements TemplateLoader {

    public static final String ENDERECO_MOCK = "ENDERECO_MOCK";

    @Override
    public void load() {
        of(Endereco.class).addTemplate(ENDERECO_MOCK, new Rule() {
            {
                add("logradouro", "Rua da esperança");
                add("numero", "33");
                add("cep", "123");
            }
        });
    }
}


