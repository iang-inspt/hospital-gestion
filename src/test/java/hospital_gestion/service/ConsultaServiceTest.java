package hospital_gestion.service;

import hospital_gestion.entity.Cama;
import hospital_gestion.entity.Medico;
import hospital_gestion.entity.Paciente;

import hospital_gestion.repository.PacienteRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ConsultaServiceTest {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private CamaService camaService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Test
    void consultarCamaDePaciente() {

        Paciente paciente = pacienteService.registrarPaciente(
                "TEST-CON-001", "Juan", "Perez");

        Cama cama = camaService.registrarCama("CAMA-971", 1);

        reservaService.reservarCama(cama.getId(), paciente.getId());

        var resultado = consultaService
                .consultarCamaPaciente(paciente.getId());

        assertEquals("CAMA-971", resultado.codigoCama());
        assertEquals(1, resultado.piso());
    }

    @Test
    void consultarPacientesDeMedico() {

        Medico medico = medicoService.registrarMedico(
                "TEST-CON-M01",
                "Carlos",
                "Garcia",
                "Cardiologia"
        );

        Paciente paciente = pacienteService.registrarPaciente(
                "TEST-CON-002", "Ana", "Lopez");

        paciente.setMedico(medico);
        pacienteRepository.save(paciente);

        var resultado = consultaService
                .consultarPacientesDeMedico(medico.getId());

        assertTrue(
                resultado.stream()
                        .anyMatch(p -> p.id().equals(paciente.getId()))
        );
    }

    @Test
    void consultarPacienteSinMedico() {

        Paciente paciente = pacienteService.registrarPaciente(
                "TEST-CON-003", "Pedro", "Martinez");

        var resultado = consultaService
                .consultarMedicoPaciente(paciente.getId());

        assertNull(resultado.nombreMedico());
        assertNull(resultado.especialidad());
    }
}