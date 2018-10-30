/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {
    
    /**
     * SwitchButton binding handler
     */
    class NtsSwitchButtonBindingHandler implements KnockoutBindingHandler {
        /**
         * Constructor.
         */
        constructor() {
        }

        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            var data = valueAccessor();
            var container = $(element);
            
            if (nts.uk.util.isNullOrUndefined(container.attr("tabindex")))
                container.attr("tabindex", "0");
            
            container.data("tabindex", container.attr("tabindex"));

            container.keydown(function (evt, ui) {
                let code = evt.which || evt.keyCode;
                if (code === 32) {
                    evt.preventDefault(); 
                }        
            });
            
            container.keyup(function (evt, ui) {
                let code = evt.which || evt.keyCode;
                if(container.data("enable") !== false){
                    if (code === 32) {
                        let selectedCode = container.find(".nts-switch-button:first").data('swbtn');     
                        data.value(selectedCode);
//                        container.focus();   
                    } else if (code === 37 || code === 38) {
                        let inputList = container.find(".nts-switch-button");
                        let currentSelect = _.findIndex(inputList, function (item){
                            return $(item).data('swbtn') === data.value();
                        });   
                        let selectedCode = $(inputList[currentSelect - 1]).data('swbtn');
                        if (!nts.uk.util.isNullOrUndefined(selectedCode)){
                            data.value(selectedCode);    
                        }
//                        container.focus();   
                    } else if (code === 39 || code === 40) {
                        let inputList = container.find(".nts-switch-button");
                        let currentSelect = _.findIndex(inputList, function (item){
                            return $(item).data('swbtn') === data.value();
                        });   
                        let selectedCode = $(inputList[currentSelect + 1]).data('swbtn');
                        if (!nts.uk.util.isNullOrUndefined(selectedCode)){
                            data.value(selectedCode);    
                        }
//                        container.focus();   
                    }             
                }  
            });
            // Default value.
            var defVal = new nts.uk.util.value.DefaultValue().onReset(container, data.value);
            
            
            container.bind("validate", function () {
                if (container.ctState("required", "get") && !container.ctState("selected", "get")) {
                    container.ntsError("set", resource.getMessage("FND_E_REQ_SELECT", [container.ctState("name", "get")]), "FND_E_REQ_SELECT");
                } else {
                    container.ntsError("clear");
                }
            });
            
            ui.bindErrorStyle.useDefaultErrorClass(container);
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            // Get data.
            var data = valueAccessor();
            var selectedCssClass = 'selected';
            // Get options.
            var options: Array<any> = ko.unwrap(data.options);

            // Get options value.
            var optionValue = ko.unwrap(data.optionsValue);
            var optionText = ko.unwrap(data.optionsText);
            var selectedValue = ko.unwrap(data.value);
            var enable = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
            
            // Container.
            var container = $(element);
            container.data("enable", enable);
            container.addClass("ntsControl switchButton-wrapper");
            container.ctState("required", "set", ko.unwrap(data.required) === true);
            container.ctState("name", "set", ko.unwrap(data.name));
            
            // Remove deleted button.
            $('button', container).each(function(index, btn) {
                var $btn = $(btn);
                var btnValue = $(btn).data('swbtn');
                // Check if btn is contained in options.
                var foundFlag = _.findIndex(options, function(opt) {
                    return opt[optionValue] == btnValue;
                }) != -1;

                if (!foundFlag) {
                    $btn.remove();
                    return; 
                } 
            })

            // Start binding new state.
            _.forEach(options, function(opt) {
                var value = opt[optionValue];
                var text = opt[optionText];

                // Find button.
                var targetBtn: JQuery = NtsSwitchButtonBindingHandler.setSelectedClass(container, selectedCssClass, selectedValue, value);

                if (targetBtn) {
                    // Do nothing.
                } else {
                    // Recreate
                    var btn = $('<button>').text(text)
                        .addClass('nts-switch-button unselectable')
                        .data('swbtn', value)
//                        .attr('tabindex', "-1")
                        .attr('unselectable', "on")
                        .on('click', function() {
                            var selectedValue = $(this).data('swbtn');
                            data.value(selectedValue); 
                            $('button', container).removeClass(selectedCssClass); 
                            $(this).addClass(selectedCssClass);
                            container.focus();
                        })
                    if (selectedValue == value) {
                        btn.addClass(selectedCssClass);
                        container.ctState("selected", "set", true);
                    }
                    container.append(btn);
                }
            });
            
            if (enable === true) {
                $('button', container).prop("disabled", false);
                container.attr("tabindex", container.data("tabindex"));
            } else {
                $('button', container).prop("disabled", true);
                new nts.uk.util.value.DefaultValue().applyReset(container, data.value);
                container.attr("tabindex", "-1");
            }
        }
        
        static setSelectedClass($container: JQuery, selectedCssClass: string, selectedValue: any, optValue?: any): any {
            var targetBtn;
            $container.ctState("selected", "set", false);
            $('button', $container).each(function(index, btn) {
                var btnValue = $(btn).data('swbtn');
                if (btnValue == optValue) {
                    targetBtn = $(btn);
                }

                if (btnValue == selectedValue) {
                    $(btn).addClass(selectedCssClass);
                    $container.ctState("selected", "set", true);
                    $container.ntsError("clear");
                } else {
                    $(btn).removeClass(selectedCssClass);
                }
            });
            return targetBtn;
        }
    }
    
    ko.bindingHandlers['ntsSwitchButton'] = new NtsSwitchButtonBindingHandler();
}
