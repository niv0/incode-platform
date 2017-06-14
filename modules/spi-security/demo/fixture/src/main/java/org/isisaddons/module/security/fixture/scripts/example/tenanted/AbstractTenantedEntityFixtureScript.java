package org.isisaddons.module.security.fixture.scripts.example.tenanted;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancyRepository;
import org.isisaddons.module.security.fixture.dom.example.tenanted.TenantedEntities;
import org.isisaddons.module.security.fixture.dom.example.tenanted.TenantedEntity;

public abstract class AbstractTenantedEntityFixtureScript extends FixtureScript {

    protected TenantedEntity create(
            final String name,
            final String tenancyPath,
            final ExecutionContext executionContext) {
        final ApplicationTenancy tenancy = applicationTenancyRepository.findByPath(tenancyPath);
        final TenantedEntity entity = exampleTenantedEntities.create(name, tenancy);
        executionContext.addResult(this, name, entity);
        return entity;
    }

    @javax.inject.Inject
    private TenantedEntities exampleTenantedEntities;

    @javax.inject.Inject
    private ApplicationTenancyRepository applicationTenancyRepository;

}
