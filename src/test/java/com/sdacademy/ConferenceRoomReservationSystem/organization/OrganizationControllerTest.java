package com.sdacademy.ConferenceRoomReservationSystem.organization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrganizationController.class)
class OrganizationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrganizationService organizationService;

    @Test
    void getByName_should_return_reposnse_200_and_resonse_body() throws Exception {
        //given
        String name = "Test";
        OrganizationEntity organization = new OrganizationEntity(1L, name, "Test desc");
        Mockito.when(organizationService.getOrganizationByName(name)).thenReturn(organization);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/organizations/" + name).contentType(MediaType.APPLICATION_JSON));

        //then
        Mockito.verify(organizationService).getOrganizationByName(name);
        perform.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo(organization.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", equalTo(organization.getDescription())));
    }

    @Test
    void getByName_should_return_not_found_if_no_organization_was_found_by_name() throws Exception {
        //given
        String name = "Test";
        Mockito.when(organizationService.getOrganizationByName(name)).thenThrow(
                new NoSuchElementException(String.format("No organization '%s' found!", name)));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/organizations/" + name).contentType(MediaType.APPLICATION_JSON));

        //then
        Mockito.verify(organizationService).getOrganizationByName(name);
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$", equalTo(String.format("No organization '%s' found!", name))));
    }

    @Test
    void add_should_provide_to_service_and_return_code_200_and_response_body() throws Exception {
        //given
        OrganizationEntity organization = new OrganizationEntity("New organization", "Some desc");
        OrganizationEntity organizationEntity = new OrganizationEntity(1L, "New organization", "Some desc");
        Mockito.when(organizationService.addOrganization(organization)).thenReturn(organizationEntity);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/organizations").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "New organization",
                            "description": "Some desc"
                        }
                        """));

        //then
        Mockito.verify(organizationService).addOrganization(organization);
        perform.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo(organizationEntity.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", equalTo(organizationEntity.getDescription())));
    }

    @Test
    void add_should_return_bad_request_if_adding_organization_with_existing_name() throws Exception {
        //given
        OrganizationEntity organization = new OrganizationEntity("New organization", "Some desc");
        Mockito.when(organizationService.addOrganization(organization)).thenThrow(
                new IllegalArgumentException(String.format("Organization with name '%s' exists!", organization.getName())));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/organizations").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "New organization",
                            "description": "Some desc"
                        }
                        """));

        //then
        Mockito.verify(organizationService).addOrganization(organization);
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$", equalTo(String.format("Organization with name '%s' exists!", organization.getName()))));

    }

    @ParameterizedTest
    @ArgumentsSource(OrganizationAddValidationArgumentsProvider.class)
    void add_should_return_bad_request_if_violating_validation_rules(OrganizationEntity organization, String field, String expected) throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/organizations").contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"name\": \"%s\",\"description\": \"%s\"}", organization.getName(), organization.getDescription())));
        //then
        Mockito.verify(organizationService, Mockito.never()).addOrganization(Mockito.any());
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath(field, equalTo(expected)));

    }

    @Test
    void update_should_return_code_200_and_response_body() throws Exception {
        //given
        String name = "Test";
        OrganizationEntity update = new OrganizationEntity("Test", null);
        OrganizationEntity result = new OrganizationEntity(1L, "Test", "some desc");
        Mockito.when(organizationService.updateOrganization(name, update)).thenReturn(result);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/organizations/" + name).contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                                {
                                    "name": "Test"
                                }
                                """
                ));

        //then
        Mockito.verify(organizationService).updateOrganization(name, update);
        perform.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo(result.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", equalTo(result.getDescription())));
    }

    @Test
    void update_should_return_not_found_if_trying_to_update_by_non_existing_name() throws Exception {
        //given
        String name = "Non exisiting";
        OrganizationEntity update = new OrganizationEntity("Test", null);
        Mockito.when(organizationService.updateOrganization(name, update)).thenThrow(
                new NoSuchElementException(String.format("No organization '%s' found!", name)));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/organizations/" + name).contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                                {
                                    "name": "Test"
                                }
                                """
                ));
        //then
        Mockito.verify(organizationService).updateOrganization(name, update);
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$", equalTo(String.format("No organization '%s' found!", name))));
    }

    @Test
    void update_should_return_bad_request_if_changing_name_to_existing_name() throws Exception {
        //given
        String name = "Test";
        OrganizationEntity update = new OrganizationEntity("Existing", null);
        Mockito.when(organizationService.updateOrganization(name, update)).thenThrow(
                new IllegalArgumentException(String.format("Organization with name %s already exists", update.getName())));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/organizations/" + name).contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                                {
                                    "name": "Existing"
                                }
                                """
                ));
        //then
        Mockito.verify(organizationService).updateOrganization(name, update);
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$", equalTo((String.format("Organization with name %s already exists", update.getName())))));
    }

    @Test
    void delete_should_return_code_200_and_return_response_body() throws Exception {
        //given
        String name = "Test";
        OrganizationEntity entity = new OrganizationEntity(23L,"Test", "some desc");
        Mockito.when(organizationService.deleteOrganization(name)).thenReturn(entity);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/organizations/" + name).contentType(MediaType.APPLICATION_JSON));

        //then
        Mockito.verify(organizationService).deleteOrganization(name);
        perform.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(23)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo(entity.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", equalTo(entity.getDescription())));
    }

    @Test
    void delete_should_return_not_found_if_no_reservation_exists_by_name(){
        //given
        String name = "Test";
        OrganizationEntity entity = new OrganizationEntity(23L,"Test", "some desc");
        Mockito.when(organizationService.deleteOrganization(name)).thenThrow(new NoSuchElementException(String.format("No organization '%s' found!", name)));

    }

}