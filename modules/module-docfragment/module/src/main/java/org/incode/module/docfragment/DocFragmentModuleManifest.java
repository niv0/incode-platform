package org.incode.module.docfragment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.isis.applib.AppManifest;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.module.docfragment.dom.DocFragmentModuleDomModule;

/**
 * Used by <code>isis-maven-plugin</code> (build-time validation of the module) and also by module-level integration tests.
 */
public class DocFragmentModuleManifest implements AppManifest {

    @Override
    public List<Class<?>> getModules() {
        return Arrays.<Class<?>>asList(
                DocFragmentModuleDomModule.class
        );
    }

    @Override
    public List<Class<?>> getAdditionalServices() {
        return Collections.emptyList();
    }

    @Override
    public String getAuthenticationMechanism() {
        return null;
    }

    @Override
    public String getAuthorizationMechanism() {
        return null;
    }

    @Override
    public List<Class<? extends FixtureScript>> getFixtures() {
        return null;
    }

    /**
     * No overrides.
     */
    @Override
    public Map<String, String> getConfigurationProperties() {
        return null;
    }

}
