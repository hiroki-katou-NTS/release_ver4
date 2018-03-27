/// <reference path="../../reference.ts"/>

interface JQuery {
    ntsGridList(action: string, param?: any): any;
    ntsGridListFeature(feature: string, action: string, ...params: any[]): any;
}

module nts.uk.ui.jqueryExtentions {

    module ntsGridList {

        let OUTSIDE_AUTO_SCROLL_SPEED = {
            RATIO: 0.2,
            MAX: 30
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
                    let scrollTop = $("#" + $grid.attr("id") + "_scrollContainer").scrollTop();
                    $(element).addClass('selected');
                    $parent.attr('data-value', selectedValue);
                    $grid.igGridUpdating("setCellValue", rowKey, columnKey, selectedValue);
                    $grid.igGrid("commit");
                    $grid.trigger("switchvaluechanged", {columnKey: columnKey, rowKey: rowKey, value: parseInt(selectedValue)});
                    if ($grid.igGrid("hasVerticalScrollbar")) {
//                        let current = $grid.ntsGridList("getSelected");
//                        if(current !== undefined){
//                            $grid.igGrid("virtualScrollTo", (typeof current === 'object' ? current.index : current[0].index) + 1);        
//                        }
                        if(!nts.uk.util.isNullOrUndefined(scrollTop) && scrollTop !== 0){
                            setTimeout(function (){
                                $("#" + $grid.attr("id") + "_scrollContainer").scrollTop(scrollTop);        
                            }, 10);
                        }
                    }
                }
            }
        }
        
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
                case 'setupScrollWhenBinding':
                    return setupScrollWhenBinding($grid);
            }
        };
        
        function setupScrollWhenBinding($grid: JQuery): any {
            let gridId = "#" + $grid.attr("id");
            $(document).delegate(gridId, "iggriddatarendered", function (evt, ui) {
                let oldSelected = getSelectRow($grid);
                if(!nts.uk.util.isNullOrEmpty(oldSelected)){
                    _.defer(() => { 
                        let selected = getSelectRow($grid);
                        if(!nts.uk.util.isNullOrEmpty(selected)){
                            selected = oldSelected;    
                        } 
                        let $scrollContainer: any = $grid.igGrid("scrollContainer");
                        _.defer(() => {
                            if ($scrollContainer.length > 0){
                                let firstRowOffset = $($("#single-list").igGrid("rowAt", 0)).offset().top;
                                let selectRowOffset = $($("#single-list").igGrid("rowAt", index)).offset().top;
                                $scrollContainer.scrollTop(selectRowOffset - firstRowOffset);
                            } else { 
                                let index = $(selected["element"]).attr("data-row-idx");
                                $grid.igGrid("virtualScrollTo", nts.uk.util.isNullOrEmpty(index) ? oldSelected.index : parseInt(index)); //.scrollTop(scrollTop);    
                            }   
                        });
                    });    
                }
            });
        }
        
        function getSelectRow($grid: JQuery) {
            var row = null;
            
            if($grid.data("igGrid")) {
                var selectedRows = $grid.igGrid("selectedRows");
                if (selectedRows) {
                    row = selectedRows[0];
                } else {
                    row = $grid.igGrid("selectedRow");
                }    
            }
            
            return row;
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
                    if (deleteField === true && primaryKey !== null && !util.isNullOrUndefined(row[primaryKey]) && $grid.data("enable") !== false) {
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


            $grid.bind('pointerdown', function(e) {

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
                $(window).bind('pointermove.NtsGridListDragging', function(e) {

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
                $(window).one('pointerup', function(e) {
                    mousePos = null;
                    dragSelectRange = [];
                    $(window).unbind('pointermove.NtsGridListDragging');
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

            $grid.unbind('pointerdown');
        }

        function unsetupSelectingEvents($grid: JQuery) {
            $grid.unbind('iggridselectionrowselectionchanged');

            //            $grid.off('mouseup');
        }
    }
}
