package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.system.Result;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.wizard.conveter.WizardDtoToWizardConverter;
import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.WizardDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import edu.tcu.cs.hogwartsartifactsonline.wizard.conveter.WizardToWizardDtoConverter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/${spring.api.endpoint.base-url}/wizards")
public class WizardController {

    private final WizardService wizardService;
    private final WizardDtoToWizardConverter wizardDtoToWizardConverter;
    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;



    public WizardController(WizardService wizardService, WizardDtoToWizardConverter wizardDtoToWizardConverter, WizardToWizardDtoConverter wizardToWizardDtoConverter) {
        this.wizardService = wizardService;
        this.wizardDtoToWizardConverter = wizardDtoToWizardConverter;
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
    }

    @GetMapping
    public Result findAllWizards() {
        List<Wizard> foundWizards = wizardService.findAllWizards();

        List<WizardDto> wizardDtos = foundWizards.stream()
                .map(this.wizardToWizardDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.OK, "All wizards", wizardDtos);
    }

    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable Integer wizardId) {
        Wizard foundWizard = this.wizardService.findById(wizardId);
        WizardDto wizardDto = this.wizardToWizardDtoConverter.convert(foundWizard);

        return new Result(true, StatusCode.OK, "Found a wizard", wizardDto);
    }

    @PostMapping
    public Result addWizard(@Valid @RequestBody WizardDto wizardDto) {
        Wizard newWizard = this.wizardDtoToWizardConverter.convert(wizardDto);
        Wizard savedWizard = this.wizardService.save(newWizard);
        WizardDto savedWizardDto = this.wizardToWizardDtoConverter.convert(savedWizard);

        return new Result(true, StatusCode.OK, "New wizard", savedWizardDto);
    }

    @PostMapping("/{wizardId}")
    public Result updateWizard(@PathVariable Integer wizardId, @Valid @RequestBody WizardDto wizardDto) {
        Wizard update = this.wizardDtoToWizardConverter.convert(wizardDto);
        Wizard updatedWizard = this.wizardService.update(wizardId, update);
        WizardDto updatedWizarDto = this.wizardToWizardDtoConverter.convert(updatedWizard);

        return new Result(true, StatusCode.OK, "Updated wizard", updatedWizarDto);
    }

    @DeleteMapping("/{wizardId}")
    public Result deleteWizard(@PathVariable Integer wizardId) {
        this.wizardService.delete(wizardId);
        return new Result(true, StatusCode.OK, "Deleted wizard");
    }

    @PutMapping("/{wizardId}/artifacts/{artifactID}")
    public Result assignArtifact(@PathVariable Integer wizardId, @PathVariable String artifactID) {
        this.wizardService.assignArtifact(wizardId, artifactID);
        return new Result(true, StatusCode.OK, "Assigned artifact");
    }
}
