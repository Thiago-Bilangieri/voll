package med.voll.api.domain.consulta.validacoes_agendamento;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class ValidadorHorarioFuncionamentoClinica implements ValidadorAgendamentoConsulta {
    public void validar(DadosAgendamentoConsulta dadosAgendamentoConsulta) {
        var domingo = dadosAgendamentoConsulta.data().getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var antesAbertura = dadosAgendamentoConsulta.data().getHour() < 7;
        var depoisDeFechar = dadosAgendamentoConsulta.data().getHour() > 18;
        if (domingo || antesAbertura || depoisDeFechar) {
            throw new ValidationException("Consulta fora do horário de funcionamento da clínica!");
        }
    }
}
