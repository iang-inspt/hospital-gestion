package hospital_gestion.service;

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
public class MedicoServiceTest {

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private PacienteRepository pacienteRepository;

    // CREATE
    @Test
    void registrarMedicoCorrectamente() {

        Medico medico = medicoService.registrarMedico(
                "TEST-M001",
                "Carlos",
                "Garcia",
                "Cardiologia"
        );

        assertNotNull(medico.getId());
        assertEquals("Cardiologia", medico.getEspecialidad());
    }

    // VALIDACIÓN DNI DUPLICADO
    @Test
    void noPermitirDniDuplicado() {

        medicoService.registrarMedico(
                "TEST-M002",
                "Laura",
                "Perez",
                "Neurologia"
        );

        assertThrows(IllegalArgumentException.class, () ->
                medicoService.registrarMedico(
                        "TEST-M002",
                        "Pedro",
                        "Lopez",
                        "Pediatria"
                )
        );
    }

    // READ
    @Test
    void buscarMedicoPorDni() {

        medicoService.registrarMedico(
                "TEST-M003",
                "Ana",
                "Martinez",
                "Traumatologia"
        );

        Medico encontrado = medicoService
                .buscarPorDni("TEST-M003")
                .orElseThrow();

        assertEquals("Ana", encontrado.getNombre());
    }

    // UPDATE
    @Test
    void modificarMedicoCorrectamente() {

        Medico medico = medicoService.registrarMedico(
                "TEST-M004",
                "Juan",
                "Gomez",
                "Clinica"
        );

        Medico modificado = medicoService.modificarMedico(
                medico.getId(),
                "TEST-M004",
                "Juan",
                "Gomez",
                "Cardiologia"
        );

        assertEquals("Cardiologia", modificado.getEspecialidad());
    }

    // DELETE
    @Test
    void eliminarMedicoSinPacientes() {

        Medico medico = medicoService.registrarMedico(
                "TEST-M005",
                "Lucia",
                "Fernandez",
                "Dermatologia"
        );

        Long id = medico.getId();

        medicoService.eliminarMedico(id);

        assertTrue(medicoService.buscarPorId(id).isEmpty());
    }

    // REGLA DE NEGOCIO
    @Test
    void noEliminarMedicoConPacientesAsignados() {

        Medico medico = medicoService.registrarMedico(
                "TEST-M006",
                "Roberto",
                "Diaz",
                "Cardiologia"
        );

        Paciente paciente = pacienteService.registrarPaciente(
                "TEST-P006",
                "Martin",
                "Suarez"
        );

        // Asociamos el médico al paciente
        paciente.setMedico(medico);
        pacienteRepository.save(paciente);

        // La eliminación debe ser rechazada
        assertThrows(IllegalArgumentException.class, () ->
                medicoService.eliminarMedico(medico.getId())
        );

        // Verificamos que el médico siga existiendo
        assertTrue(
                medicoService.buscarPorId(medico.getId()).isPresent()
        );
    }
}