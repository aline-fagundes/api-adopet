package br.com.naponline.adopet.api.service;

import br.com.naponline.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.naponline.adopet.api.dto.ReprovacaoAdocaoDto;
import br.com.naponline.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.naponline.adopet.api.model.Abrigo;
import br.com.naponline.adopet.api.model.Adocao;
import br.com.naponline.adopet.api.model.Pet;
import br.com.naponline.adopet.api.model.StatusAdocao;
import br.com.naponline.adopet.api.model.Tutor;
import br.com.naponline.adopet.api.repository.AdocaoRepository;
import br.com.naponline.adopet.api.repository.PetRepository;
import br.com.naponline.adopet.api.repository.TutorRepository;
import br.com.naponline.adopet.api.validacoes.ValidacaoPetDisponivel;
import br.com.naponline.adopet.api.validacoes.ValidacaoSolicitacaoAdocao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AdocaoServiceTest {

    @InjectMocks
    private AdocaoService adocaoService;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private EmailService emailService;

    @Spy
    private List<ValidacaoSolicitacaoAdocao> validacoes = new ArrayList<>();

    @Mock
    private ValidacaoSolicitacaoAdocao validador1;

    @Mock
    private ValidacaoPetDisponivel validador2;

    @Spy
    private Adocao adocao;

    @Mock
    private Pet pet;

    @Mock
    private Tutor tutor;

    @Mock
    private Abrigo abrigo;

    @Mock
    private AprovacaoAdocaoDto aprovacaoAdocaoDto;

    @Mock
    private ReprovacaoAdocaoDto reprovacaoAdocaoDto;

    @Captor
    private ArgumentCaptor<Adocao> adocaoCaptor;

    @Test
    void deveriaSalvarAdocaoAoSolicitar() {
        //Arrange
        SolicitacaoAdocaoDto dto = new SolicitacaoAdocaoDto(10l, 20l, "Motivo qualquer");
        given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        //Act
        adocaoService.solicitar(dto);

        //Assert
        then(adocaoRepository).should().save(adocaoCaptor.capture());
        Adocao adocaoSalva = adocaoCaptor.getValue();
        assertEquals(pet, adocaoSalva.getPet());
        assertEquals(tutor, adocaoSalva.getTutor());
        assertEquals(dto.motivo(), adocaoSalva.getMotivo());
    }

    @Test
    void deveriaChamarValidadoresDeAdocaoAoSolicitar() {
        //Arrange
        SolicitacaoAdocaoDto dto = new SolicitacaoAdocaoDto(10l, 20l, "motivo qualquer");
        given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);
        validacoes.add(validador1);
        validacoes.add(validador2);

        //Act
        adocaoService.solicitar(dto);

        //Assert
        then(validador1).should().validar(dto);
        then(validador2).should().validar(dto);
    }

    @Test
    void deveriaEnviarEmailAoSolicitarAdocao() {
        //Arrange
        SolicitacaoAdocaoDto dto = new SolicitacaoAdocaoDto(10l, 30l, "motivo teste");
        given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        //Act
        adocaoService.solicitar(dto);

        //Assert
        then(adocaoRepository).should().save(adocaoCaptor.capture());
        Adocao adocao = adocaoCaptor.getValue();
        then(emailService).should().enviarEmail(
                adocao.getPet().getAbrigo().getEmail(),
                "Solicitação de adoção",
                "Olá " + adocao.getPet().getAbrigo().getNome() + "!\n\nUma solicitação de adoção foi registrada hoje para o pet: " + adocao.getPet().getNome() + ". \nFavor avaliar para aprovação ou reprovação."
        );
    }

    @Test
    void deveriaAprovarUmaAdocao() {
        //Arrange
        given(adocaoRepository.getReferenceById(aprovacaoAdocaoDto.idAdocao())).willReturn(adocao);
        given(adocao.getPet()).willReturn(pet);
        given(pet.getAbrigo()).willReturn(abrigo);
        given(abrigo.getEmail()).willReturn("email@example.com");
        given(adocao.getTutor()).willReturn(tutor);
        given(tutor.getNome()).willReturn("Aline");
        given(adocao.getData()).willReturn(LocalDateTime.now());

        //Act
        adocaoService.aprovar(aprovacaoAdocaoDto);

        //Assert
        then(adocao).should().marcarComoAprovada();
        Assertions.assertEquals(StatusAdocao.APROVADO, adocao.getStatus());
    }

    @Test
    void deveriaEnviarEmailAoAprovarUmaAdocao() {
        //Arrange
        given(adocaoRepository.getReferenceById(aprovacaoAdocaoDto.idAdocao())).willReturn(adocao);
        given(adocao.getPet()).willReturn(pet);
        given(pet.getAbrigo()).willReturn(abrigo);
        given(abrigo.getEmail()).willReturn("email@example.com");
        given(adocao.getTutor()).willReturn(tutor);
        given(tutor.getNome()).willReturn("Aline");
        given(adocao.getData()).willReturn(LocalDateTime.now());

        //Act
        adocaoService.aprovar(aprovacaoAdocaoDto);

        //Assert
        then(emailService).should().enviarEmail(
                adocao.getPet().getAbrigo().getEmail(),
                "Adoção aprovada",
                "Parabéns " + adocao.getTutor().getNome() + "!\n\nSua adoção do pet " + adocao.getPet().getNome() + ", solicitada em " + adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + ", foi aprovada.\nFavor entrar em contato com o abrigo " + adocao.getPet().getAbrigo().getNome() + " para agendar a busca do seu pet."
        );
    }

    @Test
    void deveriaReprovarUmaAdocao() {
        //Arrange
        given(adocaoRepository.getReferenceById(aprovacaoAdocaoDto.idAdocao())).willReturn(adocao);
        given(adocao.getPet()).willReturn(pet);
        given(pet.getAbrigo()).willReturn(abrigo);
        given(abrigo.getEmail()).willReturn("email@example.com");
        given(adocao.getTutor()).willReturn(tutor);
        given(tutor.getNome()).willReturn("Aline");
        given(adocao.getData()).willReturn(LocalDateTime.now());

        //Act
        adocaoService.reprovar(reprovacaoAdocaoDto);

        //Assert
        then(adocao).should().marcarComoReprovada(reprovacaoAdocaoDto.justificativa());
        assertEquals(StatusAdocao.REPROVADO, adocao.getStatus());
    }

    @Test
    void deveriaEnviarEmailAoReprovarUmaAdocao() {
        //Arrange
        given(adocaoRepository.getReferenceById(aprovacaoAdocaoDto.idAdocao())).willReturn(adocao);
        given(adocao.getPet()).willReturn(pet);
        given(pet.getAbrigo()).willReturn(abrigo);
        given(abrigo.getEmail()).willReturn("email@example.com");
        given(adocao.getTutor()).willReturn(tutor);
        given(tutor.getNome()).willReturn("Aline");
        given(adocao.getData()).willReturn(LocalDateTime.now());

        //Act
        adocaoService.reprovar(reprovacaoAdocaoDto);

        //Assert
        then(emailService).should().enviarEmail(
                adocao.getPet().getAbrigo().getEmail(),
                "Solicitação de adoção",
                "Olá " + adocao.getTutor().getNome() + "!\n\nInfelizmente sua adoção do pet " + adocao.getPet().getNome() + ", solicitada em " + adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + ", foi reprovada pelo abrigo " + adocao.getPet().getAbrigo().getNome() + " com a seguinte justificativa: " + adocao.getJustificativaStatus()
        );
    }

}