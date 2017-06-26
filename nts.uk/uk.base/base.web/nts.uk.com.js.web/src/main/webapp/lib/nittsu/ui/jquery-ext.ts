/// <reference path="../reference.ts"/>

interface JQuery {
    ntsPopup(args: any): JQuery;
    ntsError(action: string, param?: any): any;
    ntsListBox(action: string, param?: any): any;
    ntsGridList(action: string, param?: any): any;
    ntsTreeView(action: string, param?: any): any;
    ntsGridListFeature(feature: string, action: string, ...params: any[]): any;
    ntsWizard(action: string, param?: any): any;
    ntsUserGuide(action?: string, param?: any): any;
    ntsSideBar(action?: string, param?: any): any;
    ntsEditor(action?: string, param?: any): any;
    setupSearchScroll(controlType: string, param?: any): any;
}

module nts.uk.ui.jqueryExtentions {
    module ntsFileUpload {
        export interface FileUploadOption {
            stereoType: string;
            onSuccess?(): any;
            onFail?(): any;
        }

        $.fn.ntsFileUpload = function(option: FileUploadOption) {
            let dfd = $.Deferred();
            let file: JQuery;
            if ($(this).find("input[type='file']").length == 0) {
                file = $(this)[0].files;
            } else {
                file = $(this).find("input[type='file']")[0].files;
            }
            if (file) {
                var formData = new FormData();
                formData.append("stereotype", option.stereoType);
                // HTML file input, chosen by user
                formData.append("userfile", file[0]);
                formData.append("filename", file[0].name);
                if (file[0]) {
                    return nts.uk.request.uploadFile(formData, option);
                } else {
                    dfd.reject({ message: "please select file", messageId: "-1" });
                    return dfd.promise();
                }
            } else {
                dfd.reject({ messageId: "0", message: "can not find control" });
            }
            return dfd.promise();
        }
    }

    module ntsError {
        var DATA_HAS_ERROR = 'hasError';
        var DATA_GET_ERROR = 'getError';

        $.fn.ntsError = function(action: string, message: any): any {
            var $control = $(this);
            if (action === DATA_HAS_ERROR) {
                return _.some($control, c => hasError($(c)));
            } else if (action === DATA_GET_ERROR) {
                return getErrorByElement($control.first());        
            }else {
                $control.each(function(index) {
                    var $item = $(this);
                    $item = processErrorOnItem($item, message, action);
                });
                return $control;
            }

        }

        //function for set and clear error
        function processErrorOnItem($control: JQuery, message: any, action: string) {
            switch (action) {
                case 'set':
                    return setError($control, message);
                case 'clear':
                    return clearErrors($control);
            }
        }
        
        function getErrorByElement($control: JQuery) {
            return ui.errors.getErrorByElement($control);
        }

        function setError($control: JQuery, message: any) {
            $control.data(DATA_HAS_ERROR, true);
            ui.errors.add({
                location: $control.data('name') || "",
                message: message,
                $control: $control
            });
            $control.parent().addClass('error');
            return $control;
        }

        function clearErrors($control: JQuery) {
            $control.data(DATA_HAS_ERROR, false);
            ui.errors.removeByElement($control);
            $control.parent().removeClass('error');
            return $control;
        }

        function hasError($control: JQuery) {
            return $control.data(DATA_HAS_ERROR) === true;
        }
    }

    module ntsPopup {
        let DATA_INSTANCE_NAME = 'nts-popup-panel';

        $.fn.ntsPopup = function() {
            if (arguments.length === 1) {
                var p: any = arguments[0];
                if (_.isPlainObject(p)) {
                    return init.apply(this, arguments);
                }
            }

            if (typeof arguments[0] === 'string') {
                return handleMethod.apply(this, arguments);
            }
        }

        function init(param): JQuery {
            var popup = new NtsPopupPanel($(this), param.position);
            var dismissible = param.dismissible === false;
            _.defer(function() {
                if (!dismissible) {
                    $(window).mousedown(function(e) {
                        if ($(e.target).closest(popup.$panel).length === 0) {
                            popup.hide();
                        }
                    });
                }
            });

            return popup.$panel;
        }

        function handleMethod() {
            var methodName: string = arguments[0];
            var popup: NtsPopupPanel = $(this).data(DATA_INSTANCE_NAME);

            switch (methodName) {
                case 'show':
                    popup.show();
                    break;
                case 'hide':
                    popup.hide();
                    break;
                case 'destroy':
                    popup.hide();
                    popup.destroy();
                    break;
                case 'toggle':
                    popup.toggle();
                    break;
            }
        }

        class NtsPopupPanel {

            $panel: JQuery;
            position: any;

            constructor($panel: JQuery, position: any) {

                this.position = position;
                var parent = $panel.parent();
                this.$panel = $panel
                    .data(DATA_INSTANCE_NAME, this)
                    .addClass('popup-panel')
                    .appendTo(parent);
                this.$panel.css("z-index", 100);
            }

            show() {
                this.$panel
                    .css({
                        visibility: 'hidden',
                        display: 'block'
                    })
                    .position(this.position)
                    .css({
                        visibility: 'visible'
                    });
            }

            hide() {
                this.$panel.css({
                    display: 'none'
                });
            }

            destroy() {
                this.$panel = null;
            }

            toggle() {
                var isDisplaying = this.$panel.css("display");
                if (isDisplaying === 'none') {
                    this.show();
                } else {
                    this.hide();
                }
            }
        }
    }

    module ntsGridList {

        let OUTSIDE_AUTO_SCROLL_SPEED = {
            RATIO: 0.2,
            MAX: 30
        };

        $.fn.ntsGridList = function(action: string, param?: any): any {

            var $grid = $(this);

            switch (action) {
                case 'setupSelecting':
                    return setupSelecting($grid);
                case 'unsetupSelecting':
                    return unsetupSelecting($grid);
                case 'getSelected':
                    return getSelected($grid);
                case 'setSelected':
                    return setSelected($grid, param);
                case 'deselectAll':
                    return deselectAll($grid);
                case 'setupDeleteButton':
                    return setupDeleteButton($grid, param);
            }
        };

        $.fn.ntsGridListFeature = function(feature: string, action: string, ...params: any[]): any {

            var $grid = $(this);

            switch (feature) {
                case 'switch':
                    switch (action) {
                        case 'setValue':
                            return setSwitchValue($grid, params);
                    }
            }
        };

        function setSwitchValue($grid: JQuery, ...params: any[]): any {
            let rowId: any = params[0][0];
            let columnKey: string = params[0][1];
            let selectedValue: any = params[0][2];
            let $row = $($grid.igGrid("rowById", rowId));
            let $parent = $row.find(".ntsControl");
            let currentSelect = $parent.attr('data-value');
            if (selectedValue !== currentSelect) {
                let rowKey = $row.attr("data-id");
                $parent.find(".nts-switch-button").removeClass("selected");
                let element = _.find($parent.find(".nts-switch-button"), function(e) {
                    return selectedValue.toString() === $(e).attr('data-value').toString();
                });
                if (element !== undefined) {
                    $(element).addClass('selected');
                    $parent.attr('data-value', selectedValue);
                    $grid.igGridUpdating("setCellValue", rowKey, columnKey, selectedValue);
                    $grid.igGrid("commit");
                    if ($grid.igGrid("hasVerticalScrollbar")) {
                        let current = $grid.ntsGridList("getSelected");
                        if(current !== undefined){
                            $grid.igGrid("virtualScrollTo", (typeof current === 'object' ? current.index : current[0].index) + 1);        
                        }
                    }
                }
            }
        }

        function getSelected($grid: JQuery): any {
            if ($grid.igGridSelection('option', 'multipleSelection')) {
                var selectedRows: Array<any> = $grid.igGridSelection('selectedRows');
                if (selectedRows)
                    return _.map(selectedRows, convertSelected);
                return [];
            } else {
                var selectedRow: any = $grid.igGridSelection('selectedRow');
                if (selectedRow)
                    return convertSelected(selectedRow);
                return undefined;
            }
        }

        function convertSelected(igGridSelectedRow: any) {
            return {
                id: igGridSelectedRow.id,
                index: igGridSelectedRow.index
            };
        }

        function setSelected($grid: JQuery, selectedId: any) {
            deselectAll($grid);

            if ($grid.igGridSelection('option', 'multipleSelection')) {
                (<Array<string>>selectedId).forEach(id => $grid.igGridSelection('selectRowById', id));
            } else {
                $grid.igGridSelection('selectRowById', selectedId);
            }
        }

        function deselectAll($grid: JQuery) {
            $grid.igGridSelection('clearSelection');
        }

        function setupDeleteButton($grid: JQuery, param) {
            var itemDeletedEvent = new CustomEvent("itemDeleted", {
                detail: {},
            });
            var currentColumns = $grid.igGrid("option", "columns");

            currentColumns.push({
                dataType: "bool", columnCssClass: "delete-column", headerText: "test", key: param.deleteField,
                width: 60, formatter: function createButton(deleteField, row) {
                    var primaryKey = $grid.igGrid("option", "primaryKey");
                    var result = $('<button tabindex="-1" class="small delete-button">Delete</button>');
                    result.attr("data-value", row[primaryKey]);
                    if (deleteField === true && primaryKey !== null && !util.isNullOrUndefined(row[primaryKey])) {
                        return result[0].outerHTML;
                    } else {
                        return result.attr("disabled", "disabled")[0].outerHTML;
                    }
                }
            });
            $grid.igGrid("option", "columns", currentColumns);

            $grid.on("click", ".delete-button", function() {
                var key = $(this).attr("data-value");
                var primaryKey = $grid.igGrid("option", "primaryKey");
                var source = _.cloneDeep($grid.igGrid("option", "dataSource"));
                _.remove(source, function(current) {
                    return _.isEqual(current[primaryKey].toString(), key.toString());
                });
                if (!util.isNullOrUndefined(param.sourceTarget) && typeof param.sourceTarget === "function") {
                    param.sourceTarget(source);
                } else {
                    $grid.igGrid("option", "dataSource", source);
                    $grid.igGrid("dataBind");
                }
                itemDeletedEvent.detail["target"] = key;
                document.getElementById($grid.attr('id')).dispatchEvent(itemDeletedEvent);
            });

        }

        function setupSelecting($grid: JQuery) {
            setupDragging($grid);
            setupSelectingEvents($grid);

            return $grid;
        }

        function unsetupSelecting($grid: JQuery) {
            unsetupDragging($grid);
            unsetupSelectingEvents($grid);

            return $grid;
        }

        function setupDragging($grid: JQuery) {
            var dragSelectRange = [];

            // used to auto scrolling when dragged above/below grid)
            var mousePos: { x: number, y: number, rowIndex: number } = null;


            $grid.bind('mousedown', function(e) {

                // グリッド内がマウスダウンされていない場合は処理なしで終了
                var $container = $grid.closest('.ui-iggrid-scrolldiv');
                if ($(e.target).closest('.ui-iggrid-table').length === 0) {
                    return;
                }

                // current grid size
                var gridVerticalRange = new util.Range(
                    $container.offset().top,
                    $container.offset().top + $container.height());

                mousePos = {
                    x: e.pageX,
                    y: e.pageY,
                    rowIndex: ig.grid.getRowIndexFrom($(e.target))
                };

                // set position to start dragging
                dragSelectRange.push(mousePos.rowIndex);

                var $scroller = $('#' + $grid.attr('id') + '_scrollContainer');

                // auto scroll while mouse is outside grid
                var timerAutoScroll = setInterval(() => {
                    var distance = gridVerticalRange.distanceFrom(mousePos.y);
                    if (distance === 0) {
                        return;
                    }

                    var delta = Math.min(distance * OUTSIDE_AUTO_SCROLL_SPEED.RATIO, OUTSIDE_AUTO_SCROLL_SPEED.MAX);
                    var currentScrolls = $scroller.scrollTop();
                    $grid.igGrid('virtualScrollTo', (currentScrolls + delta) + 'px');
                }, 20);

                // handle mousemove on window while dragging (unhandle when mouseup)
                $(window).bind('mousemove.NtsGridListDragging', function(e) {

                    var newPointedRowIndex = ig.grid.getRowIndexFrom($(e.target));

                    // selected range is not changed
                    if (mousePos.rowIndex === newPointedRowIndex) {
                        return;
                    }

                    mousePos = {
                        x: e.pageX,
                        y: e.pageY,
                        rowIndex: newPointedRowIndex
                    };

                    if (dragSelectRange.length === 1 && !e.ctrlKey) {
                        $grid.igGridSelection('clearSelection');
                    }

                    updateSelections();
                });

                // stop dragging
                $(window).one('mouseup', function(e) {
                    mousePos = null;
                    dragSelectRange = [];
                    $(window).unbind('mousemove.NtsGridListDragging');
                    if ($grid.data("selectUpdated") === true) {
                        $grid.triggerHandler('selectionchanged');
                    }
                    //$grid.triggerHandler('selectionchanged');  
                    clearInterval(timerAutoScroll);
                    $grid.data("selectUpdated", false);
                });
            });

            function updateSelections() {

                // rowIndex is NaN when mouse is outside grid
                if (isNaN(mousePos.rowIndex)) {
                    return;
                }

                // 以前のドラッグ範囲の選択を一旦解除する
                // TODO: probably this code has problem of perfomance when select many rows
                // should process only "differences" instead of "all"
                for (var i = 0, i_len = dragSelectRange.length; i < i_len; i++) {
                    // http://jp.igniteui.com/help/api/2016.2/ui.iggridselection#methods:deselectRow
                    $grid.igGridSelection('deselectRow', dragSelectRange[i]);
                }

                var newDragSelectRange = [];

                if (dragSelectRange[0] <= mousePos.rowIndex) {
                    for (var j = dragSelectRange[0]; j <= mousePos.rowIndex; j++) {
                        // http://jp.igniteui.com/help/api/2016.2/ui.iggridselection#methods:selectRow
                        $grid.igGridSelection('selectRow', j);
                        newDragSelectRange.push(j);
                    }
                } else if (dragSelectRange[0] > mousePos.rowIndex) {
                    for (var j = dragSelectRange[0]; j >= mousePos.rowIndex; j--) {
                        $grid.igGridSelection('selectRow', j);
                        newDragSelectRange.push(j);
                    }
                }

                dragSelectRange = newDragSelectRange;
                $grid.data("selectUpdated", true);
            }
        }

