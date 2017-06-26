/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {
    
    /**
     * ListBox binding handler
     */
    class ListBoxBindingHandler implements KnockoutBindingHandler {
        /**
         * Constructor.
         */
        constructor() {
        }
        
        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            // Get data.
            var data = valueAccessor();

            // Get options
            var options: Array<any> = ko.unwrap(data.options) ;
            // Get options value
            var optionValue = ko.unwrap(data.primaryKey === undefined ? data.optionsValue : data.primaryKey);
            var optionText = ko.unwrap(data.primaryText === undefined ? data.optionsText : data.primaryText);
            var selectedValue = ko.unwrap(data.value);
            var isMultiSelect = ko.unwrap(data.multiple);
            var enable: boolean = ko.unwrap(data.enable);
//            var required = ko.unwrap(data.required) || false;
            var columns: Array<any> = data.columns;
            // Container
            let $element = $(element);
            let elementId = $element.addClass("listbox-wrapper").attr("id");
            $element.attr("tabindex", "0");
            let gridId = elementId;
            if(nts.uk.util.isNullOrUndefined(gridId)){
                gridId = nts.uk.util.randomId();        
            } else {
                gridId += "_grid";    
            }
            $element.append("<table id='" + gridId + "' class='ntsListBox ntsControl'/>");
            var container = $element.find("#" + gridId);
            container.data("options", options.slice());
            container.data("init", true);
            container.data("enable", enable);
            
            // Create changing event.
            var changeEvent = new CustomEvent("selectionChange", {
                detail: {},
            });
              
            container.data("selectionChange", changeEvent);
            
            var features = [];
            features.push({ name: 'Selection', multipleSelection: isMultiSelect });
            
            var maxWidthCharacter = 15;
            var gridFeatures = ko.unwrap(data.features);
            var width = 0;
            let iggridColumns = [];
            if(nts.uk.util.isNullOrUndefined(columns)){
                iggridColumns.push({"key": optionValue, "width": 10 * maxWidthCharacter + 20, "headerText": '', "columnCssClass": 'nts-column', 'hidden': true});
                iggridColumns.push({"key": optionText, "width": 10 * maxWidthCharacter + 20, "headerText": '', "columnCssClass": 'nts-column'});
                width += 10 * maxWidthCharacter + 20;    
                container.data("fullValue", true);
            } else {
                let isHaveKey = false;
                iggridColumns = _.map(columns, c => {
                    c["key"] = c["key"] === undefined ? c["prop"] : c["key"];
                    c["width"] = c["length"] * maxWidthCharacter + 20;
                    c["headerText"] = '';
                    c["columnCssClass"] = 'nts-column';
                    width += c["length"] * maxWidthCharacter + 20;
                    if (optionValue === c["key"]) {
                         isHaveKey = true;   
                    }
                    return c;
                });   
                if (!isHaveKey) {
                    iggridColumns.push({"key": optionValue, "width": 10 * maxWidthCharacter + 20, "headerText": '', "columnCssClass": 'nts-column', 'hidden': true});        
                } 
            }

            var gridHeaderHeight = 24;
            container.igGrid({
                width: width + "px",
                height: (data.rows * 28 + gridHeaderHeight) + "px",
                primaryKey: optionValue,
                columns: iggridColumns,
                virtualization: true,
                virtualizationMode: 'continuous',
                features: features,
                tabIndex: -1
            });
            
            container.ntsGridList('setupSelecting');
             
            
            container.bind('iggridselectionrowselectionchanging', (evt: Event, uiX: any) => {
//                console.log(ui);
                if(container.data("enable") === false){ 
                    return false;        
                }
                let itemSelected = uiX.row.id;
                let dataSource = container.igGrid('option', "dataSource");
                if(container.data("fullValue")){
                    itemSelected = _.find(dataSource, function (d){
                        return d[optionValue].toString() === itemSelected.toString();      
                    });    
                }
                  
                var changingEvent = new CustomEvent("selectionChanging", {
                    detail: itemSelected,  
                    bubbles: true,
                    cancelable: false,
                });
                
                container.data("chaninged", true);
                
                document.getElementById(elementId).dispatchEvent(changingEvent);
            });

            container.bind('selectionchanged', () => {
//                console.log(ui);
                let itemSelected;
                if (container.igGridSelection('option', 'multipleSelection')) {
                    let selected: Array<any> = container.ntsGridList('getSelected');
                    if (selected) {
                        itemSelected = _.map(selected, s => s.id);
                    } else {
                        itemSelected = [];
                    }
                } else {
                    let selected = container.ntsGridList('getSelected');
                    if (selected) {
                        itemSelected = selected.id;
                    } else {
                        itemSelected = ('');
                    }
                }
                container.data("selected", itemSelected);
                let isMultiOld = container.igGridSelection('option', 'multipleSelection');
                if(container.data("fullValue")){
                    let dataSource = container.igGrid('option', "dataSource");
                    if (isMultiOld){
                        itemSelected = _.filter(dataSource, function(d){
                            itemSelected.indexOf(d[optionValue].toString()) >= 0;            
                        });
                    } else {
                        itemSelected = _.find(dataSource, function (d){
                            return d[optionValue].toString() === itemSelected.toString();      
                        });    
                    }
                }
                if(container.data("chaninged") !== true){
                    
                    var changingEvent = new CustomEvent("selectionChanging", {
                        detail: itemSelected,  
                        bubbles: true,
                        cancelable: false,
                    });
                    
                    document.getElementById(container.attr('id')).dispatchEvent(changingEvent);
                }
                
                container.data("chaninged", false);
                container.data("ui-changed", true);
                if(!_.isEqual(itemSelected, data.value())){
                    data.value(itemSelected);        
                }
                
            });
            container.setupSearchScroll("igGrid", true); 
            container.data("multiple", isMultiSelect);
            $("#" + gridId + "_container").find("#" + gridId + "_headers").closest("tr").hide();
            $("#" + gridId + "_container").height($("#" + gridId + "_container").height() - gridHeaderHeight);
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            // Get data.
            var data = valueAccessor();
            // Get options.
            var options: Array<any> = ko.unwrap(data.options);

            // Get options value.
            var optionValue = ko.unwrap(data.primaryKey === undefined ? data.optionsValue : data.primaryKey);
            var optionText = ko.unwrap(data.primaryText === undefined ? data.optionsText : data.primaryText);
            var selectedValue = ko.unwrap(data.value);
            var isMultiSelect = ko.unwrap(data.multiple);
            var enable: boolean = ko.unwrap(data.enable);
            var columns: Array<any> = data.columns;
            var rows = data.rows;
            // Container.
            var container = $(element).find(".ntsListBox");
            if(container.data("enable") !== enable){
                if(!enable){
                    container.ntsGridList('unsetupSelecting');
                    container.addClass("disabled");     
                } else {
                    container.ntsGridList('setupSelecting');
                    container.removeClass("disabled");    
                }    
            }
            
            container.data("enable", enable);
            
            if (!((container.attr("filtered") === true || container.attr("filtered") === "true") || container.data("ui-changed") === true)) {
                let currentSources = options.slice();
                var observableColumns = _.filter(ko.unwrap(data.columns), function(c){
                    c["key"] = c["key"] === undefined ? c["prop"] : c["key"];
                    return c["isDateColumn"] !== undefined && c["isDateColumn"] !== null && c["isDateColumn"] === true;
                });
                _.forEach(currentSources, function(s){
                    _.forEach(observableColumns, function(c){
                        let key = c["key"] === undefined ? c["prop"] : c["key"];
                        s[key] = moment(s[key]).format(c["format"]);
                    });        
                }); 
                container.igGrid('option', 'dataSource', currentSources);
                container.igGrid("dataBind");        
            }
            
            let isMultiOld = container.igGridSelection('option', 'multipleSelection');
            if(isMultiOld !== isMultiSelect){
                
                container.igGridSelection('option', 'multipleSelection', isMultiSelect);   
                if(isMultiOld && !nts.uk.util.isNullOrUndefined(data.value()) && data.value().length > 0){
                    data.value(data.value()[0]);   
                } else if (!isMultiOld && !nts.uk.util.isNullOrUndefined(data.value())){
                    data.value([data.value()]);       
                }
                let dataValue = data.value();
                if(container.data("fullValue")){
                    if (isMultiOld){
                        dataValue = _.map(dataValue, optionValue);
                    } else {
                        dataValue = dataValue[optionValue];    
                    }
                }
                container.ntsGridList('setSelected', dataValue);    
            } else {
                let dataValue = data.value();
                if(container.data("fullValue")){
                    if (isMultiOld){
                        dataValue = _.map(dataValue, optionValue);
                    } else {
                        dataValue = dataValue[optionValue];    
                    }
                }
                var currentSelectedItems = container.ntsGridList('getSelected');
                if (isMultiOld) {
                    if (currentSelectedItems) {
                        currentSelectedItems = _.map(currentSelectedItems, s => s["id"]);
                    } else {
                        currentSelectedItems = [];
                    }
                   
                    if(dataValue) {
                        dataValue = _.map(dataValue, s => s.toString());
                    }   
                } else {
                    if (currentSelectedItems) {
                        currentSelectedItems = currentSelectedItems.id;
                    } else {
                        currentSelectedItems = ('');
                    }
                    if(dataValue) {
                        dataValue = dataValue.toString();
                    }
                }
                
                var isEqual = _.isEqual(currentSelectedItems, dataValue);
                if (!isEqual) {
                    container.ntsGridList('setSelected', dataValue);
                }    
            }
            container.data("ui-changed", false);
            container.closest('.ui-iggrid').addClass('nts-gridlist').height(data.height);
        }
    }
    
    ko.bindingHandlers['ntsListBox'] = new ListBoxBindingHandler();
}