module cmm045.a  {
    __viewContext.ready(function () {
        var screenModel = new viewmodel.ScreenModel();
        screenModel.start().done(function() {
            __viewContext.bind(screenModel);
        });
    });

    let handleBasics = {
        position: {
            top: 0,
            left: 0
        },
        size: {
            width: 5,
            height: undefined,
        },
        minWidth: 15,
    };

    export function dragResize($headerTable, $bodyTable) {

        let $headerWrapper = $headerTable.parent();
        let $headerCols = $headerTable.find("col");
        let $bodyCols = $bodyTable.find("col");

        // for handles position absolute
        $headerWrapper.css("position", "relative");

        handleBasics.size.height = $headerTable.height();

        let columnsCount = $headerCols.length;
        for (let i = 0; i < columnsCount; i++) {

            let $headerCol = $headerCols.eq(i);
            let $bodyCol = $bodyCols.eq(i);

            let $handle = $("<div/>")
                .addClass("resize-handle")
                .css({
                    position: "absolute",
                    top: handleBasics.position.top,
                    background: "#330",
                    width: handleBasics.size.width,
                    height: handleBasics.size.height,
                    cursor: "col-resize",
                    opacity: 0,
                })
                .trigger("selectstart", () => false)
                .appendTo($headerWrapper);
            
            setupHandleEvent($handle, $headerCol, $bodyCol, (newWidth, $hcol, $bcol) => {
                applyResize(newWidth, $hcol, $bcol, $handles, $headerCols)
            });
        }
        
        let $handles = $headerWrapper.children(".resize-handle");

        resetPositionHandles($handles, $headerCols);
    }

    function resetPositionHandles($handles, $cols) {

        let columnsCount = $cols.length;
        let totalWidth = handleBasics.position.left;

        for (let i = 0; i < columnsCount; i++) {
            let $col = $cols.eq(i);

            totalWidth += $col.width();
            let left = handleBasics.position.left + totalWidth - handleBasics.size.width / 2;
            
            if (i === columnsCount - 1) {
                left -= 1;
            }

            $handles.eq(i).css("left", left);
        }
    }

    function setupHandleEvent($handle, $headerCol, $bodyCol, applyer: (newWidth: number, $headerCol, $bodyCol) => void) {

        $handle.bind("mousedown.resize", e1 => {
            let startMouseX = e1.screenX;
            let startColumnWidth = $headerCol.width();

            let startHandleLeft = parseInt($handle.css("left"), 10);

            $handle.css("opacity", 0.5);
            $("body, table").addClass("resizing-now");

            $(window).bind("mousemove.resize", e => {
                let deltaX = e.screenX - startMouseX;

                if (startColumnWidth + deltaX < handleBasics.minWidth) {
                    return;
                }

                $handle.css("left", startHandleLeft + deltaX);
            });

            $(window).bind("mouseup.resize", (e) => {
                $(window).unbind("mousemove.resize");
                $(window).unbind("mouseup.resize");
                $("body, table").removeClass("resizing-now");
                $handle.css("opacity", 0.0);
                
                let deltaX = e.screenX - startMouseX;
                let newWidth = Math.max(startColumnWidth + deltaX, handleBasics.minWidth);
                
                applyer(newWidth, $headerCol, $bodyCol);
            });

        });
    }

    function applyResize(newWidth, $targetHeaderCol, $targetBodyCol, $handles, $headerCols) {

        $targetHeaderCol.width(newWidth);
        $targetBodyCol.width(newWidth);

        resetColumnsSize($headerCols, $targetHeaderCol.closest("table"), $targetBodyCol.closest("table"));
        resetPositionHandles($handles, $headerCols);
    }

    export function resetColumnsSize($cols, $headerTable, $bodyTable) {

        let tableWidth = 0;
        $cols.each(function () {
            tableWidth += $(this).width();
        });

        $headerTable.width(tableWidth);
        $bodyTable.width(tableWidth);
    }

    export module fixedTable {

        export function init($originTable: JQuery, options?: any): JQuery {

            $originTable.addClass("fixed-table");
            let $colgroup = $originTable.find("colgroup");
            let $thead = $originTable.find("thead");
            let setting = $.extend({ height: "auto" }, options);
            let viewWidth = setting.width;
            let width = 0;
            $colgroup.find("col").each(function() {
                width += Number($(this).attr("width").replace(/px/gi, ''));
            });
            width++;
            if(nts.uk.util.isNullOrUndefined(viewWidth)){
                viewWidth = width;
            }
            
            let $container = $("<div class='nts-fixed-table cf'/>");
            $originTable.after($container);
            
            
            let $headerContainer = $("<div class='nts-fixed-header-container ui-iggrid nts-fixed-header'/>").css({"max-width": viewWidth});
            let $headerWrapper = $("<div class='nts-fixed-header-wrapper'/>").width(width);
            let $headerTable = $("<table class='fixed-table'></table>");
            
            
            $headerTable.append($colgroup.clone()).append($thead);
            $headerTable.appendTo($headerWrapper);
            $headerContainer.append($headerWrapper);
            
            let $header = $("<div>");
            $headerContainer.appendTo($header);
            $header.appendTo($container);
            $header.height($headerContainer.height());
            
            $originTable.addClass("nts-fixed-body-table");
            let $bodyContainer = $("<div class='nts-fixed-body-container ui-iggrid'/>"); 
            let $bodyWrapper = $("<div class='nts-fixed-body-wrapper'/>");
            let bodyHeight: any = "auto";
            if (setting.height !== "auto") {
                $bodyContainer.css("max-width", viewWidth);
                bodyHeight = Math.floor(Number(setting.height.toString().replace(/px/mi)) - $headerTable.find("thead").outerHeight());
            }
            
            $bodyContainer.scroll(function(evt, ui) {
                $headerContainer.scrollLeft($bodyContainer.scrollLeft());
            });

            $bodyWrapper.width(width).height(bodyHeight);
            $bodyWrapper.append($originTable);
            $bodyContainer.append($bodyWrapper);
            $container.append($bodyContainer);
            if (setting.height !== "auto" && bodyHeight < $originTable.height()){
                if(/Edge/.test(navigator.userAgent)){
                    $bodyContainer.css("padding-right", "12px");
                }else {
                    $bodyContainer.css("padding-right", "17px");
                }   
            }
        }
    }
}
