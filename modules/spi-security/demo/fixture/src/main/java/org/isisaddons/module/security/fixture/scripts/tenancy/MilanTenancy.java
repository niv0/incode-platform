package org.isisaddons.module.security.fixture.scripts.tenancy;

public class MilanTenancy extends AbstractTenancyFixtureScript {

    public static final String TENANCY_NAME = "Milan (Italy)";
    public static final String TENANCY_PATH = "/it/mil";

    @Override
    protected void execute(ExecutionContext executionContext) {

        // prereqs
        executionContext.executeChild(this, new ItalyTenancy());

        create(TENANCY_NAME, TENANCY_PATH, ItalyTenancy.TENANCY_PATH, executionContext);
    }

}
