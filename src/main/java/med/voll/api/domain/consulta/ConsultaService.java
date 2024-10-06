package med.voll.api.domain.consulta;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import med.voll.api.domain.consulta.validacoes_agendamento.ValidadorAgendamentoConsulta;
import med.voll.api.domain.consulta.validacoes_cancelamento.ValidadorCancelamentoDeConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private List<ValidadorAgendamentoConsulta> validador;

    @Autowired
    private List<ValidadorCancelamentoDeConsulta> validadorCancelamentoDeConsultas;

    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dadosAgendamentoConsulta) {

        Medico medico = escolherMedico(dadosAgendamentoConsulta);

        var paciente = pacienteRepository.findById(dadosAgendamentoConsulta.idPaciente())
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado com o ID: " + dadosAgendamentoConsulta.idPaciente()));

        validador.forEach(validadorAgendamentoConsulta -> validadorAgendamentoConsulta.validar(dadosAgendamentoConsulta));

        var consulta = new Consulta(null, medico, paciente, dadosAgendamentoConsulta.data(), null);
        consultaRepository.save(consulta);

        return new DadosDetalhamentoConsulta(consulta);

    }

    private Medico escolherMedico(DadosAgendamentoConsulta dadosAgendamentoConsulta) {
        if (dadosAgendamentoConsulta.idMedico() != null) {
            return medicoRepository.findById(dadosAgendamentoConsulta.idMedico())
                    .orElseThrow(() -> new EntityNotFoundException("Medico não encontrado com o ID: " + dadosAgendamentoConsulta.idMedico()));
        }
        if (dadosAgendamentoConsulta.especialidade() == null) {
            throw new ValidationException("Especialidade é obrigatória quando médico não for escolhido");
        }
        var medico = medicoRepository.escolherMedicoAleatorioLivreNaData(dadosAgendamentoConsulta.especialidade(), dadosAgendamentoConsulta.data());
        if (medico == null) {
            throw new ValidationException("Não existe medico disponivel nesta data!");
        }
        return medico;

    }

    public void cancelar(@Valid DadosCancelamentoConsulta dados) {
        validadorCancelamentoDeConsultas.forEach(validadorCancelamentoDeConsulta -> validadorCancelamentoDeConsulta.validar(dados));
        var consulta = consultaRepository.findById(dados.idConsulta()).orElseThrow(() -> new ValidationException("Consulta inexistente"));
        consulta.cancelar(dados.motivo());
        consultaRepository.save(consulta);

    }
}
