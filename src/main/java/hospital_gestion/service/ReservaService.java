package hospital_gestion.service;

import hospital_gestion.entity.Cama;
import hospital_gestion.entity.Paciente;
import hospital_gestion.repository.CamaRepository;
import hospital_gestion.repository.PacienteRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaService {

    private final CamaRepository camaRepository;
    private final PacienteRepository pacienteRepository;

    public ReservaService(
            CamaRepository camaRepository,
            PacienteRepository pacienteRepository) {

        this.camaRepository = camaRepository;
        this.pacienteRepository = pacienteRepository;
    }

    // RESERVAR CAMA
    @Transactional
    public void reservarCama(Long camaId, Long pacienteId) {

        // 1. Buscar al paciente
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Paciente no encontrado"));

        // 2. Buscar la cama
        Cama cama = camaRepository.findById(camaId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Cama no encontrada"));

        // 3. Validar disponibilidad
        if (cama.isOcupada() || cama.getPaciente() != null) {
            throw new IllegalArgumentException(
                    "La cama ya está ocupada");
        }

        // 4. Verificar que el paciente no tenga otra cama
        if (camaRepository.existsByPacienteId(pacienteId)) {
            throw new IllegalArgumentException(
                    "El paciente ya tiene una cama reservada");
        }

        // 5. Realizar reserva
        cama.reservar(paciente);

        camaRepository.save(cama);
    }

    // CANCELAR RESERVA
    @Transactional
    public void cancelarReserva(Long camaId) {

        // 1. Buscar cama
        Cama cama = camaRepository.findById(camaId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Cama no encontrada"));

        // 2. Verificar que tenga una reserva
        if (!cama.isOcupada() || cama.getPaciente() == null) {
            throw new IllegalArgumentException(
                    "La cama no tiene una reserva activa");
        }

        // 3. Liberar cama
        cama.liberar();

        camaRepository.save(cama);
    }
}