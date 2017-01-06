module qmm018.a.viewmodel {
    export class ScreenModel {
        paymentDateProcessingList: KnockoutObservableArray<any>;
        selectedPaymentDate: KnockoutObservable<any>;
        roundingRules: KnockoutObservableArray<any>;
        selectedRuleCode: any;
        itemList: KnockoutObservableArray<any>;
        itemName: KnockoutObservable<string>;
        currentCode: KnockoutObservable<number>
        isEnable: KnockoutObservable<boolean>;
        isEditable: KnockoutObservable<boolean>;
        texteditor1: any;
        texteditor2: any;
        constructor() {
            var self = this;
            self.paymentDateProcessingList = ko.observableArray([]);
            self.selectedPaymentDate = ko.observable(null);
            self.roundingRules = ko.observableArray([
                { code: '1', name: '就業からの連携' },
                { code: '2', name: '明細書項目から選択' }
            ]);
            self.selectedRuleCode = ko.observable(1);
            self.itemList = ko.observableArray([
                { code: '1', name: '足した後' },
                { code: '2', name: '足す前' }
            ]);
            self.itemName = ko.observable('');
            self.currentCode = ko.observable(3);
            self.isEnable = ko.observable(true);
            self.isEditable = ko.observable(true);
            self.texteditor1 = {
                value: ko.observable(''),
                constraint: 'ResidenceCode',
                option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                    textmode: "text",
                    placeholder: "",
                    width: "500px",
                    textalign: "left"
                })),
                required: ko.observable(true),
                enable: ko.observable(true),
                readonly: ko.observable(false)
            };
            self.texteditor2 = {
                value: ko.observable(''),
                constraint: 'ResidenceCode',
                option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                    textmode: "text",
                    placeholder: "",
                    width: "30px",
                    textalign: "center"
                })),
                required: ko.observable(true),
                enable: ko.observable(true),
                readonly: ko.observable(false)
            };
            
        }

        startPage(): JQueryPromise<any> {
            var self = this;

            var dfd = $.Deferred();
            qmm018.a.service.getPaymentDateProcessingList().done(function(data) {
                self.paymentDateProcessingList(data);
                dfd.resolve();
            }).fail(function(res) {

            });
            return dfd.promise();
        }
        
        openSubWindow() {
            nts.uk.ui.windows.sub.modal("/view/qmm/018/b/index.xhtml"); 
        }
    }
}