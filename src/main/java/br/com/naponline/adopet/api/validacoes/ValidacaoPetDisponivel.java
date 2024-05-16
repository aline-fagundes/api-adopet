package br.com.naponline.adopet.api.validacoes;

import br.com.naponline.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.naponline.adopet.api.exception.ValidacaoException;
import br.com.naponline.adopet.api.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoPetDisponivel implements ValidacaoSolicitacaoAdocao {

    @Autowired
    private PetRepository petRepository;

    public void validar(SolicitacaoAdocaoDto dto) {
        boolean petAdotado = petRepository
                .existsByIdAndAdotadoIsTrue(dto.idPet());

        if (petAdotado) {
            throw new ValidacaoException("Pet já foi adotado!");
        }
    }

}
