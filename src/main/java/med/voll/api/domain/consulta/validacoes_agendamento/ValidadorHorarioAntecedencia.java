package med.voll.api.domain.consulta.validacoes_agendamento;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorHorarioAntecedencia implements ValidadorAgendamentoConsulta{

    public void validar(DadosAgendamentoConsulta dadosAgendamentoConsulta){
        var diferenca = Duration.between(LocalDateTime.now(), dadosAgendamentoConsulta.data()).toMinutes();
        if(diferenca<30){
            throw new ValidationException("Consulta deve ser agendada com 30Min de antecedência!");
        }

    }
}
