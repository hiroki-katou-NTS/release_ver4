/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {

    /**
     * Panel binding handler
     */
    class NtsPanelBindingHandler implements KnockoutBindingHandler {

        constructor() { }
        
        /**
         * Init
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext) {
            var data = valueAccessor();
            var width: any = (data.width !== undefined) ? ko.unwrap(data.width) : null;
            var height: any = (data.height !== undefined) ? ko.unwrap(data.height) : null;
            var direction: string = (data.direction !== undefined) ? ko.unwrap(data.direction) : "right";
            var showIcon: boolean = (data.showIcon !== undefined) ? ko.unwrap(data.showIcon) : false;
            var visible: boolean = (data.visible !== undefined) ? ko.unwrap(data.visible) : true;
            
            var container = $(element);
            container.addClass("panel ntsPanel caret-background");
            let caretClass = "caret-" + direction;
            container.addClass(caretClass);
            if (showIcon === true) {
                container.append("<i class='icon icon-searchbox'></i>");
            }
        }
        
        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            // Get data
            var data = valueAccessor();
            var width: any = (data.width !== undefined) ? ko.unwrap(data.width) : null;
            var height: any = (data.height !== undefined) ? ko.unwrap(data.height) : null;
            var direction: string = (data.direction !== undefined) ? ko.unwrap(data.direction) : "right";
            var showIcon: boolean = (data.showIcon !== undefined) ? ko.unwrap(data.showIcon) : false;
            var visible: boolean = (data.visible !== undefined) ? ko.unwrap(data.visible) : null;

            // Container
            var container = $(element);
            if (!nts.uk.util.isNullOrEmpty(width))
                container.width(width);
            if (!nts.uk.util.isNullOrEmpty(height))
                container.height(height);
            if (!nts.uk.util.isNullOrEmpty(visible)) {
                if (visible === true)
                    container.show();
                else
                    container.hide();
            }
        }
    }
    
    ko.bindingHandlers['ntsPanel'] = new NtsPanelBindingHandler();
}