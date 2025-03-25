package edu.tcu.cs.hogwartsartifactsonline.artifact;

import edu.tcu.cs.hogwartsartifactsonline.artifact.utils.IdWorker;
import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {

    @Mock
    ArtifactRepository artifactRepository;

    @Mock
    IdWorker idWorker;

    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifacts;



    @BeforeEach
    void setUp() {
        Artifact a1 = new Artifact();
        a1.setName("artifact1");
        a1.setId("12345");
        a1.setImgUrl("image url");
        a1.setDescription("description");

        Artifact a2 = new Artifact();
        a2.setName("artifact2");
        a2.setId("23456");
        a2.setImgUrl("image url");
        a2.setDescription("description");

        this.artifacts = new ArrayList<>();
        this.artifacts.add(a1);
        this.artifacts.add(a2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
//        given
        Artifact a1 = new Artifact();
        a1.setId("12345");
        a1.setName("test");
        a1.setDescription("test description");
        a1.setImgUrl("test image url");

        Wizard w1 = new Wizard();
        w1.setName("Uour");
        w1.setId(3);

        a1.setOwner(w1);

        given(artifactRepository.findById("12345")).willReturn(Optional.of(a1));

//        when
        Artifact returnedArtifact = artifactService.findById("12345");
//        then
        assertThat(returnedArtifact.getId()).isEqualTo(a1.getId());
        assertThat(returnedArtifact.getName()).isEqualTo(a1.getName());
        assertThat(returnedArtifact.getDescription()).isEqualTo(a1.getDescription());
        assertThat(returnedArtifact.getImgUrl()).isEqualTo(a1.getImgUrl());
        verify(artifactRepository, times(1)).findById("12345");
    }

    @Test
    void testFindByIdFailure() {
//        given
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());
//        when
        Throwable thrown = catchThrowable(()->{
           Artifact returnedArtifact = artifactService.findById("12345");
        });

//        then
        assertThat(thrown).isInstanceOf(ArtifactNotFoundException.class)
                .hasMessage("Could not find artifact with Id 12345");
        verify(artifactRepository, times(1)).findById("12345");
    }

    @Test
    void testFindAllSuccess() {
//        given
        given(artifactRepository.findAll()).willReturn(artifacts).willReturn(this.artifacts);
//        when
        List<Artifact> returnedArtifacts = artifactService.findAll();
//        then
        assertThat(returnedArtifacts.size()).isEqualTo(artifacts.size());
        verify(artifactRepository, times(1)).findAll();
    }

    @Test
    void testSaveSuccess() {
//        given
        Artifact a1 = new Artifact();

        a1.setName("test");
        a1.setDescription("test description");
        a1.setImgUrl("test image url");

        given(idWorker.nextId()).willReturn(123456L);
        given(artifactRepository.save(a1)).willReturn(a1);

//        when
        Artifact savedArtifact = artifactService.save(a1);
//        then
        assertThat(savedArtifact.getId()).isEqualTo("123456");
        assertThat(savedArtifact.getName()).isEqualTo(a1.getName());
        assertThat(savedArtifact.getDescription()).isEqualTo(a1.getDescription());
        assertThat(savedArtifact.getImgUrl()).isEqualTo(a1.getImgUrl());

        verify(artifactRepository, times(1)).save(a1);
    }

    @Test
    void testUpdateSuccess() {
//        given
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("12345");
        oldArtifact.setName("test");
        oldArtifact.setDescription("test description");
        oldArtifact.setImgUrl("test image url");

        Artifact updatedArtifact = new Artifact();
        updatedArtifact.setId("23456");
        updatedArtifact.setName("test");
        updatedArtifact.setDescription("Updated test description");
        updatedArtifact.setImgUrl("test image url");

        given(artifactRepository.findById("12345")).willReturn(Optional.of(oldArtifact));
        given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact);
//        when
        Artifact updatedArtifactagain = artifactService.update(oldArtifact.getId(), updatedArtifact);
//        then
        assertThat(updatedArtifactagain.getId()).isEqualTo(oldArtifact.getId());
        assertThat(updatedArtifactagain.getDescription()).isEqualTo(oldArtifact.getDescription());

        verify(artifactRepository, times(1)).findById("12345");
        verify(artifactRepository, times(1)).save(updatedArtifactagain);
    }

    @Test
    void testUpdateIdNotFound() {
//        given
        Artifact oldArtifact = new Artifact();
//        oldArtifact.setId("12345");
        oldArtifact.setName("test");
        oldArtifact.setDescription("test description");
        oldArtifact.setImgUrl("test image url");

        given(artifactRepository.findById("1234567")).willReturn(Optional.empty());

//        when
        assertThrows(ArtifactNotFoundException.class, ()->{
            artifactService.update("1234567", oldArtifact);
        });
//        then
        verify(artifactRepository, times(1)).findById("1234567");
    }


    @Test
    void testDeleteSuccess() {
//        given
        Artifact a1 = new Artifact();
        a1.setId("12345");
        a1.setName("test");
        a1.setDescription("test description");
        a1.setImgUrl("test image url");

        given(artifactRepository.findById("12345")).willReturn(Optional.of(a1));
        doNothing().when(artifactRepository).deleteById("12345");
//        when
        artifactService.delete("12345");
//        then
        verify(artifactRepository, times(1)).deleteById("12345");
    }

    @Test
    void testDeleteIdNotFound() {
//        Artifact a1 = new Artifact();
//        a1.setId("12345");
//        a1.setName("test");
//        a1.setDescription("test description");
//        a1.setImgUrl("test image url");

        given(artifactRepository.findById("12345")).willReturn(Optional.empty());
//        doNothing().when(artifactRepository).deleteById("12345");
//        when
//        artifactService.delete("12345");
//        then
        assertThrows(ArtifactNotFoundException.class, ()->{
            artifactService.delete("12345");
        });

        verify(artifactRepository, times(1)).findById("12345");
    }
}