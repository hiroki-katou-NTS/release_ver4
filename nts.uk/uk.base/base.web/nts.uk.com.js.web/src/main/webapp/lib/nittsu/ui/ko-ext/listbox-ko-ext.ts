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
            if(nts.uk.util.isNullOrUndefined($element.attr("tabindex"))){
                $element.attr("tabindex", "0");    
            }
            
            $element.data("tabindex", $element.attr("tabindex"));
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
            let SCROLL_WIDTH = 20;
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
                iggridColumns = _.map(columns, (c, index, columns) => {
                    c["key"] = c["key"] === undefined ? c["prop"] : c["key"];
                    let columnWidth  = c["length"] * maxWidthCharacter + 20;
                    if (index = columns.length - 1) {
                        columnWidth += SCROLL_WIDTH;
                    }
                    c["width"] = columnWidth;
                    c["headerText"] = '';
                    c["columnCssClass"] = 'nts-column';
                    width += columnWidth;
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
                    if (!nts.uk.util.isNullOrEmpty(selected)) {
                        itemSelected = _.map(selected, s => s.id);
                    } else {
                        itemSelected = [];
                    }
                } else {
                    let selected = container.ntsGridList('getSelected');
                    if (!nts.uk.util.isNullOrEmpty(selected)) {
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
            container.ntsGridList("setupScrollWhenBinding");  
            container.data("multiple", isMultiSelect);
            $("#" + gridId + "_container").find("#" + gridId + "_headers").closest("tr").hide();
            $("#" + gridId + "_container").height($("#" + gridId + "_container").height() - gridHeaderHeight);

            
            // add validate event
            $element
                .on('validate', () => {
                    let $container = $(`#${$element.attr('id')}_grid`);

                    if ($element.data('nts_validate')) {
                        if (ko.toJS(data.required) && _.isEmpty(ko.toJS(data.value)) && $container.data("enable")) {
                            $element
                                .addClass('error')
                                .ntsError("set", nts.uk.resource.getMessage("MsgB_2", [ko.toJS(data.name)]), "MsgB_2");
                        } else {
                            $element.removeClass('error')
                                .ntsError("clear");
                        }
                    } else {
                        $element.data('nts_validate', true);
                    }
                });

            $element.prepend($('<style>', {
                type: 'text/css',
                text: `
                    #${$element.attr('id')}.error {
                        border-color: #ff6666;
                    }

                    table[id='${$element.attr('id')}_grid'] .ui-iggrid-tablebody tr,
                    table[id='${$element.attr('id')}_grid'] .ui-iggrid-tablebody tr:hover {
                        outline: none;
                    }

                    table[id='${$element.attr('id')}_grid'] .ui-iggrid-tablebody tr,
                    table[id='${$element.attr('id')}_grid'] .ui-iggrid-tablebody tr:hover {
                        cursor: pointer;
                    }`
            }));
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            // Get data.
            var data = valueAccessor();
            // Get options.
            var sources = (data.dataSource !== undefined ? data.dataSource() : data.options());

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
            var currentSource = container.igGrid('option', 'dataSource');
            if(container.data("enable") !== enable){
                if(!enable){
                    container.ntsGridList('unsetupSelecting');
                    container.addClass("disabled");     
                    $(element).attr("tabindex", "-1");
                } else {
                    container.ntsGridList('setupSelecting');
                    container.removeClass("disabled");    
                    $(element).attr("tabindex", $(element).data("tabindex"));
                }    
            }
            
            container.data("enable", enable);
            
            if (!((String(container.attr("filtered")) === "true") || container.data("ui-changed") === true)) {
                let currentSources = sources.slice();
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
                if (!_.isEqual(currentSources, container.igGrid('option', 'dataSource'))) {
                    container.igGrid('option', 'dataSource', currentSources);
                    container.igGrid("dataBind");        
                }
            } else if(String(container.attr("filtered")) === "true"){
                let filteredSource = [];
                _.forEach(currentSource, function(item){
                    let itemX = _.find(sources, function (s){
                        return s[optionValue] === item[optionValue];
                    });
                    if(!nts.uk.util.isNullOrUndefined(itemX)){ 
                        filteredSource.push(itemX);
                    }     
                });     
                if(!_.isEqual(filteredSource, currentSource)){
                    container.igGrid('option', 'dataSource', _.cloneDeep(filteredSource));
                    container.igGrid("dataBind");    
                }
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
                    _.defer(() => {container.trigger("selectChange");});  
                    container.ntsGridList('setSelected', dataValue);
                }    
            }
            container.data("ui-changed", false);
            container.closest('.ui-iggrid').addClass('nts-gridlist').height(data.height);
            
            // add validate event
            $(element).trigger('validate');
        }
    }
    
    ko.bindingHandlers['ntsListBox'] = new ListBoxBindingHandler();
}
