package com.sdacademy.ConferenceRoomReservationSystem.organization;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class OrganizationAddValidationArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                Arguments.of(
                        new OrganizationEntity("N", "Some desc"),
                        "$.name",
                        "size must be between 2 and 20"
                ),
                Arguments.of(
                        new OrganizationEntity("New organization organization", "Some desc"),
                        "$.name",
                        "size must be between 2 and 20"
                )
        );
    }
}
