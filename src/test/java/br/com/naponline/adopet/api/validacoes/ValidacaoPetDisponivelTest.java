package br.com.naponline.adopet.api.validacoes;

import br.com.naponline.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.naponline.adopet.api.exception.ValidacaoException;
import br.com.naponline.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ValidacaoPetDisponivelTest {

    @InjectMocks
    private ValidacaoPetDisponivel validador;

    @Mock
    private PetRepository petRepository;

    @Mock
    private SolicitacaoAdocaoDto dto;

    @Test
    void deveriaPermitirSolicitacaoDeAdocaoPet() {
        //Arrange
        given(petRepository.existsByIdAndAdotadoIsTrue(dto.idPet()))
                .willReturn(false);

        //Assert + Act
        assertDoesNotThrow(() -> validador.validar(dto));
    }

    @Test
    void naoDeveriaPermitirSolicitacaoDeAdocaoPet() {
        //Arrange
        given(petRepository.existsByIdAndAdotadoIsTrue(dto.idPet()))
                .willReturn(true);

        //Assert + Act
        assertThrows(ValidacaoException.class, () -> validador.validar(dto));
    }

}