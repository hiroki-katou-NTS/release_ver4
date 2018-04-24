module nts.uk.com.view.cmf003.a {
    export module viewmodel {
        export class ScreenModel {
            constructor() {
            }
            
            /**
             * request to create creation screen
             */
            save(): void {
                let self = this;
                nts.uk.request.jump("/view/cmf/003/f/index.xhtml");
            }
            
            /**
             * request to create creation screen
             */
            autoSave(): void {
                let self = this;
                nts.uk.request.jump("/view/cmf/003/g/index.xhtml");
            }
            
            /**
             * request to reference history screen
             */
            referenceHistoryScreen(): void {
                let self = this;
               nts.uk.request.jump("/view/cmf/002/h/index.xhtml");
            }
        }
    }
}