        function setupSelectingEvents($grid: JQuery) {
            $grid.bind('iggridselectioncellselectionchanging', () => {
            });
            $grid.bind('iggridselectionrowselectionchanged', () => {
                $grid.triggerHandler('selectionchanged');
            });

            //            $grid.on('mouseup', () => {
            //                $grid.triggerHandler('selectionchanged');
            //            });
        }

        function unsetupDragging($grid: JQuery) {

            $grid.unbind('mousedown');
        }

        function unsetupSelectingEvents($grid: JQuery) {
            $grid.unbind('iggridselectionrowselectionchanged');

            //            $grid.off('mouseup');
        }
    }

    module ntsTreeView {

        let OUTSIDE_AUTO_SCROLL_SPEED = {
            RATIO: 0.2,
            MAX: 30
        };

        $.fn.ntsTreeView = function(action: string, param?: any): any {

            var $tree = $(this);

            switch (action) {
                case 'getSelected':
                    return getSelected($tree);
                case 'setSelected':
                    return setSelected($tree, param);
                case 'deselectAll':
                    return deselectAll($tree);
            }
        };

        function getSelected($tree: JQuery): any {
            if ($tree.igTreeGridSelection('option', 'multipleSelection')) {
                var selectedRows: Array<any> = $tree.igTreeGridSelection('selectedRows');
                if (selectedRows)
                    return _.map(selectedRows, convertSelected);
                return [];
            } else {
                var selectedRow: any = $tree.igTreeGridSelection('selectedRow');
                if (selectedRow)
                    return convertSelected(selectedRow);
                return undefined;
            }
        }

        function convertSelected(selectedRow: any) {
            return {
                id: selectedRow.id,
                index: selectedRow.index
            };
        }

        function setSelected($tree: JQuery, selectedId: any) {
            deselectAll($tree);

            if ($tree.igTreeGridSelection('option', 'multipleSelection')) {
                (<Array<string>>selectedId).forEach(id => $tree.igTreeGridSelection('selectRowById', id));
            } else {
                $tree.igTreeGridSelection('selectRowById', selectedId);
            }
        }

        function deselectAll($grid: JQuery) {
            $grid.igTreeGridSelection('clearSelection');
        }
    }

    module ntsListBox {
        $.fn.ntsListBox = function(action: string): any {

            var $grid = $(this);

            switch (action) {
                case 'deselectAll':
                    deselectAll($grid);
                    break;
                case 'selectAll':
                    selectAll($grid);
                default:
                    break;
            }
        };

        function selectAll($list: JQuery) {
            let $grid = $list.find(".ntsListBox");
            let options = $grid.igGrid("option", "dataSource");
            let primaryKey = $grid.igGrid("option", "primaryKey");
            _.forEach(options, function (option, idx){
                $grid.igGridSelection("selectRowById", option[primaryKey]);        
            });
            $grid.triggerHandler('selectionchanged');
        }

        function deselectAll($list: JQuery) {
            let $grid = $list.find(".ntsListBox");
            $grid.igGridSelection('clearSelection');
            $grid.triggerHandler('selectionchanged');
        }
    }

    module ntsEditor {
        $.fn.ntsEditor = function(action: string): any {

            var $editor = $(this);

            switch (action) {
                case 'validate':
                    validate($editor);
                default:
                    break;
            }
        };

        function validate($editor: JQuery) {
            var validateEvent = new CustomEvent("validate", {

            });
            $editor.each(function(index) {
                var $input = $(this);
                document.getElementById($input.attr('id')).dispatchEvent(validateEvent);
            });
            //            document.getElementById($editor.attr('id')).dispatchEvent(validateEvent);
            //            $editor.trigger("validate");
        }
    }

    module ntsWizard {
        $.fn.ntsWizard = function(action: string, index?: number): any {
            var $wizard = $(this);
            if (action === "begin") {
                return begin($wizard);
            }
            else if (action === "end") {
                return end($wizard);
            }
            else if (action === "goto") {
                return goto($wizard, index);
            }
            else if (action === "prev") {
                return prev($wizard);
            }
            else if (action === "next") {
                return next($wizard);
            }
            else if (action === "getCurrentStep") {
                return getCurrentStep($wizard);
            }
            else {
                return $wizard;
            };
        }

        function begin(wizard: JQuery): JQuery {
            wizard.setStep(0);
            return wizard;
        }

        function end(wizard: JQuery): JQuery {
            wizard.setStep(wizard.data("length") - 1);
            return wizard;
        }

        function goto(wizard: JQuery, index: number): JQuery {
            wizard.setStep(index);
            return wizard;
        }

        function prev(wizard: JQuery): JQuery {
            wizard.steps("previous");
            return wizard;
        }

        function next(wizard: JQuery): JQuery {
            wizard.steps("next");
            return wizard;
        }

        function getCurrentStep(wizard: JQuery): number {
            return wizard.steps("getCurrentIndex");
        }

    }

    module ntsUserGuide {

        $.fn.ntsUserGuide = function(action?: string): any {
            var $controls = $(this);
            if (nts.uk.util.isNullOrUndefined(action) || action === "init") {
                return init($controls);
            }
            else if (action === "destroy") {
                return destroy($controls);
            }
            else if (action === "show") {
                return show($controls);
            }
            else if (action === "hide") {
                return hide($controls);
            }
            else if (action === "toggle") {
                return toggle($controls);
            }
            else if (action === "isShow") {
                return isShow($controls);
            }
            else {
                return $controls;
            };
        }

        function init(controls: JQuery): JQuery {
            controls.each(function() {
                // UserGuide container
                let $control = $(this);
                $control.remove();
                if (!$control.hasClass("ntsUserGuide"))
                    $control.addClass("ntsUserGuide");
                $($control).appendTo($("body")).show();
                let target = $control.data('target');
                let direction = $control.data('direction');

                // Userguide Information Box
                $control.children().each(function() {
                    let $box = $(this);
                    let boxDirection = $box.data("direction");
                    $box.addClass("userguide-box caret-" + getReveseDirection(boxDirection) + " caret-overlay");
                });

                // Userguide Overlay
                let $overlay = $("<div class='userguide-overlay'></div>")
                    .addClass("overlay-" + direction)
                    .appendTo($control);
                $control.hide();

            });

            // Hiding when click outside
            $("html").on("mouseup keypress", { controls: controls }, hideBinding);

            return controls;
        }

        function destroy(controls: JQuery) {
            controls.each(function() {
                $(this).remove();
            });

            // Unbind Hiding when click outside
            $("html").off("mouseup keypress", hideBinding);
            return controls;
        }

        function hideBinding(e): JQuery {
            e.data.controls.each(function() {
                $(this).hide();
            });
            return e.data.controls;
        }

        function show(controls: JQuery): JQuery {
            controls.each(function() {
                let $control = $(this);
                $control.show();

                let target = $control.data('target');
                let direction = $control.data('direction');
                $control.find(".userguide-overlay").each(function(index, elem) {
                    calcOverlayPosition($(elem), target, direction)
                });
                $control.children().each(function() {
                    let $box = $(this);
                    let boxTarget = $box.data("target");
                    let boxDirection = $box.data("direction");
                    let boxMargin = ($box.data("margin")) ? $box.data("margin") : "20";
                    calcBoxPosition($box, boxTarget, boxDirection, boxMargin);
                });
            });
            return controls;
        }


        function hide(controls: JQuery): JQuery {
            controls.each(function() {
                $(this).hide();
            });
            return controls;
        }

        function toggle(controls: JQuery): JQuery {
            if (isShow(controls))
                hide(controls);
            else
                show(controls);
            return controls;
        }

        function isShow(controls: JQuery): boolean {
            let result = true;
            controls.each(function() {
                if (!$(this).is(":visible"))
                    result = false;
            });
            return result;
        }

        function calcOverlayPosition(overlay: JQuery, target: string, direction: string): JQuery {
            if (direction === "left")
                return overlay.css("right", "auto")
                    .css("width", $(target).offset().left);
            else if (direction === "right")
                return overlay.css("left", $(target).offset().left + $(target).outerWidth());
            else if (direction === "top")
                return overlay.css("position", "absolute")
                    .css("bottom", "auto")
                    .css("height", $(target).offset().top);
            else if (direction === "bottom")
                return overlay.css("position", "absolute")
                    .css("top", $(target).offset().top + $(target).outerHeight())
                    .css("height", $("body").height() - $(target).offset().top);
        }

        function calcBoxPosition(box: JQuery, target: string, direction: string, margin: string): JQuery {
            let operation = "+";
            if (direction === "left" || direction === "top")
                operation = "-";
            return box.position({
                my: getReveseDirection(direction) + operation + margin,
                at: direction,
                of: target,
                collision: "none"
            });
        }

        function getReveseDirection(direction: string): string {
            if (direction === "left")
                return "right";
            else if (direction === "right")
                return "left";
            else if (direction === "top")
                return "bottom";
            else if (direction === "bottom")
                return "top";
        }
    }

    module ntsSearchBox {
        $.fn.setupSearchScroll = function(controlType: string, virtualization?: boolean) {
            var $control = this;
            if (controlType.toLowerCase() == 'iggrid') return setupIgGridScroll($control, virtualization);
            if (controlType.toLowerCase() == 'igtreegrid') return setupTreeGridScroll($control, virtualization);
            if (controlType.toLowerCase() == 'igtree') return setupIgTreeScroll($control);
            return this;
        }
        function setupIgGridScroll($control: JQuery, virtualization?: boolean) {
            var $grid = $control;
            if (virtualization) {
                $grid.on("selectChange", function() {
                    var row = null;
                    var selectedRows = $grid.igGrid("selectedRows");
                    if (selectedRows) {
                        row = selectedRows[0];
                    } else {
                        row = $grid.igGrid("selectedRow");
                    }
//                    if (row) $grid.igGrid("virtualScrollTo", getSelectRowIndex($grid, row.id));
                    if (row) {
                        let index = $(row.element).attr("data-row-idx");
                        $grid.igGrid("virtualScrollTo", index === undefined ? getSelectRowIndex($grid, row.id) : parseInt(index));
                    }
                });
            } else {
                $grid.on("selectChange", function() {
                    var row = null;
                    var selectedRows = $grid.igGrid("selectedRows");
                    if (selectedRows) {
                        row = selectedRows[0];
                    } else {
                        row = $grid.igGrid("selectedRow");
                    }
                    if (row) {
                        var index = row.index;
                        var height = row.element[0].scrollHeight;
                        var gridId = $grid.attr('id');
                        $("#" + gridId + "_scrollContainer").scrollTop(index * height);
                    }
                });
            }
            return $grid;
        }
        
        function getSelectRowIndex($grid: JQuery, selectedValue): number {
            let dataSource = $grid.igGrid("option", "dataSource");
            let primaryKey = $grid.igGrid("option", "primaryKey");
            return _.findIndex(dataSource, s => s[primaryKey].toString() === selectedValue.toString());        
        }

        function setupTreeGridScroll($control: JQuery, virtualization?: boolean) {
            var $treegrid = $control;
            var id = $treegrid.attr('id');
            $treegrid.on("selectChange", function() {
                var row = null;
                var selectedRows = $treegrid.igTreeGridSelection("selectedRows");
                if (selectedRows) {
                    row = selectedRows[0];
                } else {
                    row = $treegrid.igTreeGridSelection("selectedRow");
                }
                if (row) {
                    var index = row.index;
                    var height = row.element[0].scrollHeight;
                    $("#" + id + "_scroll").scrollTop(index * height);
                }
            });
            return $treegrid;
        }

        function setupIgTreeScroll($control: JQuery) {
            //implement later if needed
            return $control;
        }
    }

    module ntsSideBar {

        $.fn.ntsSideBar = function(action?: string, index?: number): any {
            var $control = $(this);
            if (nts.uk.util.isNullOrUndefined(action) || action === "init") {
                return init($control);
            }
            else if (action === "active") {
                return active($control, index);
            }
            else if (action === "enable") {
                return enable($control, index);
            }
            else if (action === "disable") {
                return disable($control, index);
            }
            else if (action === "show") {
                return show($control, index);
            }
            else if (action === "hide") {
                return hide($control, index);
            }
            else if (action === "getCurrent") {
                return getCurrent($control);
            }
            else {
                return $control;
            };
        }

        function init(control: JQuery): JQuery {
            $("html").addClass("sidebar-html");
            control.find("div[role=tabpanel]").hide();
            control.on("click", "#sidebar-area .navigator a", function(e) {
                e.preventDefault();
                if ($(this).attr("disabled") !== "true" &&
                    $(this).attr("disabled") !== "disabled" &&
                    $(this).attr("href") !== undefined) {
                    active(control, $(this).closest("li").index());
                }
            });
            control.find("#sidebar-area .navigator a.active").trigger('click');
            return control;
        }

        function active(control: JQuery, index: number): JQuery {
            control.find("#sidebar-area .navigator a").removeClass("active");
            control.find("#sidebar-area .navigator a").eq(index).addClass("active");
            control.find("div[role=tabpanel]").hide();
            $(control.find("#sidebar-area .navigator a").eq(index).attr("href")).show();
            return control;
        }

        function enable(control: JQuery, index: number): JQuery {
            control.find("#sidebar-area .navigator a").eq(index).removeAttr("disabled");
            return control;

        }

        function disable(control: JQuery, index: number): JQuery {
            control.find("#sidebar-area .navigator a").eq(index).attr("disabled", "disabled");
            return control;
        }

        function show(control: JQuery, index: number): JQuery {
            control.find("#sidebar-area .navigator a").eq(index).show();
            return control;
        }

        function hide(control: JQuery, index: number): JQuery {
            var current = getCurrent(control);
            if (current === index) {
                active(control, 0);
            }
            control.find("#sidebar-area .navigator a").eq(index).hide();
            return control;
        }

        function getCurrent(control: JQuery): number {
            let index = 0;
            index = control.find("#sidebar-area .navigator a.active").closest("li").index();
            return index;
        }

    }

