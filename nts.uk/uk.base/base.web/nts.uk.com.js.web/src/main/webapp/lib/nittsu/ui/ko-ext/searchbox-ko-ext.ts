/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {
    
     /**
     * SearchBox Binding Handler
     */
    
    class SearchBox {
        childField: string;
        searchField: Array<string>;
        source: Array<any>;
        
        constructor(source: Array<any>, searchField: Array<string>, childField?: string) {
            this.childField = childField;    
            this.source = nts.uk.util.isNullOrEmpty(source) ? [] : this.cloneDeep(source);
            this.searchField = searchField;
        }
        
        search(searchKey: string): Array<any>{
            let self = this;
            if(nts.uk.util.isNullOrEmpty(this.source)){
                return [];        
            }
            
            var flatArr = nts.uk.util.flatArray(this.source, this.childField);
            
            var filtered = _.filter(flatArr, function(item: any) {
                return _.find(self.searchField, function(x: string) {
                    if(x !== undefined && x !== null){
                        let val: string = item[x].toString();
                        return val.indexOf(searchKey.toString()) >= 0;        
                    }
                    return false;
                }) !== undefined;        
            });
            
            return filtered;
        }  
        
        setDataSource(source: Array<any>) {
            this.source = nts.uk.util.isNullOrEmpty(source) ? [] : this.cloneDeep(source);       
        }
        
        getDataSource() {
            return this.cloneDeep(this.source);       
        }
        
        private cloneDeep(source: Array<any>): Array<any>{
            let self = this;
            return self.cloneDeepX(source); 
        }
        
        private cloneDeepX(source: Array<any>): Array<any>{
            return _.cloneDeep(source); 
        }
    }
    
    class SearchResult {
        options: Array<any>;
        selectItems: Array<any>;  
        
        constructor(){
            this.options = []; 
            this.selectItems = [];   
        }
    }
    
    export class SearchPub {
        seachBox: SearchBox;
        mode: string;  
        key: string;
        
        constructor(key: string, mode: string, source: Array<any>, searchField: Array<string>, childField?: string) {
            this.seachBox = new SearchBox(source, searchField, childField);;    
            this.mode = nts.uk.util.isNullOrEmpty(mode) ? "highlight" : mode;
            this.key = key;
        }
        
        search (searchKey: string, selectedItems: Array<any>): SearchResult{
            let result = new SearchResult();   
            
            let filtered = this.seachBox.search(searchKey);
            
            if(!nts.uk.util.isNullOrEmpty(filtered)){
                let key = this.key;
                if(this.mode === "highlight"){     
                    result.options = this.seachBox.getDataSource();
                    let index = 0;
                    if (!nts.uk.util.isNullOrEmpty(selectedItems)) {
                        let firstItemValue = $.isArray(selectedItems) 
                            ? selectedItems[0]["id"].toString(): selectedItems["id"].toString();
                        index = _.findIndex(filtered, function(item: any){
                            return item[key].toString() === firstItemValue;           
                        });   
                        if(!nts.uk.util.isNullOrUndefined(index)){
                            index++;        
                        }                 
                    }  
                    if(index >= 0){
                        result.selectItems = [filtered[index >= filtered.length ? 0 : index]];        
                    }
                } else if (this.mode === "filter") {
                    result.options = filtered;   
                    let selectItem = _.filter(filtered, function (itemFilterd: any){
                        return _.find(selectedItems, function (item: any){
                            let itemVal = itemFilterd[key];
                            return itemVal === item["id"];        
                        }) !== undefined;            
                    }); 
                    result.selectItems = selectItem;
                }    
            }
            
            return result;
        }
        
        public setDataSource(source: Array<any>) {
            this.seachBox.setDataSource(source);       
        }
        
        public getDataSource() {
            return this.seachBox.getDataSource();       
        }
    }
    
    class NtsSearchBoxBindingHandler implements KnockoutBindingHandler {
        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            
            var data = ko.unwrap(valueAccessor());
            var fields = ko.unwrap(data.fields);
            var searchText = (data.searchText !== undefined) ? ko.unwrap(data.searchText) : "検索";
            var placeHolder = (data.placeHolder !== undefined) ? ko.unwrap(data.placeHolder) : "コード・名称で検索・・・"; 

            var searchMode = (data.searchMode !== undefined) ? ko.unwrap(data.searchMode) : "highlight";
            var enable = ko.unwrap(data.enable);
            var selectedKey = null;
            if (data.selectedKey) {
                selectedKey = ko.unwrap(data.selectedKey);
            }
            var dataSource = ko.unwrap(data.items);
            var childField = null;
            if (data.childField) {
                childField = ko.unwrap(data.childField);
            }
            var component;
            let targetMode = data.mode;
            if (targetMode === "listbox") {
                component = $("#" + ko.unwrap(data.comId)).find(".ntsListBox");
                targetMode = "igGrid";    
            } else {
                component = $("#" + ko.unwrap(data.comId));    
            }
            
            var $container = $(element);
            let tabIndex = nts.uk.util.isNullOrEmpty($container.attr("tabindex")) ? "0" : $container.attr("tabindex");
            $container.addClass("nts-searchbbox-wrapper").removeAttr("tabindex");
            $container.append("<span class='nts-editor-wrapped ntsControl'><input class='ntsSearchBox nts-editor ntsSearchBox_Component' type='text' /></span>");  
            $container.append("<button class='search-btn caret-bottom ntsSearchBox_Component'>" + searchText + "</button>"); 
            
            var $button = $container.find("button.search-btn");
            var $input = $container.find("input.ntsSearchBox");
            let buttonWidth = $button.outerWidth(true);
            if(searchMode === "filter"){
                $container.append("<button class='clear-btn ntsSearchBox_Component'>解除</button>"); 
                let $clearButton = $container.find("button.clear-btn");  
                buttonWidth +=  $clearButton.outerWidth(true);
                $clearButton.click(function(evt: Event, ui: any) {
                    if(component.length === 0){
                        component = $("#" + ko.unwrap(data.comId)).find(".ntsListBox");    
                    }
                    let srh: SearchPub= $container.data("searchObject");
                    component.igGrid("option", "dataSource", srh.seachBox.getDataSource());  
                    component.igGrid("dataBind"); 
                    $container.data("searchKey", null);    
                    component.attr("filtered", false);     
                    _.defer(function() {
                        component.trigger("selectChange");    
                    });     
                });      
                
            }
            
            $input.attr("placeholder", placeHolder);
            $input.attr("data-name", "検索テキストボックス");
            $input.outerWidth($container.outerWidth(true) - buttonWidth);　
            
            let primaryKey = ko.unwrap(data.targetKey);
            let searchObject = new SearchPub(primaryKey, searchMode, dataSource, fields, childField);
            $container.data("searchObject", searchObject);
            
            let search = function (searchKey: string){
                if (targetMode) {
                    let selectedItems;
                    if (targetMode == 'igGrid') {
                        if(component.length === 0){
                            component = $("#" + ko.unwrap(data.comId)).find(".ntsListBox");    
                        }
                        selectedItems = component.ntsGridList("getSelected");
                    } else if (targetMode == 'igTree') {
                        selectedItems = component.ntsTreeView("getSelected");
                    }
                    
                    let srh: SearchPub= $container.data("searchObject");
                    let result = srh.search(searchKey, selectedItems);
                    if(nts.uk.util.isNullOrEmpty(result.options) && searchMode === "highlight"){
                        nts.uk.ui.dialog.alert(nts.uk.resource.getMessage("FND_E_SEARCH_NOHIT"));
                        return;        
                    }
                    let isMulti = targetMode === 'igGrid' ? component.igGridSelection('option', 'multipleSelection') 
                        : component.igTreeGridSelection('option', 'multipleSelection')
                    
                    let selectedProperties = _.map(result.selectItems, primaryKey);
//                    let selectedValue;
//                    if(selectedKey !== null){
//                        selectedValue = isMulti ? _.map(result.selectItems, selectedKey) : 
//                            result.selectItems.length > 0 ? result.selectItems[0][selectedKey] : undefined;        
//                    } else {
//                        selectedValue = isMulti ? [result.selectItems] : 
//                            result.selectItems.length > 0 ? result.selectItems[0] : undefined;    
//                    }
                    
                    if (targetMode === 'igGrid') {  
                        component.ntsGridList("setSelected", selectedProperties);
                        if(searchMode === "filter"){
                            $container.data("filteredSrouce", result.options); 
                            component.attr("filtered", true);   
                            //selected(selectedValue);
                            //selected.valueHasMutated();
                            let source = _.filter(data.items(), function (item: any){
                                             return _.find(result.options, function (itemFilterd: any){
                                            return itemFilterd[primaryKey] === item[primaryKey];        
                                                }) !== undefined || _.find(srh.getDataSource(), function (oldItem: any){
                                             return oldItem[primaryKey] === item[primaryKey];        
                                            }) === undefined;            
                            });
                            component.igGrid("option", "dataSource", source);  
                            component.igGrid("dataBind");  
                            
                            if(nts.uk.util.isNullOrEmpty(selectedProperties)){
                                component.trigger("selectionchanged");        
                            }
                        } else {
                            component.trigger("selectionchanged");    
                        }
                    } else if (targetMode == 'igTree') {
                        component.ntsTreeView("setSelected", selectedProperties);
                        component.trigger("selectionchanged");
                        //selected(selectedValue);
                    }
                    _.defer(function() {
                        component.trigger("selectChange");    
                    });
                    
                    $container.data("searchKey", searchKey);  
                }    
            }
            
            var nextSearch = function() {
                let searchKey = $input.val();
                if(nts.uk.util.isNullOrEmpty(searchKey)) {
                    nts.uk.ui.dialog.alert(nts.uk.resource.getMessage("FND_E_SEARCH_NOWORD"));
                    return;        
                }
                search(searchKey);    
            }
            $input.keydown(function(event) {
                if (event.which == 13) {
                    event.preventDefault();
                    nextSearch();
                    _.defer(() => {
                        $input.focus();                
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
        }
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            var $searchBox = $(element);
            var data = valueAccessor();
            
            var arr = ko.unwrap(data.items);
            
            var searchMode = ko.unwrap(data.searchMode);
            let primaryKey = ko.unwrap(data.targetKey);
            let enable = ko.unwrap(data.enable);
            let targetMode = data.mode;
            let component;
            if (targetMode === "listbox") {
                component = $("#" + ko.unwrap(data.comId)).find(".ntsListBox");    
                targetMode = "igGrid";    
            } else {
                component = $("#" + ko.unwrap(data.comId));    
            }
            let srhX: SearchPub= $searchBox.data("searchObject");
            
            srhX.setDataSource(arr);
            
            if(enable === false){
                $searchBox.find(".ntsSearchBox_Component").attr('disabled', 'disabled');        
            } else {
                $searchBox.find(".ntsSearchBox_Component").removeAttr('disabled');           
            }
        }
    }
    
    ko.bindingHandlers['ntsSearchBox'] = new NtsSearchBoxBindingHandler();
}
