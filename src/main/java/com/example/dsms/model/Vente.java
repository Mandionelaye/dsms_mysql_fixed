package com.example.dsms.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entité représentant une vente (multi-base).
 * Compatible avec MySQL et stocke les UUID sous forme de texte.
 */
@Entity
@Table(name = "vente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vente {

    @Id
    @Column(length = 36, nullable = false, unique = true)
    private String id; // UUID stocké comme texte (VARCHAR(36))

    private LocalDate dateVente;

    private Double montant;

    private String produit;

    private String region;

    private LocalDateTime updatedAt;

    // ---------- Gestion automatique des dates ----------

    @PrePersist
    public void onCreate() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = UUID.randomUUID().toString(); // ✅ généré en String
        }
        if (this.dateVente == null) {
            this.dateVente = LocalDate.now();
        }
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void setId(String id) {
        if (id != null && !id.isEmpty()) {
            this.id = id;
        }
    }

    /**
     * Définit la date de mise à jour.
     * Si null, on met automatiquement la date actuelle.
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = (updatedAt != null) ? updatedAt : LocalDateTime.now();
    }


    public String getId() {
        return id;
    }

    public LocalDate getDateVente() {
        return dateVente;
    }

    public void setDateVente(LocalDate dateVente) {
        this.dateVente = dateVente;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
