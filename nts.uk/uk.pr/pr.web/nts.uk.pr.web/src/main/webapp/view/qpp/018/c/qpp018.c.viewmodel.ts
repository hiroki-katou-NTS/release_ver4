module nts.uk.pr.view.qpp018.c {
    export module viewmodel {
        export class ScreenModel {
            
            checklistPrintSettingModel: KnockoutObservable<ChecklistPrintSettingModel>;
            
            constructor() {
                let self = this;
                self.checklistPrintSettingModel = ko.observable(new ChecklistPrintSettingModel());
            }
            
            startPage(): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred<any>();
                self.loadCheckListPrintSetting().done(function() {
                    dfd.resolve(self);
                });
                return dfd.promise();
            }
            
            loadCheckListPrintSetting(): JQueryPromise<service.model.CheckListPrintSettingDto> {
                let self = this;
                let dfd = $.Deferred<service.model.CheckListPrintSettingDto>();
                service.findCheckListPrintSetting().done(function (data) {
                    self.initUI(data);
                    dfd.resolve();
                }).fail(function(res) {
                    nts.uk.ui.dialog.alert(res.message);
                })
                return dfd.promise();
            }
            
            saveConfigSetting(): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred<any>();
                if (self.validate()) {
                    return;
                }
                service.saveCheckListPrintSetting(self).done(function(res: any) {
                    dfd.resolve(res);
                }).fail(function(res) {
                    dfd.reject(res);
                }).always(function() {
                    self.closeDialog();
                });
                return dfd.promise();
            }
            
            validate(): boolean {
                let self = this;
                let isError: boolean = false;
                self.clearError();
                let checklistSetting = self.checklistPrintSettingModel();
                if (!checklistSetting.showDetail() && !checklistSetting.showOffice() && !checklistSetting.showTotal() 
                    && !checklistSetting.showDeliveryNoticeAmount()) {
                    isError = true;
                    $('#require-least-item').ntsError('set', 'が入力されていません。');
                }
                return isError;
            }
            
            clearError(): void {
                $('#require-least-item').ntsError('clear');
            }
            
            closeDialog() {
                nts.uk.ui.windows.close();
            }
            
            initUI(res: service.model.CheckListPrintSettingDto): void {
                let self = this;
                let checklistSetting = self.checklistPrintSettingModel();
                checklistSetting.setData(res);
            }
        }
        
        /**
         * The ChecklistPrintSettingModel
         */
        export class ChecklistPrintSettingModel {
            
            healthInsuranceItems: KnockoutObservableArray<any>;
            selectedHealthInsuranceItem: KnockoutObservable<string>;
            showCategoryInsuranceItem: KnockoutObservable<boolean>;
            showDeliveryNoticeAmount: KnockoutObservable<boolean>;
            showDetail: KnockoutObservable<boolean>;
            showOffice: KnockoutObservable<boolean>;
            showTotal: KnockoutObservable<boolean>;
            
            constructor() {
                let self = this;
                self.selectedHealthInsuranceItem = ko.observable("indicate");
                self.showCategoryInsuranceItem = ko.computed(function () {
                    if (self.selectedHealthInsuranceItem() == 'indicate') {
                        return true;
                    }
                    return false;
                }, self);
                self.showDetail = ko.observable(true);
                self.showOffice = ko.observable(true);
                self.showTotal = ko.observable(true);
                self.showDeliveryNoticeAmount = ko.observable(true);
                self.healthInsuranceItems = ko.observableArray([
                    {code: "indicate", name: "表示する"},
                    {code: "hide", name: "表示しない"}
                ]);
            }
            
            setData(dto: service.model.CheckListPrintSettingDto): void {
                let self = this;
                var insuranceItemCode = dto.showCategoryInsuranceItem ? 'indicate' : 'hide';
                self.selectedHealthInsuranceItem(insuranceItemCode);
                self.showDetail(dto.showDetail);
                self.showOffice(dto.showOffice);
                self.showTotal(dto.showTotal);
                self.showDeliveryNoticeAmount(dto.showDeliveryNoticeAmount);
            }
        }
    }
}