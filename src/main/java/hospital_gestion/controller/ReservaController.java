package hospital_gestion.controller;

import hospital_gestion.service.ReservaService;
import hospital_gestion.service.CamaService;
import hospital_gestion.service.PacienteService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final CamaService camaService;
    private final PacienteService pacienteService;

    public ReservaController(
            ReservaService reservaService,
            CamaService camaService,
            PacienteService pacienteService) {

        this.reservaService = reservaService;
        this.camaService = camaService;
        this.pacienteService = pacienteService;
    }

    // Mostrar gestión de reservas
    @GetMapping
    public String listarReservas(Model model) {

        model.addAttribute(
                "camas",
                camaService.listarCamasConPaciente()
        );

        model.addAttribute(
                "pacientes",
                pacienteService.listarPacientes()
        );

        return "reservas/listar";
    }

    // Registrar reserva
    @PostMapping("/reservar")
    public String reservarCama(
            @RequestParam Long camaId,
            @RequestParam Long pacienteId,
            RedirectAttributes redirectAttributes) {

        try {
            reservaService.reservarCama(camaId, pacienteId);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "Cama reservada correctamente"
            );

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/reservas";
    }

    // Cancelar reserva
    @PostMapping("/cancelar/{camaId}")
    public String cancelarReserva(
            @PathVariable Long camaId,
            RedirectAttributes redirectAttributes) {

        try {
            reservaService.cancelarReserva(camaId);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "Reserva cancelada correctamente"
            );

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/reservas";
    }
}