package com.example.dsms.service;

import com.example.dsms.model.Vente;
import com.example.dsms.dakar.repository.VenteRepositoryDakar;
import com.example.dsms.thies.repository.VenteRepositoryThies;
import com.example.dsms.stl.repository.VenteRepositoryStl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MultiVenteService {

    private final VenteRepositoryDakar repoDakar;
    private final VenteRepositoryThies repoThies;
    private final VenteRepositoryStl repoStl;

    public MultiVenteService(VenteRepositoryDakar repoDakar,
                             VenteRepositoryThies repoThies,
                             VenteRepositoryStl repoStl) {
        this.repoDakar = repoDakar;
        this.repoThies = repoThies;
        this.repoStl = repoStl;
    }

    // -------------------
    // READ
    // -------------------
    public List<Vente> findAll() {
        List<Vente> combined = new ArrayList<>();
        combined.addAll(repoDakar.findAll());
        combined.addAll(repoThies.findAll());
        combined.addAll(repoStl.findAll());

        Map<String, Vente> byId = new LinkedHashMap<>();
        for (Vente v : combined) {
            if (v == null || v.getId() == null) continue;
            Vente existing = byId.get(v.getId());
            if (existing == null || (v.getUpdatedAt() != null &&
                    (existing.getUpdatedAt() == null || v.getUpdatedAt().isAfter(existing.getUpdatedAt())))) {
                byId.put(v.getId(), v);
            }
        }
        return new ArrayList<>(byId.values());
    }

    public Optional<Vente> findById(String id) {
        return Optional.ofNullable(
                repoDakar.findById(id).orElse(
                        repoThies.findById(id).orElse(
                                repoStl.findById(id).orElse(null)
                        )
                )
        );
    }

    private void checkRegionUnlocked(String region) {
        if (isRegionLocked(region)) {
            throw new IllegalStateException("La région " + region + " est verrouillée et ne peut pas être modifiée.");
        }
    }

    // -------------------
    // CREATE / UPDATE
    // -------------------
    @Transactional
    public Vente saveOrUpdate(String region, Vente vente) {
        checkRegionUnlocked(region); // <-- vérification si la raision est bloqué ou pas
        if (vente.getId() == null || vente.getId().isEmpty()) {
            vente.setId(UUID.randomUUID().toString());
        }
        vente.setUpdatedAt(LocalDateTime.now());

        switch (region.toLowerCase()) {
            case "thies":
            case "thiès": return repoThies.save(vente);
            case "stl":
            case "st-louis":
            case "stlouis": return repoStl.save(vente);
            case "dakar":
            default: return repoDakar.save(vente);
        }
    }

    @Transactional
    public Vente updateById(String id, Vente vente) {
        Optional<Vente> existingOpt = findById(id);
        if (existingOpt.isPresent()) {
            Vente existing = existingOpt.get();
            existing.setProduit(vente.getProduit());
            existing.setMontant(vente.getMontant());
            existing.setRegion(vente.getRegion());
            existing.setDateVente(vente.getDateVente());
            existing.setUpdatedAt(LocalDateTime.now());
            return saveOrUpdate(existing.getRegion(), existing);
        }
        throw new RuntimeException("Vente non trouvée avec ID: " + id);
    }

    // -------------------
    // DELETE
    // -------------------
    @Transactional
    public boolean deleteById(String id, String region) {
        checkRegionUnlocked(region); // <-- vérification si la raision est bloqué ou pas
        boolean deleted = false;
        if (repoDakar.existsById(id)) {
            repoDakar.deleteById(id);
            deleted = true;
        }
        if (repoThies.existsById(id)) {
            repoThies.deleteById(id);
            deleted = true;
        }
        if (repoStl.existsById(id)) {
            repoStl.deleteById(id);
            deleted = true;
        }
        return deleted;
    }


    public List<Vente> findByRegion(String region) {
        if (region == null) return findAll();
        switch (region.trim().toLowerCase()) {
            case "dakar": return repoDakar.findAll();
            case "thies":
            case "thiès": return repoThies.findAll();
            case "stlouis":
            case "st-louis":
            case "stl": return repoStl.findAll();
            default: return Collections.emptyList();
        }
    }


    private final Map<String, Boolean> regionLocked = new HashMap<>();

    public void lockRegion(String region) {
        regionLocked.put(region.toLowerCase(), true);
    }

    public void unlockRegion(String region) {
        regionLocked.put(region.toLowerCase(), false);
    }

    public boolean isRegionLocked(String region) {
        return regionLocked.getOrDefault(region.toLowerCase(), false);
    }


}
