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
            var optionValue = ko.unwrap(data.optionsValue);
            var optionText = ko.unwrap(data.optionsText);
            var selectedValue = ko.unwrap(data.value);
            var editable = ko.unwrap(data.editable);
            var enable: boolean = ko.unwrap(data.enable);
            var columns: Array<any> = ko.unwrap(data.columns);

            // Container.
            var container = $(element);
            var comboMode: string = editable ? 'editable' : 'dropdown';

            // Default values.
            var distanceColumns = '     ';
            var fillCharacter = ' '; // Character used fill to the columns.
            var maxWidthCharacter = 15;
            // Default value
            var defVal = new nts.uk.util.value.DefaultValue().onReset(container, data.value);

            // Check selected code.
            if (_.find(options, item => item[optionValue] === selectedValue) === undefined && !editable) {
                selectedValue = options.length > 0 ? options[0][optionValue] : '';
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
                        newOptionText = option[optionText];
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
                    textKey: 'nts-combo-label',
                    mode: comboMode,
                    disabled: !enable,
                    placeHolder: '',
                    enableClearButton: false,
                    initialSelectedItems: [
                        { value: selectedValue }
                    ],
                    itemTemplate: itemTemplate,
                    selectionChanged: function(evt: any, ui: any) {
                        if (ui.items.length > 0) {
                            data.value(ui.items[0].data[optionValue]);
                        }
                    }
                });
            } else {
                container.igCombo("option", "disabled", !enable);
            }
            if (!enable) defVal.applyReset(container, data.value);
            if (isChangeOptions && !isInitCombo) {
                container.igCombo("option", "dataSource", options);
                container.igCombo("dataBind");
            }
            if (selectedValue !== undefined && selectedValue !== null) {
                container.igCombo("value", selectedValue);
            }

            // Set width for multi columns.
            if (haveColumn && (isChangeOptions || isInitCombo)) {
                var totalWidth = 0;
                var $dropDownOptions = $(container.igCombo("dropDown"));
                _.forEach(columns, function(item, i) {
                    var charLength: number = item.length;
                    var width = charLength * maxWidthCharacter + 10;
                    $dropDownOptions.find('.nts-combo-column-' + i).width(width);
                    if (i != columns.length - 1) {
                        $dropDownOptions.find('.nts-combo-column-' + i).css({ 'float': 'left' });
                    }
                    totalWidth += width + 10;
                });
                $dropDownOptions.find('.nts-combo-item').css({ 'min-width': totalWidth });
                container.css({ 'min-width': totalWidth });
            }

            container.data("columns", columns);
            container.data("comboMode", comboMode);
        }
    }
    
    ko.bindingHandlers['ntsComboBox'] = new ComboBoxBindingHandler();
}