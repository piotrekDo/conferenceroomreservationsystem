package com.sdacademy.ConferenceRoomReservationSystem.organization;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class OrganizationUpdatePorvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                //id
                //found by id
                //provided to update method
                //expected
                Arguments.of(
                        "Test Organization",
                        new OrganizationEntity("Test Organization", "Desc"),
                        new OrganizationEntity("Test Organization new Name", null),
                        new OrganizationEntity("Test Organization new Name", "Desc")
                ),
                Arguments.of(
                        "My Organization",
                        new OrganizationEntity("My Organization", "Some Desc"),
                        new OrganizationEntity(null, "New Desc"),
                        new OrganizationEntity("My Organization", "New Desc")
                ),
                Arguments.of(
                        "My Organization",
                        new OrganizationEntity("My Organization", "Some Desc"),
                        new OrganizationEntity("New cool name", "New Desc"),
                        new OrganizationEntity("New cool name", "New Desc")
                ),
                Arguments.of(
                        "My Organization",
                        new OrganizationEntity("My Organization", "Some Desc"),
                        new OrganizationEntity(null, null),
                        new OrganizationEntity("My Organization", "Some Desc")
                )
        );
    }
}
