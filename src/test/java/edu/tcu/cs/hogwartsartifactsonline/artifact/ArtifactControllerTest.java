package edu.tcu.cs.hogwartsartifactsonline.artifact;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;

import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ArtifactControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ArtifactService artifactService;

    @Autowired
    ObjectMapper objectMapper;


    List<Artifact> artifacts;

    @Value("${spring.api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        this.artifacts = new ArrayList<>();

        Artifact a1 = new Artifact();

        a1.setName("artifact1");
        a1.setId("12345");
        a1.setDescription("artifact1 description");
        a1.setImgUrl("artifact1 imgUrl");
        this.artifacts.add(a1);

        Artifact a2 = new Artifact();
        a2.setName("artifact2");
        a2.setId("56789");
        a2.setDescription("artifact2 description");
        a2.setImgUrl("artifact2 imgUrl");
        this.artifacts.add(a2);

        Artifact a3 = new Artifact();
        a3.setName("artifact3");
        a3.setId("67890");
        a3.setDescription("artifact3 description");
        a3.setImgUrl("artifact3 imgUrl");
        this.artifacts.add(a3);

        Artifact a4 = new Artifact();
        a4.setName("artifact4");
        a4.setId("45678");
        a4.setDescription("artifact4 description");
        a4.setImgUrl("artifact4 imgUrl");
        this.artifacts.add(a4);

        Artifact a5 = new Artifact();
        a5.setName("artifact5");
        a5.setId("37890");
        a5.setDescription("artifact5 description");
        a5.setImgUrl("artifact5 imgUrl");
        this.artifacts.add(a5);

        Artifact a6 = new Artifact();
        a6.setName("artifact6");
        a6.setId("17890");
        a6.setDescription("artifact6 description");
        a6.setImgUrl("artifact6 imgUrl");
        this.artifacts.add(a6);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindArtifactByIdSuccess() throws Exception {
//        given
        given(this.artifactService.findById("45678")).willReturn(this.artifacts.get(0));
//        when
        this.mockMvc.perform(get(this.baseUrl+ "/artifacts/45678").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.OK))
                .andExpect(jsonPath("$.message").value("Found one"))
                .andExpect(jsonPath("$.data.id").value("45678"))
                .andExpect(jsonPath("$.data.name").value("artifact1"));
    }

    @Test
    void testFindArtifactByIdNotFound() throws Exception {
//        given
        given(this.artifactService.findById("12345")).willThrow(new ObjectNotFoundException("Artifact","12345"));
//        when
        this.mockMvc.perform(get(this.baseUrl + "/artifacts/12345").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find Artifact with Id 12345"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllArtifactsSuccess() throws Exception {
        given(this.artifactService.findAll()).willReturn(this.artifacts);

        this.mockMvc.perform(get(this.baseUrl + "/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.OK))
                .andExpect(jsonPath("$.message").value("Found all artifacts"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.artifacts.size())))
                .andExpect(jsonPath("$.data[0].id").value("12345"))
                .andExpect(jsonPath("$.data[0].name").value("artifact1"));
    }

    @Test
    void testAddArtifactSuccess() throws Exception {
        ArtifactDto artifactDto = new ArtifactDto(null, "placide", "description", "image url", null);
        String json = this.objectMapper.writeValueAsString(artifactDto);

        Artifact art = new Artifact();

        art.setId("123456789");
        art.setName("placide");
        art.setDescription("description");
        art.setImgUrl("image url");

        given(this.artifactService.save(Mockito.any(Artifact.class))).willReturn(art);

        this.mockMvc.perform(post(this.baseUrl + "/artifacts").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.OK))
                .andExpect(jsonPath("$.message").value("Saved one"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(art.getName()))
                .andExpect(jsonPath("$.data.description").value(art.getDescription()))
                .andExpect(jsonPath("$.data.imgUrl").value(art.getImgUrl()));
    }

    @Test
    void testUpdateArtifactSuccess() throws Exception {
//        given
        ArtifactDto artifactDto = new ArtifactDto("123456789", "placide", "description", "image url", null);
        String json = this.objectMapper.writeValueAsString(artifactDto);

        Artifact artUpdate = new Artifact();

        artUpdate.setId("123456789");
        artUpdate.setName("John");
        artUpdate.setDescription("new or updated description");
        artUpdate.setImgUrl("image url");

        given(this.artifactService.update(eq("123456789"), Mockito.any(Artifact.class))).willReturn(artUpdate);

        this.mockMvc.perform(put(this.baseUrl + "/artifacts/123456789").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.OK))
                .andExpect(jsonPath("$.message").value("updated one"))
                .andExpect(jsonPath("$.data.id").value("123456789"))
                .andExpect(jsonPath("$.data.name").value(artUpdate.getName()))
                .andExpect(jsonPath("$.data.description").value(artUpdate.getDescription()))
                .andExpect(jsonPath("$.data.imgUrl").value(artUpdate.getImgUrl()));

    }

    @Test
    void testUpdateArtifactNotFoundId() throws Exception {
//        given
        ArtifactDto artifactDto = new ArtifactDto("123456789", "placide", "description", "image url", null);
        String json = this.objectMapper.writeValueAsString(artifactDto);

//        Artifact artUpdate = new Artifact();
//
//        artUpdate.setId("123456789");
//        artUpdate.setName("John");
//        artUpdate.setDescription("new or updated description");
//        artUpdate.setImgUrl("image url");

        given(this.artifactService.update(eq("123456789"), Mockito.any(Artifact.class))).willThrow(new ObjectNotFoundException("Artifact","123456789"));

        this.mockMvc.perform(put(this.baseUrl + "/artifacts/123456789").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find Artifact with Id " + "123456789"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteArtifactSuccess() throws Exception {
//        given
        doNothing().when(this.artifactService).delete("123456789");
//        when
        this.mockMvc.perform(delete(this.baseUrl + "/artifacts/123456789").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.OK))
                .andExpect(jsonPath("$.message").value("Deleted one"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteArtifactErrorWithNonExistentId() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("Artifact","123456789")).when(this.artifactService).delete("123456789");

        // When and then
        this.mockMvc.perform(delete(this.baseUrl+ "/artifacts/123456789").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not delete"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}