/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {

    /**
     * GridList binding handler
     */
    class NtsGridListBindingHandler implements KnockoutBindingHandler {

        init(element: HTMLElement, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            var HEADER_HEIGHT = 27;

            var $grid = $(element);
            let gridId = $grid.attr('id');
            if (nts.uk.util.isNullOrUndefined(gridId)) {
                throw new Error('the element NtsGridList must have id attribute.');
            }

            var data = valueAccessor();
            var optionsValue: string = data.primaryKey !== undefined ? data.primaryKey : data.optionsValue;
            var options = ko.unwrap(data.dataSource !== undefined ? data.dataSource : data.options);
            var deleteOptions = ko.unwrap(data.deleteOptions);
            var observableColumns = ko.unwrap(data.columns);
            var showNumbering = ko.unwrap(data.showNumbering) === true ? true : false;
            var enable: boolean = ko.unwrap(data.enable);
            var value = ko.unwrap(data.value);
            $grid.data("init", true);
            
            var features = [];
            features.push({ name: 'Selection', multipleSelection: data.multiple });
            features.push({ name: 'Sorting', type: 'local' });
            if(data.multiple || showNumbering){ 
                features.push({ name: 'RowSelectors', enableCheckBoxes: data.multiple, enableRowNumbering: showNumbering });    
            }
            
            var gridFeatures = ko.unwrap(data.features);
            var iggridColumns = _.map(observableColumns, c => {
                c["key"] = c["key"] === undefined ? c["prop"] : c["key"];
                c["dataType"] = 'string';
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
                            result.attr("data-value", val);
                            _.forEach(switchOptions, function(opt) {
                                let value = opt[switchValue];
                                let text = opt[switchText]; 
                                let btn = $('<button class="nts-switch-button" tabindex="-1"/>').text(text);
                                
                                btn.attr('data-value', value);
                                if (val == value) {
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
                    }       
                }
                return c; 
            });

            $grid.igGrid({
                width: data.width,
                height: (data.height) + "px",
                primaryKey: optionsValue,
                columns: iggridColumns,
                virtualization: true,
                virtualizationMode: 'continuous',
                features: features,
                tabIndex: -1
            });

            if (!util.isNullOrUndefined(deleteOptions) && !util.isNullOrUndefined(deleteOptions.deleteField)
                && deleteOptions.visible === true) {
                var sources = (data.dataSource !== undefined ? data.dataSource : data.options);
                $grid.ntsGridList("setupDeleteButton", {
                    deleteField: deleteOptions.deleteField,
                    sourceTarget: sources
                });
            }

            $grid.ntsGridList('setupSelecting');
            
            if (data.multiple){
                $grid.bind('iggridrowselectorscheckboxstatechanging', (evt: Event, uiX: any) => {
//                console.log(ui);
                if($grid.data("enable") === false){ 
                    return false;        
                }
            }); 
            }
            $grid.bind('iggridselectionrowselectionchanging', (evt: Event, uiX: any) => {
//                console.log(ui);
                if($grid.data("enable") === false){ 
                    return false;        
                }
            });

            $grid.bind('selectionchanged', () => {
                $grid.data("ui-changed", true);
                if (data.multiple) {
                    let selected: Array<any> = $grid.ntsGridList('getSelected');
                    if (selected) {
                        data.value(_.map(selected, s => s.id));
                    } else {
                        data.value([]);
                    }
                } else {
                    let selected = $grid.ntsGridList('getSelected');
                    if (selected) {
                        data.value(selected.id);
                    } else {
                        data.value('');
                    }
                }
            });
            
            $grid.setupSearchScroll("igGrid", true); 
        }

        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {

            var $grid = $(element);
            var data = valueAccessor();
            var enable: boolean = ko.unwrap(data.enable);
            var optionsValue: string = data.primaryKey !== undefined ? data.primaryKey : data.optionsValue;
            var currentSource = $grid.igGrid('option', 'dataSource');
            var sources = (data.dataSource !== undefined ? data.dataSource() : data.options());
            
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
            
            if (!($grid.attr("filtered") === true || $grid.attr("filtered") === "true") && $grid.data("ui-changed") !== true) {
                let currentSources = sources.slice();
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
                $grid.igGrid('option', 'dataSource', currentSources);
                $grid.igGrid("dataBind");
            }

            var currentSelectedItems = $grid.ntsGridList('getSelected');
            var isEqual = _.isEqualWith(currentSelectedItems, data.value(), function(current, newVal) {
                if ((current === undefined && newVal === undefined) || (current !== undefined && current.id === newVal)) {
                    return true;
                }
            })
            if (!isEqual) {
                $grid.ntsGridList('setSelected', data.value());
            }
            $grid.data("ui-changed", false);
            $grid.closest('.ui-iggrid').addClass('nts-gridlist').height(data.height).attr("tabindex", "0");
        }
    }
    
    ko.bindingHandlers['ntsGridList'] = new NtsGridListBindingHandler();
}