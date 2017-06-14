package org.isisaddons.module.poly.fixture.dom.modules.fixedasset;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.InheritanceStrategy;
import com.google.common.eventbus.Subscribe;
import org.isisaddons.module.poly.fixture.dom.modules.comms.CommunicationChannelOwner;
import org.isisaddons.module.poly.fixture.dom.modules.comms.CommunicationChannelOwnerLink;
import org.apache.isis.applib.AbstractSubscriber;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.bookmark.BookmarkService;

@javax.jdo.annotations.PersistenceCapable()
@javax.jdo.annotations.Inheritance(
        strategy = InheritanceStrategy.NEW_TABLE)
@DomainObject(
        objectType = "fixedasset.CommunicationChannelOwnerLinkForFixedAsset"
)
public class CommunicationChannelOwnerLinkForFixedAsset extends CommunicationChannelOwnerLink {

    @DomainService(nature = NatureOfService.DOMAIN)
    public static class InstantiationSubscriber extends AbstractSubscriber {

        @Programmatic
        @Subscribe
        public void on(final CommunicationChannelOwnerLink.InstantiateEvent ev) {
            if(ev.getPolymorphicReference() instanceof FixedAsset) {
                ev.setSubtype(CommunicationChannelOwnerLinkForFixedAsset.class);
            }
        }
    }

    @Override
    public void setPolymorphicReference(final CommunicationChannelOwner polymorphicReference) {
        super.setPolymorphicReference(polymorphicReference);
        setFixedAsset((FixedAsset) polymorphicReference);
    }

    //region > party (property)
    private FixedAsset fixedAsset;

    @Column(
            allowsNull = "false",
            name = "fixedAsset_id"
    )
    @MemberOrder(sequence = "1")
    public FixedAsset getFixedAsset() {
        return fixedAsset;
    }

    public void setFixedAsset(final FixedAsset fixedAsset) {
        this.fixedAsset = fixedAsset;
    }
    //endregion


    //region > injected services
    @javax.inject.Inject
    private BookmarkService bookmarkService;
    //endregion

}
