package br.com.naponline.adopet.api.service;

import br.com.naponline.adopet.api.dto.CadastroAbrigoDto;
import br.com.naponline.adopet.api.exception.ValidacaoException;
import br.com.naponline.adopet.api.model.Abrigo;
import br.com.naponline.adopet.api.repository.AbrigoRepository;
import br.com.naponline.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AbrigoServiceTest {

    @InjectMocks
    private AbrigoService abrigoService;

    @Mock
    private AbrigoRepository abrigoRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private Abrigo abrigo;

    @Captor
    private ArgumentCaptor<Abrigo> abrigoCaptor;

    @Test
    void deveriaChamarListaDeTodosOsAbrigos() {
        //Act
        abrigoService.listar();

        //Assert
        then(abrigoRepository).should().findAll();
    }

    @Test
    void deveriaCadastrarNovoAbrigo() {
        // Arrange
        CadastroAbrigoDto dto = new CadastroAbrigoDto("Abrigo exemplo", "(11)0000-9090", "email@example.com.br");

        given(abrigoRepository.existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email())).willReturn(false);

        // Act
        abrigoService.cadastrar(dto);

        // Assert
        then(abrigoRepository).should().save(abrigoCaptor.capture());
        Abrigo abrigoSalvo = abrigoCaptor.getValue();
        assertEquals(dto.nome(), abrigoSalvo.getNome());
        assertEquals(dto.telefone(), abrigoSalvo.getTelefone());
        assertEquals(dto.email(), abrigoSalvo.getEmail());
    }

    @Test
    void deveriaLancarValidacaoExceptionAoCadastrarAbrigoExistente() {
        // Arrange
        CadastroAbrigoDto dto = new CadastroAbrigoDto("Abrigo exemplo", "(11)0000-9090", "email@example.com.br");
        given(abrigoRepository.existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email())).willReturn(true);

        // Act & Assert
        assertThrows(ValidacaoException.class, () -> abrigoService.cadastrar(dto));
    }

    @Test
    void deveriaChamarListaDePetsDoAbrigoAtravesDoNome() {
        //Arrange
        String nome = "Abrigo exemplo";
        given(abrigoRepository.findByNome(nome)).willReturn(Optional.of(abrigo));

        //Act
        abrigoService.listarPetsDoAbrigo(nome);

        //Assert
        then(petRepository).should().findByAbrigo(abrigo);
    }

}