;(function ($, undefined) {

    'use strict';

    if (typeof(WicketStuff) !== 'object') {
        window.WicketStuff = {};
    }

    if (typeof(WicketStuff.PDFJS) === 'object') {
        return;
    }

    WicketStuff.PDFJS = {
        Topic: {
            CURRENT_PAGE: 'Wicket.PDFJS.CurrentPage',
            TOTAL_PAGES: 'Wicket.PDFJS.TotalPages',
            NEXT_PAGE: 'Wicket.PDFJS.NextPage',
            PREVIOUS_PAGE: 'Wicket.PDFJS.PreviousPage',
            GOTO_PAGE: 'Wicket.PDFJS.GoToPage',
            ZOOM_IN: 'Wicket.PDFJS.ZoomIn',
            ZOOM_OUT: 'Wicket.PDFJS.ZoomOut',
            ZOOM_TO: 'Wicket.PDFJS.ZoomTo',
            CURRENT_ZOOM : 'Wicket.PDFJS.CurrentZoom',
            HEIGHT_TO: 'Wicket.PDFJS.HeightTo',
            CURRENT_HEIGHT : 'Wicket.PDFJS.CurrentHeight',
            PRINT : 'Wicket.PDFJS.Print',
            CURRENT_PRINT_PAGE : 'Wicket.PDFJS.CurrentPrintPage'
        },

        init: function (config) {

            // If absolute URL from the remote server is provided, configure the CORS
            // header on that server.
            var url = config.documentUrl;

            //
            // Disable workers to avoid yet another cross-origin issue (workers need
            // the URL of the script to be loaded, and dynamically loading a cross-origin
            // script does not work).
            PDFJS.disableWorker = config.workerDisabled || false;
            PDFJS.workerSrc = config.workerUrl;

            var pdfDoc = null,
                pageNum = config.initialPage || 1,
                pageRendering = false,
                pageNumPending = null,
                scale = config.initialScale || 0.8,
                autoScale = "",
                canvas = $('#'+config.canvasId)[0],
                ctx = canvas.getContext('2d'),
                abortingPrinting = false;

            var MIN_SCALE = 0.25;
            var MAX_SCALE = 10.0;
            var DEFAULT_SCALE_DELTA = 1.1;
            var MAX_AUTO_SCALE = 1.25;

            var container = $('#'+canvas.id).closest('.pdfPanel');

            canvas.height = config.initialHeight;
            canvas.width = container.width();

            $(window).on('resize', function(){
                  var win = $(this); //this = window

                  var container = $('#'+canvas.id).closest('.pdfPanel');

                  canvas.width = container.width();

                  queueRenderPage(pageNum);

            });


            /**
             * Get page info from document, resize canvas accordingly, and render page.
             * @param num Page number.
             */
            function renderPage(num) {
                pageRendering = true;
                // Using promise to fetch the page
                pdfDoc.getPage(num).then(function(page) {

                    var viewport = page.getViewport(1);
                    if (autoScale) {
                        scale = calculateAutoScale(viewport)
                    }

                    viewport = page.getViewport(scale);

                    // Render PDF page into canvas context
                    var renderContext = {
                        canvasContext: ctx,
                        viewport: viewport
                    };
                    var renderTask = page.render(renderContext);
                    // Wait for rendering to finish
                    renderTask.promise.then(function () {
                        pageRendering = false;
                        if (pageNumPending !== null) {
                            // New page rendering is pending
                                renderPage(pageNumPending);
                                pageNumPending = null;
                            }
                        });
                        Wicket.Event.publish(WicketStuff.PDFJS.Topic.CURRENT_PAGE, pageNum, {"canvasId": config.canvasId});
                        Wicket.Event.publish(WicketStuff.PDFJS.Topic.CURRENT_ZOOM, autoScale || scale.toFixed(2), {"canvasId": config.canvasId});
                    Wicket.Event.publish(WicketStuff.PDFJS.Topic.CURRENT_HEIGHT, canvas.height, {"canvasId": config.canvasId});
                });
            }

            function printDocument() {
                pageRendering = true;
                abortingPrinting = false;
                var pages = pdfDoc.numPages;

                var pc = $('<div/>');
                pc.attr('id', 'pdf-js-print-container');
                $('body').append(pc);

                for (var i = 0; i < pages; i++){
                   var wrapper = $('<div/>');
                   pc.append(wrapper);
                }

                for (var i = 1; i <= pages; i++){
                    printPage(i, pages);
                }
            }

            function cleanUp(error) {
                if (abortingPrinting) { return; }

                if (error) {
                    abortingPrinting = true;
                    var printError = -1; // use -1 to indicate error printing
                    Wicket.Event.publish(WicketStuff.PDFJS.Topic.CURRENT_PRINT_PAGE, printError, {"canvasId": config.canvasId});
                }

                pageRendering = false;
                var body = $('body').removeAttr('pdf-js-printing');
                $('#pdf-js-print-container').remove();
            }


            function printPage(num, total) {

                // Using promise to fetch the page
                pdfDoc.getPage(num).then(function(page) {
                    if (abortingPrinting) { return; }

                    var viewport = page.getViewport(1);
                    var offScreenCanvas = $('<canvas/>')[0];
                    offScreenCanvas.width = viewport.width;
                    offScreenCanvas.height = viewport.height;
                    var offScreenCanvasCtx = offScreenCanvas.getContext('2d');

                    // Render PDF page into canvas context
                    var renderContext = {
                        canvasContext: offScreenCanvasCtx,
                        viewport: viewport
                    };

                    var renderTask = page.render(renderContext);
                    // Wait for rendering to finish
                    renderTask.promise.then(function () {
                        var img = $('<img/>')[0];
                        $(img).width(viewport.width);
                        $(img).height(viewport.height);

                        img.onload = function() {
                            if (abortingPrinting) { return; }

                            // image loaded so append to appropriate div.
                            var wrapper = $('#pdf-js-print-container > div:nth-child(' + num + ')');
                            var renderedPages = 0;

                            if (wrapper.length) {
                                wrapper.append(img);
                                // get count of loaded images so that once all loaded we can print
                                renderedPages = $('#pdf-js-print-container img').length;
                                // publish for progress bar
                                Wicket.Event.publish(WicketStuff.PDFJS.Topic.CURRENT_PRINT_PAGE, renderedPages, {"canvasId": config.canvasId});
                            }

                            if (renderedPages === total) {
                                try {
                                   $('body').attr('pdf-js-printing', true);
                                   print.call(window);
                                }
                                catch(e) {
                                   cleanUp(true);
                                }
                                finally{
                                   //  will do nothing if we have already aborted
                                   cleanUp(false);
                                }
                            } else if (renderedPages === 0 || renderedPages > total) {
                                // should be at least one so abort printing
                                // don't see how can ever be greater but treat as error as well;
                                cleanUp(true);
                            }
                        }

                        img.onerror = function() {
                            if (abortingPrinting) { return; }
                            cleanUp(true);
                        }

                        img.src = offScreenCanvas.toDataURL();
                    });
                   });
            }


            /**
             * If another page rendering in progress, waits until the rendering is
             * finished. Otherwise, executes rendering immediately.
             */
            function queueRenderPage(num) {
                if (pageRendering) {
                    pageNumPending = num;
                } else {
                    renderPage(num);
                }
            }

            function zoomInOnce() {
                var newScale = (scale * DEFAULT_SCALE_DELTA).toFixed(2);
                newScale = Math.ceil(newScale * 10) / 10;
                newScale = Math.min(MAX_SCALE, newScale);
                return newScale;
            }

            function zoomOutOnce() {
                var newScale = (scale / DEFAULT_SCALE_DELTA).toFixed(2);
                newScale = Math.floor(newScale * 10) / 10;
                newScale = Math.max(MIN_SCALE, newScale);
                return newScale;
            }

            function renderIfRescaled(newScale, newAutoScale){
                if (newAutoScale && newAutoScale !== autoScale) {
                    autoScale = newAutoScale;
                    queueRenderPage(pageNum);
                }
                else if (newScale !== scale){
                    autoScale = "";
                    scale = newScale;
                    queueRenderPage(pageNum);
                }
            }

            function calculateAutoScale(currentPage) {

                // todo may need to make more sohpisticated cf viewer
                //var hPadding = this.isInPresentationMode || this.removePageBorders ? 0 : SCROLLBAR_PADDING;
                //var vPadding = this.isInPresentationMode || this.removePageBorders ? 0 : VERTICAL_PADDING;
                //var pageWidthScale = (this.container.clientWidth - hPadding) / currentPage.width * currentPage.scale;
                //var pageHeightScale = (this.container.clientHeight - vPadding) / currentPage.height * currentPage.scale;

                var pageWidthScale = canvas.width / currentPage.width;
                var pageHeightScale = canvas.height / currentPage.height;

                var newScale;
                switch (autoScale) {
                case 'page-actual':
                    newScale = 1;
                    break;
                case 'page-width':
                    newScale = pageWidthScale;
                    break;
                case 'page-height':
                    newScale = pageHeightScale;
                    break;
                case 'page-fit':
                    newScale = Math.min(pageWidthScale, pageHeightScale);
                    break;
                case 'auto':
                    var isLandscape = currentPage.width > currentPage.height;
                    var horizontalScale = isLandscape ? Math.min(pageHeightScale, pageWidthScale) : pageWidthScale;
                    newScale = Math.min(MAX_AUTO_SCALE, horizontalScale);
                    break;
                default:
                    return scale;
               }

                return newScale;
            }

            /**
             * Displays previous page.
             */
            Wicket.Event.subscribe(WicketStuff.PDFJS.Topic.PREVIOUS_PAGE, function (jqEvent, data) {
                if (config.canvasId !== data.canvasId || pageNum <= 1) {
                    return;
                }
                pageNum--;
                queueRenderPage(pageNum);
            });

            /**
             * Displays next page.
             */
            Wicket.Event.subscribe(WicketStuff.PDFJS.Topic.NEXT_PAGE, function (jqEvent, data) {
                if (config.canvasId !== data.canvasId || pageNum >= pdfDoc.numPages) {
                    return;
                }
                pageNum++;
                queueRenderPage(pageNum);
            });

            /**
             * Displays selected page
             */
            Wicket.Event.subscribe(WicketStuff.PDFJS.Topic.GOTO_PAGE, function (jqEvent, data) {
                if (config.canvasId !== data.canvasId) {
                    return;
                }
                if (!data.page || data.page > pdfDoc.numPages || data.page < 1 || data.page === pageNum) {
                    return;
                }
                pageNum = data.page;
                queueRenderPage(pageNum);
            });

            /**
             * Zoom in current page
             */
            Wicket.Event.subscribe(WicketStuff.PDFJS.Topic.ZOOM_IN, function (jqEvent, data) {
                 if (config.canvasId !== data.canvasId) {
                    return;
                 }
                 renderIfRescaled(zoomInOnce());
            });

             /**
              * Zoom out current page
              */
             Wicket.Event.subscribe(WicketStuff.PDFJS.Topic.ZOOM_OUT, function (jqEvent, data) {
                  if (config.canvasId !== data.canvasId) {
                     return;
                  }
                  renderIfRescaled(zoomOutOnce());
             });

             Wicket.Event.subscribe(WicketStuff.PDFJS.Topic.ZOOM_TO, function (jqEvent, data) {
                  if (config.canvasId !== data.canvasId || !data.scale) {
                      return;
                  }
                  var rawScale = parseFloat(data.scale);
                  var newScale;
                  var newAutoScale;
                  if (isNaN(rawScale)){
                      newAutoScale = data.scale;
                  }
                  else {
                      newScale = parseFloat(rawScale.toFixed(2));
                      if (newScale >= MAX_SCALE || newScale <= MIN_SCALE) {
                          return;
                      }
                  }

                  // TODO: reinstate
                  //updateScale(newScale);

                  renderIfRescaled(newScale, newAutoScale);
              });

             Wicket.Event.subscribe(WicketStuff.PDFJS.Topic.HEIGHT_TO, function (jqEvent, data) {
                  if (config.canvasId !== data.canvasId || !data.height) {
                      return;
                  }
                  var previousHeight = canvas.height;
                  var newHeight = data.height;

                  canvas.height = newHeight;

                  // TODO: reinstate
                  // updateHeight(newHeight);

                  queueRenderPage(pageNum);


              });

            /**
             * Print document
             */
            Wicket.Event.subscribe(WicketStuff.PDFJS.Topic.PRINT, function (jqEvent, data) {
                 if (config.canvasId !== data.canvasId) {
                    return;
                 }

                 if (pageRendering) {
                    // rendering or printing
                    return;
                 }

                 printDocument();
            });

            /**
             * Asynchronously downloads PDF.
             */
            PDFJS.getDocument(url).then(function (pdfDoc_) {
                pdfDoc = pdfDoc_;
                Wicket.Event.publish(WicketStuff.PDFJS.Topic.TOTAL_PAGES, pdfDoc.numPages, {"canvasId": config.canvasId});
                renderPage(pageNum);
            });
        }
    };
})(jQuery);