    export module ntsGrid {
        $.fn.ntsGrid = function(options: any) {
            var self = this;
            
            if (typeof options === "string") {
                functions.ntsAction($(self), options, [].slice.call(arguments).slice(1));
                return;
            }
            if (options.ntsControls === undefined) {
                $(this).igGrid(options);
                return;
            }
            validation.scanValidators($(self), options.columns); 
            // Cell color
            let cellFormatter = new color.CellFormatter($(this), options.ntsFeatures);
            
            let columnControlTypes = {};
            let columnSpecialTypes = {};
            let formatColumn = function(column: any) {
                // Have column group
                if (!util.isNullOrUndefined(column.group)) {
                    let cols = _.map(column.group, formatColumn);
                    column.group = cols;
                    return column;  
                }
                // Special column types
                specialColumn.ifTrue(columnSpecialTypes, column);
                
                // Control types
                if (column.ntsControl === undefined) {
                    columnControlTypes[column.key] = ntsControls.TEXTBOX;
                    return cellFormatter.format(column);
                }
                if (column.ntsControl === ntsControls.LABEL) {
                    ntsControls.drawLabel($(self), column);
                    columnControlTypes[column.key] = ntsControls.LABEL;
                    return cellFormatter.format(column);
                }
                
                var controlDef = _.find(options.ntsControls, function(ctl: any) {
                    return ctl.name === column.ntsControl;
                });
                if (!util.isNullOrUndefined(controlDef)) columnControlTypes[column.key] = controlDef.controlType;
                else {
                    columnControlTypes[column.key] = ntsControls.TEXTBOX;
                    return cellFormatter.format(column);
                }
    
                var $self = $(self);
                column.formatter = function(value, rowObj) {
                    if (util.isNullOrUndefined(rowObj)) return value;
                    var rowId = rowObj[$self.igGrid("option", "primaryKey")];
                    var update = (val) => {
                        if ($self.data("igGrid") !== null) {
                            updating.updateCell($self, rowId, column.key, column.dataType !== 'string' ? val : val.toString());
                            if (options.autoCommit === undefined || options.autoCommit === false) {
                                var updatedRow = $self.igGrid("rowById", rowId, false);
                                $self.igGrid("commit");
                                if (updatedRow !== undefined) $self.igGrid("virtualScrollTo", $(updatedRow).data("row-idx"));
                            }
                        }
                    };
                    var deleteRow = () => {
                        if ($self.data("igGrid") !== null) $self.data("igGridUpdating").deleteRow(rowId);
                    };
    
                    var ntsControl = ntsControls.getControl(controlDef.controlType);
                    var cell = $self.igGrid("cellById", rowId, column.key);
                    var isEnable = $(cell).find("." + ntsControl.containerClass()).data("enable");
                    isEnable = isEnable !== undefined ? isEnable : controlDef.enable === undefined ? true : controlDef.enable;
                    var data = {
                        controlDef: controlDef,
                        update: update,
                        deleteRow: deleteRow,
                        initValue: value,
                        enable: isEnable
                    };
                    var controlCls = "nts-grid-control-" + column.key + "-" + rowId;
                    var $container = $("<div/>").append($("<div/>").addClass(controlCls).css("height", "35px"));
                    var $_self = $self;
                    setTimeout(function() {
                        var $self = $_self;
                        var $gridCell = $self.igGrid("cellById", rowObj[$self.igGrid("option", "primaryKey")], column.key);
                        if ($($gridCell.children()[0]).children().length === 0)
                            $("." + controlCls).append(ntsControl.draw(data));
                        ntsControl.$containedGrid = $self; 
                    }, 0);
    
                    return $container.html();
                };
                return column;
            }
            var columns = _.map(options.columns, formatColumn);
            
            options.columns = columns;
            updating.addFeature(options);
            options.autoCommit = true;
            // Decorate editor border
            events.onCellClick($(self));
            
            // Copy&Paste
            copyPaste.ifOn($(self), options);
            events.afterRendered(options);
            columnSize.init($(self), options.columns);
            
            // Group column key and its control type 
            $(this).data(internal.CONTROL_TYPES, columnControlTypes);
            // Group column key and its special type
            $(this).data(internal.SPECIAL_COL_TYPES, columnSpecialTypes);
            // Sheet
            sheet.load.setup($(self), options);
            // Common settings
            settings.build($(self), options);
            $(this).igGrid(options);
        };
        
        module feature {
            export let UPDATING = "Updating";
            export let SELECTION = "Selection";
            export let COLUMN_FIX = "ColumnFixing";
            export let COPY_PASTE = "CopyPaste";
            export let CELL_EDIT = "CellEdit";
            export let CELL_COLOR = "CellColor";
            export let HIDING = "Hiding";
            export let SHEET = "Sheet";
            export function replaceBy(options: any, featureName: string, newFeature: any) {
                let replaceId: number;
                _.forEach(options.features, function(feature: any, id: number) {
                    if (feature.name === featureName) {
                        replaceId = id;
                        return false;
                    }
                });
                options.features.splice(replaceId, 1, newFeature);
            }
            
            export function isEnable(features: any, name: string) {
                return _.find(features, function(feature: any) {
                    return feature.name === name;
                }) !== undefined;
            }
            
            export function find(features: any, name: string) {
                return _.find(features, function(feature: any) {
                    return feature.name === name;
                });
            }
        }
        
        module updating {
            
            export function addFeature(options: any) {
                let updateFeature = createUpdateOptions(options); 
                if (!feature.isEnable(options.features, feature.UPDATING)) {
                    options.features.push(updateFeature);
                } else {
                    feature.replaceBy(options, feature.UPDATING, createUpdateOptions(options));
                }
            }
            
            function createUpdateOptions(options: any) {
                let updateFeature: any = { name: feature.UPDATING, enableAddRow: false, enableDeleteRow: false, editMode: 'none' };
                if (feature.isEnable(options.ntsFeatures, feature.CELL_EDIT)) {
                    updateFeature.editMode = "cell";
                    updateFeature.editCellStarting = startEditCell;
                    updateFeature.editCellEnding = beforeFinishEditCell;
                }
                return updateFeature;
            }
            
            export function containsNtsControl($target: any) {
                let td = $target;
                if (!$target.prev().is("td")) td = $target.closest("td");
                return td.find("div[class*='nts-grid-control']").length > 0;
            } 
            
            function startEditCell(evt: any, ui: any) {
                if (containsNtsControl($(evt.currentTarget)) || utils.isEnterKey(evt) || utils.isTabKey(evt)) {
                    let selectedCell = selection.getSelectedCell($(evt.target));
                    if (util.isNullOrUndefined(selectedCell) || !utils.selectable($(evt.target))) return;
                    $(evt.target).igGridSelection("selectCell", selectedCell.rowIndex, selectedCell.index,
                                    utils.isFixedColumnCell(selectedCell, utils.getVisibleColumnsMap($(evt.target))));
                    return false;
                }
                return true; 
            }
            
            /**
             * Validate
             */
            export function onEditCell(evt: any, cell: any) {
                let $grid = fixedColumns.realGridOf($(evt.currentTarget));
                if (!utils.isEditMode($grid)) return;
                let validators: any =  $grid.data(validation.VALIDATORS);
                let fieldValidator = validators[cell.columnKey];
                if (util.isNullOrUndefined(fieldValidator)) return;
                
                let cellValue = $(cell.element).find("input:first").val();
                let result = fieldValidator.probe(cellValue);
                let $cellContainer = $(cell.element);
                errors.clear($grid, cell);
                if (!result.isValid) {
                    errors.set($grid, cell, result.errorMessage);
                }
            }
            
            export function triggerCellUpdate(evt: any, cell: any) {
                var grid = evt.currentTarget;
                let $targetGrid = fixedColumns.realGridOf($(grid));
                
                if (utils.isEditMode($targetGrid)) return;
                if (utils.isAlphaNumeric(evt)) {
                    startEdit(evt, cell);
                }
                if (utils.isDeleteKey(evt)) {
                    $targetGrid.one(events.Handler.GRID_EDIT_CELL_STARTED, function(evt: any, ui: any) {
                        $(ui.editor).find("input").val("");
                    });
                    startEdit(evt, cell);
                }
            }
            
            function startEdit(evt: any, cell: any) {
                let $targetGrid = fixedColumns.realGridOf($(evt.currentTarget));
                if (!utils.updatable($targetGrid)) return;
                utils.startEdit($targetGrid, cell);
                // Keep text contents if any, otherwise set input value
//                if ($(cell.element).text().trim() !== "") evt.preventDefault();
                if (!utils.isDeleteKey(evt)) {
                    setTimeout(function() { 
                        let $editor = $targetGrid.igGridUpdating("editorForCell", $(cell.element));
                        let newText = $editor.igTextEditor("value");
                        newText = newText.substr(newText.length - 1);
                        $editor.igTextEditor("value", newText.trim());
                    }, 100);
                }
                evt.stopImmediatePropagation();
            }
            
            /**
             * Interrupt manipulations (e.g. cell navigation) on grid if errors occurred (setting needed).
             */
            function beforeFinishEditCell(evt: any, ui: any) {
                let $grid = $(evt.target);
                let selectedCell = selection.getSelectedCell($grid);
                let settings: any = $grid.data(internal.SETTINGS); 
                if (settings.preventEditInError
                    && utils.isEditMode($grid) && errors.any(selectedCell)) {
                    return false;
                }
                
                // Remove border color of editor
                let $editorContainer = $(selectedCell.element).find(errors.EDITOR_SELECTOR);
                if ($editorContainer.length > 0) $editorContainer.css(errors.NO_ERROR_STL);
                
                specialColumn.tryDo($grid, selectedCell, ui.value); 
                return true;
            }
            
            /**
             * Update row and re-render all controls.
             * @Obsolete
             */
            export function _updateRow($grid: JQuery, rowId: any, visibleColumnsMap: any, updatedRowData: any) {
                if (util.isNullOrUndefined(updatedRowData) || Object.keys(updatedRowData).length === 0) return;
                $grid.igGridUpdating("updateRow", 
                            utils.parseIntIfNumber(rowId, $grid, visibleColumnsMap), updatedRowData);
            }
            
            /**
             * Update cell.
             */
            export function updateCell($grid: JQuery, rowId: any, columnKey: any, cellValue: any, visibleColumnsMap?: any) {
                let grid: any = $grid.data("igGrid");
                if (!utils.updatable($grid)) return;
                let gridUpdate: any = $grid.data("igGridUpdating");
                let autoCommit = grid.options.autoCommit;
                let columnsMap: any = visibleColumnsMap || utils.getVisibleColumnsMap($grid);
                let rId = utils.parseIntIfNumber(rowId, $grid, columnsMap);
                grid.dataSource.setCellValue(rId, columnKey, cellValue, autoCommit);
                renderCell($grid, rId, columnKey);
                gridUpdate._notifyCellUpdated(rId);
            }
            
            /**
             * Update row.
             */
            export function updateRow($grid: JQuery, rowId: any, updatedRowData: any, visibleColumnsMap?: any) {
                let grid: any = $grid.data("igGrid");
                if (!utils.updatable($grid)) return;
                let gridUpdate: any = $grid.data("igGridUpdating");
                let autoCommit = grid.options.autoCommit;
                let columnsMap: any = visibleColumnsMap || utils.getVisibleColumnsMap($grid);
                let rId = utils.parseIntIfNumber(rowId, $grid, columnsMap);
                let origData = gridUpdate._getLatestValues(rId); 
                grid.dataSource.updateRow(rId, $.extend({}, origData, updatedRowData), autoCommit);
                _.forEach(Object.keys(updatedRowData), function(key: any) {
                    renderCell($grid, rId, key, origData);
                });
                gridUpdate._notifyRowUpdated(rId, null);
            }
            
            export function renderCell($grid: JQuery, rowId: any, columnKey: any, latestValues?: any) {
                let grid: any = $grid.data("igGrid");
                if (!utils.updatable($grid)) return;
                let gridUpdate: any = $grid.data("igGridUpdating");
                let rowData = gridUpdate._getLatestValues(rowId);
                let column: any =  _.find(utils.getVisibleColumns($grid), function(col: any) {
                    return col.key === columnKey;
                });
                let $cell = $grid.igGrid("cellById", rowId, columnKey);
                $cell.html(String(grid._renderCell(rowData[columnKey], column, rowData)));
            }
        }
        
        module selection {
            
            export function addFeature(options: any) {
                let selection = { name: feature.SELECTION, mode: "cell", multipleSelection: true, wrapAround: false, cellSelectionChanged: selectCellChange };
                if (!feature.isEnable(options.features, feature.SELECTION)) {
                    options.features.push(selection);
                } else {
                    feature.replaceBy(options, feature.SELECTION, selection);
                }
            }
            
            export function selectPrev($grid: JQuery) {
                var selectedCell: any = getSelectedCell($grid);
                if (util.isNullOrUndefined(selectedCell)) return;
                clearSelection($grid);
                let visibleColumnsMap = utils.getVisibleColumnsMap($grid);
                let isFixed = utils.isFixedColumnCell(selectedCell, visibleColumnsMap);
                if (selectedCell.index > 0) {
                    selectCell($grid, selectedCell.rowIndex, selectedCell.index - 1, isFixed);
                } else if (selectedCell.index === 0) {
                    let columnsGroup = utils.columnsGroupOfCell(selectedCell, visibleColumnsMap);
                    if (util.isNullOrUndefined(columnsGroup) || columnsGroup.length === 0) return;
                    let fixedColumns = utils.getFixedColumns(visibleColumnsMap);
                    let unfixedColumns = utils.getUnfixedColumns(visibleColumnsMap);
                    
                    if ((isFixed || !utils.fixable($grid)) && selectedCell.rowIndex > 0) {
                        selectCell($grid, selectedCell.rowIndex - 1, unfixedColumns.length - 1);
                    } else if (utils.fixable($grid) && !isFixed) {
                        selectCell($grid, selectedCell.rowIndex, fixedColumns.length - 1, true);
                    }
                }
            }
            
            export function selectFollow($grid: JQuery, enterDirection?: string) {
                var enter = enterDirection || "right";
                if (enter === "right") selectNext($grid);
                else selectBelow($grid);
            }
            
            function selectNext($grid: JQuery) {
                var selectedCell: any = getSelectedCell($grid);
                if (util.isNullOrUndefined(selectedCell)) return;
                clearSelection($grid);
                let visibleColumnsMap = utils.getVisibleColumnsMap($grid);
                let dataSource = $grid.igGrid("option", "dataSource");
                
                let columnsGroup = utils.columnsGroupOfCell(selectedCell, visibleColumnsMap);
                if (util.isNullOrUndefined(columnsGroup) || columnsGroup.length === 0) return;
                if (selectedCell.index < columnsGroup.length - 1) { 
                    selectCell($grid, selectedCell.rowIndex, selectedCell.index + 1, columnsGroup[0].fixed); 
                } else if (selectedCell.index === columnsGroup.length - 1) {
                    if (columnsGroup[0].fixed) {
                        selectCell($grid, selectedCell.rowIndex, 0);
                    } else if (selectedCell.rowIndex < dataSource.length - 1) {
                        selectCell($grid, selectedCell.rowIndex + 1, 0, true);
                    }
                } 
            }
            
