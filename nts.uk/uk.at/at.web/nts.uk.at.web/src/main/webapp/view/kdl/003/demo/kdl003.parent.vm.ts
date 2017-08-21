module kdl003.parent.viewmodel {
    export class ScreenModel {
        //codes from parent screen
        canSelectWorkTypeCodes: KnockoutObservable<string>;
        selectWorkTypeCode: KnockoutObservable<string>;
        canSelectSiftCodes: KnockoutObservable<string>;
        selectSiftCode: KnockoutObservable<string>;

        childSelectWorkTypeCode: KnockoutObservable<string>;
        childSelectWorkTypeName: KnockoutObservable<string>;
        childSelectSiftCode: KnockoutObservable<string>;
        childSelectSiftName: KnockoutObservable<string>;
        constructor() {
            var self = this;
            //construct codes 
            self.canSelectWorkTypeCodes = ko.observable('001,002,003,004,005,006,007,008,009,010');
            self.selectWorkTypeCode = ko.observable('002');
            self.canSelectSiftCodes = ko.observable('AAA,AAD,AAG,AAI');
            self.selectSiftCode = ko.observable('AAG');

            self.childSelectWorkTypeCode = ko.observable('');
            self.childSelectWorkTypeName = ko.observable('');
            self.childSelectSiftCode = ko.observable('');
            self.childSelectSiftName = ko.observable('');
        }
        //open dialog 003 
        OpenDialog003() {
            let self = this;
            let workTypeCodes = self.canSelectWorkTypeCodes() ? self.canSelectWorkTypeCodes().split(',') : [];
            let workTimeCodes = self.canSelectSiftCodes() ? self.canSelectSiftCodes().split(',') : [];
            nts.uk.ui.windows.setShared('parentCodes', {
                workTypeCodes: workTypeCodes,
                selectedWorkTypeCode: self.selectWorkTypeCode(),
                workTimeCodes: workTimeCodes,
                selectedWorkTimeCode: self.selectSiftCode()
            }, true);

            nts.uk.ui.windows.sub.modal('/view/kdl/003/a/index.xhtml').onClosed(function(): any {
                //view all code of selected item 
                var childData = nts.uk.ui.windows.getShared('childData');
                self.childSelectWorkTypeCode(childData.selectedWorkTypeCode);
                self.childSelectWorkTypeName(childData.selectedWorkTypeName);
                self.childSelectSiftCode(childData.selectedWorkTimeCode);
                self.childSelectSiftName(childData.selectedWorkTimeName);
            })
        }
    }
}
