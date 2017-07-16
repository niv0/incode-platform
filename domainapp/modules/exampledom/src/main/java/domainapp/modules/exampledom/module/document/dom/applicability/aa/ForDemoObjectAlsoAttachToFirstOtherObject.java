package domainapp.modules.exampledom.module.document.dom.applicability.aa;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import org.apache.isis.applib.services.repository.RepositoryService;

import org.incode.module.document.dom.impl.applicability.AttachmentAdvisor;
import org.incode.module.document.dom.impl.applicability.AttachmentAdvisorAbstract;
import org.incode.module.document.dom.impl.docs.Document;
import org.incode.module.document.dom.impl.docs.DocumentTemplate;
import domainapp.modules.exampledom.module.document.dom.demo.DemoObject;
import domainapp.modules.exampledom.module.document.dom.demo2.OtherObject;

public class ForDemoObjectAlsoAttachToFirstOtherObject extends
        AttachmentAdvisorAbstract<DemoObject> {

    public ForDemoObjectAlsoAttachToFirstOtherObject() {
        super(DemoObject.class);
    }

    @Override
    protected List<AttachmentAdvisor.PaperclipSpec> doAdvise(
            final DocumentTemplate documentTemplate,
            final DemoObject demoObject,
            final Document createdDocument) {
        return determinePaperclipSpecs(demoObject, createdDocument);
    }

    protected List<AttachmentAdvisor.PaperclipSpec> determinePaperclipSpecs(
            final Object domainObject,
            final Document createdDocument) {

        final List<AttachmentAdvisor.PaperclipSpec> attachTo = Lists.newArrayList();

        attachTo.add(new AttachmentAdvisor.PaperclipSpec(null, domainObject, createdDocument));

        // also attaches to first 'otherObject' (if any)
        final Optional<OtherObject> firstOtherObjectIfAny = repositoryService.allInstances(OtherObject.class).stream().findFirst();
        if(firstOtherObjectIfAny.isPresent()) {
            attachTo.add(new AttachmentAdvisor.PaperclipSpec("other", firstOtherObjectIfAny.get(), createdDocument));
        }

        return attachTo;
    }


    @Inject
    RepositoryService repositoryService;

}