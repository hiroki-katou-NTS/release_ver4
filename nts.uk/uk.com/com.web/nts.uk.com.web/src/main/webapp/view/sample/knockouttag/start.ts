__viewContext.ready(function () {
    var x = {
        items: [ko.observable(1234), ko.observable("test")],
        visible: ko.observable(true),
        selectedCode: ko.observable(2)
    };
    
    this.bind(x);
    
});