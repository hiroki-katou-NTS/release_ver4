__viewContext.ready(function () {
    class ScreenModel {
        
        constructor() {
            var self = this;
                        
            // Init UserGuide
            $("[data-toggle='userguide']").ntsUserGuide();
        }
        
        showOverlayTop() {
            $(".userguide-top").ntsUserGuide("show");
        }
        
        showOverlayBottom() {
            $(".userguide-bottom").ntsUserGuide("show");
        }
    }
    
    this.bind(new ScreenModel());
});