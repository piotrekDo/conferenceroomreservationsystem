package com.sdacademy.ConferenceRoomReservationSystem.organization;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class OrganizationServiceTest {

    @TestConfiguration
    static class ReservationTestConfig {

        @Bean
        OrganizationService reservationService(OrganizationRepository organizationRepository) {
            return new OrganizationService(organizationRepository);
        }
    }

    @MockBean
    OrganizationRepository organizationRepository;

    @Autowired
    OrganizationService organizationService;


    @ParameterizedTest
    @ArgumentsSource(OrganizationsProvider.class)
    void should_return_all_organizations_sorted_ASC(List<OrganizationEntity> input, List<OrganizationEntity> asc) {
        //given
        Mockito.when(organizationRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(asc);

        //when
        List<OrganizationEntity> result = organizationService.getAllOrganizations(Sort.Direction.ASC);

        //then
        assertEquals(asc, result);
    }

    @ParameterizedTest
    @ArgumentsSource(OrganizationsProvider.class)
    void should_return_all_organizations_sorted_DESC(List<OrganizationEntity> input, List<OrganizationEntity> asc,
                                                     List<OrganizationEntity> desc) {
        //given
        Mockito.when(organizationRepository.findAll(Sort.by(Sort.Direction.DESC, "name"))).thenReturn(desc);

        //when
        List<OrganizationEntity> result = organizationService.getAllOrganizations(Sort.Direction.DESC);

        //then
        assertEquals(desc, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ASC", "DESC"})
    void should_return_an_empty_list(String direction) {
        //given
        Mockito.when(organizationRepository.findAll(Sort.by(Sort.Direction.valueOf(direction), "name")))
                .thenReturn(Collections.emptyList());

        //when
        List<OrganizationEntity> result = organizationService.getAllOrganizations(Sort.Direction.valueOf(direction));

        //then
        assertEquals(Collections.emptyList(), result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ASC", "DESC"})
    void when_get_all_with_asc_order_then_asc_order_info_should_be_provided_to_repo(String value) {
        //given
        ArgumentCaptor<Sort> sortArgumentCaptor = ArgumentCaptor.forClass(Sort.class);

        //when
        organizationService.getAllOrganizations(Sort.Direction.valueOf(value));

        //then
        Mockito.verify(organizationRepository).findAll(sortArgumentCaptor.capture());
        assertEquals(Sort.by(Sort.Direction.valueOf(value), "name"), sortArgumentCaptor.getValue());
    }

    @Test
    void should_add_an_organization() {
        //given
        OrganizationEntity test_organization1 = new OrganizationEntity("Test organization1", "Test Desc");
        Mockito.when(organizationRepository.findById(test_organization1.getName())).thenReturn(Optional.empty());
        Mockito.when(organizationRepository.save(test_organization1)).thenReturn(test_organization1);

        //when
        OrganizationEntity result = organizationService.addOrganization(test_organization1);

        //then
        assertEquals(test_organization1, result);
        Mockito.verify(organizationRepository, Mockito.times(1)).save(test_organization1);
    }

    @Test
    void should_throw_an_exception_when_adding_existing_organization() {
        //given
        OrganizationEntity test_organization1 = new OrganizationEntity("Test organization1", "Test Desc");
        Mockito.when(organizationRepository.findById(test_organization1.getName())).thenReturn(Optional.of(test_organization1));

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> {
            organizationService.addOrganization(test_organization1);
        });
        Mockito.verify(organizationRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void when_delete_organization_which_not_exist_in_repo_then_exception_should_be_thrown() {
        //given
        String id = "test";
        Mockito.when(organizationRepository.findById(id)).thenReturn(Optional.empty());

        //when
        //then
        assertThrows(NoSuchElementException.class, () -> {
            organizationService.deleteOrganization(id);
        });
        Mockito.verify(organizationRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void when_delete_organization_which_exist_in_repo_then_delete_on_repo_should_be_called() {
        //given
        String id = "test";
        OrganizationEntity test_organization1 = new OrganizationEntity("Test organization1", "Test Desc");
        Mockito.when(organizationRepository.findById(id)).thenReturn(Optional.of(test_organization1));

        //when
        OrganizationEntity result = organizationService.deleteOrganization(id);

        //then
        Mockito.verify(organizationRepository).delete(test_organization1);
        assertEquals(test_organization1, result);
    }

    @Test
    void when_update_non_existing_organization_then_exception_should_be_thrown() {
        //given
        String id = "Test organization1";
        OrganizationEntity test_organization1 = new OrganizationEntity("Test organization1", "Test Desc");
        Mockito.when(organizationRepository.findById("Test organization1")).thenReturn(Optional.empty());

        //when
        //then
        assertThrows(NoSuchElementException.class, () -> {
            organizationService.updateOrganization(id, test_organization1);
        });
        Mockito.verify(organizationRepository, Mockito.never()).save(Mockito.any());
    }

    @ParameterizedTest
    @ArgumentsSource(OrganizationUpdatePorvider.class)
    void when_update_existing_organization_then_should_be_updated(String id, OrganizationEntity foundById,
                                                                  OrganizationEntity provided, OrganizationEntity expected) {
        //given
        Mockito.when(organizationRepository.findById(id)).thenReturn(Optional.of(foundById));
        Mockito.when(organizationRepository.save(foundById)).thenReturn(expected);

        //when
        OrganizationEntity result = organizationService.updateOrganization(id, provided);

        //then
        assertEquals(expected, result);
        Mockito.verify(organizationRepository).save(foundById);
    }

}