package med.voll.api.domain.consulta;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record DadosDetalhamentoConsulta(Long id, Long idMedico, Long idPaciente, LocalDateTime data) {
}
