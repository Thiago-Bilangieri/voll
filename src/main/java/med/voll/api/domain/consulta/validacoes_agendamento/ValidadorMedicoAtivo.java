package med.voll.api.domain.consulta.validacoes_agendamento;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.infra.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ValidadorMedicoAtivo implements ValidadorAgendamentoConsulta{

    @Autowired
    private MedicoRepository medicoRepository;


    public void validar (DadosAgendamentoConsulta dadosAgendamentoConsulta){
        if(dadosAgendamentoConsulta.idMedico() == null){
            return;
        }

        var medico = medicoRepository.findAtivoById(dadosAgendamentoConsulta.idMedico());
        if(!medico){
            throw new ValidationException("Médico excluido");
        }
    }
}
