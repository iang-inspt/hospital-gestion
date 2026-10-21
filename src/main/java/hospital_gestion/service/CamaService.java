package hospital_gestion.service;

import hospital_gestion.entity.Cama;
import hospital_gestion.repository.CamaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CamaService {

    private final CamaRepository camaRepository;

    public CamaService(CamaRepository camaRepository) {
        this.camaRepository = camaRepository;
    }

    // CREATE
    @Transactional
    public Cama registrarCama(String codigo, Integer piso) {

        validarCodigo(codigo);
        validarPiso(piso);

        if (camaRepository.existsByCodigo(codigo)) {
            throw new IllegalArgumentException(
                    "Ya existe una cama con ese código"
            );
        }

        Cama cama = new Cama(codigo, piso);

        return camaRepository.save(cama);
    }

    // READ - Listar todas
    @Transactional(readOnly = true)
    public List<Cama> listarCamas() {
        return camaRepository.findAll();
    }

    // READ - Buscar por ID
    @Transactional(readOnly = true)
    public Optional<Cama> buscarPorId(Long id) {
        return camaRepository.findById(id);
    }

    // READ - Buscar por código
    @Transactional(readOnly = true)
    public Optional<Cama> buscarPorCodigo(String codigo) {
        return camaRepository.findByCodigo(codigo);
    }

    // READ - Camas disponibles
    @Transactional(readOnly = true)
    public List<Cama> listarCamasDisponibles() {
        return camaRepository.findByOcupadaFalse();
    }

    // READ - Camas ocupadas
    @Transactional(readOnly = true)
    public List<Cama> listarCamasOcupadas() {
        return camaRepository.findByOcupadaTrue();
    }

    // UPDATE
    @Transactional
    public Cama modificarCama(
            Long id,
            String codigo,
            Integer piso) {

        Cama cama = camaRepository.findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Cama no encontrada"
                )
                );

        validarCodigo(codigo);
        validarPiso(piso);

        Optional<Cama> camaConCodigo
                = camaRepository.findByCodigo(codigo);

        if (camaConCodigo.isPresent()
                && !camaConCodigo.get().getId().equals(id)) {

            throw new IllegalArgumentException(
                    "El código pertenece a otra cama"
            );
        }

        cama.setCodigo(codigo);
        cama.setPiso(piso);

        return camaRepository.save(cama);
    }

    // DELETE
    @Transactional
    public void eliminarCama(Long id) {

        Cama cama = camaRepository.findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Cama no encontrada"
                )
                );

        if (cama.isOcupada() || cama.getPaciente() != null) {
            throw new IllegalArgumentException(
                    "No se puede eliminar una cama ocupada"
            );
        }

        camaRepository.delete(cama);
    }

    // VALIDACIONES
    private void validarCodigo(String codigo) {

        if (codigo == null
                || !codigo.matches("^CAMA-\\d{3}$")) {

            throw new IllegalArgumentException(
                    "El código debe tener el formato CAMA-XXX"
            );
        }
    }

    private void validarPiso(Integer piso) {

        if (piso == null || piso < 0) {
            throw new IllegalArgumentException(
                    "El piso debe ser un número mayor o igual a cero"
            );
        }
    }

    //Para ReservaController
    
    @Transactional(readOnly = true)
    public List<Cama> listarCamasConPaciente() {
        return camaRepository.findAllBy();
    }
}
