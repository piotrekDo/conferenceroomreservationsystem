package com.sdacademy.ConferenceRoomReservationSystem.organization;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    List<OrganizationEntity> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    OrganizationEntity addOrganization(OrganizationEntity organization) {
        return organizationRepository.save(organization);
    }

    OrganizationEntity deleteOrganization(String name) {
        OrganizationEntity organization = organizationRepository.findById(name).orElseThrow(()->new NoSuchElementException(""));
        organizationRepository.deleteById(name);
        return organization;
    }

    OrganizationEntity updateOrganization(String name, OrganizationEntity organization) {
        OrganizationEntity organizationToUpdate = organizationRepository
                .findById(name)
                .orElseThrow(()->new NoSuchElementException(""));
        if (organization.getDescription() != null) {
            organizationToUpdate.setDescription(organization.getDescription());
        }
        if (organization.getName() != null && !organization.getName().equals(organizationToUpdate.getName())){
            organizationToUpdate.setName(organization.getName());
        }
        return organizationRepository.save(organizationToUpdate);
    }
}
