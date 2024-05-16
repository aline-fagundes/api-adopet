package br.com.naponline.adopet.api.validacoes;

import br.com.naponline.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.naponline.adopet.api.exception.ValidacaoException;
import br.com.naponline.adopet.api.model.StatusAdocao;
import br.com.naponline.adopet.api.repository.AdocaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ValidacaoTutorComLimiteDeAdocoesTest {

    @InjectMocks
    private ValidacaoTutorComLimiteDeAdocoes validador;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private SolicitacaoAdocaoDto dto;


    @Test
    void naoDeveriaPermitirSolicitacaoDeAdocaoTutorAtingiuLimiteDe5Adocoes() {
        //Arrange
        given(adocaoRepository.countByTutorIdAndStatus(dto.idTutor(), StatusAdocao.APROVADO))
                .willReturn(5L);

        //Act + Assert
        assertThrows(ValidacaoException.class, () -> validador.validar(dto));
    }

    @Test
    void deveriaPermitirSolicitacaoDeAdocaoTutorNaoAtingiuLimiteDe5Adocoes() {
        //Arrange
        given(adocaoRepository.countByTutorIdAndStatus(dto.idTutor(), StatusAdocao.APROVADO))
                .willReturn(4L);

        //Act + Assert
        assertDoesNotThrow(() -> validador.validar(dto));
    }

}