/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {

    /**
     * GridList binding handler
     */
    class NtsGridListBindingHandler implements KnockoutBindingHandler {

        init(element: HTMLElement, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            let HEADER_HEIGHT = 27;
            let ROW_HEIGHT = 23;
            let DIFF_NUMBER = 2;
            
            var $grid = $(element).addClass("nts-gridlist");
            let gridId = $grid.attr('id');
            if (nts.uk.util.isNullOrUndefined(gridId)) {
                throw new Error('the element NtsGridList must have id attribute.');
            }

            var data = valueAccessor();
            var optionsValue: string = data.primaryKey !== undefined ? data.primaryKey : data.optionsValue;
            var options = ko.unwrap(data.dataSource !== undefined ? data.dataSource : data.options);
            var deleteOptions = ko.unwrap(data.deleteOptions);
            var observableColumns = _.cloneDeep(ko.unwrap(data.columns));
            let selectionDisables = ko.unwrap(data.selectionDisables);
            var showNumbering = ko.unwrap(data.showNumbering) === true ? true : false;
            var columnResize: boolean = ko.unwrap(data.columnResize);
            var enable: boolean = ko.unwrap(data.enable);
            var value = ko.unwrap(data.value);
            let rowVirtualization = ko.unwrap(data.rowVirtualization) ? true : false;
            var virtualization = true;
            
            let rows = ko.unwrap(data.rows);
            $grid.data("init", true);
            $grid.data("selectionDisables", selectionDisables);
            $grid.data("initValue", value); 
            
            if (data.multiple){
                ROW_HEIGHT = 24;
                
                // Internet Explorer 6-11
                let _document: any = document;
                var isIE = /*@cc_on!@*/false || !!_document.documentMode;
                
                // Edge 20+
                let _window: any = window;
                var isEdge = !isIE && !!_window.StyleMedia; 
                if (isIE || isEdge) {
                    DIFF_NUMBER = -2;    
                }
            }
            var features = [];
            features.push({ name: 'Selection', multipleSelection: data.multiple });
            if(data.multiple || showNumbering){ 
                features.push({
                    name: 'RowSelectors',
                    enableCheckBoxes: data.multiple,
                    enableRowNumbering: false, //this feature is not needed
                    rowSelectorColumnWidth: 25
                });    
            }
            if(columnResize){
                features.push({
                    name: "Resizing"
                });
            }
            let tabIndex = $grid.attr("tabindex");
            $grid.data("tabindex", nts.uk.util.isNullOrEmpty(tabIndex) ? "0" : tabIndex);
            $grid.attr("tabindex", "-1");
            var gridFeatures = ko.unwrap(data.features);
            var iggridColumns = _.map(observableColumns, c => {
                c["key"] = c["key"] === undefined ? c["prop"] : c["key"];
                c["dataType"] = 'string';
                let formatter = c["formatter"];
                if(c["controlType"] === "switch"){
                    let switchF = _.find(gridFeatures, function(s){ 
                        return s["name"] === "Switch"
                    });
                    if(!util.isNullOrUndefined(switchF)){
                        features.push({name: 'Updating', enableAddRow: false, enableDeleteRow: false, editMode: 'none'});
                        let switchOptions = ko.unwrap(switchF['options']);
                        let switchValue = switchF['optionsValue']; 
                        let switchText = switchF['optionsText'];
                        c["formatter"] = function createButton(val, row) {
                            let result: JQuery = $('<div class="ntsControl"/>');
                            let rVal = nts.uk.util.isNullOrUndefined(formatter) ? val : formatter(val, row);
                            result.attr("data-value", rVal);
                            _.forEach(switchOptions, function(opt) {
                                let value = opt[switchValue];
                                let text = opt[switchText]; 
                                let btn = $('<button class="nts-switch-button" tabindex="-1"/>').text(text);
                                if ($grid.data("enable") === false){
                                    btn.attr("disabled", "disabled");      
                                }
                                btn.attr('data-value', value);
                                if (rVal == value) {
                                    btn.addClass('selected');
                                }
                                btn.appendTo(result);
                            });
                            return result[0].outerHTML;
                        };   
                        $grid.on("click", ".nts-switch-button", function(evt, ui) {
                            let $element = $(this);
                            let selectedValue = $element.attr('data-value');
                            let $tr = $element.closest("tr");  
                            $grid.ntsGridListFeature('switch', 'setValue', $tr.attr("data-id"), c["key"], selectedValue);
                        });  
                        
                        ROW_HEIGHT = 30;
                    }       
                } else {
                    let formatter = c.formatter;
                    c.formatter = function(val, row) {
                        if (row) {
                            setTimeout(() => {
                                let id = row[optionsValue];
                                let disables = $grid.data("selectionDisables");
                                if (!disables) return;
                                _.forEach(disables, d => {
                                    if (id === d) {
                                        let $row = $grid.igGrid("rowById", id, false);
                                        if (!$row.hasClass("row-disable")) $row.addClass("row-disable");
                                        return false;
                                    }
                                });
                            }, 0);
                        }
                        return nts.uk.util.isNullOrUndefined(formatter) ? val : formatter(val, row);
                    };
                }
                return c; 
            });
            
            let isDeleteButton = !util.isNullOrUndefined(deleteOptions) && !util.isNullOrUndefined(deleteOptions.deleteField)
                && deleteOptions.visible === true;
            
            let height = data.height;
            if(!nts.uk.util.isNullOrEmpty(rows)){
                if (isDeleteButton){
                    ROW_HEIGHT = 30;        
                }
                height = rows * ROW_HEIGHT + HEADER_HEIGHT - DIFF_NUMBER;   
                
                let colSettings = [];
                _.forEach(iggridColumns, function (c){
                    if(c["hidden"] === undefined || c["hidden"] === false){
                        colSettings.push({ columnKey: c["key"], allowTooltips: true }); 
                        if (nts.uk.util.isNullOrEmpty(c["columnCssClass"])) { 
                            c["columnCssClass"] = "text-limited";             
                        } else {
                            c["columnCssClass"] += " text-limited";
                        }
                    }      
                });
                
                features.push({
                    name: "Tooltips",
                    columnSettings: colSettings,
                    visibility: "overflow",
                    showDelay: 200,
                    hideDelay: 200
                });
                
                $grid.addClass("row-limited");
            }
            $grid.data("height", height);

            $grid.igGrid({
                width: data.width,
                height: height,
                primaryKey: optionsValue,
                columns: iggridColumns,
                virtualization: virtualization,
                virtualizationMode: 'continuous',
                rowVirtualization: rowVirtualization,
                features: features,
                tabIndex: -1
            });
            
            if (data.itemDraggable) {
                new SwapHandler().setModel(new GridSwapList($grid, optionsValue)).enableDragDrop(data.dataSource);
            }

            if (isDeleteButton) {
                var sources = (data.dataSource !== undefined ? data.dataSource : data.options);
                $grid.ntsGridList("setupDeleteButton", {
                    deleteField: deleteOptions.deleteField,
                    sourceTarget: sources
                });
            }

            $grid.ntsGridList('setupSelecting');
            
            if (data.multiple){
                $grid.bind('iggridrowselectorscheckboxstatechanging', (eventObject: JQueryEventObject, data: any) => {
                    if (String($grid.data("enable")) === "false") return false;
                    let disables = $grid.data("selectionDisables");
                    if (disables && !util.isNullOrUndefined(_.find(disables, d => data.rowKey === d))) {
                        return false;
                    }
                    return true;
                });
            }
            
            $grid.bind('iggridselectionrowselectionchanging', (eventObject: JQueryEventObject, ui: any) => {
                if (String($grid.data("enable")) === "false") return false;
                let disables = $grid.data("selectionDisables");
                if (disables && util.isNullOrUndefined(ui.startIndex)
                    && !util.isNullOrUndefined(_.find(disables, d => ui.row.id === d))) {
                    return false;
                }
                
                if (disables && util.isNullOrUndefined(ui.startIndex) 
                    && util.isNullOrUndefined(ui.row.id)) {
                    setTimeout(() => {
                        _.forEach(_.intersection(disables, value), iv => {
                            $grid.igGridSelection("selectRowById", iv);
                        });
                        
                        $grid.trigger("selectionchanged", [true]);
                    }, 0);
                }
                return true;
            });

            let $oselect, $iselect;
            let checkAll = function() {
                if ($oselect && $iselect && $oselect.attr("data-chk") === "off") {
                    $oselect.attr("data-chk", "on");
                    $iselect.removeClass("ui-igcheckbox-normal-off");
                    $iselect.addClass("ui-igcheckbox-normal-on");
                }
            };
            
            $grid.bind('selectionchanged', (event, isUserAction) => {
                if (isUserAction) {
                    $grid.data('user-action', true);
                }
                $grid.data("ui-changed", true);
                if (data.multiple) {
                    let selected: Array<any> = $grid.ntsGridList('getSelected');
                    
                    let disables = $grid.data("selectionDisables");
                    let disableIds = [];
                    if (disables) {
                        _.forEach(selected, (s, i) => {
                            _.forEach(disables, (d) => {
                                if (d === s.id && util.isNullOrUndefined(_.find(value, iv => iv === d))) {
                                    $grid.igGridSelection("deselectRowById", d);
                                    disableIds.push(i);
                                    return false;
                                }
                            });
                        });
                        
                        disableIds.sort((i1, i2) => i2 - i1).forEach(d => {
                            selected.splice(d, 1);
                        });
                        
                        let valueCount = _.intersection(disables, value).length; 
                        let ds = $grid.igGrid("option", "dataSource");
                        if (selected.length === ds.length - disables.length + valueCount) {
                            checkAll();
                        }
                    }
                    if (!nts.uk.util.isNullOrEmpty(selected)) {
                        let newValue = _.map(selected, s => s.id);
                        newValue = _.union(_.intersection(disables, value), newValue);
                        data.value(newValue);
                    } else {
                        data.value([]);
                    }
                } else {
                    let selected = $grid.ntsGridList('getSelected');
                    if (!nts.uk.util.isNullOrEmpty(selected)) {
                        data.value(selected.id);
                    } else {
                        data.value('');
                    }
                }
            });
            
            $grid.on("iggridvirtualrecordsrender", function(evt, ui) {
                let disables = $grid.data("selectionDisables");
                let $header = ui.owner._headerParent;
                if (!disables || !$header) return;
                let data = ui.owner.dataSource._data;
                let selected = $grid.ntsGridList('getSelected');
                let valueCount = _.intersection(disables, value).length;
                let selector = $header.find(".ui-iggrid-rowselector-header span");
                
                if (selector.length > 1) {
                    $oselect = $(selector[0]); 
                    $iselect = $(selector[1]);
                }
                
                if (selected && (data.length - disables.length + valueCount) === selected.length) {
                    checkAll();
                }
            });
            
            $grid.setupSearchScroll("igGrid", virtualization);
            $grid.ntsGridList("setupScrollWhenBinding");  
            
            $grid.bind("switchvaluechanged", function(evt, dataX){
                setTimeout(function(){
                    var source = _.cloneDeep(data.dataSource !== undefined ? data.dataSource() : data.options());
                    _.forEach(source, function(o){
                        if(o[optionsValue] === dataX.rowKey){
                            o[dataX.columnKey] = dataX.value;
                            return true;
                        }
                    })
                    $grid.data("ui-changed", true);
                    if(data.dataSource !== undefined){
                       data.dataSource(source);
                    } else {
                        data.options(source);    
                    }    
                }, 100);
            });
            
            $grid.bind("checknewitem", function(evt){
                return false;
            });
        }

        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {

            var $grid = $(element);
            var data = valueAccessor();
            var enable: boolean = ko.unwrap(data.enable);
            var optionsValue: string = data.primaryKey !== undefined ? data.primaryKey : data.optionsValue;
            var gridSource = $grid.igGrid('option', 'dataSource');
            var sources = (data.dataSource !== undefined ? data.dataSource() : data.options());
            let disables = ko.unwrap(data.selectionDisables);
            
            if($grid.data("enable") !== enable){
                if(!enable){
                    $grid.ntsGridList('unsetupSelecting');
                    $grid.addClass("disabled");     
                } else {
                    $grid.ntsGridList('setupSelecting');
                    $grid.removeClass("disabled");    
                }    
            }
            
            $grid.data("enable", enable);
            
            let currentDisables = $grid.data("selectionDisables");
            if (currentDisables && disables 
                && !_.isEqual(disables.sort((d1, d2) => d2 - d1), 
                    currentDisables.sort((d1, d2) => d2 - d1))) {
                $grid.data("selectionDisables", disables);
                
                let disableRows = function(arr) {
                    _.forEach(arr, d => {
                        let $row = $grid.igGrid("rowById", d, false);
                        if ($row && !$row.hasClass("row-disable")) {
                            $row.addClass("row-disable");
                        }
                    });
                };
                
                if (disables.length > currentDisables.length) {
                    disableRows(_.difference(disables, currentDisables));    
                } else {
                    _.forEach(currentDisables, d => {
                        let $row = $grid.igGrid("rowById", d, false);
                        if ($row && $row.hasClass("row-disable")) {
                            $row.removeClass("row-disable");
                        }
                    });
                    disableRows(disables);
                }
            }
            
            let currentSources = sources.slice();
            var currentSelectedItems = $grid.ntsGridList('getSelected');
            var removed = _.differenceWith(currentSelectedItems, currentSources, function (c1, c2){ return _.isEqual(c1.id.toString(), c2[optionsValue].toString())})
            if(!_.isEmpty(removed)){
                _.forEach(removed, (e) => {
                    $grid.igGridSelection("deselectRowById", e.id);    
                });  
                $grid.trigger("selectionchanged");
                currentSelectedItems = $grid.ntsGridList('getSelected');
            }
            
            if(String($grid.attr("filtered")) === "true"){
                let filteredSource = [];
                _.forEach(gridSource, function(item){
                    let itemX = _.find(sources, function (s){
                        return s[optionsValue] === item[optionsValue];        
                    });
                    if(!nts.uk.util.isNullOrUndefined(itemX)){ 
                        filteredSource.push(itemX);
                    }     
                });     
                if(!_.isEqual(filteredSource, gridSource)){
                    $grid.igGrid('option', 'dataSource', _.cloneDeep(filteredSource));
                    $grid.igGrid("dataBind");    
                }
            } else {
                
                var observableColumns = _.filter(ko.unwrap(data.columns), function(c){
                    c["key"] = c["key"] === undefined ? c["prop"] : c["key"];
                    return c["isDateColumn"] !== undefined && c["isDateColumn"] !== null && c["isDateColumn"] === true;
                });
                if(!nts.uk.util.isNullOrEmpty(observableColumns)){
                    _.forEach(currentSources, function(s){
                        _.forEach(observableColumns, function(c){
                            let key = c["key"] === undefined ? c["prop"] : c["key"];
                            s[key] = moment(s[key]).format(c["format"]);
                        });
                    });    
                }
                if (!_.isEqual(currentSources, gridSource)) {
                    $grid.igGrid('option', 'dataSource', _.cloneDeep(currentSources));
                    $grid.igGrid("dataBind");
                }
            }  
            var isEqual = _.isEqualWith(currentSelectedItems, data.value(), function(current, newVal) {
                if ((current === undefined && newVal === undefined) || (current !== undefined && current.id === newVal)) {
                    return true;
                }
            })
            if (!isEqual) {
                let clickCheckBox = false;
                if(!nts.uk.util.isNullOrEmpty(data.value())) {
                    let isSameSource = true,
                        sortedValue = _.sortBy(data.value()),
                        sortedSource = _.sortBy(sources, [optionsValue]);
                    _.forEach(sortedValue, (v, i) => {
                        if (v !== sortedSource[i][optionsValue]) {
                            isSameSource = false;
                            return false;
                        }
                    });
                    
                    if(isSameSource && data.value().length == sources.length && $grid.igGridSelection('option', 'multipleSelection')) {
                        let features = _.find($grid.igGrid("option", "features"), function (f){
                            return f.name === "RowSelectors";     
                        });
                        clickCheckBox = !nts.uk.util.isNullOrUndefined(features.enableCheckBoxes) && features.enableCheckBoxes;
                    }
                }
                if(clickCheckBox){
                    let $checkBox = $grid.closest('.ui-iggrid').find(".ui-iggrid-rowselector-header").find("span[data-role='checkbox']");
                    if ($checkBox.length > 0 && $checkBox[0].getAttribute("data-chk") === "off") {
                        $checkBox.click();
                    }
                } else {
                    $grid.ntsGridList('setSelected', data.value());    
                }
                
                let initVal = $grid.data("initValue");
                if (!disables || !initVal || _.intersection(disables, initVal).length === 0) { 
                    _.defer(() => {$grid.trigger("selectChange");});
                }
            }
            
            _.defer( () => {
                if ( $grid.data('user-action')) {
                    $grid.data('user-action', false);
                } else {
                    $grid.ntsGridList("scrollToSelected");
                }
            });
            $grid.data("ui-changed", false);
            $grid.closest('.ui-iggrid').addClass('nts-gridlist').height($grid.data("height")).attr("tabindex", $grid.data("tabindex"));
        }
    }
    
    ko.bindingHandlers['ntsGridList'] = new NtsGridListBindingHandler();
    
    class SwapHandler {
        private model: SwapModel;
        constructor() {
        }
        setModel(model: SwapModel) {
            this.model = model;
            return this;
        }
        get Model() {
            return this.model;
        }
        
        private handle(value: (param?: any) => any) {
            var self = this;
            var model = this.model;
            var options = {
                                items: "tbody > tr",
                                containment: this.model.$grid,
                                cursor: "move",
                                connectWith: this.model.$grid,
                                placeholder: "ui-state-highlight",
                                helper: this._createHelper,
                                appendTo: this.model.$grid,
                                start: function(evt, ui) {
                                    self.model.transportBuilder.setList(self.model.$grid.igGrid("option", "dataSource"));
                                },
                                beforeStop: function(evt, ui) {
                                    self._beforeStop.call(this, model, evt, ui);
                                },
                                update: function(evt, ui) {
                                    self._update.call(this, model, evt, ui, value);
                                }
                            };
            this.model.$grid.sortable(options).disableSelection();
        }
        
        private _createHelper(evt: Event, ui: any): Element {
            var selectedRowElms = $(evt.currentTarget).igGrid("selectedRows");
            // Set the orders same as on grid
            selectedRowElms.sort(function(one, two) {
                return one.index - two.index;
            });
            var $helper;
            if ($(evt.currentTarget).hasClass("multiple-drag") && selectedRowElms.length > 1) {
                $helper = $("<div><table><tbody></tbody></table></div>").addClass("select-drag");
                var rowId = ui.data("row-idx");
                var selectedItems: Array<JQuery> = selectedRowElms.map(function(elm) { return elm.element; });
                var height = 0;
                $.each(selectedItems, function() {
                    $helper.find("tbody").append($(this).clone()); 
                    height += $(this).outerHeight();
                    if (rowId !== this.data("row-idx")) $(this).hide(); 
                });
                $helper.height(height);
                $helper.find("tr").first().children().each(function(idx) {
                    $(this).width(ui.children().eq(idx).width());
                });
            } else {
                $helper = ui.clone();
                $helper.children().each(function(idx) {
                    $(this).width(ui.children().eq(idx).width());
                });
            }
            return $helper[0];
        }
        
        private _beforeStop(model:any, evt: any, ui: any): void {
            model.transportBuilder.toAdjacent(model.neighbor(ui)).target(model.target(ui));
            // In case of multiple selections
            if (ui.helper.hasClass("select-drag")) {
                var rowsInHelper = ui.helper.find("tr");
                var rows = rowsInHelper.toArray();
                $(this).sortable("cancel");
                for (var idx in rows) {
                    model.$grid.find("tbody").children().eq($(rows[idx]).data("row-idx")).show();
                }
            } 
        }
        
        private _update(model: any, evt: any, ui: any, value: (param?: any) => any) {
            if (ui.item.closest("table").length === 0) return;
            model.transportBuilder.update();
            model.$grid.igGrid("option", "dataSource", model.transportBuilder.getList());
            value(model.transportBuilder.getList());
            setTimeout(function() { model.dropDone(); }, 0);
        }
        
        enableDragDrop(value: (param?: any) => any) {
            this.model.enableDrag(this, value, this.handle);
        }
    }
    
    abstract class SwapModel {
        $grid: JQuery;
        primaryKey: any;
        transportBuilder: ListItemTransporter;
        
        constructor($grid: JQuery, primaryKey: any) {
            this.$grid = $grid;
            this.primaryKey = primaryKey;
            this.transportBuilder = new ListItemTransporter().primary(this.primaryKey);
        }
        
        abstract target(param: any): any;
        abstract neighbor(param: any): string;
        abstract dropDone(): void;
        abstract enableDrag(ctx: any, value: (param?: any) => any, cb: (value: (param?: any) => any) => void): void;
    }
    
    class GridSwapList extends SwapModel {
        
        target(opts): any {
            if (opts.helper !== undefined && opts.helper.hasClass("select-drag")) {
                return opts.helper.find("tr").map(function() {
                    return $(this).data("id");
                });
            } 
            return [opts.item.data("id")];
        }
        
        neighbor(opts): any {
            return opts.item.prev().length === 0 ? "ceil" : opts.item.prev().data("id");
        }
        
        dropDone(): void {
            var self = this; 
            self.$grid.igGridSelection("clearSelection");
            setTimeout(function() {
                self.$grid.igGrid("virtualScrollTo", self.transportBuilder.incomeIndex);
            }, 0); 
        }
        
        enableDrag(ctx: any, value: (param?: any) => any, cb: (value: (param?: any) => any) => void): void {
            var self = this;
            this.$grid.on("iggridrowsrendered", function(evt, ui) {
                cb.call(ctx, value);
            });
        }
    }
    
    class ListItemTransporter {
        list: Array<any>;
        primaryKey: string;
        targetIds: Array<any>;
        adjacentIncomeId: any;
        outcomeIndex: number;
        incomeIndex: number;
        
        primary(primaryKey: string) : ListItemTransporter {
            this.primaryKey = primaryKey;
            return this;
        }
        
        target(targetIds: Array<any>) : ListItemTransporter {
            this.targetIds = targetIds;
            return this;
        }
        
        toAdjacent(adjId: any) : ListItemTransporter {
            if (adjId === null) adjId = "ceil";
            this.adjacentIncomeId = adjId;
            return this;
        }
        
        indexOf(list: Array<any>, targetId: any) {
            return _.findIndex(list, elm => elm[this.primaryKey].toString() === targetId.toString());
        }
        
        update() : void {
            for (var i = 0; i < this.targetIds.length; i++) { 
                this.outcomeIndex = this.indexOf(this.list, this.targetIds[i]);
                if (this.outcomeIndex === -1) return;
                var target = this.list.splice(this.outcomeIndex, 1);
                this.incomeIndex = this.indexOf(this.list, this.adjacentIncomeId) + 1;
                if (this.incomeIndex === 0) {
                    if (this.adjacentIncomeId === "ceil") this.incomeIndex = 0;
                    else if (target !== undefined) {
                        this.list.splice(this.outcomeIndex, 0, target[0]);
                        return;
                    }
                }
                this.list.splice(this.incomeIndex + i, 0, target[0]);
            }
        }
        
        getList() : Array<any> {
            return this.list;
        }
        
        setList(list: Array<any>) {
            this.list = list;        
        }
    }
}
