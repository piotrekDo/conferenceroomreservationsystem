package pl.sdacademy.ConferenceRoomReservationSystem.organization;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sdacademy.ConferenceRoomReservationSystem.SortType;

import java.util.List;

@RestController
@RequestMapping("/organizations")
class OrganizationController {

    private final OrganizationService organizationService;

    OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    List<OrganizationDto> getAll(@RequestParam(defaultValue = "ASC") SortType sortType) {
        return organizationService.getAllOrganizations(sortType);
    }

    @GetMapping("/{name}")
    OrganizationDto getById(@PathVariable String name) {
        return organizationService.getOrganization(name);
    }

    @PostMapping
    OrganizationDto add(@Validated(value = AddOrganization.class) @RequestBody OrganizationDto organization) {
        return organizationService.addOrganization(organization);
    }

    @PutMapping("/{name}")
    OrganizationDto update(@PathVariable String name, @Validated(value = UpdateOrganization.class) @RequestBody OrganizationDto organization) {
        return organizationService.updateOrganization(name, organization);
    }

    @DeleteMapping("/{name}")
    OrganizationDto delete(@PathVariable String name) {
        return organizationService.deleteOrganization(name);
    }
}
