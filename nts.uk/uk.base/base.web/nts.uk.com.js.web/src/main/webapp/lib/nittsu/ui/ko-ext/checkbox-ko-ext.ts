/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {

    /**
     * CheckBox binding handler
     */
    class NtsCheckboxBindingHandler implements KnockoutBindingHandler {
        /**
         * Constructor.
         */
        constructor() {
        }

        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            // Get data
            var data = valueAccessor();
            var setChecked = data.checked;
            var textId: string = data.text;
            var checkBoxText: string;
            var enable: boolean = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
            
            // Container
            var container = $(element);
            if (nts.uk.util.isNullOrUndefined(container.attr("tabindex")))
	            container.attr("tabindex", "0");
            container.addClass("ntsControl ntsCheckBox").on("click", (e) => {
                if (container.data("readonly") === true) e.preventDefault();
            });

            if (textId) {
                checkBoxText = textId;
            } else {
                checkBoxText = container.text();
                container.text('');
            }

            container.data("enable", enable)
            var $checkBoxLabel = $("<label class='ntsCheckBox-label'></label>");
            var $checkBox = $('<input type="checkbox">').on("change", function() {
                if (typeof setChecked === "function")
                    setChecked($(this).is(":checked"));
            }).appendTo($checkBoxLabel);
            var $box = $("<span class='box'></span>").appendTo($checkBoxLabel);
            if(checkBoxText && checkBoxText.length > 0)
                var label = $("<span class='label'></span>").text(checkBoxText).appendTo($checkBoxLabel);
            $checkBoxLabel.appendTo(container);
            
            container.keypress(function (evt, ui){
                let code = evt.which || evt.keyCode;
                if (code === 32) {
                    if(container.data("enable") !== false){
                        let checkbox = container.find("input[type='checkbox']:first");
                        if(checkbox.is(":checked")){
                            checkbox.prop("checked", false);   
                            setChecked(false); 
                        } else {
                            checkbox.prop("checked", true);
                            setChecked(true);    
                        }       
                    }       
                    evt.preventDefault();         
                }    
            });
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            // Get data
            var data = valueAccessor();
            var checked: boolean = ko.unwrap(data.checked);
            var readonly: boolean = (data.readonly !== undefined) ? ko.unwrap(data.readonly) : false;
            var enable: boolean = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;

            // Container
            var container = $(element);
            container.data("enable", enable)
            container.data("readonly", readonly);
            var $checkBox = $(element).find("input[type='checkbox']");

            // Checked
            $checkBox.prop("checked", checked);
            // Enable
            (enable === true) ? $checkBox.removeAttr("disabled") : $checkBox.attr("disabled", "disabled");
        }
    }

    /**
     * MultiCheckbox binding handler
     */
    class NtsMultiCheckBoxBindingHandler implements KnockoutBindingHandler {

        constructor() {
        }

        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext) {
            var data = valueAccessor();
            var container = $(element);
            container.addClass("ntsControl").on("click", (e) => {
                if (container.data("readonly") === true) e.preventDefault();
            });
            let enable: boolean = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
            container.data("enable", _.clone(enable));
            container.data("init", true);
            container.data("tabindex", container.attr("tabindex"));
        	container.removeAttr("tabindex");

            // Default value
            new nts.uk.util.value.DefaultValue().onReset(container, data.value);
        }

        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            // Get data
            var data = valueAccessor();
            var options: any = data.options === undefined ? [] : JSON.parse(ko.toJSON(data.options));
            var optionValue: string = ko.unwrap(data.optionsValue);
            var optionText: string = ko.unwrap(data.optionsText);
            var selectedValue: any = data.value;
            var readonly: boolean = (data.readonly !== undefined) ? ko.unwrap(data.readonly) : false;
            var enable: boolean = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;

            // Container
            var container = $(element);
            container.data("readonly", readonly);

            // Get option or option[optionValue]
            var getOptionValue = (item) => {
                return (optionValue === undefined) ? item : item[optionValue];
            };
            
            let selectedValues = JSON.parse(ko.toJSON(data.value));
            // Render
            if (!_.isEqual(container.data("options"), options)) {
                container.empty();
                _.forEach(options, (option) => {
                    var checkBoxLabel = $("<label class='ntsCheckBox'></label>");
                    var checkBox = $('<input type="checkbox">').data("option", option).data("value", getOptionValue(option)).on("change", function() {
                        var self = $(this);
                        if (self.is(":checked"))
                            selectedValue.push(self.data("value"));
                        else
                            selectedValue.remove(_.find(selectedValue(), (value) => {
                                return _.isEqual(JSON.parse(ko.toJSON(value)), self.data("value"))
                            }));
                    });
                    
                    let disableOption = option["enable"];
                    if (nts.uk.util.isNullOrUndefined(container.data("tabindex")))
                    	checkBoxLabel.attr("tabindex", "0");
                    else {
                    	checkBoxLabel.attr("tabindex", container.data("tabindex"));
                    }
                    
                    checkBoxLabel.keypress(function (evt, ui){
                        let code = evt.which || evt.keyCode;
                        if (code === 32) {
                            if(container.data("enable") !== false && disableOption !== false){
                                let cb = checkBoxLabel.find("input[type='checkbox']:first");
                                if(cb.is(":checked")){
                                    cb.prop("checked", false);  
                                    selectedValue.remove(_.find(selectedValue(), (value) => {
                                        return _.isEqual(JSON.parse(ko.toJSON(value)), checkBox.data("value"))
                                    })); 
                                } else {
                                    if(!cb.is(":checked")){
                                        cb.prop("checked", true);   
                                        selectedValue.push(checkBox.data("value"));     
                                    } 
                                }               
                            }  
                            evt.preventDefault();       
                        }        
                    });
                    if(!nts.uk.util.isNullOrUndefined(disableOption) && (disableOption === false)){
                        checkBox.attr("disabled", "disabled");    
                    }
                    checkBox.appendTo(checkBoxLabel);
                    var box = $("<span class='box'></span>").appendTo(checkBoxLabel);
                    if (option[optionText] && option[optionText].length > 0)
                        var label = $("<span class='label'></span>").text(option[optionText]).appendTo(checkBoxLabel);
                    checkBoxLabel.appendTo(container);
                });
                // Save a clone
                container.data("options", _.cloneDeep(options));
            }

            // Checked  
            container.find("input[type='checkbox']").prop("checked", function() {
                return (_.find(selectedValue(), (value) => {
                    return _.isEqual(JSON.parse(ko.toJSON(value)), $(this).data("value"));
                }) !== undefined);
            });
            // Enable
//            if((container.data("init") && enable !== true) || !_.isEqual(container.data("enable"), enable)){
            container.data("enable", _.clone(enable));
            if(enable === true) {
                _.forEach(container.find("input[type='checkbox']"), function (checkbox){
                    let dataOpion = $(checkbox).data("option");
                    if (dataOpion["enable"] === true) {
                        $(checkbox).removeAttr("disabled");        
                    }        
                }); 
            } else if (enable === false){
                container.find("input[type='checkbox']").attr("disabled", "disabled");
                new nts.uk.util.value.DefaultValue().applyReset(container, data.value);
            }
            
        }
    }
    
    ko.bindingHandlers['ntsCheckBox'] = new NtsCheckboxBindingHandler();
    ko.bindingHandlers['ntsMultiCheckBox'] = new NtsMultiCheckBoxBindingHandler();
}