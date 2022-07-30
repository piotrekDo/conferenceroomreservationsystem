//package com.sdacademy.ConferenceRoomReservationSystem.organization;
//
//import org.junit.jupiter.api.extension.ExtensionContext;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.ArgumentsProvider;
//
//import java.util.List;
//import java.util.stream.Stream;
//
//public class OrganizationsProvider implements ArgumentsProvider {
//    @Override
//    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
//        return Stream.of(
//                Arguments.arguments(
//                        List.of(
//                                new OrganizationEntity("Test organization1"),
//                                new OrganizationEntity("Test organization2"),
//                                new OrganizationEntity("Test organization3")
//                        )
//                )
//        );
//    }
//}
