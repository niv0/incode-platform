package org.incode.domainapp.example.dom.spi.security.fixture;

import org.incode.domainapp.example.dom.spi.security.dom.demo.nontenanted.NonTenantedEntities;
import org.incode.domainapp.example.dom.spi.security.fixture.example.AllExampleEntities;
import org.incode.domainapp.example.dom.spi.security.fixture.roles.AllExampleRolesAndPermissions;
import org.incode.domainapp.example.dom.spi.security.fixture.roles.ExampleFixtureScriptsRoleAndPermissions;
import org.incode.domainapp.example.dom.spi.security.fixture.roles.ExampleGuestRoleAndPermissions;
import org.incode.domainapp.example.dom.spi.security.fixture.roles.ExampleHideNonTenantedEntityDescriptionRoleAndPermissions;
import org.incode.domainapp.example.dom.spi.security.fixture.roles.ExampleNoGuestRoleAndPremissions;
import org.incode.domainapp.example.dom.spi.security.fixture.roles.ExampleRegularRoleAndPermissions;
import org.incode.domainapp.example.dom.spi.security.fixture.tenancy.AllTenancies;
import org.incode.domainapp.example.dom.spi.security.fixture.userrole.BertTenantedUser_Has_ExampleRegularRole;
import org.incode.domainapp.example.dom.spi.security.fixture.userrole.BertTenantedUser_Has_IsisSecurityModuleRegularRole;
import org.incode.domainapp.example.dom.spi.security.fixture.userrole.BillNonTenantedUser_Has_ExampleRegularRole;
import org.incode.domainapp.example.dom.spi.security.fixture.userrole.BillNonTenantedUser_Has_IsisSecurityModuleRegularRole;
import org.incode.domainapp.example.dom.spi.security.fixture.userrole.BobUser_Has_ExampleHideEntityDescriptionRole;
import org.incode.domainapp.example.dom.spi.security.fixture.userrole.BobUser_Has_IsisSecurityAdminRole;
import org.incode.domainapp.example.dom.spi.security.fixture.userrole.ConflictedUser_Has_ExampleConflictingRoles;
import org.incode.domainapp.example.dom.spi.security.fixture.userrole.DickUser_Has_ExampleRegularRole;
import org.incode.domainapp.example.dom.spi.security.fixture.userrole.DickUser_Has_IsisSecurityModuleRegularRole;
import org.incode.domainapp.example.dom.spi.security.fixture.userrole.GuestUser_Has_ExampleGuestRole;
import org.incode.domainapp.example.dom.spi.security.fixture.userrole.GuestUser_Has_IsisSecurityModuleRegularRole;
import org.incode.domainapp.example.dom.spi.security.fixture.userrole.JoeUser_Has_ExampleGuestRole;
import org.incode.domainapp.example.dom.spi.security.fixture.userrole.JoeUser_Has_IsisSecurityModuleRegularRole;
import org.incode.domainapp.example.dom.spi.security.fixture.userrole.SvenUser_Has_IsisSecurityAdminRole;
import org.incode.domainapp.example.dom.spi.security.fixture.users.AllUsers;
import org.isisaddons.module.security.seed.SeedUsersAndRolesFixtureScript;
import org.apache.isis.applib.fixturescripts.DiscoverableFixtureScript;

public class SecurityModuleAppSetUp extends DiscoverableFixtureScript {

    @Override
    protected void execute(ExecutionContext executionContext) {

        executionContext.executeChild(this, new SecurityModuleAppTearDown());
        executionContext.executeChild(this, new SeedUsersAndRolesFixtureScript());

        // roles and perms
        executionContext.executeChild(this, new ExampleGuestRoleAndPermissions());
        executionContext.executeChild(this, new ExampleNoGuestRoleAndPremissions());
        executionContext.executeChild(this, new ExampleRegularRoleAndPermissions());
        executionContext.executeChild(this, new ExampleFixtureScriptsRoleAndPermissions());
        executionContext.executeChild(this, new ExampleHideNonTenantedEntityDescriptionRoleAndPermissions());

        executionContext.executeChild(this, new AllExampleRolesAndPermissions());

        // users, tenancies
        executionContext.executeChild(this, new AllTenancies());
        executionContext.executeChild(this, new AllUsers());

        // user/role
        executionContext.executeChild(this, new BobUser_Has_IsisSecurityAdminRole());
        executionContext.executeChild(this, new BobUser_Has_ExampleHideEntityDescriptionRole());

        executionContext.executeChild(this, new DickUser_Has_ExampleRegularRole());
        executionContext.executeChild(this, new DickUser_Has_IsisSecurityModuleRegularRole());

        executionContext.executeChild(this, new GuestUser_Has_ExampleGuestRole());
        executionContext.executeChild(this, new GuestUser_Has_IsisSecurityModuleRegularRole());

        executionContext.executeChild(this, new JoeUser_Has_ExampleGuestRole());
        executionContext.executeChild(this, new JoeUser_Has_IsisSecurityModuleRegularRole());

        executionContext.executeChild(this, new SvenUser_Has_IsisSecurityAdminRole());

        executionContext.executeChild(this, new ConflictedUser_Has_ExampleConflictingRoles());

        executionContext.executeChild(this, new BertTenantedUser_Has_ExampleRegularRole());
        executionContext.executeChild(this, new BertTenantedUser_Has_IsisSecurityModuleRegularRole());
        executionContext.executeChild(this, new BillNonTenantedUser_Has_ExampleRegularRole());
        executionContext.executeChild(this, new BillNonTenantedUser_Has_IsisSecurityModuleRegularRole());

        //  example entities
        executionContext.executeChild(this, new AllExampleEntities());

    }

    // //////////////////////////////////////

    @javax.inject.Inject
    private NonTenantedEntities exampleNonTenantedEntities;

}
