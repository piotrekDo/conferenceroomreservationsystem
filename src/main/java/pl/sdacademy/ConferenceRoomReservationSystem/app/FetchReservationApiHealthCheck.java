package pl.sdacademy.ConferenceRoomReservationSystem.app;

import org.springframework.boot.actuate.health.CompositeHealthContributor;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.NamedContributor;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


//kompozyt heath contributor przyjmuje kompjenty tubu healt contributor, agregujemy je do jejnej mapy wsztstkie te indicatory.
//iterator ma dostarcyc jakas kolekcje czyli nasze kontrybutory w postaci NamedContributor
//get contributor operuje na mapi i pobiera po nazwie nasze komponenty

@Component("FetchUsersAPI")
public class FetchReservationApiHealthCheck implements CompositeHealthContributor {

    private Map<String, HealthContributor>
            contributors = new LinkedHashMap<>();


    private DbHealthCheck dbHealthCheck;
    private NbpServiceHealthCheck nbpServiceHealthCheck;

    public FetchReservationApiHealthCheck(DbHealthCheck dbHealthCheck, NbpServiceHealthCheck nbpServiceHealthCheck) {
        // klucz musi sie zgadzac z @Component("Database") w komponencie z healthcheckiem
        this.dbHealthCheck = dbHealthCheck;
        this.nbpServiceHealthCheck = nbpServiceHealthCheck;
        contributors.put("Database", dbHealthCheck);
        contributors.put("NbpService", nbpServiceHealthCheck);
    }

    @Override
    public HealthContributor getContributor(String name) {
        return getContributor(name);
    }

    @Override
    public Iterator<NamedContributor<HealthContributor>> iterator() {
        return contributors.entrySet().stream()
                .map((entry) -> NamedContributor.of(entry.getKey(), entry.getValue())).iterator();
    }
}