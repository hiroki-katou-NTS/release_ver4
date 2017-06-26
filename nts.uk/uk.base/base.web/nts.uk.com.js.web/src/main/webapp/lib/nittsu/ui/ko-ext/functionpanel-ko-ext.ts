/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {

    /**
     * CheckBox binding handler
     */
    class NtsFunctionPanelBindingHandler implements KnockoutBindingHandler {
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
            let data = valueAccessor();
            let width: string = (data.width !== undefined) ? ko.unwrap(data.width) : 100;
            let headerText: boolean = (data.headerText !== undefined) ? ko.unwrap(data.headerText) : "";
            let items: boolean = (data.dataSource !== undefined) ? ko.unwrap(data.dataSource) : [];
            
            // Container
            let container = $(element);
            if(nts.uk.util.isNullOrEmpty(container.attr("id"))){
                container.attr("id", nts.uk.util.randomId());        
            }
            container.width(width);
            container.addClass("ntsControl ntsFunctionPanel").on("click", (e) => {
                if (container.data("readonly") === true) e.preventDefault();
            });
            container.append("<div class='function-header' /><div class='function-items'/>");
            
            let header = container.find('.function-header');
            header.append("<div class='function-icon'/><div class='function-link'><a class='header-link function-item'>" + headerText + "</a></div>");
            
            let itemAreas = container.find('.function-items');
            
            header.find(".function-item").click(function(evt: Event, ui: any){
                let current = $(this);
                if($(this).data("dbClick") === false){
                    itemAreas.find(".function-item-container").hide("fast", function (){
                        current.data("dbClick", true);     
                    });   
                } else {    
                    itemAreas.find(".function-item-container").show("fast", "linear", function (){
                        current.data("dbClick", false);        
                    });      
                }            
            })
            container.mouseleave(function(evt: Event, ui: any){
                let current = header.find(".function-item");
                itemAreas.find(".function-item-container").hide("fast", function (){
                    current.data("dbClick", true);     
                });    
            });
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            // Get data
            let data = valueAccessor();
            let enable: boolean = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
            let readonly: boolean = (data.readonly !== undefined) ? ko.unwrap(data.readonly) : true;
            let width: string = (data.width !== undefined) ? ko.unwrap(data.width) : 100;
            let headerText: boolean = (data.headerText !== undefined) ? ko.unwrap(data.headerText) : "";
            let items: boolean = (data.dataSource !== undefined) ? ko.unwrap(data.dataSource) : [];  
 
            // Container
            let container = $(element);
            let itemAreas = container.find('.function-items');
            let headerLink = container.find('.header-link');
            let containerId = container.attr("id");
            
            headerLink.text(headerText);
            itemAreas.empty();
            _.forEach(items, function(item: any, idx: number){
                let div = $("<div class='function-item-container' />");     
                div.attr("data-idx", idx); 
                div.width(width);    
                div.append("<div class='function-icon'/><div class='function-link'/>");
                
                let itemLink = $("<a id='"+ (containerId + '-' + idx) +"' class='function-item'>" + item["text"] + "</a>");
                itemLink.click(item["action"]);
                itemLink.appendTo(div.find(".function-link"));
                
                let icon = $("<img class='ft-icon' src='" + item["icon"] + "'/>");
                icon.appendTo(div.find(".function-icon"));
                div.appendTo(itemAreas);       
            });
            container.find(".function-item-container").hide();
        }
    }
    
    ko.bindingHandlers['ntsFunctionPanel'] = new NtsFunctionPanelBindingHandler();
}