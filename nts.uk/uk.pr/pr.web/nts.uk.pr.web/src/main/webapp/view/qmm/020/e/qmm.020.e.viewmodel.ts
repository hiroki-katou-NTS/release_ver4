module qmm020.e.viewmodel {
    export class ScreenModel {
        //list box
        itemList: KnockoutObservableArray<any>;
        selectedCode: KnockoutObservable<string>;
        isEnable: KnockoutObservable<boolean>;

        //Grid list
        singleSelectedCode: any;
        selectedCodes: any;
        headers: any;
        items: KnockoutObservableArray<any>;
        columns2: KnockoutObservableArray<any>;
        companyCode: KnockoutObservable<any>;
        currentCodeList: KnockoutObservableArray<any>;
        count: number = 100;

        constructor() {
            var self = this;
            //list box
            self.itemList = ko.observableArray([]);
            self.selectedCode = ko.observable(null)

            //Grid list
            self.singleSelectedCode = ko.observable(null);
            self.items = ko.observableArray([]);
            self.companyCode = ko.observable(3);
            self.isEnable = ko.observable(true);
            self.selectedCodes = ko.observableArray([]);
            self.columns2 = ko.observableArray([
                { headerText: 'コード', key: 'classificationCode', dataType: "string", width: 100 },
                { headerText: '名称', key: 'classificationName', dataType: "string", width: 150 },
                {
                    headerText: "給与明細書", key: "paymentDetailCode", dataType: "string", width: "250px",
                    template: "<input type='button' id='E_BTN_001' value='選択'/><label style='margin-left:5px;'>${paymentDetailCode}</label><label style='margin-left:15px;'></label>"
                },
                {
                    headerText: "賞与明細書", key: "bonusDetailCode", dataType: "string", width: "250px",
                    template: "<input type='button' id='E_BTN_001' value='選択'/><label style='margin-left:5px;'>${bonusDetailCode}</label><label style='margin-left:15px;'></label>"
                },
            ]);
            self.companyCode = ko.observable();
            self.selectedCode.subscribe(function(codeChanged) {
                self.getClassificationAllotSettingList(codeChanged);
            });
        }
        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();

            service.getAllClassificationAllotSettingHeader().done(function(classificationAllotSettingHeaders) {
                if (classificationAllotSettingHeaders.length > 0) {
                    var test = _.map(classificationAllotSettingHeaders, function(value) {
                        return new ClassificationAllotSettingHeaderDto(value.companyCode, value.historyId, value.startDateYM, value.endDateYM);
                    });
                    self.itemList(test);
                    self.selectedCode(ko.unwrap(classificationAllotSettingHeaders[0].historyId));
                    service.getAllClassificationAllotSetting(self.selectedCode()).done(function(classificationAllotSettings) {
                        self.items(classificationAllotSettings);
                        dfd.resolve();
                    });
                }
                else {
                    dfd.resolve();
                }
            });
            return dfd.promise();
        }


        getCurrentSelectItem(): ClassificationAllotSettingHeaderDto {
            var self = this;
            return _.find(self.itemList(), function(item: ClassificationAllotSettingHeaderDto) {
                return item.historyId == self.selectedCode();
            });
        }

        // open J dialog
        openJDialog() {
            var self = this;
            let history = _.find(self.itemList(), function(history) {
                return history.historyId == self.selectedCode();
            });
            let startYm = history.startDateYM;
            let valueShareJDialog = "1~" + startYm;
            nts.uk.ui.windows.setShared('valJDialog', valueShareJDialog);
            nts.uk.ui.windows.sub.modal('/view/qmm/020/j/index.xhtml', { title: '明細書の紐ずけ＞履歴追加' }).onClosed(function(): any {
                var jDialogResult = nts.uk.ui.windows.getShared('returnJDialog');
                if (jDialogResult !== undefined) {
                    var action: string = jDialogResult.split("~")[0];
                    var yearmonth: string = jDialogResult.split("~")[1].replace("/", "");
                    var updateItem: ClassificationAllotSettingHeaderDto = self.getCurrentSelectItem();
                    updateItem.startDateYM = yearmonth;
                    console.log(updateItem);
                    service.updateClassificationAllotSettingHeader(updateItem).done(function() {
                        service.getAllClassificationAllotSettingHeader().done(function(classificationAllotSettingHeaders) {
                            debugger;
                            var test = _.map(classificationAllotSettingHeaders, function(value) {
                                return new ClassificationAllotSettingHeaderDto(value.companyCode, value.historyId, value.startDateYM, value.endDateYM);
                            });
                            self.itemList(test);
                        });

                    });
                }
            });
        }

        // open K dialog
        openKDialog() {
            var self = this;
            //var singleSelectedCode = self.singleSelectedCode().split(';');
            //nts.uk.ui.windows.setShared('stmtCode', singleSelectedCode[0]);
            nts.uk.ui.windows.sub.modal('/view/qmm/020/k/index.xhtml', { title: '明細書の紐ずけ＞履歴編集' }).onClosed(function(): any {
                //self.start(self.singleSelectedCode());
            });
        }

        getClassificationAllotSettingList(historyId: String) {
            var self = this;
            service.getAllClassificationAllotSetting(self.selectedCode()).done(function(classificationAllotSettings) {
                self.items(classificationAllotSettings);
            });
        }
    }
    export class ClassificationAllotSettingHeaderDto {
        uniqueKey: string;
        companyCode: string;
        historyId: string;
        startDateYM: string;
        endDateYM: string;
        displayDate: string;
        constructor(companyCode: string, historyId: string, startDateYM: string, endDateYM: string) {
            var self = this;
            this.companyCode = companyCode;
            this.historyId = historyId;
            this.uniqueKey = companyCode + "-" + historyId;
            this.startDateYM = startDateYM;
            this.endDateYM = endDateYM;
            this.displayDate = nts.uk.time.parseYearMonth(self.startDateYM).format() + " ~ " + nts.uk.time.parseYearMonth(self.endDateYM).format();
        }
    }

    export class ClassificationAllotSettingDto {
        bonusDetailCode: string;
        classificationCode: string;
        companyCode: string;
        historyId: string;
        paymentDetailCode: string;

        constructor(bonusDetailCode: string, classificationCode: string, companyCode: string, historyId: string, paymentDetailCode: string) {
            this.bonusDetailCode = bonusDetailCode;
            this.classificationCode = classificationCode;
            this.companyCode = companyCode;
            this.historyId = historyId;
            this.paymentDetailCode = paymentDetailCode;
        }
    }
}