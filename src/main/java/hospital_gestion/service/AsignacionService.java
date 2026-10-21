package hospital_gestion.service;

import hospital_gestion.entity.Medico;
import hospital_gestion.entity.Paciente;
import hospital_gestion.repository.MedicoRepository;
import hospital_gestion.repository.PacienteRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AsignacionService {

    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    public AsignacionService(
            MedicoRepository medicoRepository,
            PacienteRepository pacienteRepository) {

        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    // ASIGNAR O CAMBIAR MÉDICO
    @Transactional
    public void asignarMedico(Long pacienteId, Long medicoId) {

        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Paciente no encontrado"));

        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Médico no encontrado"));

        paciente.setMedico(medico);

        pacienteRepository.save(paciente);
    }

    // QUITAR ASIGNACIÓN
    @Transactional
    public void quitarMedico(Long pacienteId) {

        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Paciente no encontrado"));

        if (paciente.getMedico() == null) {
            throw new IllegalArgumentException(
                    "El paciente no tiene médico asignado");
        }

        paciente.setMedico(null);

        pacienteRepository.save(paciente);
    }

    // CONSULTAR PACIENTES DE UN MÉDICO
    @Transactional(readOnly = true)
    public List<Paciente> obtenerPacientesDeMedico(Long medicoId) {

        if (!medicoRepository.existsById(medicoId)) {
            throw new IllegalArgumentException(
                    "Médico no encontrado");
        }

        return pacienteRepository.findByMedicoId(medicoId);
    }
}