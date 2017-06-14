package org.isisaddons.module.excel.fixture.scripts;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.fixturespec.FixtureScriptsSpecification;
import org.apache.isis.applib.services.fixturespec.FixtureScriptsSpecificationProvider;

@DomainService(
        nature = NatureOfService.DOMAIN
)
@DomainServiceLayout(
        named = "Prototyping",
        menuBar = DomainServiceLayout.MenuBar.SECONDARY,
        menuOrder = "499"
)
public class ExcelModuleDemoFixtureScriptsSpecificationProvider implements FixtureScriptsSpecificationProvider {

    @Override
    public FixtureScriptsSpecification getSpecification() {
        return FixtureScriptsSpecification
                .builder(ExcelModuleDemoFixtureScriptsSpecificationProvider.class)
                .withRecreate(RecreateToDoItems.class)
                .with(FixtureScripts.MultipleExecutionStrategy.EXECUTE_ONCE_BY_VALUE)
                .withRunScriptDefault(RecreateToDoItems.class)
                .withRunScriptDropDown(FixtureScriptsSpecification.DropDownPolicy.CHOICES)
                .build();
    }


}