            function selectBelow($grid: JQuery) {
                var selectedCell: any = getSelectedCell($grid);
                if (util.isNullOrUndefined(selectedCell)) return;
                clearSelection($grid);
                let isFixed = utils.isFixedColumnCell(selectedCell, utils.getVisibleColumnsMap($grid));
                let dataSource = $grid.igGrid("option", "dataSource");
                if (selectedCell.rowIndex < dataSource.length - 1) {
                    selectCell($grid, selectedCell.rowIndex + 1, selectedCell.index, isFixed);
                }
            }
            
            export function getSelectedCell($grid: JQuery) {
                if (!utils.selectable($grid)) { 
                    let $targetGrid = fixedColumns.realGridOf($grid); 
                    if (!util.isNullOrUndefined($targetGrid)) {
                        return $targetGrid.igGridSelection("selectedCells")[0] || $targetGrid.data(internal.SELECTED_CELL); 
                    }
                }
                return $grid.igGridSelection("selectedCells")[0] || $grid.data(internal.SELECTED_CELL);
            }
            
            export function getSelectedCells($grid: JQuery) {
                return utils.selectable($grid) ? $grid.igGridSelection("selectedCells") : undefined;
            }
            
            export function selectCell($grid: JQuery, rowIndex: number, columnIndex: number, isFixed?: boolean) {
                if (!utils.selectable($grid)) return;
                $grid.igGridSelection("selectCell", rowIndex, columnIndex, 
                        !util.isNullOrUndefined($grid.data("igGridColumnFixing")) ? isFixed : undefined);
                
                // Fire cell selection change
                let ui: any = { owner: $grid.data("igGridSelection"),
                                selectedCells: $grid.igGridSelection("selectedCells") };
                let selectedCells = $grid.igGridSelection("selectedCells");
                if (selectedCells.length > 0) ui.cell = selectedCells[0];
                selectCellChange({ target: $grid[0] }, ui);
                
                // TODO: Focus nts common controls if exists.
                let selectedCell: any = getSelectedCell($grid);
                let ntsCombo = $(selectedCell.element).find(".nts-combo-container"); 
                if (ntsCombo.length > 0) {
                    ntsCombo.find("input").select();
                }
            }
            
            export function selectCellById($grid: JQuery, rowId: any, columnKey: string) {
                return;
            }
            
            function selectCellChange(evt: any, ui: any) {
                if (util.isNullOrUndefined(ui.cell)) return;
                $(evt.target).data(internal.SELECTED_CELL, ui.cell);
            }
            
            export function onCellNavigate(evt: any, enterDirection?: string) {
                var grid = evt.currentTarget;
                let $targetGrid = fixedColumns.realGridOf($(grid));
                
                if (utils.isTabKey(evt)) {
                    if (utils.isEditMode($targetGrid))
                        $targetGrid.igGridUpdating("endEdit");
                    
                    if (evt.shiftKey) {
                        selection.selectPrev($targetGrid);
                    } else {
                        selection.selectFollow($targetGrid);
                    }
                    evt.preventDefault();
                    return;
                }
                 
                if (utils.isEnterKey(evt)) {
                    if (evt.shiftKey) {
                        selection.selectPrev($targetGrid);
                    } else {
                        selection.selectFollow($targetGrid, enterDirection);
                    }
                    evt.stopImmediatePropagation();
                    return;
                }
            }
            
            function clearSelection($grid) {
                if (utils.selectable($grid)) {
                    $grid.igGridSelection("clearSelection");
                    return;
                }
                let $targetGrid = fixedColumns.realGridOf($grid);
                if (!util.isNullOrUndefined($targetGrid) && utils.selectable($targetGrid))
                    $targetGrid.igGridSelection("clearSelection");
            }
            
            export class Direction {
                to: string;
                bind(evt: any) {
                    onCellNavigate(evt, this.to);
                }
            }
        }
        
        module columnSize {
            
            export function init($grid: JQuery, columns: any) {
                if (initValueExists($grid)) return;
                let columnWidths: {[ key: string ]: number } = {};
                _.forEach(columns, function(col: any, index: number) {
                    columnWidths[col.key] = parseInt(col.width);
                });
                saveAll($grid, columnWidths);
            }
            
            export function load($grid: JQuery) {
                let storeKey = getStorageKey($grid);
                uk.localStorage.getItem(storeKey).ifPresent((columns) => {
                    let widthColumns: any = JSON.parse(columns);
                    setWidths($grid, widthColumns);
                    return null;
                });
            }
            
            export function save($grid: JQuery, columnKey: string, columnWidth: number) {
                let storeKey = getStorageKey($grid);
                let columnsWidth = uk.localStorage.getItem(storeKey);
                let widths = {};
                if (columnsWidth.isPresent()) {
                    widths = JSON.parse(columnsWidth.get());
                    widths[columnKey] = columnWidth;
                } else {
                    widths[columnKey] = columnWidth;
                }
                uk.localStorage.setItemAsJson(storeKey, widths);
            }
            
            function saveAll($grid: JQuery, widths: {[ key: string ]: number }) {
                let storeKey = getStorageKey($grid);
                let columnWidths = uk.localStorage.getItem(storeKey);
                if (!columnWidths.isPresent()) {
                    uk.localStorage.setItemAsJson(storeKey, widths);
                }
            }
            
            function initValueExists($grid: JQuery) {
                let storeKey = getStorageKey($grid);
                let columnWidths = uk.localStorage.getItem(storeKey);
                return columnWidths.isPresent();
            }
            
            function getStorageKey($grid: JQuery) {
                return request.location.current.rawUrl + "/" + $grid.attr("id");
            }
            
            export function loadOne($grid: JQuery, columnKey: string) {
                let storeKey = getStorageKey($grid);
                uk.localStorage.getItem(storeKey).ifPresent((columns) => {
                    let widthColumns: any = JSON.parse(columns);
                    setWidth($grid, columnKey, widthColumns[columnKey]);
                    return null;
                });
            }
            export function loadFixedColumns($grid: JQuery) {
                let storeKey = getStorageKey($grid);
                uk.localStorage.getItem(storeKey).ifPresent((columns) => {
                    let fixedColumns = utils.getVisibleFixedColumns($grid);
                    if (util.isNullOrUndefined(fixedColumns) || fixedColumns.length === 0) return;
                    let widthColumns: any = JSON.parse(columns);
                    _.forEach(fixedColumns, function(fixedCol) {
                        setWidth($grid, fixedCol.key, widthColumns[fixedCol.key]);
                    });
                    return null; 
                });
            }
            
            function setWidth($grid: JQuery, columnKey: string, width: number, noCheck?: boolean) {
                if (noCheck !== true && util.isNullOrUndefined($grid.data("igGridResizing"))) return;
                try {
                    $grid.igGridResizing("resize", columnKey, width);
                } catch (e) {}
            }
            
            function setWidths($grid: JQuery, columns: {[ key: string ]: number}) {
                if (util.isNullOrUndefined($grid.data("igGridResizing"))
                    || util.isNullOrUndefined(columns)) return;
                let columnKeys = Object.keys(columns);
                _.forEach(columnKeys, function(key: any, index: number) {
                    setWidth($grid, key, columns[key], true);
                });
            }
        }

        module functions {
            export function ntsAction($grid: JQuery, method: string, params: Array<any>) {
                switch (method) {
                    case "updateRow":
                        var autoCommit = $grid.data("igGrid") !== null && $grid.igGrid("option", "autoCommit") ? true : false;
                        updateRow($grid, params[0], params[1], autoCommit);
                        break;
                    case "enableNtsControlAt":
                        enableNtsControlAt($grid, params[0], params[1], params[2]);
                        break;
                    case "disableNtsControlAt":
                        disableNtsControlAt($grid, params[0], params[1], params[2]);
                        break;
                    case "directEnter":
                        var direction: selection.Direction = $grid.data(internal.ENTER_DIRECT);
                        direction.to = params[0];
                        if (utils.fixable($grid)) {
                            let fixedTable = fixedColumns.getFixedTable($grid)
                            if (!util.isNullOrUndefined(fixedTable)) {
                                fixedTable.data(internal.ENTER_DIRECT).to = params[0];
                            }
                        }
                        break;
                }
            }
    
            function updateRow($grid: JQuery, rowId: any, object: any, autoCommit: boolean) {
                updating.updateRow($grid, rowId, object);
                if (!autoCommit) {
                    var updatedRow = $grid.igGrid("rowById", rowId, false);
                    $grid.igGrid("commit");
                    if (updatedRow !== undefined) $grid.igGrid("virtualScrollTo", $(updatedRow).data("row-idx"));
                }
            }
    
            function disableNtsControlAt($grid: JQuery, rowId: any, columnKey: any, controlType: string) {
                var cellContainer = $grid.igGrid("cellById", rowId, columnKey);
                var control = ntsControls.getControl(controlType);
                control.disable($(cellContainer));
            }
    
            function enableNtsControlAt($grid: JQuery, rowId: any, columnKey: any, controlType: string) {
                var cellContainer = $grid.igGrid("cellById", rowId, columnKey);
                var control = ntsControls.getControl(controlType);
                control.enable($(cellContainer));
            }
        }
        
        module ntsControls {
            export let LABEL: string = 'Label';
            export let LINK_LABEL: string = 'LinkLabel';
            export let CHECKBOX: string = 'CheckBox';
            export let SWITCH_BUTTONS: string = 'SwitchButtons';
            export let COMBOBOX: string = 'ComboBox'; 
            export let BUTTON: string = 'Button';
            export let DELETE_BUTTON = 'DeleteButton';
            export let TEXTBOX = 'TextBox';

            export function getControl(name: string): NtsControlBase {
                switch (name) {
                    case CHECKBOX:
                        return new CheckBox();
                    case SWITCH_BUTTONS:
                        return new SwitchButtons();
                    case COMBOBOX:
                        return new ComboBox();
                    case BUTTON:
                        return new Button();
                    case DELETE_BUTTON:
                        return new DeleteButton();
                    case LINK_LABEL:
                        return new LinkLabel();
                }
            }
            
            export function drawLabel($grid: JQuery, column: any): void {
                column.formatter = function(value, rowObj) {
                    if (util.isNullOrUndefined(rowObj)) return value;
                    var $self = this;
                    var rowId = rowObj[$grid.igGrid("option", "primaryKey")];
                    var controlCls = "nts-grid-control-" + column.key + "-" + rowId;
                    var $container = $("<div/>").append($("<div/>").addClass(controlCls).css("height", "35px"));
                    setTimeout(function() {
                        var $gridCell = $grid.igGrid("cellById", rowObj[$grid.igGrid("option", "primaryKey")], column.key);
                        if ($($gridCell.children()[0]).children().length === 0)
                            $("." + controlCls).append(new Label().draw({ text: value }));
                    }, 0);

                    return $container.html();
                };
            }
    
            abstract class NtsControlBase {
                $containedGrid: JQuery;
                readOnly: boolean = false;
                abstract containerClass(): string;
                abstract draw(data: any): JQuery;
                abstract enable($container: JQuery): void;
                abstract disable($container: JQuery): void;
            }
    
            class CheckBox extends NtsControlBase {
                containerClass(): string {
                    return "nts-checkbox-container";
                }
    
                draw(data: any): JQuery {
                    var checkBoxText: string;
                    var setChecked = data.update;
                    var initValue = data.initValue;
                    var $wrapper = $("<div/>").addClass(this.containerClass()).data("enable", data.enable);
                    $wrapper.addClass("ntsControl").on("click", (e) => {
                        if ($wrapper.data("readonly") === true) e.preventDefault();
                    });
    
                    var text = data.controlDef.options[data.controlDef.optionsText];
                    if (text) {
                        checkBoxText = text;
                    } else {
                        checkBoxText = $wrapper.text();
                        $wrapper.text('');
                    }
                    var $checkBoxLabel = $("<label class='ntsCheckBox'></label>");
                    var $checkBox = $('<input type="checkbox">').on("change", function() {
                        setChecked($(this).is(":checked"));
                    }).appendTo($checkBoxLabel);
                    var $box = $("<span class='box'></span>").appendTo($checkBoxLabel);
                    if (checkBoxText && checkBoxText.length > 0)
                        var label = $("<span class='label'></span>").text(checkBoxText).appendTo($checkBoxLabel);
                    $checkBoxLabel.appendTo($wrapper);
    
                    var checked = initValue !== undefined ? initValue : true;
                    $wrapper.data("readonly", this.readOnly);
                    var $checkBox = $wrapper.find("input[type='checkbox']");
    
                    if (checked === true) $checkBox.attr("checked", "checked");
                    else $checkBox.removeAttr("checked");
                    if (data.enable === true) $checkBox.removeAttr("disabled");
                    else $checkBox.attr("disabled", "disabled");
                    return $wrapper;
                }
    
                disable($container: JQuery): void {
                    var $wrapper = $container.find("." + this.containerClass()).data("enable", false);
                    $wrapper.find("input[type='checkbox']").attr("disabled", "disabled");
                }
    
                enable($container: JQuery): void {
                    var $wrapper = $container.find("." + this.containerClass()).data("enable", true);
                    $wrapper.find("input[type='checkbox']").removeAttr("disabled");
                }
            }
    
            class SwitchButtons extends NtsControlBase {
                containerClass(): string {
                    return "nts-switch-container";
                }
    
                draw(data: any): JQuery {
                    var selectedCssClass = 'selected';
                    var options = data.controlDef.options;
                    var optionsValue = data.controlDef.optionsValue;
                    var optionsText = data.controlDef.optionsText;
                    var selectedValue = data.initValue;
                    var container = $("<div/>").addClass(this.containerClass()).data("enable", data.enable);
    
                    _.forEach(options, function(opt) {
                        var value = opt[optionsValue];
                        var text = opt[optionsText];
    
                        var btn = $('<button>').text(text)
                            .addClass('nts-switch-button')
                            .attr('data-swbtn', value)
                            .on('click', function() {
                                var selectedValue = $(this).data('swbtn');
                                $('button', container).removeClass(selectedCssClass);
                                $(this).addClass(selectedCssClass);
                                data.update(selectedValue);
                            });
                        if (value === selectedValue) {
                            btn.addClass(selectedCssClass);
                        }
                        container.append(btn);
                    });
                    (data.enable === true) ? $('button', container).prop("disabled", false)
                        : $('button', container).prop("disabled", true);
                    return container;
                }
    
                enable($container: JQuery): void {
                    var $wrapper = $container.find("." + this.containerClass()).data("enable", true);
                    $('button', $wrapper).prop("disabled", false);
                }
    
