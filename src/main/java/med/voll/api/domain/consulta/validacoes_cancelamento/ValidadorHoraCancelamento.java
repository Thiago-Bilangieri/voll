package med.voll.api.domain.consulta.validacoes_cancelamento;

import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import med.voll.api.infra.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;


@Component
public class ValidadorHoraCancelamento implements ValidadorCancelamentoDeConsulta {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Override
    public void validar(DadosCancelamentoConsulta dadosCancelamentoConsulta) {
        var consulta = consultaRepository.findById(dadosCancelamentoConsulta.idConsulta()).orElseThrow(() -> new ValidationException("Consulta Inexistente!"));

        var antecedencia = Duration.between(LocalDateTime.now(), consulta.getData());

        if (antecedencia.toHours() < 24) {
            throw new ValidationException("O cancelamento só é permitido com 24 horas de antecedência.");
        }
    }
}
