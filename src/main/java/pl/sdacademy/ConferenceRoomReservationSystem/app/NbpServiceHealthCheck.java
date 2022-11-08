package pl.sdacademy.ConferenceRoomReservationSystem.app;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("NbpService")
public class NbpServiceHealthCheck implements HealthIndicator, HealthContributor {


    @Override
    public Health health() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;
        String url
                = "http://api.nbp.pl/api/exchangerates/rates/c/usd/2016-04-04/?format=json";
        try {
            response = restTemplate.getForEntity(url, String.class);
        } catch (Exception e) {
            return Health.outOfService().withException(e).build();

        }
        return Health.up().build();
    }
}