                disable($container: JQuery): void {
                    var $wrapper = $container.find("." + this.containerClass()).data("enable", false);
                    $('button', $wrapper).prop("disabled", true);
                }
    
            }
    
            class ComboBox extends NtsControlBase {
                containerClass(): string {
                    return "nts-combo-container";
                }
    
                draw(data: any): JQuery {
                    var self = this;
                    // Default values.
                    var distanceColumns = '     ';
                    // Character used fill to the columns.
                    var fillCharacter = ' ';
                    var maxWidthCharacter = 15;
                    var container = $("<div/>").addClass(this.containerClass()).data("enable", data.enable);
                    var columns: Array<any> = data.controlDef.columns;
    
                    // Set attribute for multi column.
                    var itemTemplate: string = undefined;
                    var haveColumn = columns && columns.length > 0;
                    if (haveColumn) {
                        itemTemplate = '<div class="nts-combo-item">';
                        _.forEach(columns, function(item, i) {
                            // Set item template.
                            itemTemplate += '<div class="nts-column nts-combo-column-' + i + '">${' + item.prop + '}</div>';
                        });
                        itemTemplate += '</div>';
                    }
    
                    // Display full code name
                    if (data.controlDef.displayMode === "codeName") {
                        data.controlDef.options = data.controlDef.options.map((option) => {
                            var newOptionText: string = '';
                            if (haveColumn) {
                                _.forEach(columns, function(item, i) {
                                    var prop: string = option[item.prop];
                                    var length: number = item.length;
        
                                    if (i === columns.length - 1) {
                                        newOptionText += prop;
                                    } else {
                                        newOptionText += text.padRight(prop, fillCharacter, length) + distanceColumns;
                                    }
                                });
        
                            } else {
                                newOptionText = option[data.controlDef.optionsText];
                            }
                            option['nts-combo-label'] = newOptionText;
                            return option;
                        });
                    }
    
                    var comboMode: string = data.controlDef.editable ? 'editable' : 'dropdown';
                    container.igCombo({
                        dataSource: data.controlDef.options,
                        valueKey: data.controlDef.optionsValue,
                        textKey: data.controlDef.displayMode === 'codeName' 
                                    ? 'nts-combo-label' : data.controlDef.optionsText,
                        mode: comboMode,
                        disabled: !data.enable,
                        placeHolder: '',
                        enableClearButton: false,
                        initialSelectedItems: [
                            { value: data.initValue }
                        ],
                        itemTemplate: itemTemplate,
                        selectionChanging: function(evt: any, ui: any) {
                            var __self = self; 
                            let $gridControl = $(evt.target).closest("div[class*=nts-grid-control]");
                            if (util.isNullOrUndefined($gridControl)) return;
                            let cls = $gridControl.attr("class");
                            let classNameParts = cls.split("-");
                            let rowId = classNameParts.pop();
                            let columnKey = classNameParts.pop();
                            let targetCell: any = __self.$containedGrid.igGrid("cellById", rowId, columnKey);
                            let $comboContainer = $(targetCell).find("." + __self.containerClass());
                            // Clear error if any
                            let comboInput = $($comboContainer.find("input")[1]);
                            comboInput.ntsError("clear");
                            nts.uk.ui.errors.removeByElement(comboInput);
                            comboInput.parent().removeClass("error");
                        },
                        selectionChanged: function(evt: any, ui: any) {
                            var _self = self;
                            if (ui.items.length > 0) {
                                let selectedValue = ui.items[0].data[data.controlDef.optionsValue];
                                data.update(selectedValue);
                                
                                setTimeout(function() {
                                    var __self = _self; 
                                    let $gridControl = $(evt.target).closest("div[class*=nts-grid-control]");
                                    if (util.isNullOrUndefined($gridControl)) return;
                                    let cls = $gridControl.attr("class");
                                    let classNameParts = cls.split("-");
                                    let rowId = classNameParts.pop();
                                    let columnKey = classNameParts.pop();
                                    let targetCell: any = __self.$containedGrid.igGrid("cellById", rowId, columnKey);
                                    let $comboContainer = $(targetCell).find("." + __self.containerClass());
                                    // Save selected item
                                    $comboContainer.data(internal.COMBO_SELECTED, selectedValue);
                                }, 0);
                            }
                        }
                    });
                    // Save init value
                    container.data(internal.COMBO_SELECTED, data.initValue);
    
                    // Set width for multi columns.
                    if (haveColumn) {
                        var totalWidth = 0;
                        var $dropDownOptions = $(container.igCombo("dropDown"));
                        _.forEach(columns, function(item, i) {
                            var charLength: number = item.length;
                            var width = charLength * maxWidthCharacter + 10;
                            $dropDownOptions.find('.nts-combo-column-' + i).width(width);
                            if (i !== columns.length - 1) {
                                $dropDownOptions.find('.nts-combo-column-' + i).css({ 'float': 'left' });
                            }
                            totalWidth += width + 10;
                        });
                        $dropDownOptions.find('.nts-combo-item').css({ 'min-width': totalWidth });
                        container.css({ 'min-width': totalWidth });
                    }
    
                    container.data("columns", columns);
                    container.data("comboMode", comboMode);
                    return container;
                }
    
                enable($container: JQuery): void {
                    var $wrapper = $container.find("." + this.containerClass());
                    $wrapper.data("enable", true);
                    $wrapper.igCombo("option", "disabled", false);
                }
                disable($container: JQuery): void {
                    var $wrapper = $container.find("." + this.containerClass());
                    $wrapper.data("enable", false);
                    $wrapper.igCombo("option", "disabled", true);
                }
            }
    
            class Button extends NtsControlBase {
                containerClass(): string {
                    return "nts-button-container";
                }
    
                draw(data: any): JQuery {
                    var $container = $("<div/>").addClass(this.containerClass());
                    var $button = $("<button/>").addClass("ntsButton").appendTo($container).text(data.controlDef.text || data.initValue)
                        .data("enable", data.enable).on("click", data.controlDef.click);
                    $button.prop("disabled", !data.enable);
                    return $container;
                }
    
                enable($container: JQuery): void {
                    var $wrapper = $container.find("." + this.containerClass()).data("enable", true);
                    $wrapper.find(".ntsButton").prop("disabled", false);
                }
                disable($container: JQuery): void {
                    var $wrapper = $container.find("." + this.containerClass()).data("enable", false);
                    $wrapper.find(".ntsButton").prop("disabled", true);
                }
            }
    
            class DeleteButton extends Button {
                draw(data: any): JQuery {
                    var btnContainer = super.draw(data);
                    var btn = btnContainer.find("button");
                    btn.off("click", data.controlDef.click);
                    btn.on("click", data.deleteRow);
                    return btn;
                }
            }
            
            class Label extends NtsControlBase {
                containerClass(): string {
                    return "nts-label-container";
                }
                
                draw(data: any): JQuery {
                    var $container = $("<div/>").addClass(this.containerClass());
                    $("<label/>").addClass("ntsLabel").css("padding-left", "0px").text(data.text).appendTo($container);
                    return $container;
                }
                
                enable($container: JQuery): void {
                    return;
                }
                disable($container: JQuery): void {
                    return;
                }
            }
            
            class LinkLabel extends NtsControlBase {
                containerClass(): string {
                    return "nts-link-container";
                }
                
                draw(data: any): JQuery {
                    return $('<div/>').addClass(this.containerClass()).append($("<a/>")
                                        .addClass("link-button").css({ backgroundColor: "inherit", color: "deepskyblue" })
                                        .text(data.initValue).on("click", data.controlDef.click));
                }
                
                enable($container: JQuery): void {
                    return;
                }
                disable($container: JQuery): void {
                    return;
                }
            }
            
            export module comboBox {
                
                export function getCopiedValue(cell: any, copiedText: string) {
                    let copiedValue;
                    let $comboBox = utils.comboBoxOfCell(cell);
                    if ($comboBox.length > 0) {
                        let items = $comboBox.igCombo("items");                   
                        let textKey = $comboBox.igCombo("option", "textKey");
                        let valueKey = $comboBox.igCombo("option", "valueKey");
                        _.forEach(items, function(item: any) {
                            if (item.data[textKey] === copiedText.trim()) {
                                copiedValue = item.data[valueKey];
                                return false;
                            }
                        });
                    }
                    return copiedValue;
                }
            }
        }
        
        module specialColumn {
            export let CODE: string = "code";
            
            export function ifTrue(columnSpecialTypes: any, column: any) {
                if (util.isNullOrUndefined(column.ntsType)) return;
                if (column.ntsType === CODE) {
                    columnSpecialTypes[column.key] = { type: column.ntsType,
                                                       onChange: column.onChange };
                }
            }
            
            export function tryDo($grid: JQuery, cell: any, pastedText: any, visibleColumnsMap?: any) {
                let columnTypes = $grid.data(internal.SPECIAL_COL_TYPES);
                let specialColumn;
                let columnKey = cell.columnKey;
                for (let key in columnTypes) {
                    if (key === columnKey) {
                        specialColumn = columnTypes[key];
                        break;
                    }
                }
                
                if (util.isNullOrUndefined(specialColumn)) return;
                visibleColumnsMap = !util.isNullOrUndefined(visibleColumnsMap) ? visibleColumnsMap : utils.getVisibleColumnsMap($grid);
                let isFixedColumn = utils.isFixedColumn(columnKey, visibleColumnsMap);
                let nextColumn = utils.nextColumnByKey(visibleColumnsMap, columnKey, isFixedColumn);
                if (util.isNullOrUndefined(nextColumn) || nextColumn.index === 0) return;
                
                specialColumn.onChange(pastedText).done(function(res: any) {
                    let updatedRow = {};
                    let $gridRow = utils.rowAt(cell);
                    updatedRow[nextColumn.options.key] = res; 
                    updating.updateRow($grid, $gridRow.data("id"), updatedRow, visibleColumnsMap);
                }).fail(function(res: any) {
                    
                });
                return true;
            }
            
            export function onChange($grid: JQuery, rowId: any, updatedRow: any) {
                let columnTypes = $grid.data(internal.SPECIAL_COL_TYPES);
                let columnKeys = Object.keys(updatedRow);
                _.find(columnKeys, function(key: any) {
                    return _.find(columnTypes, function(column: any) {
                        return column.type === key;
                    }) !== undefined;
                });
            }
        }
        
        module copyPaste {
            
            enum CopyMode {
                SINGLE,
                MULTIPLE
            }
            
            enum PasteMode {
                NEW,
                UPDATE
            }
            
            export class Processor {
                $grid: JQuery;
                options: any;
                pasteInMode: PasteMode = PasteMode.UPDATE;
                copyMode: CopyMode;
                visibleColumnsMap: any;
            
                constructor(options?: any) {
                    this.options = options;
                }
                static addFeatures(options: any) {
                    selection.addFeature(options);
                    return new Processor(options);
                }
                
                /**
                 * $grid to handle copy paste
                 * $target to bind events to
                 */
                chainEvents($grid: JQuery, $target?: JQuery) {
                    var self = this;
                    self.$grid = $grid;
                    let target = !util.isNullOrUndefined($target) ? $target : $grid; 
                    events.Handler.pull(target).focusInWith(self).ctrlCxpWith(self);
                }
                
                copyHandler(cut?: boolean) {
                    let selectedCells: Array<any> = selection.getSelectedCells(this.$grid);
                    let copiedData;
                    let checker = cut ? utils.isCuttableControls : utils.isCopiableControls;
                    if (selectedCells.length === 1) {
                        this.copyMode = CopyMode.SINGLE;
                        if (!checker(this.$grid, selectedCells[0].columnKey)) return;
                        if (utils.isComboBox(this.$grid, selectedCells[0].columnKey)) {
                            let $comboBox = utils.comboBoxOfCell(selectedCells[0]);
                            if ($comboBox.length > 0) {
                                copiedData = $comboBox.igCombo("text");
                            }
                        } else {
                            copiedData = selectedCells[0].element.text();
                        }
                    } else {
                        this.copyMode = CopyMode.MULTIPLE;
                        copiedData = this.converseStructure(selectedCells, cut);
                    }
                    $("#copyHelper").val(copiedData).select();
                    document.execCommand("copy");
                    return selectedCells;
                }
                
                converseStructure(cells: Array<any>, cut: boolean): string {
                    let self = this;
                    let maxRow = 0;
                    let minRow = 0;
                    let maxColumn = 0;
                    let minColumn = 0;
                    let structure = [];
                    let structData: string = "";
                    let checker = cut ? utils.isCuttableControls : utils.isCopiableControls;
                    _.forEach(cells, function(cell: any, index: number) {
                        let rowIndex = cell.rowIndex;
                        let columnIndex = utils.getDisplayColumnIndex(self.$grid, cell);
                        if (index === 0) {
                            minRow = maxRow = rowIndex;
                            minColumn = maxColumn = columnIndex;
                        }
                        if (rowIndex < minRow) minRow = rowIndex;
                        if (rowIndex > maxRow) maxRow = rowIndex;
                        if (columnIndex < minColumn) minColumn = columnIndex;
                        if (columnIndex > maxColumn) maxColumn = columnIndex;
                        if (util.isNullOrUndefined(structure[rowIndex])) {
                            structure[rowIndex] = {};
                        }
                        if (!checker(self.$grid, cell.columnKey)) return;
                        if (utils.isComboBox(self.$grid, cell.columnKey)) {
                            let $comboBox = utils.comboBoxOfCell(cell);
                            if ($comboBox.length > 0) {
                                structure[rowIndex][columnIndex] = $comboBox.igCombo("text");
                            }
                        } else {
                            structure[rowIndex][columnIndex] = cell.element.text();
                        }
                    });
                    
                    for (var i = minRow; i <= maxRow; i++) {
                        for (var j = minColumn; j <= maxColumn; j++) {
                            if (util.isNullOrUndefined(structure[i]) || util.isNullOrUndefined(structure[i][j])) {
                                structData += "null";
                            } else {
                                structData += structure[i][j];
                            }
                            
                            if (j === maxColumn) structData += "\n";
                            else structData += "\t";
                        }
                    }
                    return structData;
                }
                
