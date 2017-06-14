package org.isisaddons.module.poly.fixture.dom.modules.casemgmt;

import java.util.List;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        repositoryFor = Case.class
)
@DomainServiceLayout(menuOrder = "30")
public class Cases {

    //region > listAll (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public List<Case> listAll() {
        return container.allInstances(Case.class);
    }
    //endregion

    //region > create (action)

    @MemberOrder(sequence = "3")
    public Case create(
            final @ParameterLayout(named = "Name") String name) {
        final Case aCase = container.newTransientInstance(Case.class);
        aCase.setName(name);

        container.persistIfNotAlready(aCase);
        return aCase;
    }

    //endregion

    //region > injected services
    @javax.inject.Inject
    DomainObjectContainer container;
    //endregion
}
