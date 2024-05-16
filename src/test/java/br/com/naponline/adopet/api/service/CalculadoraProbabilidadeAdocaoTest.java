package br.com.naponline.adopet.api.service;

import br.com.naponline.adopet.api.dto.CadastroAbrigoDto;
import br.com.naponline.adopet.api.dto.CadastroPetDto;
import br.com.naponline.adopet.api.model.Abrigo;
import br.com.naponline.adopet.api.model.Pet;
import br.com.naponline.adopet.api.model.ProbabilidadeAdocao;
import br.com.naponline.adopet.api.model.TipoPet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CalculadoraProbabilidadeAdocaoTest {

    @Test
    void deveriaRetornarProbabilidadeAltaParaPetComIdadeBaixaEPesoBaixo() {
        //4 anos e 4kg - ALTA

        //Arrange
        Abrigo abrigo = new Abrigo(new CadastroAbrigoDto(
                "Abrigo exemplo",
                "(11)0000-9090",
                "email@example.com.br"
        ));

        Pet pet = new Pet(new CadastroPetDto(
                TipoPet.GATO,
                "Miau",
                "Siames",
                4,
                "Cinza",
                4.0f
        ), abrigo);

        //Act
        CalculadoraProbabilidadeAdocao calculadora = new CalculadoraProbabilidadeAdocao();
        ProbabilidadeAdocao probabilidade = calculadora.calcular(pet);

        //Assert
        Assertions.assertEquals(ProbabilidadeAdocao.ALTA, probabilidade);
    }

    @Test
    void deveriaRetornarProbabilidadeMediaParaPetComIdadeAltaEPesoBaixo() {
        //15 anos e 4kg - MÉDIA

        //Arrange
        Abrigo abrigo = new Abrigo(new CadastroAbrigoDto(
                "Abrigo feliz",
                "(11)0000-9090",
                "email@example.com.br"
        ));

        Pet pet = new Pet(new CadastroPetDto(
                TipoPet.GATO,
                "Miau",
                "Siames",
                15,
                "Cinza",
                4.0f
        ), abrigo);

        //Act
        CalculadoraProbabilidadeAdocao calculadora = new CalculadoraProbabilidadeAdocao();
        ProbabilidadeAdocao probabilidade = calculadora.calcular(pet);

        //Assert
        Assertions.assertEquals(ProbabilidadeAdocao.MEDIA, probabilidade);
    }

}