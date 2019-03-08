/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {
    
    /**
     * Dialog binding handler
     */
    class NtsColorPickerBindingHandler implements KnockoutBindingHandler {

        /**
         * Init. 
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            let data = valueAccessor();
            let $container = $(element);
            
            let width = ko.unwrap(data.width);
            let color = ko.unwrap(data.value);
            let dataName = data.name === undefined ? "" : nts.uk.resource.getControlName(ko.unwrap(data.name));
            let enable = data.enable === undefined ? true : ko.unwrap(data.enable);
            let required = data.required === undefined ? false : ko.unwrap(data.required);
            
            let tag = $container.prop("tagName").toLowerCase();
            let id = $container.attr("id");
            let $picker;
            if (tag === "input"){
                $picker = $container; 
                $picker.wrap("<div class='ntsControl ntsColorPicker_Container'/>");     
                $picker.addClass("ntsColorPicker");
                $container = $picker.parent();
            } else if (tag === 'div') {
                $container.addClass("ntsControl ntsColorPicker_Container");
                $container.append("<input class='ntsColorPicker'/>");    
                $picker = $container.find(".ntsColorPicker");                  
            } else {
                $container.wrap("<div class='ntsControl ntsColorPicker_Container'/>");
                $container.removeAttr("id");
                $container.hide();
                $container = $container.parent();
                $container.append("<input class='ntsColorPicker'/>"); 
                $picker = $container.find(".ntsColorPicker");     
            }
            
            $container.css("min-height", 32).attr("id", id);
            
            $picker.data("required", required).removeAttr("id");
            if(nts.uk.util.isNullOrEmpty($container.attr("tabindex"))){
                $container.attr("tabindex", "0");        
            }
            
            $picker.addClass("ntsColorPicker").attr("data-name", dataName);
            
            $picker.spectrum({
                preferredFormat: "name",
                showPaletteOnly: true,
                togglePaletteOnly: true,
                togglePaletteMoreText: 'その他の色', 
                togglePaletteLessText: '隠す',
                color: color,
                disabled: !enable,
                showInput: true,
                showSelectionPalette: true,
                showInitial: true,
                chooseText: "確定",
                cancelText: "キャンセル",
                allowEmpty:true,
                showAlpha: false, //transparency setting
                palette: [
                    ["#000","#444","#666","#999","#ccc","#eee","#f3f3f3","#fff"],
                    ["#f00","#f90","#ff0","#0f0","#0ff","#00f","#90f","#f0f"],
                    ["#f4cccc","#fce5cd","#fff2cc","#d9ead3","#d0e0e3","#cfe2f3","#d9d2e9","#ead1dc"],
                    ["#ea9999","#f9cb9c","#ffe599","#b6d7a8","#a2c4c9","#9fc5e8","#b4a7d6","#d5a6bd"],
                    ["#e06666","#f6b26b","#ffd966","#93c47d","#76a5af","#6fa8dc","#8e7cc3","#c27ba0"],
                    ["#c00","#e69138","#f1c232","#6aa84f","#45818e","#3d85c6","#674ea7","#a64d79"],
                    ["#900","#b45f06","#bf9000","#38761d","#134f5c","#0b5394","#351c75","#741b47"],
                    ["#600","#783f04","#7f6000","#274e13","#0c343d","#073763","#20124d","#4c1130"] 
                ],
                change: function(color) {
                    let required = $picker.data("required");
                    $picker.ntsError('clear');
                    if(!nts.uk.util.isNullOrUndefined(color) && !nts.uk.util.isNullOrUndefined(data.value)){
                        data.value(color.toHexString()); // #ff0000    
                    } else if (nts.uk.util.isNullOrUndefined(color)) {
                        if(required === true){
                            $picker.ntsError('set', nts.uk.resource.getMessage('MsgB_1', [ dataName ]), 'MsgB_1');
                        } else {
                            data.value(null);
                        }
                            
//                        _.defer(() => { 
                         
//                        });      
                    }
                }
            });
            
            let validateRequired = function ($p){
                $p.ntsError('clear');
                    let value = $p.spectrum("get");
                    if(nts.uk.util.isNullOrUndefined(value)){
                        $p.ntsError('set', nts.uk.resource.getMessage('MsgB_1', [ dataName ]), 'MsgB_1');
                    }    
            }
            
            $container.keydown((evt, ui) => {
                let code = evt.which || evt.keyCode;
                if (code.toString() === "9"){
                    
                    if (required === true){
                        validateRequired($picker);       
                    }
                    $picker.spectrum("hide");
                }  
            });
            
            $container.on('validate', (function(e: Event) {
                if (required === true){
                    validateRequired($picker);           
                }
            }));
            
            $picker.spectrum("container").find(".sp-clear").click(function (e: Event){
                 $picker.spectrum("set", null);
                 if (required === true){
                    validateRequired($picker);       
                 }   else if(!nts.uk.util.isNullOrUndefined(data.value)){
                    data.value(null);
                 }                
            });
            
            if(!nts.uk.util.isNullOrUndefined(width) && nts.uk.ntsNumber.isNumber(width)){
                $container.width(width);
                $container.find(".sp-replacer").width(width - 10);
                $container.find(".sp-preview").width(width - 30);        
            }
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            let data = valueAccessor();
            let tag = $(element).prop("tagName").toLowerCase();
            let $picker;
            if (tag === "input"){
                $picker = $(element);   
            } else if (tag === 'div'){  
                $picker = $(element).find(".ntsColorPicker");                  
            } else {
                $picker = $(element).parent().find(".ntsColorPicker");      
                $(element).hide();    
            }
            
            let colorCode = ko.unwrap(data.value);
            let enable = data.enable === undefined ? true : ko.unwrap(data.enable);
            let required = data.required === undefined ? false : ko.unwrap(data.required);
            
            $picker.data("required", required);
            
            $picker.spectrum("set", colorCode);
            if(enable !== false){
                $picker.spectrum("enable");        
            } else {
                $picker.spectrum("disable");    
            }
        }
    }

    ko.bindingHandlers['ntsColorPicker'] = new NtsColorPickerBindingHandler();
}