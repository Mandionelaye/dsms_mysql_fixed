package com.example.dsms.stl.repository;

import com.example.dsms.model.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VenteRepositoryStl extends JpaRepository<Vente, String> {
}

