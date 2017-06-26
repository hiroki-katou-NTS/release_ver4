/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {
    
    /**
     * SwapList binding handler
     */
    class NtsSwapListBindingHandler implements KnockoutBindingHandler {
        
        swapper: SwapHandler;
        /**
         * Constructor.
         */
        constructor() {
        }

        /**
         * Init.
         */
        init(element: HTMLElement, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            var HEADER_HEIGHT = 27;
            var CHECKBOX_WIDTH = 70;
            var SEARCH_AREA_HEIGHT = 45;
            var BUTTON_SEARCH_WIDTH = 60;
            var INPUT_SEARCH_PADDING = 65;

            var $swap = $(element);
            var elementId = $swap.attr('id');
            if (nts.uk.util.isNullOrUndefined(elementId)) {
                throw new Error('the element NtsSwapList must have id attribute.');
            }

            var data = valueAccessor();
            var originalSource = ko.unwrap(data.dataSource !== undefined ? data.dataSource : data.options);
            //            var selectedValues = ko.unwrap(data.value);
            var totalWidth = ko.unwrap(data.width);
            var height = ko.unwrap(data.height);
            var showSearchBox = ko.unwrap(data.showSearchBox);
            var primaryKey: string = data.primaryKey !== undefined ? data.primaryKey : data.optionsValue;
            var columns: KnockoutObservableArray<any> = data.columns;

            $swap.wrap("<div class= 'ntsComponent ntsSwapList' id='" + elementId + "_container'/>");
            if (totalWidth !== undefined) {
                $swap.parent().width(totalWidth);
            }
            $swap.parent().height(height);
            $swap.addClass("ntsSwapList-container");

            var gridWidth = _.sumBy(columns(), c => {
                return c.width;
            });
            var iggridColumns = _.map(columns(), c => {
                c["key"] = c.key === undefined ? c.prop : c.key;
                c["dataType"] = 'string';
                return c;
            });
            var gridHeight = (height - 20);
            
            var grid1Id = "#" + elementId + "-grid1";
            var grid2Id = "#" + elementId + "-grid2";
            if (!util.isNullOrUndefined(showSearchBox) && (showSearchBox.showLeft || showSearchBox.showEright)) {
                
                var initSearchArea = function ($SearchArea, targetId, searchMode){
                    $SearchArea.append("<div class='ntsSearchTextContainer'/>")
                        .append("<div class='ntsSearchButtonContainer'/>");
                    if(searchMode === "filter"){
                        $SearchArea.append("<div class='ntsClearButtonContainer'/>");
                        $SearchArea.find(".ntsClearButtonContainer")
                            .append("<button id = " + searchAreaId + "-clear-btn" + " class='ntsSearchButton clear-btn'/>");  
                        $SearchArea.find(".clear-btn").text("検索");        
                    }
                    $SearchArea.find(".ntsSearchTextContainer")
                        .append("<input id = " + searchAreaId + "-input" + " class = 'ntsSearchInput ntsSearchBox'/>");
                    $SearchArea.find(".ntsSearchButtonContainer")
                        .append("<button id = " + searchAreaId + "-btn" + " class='ntsSearchButton search-btn caret-bottom'/>");
                    $SearchArea.find(".ntsSearchInput").attr("placeholder", "コード・名称で検索・・・");
                    $SearchArea.find(".search-btn").text("検索");  
                }
                
                var searchAreaId = elementId + "-search-area";
                $swap.append("<div class = 'ntsSearchArea' id = " + searchAreaId + "/>");
                var $searchArea = $swap.find(".ntsSearchArea");
                $searchArea.append("<div class='ntsSwapSearchLeft'/>")
                    .append("<div class='ntsSwapSearchRight'/>");
                $searchArea.css({position: "relative"});
                var searchAreaWidth = gridWidth + CHECKBOX_WIDTH;
                if(showSearchBox.showLeft){
                    var $searchLeftContainer = $swap.find(".ntsSwapSearchLeft");
                    
                    $searchLeftContainer.width(searchAreaWidth).css({position: "absolute", left: 0});
                    
                    initSearchArea($searchLeftContainer, grid1Id, data.searchMode);
                }
                
                if(showSearchBox.showRight){
                    var $searchRightContainer = $swap.find(".ntsSwapSearchRight");
                    
                    $searchRightContainer.width(gridWidth + CHECKBOX_WIDTH).css({position: "absolute", right: 0});
                    
                    initSearchArea($searchRightContainer, grid2Id, data.searchMode);
                }
                $searchArea.find(".ntsSearchBox").width(searchAreaWidth - BUTTON_SEARCH_WIDTH - INPUT_SEARCH_PADDING - (data.searchMode === "filter" ? BUTTON_SEARCH_WIDTH : 0));
                $searchArea.height(SEARCH_AREA_HEIGHT);
                gridHeight -= SEARCH_AREA_HEIGHT;
            }
            $swap.append("<div class= 'ntsSwapArea ntsGridArea'/>");
            $swap.find(".ntsGridArea").append("<div class = 'ntsSwapGridArea ntsSwapComponent' id = " + elementId + "-gridArea1" + "/>")
                .append("<div class = 'ntsMoveDataArea ntsSwapComponent' id = " + elementId + "-move-data" + "/>")
                .append("<div class = 'ntsSwapGridArea ntsSwapComponent' id = " + elementId + "-gridArea2" + "/>");

            $swap.find("#" + elementId + "-gridArea1").append("<table class = 'ntsSwapGrid' id = " + elementId + "-grid1" + "/>");
            $swap.find("#" + elementId + "-gridArea2").append("<table class = 'ntsSwapGrid' id = " + elementId + "-grid2" + "/>");

            var $grid1 = $swap.find(grid1Id);
            var $grid2 = $swap.find(grid2Id); 

            var features = [{ name: 'Selection', multipleSelection: true },
                            { name: 'Sorting', type: 'local' },
                            { name: 'RowSelectors', enableCheckBoxes: true, enableRowNumbering: true }];
          
            $swap.find(".nstSwapGridArea").width(gridWidth + CHECKBOX_WIDTH);
            
            var criterion = _.map(columns(), c => { return c.key === undefined ? c.prop : c.key; });
            var swapParts: Array<SwapPart> = new Array<SwapPart>();
            swapParts.push(new GridSwapPart().listControl($grid1)
                                .searchControl($swap.find(".ntsSwapSearchLeft").find(".search-btn")) 
                                .clearControl($swap.find(".ntsSwapSearchLeft").find(".clear-btn"))
                                .searchBox($swap.find(".ntsSwapSearchLeft").find(".ntsSearchBox"))
                                .setDataSource(originalSource)
                                .setSearchCriterion(data.searchCriterion || criterion)
                                .setSearchMode(data.searchMode || "highlight")
                                .setColumns(columns())
                                .setPrimaryKey(primaryKey)
                                .setInnerDrop((data.innerDrag && data.innerDrag.left !== undefined) ? data.innerDrag.left : true)
                                .setOuterDrop((data.outerDrag && data.outerDrag.left !== undefined) ? data.outerDrag.left : true)
                                .build());
            swapParts.push(new GridSwapPart().listControl($grid2)
                                .searchControl($swap.find(".ntsSwapSearchRight").find(".search-btn")) 
                                .clearControl($swap.find(".ntsSwapSearchRight").find(".clear-btn"))
                                .searchBox($swap.find(".ntsSwapSearchRight").find(".ntsSearchBox"))
                                .setDataSource(data.value())
                                .setSearchCriterion(data.searchCriterion || criterion)
                                .setSearchMode(data.searchMode || "highlight")
                                .setColumns(columns())
                                .setPrimaryKey(primaryKey)
                                .setInnerDrop((data.innerDrag && data.innerDrag.right !== undefined) ? data.innerDrag.right : true)
                                .setOuterDrop((data.outerDrag && data.outerDrag.right !== undefined) ? data.outerDrag.right : true)
                                .build());
            this.swapper = new SwapHandler().setModel(new GridSwapList($swap, swapParts));
            $grid1.igGrid({
                width: gridWidth + CHECKBOX_WIDTH,
                height: (gridHeight) + "px",
                primaryKey: primaryKey,
                columns: iggridColumns,
                virtualization: true,
                virtualizationMode: 'continuous',
                features: features
            });

            $grid1.closest('.ui-iggrid')
                .addClass('nts-gridlist')
                .height(gridHeight);

            $grid1.ntsGridList('setupSelecting');

            $grid2.igGrid({
                width: gridWidth + CHECKBOX_WIDTH,
                height: (gridHeight) + "px",
                primaryKey: primaryKey,
                columns: iggridColumns,
                virtualization: true,
                virtualizationMode: 'continuous',
                features: features
            });
            if (data.draggable === true) {
                this.swapper.enableDragDrop(data.value);
                if (data.multipleDrag && data.multipleDrag.left === true) {
                    this.swapper.Model.swapParts[0].$listControl.addClass("multiple-drag");
                }
                if (data.multipleDrag && data.multipleDrag.right === true) {
                    this.swapper.Model.swapParts[1].$listControl.addClass("multiple-drag");
                }
            }
            
            $grid2.closest('.ui-iggrid')
                .addClass('nts-gridlist')
                .height(gridHeight); 

            $grid2.ntsGridList('setupSelecting');

            var $moveArea = $swap.find("#" + elementId + "-move-data")
                .append("<button class = 'move-button move-forward'><i class='icon icon-button-arrow-right'></i></button>")
                .append("<button class = 'move-button move-back'><i class='icon icon-button-arrow-left'></i></button>");
            var $moveForward = $moveArea.find(".move-forward");
            var $moveBack = $moveArea.find(".move-back");

            var swapper = this.swapper;
            $moveForward.click(function() {
                swapper.Model.move(true, data.value);
            });
            $moveBack.click(function() {
                swapper.Model.move(false, data.value);
            });
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any,
            bindingContext: KnockoutBindingContext): void {
            var data = valueAccessor();
            var elementId = this.swapper.Model.$container.attr('id');
            var primaryKey: string = this.swapper.Model.swapParts[0].primaryKey;
            if (nts.uk.util.isNullOrUndefined(elementId)) {
                throw new Error('the element NtsSwapList must have id attribute.');
            }
            var $grid1 = this.swapper.Model.swapParts[0].$listControl;
            var $grid2 = this.swapper.Model.swapParts[1].$listControl;

            var currentSource = $grid1.igGrid('option', 'dataSource');
            var currentSelectedList = $grid2.igGrid('option', 'dataSource');
            var newSources = (data.dataSource !== undefined ? data.dataSource() : data.options());
            var newSelectedList = data.value();
            _.remove(newSources, function(item) {
                return _.find(newSelectedList, function(selected) {
                    return selected[primaryKey] === item[primaryKey];
                }) !== undefined;
            });
            if (!_.isEqual(currentSource, newSources)) {
                this.swapper.Model.swapParts[0].bindData(newSources.slice());
                this.swapper.Model.transportBuilder.setFirst(newSources);
            }

            if (!_.isEqual(currentSelectedList, newSelectedList)) {
                this.swapper.Model.swapParts[1].bindData(newSelectedList.slice());
                this.swapper.Model.transportBuilder.setSecond(newSelectedList);
            }
        }
        
        /**
         * Share swapper b/w init and update
         */
        makeBindings() : any {
            var handler = this;
            return {
                init: function(element, valueAccessor, allBindings, viewModel, bindingContext) {
                    var newHandler = Object.create(handler);
                    if (handler.init) {
                        handler.init.call(newHandler, element, valueAccessor, allBindings, viewModel, bindingContext);
                    }
                    
                    if (handler.update) {
                        ko.computed({
                            read: handler.update.bind(newHandler, element, valueAccessor, allBindings, viewModel, bindingContext),
                            disposeWhenNodeIsRemoved: element
                        });
                    }
                }
            };
        }
    }
    
    ko.bindingHandlers['ntsSwapList'] = new NtsSwapListBindingHandler().makeBindings();
    
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
        
        private handle(parts: Array<number>, value: (param?: any) => any) {
            var self = this;
            var model = this.model;
            for (var id in parts) {
                var options = {
                                    items: "tbody > tr",
                                    containment: this.model.$container,
                                    cursor: "move",
                                    connectWith: ".ntsSwapGrid",
                                    placeholder: "ui-state-highlight",
                                    helper: this._createHelper,
                                    appendTo: this.model.$container,
                                    start: function(evt, ui) {
                                        model.transportBuilder.at(model.sender(ui));
                                    },
                                    beforeStop: function(evt, ui) {
                                        self._beforeStop.call(this, model, evt, ui);
                                    },
                                    update: function(evt, ui) {
                                        self._update.call(this, model, evt, ui, value);
                                    }
                                };
                this.model.swapParts[parts[id]].initDraggable(options); 
            }
        }
        
        private _createHelper(evt: any, ui: any): void {
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
            var partId = model.transportBuilder.startAt === "first" ? 0 : 1;
            var destPartId = model.receiver(ui) === "first" ? 0 : 1;
            model.transportBuilder.toAdjacent(model.neighbor(ui)).target(model.target(ui));
            // In case of multiple selections
            if (ui.helper.hasClass("select-drag") === true) {
                var rowsInHelper = ui.helper.find("tr");
                var rows = rowsInHelper.toArray(); 
                if (model.transportBuilder.startAt === model.receiver(ui)
                    || (model.swapParts[partId].outerDrop === false
                        && model.transportBuilder.startAt !== model.receiver(ui))) {
                    $(this).sortable("cancel");
                    for (var idx in rows) {
                        model.swapParts[partId].$listControl.find("tbody").children()
                                                .eq($(rows[idx]).data("row-idx")).show();
                    }
                    return;
                } else {
                    var standardIdx = ui.placeholder.index();
                    var targetIdx = ui.item.data("row-idx");
                    var rowsBefore = new Array<any>();
                    var rowsAfter = new Array<any>();
                    for (var id in rows) {
                        if ($(rows[id]).data("row-idx") < targetIdx) rowsBefore.push(rows[id]);
                        else if ($(rows[id]).data("row-idx") > targetIdx) rowsAfter.push(rows[id]);
                    }
                    model.swapParts[destPartId].$listControl.find("tbody").children()
                                                .eq(standardIdx - 1).before(rowsBefore).after(rowsAfter);
                    // Remove rows in source
                    var sourceRows = model.swapParts[partId].$listControl.find("tbody").children(); 
                    for (var index = rowsAfter.length - 1; index >= 0; index--) {
                        if ($(rows[index]).data("row-idx") === targetIdx) continue;
                        sourceRows.eq($(rowsAfter[index]).data("row-idx") - 1).remove();
                    }
                    for (var val in rowsBefore) {
                        sourceRows.eq($(rowsBefore[val]).data("row-idx")).remove();
                    } 
                }
            } else if ((model.swapParts[partId].innerDrop === false
                && model.transportBuilder.startAt === model.receiver(ui))
                || (model.swapParts[partId].outerDrop === false
                    && model.transportBuilder.startAt !== model.receiver(ui))) {
                $(this).sortable("cancel");
            }
        }
        
        private _update(model: any, evt: any, ui: any, value: (param?: any) => any) {
            if (ui.item.closest("table").length === 0) return;
            model.transportBuilder.directTo(model.receiver(ui)).update();
            if (model.transportBuilder.startAt === model.transportBuilder.direction) {
                model.transportBuilder.startAt === "first" 
                ? model.swapParts[0].bindData(model.transportBuilder.getFirst())
                : model.swapParts[1].bindData(model.transportBuilder.getSecond());   
            } else {
                model.swapParts[0].bindData(model.transportBuilder.getFirst());
                model.swapParts[1].bindData(model.transportBuilder.getSecond());
            }
            value(model.transportBuilder.getSecond());
            setTimeout(function() { model.dropDone(); }, 0);
        }
        
        enableDragDrop(value: (param?: any) => any, parts?: Array<number>) {
            parts = parts || [0, 1];
            this.model.enableDrag(this, value, parts, this.handle);
        }
    }
    
    abstract class SwapModel implements ISwapAction {
        $container: JQuery;
        swapParts: Array<SwapPart>;
        transportBuilder: ListItemTransporter;
        
        constructor($container: JQuery, swapParts: Array<SwapPart>) {
            this.$container = $container;
            this.swapParts = swapParts;
            this.transportBuilder = new ListItemTransporter(this.swapParts[0].dataSource, this.swapParts[1].dataSource)
                                        .primary(this.swapParts[0].primaryKey);
        }
        
        abstract sender(param: any): string;
        abstract receiver(param: any): string;
        abstract target(param: any): any;
        abstract neighbor(param: any): string;
        abstract dropDone(): void;
        abstract enableDrag(ctx: any, value: (param?: any) => any, parts: Array<number>, cb: (parts: Array<number>, value: (param?: any) => any) => void): void;
        abstract move(forward: boolean, value: (param?: Array<any>) => Array<any>): void;
    }
    
    interface ISwapAction {
        sender(param: any): string;
        receiver(param: any): string;
        target(param: any): any;
        neighbor(param: any): string;
        dropDone(): void;
        enableDrag(ctx: any, value: (param?: any) => any, parts: Array<number>, cb: (parts: Array<number>, value: (param?: any) => any) => void): void;
        move(forward: boolean, value: (param?: Array<any>) => Array<any>): void;
    }
    
    class SearchResult {
        data: Array<any>[];
        indices: Array<number>;
        
        constructor(results: Array<any>[], indices: Array<number>) {
            this.data = results;
            this.indices = indices;
        }
    }
    
    abstract class SwapPart {
        $listControl: JQuery;
        $searchControl: JQuery;
        $clearControl: JQuery;
        $searchBox: JQuery;
        searchMode: string = "highlight"; // highlight & filter - Default: highlight
        searchCriterion: Array<string>;
        private originalDataSource: Array<any>;
        dataSource: Array<any>;
        columns: Array<any>;
        primaryKey: string;
        innerDrop: boolean = true;
        outerDrop: boolean = true;
        
        constructor() {
        }
        
        listControl($listControl: JQuery) {
            this.$listControl = $listControl;
            return this;
        }
        
        searchControl($searchControl: JQuery) {
            this.$searchControl = $searchControl;
            return this;
        }
        
        clearControl($clearControl: JQuery) {
            this.$clearControl = $clearControl;
            return this;
        }
        
        searchBox($searchBox: JQuery) {
            this.$searchBox = $searchBox;
            return this;
        }
        
        setSearchMode(searchMode: string) {
            this.searchMode = searchMode;
            return this;
        }
        
        setSearchCriterion(searchCriterion: Array<string>) {
            this.searchCriterion = searchCriterion;
            return this;
        }
        
        setDataSource(dataSource: Array<any>) {
            this.dataSource = dataSource;
            return this;
        }
        
        setColumns(columns: Array<any>) {
            this.columns = columns;
            return this;
        }
        
        setPrimaryKey(primaryKey: string) {
            this.primaryKey = primaryKey;
            return this;
        }
        
        setInnerDrop(innerDrop: boolean) {
            this.innerDrop = innerDrop;
            return this;
        }
        
        setOuterDrop(outerDrop: boolean) {
            this.outerDrop = outerDrop;
            return this;
        }
        
        initDraggable(opts: Object) {
            this.$listControl.sortable(opts).disableSelection();
        }
        
        resetOriginalDataSource() {
            this.originalDataSource = this.dataSource;
        }
        
        search(): SearchResult {
            var searchContents = this.$searchBox.val();
            var orders: Array<number> = new Array<number>(); 
            if (nts.uk.util.isNullOrEmpty(searchContents)) {
                nts.uk.ui.dialog.alert(nts.uk.resource.getMessage("FND_E_SEARCH_NOWORD"));
                return null;
            }
            var searchCriterion = this.searchCriterion;
            if (this.originalDataSource === undefined) this.resetOriginalDataSource();
            var results = _.filter(this.originalDataSource, function(value: any, index: number) {
                var found: boolean = false;
                _.forEach(searchCriterion, function(criteria) {
                    if (value[criteria].toString().indexOf(searchContents) !== -1) { 
                        found = true;
                        return false;
                    } else return true;
                });
                orders.push(index);
                return found;
            });
            return new SearchResult(results, orders);
        }
        
        abstract highlightSearch(): void;
        abstract bindIn(src: Array<any>[]): void;
        bindData(src: Array<any>): void {
            this.bindIn(src);
            this.dataSource = src;
        }
        
        private bindSearchEvent(): void {
            var self = this;
            var proceedSearch = this.proceedSearch;
            var clearFilter = this.clearFilter;
            this.$searchControl.click(function(evt, ui) {
                proceedSearch.apply(self);
            });
            
            this.$clearControl.click(function(evt, ui) {
                clearFilter.apply(self);;
            });
            
            this.$searchBox.keydown(function(evt, ui) {
                if (evt.which === 13) {
                    proceedSearch.apply(self);
                    _.defer(() => {
                        $input.focus();                
                    });
                }
            });
        }
        
        private proceedSearch() {
            if (this.searchMode === "filter") {
                var results: SearchResult = this.search();
                if (results === null) return;
                this.bindData(results.data);
            } else {
                this.highlightSearch();
            }
        }
        
        private clearFilter() {
            if (this.searchMode === "filter") {
                this.bindData(this.originalDataSource);
            }
        }
        
        build() {
            this.bindSearchEvent();
            return this;
        }
    }
    
    class GridSwapPart extends SwapPart {
        search(): SearchResult {
            return super.search();
        }
        
        highlightSearch(): void {
            var value = this.$searchBox.val();
            if (nts.uk.util.isNullOrEmpty(value)) {
                nts.uk.ui.dialog.alert(nts.uk.resource.getMessage("FND_E_SEARCH_NOWORD"));
                return;    
            }
            var source = this.dataSource.slice();
            var selected = this.$listControl.ntsGridList("getSelected");

            if (selected.length > 0) {
                var gotoEnd = source.splice(0, selected[0].index + 1);
                source = source.concat(gotoEnd);
            }
            var iggridColumns = _.map(this.columns, c => {
                c["key"] = c.key === undefined ? c.prop : c.key;
                c["dataType"] = 'string';
                return c;
            });
            var searchedValues = _.find(source, function(val) {
                return _.find(iggridColumns, function(x) {
                    return x !== undefined && x !== null && val[x["key"]].toString().indexOf(value) >= 0;
                }) !== undefined;
            });
            
            if(searchedValues === undefined){
                nts.uk.ui.dialog.alert(nts.uk.resource.getMessage("FND_E_SEARCH_NOHIT"));
                return;        
            }

            this.$listControl.ntsGridList('setSelected', searchedValues !== undefined ? [searchedValues[this.primaryKey]] : []);

            if (searchedValues !== undefined && (selected.length === 0 ||
                selected[0].id !== searchedValues[this.primaryKey])) {
                var current = this.$listControl.igGrid("selectedRows");
                if (current.length > 0 && this.$listControl.igGrid("hasVerticalScrollbar")) {
                    this.$listControl.igGrid("virtualScrollTo", current[0].index === source.length - 1
                        ? current[0].index : current[0].index + 1);
                }
            }
        }
        
        bindIn(src): void {
            this.$listControl.igGrid("option", "dataSource", src);
            this.$listControl.igGrid("dataBind");
        }
    }
    
    class GridSwapList extends SwapModel {
        
        sender(opts): string {
            return opts.item.closest("table")[0].id == this.swapParts[0].$listControl.attr("id")
                            ? "first" : "second";
        }
        
        receiver(opts): string {
            return opts.item.closest("table")[0].id == this.swapParts[1].$listControl.attr("id") 
                                            ? "second" : "first";
        }
        
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
            self.swapParts[0].$listControl.igGridSelection("clearSelection");
            self.swapParts[1].$listControl.igGridSelection("clearSelection");
            setTimeout(function() {
                (self.transportBuilder.direction === "first" 
                                    ? self.swapParts[0].$listControl 
                                    : self.swapParts[1].$listControl).igGrid("virtualScrollTo", 
                                                                        self.transportBuilder.incomeIndex); 
                (self.transportBuilder.startAt === "first"
                                    ? self.swapParts[0].$listControl
                                    : self.swapParts[1].$listControl).igGrid("virtualScrollTo",
                                                                        self.transportBuilder.outcomeIndex + 1);
            }, 0); 
        }
        
        enableDrag(ctx: any, value: (param?: any) => any, parts: Array<number>, cb: (parts: Array<number>, value: (param?: any) => any) => void): void {
            var self = this;
            for (var idx in parts) {
                this.swapParts[parts[idx]].$listControl.on("iggridrowsrendered", function(evt, ui) {
                    cb.call(ctx, parts, value);
                });
            }
        }
        
        move(forward: boolean, value: (param?: Array<any>) => Array<any>): void {
            var $source = forward === true ? this.swapParts[0].$listControl : this.swapParts[1].$listControl;
            var sourceList = forward === true ? this.swapParts[0].dataSource : this.swapParts[1].dataSource;
            var $dest = forward === true ? this.swapParts[1].$listControl : this.swapParts[0].$listControl;
            var destList = forward === true ? this.swapParts[1].dataSource : this.swapParts[0].dataSource;
            var selectedRows = $source.igGrid("selectedRows");
            selectedRows.sort(function(one, two) {
                return one.index - two.index; 
            });
            var selectedIds = selectedRows.map(function(row) { return row.id; });
            this.transportBuilder.at(forward ? "first" : "second").directTo(forward ? "second" : "first")
                    .target(selectedIds).toAdjacent(destList.length > 0 ? destList[destList.length - 1][this.swapParts[0].primaryKey] : null).update();
            this.swapParts[0].bindData(this.transportBuilder.getFirst());
            this.swapParts[1].bindData(this.transportBuilder.getSecond());
            value(this.transportBuilder.getSecond());
            $source.igGridSelection("clearSelection");
            $dest.igGridSelection("clearSelection");
            setTimeout(function() {
                $source.igGrid("virtualScrollTo", sourceList.length - 1 === selectedRows[0].index 
                                   ? selectedRows[0].index - 1 : selectedRows[0].index + 1);
                $dest.igGrid("virtualScrollTo", destList.length - 1);
            }, 10);
        }
    }
    
    class ListItemTransporter {
        private firstList: Array<any>;
        private secondList: Array<any>;
        startAt: string;
        direction: string;
        primaryKey: string;
        targetIds: Array<any>;
        adjacentIncomeId: any;
        outcomeIndex: number;
        incomeIndex: number;
        
        constructor(firstList: Array<any>, secondList: Array<any>) {
            this.firstList = firstList;
            this.secondList = secondList;
        }
        
        private first(firstList: Array<any>) : ListItemTransporter {
            this.firstList = firstList;
            return this;
        }
        
        private second(secondList: Array<any>) : ListItemTransporter {
            this.secondList = secondList;
            return this;
        }
        
        at(startAt: string) : ListItemTransporter {
            this.startAt = startAt;
            return this;
        }
        
        directTo(direction: string) : ListItemTransporter {
            this.direction = direction;
            return this;
        }
        
        out(index: number) : ListItemTransporter {
            this.outcomeIndex = index;
            return this;
        }
        
        into(index: number) : ListItemTransporter {
            this.incomeIndex = index;
            return this;
        }
        
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
        
        move(src: Array<any>, dest: Array<any>) {
            for (var i = 0; i < this.targetIds.length; i++) { 
                this.outcomeIndex = this.indexOf(src, this.targetIds[i]);
                if (this.outcomeIndex === -1) return;
                var target = src.splice(this.outcomeIndex, 1);
                this.incomeIndex = this.indexOf(dest, this.adjacentIncomeId) + 1;
                if (this.incomeIndex === 0) {
                    if (this.adjacentIncomeId === "ceil") this.incomeIndex = 0;
                    else if (target !== undefined) {
                        src.splice(this.outcomeIndex, 0, target[0]);
                        return;
                    }
                }
                dest.splice(this.incomeIndex + i, 0, target[0]);
            }
        }
        
        determineDirection() : string {
            if (this.startAt.toLowerCase() !== this.direction.toLowerCase()
                && this.direction.toLowerCase() === "second") {
                return "firstToSecond";
            } else if (this.startAt.toLowerCase() !== this.direction.toLowerCase()
                        && this.direction.toLowerCase() === "first") {
                return "secondToFirst";
            } else if (this.startAt.toLowerCase() === this.direction.toLowerCase()
                        && this.direction.toLowerCase() === "first") {
                return "insideFirst";
            } else return "insideSecond";
        }
        
        update() : void {
            switch (this.determineDirection()) {
                case "firstToSecond":
                    this.move(this.firstList, this.secondList);
                    break;
                case "secondToFirst":
                    this.move(this.secondList, this.firstList);
                    break;
                case "insideFirst":
                    this.move(this.firstList, this.firstList);
                    break;
                case "insideSecond":
                    this.move(this.secondList, this.secondList);
                    break;
            }
        }
        
        getFirst() : Array<any> {
            return this.firstList;
        }
        getSecond() : Array<any> {
            return this.secondList;
        }
        
        setFirst(first: Array<any>) {
            this.firstList = first;        
        }
        setSecond(second: Array<any>) {
            this.secondList = second;        
        }
    }
}