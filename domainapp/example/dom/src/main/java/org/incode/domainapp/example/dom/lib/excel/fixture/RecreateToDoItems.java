package org.incode.domainapp.example.dom.lib.excel.fixture;

import org.apache.isis.applib.fixturescripts.DiscoverableFixtureScript;

import org.incode.domainapp.example.dom.demo.fixture.todoitems.CreateAllToDoItems;
import org.incode.domainapp.example.dom.demo.fixture.todoitems.DeleteToDoItemsForUser;

public class RecreateToDoItems extends DiscoverableFixtureScript {

    private final String user;

    public RecreateToDoItems() {
        this(null);
    }

    public RecreateToDoItems(String ownedBy) {
        this.user = ownedBy;
    }

    @Override
    public void execute(ExecutionContext executionContext) {

        final String ownedBy = this.user != null ? this.user : getContainer().getUser().getName();

        executionContext.executeChild(this, new DeleteToDoItemsForUser(ownedBy));
        executionContext.executeChild(this, new CreateAllToDoItems(ownedBy));

        getContainer().flush();
    }

}