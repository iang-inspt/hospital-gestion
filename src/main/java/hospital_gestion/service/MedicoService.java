package hospital_gestion.service;

import hospital_gestion.entity.Medico;
import hospital_gestion.entity.Paciente;
import hospital_gestion.repository.MedicoRepository;
import hospital_gestion.repository.PacienteRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    public MedicoService(
            MedicoRepository medicoRepository,
            PacienteRepository pacienteRepository) {

        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    // CREATE
    @Transactional
    public Medico registrarMedico(
            String dni,
            String nombre,
            String apellido,
            String especialidad) {

        if (medicoRepository.existsByDni(dni)) {
            throw new IllegalArgumentException(
                    "Ya existe un médico con ese DNI"
            );
        }

        Medico medico = new Medico(
                dni, nombre, apellido, especialidad
        );

        return medicoRepository.save(medico);
    }

    // READ - Listar médicos
    @Transactional(readOnly = true)
    public List<Medico> listarMedicos() {
        return medicoRepository.findAll();
    }

    // READ - Buscar por ID
    @Transactional(readOnly = true)
    public Optional<Medico> buscarPorId(Long id) {
        return medicoRepository.findById(id);
    }

    // READ - Buscar por DNI
    @Transactional(readOnly = true)
    public Optional<Medico> buscarPorDni(String dni) {
        return medicoRepository.findByDni(dni);
    }

    // READ - Pacientes asignados
    @Transactional(readOnly = true)
    public List<Paciente> listarPacientesDeMedico(Long medicoId) {

        if (!medicoRepository.existsById(medicoId)) {
            throw new IllegalArgumentException(
                    "Médico no encontrado"
            );
        }

        return pacienteRepository.findByMedicoId(medicoId);
    }

    // UPDATE
    @Transactional
    public Medico modificarMedico(
            Long id,
            String dni,
            String nombre,
            String apellido,
            String especialidad) {

        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Médico no encontrado"
                        )
                );

        Optional<Medico> medicoConDni =
                medicoRepository.findByDni(dni);

        if (medicoConDni.isPresent()
                && !medicoConDni.get().getId().equals(id)) {

            throw new IllegalArgumentException(
                    "El DNI pertenece a otro médico"
            );
        }

        medico.setDni(dni);
        medico.setNombre(nombre);
        medico.setApellido(apellido);
        medico.setEspecialidad(especialidad);

        return medicoRepository.save(medico);
    }

    // DELETE
    @Transactional
    public void eliminarMedico(Long id) {

        if (!medicoRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Médico no encontrado"
            );
        }

        if (pacienteRepository.existsByMedicoId(id)) {
            throw new IllegalArgumentException(
                    "No se puede eliminar un médico con pacientes asignados"
            );
        }

        medicoRepository.deleteById(id);
    }
}