package hospital_gestion.repository;

import hospital_gestion.entity.Cama;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CamaRepository extends JpaRepository<Cama, Long> {

    // Buscar una cama por su código
    Optional<Cama> findByCodigo(String codigo);

    // Verificar si existe una cama con ese código
    boolean existsByCodigo(String codigo);

    // Listar camas disponibles
    List<Cama> findByOcupadaFalse();

    // Listar camas ocupadas
    List<Cama> findByOcupadaTrue();

    // Verificar si un paciente tiene una cama asignada
    boolean existsByPacienteId(Long pacienteId);

    //antes de mostrar el nombre del paciente de una cama ocupada, verificar que
    //la relación esté cargada
    @EntityGraph(attributePaths = {"paciente"})
    List<Cama> findAllBy();
}
