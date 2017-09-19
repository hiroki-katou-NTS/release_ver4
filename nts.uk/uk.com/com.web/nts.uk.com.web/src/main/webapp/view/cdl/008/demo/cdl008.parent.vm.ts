module nts.uk.com.view.cdl008.parent.viewmodel {
    
    export class ScreenModel {
        //codes from parent screen
        canSelectWorkplaceIds: KnockoutObservable<string>;
        selectWorkplaceIds: KnockoutObservable<string>;
        selectMode: KnockoutObservable<boolean>;
        baseDate: KnockoutObservable<Date>
        constructor() {
            var self = this;
            //construct codes 
            self.canSelectWorkplaceIds = ko.observable('000000000000000000000000000000000002,000000000000000000000000000000000003,0000000001,0000000002,0000000003,0000000005');
            self.selectMode = ko.observable(true);
            self.selectWorkplaceIds = ko.observable('');
            self.baseDate = ko.observable(new Date());
        }

        /**
         * open dialog cdl008
         */
        public openDialogCDL008(): void {
            let self = this;
            let canSelected = self.canSelectWorkplaceIds() ? self.canSelectWorkplaceIds().split(',') : [];
            nts.uk.ui.windows.setShared('inputCDL008', {
                canSelected: canSelected,
                baseDate: self.baseDate(),
                isMultiple: self.selectMode()
            }, true);

            nts.uk.ui.windows.sub.modal('/view/cdl/008/a/index.xhtml').onClosed(function(): any {
                //view all code of selected item 
                var output = nts.uk.ui.windows.getShared('outputCDL008');
                if (output) {
                    self.selectWorkplaceIds(output.selectedCode);
                }
            })
        }
    }
}
