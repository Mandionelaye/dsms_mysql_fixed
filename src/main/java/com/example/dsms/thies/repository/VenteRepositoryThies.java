package com.example.dsms.thies.repository;

import com.example.dsms.model.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VenteRepositoryThies extends JpaRepository<Vente, String> {
}
