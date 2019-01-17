/// <reference path="../../reference.ts"/>

interface JQuery {
    setupSearchScroll(controlType: string, param?: any): any;
}

module nts.uk.ui.jqueryExtentions {

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
                    if ($grid.igGridSelection('option', 'multipleSelection')) {
                        let chk = $grid.closest('.ui-iggrid').find(".ui-iggrid-rowselector-header").find("span[data-role='checkbox']");
                        if (chk[0].getAttribute("data-chk") == "on") {
                            return;
                        }
                    }
                    var selectedRows = $grid.igGrid("selectedRows");
                    var keyProperty = $grid.igGrid("option", "primaryKey");
                    var sourceKeys = _.map($grid.igGrid("option", "dataSource"), (o) => o[keyProperty]);
                    var selectedKeys = _.map(selectedRows, (o) => o["id"]);
                    if (_.isEqual(_.sortBy(_.uniq(sourceKeys)), _.sortBy(_.uniq(selectedKeys)))) {
                        return;    
                    }
                    if (selectedRows) {
                        row = selectedRows[0];
                    } else {
                        row = $grid.igGrid("selectedRow");
                    }
                    if (row) {
                        ui.ig.grid.virtual.expose(row, $grid);
                    }
                });
            } else {
                $grid.on("selectChange", function() {
                    var row = null;
                    if ($grid.igGridSelection('option', 'multipleSelection')) {
                        let chk = $grid.closest('.ui-iggrid').find(".ui-iggrid-rowselector-header").find("span[data-role='checkbox']");
                        if (chk[0].getAttribute("data-chk") == "on") {
                            return;
                        }
                    }
                    var selectedRows = $grid.igGrid("selectedRows");
                    var keyProperty = $grid.igGrid("option", "primaryKey");
                    var sourceKeys = _.map($grid.igGrid("option", "dataSource"), (o) => o[keyProperty]);
                    var selectedKeys = _.map(selectedRows, (o) => o["id"]);
                    if (_.isEqual(_.sortBy(_.uniq(sourceKeys)), _.sortBy(_.uniq(selectedKeys)))) {
                        return;    
                    }
                    if (selectedRows) {
                        row = selectedRows[0];
                    } else {
                        row = $grid.igGrid("selectedRow");
                    }
                    if (row) {
                        ui.ig.grid.expose(row, $grid);
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
                    ui.ig.tree.grid.expandTo(row.id, $treegrid);
                }
            });
            return $treegrid;
        }

        function setupIgTreeScroll($control: JQuery) {
            var id = $control.attr('id');
            $control.on("selectChange", function() {
                var selectedRows = $control.ntsTreeDrag("getSelected");   
                if ($.isArray(selectedRows)) {
                    selectedRows = selectedRows[0];
                } 
                if (!nts.uk.util.isNullOrUndefined(selectedRows)) {
                    $control.igTree("expandToNode", selectedRows.element);
                    let index = _.findIndex($control.find("li"), function(e){
                        return  $(e).is(selectedRows.element);     
                    });
                    if(index >= 0){
                        let scrollTo = index * 29;
                        let scrollTop = $control.scrollTop();
                        let height = $control.height();
                        if(scrollTo < scrollTop || scrollTo > scrollTop + height - 28){
                            $control.scrollTop(scrollTo);     
                        }       
                    }
                }
            });
            return $control;
        }
        
        $.fn.ntsSearchBox = function(options) {
            let self = this;
            let $container = $(self);
            
            if (typeof options === "string") {
                return delegateMethod($container, options, arguments[1]);
            }
            
            let minusWidth = 0;
            let fields = options.fields;
            
            let placeHolder = (options.placeHolder !== undefined) ? options.placeHolder : "コード・名称で検索・・・"; 
            
            let searchMode = (options.searchMode !== undefined) ? options.searchMode : "highlight";
            let defaultSearchText = (searchMode === 'highlight') ? '検索' : '絞り込み';
            let searchText = (options.searchText !== undefined) ? options.searchText : defaultSearchText;
            let label = (options.label !== undefined) ? options.label : "";
            let enable = options.enable;
            let dataSource = options.items;
            let childField = null;
            if (options.childField) {
                childField = options.childField;
            }
            let targetMode = options.mode;
            if (targetMode === "listbox") {
                targetMode = "igGrid";    
            }
            
            let tabIndex = nts.uk.util.isNullOrEmpty($container.attr("tabindex")) ? "0" : $container.attr("tabindex");
            $container.addClass("nts-searchbbox-wrapper").removeAttr("tabindex");
            $container.append("<div class='input-wrapper'><span class='nts-editor-wrapped ntsControl'><input class='ntsSearchBox nts-editor ntsSearchBox_Component' type='text' /></span></div>");  
            $container.append("<div class='input-wrapper'><button class='search-btn caret-bottom ntsSearchBox_Component'>" + searchText + "</button></div>"); 
            
            if (!nts.uk.util.isNullOrEmpty(label)) {
                var $formLabel = $("<div>", { text: label });
                $formLabel.prependTo($container);
                (<any>ko).bindingHandlers["ntsFormLabel"].init($formLabel[0], function() {
                    return {};　
                });
                minusWidth += $formLabel.outerWidth(true);
            }
            
            let $button = $container.find("button.search-btn");
            let $input = $container.find("input.ntsSearchBox");
            minusWidth += $button.outerWidth(true);
            if (searchMode === "filter") {
                $container.append("<button class='clear-btn ntsSearchBox_Component'>"+ nts.uk.ui.toBeResource.clear +"</button>"); 
                let $clearButton = $container.find("button.clear-btn");  
                minusWidth +=  $clearButton.outerWidth(true);
                $clearButton.click(function(evt: Event, ui: any) {
                    let component = $("#" + options.comId);    
                    if(component.hasClass("listbox-wrapper")){
                        component = $("#" + options.comId).find(".ntsListBox");     
                    }
                    let srh: koExtentions.SearchPub= $container.data("searchObject");
                    $input.val("");
                    component.igGrid("option", "dataSource", srh.seachBox.getDataSource());  
                    component.igGrid("dataBind"); 
                    $container.data("searchKey", null);    
                    component.attr("filtered", "false");     
                    _.defer(function() {
                        component.trigger("selectChange");    
                    });     
                });      
                
            }
            
            $input.attr("placeholder", placeHolder);
            $input.attr("data-name", nts.uk.ui.toBeResource.searchBox);
            $input.outerWidth($container.outerWidth(true) - minusWidth);　
            
            let primaryKey = options.targetKey;
            let searchObject = new koExtentions.SearchPub(primaryKey, searchMode, dataSource, fields, childField);
            $container.data("searchObject", searchObject);
            
            let search = function (searchKey: string){
                if (targetMode) {
                    let selectedItems, isMulti;
                    let component = $("#" + options.comId);   
                    if (targetMode == 'igGrid') {
                        if(component.hasClass("listbox-wrapper")){
                            component = $("#" + options.comId).find(".ntsListBox");     
                        }
                        selectedItems = component.ntsGridList("getSelected");
                        isMulti = component.igGridSelection('option', 'multipleSelection');
                    } else if (targetMode == 'igTree') {
                        selectedItems = component.ntsTreeView("getSelected");
                        isMulti = component.igTreeGridSelection('option', 'multipleSelection');
                    } else if (targetMode == 'igTreeDrag') {
                        selectedItems = component.ntsTreeDrag("getSelected");  
                        isMulti = component.ntsTreeDrag('option', 'isMulti') ;  
                    }
                    
                    let srh: koExtentions.SearchPub = $container.data("searchObject");
                    let result = srh.search(searchKey, selectedItems);
                    if(nts.uk.util.isNullOrEmpty(result.options)){
                        let mes = '';
                        if(searchMode === "highlight"){
                            mes = nts.uk.resource.getMessage("FND_E_SEARCH_NOHIT");
                        } else {
                            mes = nts.uk.ui.toBeResource.targetNotFound;    
                        }
                        nts.uk.ui.dialog.alert(mes).then(() => { 
                            $input.focus(); 
                            $input.select();
                        });
                        return false;        
                    }
                    
                    let selectedProperties = _.map(result.selectItems, primaryKey);
                    
                    if (targetMode === 'igGrid') {  
                        component.ntsGridList("setSelected", selectedProperties);
                        if(searchMode === "filter"){
                            $container.data("filteredSrouce", result.options); 
                            component.attr("filtered", "true");   
                            //selected(selectedValue);
                            //selected.valueHasMutated();
//                            let source = _.filter(srh.getDataSource(), function (item: any){
//                                             return _.find(result.options, function (itemFilterd: any){
//                                            return itemFilterd[primaryKey] === item[primaryKey];        
//                                                }) !== undefined || _.find(srh.getDataSource(), function (oldItem: any){
//                                             return oldItem[primaryKey] === item[primaryKey];        
//                                            }) === undefined;            
//                            });
//                            component.igGrid("option", "dataSource", _.cloneDeep(source));
                            component.igGrid("option", "dataSource", _.cloneDeep(result.options));  
                            component.igGrid("dataBind");  
                            
//                            if(nts.uk.util.isNullOrEmpty(selectedProperties)){
                                component.trigger("selectionchanged");        
//                            }
                        } else {
                            component.trigger("selectionchanged");    
                        }
                    } else if (targetMode == 'igTree') {
                        component.ntsTreeView("setSelected", selectedProperties);
                        component.trigger("selectionchanged");
                        //selected(selectedValue);
                    } else if(targetMode == 'igTreeDrag'){
                        component.ntsTreeDrag("setSelected", selectedProperties);
                    }
                    _.defer(function() {
                        component.trigger("selectChange");    
                    });
                    
                    $container.data("searchKey", searchKey);  
                }
                return true;    
            } 
            
            var nextSearch = function() {
                let searchKey = $input.val();
                if(nts.uk.util.isNullOrEmpty(searchKey)) {
                    nts.uk.ui.dialog.alert(nts.uk.resource.getMessage("FND_E_SEARCH_NOWORD")).then(() => { 
                        $input.focus(); 
//                        $input.select();
                    });
                    return false;        
                }
                return search(searchKey);    
            }
            $input.keydown(function(event) {
                if (event.which == 13) {
                    event.preventDefault();
                    let result = nextSearch();
                    _.defer(() => {
                        if(result){
                            $input.focus();         
                        }                
                    });
                }
            });
            $button.click(function() {
                nextSearch();    
            });
            
            $container.find(".ntsSearchBox_Component").attr("tabindex", tabIndex);
            
            if(enable === false){
                $container.find(".ntsSearchBox_Component").attr('disabled', 'disabled');        
            }
        };
        
        function delegateMethod($searchBox: JQuery, action: string, param?: any) {
            switch(action) {
                case "setDataSource":
                    setDataSource($searchBox, param);
                    break;
                case "setEnable":
                    setEnable($searchBox, param);
                    break;
            }
        }
        
        function setDataSource($searchBox: JQuery, dataSource: any) {
            let srhX: koExtentions.SearchPub= $searchBox.data("searchObject");
            srhX.setDataSource(dataSource);
        }
        
        function setEnable($searchBox: JQuery, enable: boolean) {
            if (enable === false) {
                $searchBox.find(".ntsSearchBox_Component").attr('disabled', 'disabled');        
            } else {
                $searchBox.find(".ntsSearchBox_Component").removeAttr('disabled');           
            }
        }
    }
}