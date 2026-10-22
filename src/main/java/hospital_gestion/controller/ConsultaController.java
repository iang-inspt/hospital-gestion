package hospital_gestion.controller;

import hospital_gestion.service.ConsultaService;
import hospital_gestion.service.PacienteService;
import hospital_gestion.service.MedicoService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService consultaService;
    private final PacienteService pacienteService;
    private final MedicoService medicoService;

    // INYECCIÓN DE DEPENDENCIAS
    public ConsultaController(
            ConsultaService consultaService,
            PacienteService pacienteService,
            MedicoService medicoService) {

        this.consultaService = consultaService;
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
    }

    // PANTALLA PRINCIPAL
    @GetMapping
    public String mostrarConsultas(Model model) {

        cargarOpciones(model);

        return "consultas/index";
    }

    // CONSULTA 1: CAMA DE UN PACIENTE
    @GetMapping("/cama")
    public String consultarCama(
            @RequestParam Long pacienteId,
            Model model) {

        cargarOpciones(model);

        try {
            model.addAttribute(
                    "resultadoCama",
                    consultaService.consultarCamaPaciente(pacienteId)
            );
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "consultas/index";
    }

    // CONSULTA 2: PACIENTES DE UN MÉDICO
    @GetMapping("/medico-pacientes")
    public String consultarPacientesDeMedico(
            @RequestParam Long medicoId,
            Model model) {

        cargarOpciones(model);

        try {
            var medico = medicoService.buscarPorId(medicoId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Médico no encontrado"));

            model.addAttribute(
                    "nombreMedicoConsultado",
                    medico.getNombre() + " " + medico.getApellido()
            );

            model.addAttribute(
                    "pacientesDelMedico",
                    consultaService.consultarPacientesDeMedico(medicoId)
            );

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "consultas/index";
    }

    // CONSULTA 3: MÉDICO DE UN PACIENTE
    @GetMapping("/paciente-medico")
    public String consultarMedico(
            @RequestParam Long pacienteId,
            Model model) {

        cargarOpciones(model);

        try {
            model.addAttribute(
                    "resultadoMedico",
                    consultaService.consultarMedicoPaciente(pacienteId)
            );

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "consultas/index";
    }

    // CARGAR OPCIONES PARA LOS FORMULARIOS
    private void cargarOpciones(Model model) {

        model.addAttribute(
                "pacientes",
                pacienteService.listarPacientes()
        );

        model.addAttribute(
                "medicos",
                medicoService.listarMedicos()
        );
    }
}