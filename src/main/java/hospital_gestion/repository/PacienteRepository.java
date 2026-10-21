package hospital_gestion.repository;

import hospital_gestion.entity.Paciente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

     Optional<Paciente> findByDni(String dni);
     boolean existsByDni(String dni);
}