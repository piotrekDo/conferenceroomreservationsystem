package com.sdacademy.ConferenceRoomReservationSystem.organization;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

public class OrganizationAddValidationArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                Arguments.of(
                        new OrganizationEntity("N", "Some desc"),
                        400,
                        "Bad Request",
                        List.of("size must be between 2 and 20")
                ),
                Arguments.of(
                        new OrganizationEntity("New organization organization", "Some desc"),
                        400,
                        "Bad Request",
                        List.of("size must be between 2 and 20")
                ),
                Arguments.of(
                        new OrganizationEntity("", "Some desc"),
                        400,
                        "Bad Request",
                        List.of("must not be blank", "size must be between 2 and 20")
                )
        );
    }
}
