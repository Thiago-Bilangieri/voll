package med.voll.api.controller;

import med.voll.api.domain.consulta.ConsultaService;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.medico.Especialidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson;
    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;

    @MockBean
    private ConsultaService consultaService;


    @Test
    @DisplayName("Deveria devolver codigo 400 quando informações estão inválidas")
    @WithMockUser
    void agendarCenario01() throws Exception {

        var response = mockMvc.perform(post("/consultas"))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    @DisplayName("Deveria devolver codigo 200 quando informações estão validas")
    @WithMockUser
    void agendarCenario02() throws Exception {
        var data = LocalDateTime.now().plusHours(2);
        when(consultaService.agendar(any())).thenReturn(new DadosDetalhamentoConsulta(null, 2L,12L, data));

        var response = mockMvc.perform(post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosAgendamentoConsultaJson
                                .write(new DadosAgendamentoConsulta(2L, 12L, data, Especialidade.CARDIOLOGIA)).getJson()
                        )
                )
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        var jsonEsperado = dadosDetalhamentoConsultaJson
                .write(new DadosDetalhamentoConsulta(null, 2L, 12L, data)
                ).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }


    @Test
    void delete() {
    }
}\