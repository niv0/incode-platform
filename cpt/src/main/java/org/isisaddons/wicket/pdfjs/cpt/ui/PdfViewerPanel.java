/*
 *  Copyright 2013~2014 Dan Haywood
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.isisaddons.wicket.pdfjs.cpt.ui;

import org.apache.isis.applib.value.Blob;
import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.viewer.wicket.model.models.ScalarModel;
import org.apache.isis.viewer.wicket.ui.panels.PanelAbstract;
import org.apache.wicket.IResourceListener;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.handler.resource.ResourceRequestHandler;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.resource.JQueryPluginResourceReference;
import org.isisaddons.wicket.pdfjs.cpt.applib.PdfViewerFromAnnotationFacet;
import org.wicketstuff.pdfjs.PdfJsConfig;
import org.wicketstuff.pdfjs.PdfJsPanel;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;

/**
 *
 */
class PdfViewerPanel extends PanelAbstract<ScalarModel> implements IResourceListener {
    private static final long serialVersionUID = 1L;

    private static final String ID_SCALAR_NAME = "scalarName";
    private static final String ID_SCALAR_VALUE = "scalarValue";
    private static final String ID_FEEDBACK = "feedback";

    PdfViewerPanel(String id, ScalarModel scalarModel) {
        super(id, scalarModel);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        this.buildGui();
    }

    private void buildGui() {
        final ScalarModel scalarModel = this.getModel();
        String name = scalarModel.getName();
        Label scalarName = new Label(ID_SCALAR_NAME, name);
        addOrReplace(scalarName);

        final ObjectAdapter adapter = scalarModel.getObject();
        if (adapter != null) {
            PdfViewerFromAnnotationFacet pdfViewerFacet = scalarModel.getTypeOfSpecification().getFacet(PdfViewerFromAnnotationFacet.class);
            PdfJsConfig config = pdfViewerFacet != null ? pdfViewerFacet.getConfig() : new PdfJsConfig();
            config.withDocumentUrl(urlFor(IResourceListener.INTERFACE, null));
            PdfJsPanel pdfJsPanel = new PdfJsPanel(ID_SCALAR_VALUE, config);
            MarkupContainer prevPageButton = createComponent("prevPage", config);
            MarkupContainer nextPageButton = createComponent("nextPage", config);
            MarkupContainer currentPageLabel = createComponent("currentPage", config);
            MarkupContainer totalPagesLabel = createComponent("totalPages", config);
            addOrReplace(pdfJsPanel, prevPageButton, nextPageButton, currentPageLabel, totalPagesLabel);
            addOrReplace(new NotificationPanel(ID_FEEDBACK, pdfJsPanel, new ComponentFeedbackMessageFilter(pdfJsPanel)));
        } else {
            permanentlyHide(ID_SCALAR_VALUE, ID_FEEDBACK);
        }

    }

    private MarkupContainer createComponent(final String id, final PdfJsConfig config) {
        return new WebMarkupContainer(id) {
            @Override
            protected void onComponentTag(final ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("data-canvas-id", config.getCanvasId());
            }
        };
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);

        response.render(JavaScriptHeaderItem.forReference(
                new JQueryPluginResourceReference(PdfViewerPanel.class, "PdfViewerPanel.js")));
    }

    @Override
    public void onResourceRequested() {
        final ObjectAdapter adapter = getModel().getObject();
        if (adapter != null) {
            Blob pdfBlob = (Blob) adapter.getObject();
            final byte[] bytes = pdfBlob.getBytes();
            final ByteArrayResource resource = new ByteArrayResource("application/pdf", bytes);
            final ResourceRequestHandler handler = new ResourceRequestHandler(resource, null);
            getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
        }
    }
}
