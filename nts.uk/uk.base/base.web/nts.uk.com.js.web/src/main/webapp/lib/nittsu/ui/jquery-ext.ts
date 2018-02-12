interface JQuery {
    exposeVertically($target: JQuery);
    onkey(command: "down"|"up"|"press", keyCode: number, handler: (JQueryEventObject) => void): JQuery;
    dialogPositionControl(): JQuery;
    
}

module nts.uk.ui.jqueryExtentions {
    // This file left here for log purpose
    
    $.fn.exposeVertically = function ($target: JQuery) {
        let $scroll = $(this);
        let currentViewTopPosition = $scroll.scrollTop();
        let currentViewBottomPosition = currentViewTopPosition + $scroll.height();
        let targetTopPosition = $target.position().top + currentViewTopPosition;
        let targetBottomPosition = targetTopPosition + $target.outerHeight();
        
        if (currentViewTopPosition <= targetTopPosition && targetBottomPosition <= currentViewBottomPosition) {
            return;
        }
        
        if (targetTopPosition <= currentViewTopPosition) {
            let gap = currentViewTopPosition - targetTopPosition;
            $scroll.scrollTop(currentViewTopPosition - gap);
            return;
        }
        
        if (currentViewBottomPosition <= targetBottomPosition) {
            let gap = targetBottomPosition - currentViewBottomPosition;
            $scroll.scrollTop(currentViewTopPosition + gap);
            return;
        }
    }
    
    $.fn.onkey = function (command: "down"|"up"|"press", keyCode: number, handler: (JQueryEventObject) => void) {
        var $element = $(this);
        
        $element.on("key" + command, e => {
            if (e.keyCode === keyCode) {
                return handler(e);
            }
        });
        
        return $element;
    };
    
    $.fn.dialogPositionControl = function () {
        let $dialog = $(this);
        
        $dialog.dialog("option", "position", {
            my: "center",
            at: "center",
            of: window,
            collision: "none"
        });
        
        let $container = $dialog.closest(".ui-dialog");
        
        let offsetContentsArea = $("#header").height();
        let offsetDialog = $container.offset();
        
        if (offsetDialog.top < offsetContentsArea) {
            offsetDialog.top = offsetContentsArea;
        }
        
        if (offsetDialog.left < 0) {
            offsetDialog.left = 0;
        }
        
        $container.offset(offsetDialog);
        
        $dialog.dialog({dragStop: (event, ui) => {
            
            let offsetDialog = $container.offset();
            
            if (offsetDialog.top < offsetContentsArea) {
                offsetDialog.top = offsetContentsArea;
                $container.offset(offsetDialog);
                return false;
            }
        }});
        
        return $dialog;
    };
}