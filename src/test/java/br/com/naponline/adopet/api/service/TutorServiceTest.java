package br.com.naponline.adopet.api.service;

import br.com.naponline.adopet.api.dto.AtualizacaoTutorDto;
import br.com.naponline.adopet.api.dto.CadastroTutorDto;
import br.com.naponline.adopet.api.exception.ValidacaoException;
import br.com.naponline.adopet.api.model.Tutor;
import br.com.naponline.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @InjectMocks
    private TutorService tutorService;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private Tutor tutor;

    @Mock
    private CadastroTutorDto cadastroTutorDto;

    @Mock
    private AtualizacaoTutorDto atualizacaoTutorDto;

    @Test
    void NaoDeveriaCadastrarTutorComTelefoneOuEmailJaCadastrado() {
        //Arrange
        given(tutorRepository.existsByTelefoneOrEmail(cadastroTutorDto.telefone(), cadastroTutorDto.email())).willReturn(true);

        //Act + Assert
        assertThrows(ValidacaoException.class, () -> tutorService.cadastrar(cadastroTutorDto));
    }

    @Test
    void deveriaCadastrarTutor() {
        //Arrange
        given(tutorRepository.existsByTelefoneOrEmail(cadastroTutorDto.telefone(), cadastroTutorDto.email())).willReturn(false);

        //Act + Assert
        assertDoesNotThrow(() -> tutorService.cadastrar(cadastroTutorDto));
        then(tutorRepository).should().save(new Tutor(cadastroTutorDto));
    }

    @Test
    void deveriaAtualizarDadosTutor() {
        //Arrange
        given(tutorRepository.getReferenceById(atualizacaoTutorDto.id())).willReturn(tutor);

        //Act
        tutorService.atualizar(atualizacaoTutorDto);

        //Assert
        then(tutor).should().atualizarDados(atualizacaoTutorDto);
    }

}