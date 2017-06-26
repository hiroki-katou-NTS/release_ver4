module nts.uk.pr.view.qpp018.a {
    export module viewmodel {
        import InsuranceOffice = service.model.InsuranceOffice;
        export class ScreenModel {
            
            yearMonth: KnockoutObservable<number>;
            isEqual: KnockoutObservable<boolean>;
            isDeficient: KnockoutObservable<boolean>;
            isRedundant: KnockoutObservable<boolean>;
            insuranceOffice: KnockoutObservable<InsuranceOfficeModel>;
            japanYearmonth: KnockoutComputed<string>;
            
            constructor() {
                let self = this;
                self.yearMonth = ko.observable(null);
                self.isEqual = ko.observable(true);
                self.isDeficient = ko.observable(true);
                self.isRedundant = ko.observable(true);
                self.insuranceOffice = ko.observable(new InsuranceOfficeModel());
                self.japanYearmonth = ko.computed(() => {
                    return nts.uk.time.yearmonthInJapanEmpire(self.yearMonth()).toString();
                })
            }
            
            /**
             * start page
             */
            startPage(): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred();
                self.yearMonth(self.getCurrentYearMonth());
                
                $.when(self.insuranceOffice().findAllInsuranceOffice()).done(function() {
                    if (self.insuranceOffice().items().length > 0) {
                        self.insuranceOffice().selectFirst();
                    }
                    dfd.resolve();
                })
                return dfd.promise();
            }
            
            /**
             *  Export Data
             */
            exportData(): void {
                let self = this;
                let dfd = $.Deferred<void>();
                self.clearAllError();
                if (self.validate()) {
                    return;
                }
                let command = self.toJSObjet();
                service.saveAsPdf(command).done(function() {
                    dfd.resolve();
                }).fail(function(res) {
                    nts.uk.ui.dialog.alert(res.message);
                });
                
            }
            
            /**
             * to JSon Object
             */
            private toJSObjet(): any {
                let self = this;
                let command: any = {};
                command.yearMonth = self.yearMonth();
                command.isEqual = self.isEqual();
                command.isDeficient = self.isDeficient();
                command.isRedundant = self.isRedundant();
                command.officeCodes = self.insuranceOffice().getSelectedOffice();
                return command;
            }
            
            /**
             *  Show dialog ChecklistPrintSetting
             */
            showDialogChecklistPrintSetting(): void {
                nts.uk.ui.windows.setShared("socialInsuranceFeeChecklist", null);
                nts.uk.ui.windows.sub.modal("/view/qpp/018/c/index.xhtml", { title: "印刷の設定" });
            }
            
            /**
             * validate
             */
            private validate(): boolean {
                let self = this;
                let isError = false;
                // Validate year month
                $('#date-picker').ntsEditor('validate');
                if (!self.isEqual() && !self.isDeficient() && !self.isRedundant()){
                    // message ER001
                    $('.extract-condition-error').ntsError('set', 'が入力されていません。');
                    isError = true;
                }
                if (self.insuranceOffice().selectedOfficeCodeList().length <= 0) {
                    $('.grid-error').ntsError('set', '社会保険事業所が入力されていません。');
                    isError = true;
                }
                return isError || $('.nts-input').ntsError('hasError');
            }
            
            private clearAllError(): void {
                $('#date-picker').ntsError('clear')
                $('.grid-error').ntsError('clear');
                $('.extract-condition-error').ntsError('clear');
            }
            
            /**
             * get year and month current.
             */
            private getCurrentYearMonth(): number {
                let today = new Date();
                let month = today.getMonth() + 1; //January is 0!
                let year = today.getFullYear();
                let yearMonth = year * 100 + month;
                return yearMonth; 
            }

        }
        
        /**
          * Class InsuranceOfficeModel
          */
        export class InsuranceOfficeModel {
            
            columns: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
            items: KnockoutObservableArray<InsuranceOffice>;
            selectedOfficeCodeList: KnockoutObservableArray<string>;
            
            constructor() {
                let self = this;
                self.items = ko.observableArray<InsuranceOffice>([]);
                self.selectedOfficeCodeList = ko.observableArray<string>([]);
                self.columns = ko.observableArray<nts.uk.ui.NtsGridListColumn>([
                    { headerText: 'コード', key: 'code', width: 200 },
                    { headerText: '名称 ', key: 'name', width: 200 }
                ]);
            }
            
            /**
             * find list insurance office.
             */
            findAllInsuranceOffice(): JQueryPromise<InsuranceOffice[]> {
                let self = this;
                let dfd = $.Deferred<InsuranceOffice[]>();
                service.findAllInsuranceOffice().done(function(res: InsuranceOffice[]) {
                    self.items(res);
                    dfd.resolve();
                }).fail(function(res) {
                    nts.uk.ui.dialog.alert(res.message);
                })
                return dfd.promise();
            }
            
            selectFirst(): void {
                let self = this;
                let code = self.items()[0].code;
                self.selectedOfficeCodeList().push(code);
            }
            
            getSelectedOffice(): String[] {
                let self = this;
                let officeCodes: String[] = [];
                for(let i in self.selectedOfficeCodeList()) {
                    let code: string = self.selectedOfficeCodeList()[i];
                    if (code) {
                        officeCodes.push(code);
                    }
                }
                return officeCodes;
            }
        
            getOfficeByCode(code: string): InsuranceOffice {
                let self = this;
                for(let i in self.items()) {
                    let office = self.items()[i];
                    if (office.code == code) {
                        return office;
                    }
                }
                return null;
            }
        }
    }
}