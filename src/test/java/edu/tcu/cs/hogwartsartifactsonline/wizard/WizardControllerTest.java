package edu.tcu.cs.hogwartsartifactsonline.wizard;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.WizardDto;
import org.hamcrest.Matchers;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class WizardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    WizardService wizardService;

    List<Wizard> wizards;

    @BeforeEach
    void setUp()  throws Exception {
        this.wizards = new ArrayList<>();

        Artifact a1 = new Artifact();
        a1.setName("artifact1");
        a1.setDescription("artifact1 description");
        a1.setImgUrl("artifact1 imgUrl");
        a1.setId("98765");

        Artifact a2 = new Artifact();
        a2.setName("artifact2");
        a2.setDescription("artifact2 description");
        a2.setImgUrl("artifact2 imgUrl");
        a2.setId("87654");

        Artifact a3 = new Artifact();
        a3.setName("artifact3");
        a3.setId("3453");
        a3.setDescription("artifact3 description");
        a3.setImgUrl("artifact3 imgUrl");
//        this.artifacts.add(a3);

        Artifact a4 = new Artifact();
        a4.setName("artifact4");
        a4.setId("45678");
        a4.setDescription("artifact4 description");
        a4.setImgUrl("artifact4 imgUrl");
//        this.artifacts.add(a4);

        Artifact a5 = new Artifact();
        a5.setName("artifact5");
        a5.setId("2345");
        a5.setDescription("artifact5 description");
        a5.setImgUrl("artifact5 imgUrl");
//        this.artifacts.add(a5);

        Artifact a6 = new Artifact();
        a6.setName("artifact6");
        a6.setId("53143");
        a6.setDescription("artifact6 description");
        a6.setImgUrl("artifact6 imgUrl");

//        wizard
        Wizard w1 = new Wizard();
        w1.setName("wizard1");
        w1.setId(1);
        w1.addArtifact(a1);
        w1.addArtifact(a3);
        this.wizards.add(w1);

        Wizard w2 = new Wizard();
        w2.setName("wizard2");
        w2.setId(2);
        w2.addArtifact(a2);
        w2.addArtifact(a4);
        this.wizards.add(w2);

        Wizard w3 = new Wizard();
        w3.setName("wizard3");
        w3.setId(3);
        w3.addArtifact(a5);

        this.wizards.add(w3);
    }

//    @AfterEach
    void tearDown() {
    }

    @Test
    void findAllWizards() throws Exception {
        given(this.wizardService.findAllWizards()).willReturn(this.wizards);

        // When and then
        this.mockMvc.perform(get("/api/v1/wizards").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.OK))
                .andExpect(jsonPath("$.message").value("All wizards"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.wizards.size())))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].name").value("wizard2"));
    }

    @Test
    void findWizardById() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
        given(this.wizardService.findById(1)).willReturn(this.wizards.get(0));

        // When and then
        this.mockMvc.perform(get("/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.OK))
                .andExpect(jsonPath("$.message").value("Found a wizard"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Albus Dumbledore"));
    }

    @Test
    void findWizardByIdNotFound() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
        given(this.wizardService.findById(5)).willThrow(new ObjectNotFoundException("wizard", 5));

        // When and then
        this.mockMvc.perform(get( "/wizards/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with Id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void updateWizard() throws Exception {
        WizardDto wizardDto = new WizardDto(null, "Updated wizard name", 0);

        Wizard updatedWizard = new Wizard();
        updatedWizard.setId(1);
        updatedWizard.setName("Updated wizard name");

        String json = this.objectMapper.writeValueAsString(updatedWizard);

        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
        given(this.wizardService.update(eq(1), Mockito.any(Wizard.class))).willReturn(updatedWizard);

        // When and then
        this.mockMvc.perform(put( "/wizards/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.OK))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Updated wizard"));
    }

    @Test
    void updateWizardNotFound() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
        given(this.wizardService.update(eq(5), Mockito.any(Wizard.class))).willThrow(new ObjectNotFoundException("wizard", 5));

        WizardDto wizardDto = new WizardDto(5, // This id does not exist in the database.
                "Updated wizard name",
                0);

        String json = this.objectMapper.writeValueAsString(wizardDto);

        // When and then
        this.mockMvc.perform(put("/wizards/5").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with Id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }


    @Test
    void deleteWizard() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
        doNothing().when(this.wizardService).delete(3);

        // When and then
        this.mockMvc.perform(delete( "/wizards/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.OK))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void deleteWizardNotFound() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
        doThrow(new ObjectNotFoundException("wizard", 5)).when(this.wizardService).delete(5);

        // When and then
        this.mockMvc.perform(delete( "/wizards/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with Id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }


}