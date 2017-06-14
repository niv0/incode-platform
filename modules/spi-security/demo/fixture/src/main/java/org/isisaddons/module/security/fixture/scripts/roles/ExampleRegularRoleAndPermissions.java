package org.isisaddons.module.security.fixture.scripts.roles;

import org.isisaddons.module.security.dom.permission.ApplicationPermissionMode;
import org.isisaddons.module.security.dom.permission.ApplicationPermissionRule;
import org.isisaddons.module.security.fixture.dom.example.nontenanted.NonTenantedEntity;
import org.isisaddons.module.security.fixture.dom.example.tenanted.TenantedEntity;
import org.isisaddons.module.security.seed.scripts.AbstractRoleAndPermissionsFixtureScript;

public class ExampleRegularRoleAndPermissions extends AbstractRoleAndPermissionsFixtureScript {

    public static final String ROLE_NAME = "example-regular-role";

    public ExampleRegularRoleAndPermissions() {
        super(ROLE_NAME, "Read/write access to example dom");
    }

    @Override
    protected void execute(ExecutionContext executionContext) {
        newPackagePermissions(
                ApplicationPermissionRule.ALLOW,
                ApplicationPermissionMode.CHANGING,
                NonTenantedEntity.class.getPackage().getName(),
                TenantedEntity.class.getPackage().getName()
                );
    }

}
