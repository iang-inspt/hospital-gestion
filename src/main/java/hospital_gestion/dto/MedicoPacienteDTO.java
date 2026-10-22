package hospital_gestion.dto;

public record MedicoPacienteDTO(
        Long pacienteId,
        String nombrePaciente,
        String nombreMedico,
        String especialidad
) {
}