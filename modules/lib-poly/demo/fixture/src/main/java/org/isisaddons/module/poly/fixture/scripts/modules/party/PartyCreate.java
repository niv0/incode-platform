package org.isisaddons.module.poly.fixture.scripts.modules.party;

import org.isisaddons.module.poly.fixture.dom.modules.party.Parties;
import org.isisaddons.module.poly.fixture.dom.modules.party.Party;

import org.apache.isis.applib.fixturescripts.FixtureScript;

public class PartyCreate extends FixtureScript {

    //region > name (input)
    private String name;
    public String getName() {
        return name;
    }

    public PartyCreate setName(final String name) {
        this.name = name;
        return this;
    }
    //endregion

    //region > party (output)
    private Party party;

    /**
     * The created party (output).
     */
    public Party getParty() {
        return party;
    }
    //endregion

    @Override
    protected void execute(final ExecutionContext ec) {

        final String name = checkParam("name", ec, String.class);

        this.party = parties.create(name);

        // also make available to UI
        ec.addResult(this, party);
    }

    @javax.inject.Inject
    private Parties parties;

}
