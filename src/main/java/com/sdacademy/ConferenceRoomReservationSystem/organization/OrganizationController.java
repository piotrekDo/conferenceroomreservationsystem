package com.sdacademy.ConferenceRoomReservationSystem.organization;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    List<OrganizationEntity> getAll(@RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        return organizationService.getAllOrganizations(direction);
    }

    @GetMapping("/{name}")
    OrganizationEntity getByName(@PathVariable String name) {
        return organizationService.getOrganizationByName(name);
    }

    @PostMapping
    OrganizationEntity add(@Validated(value = AddOrganization.class) @RequestBody OrganizationEntity organization) {
        return organizationService.addOrganization(organization);
    }

    @PutMapping("/{name}")
    OrganizationEntity update(@PathVariable String name, @Validated(value = UpdateOrganization.class) @RequestBody OrganizationEntity organization) {
        return organizationService.updateOrganization(name, organization);
    }

    @DeleteMapping("/{name}")
    OrganizationEntity delete(@PathVariable String name) {
        return organizationService.deleteOrganization(name);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    ResponseEntity<Object> handleNoSuchElementException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
