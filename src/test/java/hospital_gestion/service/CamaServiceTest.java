package hospital_gestion.service;

import hospital_gestion.entity.Cama;
import hospital_gestion.entity.Paciente;
import hospital_gestion.repository.CamaRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CamaServiceTest {

    @Autowired
    private CamaService camaService;

    @Autowired
    private CamaRepository camaRepository;

    @Autowired
    private PacienteService pacienteService;

    // CREATE
    @Test
    void registrarCamaCorrectamente() {

        Cama cama = camaService.registrarCama("CAMA-901", 1);

        assertNotNull(cama.getId());
        assertEquals("CAMA-901", cama.getCodigo());
        assertFalse(cama.isOcupada());
        assertNull(cama.getPaciente());
    }

    // VALIDAR CÓDIGO DUPLICADO
    @Test
    void noPermitirCodigoDuplicado() {

        camaService.registrarCama("CAMA-902", 2);

        assertThrows(IllegalArgumentException.class, () ->
                camaService.registrarCama("CAMA-902", 3)
        );
    }

    // VALIDAR FORMATO
    @Test
    void rechazarCodigoInvalido() {

        assertThrows(IllegalArgumentException.class, () ->
                camaService.registrarCama("CAMA-ABC", 1)
        );
    }

    // UPDATE
    @Test
    void modificarCamaCorrectamente() {

        Cama cama = camaService.registrarCama("CAMA-903", 1);

        Cama modificada = camaService.modificarCama(
                cama.getId(),
                "CAMA-904",
                2
        );

        assertEquals("CAMA-904", modificada.getCodigo());
        assertEquals(2, modificada.getPiso());
    }

    // READ - CAMAS DISPONIBLES
    @Test
    void listarCamasDisponibles() {

        Cama cama = camaService.registrarCama("CAMA-905", 1);

        assertTrue(
                camaService.listarCamasDisponibles()
                        .stream()
                        .anyMatch(c -> c.getId().equals(cama.getId()))
        );
    }

    // DELETE - CAMA LIBRE
    @Test
    void eliminarCamaLibre() {

        Cama cama = camaService.registrarCama("CAMA-906", 1);

        Long id = cama.getId();

        camaService.eliminarCama(id);

        assertFalse(camaRepository.existsById(id));
    }

    // REGLA DE NEGOCIO - NO ELIMINAR CAMA OCUPADA
    @Test
    void noEliminarCamaOcupada() {

        Cama cama = camaService.registrarCama("CAMA-907", 2);

        Paciente paciente = pacienteService.registrarPaciente(
                "TEST-CAMA-907",
                "Juan",
                "Perez"
        );

        cama.reservar(paciente);
        camaRepository.saveAndFlush(cama);

        assertThrows(IllegalArgumentException.class, () ->
                camaService.eliminarCama(cama.getId())
        );

        assertTrue(camaRepository.existsById(cama.getId()));
    }
}