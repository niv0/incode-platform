package org.incode.domainapp.example.dom.dom.document.dom.applicability.rmf;

import java.util.List;

import javax.inject.Inject;

import org.incode.domainapp.example.dom.demo.dom.demowithnotes.DemoObjectWithNotes;
import org.incode.domainapp.example.dom.demo.dom.invoice.DemoInvoice;
import org.incode.example.document.dom.impl.applicability.RendererModelFactoryAbstract;
import org.incode.example.document.dom.impl.docs.Document;
import org.incode.example.document.dom.impl.docs.DocumentTemplate;
import org.incode.example.document.dom.impl.paperclips.Paperclip;
import org.incode.example.document.dom.impl.paperclips.PaperclipRepository;

import lombok.Value;

public class RenderModelFactoryOfDocumentAttachedToDemoInvoice extends RendererModelFactoryAbstract<Document> {

    public RenderModelFactoryOfDocumentAttachedToDemoInvoice() {
        super(Document.class);
    }

    @Override
    protected Object doNewRendererModel(
            final DocumentTemplate documentTemplate, final Document document) {

        final List<Paperclip> paperclips = paperclipRepository.findByDocument(document);
        final DemoInvoice demoInvoice =
                paperclips.stream().map(Paperclip::getAttachedTo)
                .filter(DemoInvoice.class::isInstance)
                .map(DemoInvoice.class::cast)
                .findFirst()
                .get(); // is safe to do this, because attachment advisor will have already run

        return new DataModel(demoInvoice.getCustomer(), demoInvoice);
    }


    @Inject
    PaperclipRepository paperclipRepository;

    @Value
    public static class DataModel {
        DemoObjectWithNotes demoCustomer;
        DemoInvoice demoInvoice;
    }


}

