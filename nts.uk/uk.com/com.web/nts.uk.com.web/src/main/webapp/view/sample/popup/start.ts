__viewContext.ready(function () {
    class ScreenModel {
    
        constructor() {
            var self = this;
        }
    }
    
    $('.popup-area').each(function() {
        var popPos = $(this).siblings('.show-popup');
        $(this).ntsPopup({
            position: {
                my: 'left top',
                at: 'left bottom',
                of: popPos
            },
            dismissible: false
        });
    });
    $('.show-popup').click(function () {
        $(this).siblings('.popup-area').ntsPopup('show');
    });
    $('.toggle-popup').click(function () {
        $(this).siblings('.popup-area').ntsPopup('toggle');
    });
    $('.close-popup').click(function(){
        $(this).parent().ntsPopup('hide');
    });
    $('.destroy-popup').click(function() {
       var currentPopup = $(this).parent();
       currentPopup.ntsPopup('destroy');
       currentPopup.siblings('.toggle-popup').prop("disabled",true);
       currentPopup.siblings('.show-popup').prop("disabled",true);
    });
    $('.reset-popup').click(function () {
        var currentPop = $(this).siblings('.popup-area');
        var showPop = $(this).siblings('.show-popup');
        currentPop.ntsPopup({
            position: {
                my: 'left top',
                at: 'left bottom',
                of: showPop
            },
            dismissible: false
        });
        $(this).siblings('.toggle-popup').prop("disabled",false);
        $(this).siblings('.show-popup').prop("disabled",false);
    });
    this.bind(new ScreenModel());
    
});