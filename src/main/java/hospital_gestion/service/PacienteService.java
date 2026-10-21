/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospital_gestion.service;

import hospital_gestion.entity.Paciente;
import hospital_gestion.repository.PacienteRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author Ian
 */
@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    // Inyección de dependencias
    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    // CREATE
    @Transactional
    public Paciente registrarPaciente(
            String dni,
            String nombre,
            String apellido) {

        if (pacienteRepository.existsByDni(dni)) {
            throw new IllegalArgumentException(
                    "Ya existe un paciente con ese DNI"
            );
        }

        Paciente paciente = new Paciente(dni, nombre, apellido);

        return pacienteRepository.save(paciente);
    }

    // READ - Todos los pacientes
    @Transactional(readOnly = true)
    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    // READ - Buscar por ID
    @Transactional(readOnly = true)
    public Optional<Paciente> buscarPorId(Long id) {
        return pacienteRepository.findById(id);
    }

    // READ - Buscar por DNI
    @Transactional(readOnly = true)
    public Optional<Paciente> buscarPorDni(String dni) {
        return pacienteRepository.findByDni(dni);
    }

    // UPDATE
    @Transactional
    public Paciente modificarPaciente(
            Long id,
            String dni,
            String nombre,
            String apellido) {

        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Paciente no encontrado"
                )
                );

        Optional<Paciente> pacienteConDni
                = pacienteRepository.findByDni(dni);

        if (pacienteConDni.isPresent()
                && !pacienteConDni.get().getId().equals(id)) {

            throw new IllegalArgumentException(
                    "El DNI pertenece a otro paciente"
            );
        }

        paciente.setDni(dni);
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);

        return pacienteRepository.save(paciente);
    }

    // DELETE
    @Transactional
    public void eliminarPaciente(Long id) {

        if (!pacienteRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Paciente no encontrado"
            );
        }

        pacienteRepository.deleteById(id);
    }
}
