/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {

    /**
     * ComboBox binding handler
     */
    class ComboBoxBindingHandler implements KnockoutBindingHandler {
        /**
         * Constructor.
         */
        constructor() {
        }

        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            var container = $(element);
            
            if(nts.uk.util.isNullOrUndefined(container.attr("tabindex"))){
                container.attr("tabindex", "0");    
            }
            
            container.data("tabindex", container.attr("tabindex"));
            container.keypress(function (evt, ui){
                let code = evt.which || evt.keyCode;
                if (code === 32) {
                    container.igCombo("openDropDown");
                    evt.preventDefault();      
//                    $('html, body').scrollTop(container.first().offset().top - 200);      
                } 
            });
        }

        /** 
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            // Get data.
            var data = valueAccessor();
            var self = this;

            // Get options. 
            var options: Array<any> = ko.unwrap(data.options);

            // Get options value.
            var optionValue = data.optionsValue === undefined ? null : ko.unwrap(data.optionsValue);
            var optionText = data.optionsText === undefined ? null : ko.unwrap(data.optionsText);
            var selectedValue = ko.unwrap(data.value);
            var editable = ko.unwrap(data.editable);
            var enable: boolean = data.enable !== undefined ? ko.unwrap(data.enable) : true;
            var columns: Array<any> = ko.unwrap(data.columns);
            var visibleItemsCount = data.visibleItemsCount === undefined ? 5 : ko.unwrap(data.visibleItemsCount);
            var dropDownAttachedToBody: boolean = data.dropDownAttachedToBody === undefined ? null : ko.unwrap(data.dropDownAttachedToBody);
            if (dropDownAttachedToBody === null) {
                if ($(element).closest(".ui-iggrid").length != 0)
                    dropDownAttachedToBody = true;
                else
                    dropDownAttachedToBody = false;
            }
            
            // Container.
            var container = $(element);
            var comboMode: string = editable ? 'editable' : 'dropdown';

            // Default values.
            var distanceColumns = '     ';
            var fillCharacter = ' '; // Character used fill to the columns.
            var maxWidthCharacter = 15;
            
            // Default value
            var defVal = new nts.uk.util.value.DefaultValue().onReset(container, data.value);
            var getValue = function (item){
                return optionValue === null ? item : item[optionValue];        
            };
            
            // Check selected code.
            if (_.find(options, item => getValue(item) === selectedValue) === undefined && !editable) {
                selectedValue = options.length > 0 ?getValue(options[0]) : '';
                data.value(selectedValue);
            }

            var haveColumn = columns && columns.length > 0;

            var isChangeOptions = !_.isEqual(container.data("options"), options);
            if (isChangeOptions) {
                container.data("options", options.slice());
                options = options.map((option) => {
                    var newOptionText: string = '';

                    // Check muti columns.
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
                        newOptionText = optionText === null ? option : option[optionText]; 
                    }
                    // Add label attr.
                    option['nts-combo-label'] = newOptionText;
                    return option;
                });
            }

            var currentColumnSetting = container.data("columns");
            var currentComboMode = container.data("comboMode");
            var isInitCombo = !_.isEqual(currentColumnSetting, columns) || !_.isEqual(currentComboMode, comboMode);
            if (isInitCombo) {
                // Delete igCombo.
                if (container.data("igCombo") != null) {
                    container.igCombo('destroy');
                    container.removeClass('ui-state-disabled');
                }

                // Set attribute for multi column.
                var itemTemplate: string = undefined;
                if (haveColumn) {
                    itemTemplate = '<div class="nts-combo-item">';
                    _.forEach(columns, function(item, i) {
                        // Set item template.
                        itemTemplate += '<div class="nts-column nts-combo-column-' + i + '">${' + item.prop + '}</div>';
                    });
                    itemTemplate += '</div>';
                }

                // Create igCombo.
                container.igCombo({
                    dataSource: options,
                    valueKey: data.optionsValue,
                    visibleItemsCount: visibleItemsCount,
                    dropDownAttachedToBody : dropDownAttachedToBody,
                    textKey: 'nts-combo-label',
                    mode: comboMode,
                    disabled: !enable,
                    placeHolder: '',
                    tabIndex : -1,
                    enableClearButton: false,
                    initialSelectedItems: [
                        { value: selectedValue }
                    ],
                    itemTemplate: itemTemplate,
                    selectionChanged: function(evt: any, ui: any) {
                        if (ui.items.length > 0) {
                            data.value(getValue(ui.items[0].data));
                        }
                    }
                });
            } else {
                container.igCombo("option", "disabled", !enable);
            }
            if (!enable) { 
                defVal.applyReset(container, data.value);
                container.attr("tabindex", "-1");
            } else {
                container.attr("tabindex", container.data("tabindex"));    
            }
            if (isChangeOptions && !isInitCombo) {
                container.igCombo("option", "dataSource", options);
                container.igCombo("dataBind");
            }
            if (selectedValue !== undefined && selectedValue !== null) {
                container.igCombo("value", selectedValue);
            }
            
            var isDropDownWidthSpecified = false;

            // Set width for multi columns.
            if (haveColumn && (isChangeOptions || isInitCombo)) {
                var componentWidth = 0;
                var $dropDownOptions = $(container.igCombo("dropDown"));
                _.forEach(columns, function(item, i) {
                    isDropDownWidthSpecified = (isDropDownWidthSpecified || item.lengthDropDown !== undefined);
                    if (item.lengthDropDown === undefined) {
                        item.lengthDropDown = item.length;
                    }
                    
                    var componentColumnWidth = item.length * maxWidthCharacter + 10;
                    var dropDownColumnWidth = item.lengthDropDown * maxWidthCharacter + 10;
                    $dropDownOptions.find('.nts-combo-column-' + i).css("width", dropDownColumnWidth);
                    componentWidth += componentColumnWidth + 10;
                });
                
                container.css({ 'min-width': componentWidth });
                
                if (isDropDownWidthSpecified) {
                    container.find(".ui-igcombo-dropdown").css("width", "auto");
                }
            }

            container.data("columns", columns);
            container.data("comboMode", comboMode);
        }
    }
    
    ko.bindingHandlers['ntsComboBox'] = new ComboBoxBindingHandler();
}