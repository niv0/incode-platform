package org.isisaddons.module.tags.integtests.tags;

import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.jdo.Query;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.isisaddons.module.tags.dom.Tag;
import org.isisaddons.module.tags.dom.Tags;
import org.isisaddons.module.tags.fixture.dom.ExampleTaggableEntities;
import org.isisaddons.module.tags.fixture.dom.ExampleTaggableEntity;
import org.isisaddons.module.tags.fixture.scripts.ExampleTaggableEntitiesTearDownFixture;
import org.isisaddons.module.tags.fixture.scripts.entities.Bar_Pepsi_Drink;
import org.isisaddons.module.tags.fixture.scripts.entities.Baz_McDonalds_FastFood;
import org.isisaddons.module.tags.fixture.scripts.entities.Bip_CocaCola_Drink;
import org.isisaddons.module.tags.fixture.scripts.entities.Bop_Levis_Clothing;
import org.isisaddons.module.tags.integtests.TagsModuleAppIntegTest;
import org.junit.Before;
import org.junit.Test;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class TagsIntegTest extends TagsModuleAppIntegTest {

    ExampleTaggableEntity entity;

    @Before
    public void setUpData() throws Exception {
        scenarioExecution().install(
                new ExampleTaggableEntitiesTearDownFixture(),
                new Bip_CocaCola_Drink(),
                new Bar_Pepsi_Drink(),
                new Baz_McDonalds_FastFood(),
                new Bop_Levis_Clothing()
                );
    }

    @Inject
    ExampleTaggableEntities exampleTaggableEntities;

    @Inject
    Tags tags;

    @Inject
    IsisJdoSupport isisJdoSupport;

    @Before
    public void setUp() throws Exception {
        final List<ExampleTaggableEntity> all = wrap(exampleTaggableEntities).listAll();
        assertThat(all.size(), is(4));

        entity = all.get(0);
        assertThat(entity.getBrand(), is("Coca Cola"));
        assertThat(entity.getSector(), is("Drink"));
    }

    public static class TagsFor extends TagsIntegTest {

        @Test
        public void whenOthersUseTheTag() throws Exception {

            // given
            assertThat(entity.choicesSector(), containsInAnyOrder("Drink", "Fast food", "Clothing"));
            assertThat(uniqueValuesOf(tagsWithKey("Sector")), hasSize(3));
            assertThat(entity.getSector(), is("Drink")); // 2 using Drink

            // when
            entity.setSector("Fast food");
            nextTransaction();

            // then updated...
            assertThat(entity.getSector(), is("Fast food"));
            // but no tags removed
            assertThat(entity.choicesSector(), containsInAnyOrder("Drink", "Fast food", "Clothing"));
            assertThat(uniqueValuesOf(tagsWithKey("Sector")), hasSize(3));
        }

        @Test
        public void whenNoOthersUseTheTag() throws Exception {

            // given
            assertThat(entity.choicesBrand(), containsInAnyOrder("Coca Cola", "Levi's", "McDonalds", "Pepsi"));
            assertThat(uniqueValuesOf(tagsWithKey("Brand")), hasSize(4));

            // when
            entity.setBrand("Pepsi");
            nextTransaction();

            // then updated...
            assertThat(entity.getBrand(), is("Pepsi"));
            // then tag removed
            assertThat(entity.choicesBrand(), containsInAnyOrder("Levi's", "McDonalds", "Pepsi"));
            assertThat(uniqueValuesOf(tagsWithKey("Brand")), hasSize(3));
        }

        private List<Tag> tagsWithKey(String key) {
            final Query query = isisJdoSupport.getJdoPersistenceManager().newQuery("SELECT FROM " + Tag.class.getName() + " where key == :key");
            return (List<Tag>) query.execute(key);
        }

        private static Set<String> uniqueValuesOf(List<Tag> tags) {
            return Sets.newTreeSet(Iterables.transform(tags, Tag.Functions.GET_VALUE));
        }

    }



}