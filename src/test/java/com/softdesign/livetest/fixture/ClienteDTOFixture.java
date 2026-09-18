package com.softdesign.livetest.fixture;

import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.softdesign.livetest.api.cliente.ClienteDTO;
import com.softdesign.livetest.api.cliente.EnderecoDTO;

import static br.com.six2six.fixturefactory.Fixture.of;

public class ClienteDTOFixture implements TemplateLoader {

    public static final String CLIENTE_DTO_MOCK = "CLIENTE_DTO_MOCK";

    @Override
    public void load() {
        of(ClienteDTO.class).addTemplate(CLIENTE_DTO_MOCK, new Rule() {
            {
                add("id", "123");
                add("nome", "João");
                add("idade", 33);
                add("enderecoDTO", one(EnderecoDTO.class, EnderecoDTOFixture.ENDERECO_DTO_MOCK));
            }
        });
    }
}
