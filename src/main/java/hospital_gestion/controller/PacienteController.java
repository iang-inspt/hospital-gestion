/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospital_gestion.controller;

import hospital_gestion.entity.Paciente;
import hospital_gestion.service.PacienteService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Ian
 */
@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public String listarPacientes(Model model) {

        model.addAttribute(
                "pacientes",
                pacienteService.listarPacientes()
        );

        return "pacientes/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "pacientes/formulario";
    }

    @PostMapping("/guardar")
    public String guardarPaciente(
            @ModelAttribute Paciente paciente,
            RedirectAttributes redirectAttributes) {

        try {
            pacienteService.registrarPaciente(
                    paciente.getDni(),
                    paciente.getNombre(),
                    paciente.getApellido()
            );

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "Paciente registrado correctamente"
            );

            return "redirect:/pacientes";

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );

            return "redirect:/pacientes/nuevo";
        }
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(
            @PathVariable Long id,
            Model model) {

        Paciente paciente = pacienteService.buscarPorId(id)
                .orElseThrow(()
                        -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente no encontrado"
                )
                );

        model.addAttribute("paciente", paciente);

        return "pacientes/editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarPaciente(
            @PathVariable Long id,
            @ModelAttribute Paciente paciente,
            RedirectAttributes redirectAttributes) {

        try {
            pacienteService.modificarPaciente(
                    id,
                    paciente.getDni(),
                    paciente.getNombre(),
                    paciente.getApellido()
            );

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "Paciente actualizado correctamente"
            );

            return "redirect:/pacientes";

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );

            return "redirect:/pacientes/editar/" + id;
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarPaciente(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            pacienteService.eliminarPaciente(id);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "Paciente eliminado correctamente"
            );

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/pacientes";
    }
}
