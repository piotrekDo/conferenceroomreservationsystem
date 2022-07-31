package com.sdacademy.ConferenceRoomReservationSystem.organization;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    List<OrganizationEntity> getAllOrganizations(Sort.Direction direction) {
        return organizationRepository
                .findAll(Sort.by(direction, "name"));
    }

    public OrganizationEntity getOrganizationByName(String name) {
        return organizationRepository.findByName(name).orElseThrow(() -> new NoSuchElementException(String.format("No organization '%s' found!", name)));
    }

    OrganizationEntity addOrganization(OrganizationEntity organization) {
        organizationRepository.findByName(organization.getName()).ifPresent(o -> {
            throw new IllegalArgumentException(String.format("Organization with name '%s' exists!", organization.getName()));
        });
        return organizationRepository.save(organization);
    }

    OrganizationEntity deleteOrganization(String name) {
        OrganizationEntity organization = organizationRepository.findByName(name).orElseThrow(() -> new NoSuchElementException(""));
        organizationRepository.delete(organization);
        return organization;
    }

    OrganizationEntity updateOrganization(String name, OrganizationEntity organization) {
        OrganizationEntity organizationToUpdate = organizationRepository
                .findByName(name)
                .orElseThrow(() -> new NoSuchElementException(""));
        if (organization.getDescription() != null) {
            organizationToUpdate.setDescription(organization.getDescription());
        }
        if (organization.getName() != null && !organization.getName().equals(organizationToUpdate.getName())) {
            organizationToUpdate.setName(organization.getName());
        }
        return organizationRepository.save(organizationToUpdate);
    }
}
