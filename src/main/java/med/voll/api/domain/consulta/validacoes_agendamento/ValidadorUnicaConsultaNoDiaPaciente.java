package med.voll.api.domain.consulta.validacoes_agendamento;

import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.infra.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorUnicaConsultaNoDiaPaciente implements ValidadorAgendamentoConsulta{

    @Autowired
    private ConsultaRepository consultaRepository;

    public void validar(DadosAgendamentoConsulta dadosAgendamentoConsulta) {
        var primeiroHorario = dadosAgendamentoConsulta.data().withHour(7);
        var ultimoHorario = dadosAgendamentoConsulta.data().withHour(18);
        if (consultaRepository.existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNull(dadosAgendamentoConsulta.idPaciente(), primeiroHorario, ultimoHorario)) {
            throw new ValidationException("Paciente já possui uma consulta agendada nesse dia");
        }
    }
}
