__viewContext.ready(function () {
    class ScreenModel {
        linkText: KnockoutObservable<string>;
        
        constructor() {
            var self = this;
            self.linkText = ko.observable("Do something");
        }
        
        doSomething(s) {
            var self = this;
            self.linkText(self.linkText() + "❤" + s);
        }
    }
    
    this.bind(new ScreenModel());
    
});