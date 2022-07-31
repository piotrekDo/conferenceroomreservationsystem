package com.sdacademy.ConferenceRoomReservationSystem.organization;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

public class OrganizationsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                //input
                //sorted ASC
                //sorted DESC
                Arguments.arguments(
                        List.of(
                                new OrganizationEntity("Test organization1", "Test desc"),
                                new OrganizationEntity("Test organization12", "Test desc"),
                                new OrganizationEntity("Test organization11", "Test desc"),
                                new OrganizationEntity("Test organization9", "Test desc"),
                                new OrganizationEntity("Test organization2", "Test desc 2"),
                                new OrganizationEntity("Test organization3", "Test desc 3")
                        ), List.of(
                                new OrganizationEntity(1L, "Test organization1", "Test desc"),
                                new OrganizationEntity(3L, "Test organization11", "Test desc"),
                                new OrganizationEntity(2L, "Test organization12", "Test desc"),
                                new OrganizationEntity(5L, "Test organization2", "Test desc 2"),
                                new OrganizationEntity(6L, "Test organization3", "Test desc 3"),
                                new OrganizationEntity(4L, "Test organization9", "Test desc")
                        ),
                        List.of(
                                new OrganizationEntity(4L, "Test organization9", "Test desc"),
                                new OrganizationEntity(6L, "Test organization3", "Test desc 3"),
                                new OrganizationEntity(5L, "Test organization2", "Test desc 2"),
                                new OrganizationEntity(2L, "Test organization12", "Test desc"),
                                new OrganizationEntity(3L, "Test organization11", "Test desc"),
                                new OrganizationEntity(1L, "Test organization1", "Test desc")
                        )
                )
        );
    }
}
