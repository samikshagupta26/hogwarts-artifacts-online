package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifact.ArtifactRepository;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {

    @Mock
    private WizardRepository wizardRepository;

    @Mock
    private ArtifactRepository artifactRepository;

    @InjectMocks
    private WizardService wizardService;

    List<Wizard> wizards;

    @BeforeEach
    void setUp() {
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");

        this.wizards = new ArrayList<>();
        this.wizards.add(w1);
        this.wizards.add(w2);
        this.wizards.add(w3);
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void findAllSuccess() {
        given(this.wizardRepository.findAll()).willReturn(this.wizards);

        // When. Act on the target behavior. Act steps should cover the method to be tested.
        List<Wizard> actualWizards = this.wizardService.findAllWizards();

        // Then. Assert expected outcomes.
        assertThat(actualWizards.size()).isEqualTo(this.wizards.size());

        // Verify wizardRepository.findAll() is called exactly once.
        verify(this.wizardRepository, times(1)).findAll();
    }

    @Test
    void findByIdSuccess() {
        Wizard w = new Wizard();
        w.setId(1);
        w.setName("Albus Dumbledore");

        given(this.wizardRepository.findById(1)).willReturn(Optional.of(w)); // Define the behavior of the mock object.

        // When. Act on the target behavior. Act steps should cover the method to be tested.
        Wizard returnedWizard = this.wizardService.findById(1);

        // Then. Assert expected outcomes.
        assertThat(returnedWizard.getId()).isEqualTo(w.getId());
        assertThat(returnedWizard.getName()).isEqualTo(w.getName());
        verify(this.wizardRepository, times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        given(this.wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> {
            Wizard returnedWizard = this.wizardService.findById(1);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException.class)
                .hasMessage("Could not find Wizard with Id 1");
        verify(this.wizardRepository, times(1)).findById(Mockito.any(Integer.class));
    }

    @Test
    void testSaveSuccess() {
        // Given
        Wizard newWizard = new Wizard();
        newWizard.setName("Hermione Granger");

        given(this.wizardRepository.save(newWizard)).willReturn(newWizard);

        // When
        Wizard returnedWizard = this.wizardService.save(newWizard);

        // Then
        assertThat(returnedWizard.getName()).isEqualTo(newWizard.getName());
        verify(this.wizardRepository, times(1)).save(newWizard);
    }

    @Test
    void testUpdateSuccess() {
        // Given
        Wizard oldWizard = new Wizard();
        oldWizard.setId(1);
        oldWizard.setName("Albus Dumbledore");

        Wizard update = new Wizard();
        update.setName("Albus Dumbledore - update");

        given(this.wizardRepository.findById(1)).willReturn(Optional.of(oldWizard));
        given(this.wizardRepository.save(oldWizard)).willReturn(oldWizard);

        // When
        Wizard updatedWizard = this.wizardService.update(1, update);

        // Then
        assertThat(updatedWizard.getId()).isEqualTo(1);
        assertThat(updatedWizard.getName()).isEqualTo(update.getName());
        verify(this.wizardRepository, times(1)).findById(1);
        verify(this.wizardRepository, times(1)).save(oldWizard);
    }

    @Test
    void testUpdateNotFound() {
        // Given
        Wizard update = new Wizard();
        update.setName("Albus Dumbledore - update");

        given(this.wizardRepository.findById(1)).willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.update(1, update);
        });

        // Then
        verify(this.wizardRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteSuccess() {
        // Given
        Wizard wizard = new Wizard();
        wizard.setId(1);
        wizard.setName("Albus Dumbledore");

        given(this.wizardRepository.findById(1)).willReturn(Optional.of(wizard));
        doNothing().when(this.wizardRepository).deleteById(1);

        // When
        this.wizardService.delete(1);

        // Then
        verify(this.wizardRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound() {
        // Given
        given(this.wizardRepository.findById(1)).willReturn(Optional.empty());

        // When
        assertThrows(edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException.class, () -> {
            this.wizardService.delete(1);
        });

        // Then
        verify(this.wizardRepository, times(1)).findById(1);
    }


    @Test
    void testAssignArtifactSuccess() {
//        given
        Artifact artifact = new Artifact();
        artifact.setId("132435");
        artifact.setName("Passer");
        artifact.setDescription("Description for passer");
        artifact.setImgUrl("image url");

        Wizard wizard1 = new Wizard();
        wizard1.setName("Albus Dumbledore");
        wizard1.setId(1);
        wizard1.addArtifact(artifact);

        Wizard wizard2 = new Wizard();
        wizard2.setName("given");
        wizard2.setId(2);

        given(artifactRepository.findById("132435")).willReturn(Optional.of(artifact));
        given(this.wizardRepository.findById(2)).willReturn(Optional.of(wizard2));
//        when
        this.wizardService.assignArtifact(2,"132435");

//        then
        assertThat(artifact.getOwner().getId()).isEqualTo(2);
        assertThat(wizard2.getArtifacts()).contains(artifact);

    }

    @Test
    void testAssignArtifactNoArtifactId() {
//
        given(this.artifactRepository.findById("132435")).willReturn(Optional.empty());
//        when
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.assignArtifact(2,"132435");
        });

//        then
        assertThat(thrown)
        .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find Artifact with Id 132435");

    }

    @Test
    void testAssignArtifactNoWizardId() {
//        given
        Artifact artifact = new Artifact();
        artifact.setId("132435");
        artifact.setName("Passer");
        artifact.setDescription("Description for passer");
        artifact.setImgUrl("image url");

        Wizard wizard1 = new Wizard();
        wizard1.setName("Albus Dumbledore");
        wizard1.setId(1);
        wizard1.addArtifact(artifact);



        given(artifactRepository.findById("132435")).willReturn(Optional.of(artifact));
        given(this.wizardRepository.findById(2)).willReturn(Optional.empty());
//        when
//        this.wizardService.assignArtifact(2,"132435");
        Throwable thrown = assertThrows(ObjectNotFoundException.class, ()->{
            this.wizardService.assignArtifact(2,"132435");}
        );
//        then
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
                        .hasMessage("Could not find Wizard with Id 2");
        assertThat(artifact.getOwner().getId()).isEqualTo(1);
    }
}