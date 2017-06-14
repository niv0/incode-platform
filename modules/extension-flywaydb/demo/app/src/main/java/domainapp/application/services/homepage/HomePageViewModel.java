package domainapp.application.services.homepage;

import java.util.List;

import org.apache.isis.applib.annotation.ViewModel;
import org.apache.isis.applib.services.i18n.TranslatableString;

import domainapp.modules.simple.dom.impl.FlywayDemoObject;
import domainapp.modules.simple.dom.impl.FlywayDemoObjectRepository;

@ViewModel
public class HomePageViewModel {

    //region > title
    public TranslatableString title() {
        return TranslatableString.tr("{num} objects", "num", getObjects().size());
    }
    //endregion

    //region > object (collection)
    @org.apache.isis.applib.annotation.HomePage
    public List<FlywayDemoObject> getObjects() {
        return flywayDemoObjectRepository.listAll();
    }
    //endregion

    //region > injected services

    @javax.inject.Inject
    FlywayDemoObjectRepository flywayDemoObjectRepository;

    //endregion
}
