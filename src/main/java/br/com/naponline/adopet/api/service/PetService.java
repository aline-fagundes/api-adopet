package br.com.naponline.adopet.api.service;

import br.com.naponline.adopet.api.dto.CadastroPetDto;
import br.com.naponline.adopet.api.dto.PetDto;
import br.com.naponline.adopet.api.model.Abrigo;
import br.com.naponline.adopet.api.model.Pet;
import br.com.naponline.adopet.api.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    public List<PetDto> buscarPetsDisponiveis() {
        return petRepository
                .findAllByAdotadoFalse()
                .stream()
                .map(PetDto::new)
                .toList();
    }

    public void cadastrarPet(Abrigo abrigo, CadastroPetDto dto) {
        petRepository.save(new Pet(dto, abrigo));
    }

}
