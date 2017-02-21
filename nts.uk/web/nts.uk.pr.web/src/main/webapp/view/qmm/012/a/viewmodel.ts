module qmm012.a.viewmodel {
    export class ScreenModel {
        //Switch
        roundingRules: KnockoutObservableArray<any>;
        selectedRuleCode: any;
        enable: KnockoutObservable<boolean>;
        constructor() {
            let self = this;
            //start Switch Data
            self.enable = ko.observable(true);
            self.roundingRules = ko.observableArray([
                { code: 'group1', name: '�x������' },
                { code: 'group2', name: '�T������' },
                { code: 'group3', name: '�ΑӍ���' }
            ]);
            self.selectedRuleCode = ko.observable('group1');
            //endSwitch Data
        }
        submitInfo() {
            let self = this;
            let groupName = self.selectedRuleCode;
            nts.uk.ui.windows.setShared('groupName', groupName());
            nts.uk.ui.windows.close();
        }
        closeDialog() {
            nts.uk.ui.windows.close();
        }
        
        start(): JQueryPromise<any> {
            let self = this;
            // Page load dfd.
            var dfd = $.Deferred();
            //dropdownlist event
        }
    }

}