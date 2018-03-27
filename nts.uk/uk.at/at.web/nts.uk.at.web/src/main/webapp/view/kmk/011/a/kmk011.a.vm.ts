module nts.uk.at.view.kmk011.a {
  
    export module viewmodel {

        export class ScreenModel {
            constructor() {
                var self = this;   
            }
            
            /**
             * open DivergenceTimeSetting (init screen b)
             */
            public openDivergenceTimeSetting(): void {
                nts.uk.request.jump("/view/kmk/011/b/index.xhtml");
            }
            
            /**
             * open Creating divergence reference time page (init screen d)
             */
            public openCreatingDivergenceRefTimePage(): void {
                nts.uk.request.jump("/view/kmk/011/d/index.xhtml");
            }

            /**
             * open dialog H
             */
            public openUsageUnitSettingDialog(): void {
                nts.uk.ui.windows.sub.modal("/view/kmk/011/h/index.xhtml").onClosed(function() {
                       
                });
            }
        }
        
    }
}