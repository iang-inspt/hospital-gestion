package hospital_gestion.controller;

import hospital_gestion.entity.Medico;
import hospital_gestion.service.MedicoService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    // READ - Listar médicos
    @GetMapping
    public String listarMedicos(Model model) {

        model.addAttribute(
                "medicos",
                medicoService.listarMedicos()
        );

        return "medicos/listar";
    }

    // CREATE - Mostrar formulario
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {

        model.addAttribute("medico", new Medico());

        return "medicos/formulario";
    }

    // CREATE - Guardar médico
    @PostMapping("/guardar")
    public String guardarMedico(
            @ModelAttribute Medico medico,
            RedirectAttributes redirectAttributes) {

        try {
            medicoService.registrarMedico(
                    medico.getDni(),
                    medico.getNombre(),
                    medico.getApellido(),
                    medico.getEspecialidad()
            );

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "Médico registrado correctamente"
            );

            return "redirect:/medicos";

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );

            return "redirect:/medicos/nuevo";
        }
    }

    // UPDATE - Mostrar formulario
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(
            @PathVariable Long id,
            Model model) {

        Medico medico = medicoService.buscarPorId(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Médico no encontrado"
                        )
                );

        model.addAttribute("medico", medico);

        return "medicos/editar";
    }

    // UPDATE - Guardar cambios
    @PostMapping("/actualizar/{id}")
    public String actualizarMedico(
            @PathVariable Long id,
            @ModelAttribute Medico medico,
            RedirectAttributes redirectAttributes) {

        try {
            medicoService.modificarMedico(
                    id,
                    medico.getDni(),
                    medico.getNombre(),
                    medico.getApellido(),
                    medico.getEspecialidad()
            );

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "Médico actualizado correctamente"
            );

            return "redirect:/medicos";

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );

            return "redirect:/medicos/editar/" + id;
        }
    }

    // DELETE
    @PostMapping("/eliminar/{id}")
    public String eliminarMedico(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            medicoService.eliminarMedico(id);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "Médico eliminado correctamente"
            );

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/medicos";
    }
}