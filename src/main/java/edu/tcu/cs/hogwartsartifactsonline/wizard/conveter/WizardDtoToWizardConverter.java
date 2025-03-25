package edu.tcu.cs.hogwartsartifactsonline.wizard.conveter;

import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.WizardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class WizardDtoToWizardConverter implements Converter<WizardDto, Wizard> {

    @Override
    public Wizard convert(WizardDto source) {
        Wizard wizard = new Wizard();
        wizard.setName(source.name());
        wizard.setId(source.id());

        return wizard;
    }
}
