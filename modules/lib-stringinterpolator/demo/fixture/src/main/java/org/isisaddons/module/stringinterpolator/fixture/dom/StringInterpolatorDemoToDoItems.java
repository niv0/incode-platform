package org.isisaddons.module.stringinterpolator.fixture.dom;

import java.util.List;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;

@DomainService
@DomainServiceLayout(named = "ToDos")
public class StringInterpolatorDemoToDoItems {


    //region > identification

    public String getId() {
        return "toDoItems";
    }

    public String iconName() {
        return "ToDoItem";
    }

    //endregion

    //region > newToDo (action)

    @MemberOrder(sequence = "40")
    public StringInterpolatorDemoToDoItem newToDo(
            @ParameterLayout(named = "Description") @Parameter(regexPattern = "\\w[@&:\\-\\,\\.\\+ \\w]*")
            final String description,
            @ParameterLayout(named = "Documentation page")
            final String documentationPage) {

        final StringInterpolatorDemoToDoItem toDoItem = container.newTransientInstance(StringInterpolatorDemoToDoItem.class);
        toDoItem.setDescription(description);
        toDoItem.setDocumentationPage(documentationPage);

        container.persist(toDoItem);
        container.flush();

        return toDoItem;
    }
    //endregion

    //region > allToDos (action)


    @Action(
            semantics = SemanticsOf.SAFE
    )
    @MemberOrder(sequence = "50")
    public List<StringInterpolatorDemoToDoItem> allToDos() {
        return container.allInstances(StringInterpolatorDemoToDoItem.class);
    }

    //endregion

    //region > injected services

    @javax.inject.Inject
    private DomainObjectContainer container;

    //endregion

}
