package hospital_gestion.service;

import hospital_gestion.dto.CamaPacienteDTO;
import hospital_gestion.dto.MedicoPacienteDTO;
import hospital_gestion.dto.PacienteResumenDTO;

import hospital_gestion.entity.Cama;
import hospital_gestion.entity.Medico;
import hospital_gestion.entity.Paciente;

import hospital_gestion.repository.CamaRepository;
import hospital_gestion.repository.MedicoRepository;
import hospital_gestion.repository.PacienteRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultaService {

    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final CamaRepository camaRepository;

    // INYECCIÓN DE DEPENDENCIAS
    public ConsultaService(
            PacienteRepository pacienteRepository,
            MedicoRepository medicoRepository,
            CamaRepository camaRepository) {

        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.camaRepository = camaRepository;
    }

    // CONSULTA 1: CAMA ASIGNADA A UN PACIENTE
    @Transactional(readOnly = true)
    public CamaPacienteDTO consultarCamaPaciente(Long pacienteId) {

        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Paciente no encontrado"));

        Cama cama = camaRepository.findByPacienteId(pacienteId)
                .orElse(null);

        String nombreCompleto =
                paciente.getNombre() + " " + paciente.getApellido();

        return new CamaPacienteDTO(
                paciente.getId(),
                nombreCompleto,
                cama != null ? cama.getCodigo() : null,
                cama != null ? cama.getPiso() : null
        );
    }

    // CONSULTA 2: PACIENTES ASIGNADOS A UN MÉDICO
    @Transactional(readOnly = true)
    public List<PacienteResumenDTO> consultarPacientesDeMedico(
            Long medicoId) {

        if (!medicoRepository.existsById(medicoId)) {
            throw new IllegalArgumentException(
                    "Médico no encontrado");
        }

        return pacienteRepository.findByMedicoId(medicoId)
                .stream()
                .map(paciente -> new PacienteResumenDTO(
                        paciente.getId(),
                        paciente.getDni(),
                        paciente.getNombre(),
                        paciente.getApellido()
                ))
                .toList();
    }

    // CONSULTA 3: MÉDICO ASIGNADO A UN PACIENTE
    @Transactional(readOnly = true)
    public MedicoPacienteDTO consultarMedicoPaciente(Long pacienteId) {

        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Paciente no encontrado"));

        Medico medico = paciente.getMedico();

        String nombrePaciente =
                paciente.getNombre() + " " + paciente.getApellido();

        return new MedicoPacienteDTO(
                paciente.getId(),
                nombrePaciente,
                medico != null
                        ? medico.getNombre() + " " + medico.getApellido()
                        : null,
                medico != null ? medico.getEspecialidad() : null
        );
    }
}