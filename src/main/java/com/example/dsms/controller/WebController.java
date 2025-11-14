package com.example.dsms.controller;

import com.example.dsms.model.Vente;
import com.example.dsms.service.MultiVenteService;
import com.example.dsms.service.SyncService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class WebController {

    private final MultiVenteService multiVenteService;
    private final SyncService syncService;

    public WebController(MultiVenteService multiVenteService, SyncService syncService) {
        this.multiVenteService = multiVenteService;
        this.syncService = syncService;
    }

    // -----------------------
    // Page principale
    // -----------------------
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("ventes", multiVenteService.findAll());
        model.addAttribute("vente", new Vente());
        return "index";
    }

    // -----------------------
    // Ajout d’une vente + synchronisation
    // -----------------------
    @PostMapping("/add")
    public String addVente(@ModelAttribute Vente vente, @RequestParam("region") String region) {
        vente.setUpdatedAt(LocalDateTime.now());
        syncService.addAndSync(vente, region);
        return "redirect:/";
    }

    // -----------------------
    // Édition : afficher formulaire pré-rempli
    // -----------------------
    @GetMapping("/edit/{id}")
    public String editVente(@PathVariable("id") String id, Model model) {
        Vente vente = syncService.getById(id);
        model.addAttribute("vente", vente);
        model.addAttribute("ventes", multiVenteService.findAll());
        return "index";
    }

    // -----------------------
    // Mise à jour d’une vente
    // -----------------------
    @PostMapping("/update/{id}")
    public String updateVente(@PathVariable("id") String id, @ModelAttribute Vente vente, @RequestParam("region") String region) {
        vente.setId(id);
        vente.setUpdatedAt(LocalDateTime.now());
        syncService.update(id, vente); // update via SyncService -> MultiVenteService
        syncService.syncAllRegions();  // synchronisation LWW
        return "redirect:/";
    }

    // -----------------------
    // Suppression d’une vente
    // -----------------------
    @PostMapping("/delete/{id}/{region}")
    public String deleteVente(@PathVariable("id") String id, @PathVariable("region") String region) {
        boolean deleted = syncService.delete(id, region); // supprime de toutes les régions
        if (!deleted) {
            throw new RuntimeException("Impossible de supprimer la vente avec ID : " + id);
        }
        syncService.syncAllRegions(); // pour propager la suppression
        return "redirect:/";
    }


    @PostMapping("/lock/{region}")
    public String lockRegion(@PathVariable("region") String region) {
        multiVenteService.lockRegion(region);
        return "redirect:/";
    }

    @PostMapping("/unlock/{region}")
    public String unlockRegion(@PathVariable("region") String region) {
        multiVenteService.unlockRegion(region);
        return "redirect:/";
    }
}
