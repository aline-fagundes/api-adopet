package br.com.naponline.adopet.api.controller;

import br.com.naponline.adopet.api.exception.ValidacaoException;
import br.com.naponline.adopet.api.model.Abrigo;
import br.com.naponline.adopet.api.service.AbrigoService;
import br.com.naponline.adopet.api.service.PetService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
class AbrigoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AbrigoService abrigoService;

    @MockBean
    private PetService petService;

    @Mock
    private Abrigo abrigo;

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoDeListarAbrigos() throws Exception {
        //Act
        MockHttpServletResponse response = mockMvc.perform(
                get("/abrigos")
        ).andReturn().getResponse();

        //Assert
        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoDeCadastrarAbrigo() throws Exception {
        //Arrange
        String json = """
                {
                    "nome": "Abrigo exemplo",
                    "telefone": "(11)0000-9090",
                    "email": "email@example.com.br"
                }
                """;

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                post("/abrigos")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //Assert
        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo400ParaRequisicaoDeCadastrarAbrigo() throws Exception {
        //Arrange
        String json = """
                {
                    "nome": "Abrigo exemplo",
                    "telefone": "(11)0000-90900",
                    "email": "email@example.com.br"
                }
                """;

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                post("/abrigos")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //Assert
        assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoDeListarPetsDoAbrigoPorNome() throws Exception {
        //Arrange
        String nome = "Abrigo exemplo";

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                get("/abrigos/{nome}/pets", nome)
        ).andReturn().getResponse();

        //Assert
        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoDeListarPetsDoAbrigoPorId() throws Exception {
        //Arrange
        String id = "1";

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                get("/abrigos/{id}/pets", id)
        ).andReturn().getResponse();

        //Assert
        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo404ParaRequisicaoDeListarPetsDoAbrigoPorIdInvalido() throws Exception {
        //Arrange
        String id = "1";

        given(abrigoService.listarPetsDoAbrigo(id)).willThrow(ValidacaoException.class);

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                get("/abrigos/{id}/pets", id)
        ).andReturn().getResponse();

        //Assert
        assertEquals(404, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo404ParaRequisicaoDeListarPetsDoAbrigoPorNomeInvalido() throws Exception {
        //Arrange
        String nome = "Miau";
        given(abrigoService.listarPetsDoAbrigo(nome)).willThrow(ValidacaoException.class);

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                get("/abrigos/{nome}/pets", nome)
        ).andReturn().getResponse();

        //Assert
        assertEquals(404, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoDeCadastrarPetComIdDoAbrigo() throws Exception {
        //Arrange
        String json = """
                {
                    "tipo": "GATO",
                    "nome": "Miau",
                    "raca": "Siames",
                    "idade": "5",
                    "cor" : "Parda",
                    "peso": "6.5"
                }
                """;

        String abrigoId = "1";

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                post("/abrigos/{abrigoId}/pets", abrigoId)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //Assert
        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoDeCadastrarPetComNomeDoAbrigo() throws Exception {
        //Arrange
        String json = """
                {
                    "tipo": "GATO",
                    "nome": "Miau",
                    "raca": "Siames",
                    "idade": "5",
                    "cor" : "Parda",
                    "peso": "6.5"
                }
                """;

        String abrigoNome = "Abrigo exemplo";

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                post("/abrigos/{abrigoNome}/pets", abrigoNome)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //Assert
        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo404ParaRequisicaoDeCadastrarPetAbrigoNaoEncontradoPeloId() throws Exception {
        //Arrange
        String json = """
                {
                    "tipo": "GATO",
                    "nome": "Miau",
                    "raca": "Siames",
                    "idade": "5",
                    "cor" : "Parda",
                    "peso": "6.5"
                }
                """;

        String abrigoId = "1";

        given(abrigoService.carregarAbrigo(abrigoId)).willThrow(ValidacaoException.class);

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                post("/abrigos/{abrigoId}/pets", abrigoId)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //Assert
        assertEquals(404, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo404ParaRequisicaoDeCadastrarPetAbrigoNaoEncontradoPeloNome() throws Exception {
        //Arrange
        String json = """
                {
                    "tipo": "GATO",
                    "nome": "Miau",
                    "raca": "Siames",
                    "idade": "5",
                    "cor" : "Parda",
                    "peso": "6.4"
                }
                """;

        String abrigoNome = "Abrigo inexistente";

        given(abrigoService.carregarAbrigo(abrigoNome)).willThrow(ValidacaoException.class);

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                post("/abrigos/{abrigoNome}/pets", abrigoNome)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //Assert
        assertEquals(404, response.getStatus());
    }

}