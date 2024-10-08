package med.voll.api.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.voll.api.domain.consulta.ConsultaService;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultas")
@SecurityRequirement(name = "bearer-key")
public class    ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoConsulta> agendar(@RequestBody @Valid DadosAgendamentoConsulta dados) {
        System.out.println(dados);

        var consulta =consultaService.agendar(dados);

        return ResponseEntity.ok(new DadosDetalhamentoConsulta(null, consulta.idMedico(), consulta.idPaciente(), consulta.data()    ));

    }

    @DeleteMapping
    public ResponseEntity delete(@RequestBody @Valid DadosCancelamentoConsulta dados) {
        consultaService.cancelar(dados);
        return ResponseEntity.noContent().build();
    }
}
