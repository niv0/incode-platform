package org.incode.module.communications.demo.application.manifest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.module.communications.demo.module.fixture.scenario.DemoModuleFixture;

/**
 * Run the app but without setting up any fixtures.
 */
public class DemoAppAppManifestWithFixtures extends DemoAppAppManifest {

    /**
     * Fixtures to be installed.
     */
    @Override
    public List<Class<? extends FixtureScript>> getFixtures() {
        return Lists.newArrayList(
                DemoModuleFixture.class
        );
    }

    /**
     * Force fixtures to be loaded.
     */
    @Override
    public Map<String, String> getConfigurationProperties() {
        HashMap<String,String> props = Maps.newHashMap();
        props.put("isis.persistor.datanucleus.install-fixtures","true");
        return props;
    }

}
