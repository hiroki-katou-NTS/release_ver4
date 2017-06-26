__viewContext.ready(function() {

    class ScreenModel {
        functionItems: KnockoutObservableArray<any>;
        
        constructor() {
            this.functionItems = ko.observableArray([]);
            var functionItems = [];
            for (var i = 0; i < 10; i++) {
                let x = i;
                functionItems.push({icon: "19209343.png", text: 'item-' + i, action: function(evt, ui){ 
                    alert(x); 
                }});
            }
            this.functionItems(functionItems);
        }
        
        remove(){
            this.functionItems.shift();            
        }
    }

    this.bind(new ScreenModel());

});
