package org.isisaddons.module.excel.fixture.scripts;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.joda.time.LocalDate;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.clock.Clock;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.module.excel.dom.ExcelFixture;
import org.isisaddons.module.excel.dom.ExcelFixtureRowHandler;
import org.isisaddons.module.excel.fixture.dom.ExcelModuleDemoToDoItem;
import org.isisaddons.module.excel.fixture.dom.ExcelModuleDemoToDoItems;

import lombok.Getter;
import lombok.Setter;

public class ExcelModuleDemoToDoItemRowHandler implements ExcelFixtureRowHandler {

    @Getter @Setter
    private String description;

    @Getter @Setter
    private ExcelModuleDemoToDoItem.Category category;

    @Getter @Setter
    private ExcelModuleDemoToDoItem.Subcategory subcategory;

    @Getter @Setter
    private Integer daysFromToday;

    @Getter @Setter
    private BigDecimal cost;

    @Override
    public List<Object> handleRow(
            final FixtureScript.ExecutionContext executionContext,
            final ExcelFixture excelFixture,
            final Object previousRow) {
        final ExcelModuleDemoToDoItemRowHandler previous = (ExcelModuleDemoToDoItemRowHandler) previousRow;
        if(category == null) {
            category = previous.category;
        }
        if(subcategory == null) {
            subcategory = previous.subcategory;
        }

        final LocalDate dueBy = daysFromToday(daysFromToday);
        final String user = executionContext.getParameter("user");
        final String username = user != null && user.length() > 0 ? user : container.getUser().getName();
        ExcelModuleDemoToDoItem toDoItem = toDoItemRepository.findByDescription(description);
        if(toDoItem != null) {
            toDoItem.setCategory(category);
            toDoItem.setSubcategory(subcategory);
            toDoItem.setDueBy(dueBy);
            toDoItem.setCost(cost);
            toDoItem.setOwnedBy(username);
        } else {
            toDoItem = toDoItemRepository.newToDo(description, category, subcategory, username, dueBy, cost);
        }
        executionContext.addResult(excelFixture, toDoItem);
        return Collections.<Object>singletonList(toDoItem);
    }

    private static LocalDate daysFromToday(final Integer i) {
        if(i == null) {
            return null;
        }
        final LocalDate date = new LocalDate(Clock.getTimeAsDateTime());
        return date.plusDays(i);
    }


    @javax.inject.Inject
    private ExcelModuleDemoToDoItems toDoItemRepository;

    @javax.inject.Inject
    private DomainObjectContainer container;
}
