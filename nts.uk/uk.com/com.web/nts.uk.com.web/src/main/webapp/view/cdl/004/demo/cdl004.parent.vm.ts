module nts.uk.com.view.cdl004.parent.viewmodel {
    
    export class ScreenModel {
        //codes from parent screen
        canSelectJobtitleCodes: KnockoutObservable<string>;
        selectJobtitleCodes: KnockoutObservable<string>;
        selectMode: KnockoutObservable<boolean>;
        showNoSelection: KnockoutObservable<boolean>;
        baseDate: KnockoutObservable<Date>
        constructor() {
            var self = this;
            //construct codes 
            self.canSelectJobtitleCodes = ko.observable('00000000-0000-0000-0000-000000000006,00000000-0000-0000-0000-000000000005');
            self.selectMode = ko.observable(true);
            self.showNoSelection = ko.observable(false);
            self.selectJobtitleCodes = ko.observable('');
            self.baseDate = ko.observable(new Date());
        }

        /**
         * open dialog cdl004
         */
        public openDialogCDL004(): void {
            let self = this;
            let canSelected = self.canSelectJobtitleCodes() ? self.canSelectJobtitleCodes().split(',') : [];
            nts.uk.ui.windows.setShared('inputCDL004', {
                baseDate: self.baseDate(),
                canSelected: canSelected,
                showNoSelection: self.showNoSelection(),
                isMultiple: self.selectMode()
            }, true);

            nts.uk.ui.windows.sub.modal('/view/cdl/004/a/index.xhtml').onClosed(function(): any {
                //view all code of selected item 
                var output = nts.uk.ui.windows.getShared('outputCDL004');
                if (output) {
                    self.selectJobtitleCodes(output.selectedCode);
                }
            })
        }
    }
}
