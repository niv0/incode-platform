package org.incode.domainapp.example.dom.dom.communications.fixture;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;

import org.incode.domainapp.example.dom.demo.fixture.teardown.DemoModuleTearDown;
import org.incode.module.communications.fixture.teardown.CommunicationModule_tearDown;
import org.incode.module.country.fixture.teardown.CountryModule_tearDown;
import org.incode.module.document.fixture.teardown.DocumentModule_tearDown;

public class DemoObjectWithNotes_and_DemoInvoice2_and_docs_and_comms_tearDown extends FixtureScript {

    @Override
    protected void execute(ExecutionContext executionContext) {

        // communication & commchannel links
        isisJdoSupport.executeUpdate("delete from \"exampleDomCommunications\".\"PaperclipForDemoInvoice2\"");

        isisJdoSupport.executeUpdate("delete from \"exampleDomCommunications\".\"CommunicationChannelOwnerLinkForDemoObjectWithNotes\"");

        // comms, doc, country
        executionContext.executeChild(this, new CommunicationModule_tearDown());
        executionContext.executeChild(this, new DocumentModule_tearDown());
        executionContext.executeChild(this, new CountryModule_tearDown());

        // demo objects
        executionContext.executeChild(this, new DemoModuleTearDown());

    }


    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;

}