                cutHandler() {
                    var self = this;
                    var selectedCells = this.copyHandler(true);
                    var cellsGroup = _.groupBy(selectedCells, "rowIndex");
                    _.forEach(Object.keys(cellsGroup), function(rowIdx: any) {
                        var $row = utils.rowAt(cellsGroup[rowIdx][0]);
                        var updatedRowData = {};
                        _.forEach(cellsGroup[rowIdx], function(cell: any) {
                            if (!utils.isCuttableControls(self.$grid, cell.columnKey)) return;
                            updatedRowData[cell.columnKey] = "";
                        });
                        updating.updateRow(self.$grid, $row.data("id"), updatedRowData);
                    });
                }
                
                
                pasteHandler(evt: any) {
                    if (this.copyMode === CopyMode.SINGLE) {
                        this.pasteSingleCellHandler(evt);
                    } else {
                        this.pasteRangeHandler(evt);
                    }
                }
                
                pasteSingleCellHandler(evt: any) {
                    let self = this;
                    let cbData = this.getClipboardContent(evt);
                    let selectedCells = selection.getSelectedCells(this.$grid);
                    let visibleColumnsMap = utils.getVisibleColumnsMap(self.$grid);
                    _.forEach(selectedCells, function(cell: any, index: number) {
                        if (!utils.isPastableControls(self.$grid, cell.columnKey)) return;
                        let rowIndex = cell.rowIndex;
                        let columnIndex = cell.index;
                        let $gridRow = utils.rowAt(cell);
                        let updatedRow = {};
                        let columnsGroup = utils.columnsGroupOfCell(cell, visibleColumnsMap);
                        let columnKey = columnsGroup[columnIndex].key;
                        
                        // When pasted cell is combox
                        if (utils.isComboBox(self.$grid, cell.columnKey)) {
                            let copiedValue = ntsControls.comboBox.getCopiedValue(cell, cbData);
                            if (!util.isNullOrUndefined(copiedValue)) {
                                updatedRow[columnKey] = columnsGroup[columnIndex].dataType === "number" 
                                    ? parseInt(copiedValue) : copiedValue;
                            } else {
                                // TODO: Handle if texts in item list not map pasted text.
                                let $combo = cell.element.find(".nts-combo-container")
                                let $comboInput = $($combo.find("input")[1]);
                                $comboInput.ntsError("set", "Pasted text not valid");
                                $combo.igCombo("text", "");
                                return;
                            }
                        } else {
                            specialColumn.tryDo(self.$grid, cell, cbData, visibleColumnsMap);
                            if (columnsGroup[columnIndex].dataType === "number") {
                                updatedRow[columnKey] = parseInt(cbData);
                            } else {
                                updatedRow[columnKey] = cbData;
                            }
                        }
                        updating.updateRow(self.$grid, $gridRow.data("id"), updatedRow, visibleColumnsMap);
                    });
                }
                
                pasteRangeHandler(evt: any) {
                    var cbData = this.getClipboardContent(evt);
                    if (utils.isEditMode(this.$grid)) {
                        cbData = this.processInEditMode(cbData);
                        this.updateInEditMode(cbData);
                    } else {
                        cbData = this.process(cbData);
                        this.pasteInMode === PasteMode.UPDATE ? this.updateWith(cbData) : this.addNew(cbData);
                    }
                }
                
                getClipboardContent(evt: any) {
                    if (window.clipboardData) {
                        window.event.returnValue = false;
                        return window.clipboardData.getData("text");
                    } else {
                        return evt.originalEvent.clipboardData.getData("text/plain");
                    }
                }
                
                private processInEditMode(data: string) {
                    if (util.isNullOrUndefined(data)) return;
                    return data.split("\n")[0];
                }
                
                private updateInEditMode(data: string) {
                    let selectedCell = selection.getSelectedCell(this.$grid);
                    let rowIndex = selectedCell.rowIndex;
                    let columnIndex = selectedCell.index;
                    let visibleColumnsMap = utils.getVisibleColumnsMap(this.$grid);
                    let updateRow = {};
                    let columnsGroup = utils.columnsGroupOfCell(selectedCell, visibleColumnsMap);
                    let columnKey = columnsGroup[columnIndex].key;
                    updateRow[columnKey] = data;
                    let $gridRow = utils.rowAt(selectedCell);
                    updating.updateRow(this.$grid, $gridRow.data("id"), updateRow, visibleColumnsMap);
                }
                
                private process(data: string) {
                    var dataRows = _.map(data.split("\n"), function(row) {
                        return row.split("\t");
                    });
                    
                    var rowsCount = dataRows.length;
                    if ((dataRows[rowsCount - 1].length === 1 && dataRows[rowsCount - 1][0] === "")
                        || dataRows.length === 1 && dataRows[0].length === 1 
                            && (dataRows[0][0] === "" || dataRows[0][0] === "\r")) {
                        dataRows.pop();
                    }
                    return dataRows;
                }
                
                private updateWith(data: any) {
                    var self = this;
                    if (!utils.selectable(this.$grid) || !utils.updatable(this.$grid)) return;
                    var selectedCell: any = selection.getSelectedCell(this.$grid);
                    if (selectedCell === undefined) return;
                    selectedCell.element.focus();
                    
                    var visibleColumnsMap = utils.getVisibleColumnsMap(self.$grid);
                    var visibleColumns = utils.visibleColumnsFromMap(visibleColumnsMap);
                    var columnIndex = selectedCell.index;
                    var rowIndex = selectedCell.rowIndex;
//                    if (!this.pasteable(columnIndex + data[0].length - visibleColumns.length)) return;
                    
                    let targetCol = _.find(visibleColumns, function(column: any) {
                        return column.key === selectedCell.columnKey;
                    });
                    if (util.isNullOrUndefined(targetCol)) return;
                    
                    _.forEach(data, function(row: any, idx: number) {
                        var $gridRow;
                        if (idx === 0) $gridRow = utils.rowAt(selectedCell);
                        else $gridRow = utils.nextNRow(selectedCell, idx);
                        if (util.isNullOrUndefined($gridRow)) return;
                        var rowData = {};
                        let targetIndex = columnIndex;
                        let targetCell = selectedCell;
                        let targetColumn = targetCol;
                        
                        // Errors
                        let comboErrors = [];
                        for (var i = 0; i < row.length; i++) {
                            let nextColumn;
                            let columnKey = targetColumn.key;
                            if ((!util.isNullOrUndefined(row[i]) && row[i].trim() === "null")
                                || !utils.isPastableControls(self.$grid, columnKey)) {
                                // Go to next column
                                nextColumn = utils.nextColumn(visibleColumnsMap, targetIndex, targetColumn.fixed);
                                targetColumn = nextColumn.options;
                                targetIndex = nextColumn.index;
                                continue;
                            }
                            let columnsGroup = utils.columnsGroupOfColumn(targetColumn, visibleColumnsMap);
                            if (targetIndex > columnsGroup.length - 1) break;
                            
                            let cellElement = self.$grid.igGrid("cellById", $gridRow.data("id"), columnKey);
                            if (utils.isComboBox(self.$grid, columnKey)) {
                                let cellContent = row[i].trim();
                                let copiedValue = ntsControls.comboBox.getCopiedValue({ element: cellElement[0] }, cellContent);
                                if (!util.isNullOrUndefined(copiedValue)) {
                                    rowData[columnKey] = targetColumn.dataType === "number" ? parseInt(copiedValue) : copiedValue;
                                } else {
                                    // TODO: Handle if copied text not match any item in combobox list
                                    comboErrors.push({ cell: cellElement, content: cellContent });
                                    
                                    // Go to next column
                                    nextColumn = utils.nextColumn(visibleColumnsMap, targetIndex, targetColumn.fixed);
                                    targetColumn = nextColumn.options;
                                    targetIndex = nextColumn.index;
                                    continue;
                                }
                            } else {
                                let cell: any = {};
                                cell.columnKey = columnKey;
                                cell.element = cellElement;
                                cell.id = $gridRow.data("id");
                                cell.index = targetIndex;
                                cell.row = $gridRow;
                                cell.rowIndex = $gridRow.data("rowIdx");
                                specialColumn.tryDo(self.$grid, cell, row[i].trim(), visibleColumnsMap);
                                
                                if (targetColumn.dataType === "number") {
                                    rowData[columnKey] = parseInt(row[i]);
                                } else {
                                    rowData[columnKey] = row[i];
                                }
                            }
                            // Go to next column
                            nextColumn = utils.nextColumn(visibleColumnsMap, targetIndex, targetColumn.fixed);
                            targetColumn = nextColumn.options;
                            targetIndex = nextColumn.index;
                        }
                        updating.updateRow(self.$grid, $gridRow.data("id"), rowData, visibleColumnsMap);    
                        _.forEach(comboErrors, function(combo: any) {
                            setTimeout(function() {
                                let $container = combo.cell.find(".nts-combo-container");
                                let $comboInput = $($container.find("input")[1]);
                                $comboInput.ntsError("set", "Pasted text not valid");
                                $container.igCombo("text", combo.content);
                            }, 0);
                        });
                    });
                }
                
                private addNew(data: any) {
                    var self = this;
//                    var visibleColumns = this.getVisibleColumns();
                    var visibleColumns = null;
                    if (!this.pasteable(data[0].length - visibleColumns.length)) return;
                    
                    _.forEach(data, function(row: any, idx: number) {
                        var rowData = {};
                        for (var i = 0; i < visibleColumns.length; i++) {
                            var columnKey = visibleColumns[i].key;
                            if (visibleColumns[i].dataType === "number") {
                                rowData[columnKey] = parseInt(row[i]);
                            } else {
                                rowData[columnKey] = row[i];
                            }
                        }
                        self.$grid.igGridUpdating("addRow", rowData);
                    });
                }
                
                private pasteable(excessColumns) {
                    if (excessColumns > 0) {
                        nts.uk.ui.dialog.alert("Copied table structure doesn't match.");
                        return false;
                    }
                    return true; 
                } 
            }
            
            export function ifOn($grid: JQuery, options: any) {
                if (options.ntsFeatures === undefined) return;
                _.forEach(options.ntsFeatures, function(f: any) {
                    if (f.name === feature.COPY_PASTE) {
                        Processor.addFeatures(options).chainEvents($grid);
                        return false;
                    }    
                });
            }   
        }
        
        module events {
            export class Handler {
                static KEY_DOWN: string = "keydown";
                static KEY_UP: string = "keyup";
                static FOCUS_IN: string = "focusin";
                static CLICK: string = "click";
                static MOUSE_DOWN: string = "mousedown";
                static GRID_EDIT_CELL_STARTED: string = "iggridupdatingeditcellstarted";
                static COLUMN_RESIZING: string = "iggridresizingcolumnresizing";
                static CELL_CLICK: string = "iggridcellclick";
                $grid: JQuery;
                preventEditInError: boolean;
                
                constructor($grid: JQuery, preventEditInError: boolean) {
                    this.$grid = $grid;
                    this.preventEditInError = preventEditInError;
                }
                
                static pull($grid: JQuery, preventEditInError?: boolean): Handler {
                    return new Handler($grid, preventEditInError);
                }
                
                turnOn($mainGrid?: JQuery) {
                    this.filter($mainGrid).onCellUpdate().onCellUpdateKeyUp().onDirectEnter().onSpacePress().onColumnResizing();
                }
                
                /**
                 * Handle enter direction.
                 */
                onDirectEnter() {
                    // Enter direction
                    var direction: selection.Direction = new selection.Direction();
                    this.$grid.on(Handler.KEY_DOWN, $.proxy(direction.bind, direction));
                    this.$grid.data(internal.ENTER_DIRECT, direction);
                    return this;
                }
                
                /**
                 * Handle cell edit.
                 */
                onCellUpdate() {
                    var self = this;
                    this.$grid.on(Handler.KEY_DOWN, function(evt: any) {
                        if (evt.ctrlKey) return;
                        let selectedCell: any = selection.getSelectedCell(self.$grid);
                        updating.triggerCellUpdate(evt, selectedCell);
                    });
                    return this;
                }
                
                /**
                 * Handle validation.
                 */
                onCellUpdateKeyUp() {
                    var self = this;
                    this.$grid.on(Handler.KEY_UP, function(evt: any) {
                        if (evt.ctrlKey) return;
                        let selectedCell: any = selection.getSelectedCell(self.$grid);
                        updating.onEditCell(evt, selectedCell);
                    });
                    return this;
                }
                
                /**
                 * Handle press space key on combobox.
                 */
                onSpacePress() {
                    var self = this;
                    self.$grid.on(Handler.KEY_DOWN, function(evt: any) {
                        if (!utils.isSpaceKey(evt)) return;
                        var selectedCell: any = selection.getSelectedCell(self.$grid);
                        if (util.isNullOrUndefined(selectedCell)) return;
                        var checkbox = $(selectedCell.element).find(".nts-checkbox-container");
                        if (checkbox.length > 0) {
                            checkbox.find("input[type='checkbox']").click();
                        }
                    });
                    return this;
                }
                
                /**
                 * Support copy paste.
                 */
                focusInWith(processor: copyPaste.Processor) {
                    this.$grid.on(Handler.FOCUS_IN, function(evt: any) {
                        if ($("#pasteHelper").length > 0 && $("#copyHelper").length > 0) return;
                        var pasteArea = $("<textarea id='pasteHelper'/>").css({ "opacity": 0, "overflow": "hidden" })
                                            .on("paste", $.proxy(processor.pasteHandler, processor));
                        var copyArea = $("<textarea id='copyHelper'/>").css({ "opacity": 0, "overflow": "hidden" });
                        $("<div/>").css({ "position": "fixed", "top": -10000, "left": -10000 })
                                    .appendTo($(document.body)).append(pasteArea).append(copyArea);
                    });
                    return this;
                }
                
                /**
                 * Copy, cut, paste events.
                 */
                ctrlCxpWith(processor: copyPaste.Processor) {
                    this.$grid.on(Handler.KEY_DOWN, function(evt: any) {
                        if (evt.ctrlKey && utils.isPasteKey(evt)) {
                            $("#pasteHelper").focus();
                        } else if (evt.ctrlKey && utils.isCopyKey(evt)) {
                            processor.copyHandler();
                        } else if (evt.ctrlKey && utils.isCutKey(evt)) {
                            processor.cutHandler();   
                        }
                    });
                    return this;
                }
                
