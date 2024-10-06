package med.voll.api.domain.consulta.validacoes_cancelamento;

import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import med.voll.api.infra.exception.ValidationException;
import org.springframework.stereotype.Component;


@Component
public class ValidadorMotivoCancelamento implements ValidadorCancelamentoDeConsulta {
    @Override
    public void validar(DadosCancelamentoConsulta dadosCancelamentoConsulta) {
        if (dadosCancelamentoConsulta.motivo() == null) {
            throw new ValidationException("Motivo do cancelamento obrigatório");
        }
    }
}
