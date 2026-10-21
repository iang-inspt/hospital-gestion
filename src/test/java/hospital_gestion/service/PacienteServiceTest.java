package hospital_gestion.service;

import hospital_gestion.entity.Paciente;
import hospital_gestion.repository.PacienteRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PacienteServiceTest {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Test
    void registrarPacienteCorrectamente() {

        Paciente paciente = pacienteService.registrarPaciente(
                "TEST-12345678",
                "Juan",
                "Perez"
        );

        assertNotNull(paciente.getId());

        Paciente recuperado = pacienteService
                .buscarPorDni("TEST-12345678")
                .orElseThrow();

        assertEquals("Juan", recuperado.getNombre());
        assertEquals("Perez", recuperado.getApellido());
    }

    @Test
    void noPermitirDniDuplicado() {

        pacienteService.registrarPaciente(
                "TEST-87654321",
                "Maria",
                "Garcia"
        );

        assertThrows(IllegalArgumentException.class, () -> {
            pacienteService.registrarPaciente(
                    "TEST-87654321",
                    "Pedro",
                    "Lopez"
            );
        });
    }

    @Test
    void modificarPacienteCorrectamente() {

        Paciente paciente = pacienteService.registrarPaciente(
                "TEST-11223344",
                "Ana",
                "Martinez"
        );

        Paciente modificado = pacienteService.modificarPaciente(
                paciente.getId(),
                "TEST-11223344",
                "Ana Maria",
                "Martinez"
        );

        assertEquals("Ana Maria", modificado.getNombre());
    }

    @Test
    void eliminarPacienteCorrectamente() {

        Paciente paciente = pacienteService.registrarPaciente(
                "TEST-44332211",
                "Carlos",
                "Gomez"
        );

        Long id = paciente.getId();

        pacienteService.eliminarPaciente(id);

        assertFalse(pacienteRepository.existsById(id));
    }
}