__viewContext.ready(function () {
    class ScreenModel {

        constructor() {
            var self = this;
        }
        
        resize(){
            if($("#tabs-complex").width() > 700)
                $("#tabs-complex").width(700);
            else
                $("#tabs-complex").width("auto");
        }
    }
    
    $(".draggable").draggable({});
    
    this.bind(new ScreenModel());
    
});