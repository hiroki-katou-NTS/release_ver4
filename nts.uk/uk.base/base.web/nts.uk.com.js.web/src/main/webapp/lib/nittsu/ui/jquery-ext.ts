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
}