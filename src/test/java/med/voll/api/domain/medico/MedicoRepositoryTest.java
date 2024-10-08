package med.voll.api.domain.medico;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.consulta.MotivoCancelamento;
import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.paciente.DadosCadastroPaciente;
import med.voll.api.domain.paciente.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("Deveria devolver null quando nao tiver medico cadastrado disponível na data.")
    void escolherMedicoAleatorioLivreNaDataCenario1() {
        var proximaSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);
        var medico = cadastrarMedico("Thiago Bilangieri", "thiagobilan@gmail.com", "123321", Especialidade.CARDIOLOGIA);
        var paciente = cadastrarPaciente("Dione Bilangieri", "dbilan@gmail.com", "12312312312");
        cadastrarConsulta(medico, paciente, proximaSegundaAs10);

        var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAs10);
        assertThat(medicoLivre).isNull();
    }


    @Test
    @DisplayName("Deveria devolver medico quando tiver disponível na data.")
    void escolherMedicoAleatorioLivreNaDataCenario2() {
        var proximaSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);
        var medico = cadastrarMedico("Thiago Bilangieri", "thiagobilan@gmail.com", "123321", Especialidade.CARDIOLOGIA);

        var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAs10);
        assertThat(medicoLivre).isEqualTo(medico);
    }

    private DadosEndereco dadosEndereco() {
        return new DadosEndereco(
                "Rua Penedo",
                "Olaria",
                "21070740",
                "Rio de Janeiro",
                "RJ",
                "Casa 05",
                "142"
        );
    }


    private DadosCadastroPaciente dadosCadastroPaciente(String nome, String email, String cpf) {
        return new DadosCadastroPaciente(nome, email, "999999999", cpf, dadosEndereco());
    }

    private Paciente cadastrarPaciente(String nome, String email, String cpf) {
        var paciente = new Paciente(dadosCadastroPaciente(nome, email, cpf));
        testEntityManager.persist(paciente);
        return paciente;
    }

    private DadosCadastroMedico dadosCadastroMedico(String nome, String email, String crm, Especialidade especialidade) {
        return new DadosCadastroMedico(nome, email, "987987678", crm, especialidade, dadosEndereco());
    }

    private Medico cadastrarMedico(String nome, String email, String crm, Especialidade especialidade) {
        var medico = new Medico(dadosCadastroMedico(nome, email, crm, especialidade));
        testEntityManager.persist(medico);
        return medico;

    }

    private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime dateTime) {
        testEntityManager.persist(new Consulta(null, medico, paciente, dateTime));
    }

}