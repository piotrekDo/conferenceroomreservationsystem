package com.sdacademy.ConferenceRoomReservationSystem.organization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrganizationRepositoryTest {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void should_add_an_organization() {
        //given
        OrganizationEntity test_organization1 = new OrganizationEntity("Test organization1", "Test Desc");

        //when
        organizationRepository.save(test_organization1);

        //then
        assertEquals(test_organization1, testEntityManager.find(OrganizationEntity.class, "Test organization1"));
    }

    @Test
    void should_find_by_id_if_exists() {
        //given
        OrganizationEntity test_organization1 = new OrganizationEntity("Test organization1", "Test Desc");
        testEntityManager.persist(test_organization1);

        //when
        OrganizationEntity result = organizationRepository.findById("Test organization1").orElseThrow();

        //then
        assertEquals(test_organization1, result);
    }

    @ParameterizedTest
    @ArgumentsSource(OrganizationFindByIdProvider.class)
    void find_by_id_test(List<OrganizationEntity> source, String find, Optional<OrganizationEntity> expected) {
        //given
        source.forEach(testEntityManager::persist);

        Optional<OrganizationEntity> result = organizationRepository.findById(find);

        assertEquals(expected, result);
    }

    @Test
    void should_throw_an_exception_if_no_organization_found() {
        //when + then
        assertThrows(NoSuchElementException.class, () -> organizationRepository.findById("Test organization").orElseThrow());
    }

    @Test
    void should_retun_an_empty_list_if_no_organizations_yet() {
        //when
        List<OrganizationEntity> result = organizationRepository.findAll();

        //then
        assertEquals(Collections.emptyList(), result);
    }

    @ParameterizedTest
    @ArgumentsSource(OrganizationsProvider.class)
    void should_retun_all_organizations(List<OrganizationEntity> expected) {
        //given
        expected.forEach(testEntityManager::persist);

        //when
        List<OrganizationEntity> result = organizationRepository.findAll();

        //then
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ArgumentsSource(OrganizationsProvider.class)
    void should_delete_organization(List<OrganizationEntity> input) {
        //given
        input.forEach(testEntityManager::persist);
        List<OrganizationEntity> expected = new ArrayList<>();

        //when
        organizationRepository.delete(input.get(0));

        //then
        assertNull(testEntityManager.find(OrganizationEntity.class, "Test organization1"));
        assertEquals(input.get(1), testEntityManager.find(OrganizationEntity.class, "Test organization12"));
    }

    @ParameterizedTest
    @ArgumentsSource(OrganizationsProvider.class)
    void should_find_all_organizations_sorted_by_name_ASC(List<OrganizationEntity> input, List<OrganizationEntity> asc) {
        //given
        input.forEach(testEntityManager::persist);

        //when
        List<OrganizationEntity> result = organizationRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));

        //then
        assertEquals(asc, result);
    }

    @ParameterizedTest
    @ArgumentsSource(OrganizationsProvider.class)
    void should_find_all_organizations_sorted_by_name_DESC(List<OrganizationEntity> input, List<OrganizationEntity> asc,
                                                           List<OrganizationEntity> desc) {
        //given
        input.forEach(testEntityManager::persist);

        //when
        List<OrganizationEntity> result = organizationRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));

        //then
        assertEquals(desc, result);
    }

}