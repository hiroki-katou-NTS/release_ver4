/// <reference path="../reference.ts"/>

module nts.uk.ui.notify {
    
    module error {
        
        ui.documentReady.add(() => {
            var $functionsArea = $('#functions-area');
            var $functionsAreaBottom = $('#functions-area-bottom');
            if ($functionsArea.length > 0) {
                _.defer(() => {
                    $('#func-notifier-errors').position({ my: 'left+5 top-5', at: 'left bottom', of: $('#functions-area') });
                });
            } else if ($functionsAreaBottom.length > 0) {
                // Defer in case dialog not showing yet. Should fix by using CSS for position, not JQuery
                _.defer(() => {
                    $('#func-notifier-errors').position({ my: 'left+5 top+48', at: 'left top', of: $('#functions-area-bottom') });
                });
            } else {
                return;
            }
        });
        
    }
}