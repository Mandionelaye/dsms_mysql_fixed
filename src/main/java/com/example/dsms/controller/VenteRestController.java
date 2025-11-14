package com.example.dsms.controller;

import com.example.dsms.model.Vente;
import com.example.dsms.service.MultiVenteService;
import com.example.dsms.service.SyncService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ventes")
public class VenteRestController {

    private final MultiVenteService multiVenteService;
    private final SyncService syncService;

    public VenteRestController(MultiVenteService multiVenteService, SyncService syncService) {
        this.multiVenteService = multiVenteService;
        this.syncService = syncService;
    }

    // -------------------------
    // Lire toutes les ventes
    // -------------------------
    @GetMapping
    public List<Vente> getAll() {
        return multiVenteService.findAll();
    }

    // -------------------------
    // Lire les ventes par région
    // -------------------------
    @GetMapping("/region/{region}")
    public List<Vente> getByRegion(@PathVariable("region") String region) {
        return multiVenteService.findByRegion(region);
    }

    // -------------------------
    // Lire une vente par ID
    // -------------------------
    @GetMapping("/{region}/{id}")
    public Optional<Vente> getById(@PathVariable("region") String region,
                                   @PathVariable("id") String id) {
        return multiVenteService.findByRegion(region)
                .stream()
                .filter(v -> v.getId().equals(id))
                .findFirst();
    }

    // -------------------------
    // Ajouter une vente et synchroniser
    // -------------------------
    @PostMapping("/{region}")
    public String addVente(@RequestBody Vente vente, @PathVariable("region") String region) {
        syncService.addAndSync(vente, region);
        return "Vente ajoutée et synchronisée avec succès";
    }

    // -------------------------
    // Mettre à jour une vente par ID
    // -------------------------
    @PutMapping("/{region}/{id}")
    public String updateVente(@PathVariable("id") String id,
                              @RequestBody Vente vente) {
        vente.setId(id);
        vente.setUpdatedAt(LocalDateTime.now());
        multiVenteService.updateById(id, vente);
        syncService.syncAllRegions();
        return "Vente mise à jour avec succès";
    }

    // -------------------------
    // Supprimer une vente par ID
    // -------------------------
    @DeleteMapping("/{region}/{id}")
    public String deleteVente(@PathVariable("id") String id, @PathVariable("region") String region) {
        multiVenteService.deleteById(id, region);
        syncService.syncAllRegions();
        return "Vente supprimée avec succès";
    }

    // -------------------------
    // Synchronisation manuelle
    // -------------------------
    @PostMapping("/sync")
    public String syncAll() {
        syncService.syncAllRegions();
        return "Synchronisation complète effectuée avec succès";
    }


    @PostMapping("/lock/{region}")
    public String lockRegion(@PathVariable("region") String region) {
        multiVenteService.lockRegion(region);
        return "Région " + region + " verrouillée pour modifications.";
    }

    @PostMapping("/unlock/{region}")
    public String unlockRegion(@PathVariable("region") String region) {
        multiVenteService.unlockRegion(region);
        return "Région " + region + " déverrouillée pour modifications.";
    }

    @GetMapping("/isLocked/{region}")
    public boolean isLocked(@PathVariable("region") String region) {
        return  multiVenteService.isRegionLocked(region) ;
    }
}
