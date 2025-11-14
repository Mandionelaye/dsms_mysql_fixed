
package com.example.dsms.sync;

import com.example.dsms.service.SyncService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SyncScheduler {

    private final SyncService syncService;

    public SyncScheduler(SyncService syncService) {
        this.syncService = syncService;
    }

    /**
     * 🔁 Synchronisation automatique toutes les 60 secondes (modifiable)
     */
    @Scheduled(fixedRateString = "${sync.interval:60000}")
    public void synchronize() {
        try {
            System.out.println("[SYNC] Démarrage automatique de la synchronisation...");
            syncService.syncAllRegions(); // ✅ Appel direct à la méthode du SyncService
            System.out.println("[SYNC] Synchronisation terminée avec succès !");
        } catch (Exception e) {
            System.err.println("[SYNC] Erreur pendant la synchronisation : " + e.getMessage());
        }
    }
}
