package pl.sdacademy.ConferenceRoomReservationSystem.reservation;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public class FindByDateOkArgumtntsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                //reservation from 20.08 at 10 - 11
                Arguments.of(
                        LocalDateTime.of(2022, 8, 20, 11, 0),
                        LocalDateTime.of(2022, 8, 20, 12, 0)
                ),
                Arguments.of(
                        LocalDateTime.of(2022, 8, 20, 9, 0),
                        LocalDateTime.of(2022, 8, 20, 10, 0)
                ),
                Arguments.of(
                        LocalDateTime.of(2022, 8, 22, 10, 0),
                        LocalDateTime.of(2022, 8, 22, 11, 0)
                ),
                Arguments.of(
                        LocalDateTime.of(2022, 8, 22, 8, 0),
                        LocalDateTime.of(2022, 8, 22, 13, 0)
                )
        );
    }
}
