package com.sdacademy.ConferenceRoomReservationSystem.organization;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FindByIdProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new OrganizationEntity("Test organization1", "Test desc"),
                                new OrganizationEntity("Test organization12", "Test desc"),
                                new OrganizationEntity("Test organization11", "Test desc"),
                                new OrganizationEntity("Test organization9", "Test desc"),
                                new OrganizationEntity("Test organization2", "Test desc 2"),
                                new OrganizationEntity("Test organization3", "Test desc 3")
                        ),
                        "Test organization12",
                        Optional.of(new OrganizationEntity("Test organization12", "Test desc"))
                ), Arguments.of(
                        Collections.emptyList(),
                        "Test organization12",
                        Optional.empty()
                ),
                Arguments.of(
                        List.of(
                                new OrganizationEntity("Test organization1", "Test desc"),
                                new OrganizationEntity("Test organization12", "Test desc"),
                                new OrganizationEntity("Test organization11", "Test desc"),
                                new OrganizationEntity("Test organization9", "Test desc"),
                                new OrganizationEntity("Test organization2", "Test desc 2"),
                                new OrganizationEntity("Test organization3", "Test desc 3")
                        ),
                        "wrong",
                        Optional.empty()
                )

        );
    }
}
