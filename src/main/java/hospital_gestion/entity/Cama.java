package hospital_gestion.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "camas")
public class Cama {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false)
    private Integer piso;

    @Column(nullable = false)
    private boolean ocupada = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", unique = true)
    private Paciente paciente;

    // Constructor requerido por JPA
    public Cama() {
    }

    // Constructor para registrar una cama
    public Cama(String codigo, Integer piso) {
        this.codigo = codigo;
        this.piso = piso;
        this.ocupada = false;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public Integer getPiso() {
        return piso;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    // Setters
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setPiso(Integer piso) {
        this.piso = piso;
    }

    // Operaciones sobre el estado
    public void reservar(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException(
                    "Debe indicar un paciente"
            );
        }

        this.paciente = paciente;
        this.ocupada = true;
    }

    public void liberar() {
        this.paciente = null;
        this.ocupada = false;
    }
}