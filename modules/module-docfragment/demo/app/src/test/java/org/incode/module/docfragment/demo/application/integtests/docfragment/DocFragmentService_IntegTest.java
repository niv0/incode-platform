package org.incode.module.docfragment.demo.application.integtests.docfragment;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.xactn.TransactionService;

import org.incode.module.docfragment.demo.application.fixture.scenarios.DemoAppFixture;
import org.incode.module.docfragment.demo.application.integtests.DocFragmentModuleIntegTestAbstract;
import org.incode.module.docfragment.demo.module.dom.impl.invoices.DemoInvoice;
import org.incode.module.docfragment.demo.module.fixture.invoices.DemoInvoiceData;

import static org.assertj.core.api.Assertions.assertThat;

public class DocFragmentService_IntegTest extends DocFragmentModuleIntegTestAbstract {

    @Inject
    FixtureScripts fixtureScripts;
    @Inject
    TransactionService transactionService;

    @Inject
    ServiceRegistry2 serviceRegistry2;


    @Before
    public void setUp() throws Exception {
        // given
        fixtureScripts.runFixtureScript(new DemoAppFixture(), null);
        transactionService.nextTransaction();
    }


    public static class Render2 extends DocFragmentService_IntegTest {

        @Test
        public void happy_case() throws Exception {
            // given
            final DemoInvoice invoice1 = DemoInvoiceData.Invoice1.findUsing(serviceRegistry2);
            assertThat(invoice1.getRendered()).isNull();

            // when
            invoice1.render(invoice1.default0Render());

            // then
            assertThat(invoice1.getRendered()).isEqualTo("The invoice will be due on the 31-Jan-2017, payable in 30 days");
        }
    }



}