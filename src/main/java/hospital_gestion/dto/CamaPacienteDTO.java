package hospital_gestion.dto;

public record CamaPacienteDTO(
        Long pacienteId,
        String nombrePaciente,
        String codigoCama,
        Integer piso
) {
}