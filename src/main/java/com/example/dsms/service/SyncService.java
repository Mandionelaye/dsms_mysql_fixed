package com.example.dsms.service;

import com.example.dsms.model.Vente;
import com.example.dsms.dakar.repository.VenteRepositoryDakar;
import com.example.dsms.thies.repository.VenteRepositoryThies;
import com.example.dsms.stl.repository.VenteRepositoryStl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class SyncService {

    private final MultiVenteService multiVenteService;
    private final VenteRepositoryDakar repoDakar;
    private final VenteRepositoryThies repoThies;
    private final VenteRepositoryStl repoStl;

    public SyncService(MultiVenteService multiVenteService,
                       VenteRepositoryDakar repoDakar,
                       VenteRepositoryThies repoThies,
                       VenteRepositoryStl repoStl) {
        this.multiVenteService = multiVenteService;
        this.repoDakar = repoDakar;
        this.repoThies = repoThies;
        this.repoStl = repoStl;
    }

    // -------------------------
    // Ajouter une vente et synchroniser toutes les régions
    // -------------------------
    @Transactional
    public void addAndSync(Vente vente, String region) {
        if (vente.getId() == null || vente.getId().isEmpty()) {
            vente.setId(UUID.randomUUID().toString());
        }
        multiVenteService.saveOrUpdate(region, vente);
        syncAllRegions();
    }

    // -------------------------
    // Synchroniser toutes les bases entre elles selon LWW
    // -------------------------
    @Transactional
    public void syncAllRegions() {
        List<Vente> allDakar = repoDakar.findAll();
        List<Vente> allThies = repoThies.findAll();
        List<Vente> allStl = repoStl.findAll();

        // Dakar → Thies et Saint-Louis
        for (Vente v : allDakar) {
            saveOrUpdateRepo(repoThies, v);
            saveOrUpdateRepo(repoStl, v);
        }

        // Thies → Dakar et Saint-Louis
        for (Vente v : allThies) {
            saveOrUpdateRepo(repoDakar, v);
            saveOrUpdateRepo(repoStl, v);
        }

        // Saint-Louis → Dakar et Thies
        for (Vente v : allStl) {
            saveOrUpdateRepo(repoDakar, v);
            saveOrUpdateRepo(repoThies, v);
        }
    }

    // -------------------------
    // Sauvegarde ou met à jour une vente dans un repository selon LWW
    // -------------------------
    private void saveOrUpdateRepo(org.springframework.data.jpa.repository.JpaRepository<Vente, String> repo, Vente v) {
        if (v.getId() == null) return;

        if (repo.findById(v.getId()).isPresent()) {
            Vente existing = repo.findById(v.getId()).get();
            if (existing.getUpdatedAt() == null ||
                    (v.getUpdatedAt() != null && v.getUpdatedAt().isAfter(existing.getUpdatedAt()))) {
                copyFields(existing, v);
                repo.save(existing);
            }
        } else {
            repo.save(v);
        }
    }

    // -------------------------
    // Copie des champs métier d'une vente
    // -------------------------
    private void copyFields(Vente target, Vente source) {
        target.setProduit(source.getProduit());
        target.setMontant(source.getMontant());
        target.setRegion(source.getRegion());
        target.setDateVente(source.getDateVente());
        target.setUpdatedAt(source.getUpdatedAt());
    }

    // -------------------------
    // Méthodes CRUD "utilitaires" via MultiVenteService
    // -------------------------
    public Vente getById(String id) {
        return multiVenteService.findById(id)
                .orElseThrow(() -> new RuntimeException("Vente non trouvée avec ID: " + id));
    }

    public Vente update(String id, Vente vente) {
        return multiVenteService.updateById(id, vente);
    }

    public boolean delete(String id, String region) {
        return multiVenteService.deleteById(id, region);
    }

    public MultiVenteService getMultiVenteService() {
        return multiVenteService;
    }
}
