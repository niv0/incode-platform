package org.isisaddons.module.audit.fixture.scripts;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.services.fixturespec.FixtureScriptsSpecification;
import org.apache.isis.applib.services.fixturespec.FixtureScriptsSpecificationProvider;

@DomainService(nature = NatureOfService.DOMAIN)
public class AuditDemoAppFixtureScriptsSpecificationProvider implements FixtureScriptsSpecificationProvider {

    @Override
    public FixtureScriptsSpecification getSpecification() {
        return FixtureScriptsSpecification.builder(AuditDemoAppFixtureScriptsSpecificationProvider.class)
                .withRunScriptDropDown(FixtureScriptsSpecification.DropDownPolicy.CHOICES)
                .withRunScriptDefault(AuditDemoAppFixture.class)
                .build();
    }


}
