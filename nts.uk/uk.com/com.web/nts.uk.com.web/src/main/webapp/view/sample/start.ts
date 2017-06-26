__viewContext.ready(function () {

    $('#popup-area').ntsPopup({
        position: {
            my: 'left top',
            at: 'left bottom',
            of: $('#show-popup')
        }
    });
    
    $('#show-popup').click(function () {
        $('#popup-area').ntsPopup('show');
    });

    var vm = {
        hoge: ko.observable('aaa'),
        
        dynamic: {
            value: ko.observable(1),
            constraint: ko.observable('LayoutCode')
        },
        
        timer: new nts.uk.ui.sharedvm.KibanTimer('test'),
        start: function(){
            this.timer.start();    
        },end: function(){
            this.timer.end();    
        }
    }; // developer's view model
    
    this.bind(vm);
    
});