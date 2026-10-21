/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hospital_gestion.repository;

import hospital_gestion.entity.Medico;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Ian
 */
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Optional<Medico> findByDni(String dni);

    boolean existsByDni(String dni);
}
