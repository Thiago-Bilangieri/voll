package med.voll.api.domain.consulta.validacoes_agendamento;

import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.infra.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorUnicaConsultaNoHorario implements ValidadorAgendamentoConsulta {

    @Autowired
    private ConsultaRepository consultaRepository;

    public void validar (DadosAgendamentoConsulta dadosAgendamentoConsulta){
        if (consultaRepository.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(dadosAgendamentoConsulta.idMedico(), dadosAgendamentoConsulta.data())){
            throw new ValidationException("Médico já possui consulta marcada nesse horário!");
        }
    }
}
