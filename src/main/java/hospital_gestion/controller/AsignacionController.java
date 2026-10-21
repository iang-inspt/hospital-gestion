package hospital_gestion.controller;

import hospital_gestion.service.AsignacionService;
import hospital_gestion.service.PacienteService;
import hospital_gestion.service.MedicoService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/asignaciones")
public class AsignacionController {

    private final AsignacionService asignacionService;
    private final PacienteService pacienteService;
    private final MedicoService medicoService;

    public AsignacionController(
            AsignacionService asignacionService,
            PacienteService pacienteService,
            MedicoService medicoService) {

        this.asignacionService = asignacionService;
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
    }

    @GetMapping
    public String mostrarAsignaciones(Model model) {

        model.addAttribute(
                "pacientes",
                pacienteService.listarPacientesConMedico());

        model.addAttribute(
                "medicos",
                medicoService.listarMedicos());

        return "asignaciones/listar";
    }

    @PostMapping("/asignar")
    public String asignarMedico(
            @RequestParam Long pacienteId,
            @RequestParam Long medicoId,
            RedirectAttributes redirectAttributes) {

        try {
            asignacionService.asignarMedico(pacienteId, medicoId);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "Médico asignado correctamente");

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());
        }

        return "redirect:/asignaciones";
    }

    @PostMapping("/quitar/{pacienteId}")
    public String quitarMedico(
            @PathVariable Long pacienteId,
            RedirectAttributes redirectAttributes) {

        try {
            asignacionService.quitarMedico(pacienteId);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "Asignación eliminada correctamente");

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());
        }

        return "redirect:/asignaciones";
    }
}
