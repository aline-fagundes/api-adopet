package br.com.naponline.adopet.api.repository;

import br.com.naponline.adopet.api.model.Adocao;
import br.com.naponline.adopet.api.model.StatusAdocao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdocaoRepository extends JpaRepository<Adocao, Long> {

    boolean existsByPetIdAndStatus(Long idPet, StatusAdocao status);

    long countByTutorIdAndStatus(Long idTutor, StatusAdocao status);

    boolean existsByTutorIdAndStatus(Long idTutor, StatusAdocao status);

}
