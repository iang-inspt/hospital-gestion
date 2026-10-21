package hospital_gestion.service;

import hospital_gestion.entity.Cama;
import hospital_gestion.entity.Paciente;
import hospital_gestion.repository.CamaRepository;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ReservaServiceTest {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private CamaService camaService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private CamaRepository camaRepository;

    @Autowired
    private EntityManager entityManager;

    // 1. RESERVAR CAMA LIBRE
    @Test
    void reservarCamaLibreCorrectamente() {

        Cama cama = camaService.registrarCama("CAMA-801", 1);

        Paciente paciente = pacienteService.registrarPaciente(
                "TEST-R801", "Juan", "Perez");

        reservaService.reservarCama(cama.getId(), paciente.getId());

        entityManager.flush();
        entityManager.clear();

        Cama recuperada = camaRepository.findById(cama.getId())
                .orElseThrow();

        assertTrue(recuperada.isOcupada());
        assertEquals(
                paciente.getId(),
                recuperada.getPaciente().getId()
        );
    }

    // 2. NO RESERVAR CAMA OCUPADA
    @Test
    void noReservarCamaOcupada() {

        Cama cama = camaService.registrarCama("CAMA-802", 1);

        Paciente paciente1 = pacienteService.registrarPaciente(
                "TEST-R802A", "Ana", "Garcia");

        Paciente paciente2 = pacienteService.registrarPaciente(
                "TEST-R802B", "Pedro", "Lopez");

        reservaService.reservarCama(cama.getId(), paciente1.getId());

        assertThrows(IllegalArgumentException.class, () ->
                reservaService.reservarCama(
                        cama.getId(), paciente2.getId())
        );
    }

    // 3. NO PERMITIR DOS CAMAS PARA EL MISMO PACIENTE
    @Test
    void noPermitirDosCamasParaMismoPaciente() {

        Cama cama1 = camaService.registrarCama("CAMA-803", 1);
        Cama cama2 = camaService.registrarCama("CAMA-804", 1);

        Paciente paciente = pacienteService.registrarPaciente(
                "TEST-R803", "Lucia", "Martinez");

        reservaService.reservarCama(cama1.getId(), paciente.getId());

        assertThrows(IllegalArgumentException.class, () ->
                reservaService.reservarCama(
                        cama2.getId(), paciente.getId())
        );
    }

    // 4. CANCELAR RESERVA EXISTENTE
    @Test
    void cancelarReservaCorrectamente() {

        Cama cama = camaService.registrarCama("CAMA-805", 2);

        Paciente paciente = pacienteService.registrarPaciente(
                "TEST-R805", "Carlos", "Gomez");

        reservaService.reservarCama(cama.getId(), paciente.getId());

        reservaService.cancelarReserva(cama.getId());

        entityManager.flush();
        entityManager.clear();

        Cama recuperada = camaRepository.findById(cama.getId())
                .orElseThrow();

        assertFalse(recuperada.isOcupada());
        assertNull(recuperada.getPaciente());
    }

    // 5. NO CANCELAR RESERVA DE CAMA LIBRE
    @Test
    void noCancelarReservaDeCamaLibre() {

        Cama cama = camaService.registrarCama("CAMA-806", 2);

        assertThrows(IllegalArgumentException.class, () ->
                reservaService.cancelarReserva(cama.getId())
        );
    }

    // 6. PACIENTE INEXISTENTE
    @Test
    void noReservarParaPacienteInexistente() {

        Cama cama = camaService.registrarCama("CAMA-807", 2);

        assertThrows(IllegalArgumentException.class, () ->
                reservaService.reservarCama(cama.getId(), -1L)
        );
    }

    // 7. CAMA INEXISTENTE
    @Test
    void noReservarCamaInexistente() {

        Paciente paciente = pacienteService.registrarPaciente(
                "TEST-R808", "Maria", "Fernandez");

        assertThrows(IllegalArgumentException.class, () ->
                reservaService.reservarCama(-1L, paciente.getId())
        );
    }

    // 8. RESERVAR OTRA CAMA DESPUÉS DE CANCELAR
    @Test
    void reservarOtraCamaDespuesDeCancelar() {

        Cama cama1 = camaService.registrarCama("CAMA-809", 3);
        Cama cama2 = camaService.registrarCama("CAMA-810", 3);

        Paciente paciente = pacienteService.registrarPaciente(
                "TEST-R809", "Martin", "Suarez");

        reservaService.reservarCama(cama1.getId(), paciente.getId());

        reservaService.cancelarReserva(cama1.getId());

        reservaService.reservarCama(cama2.getId(), paciente.getId());

        entityManager.flush();
        entityManager.clear();

        Cama primera = camaRepository.findById(cama1.getId())
                .orElseThrow();

        Cama segunda = camaRepository.findById(cama2.getId())
                .orElseThrow();

        assertFalse(primera.isOcupada());
        assertNull(primera.getPaciente());

        assertTrue(segunda.isOcupada());
        assertEquals(
                paciente.getId(),
                segunda.getPaciente().getId()
        );
    }
}