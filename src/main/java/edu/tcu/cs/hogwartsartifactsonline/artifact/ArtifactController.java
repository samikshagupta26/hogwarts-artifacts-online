package edu.tcu.cs.hogwartsartifactsonline.artifact;


import edu.tcu.cs.hogwartsartifactsonline.artifact.conveter.ArtifactDtoToArtofactConverter;
import edu.tcu.cs.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import edu.tcu.cs.hogwartsartifactsonline.system.Result;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${spring.api.endpoint.base-url}/artifacts")
public class ArtifactController {

    private final ArtifactService artifactService;

    private final edu.tcu.cs.hogwartsartifactsonline.artifact.conveter.ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;


    private final ArtifactDtoToArtofactConverter artifactDtoToArtifactConverter;

    public ArtifactController(ArtifactService artifactService, edu.tcu.cs.hogwartsartifactsonline.artifact.conveter.ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter, ArtifactDtoToArtofactConverter artifactDtoToArtifactConverter) {
        this.artifactService = artifactService;
        this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
        this.artifactDtoToArtifactConverter = artifactDtoToArtifactConverter;
    }


    @GetMapping("/{artifactId}")
    public Result findArtifactById(@PathVariable String artifactId){
        Artifact foundArtifact = this.artifactService.findById(artifactId);
        ArtifactDto artifactDto = this.artifactToArtifactDtoConverter.convert(foundArtifact);
        return new Result(true, StatusCode.OK, "Find One Success", artifactDto);
    }

    @GetMapping
    public Result findAllArtifacts(){
        List<Artifact> foundArtifacts = this.artifactService.findAll();
        // Convert foundArtifacts to a list of artifactDtos
        List<ArtifactDto> artifactDtos = foundArtifacts.stream()
                .map(this.artifactToArtifactDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.OK, "Find All Success", artifactDtos);
    }

    @PostMapping
    public Result addArtifact(@Valid @RequestBody ArtifactDto artifactDto){
        // Convert artifactDto to artifact
        Artifact newArtifact = this.artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact savedArtifact = this.artifactService.save(newArtifact);
        ArtifactDto savedArtifactDto = this.artifactToArtifactDtoConverter.convert(savedArtifact);
        return new Result(true, StatusCode.OK, "Add Success", savedArtifactDto) ;
    }

    @PutMapping("/{artifactId}")
    public Result updateArtifact(@PathVariable String artifactId, @Valid @RequestBody ArtifactDto artifactDto){
        Artifact update = this.artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact updatedArtifact = this.artifactService.update(artifactId, update);
        ArtifactDto updatedArtifactDto = this.artifactToArtifactDtoConverter.convert(updatedArtifact);
        return new Result(true, StatusCode.OK, "Update Success", updatedArtifactDto);
    }

    @DeleteMapping("/{artifactId}")
    public Result deleteArtifact(@PathVariable String artifactId){
        this.artifactService.delete(artifactId);
        return new Result(true, StatusCode.OK, "Delete Success");
    }

}