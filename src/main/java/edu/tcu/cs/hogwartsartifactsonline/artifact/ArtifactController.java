package edu.tcu.cs.hogwartsartifactsonline.artifact;

import edu.tcu.cs.hogwartsartifactsonline.artifact.conveter.ArtifactDtoToArtofactConverter;
import edu.tcu.cs.hogwartsartifactsonline.artifact.conveter.ArtifactToArtifactDtoConverter;
import edu.tcu.cs.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import edu.tcu.cs.hogwartsartifactsonline.system.Result;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/${spring.api.endpoint.base-url}/artifacts")
public class ArtifactController {

    private final ArtifactService artifactService;
    private final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;
    private final ArtifactDtoToArtofactConverter artifactDtoToArtofactConverter;

    public ArtifactController(ArtifactService artifactService, ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter, ArtifactDtoToArtofactConverter artifactDtoToArtofactConverter) {
        this.artifactService = artifactService;
        this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
        this.artifactDtoToArtofactConverter = artifactDtoToArtofactConverter;
    }

    @GetMapping("/{artifactId}")
    public Result findArtifactById(@PathVariable String artifactId){
        Artifact foundArtifact = this.artifactService.findById(artifactId);
        ArtifactDto artifactDto = this.artifactToArtifactDtoConverter.convert(foundArtifact);
        return new Result(true, StatusCode.OK, "Found one", artifactDto);
    }

    @GetMapping
    public Result findAllArtifacts(){
        List<Artifact> foundArtifacts = this.artifactService.findAll();
        List<ArtifactDto> collectedArtifactDto = foundArtifacts.stream()
                .map(foundArtifact -> this.artifactToArtifactDtoConverter
                        .convert(foundArtifact)).collect(Collectors.toList());
        return new Result(true, StatusCode.OK, "Found all artifacts", collectedArtifactDto);
    }

    @PostMapping
    public Result addArtifact(@Valid @RequestBody ArtifactDto artifactDto){
        Artifact newArtifact = this.artifactDtoToArtofactConverter.convert(artifactDto);
        Artifact savedArtifact = this.artifactService.save(newArtifact);

        ArtifactDto savedArtifactDto = this.artifactToArtifactDtoConverter.convert(savedArtifact);
        return new Result(true, StatusCode.OK, "Saved one", savedArtifactDto);
    }

    @PutMapping("/{artifactId}")
    public Result UpdateArtifact(@PathVariable String artifactId,@Valid @RequestBody ArtifactDto artifactDto){
        Artifact updateArtifact = this.artifactDtoToArtofactConverter.convert(artifactDto);
        Artifact updatedArtifact = this.artifactService.update(artifactId, updateArtifact);

        ArtifactDto updatedArtifactDto = this.artifactToArtifactDtoConverter.convert(updatedArtifact);

        return new Result(true, StatusCode.OK, "updated one", updatedArtifactDto);
    }

    @DeleteMapping("/{artifactID}")
    public Result deleteArtifact(@PathVariable String artifactID){
        this.artifactService.findById(artifactID);
        return new Result(true, StatusCode.OK, "Deleted one");
    }
}
