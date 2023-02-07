package pl.sdacademy.ConferenceRoomReservationSystem.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

//repprezenuntuje health indicator- koponent pozwalajacy sprawdzac statusy
//metoda health sprawdzas tatus komponentu, w tym przypadku realizujemy polaczenie z baza danych wiec wstzykujemy DS
//metoda jest wywolywana cyklicznie

//health contributor jest znacznikiem ktory mowi ze mozemy z tego korzystac do agregowani w kompozyt
//w ramach wspolnej funkcjonalnosci bedzimy traktowac to jako grupe

@Component("Database")
public class DbHealthCheck implements HealthContributor, HealthIndicator {

    @Autowired
    private DataSource ds;

    @Override
    public Health health() {
        try (Connection connection = ds.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("select * from reservation");
        } catch (SQLException e) {
            return Health.outOfService().withException(e).build();
        }
        return Health.up().build();
    }
}
