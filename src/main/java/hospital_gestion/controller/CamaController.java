package hospital_gestion.controller;

import hospital_gestion.entity.Cama;
import hospital_gestion.service.CamaService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Controller
@RequestMapping("/camas")
public class CamaController {

    private final CamaService camaService;

    public CamaController(CamaService camaService) {
        this.camaService = camaService;
    }

    // READ
    @GetMapping
    public String listarCamas(Model model) {
        model.addAttribute("camas", camaService.listarCamas());
        return "camas/listar";
    }

    // CREATE - Mostrar formulario
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("cama", new Cama());
        return "camas/formulario";
    }

    // CREATE - Registrar cama
    @PostMapping("/guardar")
    public String guardarCama(
            @ModelAttribute Cama cama,
            RedirectAttributes redirectAttributes) {

        try {
            camaService.registrarCama(
                    cama.getCodigo(),
                    cama.getPiso()
            );

            redirectAttributes.addFlashAttribute(
                    "mensaje", "Cama registrada correctamente");

            return "redirect:/camas";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "error", e.getMessage());

            return "redirect:/camas/nuevo";
        }
    }

    // UPDATE - Mostrar formulario
    @GetMapping("/editar/{id}")
    public String mostrarEdicion(
            @PathVariable Long id,
            Model model) {

        Cama cama = camaService.buscarPorId(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Cama no encontrada"));

        model.addAttribute("cama", cama);
        return "camas/editar";
    }

    // UPDATE - Guardar cambios
    @PostMapping("/actualizar/{id}")
    public String actualizarCama(
            @PathVariable Long id,
            @ModelAttribute Cama cama,
            RedirectAttributes redirectAttributes) {

        try {
            camaService.modificarCama(
                    id,
                    cama.getCodigo(),
                    cama.getPiso()
            );

            redirectAttributes.addFlashAttribute(
                    "mensaje", "Cama actualizada correctamente");

            return "redirect:/camas";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "error", e.getMessage());

            return "redirect:/camas/editar/" + id;
        }
    }

    // DELETE
    @PostMapping("/eliminar/{id}")
    public String eliminarCama(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            camaService.eliminarCama(id);

            redirectAttributes.addFlashAttribute(
                    "mensaje", "Cama eliminada correctamente");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "error", e.getMessage());
        }

        return "redirect:/camas";
    }
}