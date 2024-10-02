package med.voll.api.domain.consulta;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public void agendar(DadosAgendamentoConsulta dadosAgendamentoConsulta) {

        Medico medico = escolherMedico(dadosAgendamentoConsulta);

//        var paciente = pacienteRepository.findById(dadosAgendamentoConsulta.idPaciente()).get();
        var paciente = pacienteRepository.findById(dadosAgendamentoConsulta.idPaciente())
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado com o ID: " + dadosAgendamentoConsulta.idPaciente()));


        var consulta = new Consulta(null, medico, paciente, dadosAgendamentoConsulta.data(), null);
        consultaRepository.save(consulta);

    }

    private Medico escolherMedico(DadosAgendamentoConsulta dadosAgendamentoConsulta) {
        if (dadosAgendamentoConsulta.idMedico() != null) {
            return medicoRepository.findById(dadosAgendamentoConsulta.idMedico())
                    .orElseThrow(() -> new EntityNotFoundException("Medico não encontrado com o ID: " + dadosAgendamentoConsulta.idMedico()));
//            return medicoRepository.findById(dadosAgendamentoConsulta.idMedico()).get();
        }
        if (dadosAgendamentoConsulta.especialidade() == null) {
            throw new ValidationException("Especialidade é obrigatória quando médico não for escolhido");
        }
        return medicoRepository.escolherMedicoAleatorioLivreNaData(dadosAgendamentoConsulta.especialidade(), dadosAgendamentoConsulta.data());

    }

    public void cancelar(@Valid DadosCancelamentoConsulta dados) {
//        var consulta = consultaRepository.findById(dados.idConsulta()).orElseThrow(() -> new ValidationException("Consulta inexistente"));
        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivo());
        consultaRepository.save(consulta);

    }
}