                /**
                 * Prevent forwarding events in particular cases.
                 */
                filter($target?: JQuery) {
                    var self = this;
                    let $mainGrid = !util.isNullOrUndefined($target) ? $target : self.$grid; 
                    
                    self.$grid.on(Handler.KEY_DOWN, function(evt: any) {
                        if (utils.isAlphaNumeric(evt) || utils.isDeleteKey(evt)) {
                            let cell = selection.getSelectedCell($mainGrid);
                            if (cell === undefined || updating.containsNtsControl($(evt.target)))  
                                evt.stopImmediatePropagation();
                            return;
                        }
                        
                        if (utils.isTabKey(evt) && utils.isErrorStatus($mainGrid)) {
                            evt.preventDefault();
                            evt.stopImmediatePropagation();
                        }
                    });
                    
                    if (this.preventEditInError) {
                        self.$grid[0].addEventListener(Handler.MOUSE_DOWN, function(evt: any) {
                            if (utils.isNotErrorCell($mainGrid, evt)) {
                                evt.preventDefault();
                                evt.stopImmediatePropagation();
                            }
                        }, true);
                        self.$grid[0].addEventListener(Handler.CLICK, function(evt: any) {
                            if (utils.isNotErrorCell($mainGrid, evt)) {
                                evt.preventDefault();
                                evt.stopImmediatePropagation();
                            }
                        }, true);
                    }
                    
                    return this;
                }
                
                onColumnResizing() {
                    var self = this;
                    // Not fired on fixed table but main grid (table)
                    self.$grid.on(Handler.COLUMN_RESIZING, function(evt: any, args: any) {
                        columnSize.save(self.$grid, args.columnKey, args.desiredWidth);
                    });
                    return this;
                }
            }
            
            export function afterRendered(options: any) {
                options.rendered = function(evt: any, ui: any) {
                    let $grid = $(evt.target)
                    events.Handler.pull($grid, options.preventEditInError).turnOn();   
                    
                    // Bind events for fixed table part
                    let $fixedTbl = fixedColumns.getFixedTable($grid);
                    if ($fixedTbl.length === 0) return;
                    new copyPaste.Processor().chainEvents($grid, $fixedTbl);
                    events.Handler.pull($fixedTbl, options.preventEditInError).turnOn($grid);
                    // Load columns size
                    columnSize.load($grid);
                    // Set selected cell if any
                    let selectedCell = $grid.data(internal.SELECTED_CELL);
                    if (!util.isNullOrUndefined(selectedCell)) {
                        let fixedColumns = utils.getVisibleFixedColumns($grid);
                        if (_.find(fixedColumns, function(col) {
                                return col.key === selectedCell.columnKey;
                            }) !== undefined) {
                            $grid.igGrid("virtualScrollTo", selectedCell.rowIndex);
                            setTimeout(function() {
                                selection.selectCell($grid, selectedCell.rowIndex, selectedCell.index, true);
                            }, 1);
                        }
                    }
                    // Mark errors
                    errors.mark($grid);
                };
            }
            
            export function onCellClick($grid: JQuery) {
                $grid.on(Handler.CELL_CLICK, function(evt: any, ui: any) {
                    if (!utils.isEditMode($grid) && errors.any({ element: ui.cellElement })) {
                        _.defer(function() {
                            let $editor = $(ui.cellElement).find(errors.EDITOR_SELECTOR);
                            if ($editor.length === 0) return;
                            $editor.css(errors.ERROR_STL);
                        });
                    }
                });
            }
        }
        
        module validation {
            export let VALIDATORS: string = "ntsValidators"; 
            export class ColumnFieldValidator {
                name: string;
                primitiveValue: string;
                options: any;
                constructor(name: string, primitiveValue: string, options: any) {
                    this.name = name;
                    this.primitiveValue = primitiveValue;
                    this.options = options;
                }
                
                probe(value: string) {
                    let constraint = nts.uk.ui.validation.getConstraint(this.primitiveValue);
                    switch (constraint.valueType) {
                        case "String":
                            return new nts.uk.ui.validation.StringValidator(this.name, this.primitiveValue, this.options)
                                    .validate(value, this.options);
                        case "Integer":
                        case "Decimal":
                        case "HalfInt":
                            return new nts.uk.ui.validation.NumberValidator(this.name, this.primitiveValue, this.options)
                                    .validate(value);
                        case "Time":
                            this.options.mode = "time";
                            return new nts.uk.ui.validation.TimeValidator(this.name, this.primitiveValue, this.options)
                                    .validate(value);
                        case "Clock":
                            this.options.outputFormat = "time";
                            return new nts.uk.ui.validation.TimeValidator(this.name, this.primitiveValue, this.options)
                                    .validate(value);
                    }
                }
            }
            
            function getValidators(columnsDef: any) : { [columnKey: string]: ColumnFieldValidator } {
                var validators: any = {};
                _.forEach(columnsDef, function(def: any) {
                    if (def.constraint === undefined) return;
                    validators[def.key] = new ColumnFieldValidator(def.headerText, def.constraint.primitiveValue, def.constraint); 
                });
                return validators;
            }
            
            export function scanValidators($grid: JQuery, columnsDef: any) {
                $grid.data(VALIDATORS, getValidators(utils.analyzeColumns(columnsDef)));
            }
        }
        
        module errors {
            export let HAS_ERROR = "hasError";
            export let ERROR_STL = { "border-color": "#ff6666" };
            export let NO_ERROR_STL = { "border-color": "" };
            export let EDITOR_SELECTOR = "div.ui-igedit-container";
            
            export function mark($grid: JQuery) {
                let errorsLog = $grid.data(internal.ERRORS_LOG);
                if (util.isNullOrUndefined(errorsLog)) return;
                let sheets: any = $grid.data(internal.SHEETS);
                let sheetErrors = errorsLog[sheets.currentSheet];
                if (util.isNullOrUndefined(sheetErrors)) return;
                _.forEach(sheetErrors, function(cell: any) {
                    let $cell = $grid.igGrid("cellById", cell.id, cell.columnKey);
                    decorate($cell);
                });
            }
            
            function decorate($cell: any) {
                $cell.addClass(HAS_ERROR);
                $cell.css(ERROR_STL);
                let $editor = $cell.find(EDITOR_SELECTOR);
                if ($editor.length > 0) $editor.css(ERROR_STL); 
            }
            
            export function set($grid: JQuery, cell: any, message: string) {
                if (any(cell)) return;
                let $cell = $(cell.element);
                decorate($cell);
                ui.errors.add({
                    location: cell.rowIndex,
                    message: message,
                    $control: $cell,
                    messageText: ""
                });
                addErrorInSheet($grid, cell);
            }
            
            export function clear($grid: JQuery, cell: any) {
                if (!any(cell)) return;
                let $cell = $(cell.element);
                $cell.removeClass(HAS_ERROR);
                $cell.css(NO_ERROR_STL);
                let $editor = $cell.find(EDITOR_SELECTOR);
                if ($editor.length > 0) $editor.css(NO_ERROR_STL); 
                ui.errors.removeByElement($cell);
                removeErrorFromSheet($grid, cell);
            }
            
            export function any(cell: any) {
                return $(cell.element).hasClass(HAS_ERROR);
            }
            
            function addErrorInSheet($grid: JQuery, cell: any) {
                let errorsLog = $grid.data(internal.ERRORS_LOG) || {};
                let sheets: any = $grid.data(internal.SHEETS);
                if (util.isNullOrUndefined(errorsLog[sheets.currentSheet])) {
                    errorsLog[sheets.currentSheet] = [];
                } 
                errorsLog[sheets.currentSheet].push(cell);
                $grid.data(internal.ERRORS_LOG, errorsLog);
            }
            
            function removeErrorFromSheet($grid: JQuery, cell: any) {
                let errorsLog = $grid.data(internal.ERRORS_LOG);
                if (util.isNullOrUndefined(errorsLog)) return;
                let sheets: any = $grid.data(internal.SHEETS);
                let sheetErrors = errorsLog[sheets.currentSheet];
                if (util.isNullOrUndefined(sheetErrors)) return;
                let cellErrorIdx;
                _.forEach(sheetErrors, function(errorCell: any, i: number) {
                    if (cellEquals(errorCell, cell)) {
                        cellErrorIdx = i;
                        return false;
                    }
                });
                if (!util.isNullOrUndefined(cellErrorIdx)) {
                    errorsLog[sheets.currentSheet].splice(cellErrorIdx, 1);
                }
            }
            
            export function markIfError($grid: JQuery, cell: any) {
                let errorsLog = $grid.data(internal.ERRORS_LOG);
                if (util.isNullOrUndefined(errorsLog)) return;
                let sheets: any = $grid.data(internal.SHEETS);
                let sheetErrors = errorsLog[sheets.currentSheet];
                if (util.isNullOrUndefined(sheetErrors)) return;
                _.forEach(sheetErrors, function(c: any) {
                    if (cellEquals(c, cell)) {
                        decorate($(cell.element));
                        return false;
                    }
                });
            }
            
            function cellEquals(one: any, other: any) {
                if (one.columnKey !== other.columnKey) return false;
                if (one.rowIndex !== other.rowIndex) return false;
                if (one.id !== other.id) return false;
                return true;
            }
        }
        
        module color {
            export class CellFormatter {
                $grid: JQuery;
                colorFeatureDef: any;
                
                constructor($grid, features) {
                    this.$grid = $grid;
                    this.colorFeatureDef = this.getColorFeatureDef(features);
                }
                
                private getColorFeatureDef(features: any) {
                    let colorFeature = _.find(features, function(f: any) {
                        return f.name === feature.CELL_COLOR;
                    });
                    
                    if (colorFeature !== undefined) return colorFeature.columns;
                }
                
                format(column: any) {
                    var self = this;
                    if (util.isNullOrUndefined(this.colorFeatureDef) 
                        || column.formatter !== undefined) return column;
                    
                    column.formatter = function(value, rowObj) {
                        if (util.isNullOrUndefined(rowObj)) return value;
                        var _self = self;
                        setTimeout(function() {
                            let $gridCell = self.$grid.igGrid("cellById", rowObj[self.$grid.igGrid("option", "primaryKey")], column.key);
                            let $tr = $gridCell.closest("tr");
                            let cell = {
                                columnKey: column.key,
                                element: $gridCell[0],
                                rowIndex: $tr.data("rowIdx"),
                                id: $tr.data("id")
                            };
                            // If cell has error, mark it
                            errors.markIfError(self.$grid, cell);
                            
                            let aColumn = _.find(_self.colorFeatureDef, function(col: any) {
                                return col.key === column.key;
                            });
                            
                            if (util.isNullOrUndefined(aColumn)) return;
                            let cellColor = aColumn.map(aColumn.parse(value));
                            $gridCell.css("background-color", cellColor);
                        }, 0);
                        return value;
                    };
                    return column;
                }
            } 
        }
        
        module fixedColumns {
            export function getFixedTable($grid: JQuery): JQuery {
                return $("#" + $grid.attr("id") + "_fixed");
            }
            
            export function realGridOf($grid: JQuery) {
                if (utils.isIgGrid($grid)) return $grid;
                let gridId = $grid.attr("id");
                if (util.isNullOrUndefined(gridId)) return; 
                let endIdx = gridId.indexOf("_fixed"); 
                if (endIdx !== -1) {
                    let referGrid = $("#" + gridId.substring(0, endIdx)); 
                    if (!util.isNullOrUndefined(referGrid) && utils.fixable(referGrid))
                        return referGrid; 
                }
            }
        }
        
        module sheet {
            let normalStyles = { backgroundColor: '', color: '' };
            let selectedStyles = { backgroundColor: '#00B050', color: '#fff' };
            export class Configurator {
                currentSheet: string;
                sheets: any;
                
                constructor(currentSheet: string, sheets: any) {
                    this.currentSheet = currentSheet;
                    this.sheets = sheets;
                }
                
                static load($grid: JQuery, sheetFeature: any) {
                    let config = new Configurator(sheetFeature.initialDisplay, sheetFeature.sheets);
                    $grid.data(internal.SHEETS, config);
                }
            }
            
            /**
             * Unused
             */
            export function setup($grid: JQuery, options: any) {
                let sheetFeature = feature.find(options.ntsFeatures, feature.SHEET);
                if (util.isNullOrUndefined(sheetFeature)) return;
                let hidingFeature: any = { name: 'Hiding' };
                if (feature.isEnable(options.features, feature.HIDING)) {
                    feature.replaceBy(options, feature.HIDING, hidingFeature);
                } else {
                    options.features.push(hidingFeature);
                }
                
                Configurator.load($grid, sheetFeature);
                configButtons($grid, sheetFeature.sheets);
            }
            
            /**
             * Unused
             */
            function configButtons($grid: JQuery, sheets: any) {
                let gridWrapper = $("<div class='nts-grid-wrapper'/>");
                $grid.wrap($("<div class='nts-grid-container'/>")).wrap(gridWrapper);
                let gridContainer = $grid.closest(".nts-grid-container");
                let sheetButtonsWrapper = $("<div class='nts-grid-sheet-buttons'/>").appendTo(gridContainer);
                
                let sheetMng: any = $grid.data(internal.SHEETS);
                _.forEach(sheets, function(sheet: any) {
                    let btn = $("<button/>").addClass(sheet.name).text(sheet.text).appendTo(sheetButtonsWrapper);
                    if (sheetMng.currentSheet === sheet.name) btn.css(selectedStyles);
                    btn.on("click", function(evt: any) {
                        if (!utils.hidable($grid) || utils.isErrorStatus($grid)) return;
                        updateCurrentSheet($grid, sheet.name);
                        utils.showColumns($grid, sheet.columns);
                        hideOthers($grid);
                        
                        // Styles
                        sheetButtonsWrapper.find("button").css(normalStyles);
                        $(this).css(selectedStyles);
                    });
                });
            }
            
            export function hideOthers($grid: JQuery) {
                let sheetMng: any = $grid.data(internal.SHEETS);
                if (util.isNullOrUndefined(sheetMng)) return;
                let displayColumns;
                _.forEach(sheetMng.sheets, function(sheet: any) {
                    if (sheet.name !== sheetMng.currentSheet) {
                        utils.hideColumns($grid, sheet.columns);
                    } else {
                        displayColumns = sheet.columns;
                    }
                });
                
                // Resize displaying columns
                setTimeout(function() {
                    _.forEach(displayColumns, function(column: any) {
                         columnSize.loadOne($grid, column);
                    });
                }, 0);                
            }
            
            function updateCurrentSheet($grid: JQuery, name: string) {
                let sheetMng: any = $grid.data(internal.SHEETS);
                if (util.isNullOrUndefined(sheetMng)) return;
                sheetMng.currentSheet = name;
                $grid.data(internal.SHEETS, sheetMng);
            }
            
            export module load {
                
