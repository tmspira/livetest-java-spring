package com.softdesign.livetest.fixture;

import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.softdesign.livetest.domain.cliente.Cliente;
import com.softdesign.livetest.domain.cliente.Endereco;

import static br.com.six2six.fixturefactory.Fixture.of;

public class ClienteFixture implements TemplateLoader {

    public static final String CLIENTE_MOCK = "CLIENTE_MOCK";

    @Override
    public void load() {
        of(Cliente.class).addTemplate(CLIENTE_MOCK, new Rule() {
            {
                add("id", "123");
                add("nome", "João");
                add("idade", 33);
                add("endereco", one(Endereco.class, EnderecoFixture.ENDERECO_MOCK));
            }
        });
    }
}
