package br.com.naponline.adopet.api.service;

import br.com.naponline.adopet.api.model.Abrigo;
import br.com.naponline.adopet.api.model.Pet;
import br.com.naponline.adopet.api.dto.CadastroPetDto;
import br.com.naponline.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @InjectMocks
    private PetService petService;

    @Mock
    private PetRepository petRepository;

    @Mock
    private CadastroPetDto cadastroPetDto;

    @Mock
    private Abrigo abrigo;

    @Test
    void deveriaRetornarTodosOsPetsDisponiveis() {
        //Act
        petService.buscarPetsDisponiveis();

        //Assert
        then(petRepository).should().findAllByAdotadoFalse();
    }

    @Test
    void deveriaCadastrarPet() {
        //Act
        petService.cadastrarPet(abrigo, cadastroPetDto);

        //Assert
        then(petRepository).should().save(new Pet(cadastroPetDto, abrigo));
    }

}