                export function setup($grid: JQuery, options: any) {
                    let sheetFeature = feature.find(options.ntsFeatures, feature.SHEET);
                    if (util.isNullOrUndefined(sheetFeature)) return;
                    Configurator.load($grid, sheetFeature);
                    configButtons($grid, sheetFeature.sheets);
                    if (!util.isNullOrUndefined($grid.data(internal.GRID_OPTIONS))) return; 
                    $grid.data(internal.GRID_OPTIONS, _.cloneDeep(options));
                    // Initial sheet
                    let sheetMng: any = $grid.data(internal.SHEETS);
                    let sheet: any = _.filter(sheetMng.sheets, function(sheet: any) {
                        return sheet.name === sheetMng.currentSheet;
                    });
                    
                    let columns = getSheetColumns(options.columns, sheet[0], options.features);
                    options.columns = columns;
                }
                
                function configButtons($grid: JQuery, sheets: any) {
                    if ($grid.closest(".nts-grid-container").length > 0) return;
                    let gridWrapper = $("<div class='nts-grid-wrapper'/>");
                    $grid.wrap($("<div class='nts-grid-container'/>")).wrap(gridWrapper);
                    let gridContainer = $grid.closest(".nts-grid-container");
                    let sheetButtonsWrapper = $("<div class='nts-grid-sheet-buttons'/>").appendTo(gridContainer);
                    
                    let sheetMng: any = $grid.data(internal.SHEETS);
                    _.forEach(sheets, function(sheet: any) {
                        let btn = $("<button/>").addClass(sheet.name).text(sheet.text).appendTo(sheetButtonsWrapper);
                        if (sheetMng.currentSheet === sheet.name) btn.css(selectedStyles);
                        btn.on("click", function(evt: any) {
                            if (utils.isErrorStatus($grid)) return;
                            updateCurrentSheet($grid, sheet.name);
                            let options = $grid.data(internal.GRID_OPTIONS);
                            let columns = getSheetColumns(options.columns, sheet, options.features);
                            let clonedOpts = _.cloneDeep(options);
                            clonedOpts.columns = columns;
                            clonedOpts.dataSource = $grid.igGrid("option", "dataSource");
                            $grid.igGrid("destroy");
                            $grid.off();
                            $grid.ntsGrid(clonedOpts);
                            
                            // Styles
                            sheetButtonsWrapper.find("button").css(normalStyles);
                            $(this).css(selectedStyles);
                        });
                    });
                }
                
                function getSheetColumns(allColumns: any, displaySheet: any, features: any) {
                    return _.filter(allColumns, function(column: any) {
                        if (column.group !== undefined && _.find(displaySheet.columns, function(col) {
                                return col === column.group[0].key;
                            }) !== undefined)
                            return true; 
                        
                        let belongToSheet = _.find(displaySheet.columns, function(col) {
                            return col === column.key;
                        }) !== undefined;
                        
                        let columnFixFeature = feature.find(features, feature.COLUMN_FIX);
                        if (!util.isNullOrUndefined(columnFixFeature)) {
                            return _.find(columnFixFeature.columnSettings, function(s: any) {
                                return s.columnKey === column.key;
                            }) !== undefined || belongToSheet;
                        }
                        return belongToSheet;
                    });
                }
            }
        }
        
        module settings {
            export function build($grid: JQuery, options: any) {
                let data: any = {};
                data.preventEditInError = options.preventEditInError;
                $grid.data(internal.SETTINGS, data);
            }
        }
        
        module internal {
            export let CONTROL_TYPES = "ntsControlTypesGroup";
            export let COMBO_SELECTED = "ntsComboSelection";
            export let GRID_OPTIONS = "ntsGridOptions";
            export let SELECTED_CELL = "ntsSelectedCell";
            export let SHEETS: string = "ntsGridSheets";
            export let SPECIAL_COL_TYPES = "ntsSpecialColumnTypes";
            export let ENTER_DIRECT = "enter";
            export let SETTINGS = "ntsSettings";
            export let ERRORS_LOG = "ntsErrorsLog";
        }
        
        module utils {
            
            export function isArrowKey(evt: any) {
                return evt.keyCode >= 37 && evt.keyCode <= 40;
            }
            export function isAlphaNumeric(evt: any) {
                return evt.keyCode >= 48 && evt.keyCode <= 90;
            }
            export function isTabKey(evt: any) {
                return evt.keyCode === 9;
            }
            export function isEnterKey(evt: any) {
                return evt.keyCode === 13;
            }
            export function isSpaceKey(evt: any) {
                return evt.keyCode === 32;
            }
            export function isDeleteKey(evt: any) {
                return evt.keyCode === 46;
            }
            export function isPasteKey(evt: any) {
                return evt.keyCode === 86;
            }
            export function isCopyKey(evt: any) {
                return evt.keyCode === 67;
            }
            export function isCutKey(evt: any) {
                return evt.keyCode === 88;
            }
            
            export function isErrorStatus($grid: JQuery) {
                let cell = selection.getSelectedCell($grid);
                return isEditMode($grid) && errors.any(cell);
            }
            /**
             * Only used in edit mode
             */
            export function isNotErrorCell($grid: JQuery, evt: any) {
                let cell = selection.getSelectedCell($grid);
                let $target = $(evt.target);
                let td = $target;
                if (!$target.prev().is("td")) td = $target.closest("td"); 
                return isEditMode($grid) && td.length > 0 && td[0] !== cell.element[0]
                        && errors.any(cell);
            }
            export function isEditMode($grid: JQuery) {
                return (updatable($grid) && $grid.igGridUpdating("isEditing"));
            }
            
            export function isIgGrid($grid: JQuery) {
                return !util.isNullOrUndefined($grid.data("igGrid"));
            }
            export function selectable($grid: JQuery) {
                return !util.isNullOrUndefined($grid.data("igGridSelection"));
            }
            export function updatable($grid: JQuery) {
                return !util.isNullOrUndefined($grid.data("igGridUpdating"));
            }
            export function fixable($grid: JQuery) {
                return !util.isNullOrUndefined($grid.data("igGridColumnFixing"));
            }
            export function hidable($grid: JQuery) {
                return !util.isNullOrUndefined($grid.data("igGridHiding"));
            }
            
            export function dataTypeOfPrimaryKey($grid: JQuery, visibleColumnsMap: any) : string {
                if (util.isNullOrUndefined(visibleColumnsMap)) return;
                let visibleColumns = visibleColumnsMap["undefined"];
                if (Object.keys(visibleColumnsMap).length > 1) {
                    visibleColumns = _.concat(visibleColumnsMap["true"], visibleColumnsMap["undefined"]);
                }
                let primaryKey = $grid.igGrid("option", "primaryKey");
                let keyColumn: Array<any> =  _.filter(visibleColumns, function(column: any) {
                    return column.key === primaryKey;
                });
                if (!util.isNullOrUndefined(keyColumn)) return keyColumn[0].dataType;
                return;
            }
            export function parseIntIfNumber(value: any, $grid: JQuery, visibleColumnsMap: any) {
                if (dataTypeOfPrimaryKey($grid, visibleColumnsMap) === "number") {
                    return parseInt(value);
                }
                return value;
            }
            
            export function isCopiableControls($grid: JQuery, columnKey: string) {
                let columnControlTypes = $grid.data(internal.CONTROL_TYPES);
                switch (columnControlTypes[columnKey]) {
                    case ntsControls.LINK_LABEL:
                    case ntsControls.TEXTBOX:
                    case ntsControls.LABEL:
                        return true;
                }
                return false;
            }
            export function isCuttableControls($grid: JQuery, columnKey: string) {
                let columnControlTypes = $grid.data(internal.CONTROL_TYPES);
                switch (columnControlTypes[columnKey]) {
                    case ntsControls.TEXTBOX:
                        return true;
                }
                return false;
            }
            export function isPastableControls($grid: JQuery, columnKey: string) {
                let columnControlTypes = $grid.data(internal.CONTROL_TYPES);
                switch (columnControlTypes[columnKey]) {
                    case ntsControls.LABEL:
                    case ntsControls.CHECKBOX:
                    case ntsControls.LINK_LABEL:
                    case ntsControls.COMBOBOX:
                        return false;
                }
                return true;
            }
            export function isComboBox($grid: JQuery, columnKey: string) {
                let columnControlTypes = $grid.data(internal.CONTROL_TYPES);
                if (columnControlTypes[columnKey] === ntsControls.COMBOBOX) return true;
                return false;
            }
            
            export function comboBoxOfCell(cell: any) {
                return $(cell.element).find(".nts-combo-container");
            }
            
            export function getColumns($grid: JQuery) {
                if (isIgGrid($grid)) {
                    return $grid.igGrid("option", "columns");
                }
                let referGrid = fixedColumns.realGridOf($grid);
                if (!util.isNullOrUndefined(referGrid)) return referGrid.igGrid("option", "columns");
            }
            export function getVisibleColumns($grid: JQuery) {
                return _.filter(getColumns($grid), function(column: any) {
                    return column.hidden !== true;
                }); 
            }
            
            export function getVisibleColumnsMap($grid: JQuery) {
                let visibleColumns = getVisibleColumns($grid);
                return _.groupBy(visibleColumns, "fixed");
            }
            export function getVisibleFixedColumns($grid: JQuery) {
                return _.filter(getColumns($grid), function(column: any) {
                    return column.hidden !== true && column.fixed === true;
                });
            }
            
            export function isFixedColumn(columnKey: any, visibleColumnsMap: any) {
                return _.find(visibleColumnsMap["true"], function(column: any) {
                    return column.key === columnKey;
                }) !== undefined;
            }
            export function isFixedColumnCell(cell: any, visibleColumnsMap: any) {
                return _.find(visibleColumnsMap["true"], function(column: any) {
                    return column.key === cell.columnKey;
                }) !== undefined;
            }
            export function columnsGroupOfColumn(column: any, visibleColumnsMap: any) {
                return visibleColumnsMap[column.fixed ? "true" : "undefined" ];
            }
            export function columnsGroupOfCell(cell: any, visibleColumnsMap: any) {
                if (isFixedColumnCell(cell, visibleColumnsMap)) return visibleColumnsMap["true"];
                return visibleColumnsMap["undefined"];
            }
            export function visibleColumnsFromMap(visibleColumnsMap: any) {
                return _.concat(visibleColumnsMap["true"], visibleColumnsMap["undefined"]);
            }
            export function noOfVisibleColumns(visibleColumnsMap: any) {
                return visibleColumnsMap["true"].length + visibleColumnsMap["undefined"].length;
            }
            export function getFixedColumns(visibleColumnsMap: any) {
                return visibleColumnsMap["true"];
            }
            export function getUnfixedColumns(visibleColumnsMap: any) {
                return visibleColumnsMap["undefined"];
            }
            
            export function nextColumn(visibleColumnsMap: any, columnIndex: number, isFixed: boolean) {
                if (util.isNullOrUndefined(visibleColumnsMap)) return;
                let nextCol: any = {};
                let mapKeyName = isFixed ? "true" : "undefined";
                let reverseKeyName = isFixed ? "undefined" : "true";
                if (columnIndex < visibleColumnsMap[mapKeyName].length - 1) {
                    return {
                                options: visibleColumnsMap[mapKeyName][columnIndex + 1],
                                index: columnIndex + 1
                           };
                } else if (columnIndex === visibleColumnsMap[mapKeyName].length - 1) {
                    return {
                                options: visibleColumnsMap[reverseKeyName][0],
                                index: 0
                           };
                }
            }
            export function nextColumnByKey(visibleColumnsMap: any, columnKey: string, isFixed: boolean) {
                if (util.isNullOrUndefined(visibleColumnsMap)) return;
                let currentColumnIndex;
                let currentColumn;
                let fixedColumns = visibleColumnsMap["true"];
                let unfixedColumns = visibleColumnsMap["undefined"];
                
                if (isFixed && fixedColumns.length > 0) {
                    _.forEach(fixedColumns, function(col: any, index: number) {
                        if (col.key === columnKey) {
                            currentColumnIndex = index;
                            currentColumn = col;
                            return false;
                        }
                    });
                    if (util.isNullOrUndefined(currentColumn) || util.isNullOrUndefined(currentColumnIndex)) return;
                    if (currentColumnIndex === fixedColumns.length - 1) {
                        return {
                            options: unfixedColumns[0],
                            index: 0
                        };
                    }
                    return {
                        options: fixedColumns[currentColumnIndex + 1],
                        index: currentColumnIndex + 1
                    }
                } 
                
                if (!isFixed && unfixedColumns.length > 0) {
                    _.forEach(unfixedColumns, function(col: any, index: number) {
                        if (col.key === columnKey) {
                            currentColumnIndex = index;
                            currentColumn = col;
                            return false;
                        }
                    });
                    if (util.isNullOrUndefined(currentColumn) || util.isNullOrUndefined(currentColumnIndex)) return;
                    if (currentColumnIndex === unfixedColumns.length - 1) {  
                        return {
                            options: fixedColumns.length > 0 ? fixedColumns[0] : unfixedColumns[0],
                            index: 0  
                        };
                    }
                    return {
                        options: unfixedColumns[currentColumnIndex + 1],
                        index: currentColumnIndex + 1
                    }
                }
            }
            
            export function rowAt(cell: any) {
                if (util.isNullOrUndefined(cell)) return;
                return $(cell.element).closest("tr");
            }
            export function nextNRow(cell: any, noOfNext: number) {
                return $(cell.element).closest("tr").nextAll("tr:eq(" + (noOfNext - 1) + ")");
            }
            export function getDisplayColumnIndex($grid: JQuery, cell: any) {
                let columns = $grid.igGrid("option", "columns");
                for (let i = 0; i < columns.length; i++) {
                    if (columns[i].key === cell.columnKey)
                        return i;
                }
                return -1;
            }
            
            export function startEdit($grid: JQuery, cell: any) {
                let visibleColumns = getVisibleColumns($grid);
                for (let i = 0; i < visibleColumns.length; i++) {
                    if (visibleColumns[i].key === cell.columnKey) {
                        $grid.igGridUpdating("startEdit", cell.id, i);
                        break;
                    }
                }
            }
            
            export function hideColumns($grid: JQuery, columns: Array<any>) {
                $grid.igGridHiding("hideMultiColumns", columns);
            }
            export function showColumns($grid: JQuery, columns: Array<any>) {
                $grid.igGridHiding("showMultiColumns", columns);
            }
            
            export function analyzeColumns(columns: any) {
                let flatCols = [];
                flatColumns(columns, flatCols);
                return flatCols;
            }
            
            function flatColumns(columns: any, flatCols: Array<any>) {
                _.forEach(columns, function(column) {
                    if (util.isNullOrUndefined(column.group)) {
                        flatCols.push(column);
                        return;
                    }
                    flatColumns(column.group, flatCols);
                });
            }
            
        }
    }
}

