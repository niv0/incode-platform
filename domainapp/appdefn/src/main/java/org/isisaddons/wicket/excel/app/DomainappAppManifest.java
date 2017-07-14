package org.isisaddons.wicket.excel.app;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import org.apache.isis.applib.AppManifest;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.module.audit.AuditModule;
import org.isisaddons.module.command.CommandModule;
import org.isisaddons.module.docx.DocxModule;
import org.isisaddons.module.excel.ExcelModule;
import org.isisaddons.module.fakedata.FakeDataModule;
import org.isisaddons.module.freemarker.dom.FreeMarkerModule;
import org.isisaddons.module.pdfbox.dom.PdfBoxModule;
import org.isisaddons.module.publishmq.PublishMqModule;
import org.isisaddons.module.security.SecurityModule;
import org.isisaddons.module.servletapi.ServletApiModule;
import org.isisaddons.module.sessionlogger.SessionLoggerModule;
import org.isisaddons.module.settings.SettingsModule;
import org.isisaddons.module.stringinterpolator.StringInterpolatorModule;
import org.isisaddons.module.tags.TagsModule;
import org.isisaddons.module.togglz.TogglzModule;
import org.isisaddons.module.xdocreport.dom.XDocReportModule;
import org.isisaddons.wicket.excel.cpt.ui.ExcelUiModule;
import org.isisaddons.wicket.fullcalendar2.cpt.ui.FullCalendar2UiModule;
import org.isisaddons.wicket.gmap3.cpt.applib.Gmap3ApplibModule;
import org.isisaddons.wicket.gmap3.cpt.service.Gmap3ServiceModule;
import org.isisaddons.wicket.gmap3.cpt.ui.Gmap3UiModule;
import org.isisaddons.wicket.pdfjs.cpt.PdfjsCptModule;
import org.isisaddons.wicket.summernote.cpt.ui.SummernoteUiModule;
import org.isisaddons.wicket.wickedcharts.cpt.ui.WickedChartsUiModule;

import org.incode.module.alias.dom.AliasModule;
import org.incode.module.classification.dom.ClassificationModule;
import org.incode.module.commchannel.dom.CommChannelModule;
import org.incode.module.communications.dom.CommunicationsModuleDomModule;
import org.incode.module.country.dom.CountryModule;
import org.incode.module.docfragment.dom.DocFragmentModuleDomModule;
import org.incode.module.docrendering.freemarker.dom.FreemarkerDocRenderingModule;
import org.incode.module.docrendering.stringinterpolator.dom.StringInterpolatorDocRenderingModule;
import org.incode.module.docrendering.xdocreport.dom.XDocReportDocRenderingModule;
import org.incode.module.document.dom.DocumentModule;
import org.incode.module.note.dom.NoteModule;

import domainapp.modules.simple.dom.ExampleDomSubmodule;

public class DomainappAppManifest implements AppManifest {

    @Override
    public List<Class<?>> getModules() {
        return Arrays.asList(

                ExampleDomSubmodule.class,

                // extensions
                TogglzModule.class,

                // lib
                FreemarkerDocRenderingModule.class,
                StringInterpolatorDocRenderingModule.class,
                XDocReportDocRenderingModule.class,
                DocxModule.class,
                ExcelModule.class,
                FakeDataModule.class,
                FreeMarkerModule.class,
                PdfBoxModule.class,
                ServletApiModule.class,
                StringInterpolatorModule.class,
                XDocReportModule.class,

                // modules
                AliasModule.class,
                ClassificationModule.class,
                CommChannelModule.class,
                CommunicationsModuleDomModule.class,
                CountryModule.class,
                DocFragmentModuleDomModule.class,
                DocumentModule.class,
                NoteModule.class,
                SettingsModule.class,
                TagsModule.class,

                // spi
                AuditModule.class,
                CommandModule.class,
                PublishMqModule.class,
                SecurityModule.class,
                SessionLoggerModule.class,

                // cpt (wicket ui)
                ExcelUiModule.class,
                FullCalendar2UiModule.class,
                Gmap3ApplibModule.class,
                Gmap3ServiceModule.class,
                Gmap3UiModule.class,
                PdfjsCptModule.class,
                SummernoteUiModule.class,
                WickedChartsUiModule.class

                // deprecated
                // PolyModule.class

        );
    }


    @Override
    public List<Class<?>> getAdditionalServices() { return null; }
    @Override
    public String getAuthenticationMechanism() { return null; }
    @Override
    public String getAuthorizationMechanism() { return null; }
    @Override
    public List<Class<? extends FixtureScript>> getFixtures() { return null; }
    @Override
    public Map<String, String> getConfigurationProperties() {
        HashMap<String,String> props = Maps.newHashMap();
        props.put("isis.viewer.wicket.rememberMe.cookieKey","DomainAppEncryptionKey");
        return props;
    }

}
