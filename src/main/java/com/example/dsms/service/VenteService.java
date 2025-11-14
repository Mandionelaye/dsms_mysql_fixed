package com.example.dsms.service;

import com.example.dsms.model.Vente;
import com.example.dsms.dakar.repository.VenteRepositoryDakar;
import com.example.dsms.thies.repository.VenteRepositoryThies;
import com.example.dsms.stl.repository.VenteRepositoryStl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenteService {

    private final VenteRepositoryDakar venteRepositoryDakar;
    private final VenteRepositoryThies venteRepositoryThies;
    private final VenteRepositoryStl venteRepositoryStl;

    @Autowired
    public VenteService(
            VenteRepositoryDakar venteRepositoryDakar,
            VenteRepositoryThies venteRepositoryThies,
            VenteRepositoryStl venteRepositoryStl) {
        this.venteRepositoryDakar = venteRepositoryDakar;
        this.venteRepositoryThies = venteRepositoryThies;
        this.venteRepositoryStl = venteRepositoryStl;
    }

    // 📍 Méthodes pour chaque ville
    public List<Vente> getAllDakar() {
        return venteRepositoryDakar.findAll();
    }

    public List<Vente> getAllThies() {
        return venteRepositoryThies.findAll();
    }

    public List<Vente> getAllStLouis() {
        return venteRepositoryStl.findAll();
    }

    // Exemple : ajouter une vente dans une base spécifique
    public Vente saveDakar(Vente vente) {
        return venteRepositoryDakar.save(vente);
    }

    public Vente saveThies(Vente vente) {
        return venteRepositoryThies.save(vente);
    }

    public Vente saveStLouis(Vente vente) {
        return venteRepositoryStl.save(vente);
    }
}


