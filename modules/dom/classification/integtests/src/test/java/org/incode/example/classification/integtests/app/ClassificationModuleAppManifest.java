package org.incode.example.classification.integtests.app;

import org.apache.isis.applib.AppManifestAbstract;

import org.incode.example.classification.dom.ClassificationModule;
import org.incode.domainapp.example.dom.dom.classification.ExampleDomModuleClassificationModule;

/**
 * Bootstrap the application.
 */
public class ClassificationModuleAppManifest extends AppManifestAbstract {

    public static final Builder BUILDER = Builder.forModules(
            ClassificationModule.class, // dom module
            ExampleDomModuleClassificationModule.class,
            ClassificationAppModule.class
    );

    public ClassificationModuleAppManifest() {
        super(BUILDER);
    }


}
