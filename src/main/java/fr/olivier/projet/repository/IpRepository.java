package fr.olivier.projet.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import fr.olivier.projet.bo.NmapBo;
import io.quarkus.runtime.Startup;
import jakarta.inject.Singleton;

@Startup
@Singleton
public class IpRepository {

        /**
     * Map contenant les adresse reseau des equipements.
     */
    private Map<String,NmapBo> mapReseau = new HashMap<>();

    public void add(NmapBo pBo) {
        mapReseau.put(pBo.getMac(), pBo);
    }
    
    public Optional<NmapBo> find(String pMac) {
        return Optional.ofNullable(mapReseau.get(pMac));
    }

    public Collection<NmapBo> findAll() {
        return mapReseau.values();
    }
}
