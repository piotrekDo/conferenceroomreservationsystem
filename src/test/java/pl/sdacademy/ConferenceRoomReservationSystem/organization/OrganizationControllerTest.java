package pl.sdacademy.ConferenceRoomReservationSystem.organization;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentCaptor;
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
import pl.sdacademy.ConferenceRoomReservationSystem.SortType;
import pl.sdacademy.ConferenceRoomReservationSystem.organization.args.SortTypeArgumentProvider;
import pl.sdacademy.ConferenceRoomReservationSystem.organization.args.ValidateAddOrganizationArgumentProvider;
import pl.sdacademy.ConferenceRoomReservationSystem.organization.args.ValidateUpdateOrganizationArgumentProvider;

import java.util.*;

import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrganizationController.class)
class OrganizationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrganizationService organizationService;

    @Test
    void when_add_valid_organization_then_should_be_saved() throws Exception {
        //given
        OrganizationDto organization = new OrganizationDto("Intive", "IT company");
        OrganizationDto addedOrganization = new OrganizationDto(1L, "Intive", "IT company");
        Mockito.when(organizationService.addOrganization(organization)).thenReturn(addedOrganization);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                        {
                                          "description": "IT company",
                                          "name": "Intive"
                                        }
                                        """
                        )
                ).andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("Intive")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", equalTo("IT company")));
    }

    @ParameterizedTest
    @ArgumentsSource(ValidateAddOrganizationArgumentProvider.class)
    void when_add_invalid_organization_arg1_then_exception_should_be_thrown_with_arg2_details(String arg1, String[] arg2) throws Exception {
        //given
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(arg1)
                ).andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalToIgnoringCase("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details.name").value(containsInAnyOrder(arg2)));
    }

    @Test
    void when_add_already_existing_organization_then_exception_should_be_thrown() throws Exception {
        //given
        OrganizationDto organization = new OrganizationDto("Intive", "IT company");
        Mockito.when(organizationService.addOrganization(organization)).thenThrow(new IllegalArgumentException("organization already exists!"));

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                        {
                                          "description": "IT company",
                                          "name": "Intive"
                                        }
                                        """
                        )
                ).andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("organization already exists!")));
    }

    @Test
    void when_delete_existing_organization_then_it_should_be_deleted() throws Exception {
        //given
        String name = "Intive";
        OrganizationDto deletedOrganization = new OrganizationDto(1L, name, "IT company");
        Mockito.when(organizationService.deleteOrganization("Intive")).thenReturn(deletedOrganization);


        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/organizations/"+name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("Intive")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", equalTo("IT company")));
    }

    @Test
    void when_delete_non_existing_organization_then_exception_should_be_thrown() throws Exception {
        //given
        String name = "Intive";
        Mockito.when(organizationService.deleteOrganization(name)).thenThrow(new NoSuchElementException("No organization found"));

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/organizations/"+name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No organization found")));
    }

    @ParameterizedTest
    @ArgumentsSource(ValidateUpdateOrganizationArgumentProvider.class)
    void when_update_organization_arg1_then_validation_should_happen(String arg1, boolean result, List<String> arg2) throws Exception {
        //given
        String existingOrgName = "Tieto";

        //when
        //then
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/organizations/"+existingOrgName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(arg1)
        );
        if (result) {
            resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        } else {
            resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalToIgnoringCase("Bad Request")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.details.name", equalTo(arg2)));
        }
    }

    @Test
    void when_update_non_existing_organization_then_exception_should_be_thrown() throws Exception {
        //given
        String name = "Intive";
        OrganizationDto organization = new OrganizationDto(name, "TEST");
        Mockito.when(organizationService.updateOrganization(name, organization)).thenThrow(new NoSuchElementException("No organization found"));

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/organizations/"+name)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                        {
                                          "description": "TEST",
                                          "name": "Intive"
                                        }
                                        """
                        )
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No organization found")));
    }

    @Test
    void when_update_existing_organization_then_organization_should_be_updated() throws Exception {
        //given
        String name = "Intive";
        OrganizationDto organization = new OrganizationDto(name, "IT company");
        OrganizationDto updatedOrganization = new OrganizationDto(1L, name, "IT company");
        Mockito.when(organizationService.updateOrganization(name, organization)).thenReturn(updatedOrganization);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/organizations/"+name)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                        {
                                          "description": "IT company",
                                          "name": "Intive"
                                        }
                                        """
                        )
                ).andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("Intive")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", equalTo("IT company")));
    }

    @Test
    void when_get_empty_organization_list_then_empty_array_should_be_returned() throws Exception {
        //given
        Mockito.when(organizationService.getAllOrganizations(SortType.ASC)).thenReturn(Collections.emptyList());

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/organizations")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @ParameterizedTest
    @ArgumentsSource(SortTypeArgumentProvider.class)
    void when_get_non_empty_organization_list_then_array_with_orgs_should_be_returned(String arg1, SortType arg2) throws Exception {
        //given
        ArgumentCaptor<SortType> sortArgumentCaptor = ArgumentCaptor.forClass(SortType.class);
        Mockito.when(organizationService.getAllOrganizations(arg2)).thenReturn(
                Arrays.asList(
                        new OrganizationDto("Intive", "IT company"),
                        new OrganizationDto("Tieto", "Delivery company")
                )
        );

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/organizations"+arg1)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
        Mockito.verify(organizationService).getAllOrganizations(sortArgumentCaptor.capture());
        Assertions.assertEquals(arg2, sortArgumentCaptor.getValue());
    }
}