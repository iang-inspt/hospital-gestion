package hospital_gestion.dto;

public record PacienteResumenDTO(
        Long id,
        String dni,
        String nombre,
        String apellido
) {
}