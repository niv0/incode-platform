package org.incode.domainapp.example.dom.dom.docfragment.fixture;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.domainapp.example.dom.demo.fixture.data.DemoCustomerData;
import org.incode.domainapp.example.dom.demo.fixture.data.DemoInvoiceData;
import org.incode.domainapp.example.dom.dom.docfragment.fixture.data.DocFragmentData;

public class DemoCustomer_and_DemoInvoice_and_DocFragment_createSome extends FixtureScript {

    @Override
    protected void execute(final ExecutionContext ec) {

        ec.executeChild(this, new DemoCustomerData.PersistScript());
        ec.executeChild(this, new DemoInvoiceData.PersistScript());
        ec.executeChild(this, new DocFragmentData.PersistScript());

    }
}
