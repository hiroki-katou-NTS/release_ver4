module nts.uk.pr.view.qmm008.b {
    export module viewmodel {
        import InsuranceOfficeItem = service.model.finder.InsuranceOfficeItemDto;
        import RoundingDto = service.model.finder.RoundingDto;
        import RoundingItemDto = service.model.finder.RoundingItemDto;
        import Enum = service.model.finder.Enum;
        import HealthInsuranceRateDto = service.model.finder.HealthInsuranceRateDto;
        import OfficeItemDto = service.model.finder.OfficeItemDto;
        import HealthInsuranceRateItemDto = service.model.finder.HealthInsuranceRateItemDto;
        import ChargeRateItemDto = service.model.finder.ChargeRateItemDto;
        import ScreenBaseModel = base.simplehistory.viewmodel.ScreenBaseModel;

        export class ScreenModel extends ScreenBaseModel<service.model.Office, service.model.Health>{
            //Health insurance rate Model
            healthModel: KnockoutObservable<HealthInsuranceRateModel>;
            healthInsuranceOfficeList: KnockoutObservableArray<InsuranceOfficeItem>;
            //list rounding options
            roundingList: KnockoutObservableArray<Enum>;
            //numberInputOptions
            Rate3: KnockoutObservable<nts.uk.ui.option.NumberEditorOption>;
            Rate5: KnockoutObservable<nts.uk.ui.option.NumberEditorOption>;
            healthFilteredData: KnockoutObservableArray<any>;
            //for health auto calculate switch button
            healthAutoCalculateOptions: KnockoutObservableArray<any>;
            //for control data after close dialog
            isTransistReturnData: KnockoutObservable<boolean>;
            // Flags
            isLoading: KnockoutObservable<boolean>;
            currentOfficeCode: KnockoutObservable<string>;
            sendOfficeData: KnockoutObservable<string>;

            japanYear: KnockoutObservable<string>;

            errorList: KnockoutObservableArray<any>;
            dirty: nts.uk.ui.DirtyChecker;
            backupDataDirty: KnockoutObservable<HealthInsuranceRateDto>;
            canOpenOfficeRegisterDialog: KnockoutObservable<boolean>;
            constructor() {
                super({
                    functionName: '健康保険',
                    service: service.instance,
                    removeMasterOnLastHistoryRemove: false
                });
                var self = this;
                //init model
                self.healthModel = ko.observable(new HealthInsuranceRateModel());
                // init insurance offices list
                self.healthInsuranceOfficeList = ko.observableArray<InsuranceOfficeItem>([]);

                self.healthFilteredData = ko.observableArray(nts.uk.util.flatArray(self.healthInsuranceOfficeList(), "childs"));
                //init rounding list
                self.roundingList = ko.observableArray<Enum>([]);
                self.Rate3 = ko.mapping.fromJS(new nts.uk.ui.option.NumberEditorOption({
                    grouplength: 3,
                    decimallength: 3
                }));
                self.Rate5 = ko.mapping.fromJS(new nts.uk.ui.option.NumberEditorOption({
                    grouplength: 3,
                    decimallength: 5
                }));
                //health calculate switch
                self.healthAutoCalculateOptions = ko.observableArray([
                    { code: '0', name: 'する' },
                    { code: '1', name: 'しない' }
                ]);
                // add history dialog
                self.isTransistReturnData = ko.observable(false);
                // Health CurrencyEditor
                self.isLoading = ko.observable(true);
                self.currentOfficeCode = ko.observable('');
                self.sendOfficeData = ko.observable('');
                self.japanYear = ko.observable('');
                self.errorList = ko.observableArray([
                    { messageId: "ER001", message: "＊が入力されていません。" },
                    { messageId: "ER007", message: "＊が選択されていません。" },
                    { messageId: "ER005", message: "入力した＊は既に存在しています。\r\n ＊を確認してください。" },
                    { messageId: "ER008", message: "選択された＊は使用されているため削除できません。" },
                    { messageId: "AL001", message: "変更された内容が登録されていません。\r\n よろしいですか。" }
                ]);
                self.dirty = new nts.uk.ui.DirtyChecker(self.healthModel);
                self.backupDataDirty = ko.observable<HealthInsuranceRateDto>();
                self.canOpenOfficeRegisterDialog = ko.observable(true);
            } //end constructor

            // Start
            public start(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred<any>();
                self.getAllRounding().done(function() {
                    // Resolve
                    dfd.resolve(null);
                }).fail((res) => {
                    nts.uk.ui.dialog.alert(res.message);
                });
                // Return.
                return dfd.promise();
            }

            //load All rounding method
            public getAllRounding(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred<any>();
                // Invoked service method
                service.findAllRounding().done(function(data: Array<Enum>) {
                    // Set list.
                    self.roundingList(data);
                    dfd.resolve(data);
                }).fail((res) => {
                    nts.uk.ui.dialog.alert(res.message);
                });
                // Return.
                return dfd.promise();
            }

            //string rounding to value
            public convertRounding(stringRounding: string) {
                switch (stringRounding) {
                    case Rounding.TRUNCATION: return "0";
                    case Rounding.ROUNDUP: return "1";
                    case Rounding.DOWN4_UP5: return "2";
                    case Rounding.DOWN5_UP6: return "3";
                    case Rounding.ROUNDDOWN: return "4";
                    default: return "0";
                }
            }

            //value to string rounding
            public convertToRounding(stringValue: string) {
                switch (stringValue) {
                    case "0": return Rounding.TRUNCATION;
                    case "1": return Rounding.ROUNDUP;
                    case "2": return Rounding.DOWN4_UP5;
                    case "3": return Rounding.DOWN5_UP6;
                    case "4": return Rounding.ROUNDDOWN;
                    default: return Rounding.TRUNCATION;
                }
            }

            //load health data by history code
            public loadHealth(data: HealthInsuranceRateDto) {
                var self = this;
                if (data == null) {
                    return;
                }
                //Set health detail.
                self.healthModel().historyId = data.historyId;
                self.healthModel().startMonth(nts.uk.time.formatYearMonth(parseInt(data.startMonth)));
                self.healthModel().endMonth(nts.uk.time.formatYearMonth(parseInt(data.endMonth)));
                self.japanYear("(" + nts.uk.time.yearmonthInJapanEmpire(data.startMonth).toString() + ")");

                self.healthModel().companyCode = data.companyCode;
                self.healthModel().officeCode(data.officeCode);
                self.healthModel().autoCalculate(data.autoCalculate);
                data.rateItems.forEach(function(item, index) {
                    if (item.payType == PaymentType.SALARY && item.insuranceType == HealthInsuranceType.GENERAL) {
                        self.healthModel().rateItems().healthSalaryPersonalGeneral(item.chargeRate.personalRate);
                        self.healthModel().rateItems().healthSalaryCompanyGeneral(item.chargeRate.companyRate);
                    }
                    if (item.payType == PaymentType.BONUS && item.insuranceType == HealthInsuranceType.GENERAL) {
                        self.healthModel().rateItems().healthBonusPersonalGeneral(item.chargeRate.personalRate);
                        self.healthModel().rateItems().healthBonusCompanyGeneral(item.chargeRate.companyRate);
                    }
                    if (item.payType == PaymentType.SALARY && item.insuranceType == HealthInsuranceType.NURSING) {
                        self.healthModel().rateItems().healthSalaryPersonalNursing(item.chargeRate.personalRate);
                        self.healthModel().rateItems().healthSalaryCompanyNursing(item.chargeRate.companyRate);
                    }
                    if (item.payType == PaymentType.BONUS && item.insuranceType == HealthInsuranceType.NURSING) {
                        self.healthModel().rateItems().healthBonusPersonalNursing(item.chargeRate.personalRate);
                        self.healthModel().rateItems().healthBonusCompanyNursing(item.chargeRate.companyRate);
                    }
                    if (item.payType == PaymentType.SALARY && item.insuranceType == HealthInsuranceType.BASIC) {
                        self.healthModel().rateItems().healthSalaryPersonalBasic(item.chargeRate.personalRate);
                        self.healthModel().rateItems().healthSalaryCompanyBasic(item.chargeRate.companyRate);
                    }
                    if (item.payType == PaymentType.BONUS && item.insuranceType == HealthInsuranceType.BASIC) {
                        self.healthModel().rateItems().healthBonusPersonalBasic(item.chargeRate.personalRate);
                        self.healthModel().rateItems().healthBonusCompanyBasic(item.chargeRate.companyRate);
                    }
                    if (item.payType == PaymentType.SALARY && item.insuranceType == HealthInsuranceType.SPECIAL) {
                        self.healthModel().rateItems().healthSalaryPersonalSpecific(item.chargeRate.personalRate);
                        self.healthModel().rateItems().healthSalaryCompanySpecific(item.chargeRate.companyRate);
                    }
                    if (item.payType == PaymentType.BONUS && item.insuranceType == HealthInsuranceType.SPECIAL) {
                        self.healthModel().rateItems().healthBonusPersonalSpecific(item.chargeRate.personalRate);
                        self.healthModel().rateItems().healthBonusCompanySpecific(item.chargeRate.companyRate);
                    }
                });
                //set rounding list
                self.healthModel().roundingMethods().healthSalaryPersonalComboBox(self.roundingList());
                self.healthModel().roundingMethods().healthSalaryCompanyComboBox(self.roundingList());
                self.healthModel().roundingMethods().healthBonusPersonalComboBox(self.roundingList());
                self.healthModel().roundingMethods().healthBonusCompanyComboBox(self.roundingList());

                //Set selected rounding method
                data.roundingMethods.forEach(function(item, index) {
                    if (item.payType == PaymentType.SALARY) {
                        self.healthModel().roundingMethods().healthSalaryPersonalComboBoxSelectedCode(self.convertRounding(item.roundAtrs.personalRoundAtr));
                        self.healthModel().roundingMethods().healthSalaryCompanyComboBoxSelectedCode(self.convertRounding(item.roundAtrs.companyRoundAtr));
                    }
                    else {
                        self.healthModel().roundingMethods().healthBonusPersonalComboBoxSelectedCode(self.convertRounding(item.roundAtrs.personalRoundAtr));
                        self.healthModel().roundingMethods().healthBonusCompanyComboBoxSelectedCode(self.convertRounding(item.roundAtrs.companyRoundAtr));
                    }
                });
                self.healthModel().maxAmount(data.maxAmount);
            }

            private healthCollectData() {
                var self = this;
                var rates = self.healthModel().rateItems();

                var rateItems: Array<HealthInsuranceRateItemDto> = [];
                rateItems.push(new HealthInsuranceRateItemDto(PaymentType.SALARY, HealthInsuranceType.GENERAL, new ChargeRateItemDto(rates.healthSalaryCompanyGeneral(), rates.healthSalaryPersonalGeneral())));
                rateItems.push(new HealthInsuranceRateItemDto(PaymentType.SALARY, HealthInsuranceType.NURSING, new ChargeRateItemDto(rates.healthSalaryCompanyNursing(), rates.healthSalaryPersonalNursing())));
                rateItems.push(new HealthInsuranceRateItemDto(PaymentType.SALARY, HealthInsuranceType.BASIC, new ChargeRateItemDto(rates.healthSalaryCompanyBasic(), rates.healthSalaryPersonalBasic())));
                rateItems.push(new HealthInsuranceRateItemDto(PaymentType.SALARY, HealthInsuranceType.SPECIAL, new ChargeRateItemDto(rates.healthSalaryCompanySpecific(), rates.healthSalaryPersonalSpecific())));
                rateItems.push(new HealthInsuranceRateItemDto(PaymentType.BONUS, HealthInsuranceType.GENERAL, new ChargeRateItemDto(rates.healthBonusCompanyGeneral(), rates.healthBonusPersonalGeneral())));
                rateItems.push(new HealthInsuranceRateItemDto(PaymentType.BONUS, HealthInsuranceType.NURSING, new ChargeRateItemDto(rates.healthBonusCompanyNursing(), rates.healthBonusPersonalNursing())));
                rateItems.push(new HealthInsuranceRateItemDto(PaymentType.BONUS, HealthInsuranceType.BASIC, new ChargeRateItemDto(rates.healthBonusCompanyBasic(), rates.healthBonusPersonalBasic())));
                rateItems.push(new HealthInsuranceRateItemDto(PaymentType.BONUS, HealthInsuranceType.SPECIAL, new ChargeRateItemDto(rates.healthBonusCompanySpecific(), rates.healthBonusPersonalSpecific())));

                var roundingMethods: Array<RoundingDto> = [];
                var rounding = self.healthModel().roundingMethods();
                roundingMethods.push(new RoundingDto(PaymentType.SALARY, new RoundingItemDto(self.convertToRounding(self.healthModel().roundingMethods().healthSalaryPersonalComboBoxSelectedCode()), self.convertToRounding(self.healthModel().roundingMethods().healthSalaryCompanyComboBoxSelectedCode()))));
                roundingMethods.push(new RoundingDto(PaymentType.BONUS, new RoundingItemDto(self.convertToRounding(self.healthModel().roundingMethods().healthBonusPersonalComboBoxSelectedCode()), self.convertToRounding(self.healthModel().roundingMethods().healthBonusCompanyComboBoxSelectedCode()))));
                return new service.model.finder.HealthInsuranceRateDto(self.healthModel().historyId, self.healthModel().companyCode, self.currentOfficeCode(), self.healthModel().startMonth(), self.healthModel().endMonth(), self.healthModel().autoCalculate(), rateItems, roundingMethods, self.healthModel().maxAmount());
            }

            //get current item office 
            public getDataOfHealthSelectedOffice(): InsuranceOfficeItem {
                var self = this;
                var saveVal: InsuranceOfficeItem = null;
                // Set parent value
                self.healthInsuranceOfficeList().forEach(function(item, index) {
                    if (self.currentOfficeCode() == item.code) {
                        saveVal = item;
                    }
                });
                return saveVal;
            }

            /**
             * Load History detail.
             */
            onSelectHistory(id: string): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();
                self.isLoading(true);
                self.isClickHistory(true);
                //get current office
                self.currentOfficeCode(self.getCurrentOfficeCode(id));
                // clear all error

                service.instance.findHistoryByUuid(id).done(dto => {
                    self.backupDataDirty(dto);
                    self.loadHealth(dto);
                    self.dirty = new nts.uk.ui.DirtyChecker(self.healthModel);
                    self.isLoading(false);
                    dfd.resolve();
                }).fail((res) => {
                    nts.uk.ui.dialog.alert(res.message);
                });

                return dfd.promise();

            }

            onSave(): JQueryPromise<string> {
                var self = this;
                var dfd = $.Deferred<string>();

                self.dirty.reset();
                
                //check auto calculate
                if (self.healthModel().autoCalculate() == AutoCalculateType.Auto) {
                    nts.uk.ui.dialog.confirm("自動計算が行われます。登録しますか？").ifYes(function() {
                        // Validate.
                        $('.nts-input').ntsEditor('validate');
                        if ($('.nts-input').ntsError('hasError')) {
                            dfd.reject();
                            return dfd.promise();
                        }

                        //update health
                        service.updateHealthRate(self.healthCollectData()).done(function() {
                            self.backupDataDirty(self.healthCollectData());
                        }).fail((res) => {
                            nts.uk.ui.dialog.alert(res.message);
                        });
                    }).ifNo(function() {
                    });
                }
                else {
                    // Validate.
                    $('.nts-input').ntsEditor('validate');
                    if ($('.nts-input').ntsError('hasError')) {
                        dfd.reject();
                        return dfd.promise();
                    }

                    //update health
                    service.updateHealthRate(self.healthCollectData()).done(function() {
                        self.backupDataDirty(self.healthCollectData());
                    }).fail((res) => {
                        nts.uk.ui.dialog.alert(res.message);
                    });
                }

                dfd.resolve(self.healthModel().historyId)
                return dfd.promise();
            }

            clearErrors(): void {
                if (nts.uk.ui._viewModel) {
                    $('.save-error').ntsError('clear');
                }
            }

            /**
             * On select master data.
             */
            onSelectMaster(code: string): void {
                var self = this;
                self.isClickHistory(false);
                self.clearErrors();
            }

            public getCurrentOfficeCode(childId: string): string {
                var self = this;
                var returnValue: string;
                if (self.masterHistoryList.length > 0) {
                    self.masterHistoryList.forEach(function(parentItem) {
                        if (parentItem.historyList) {
                            parentItem.historyList.forEach(function(childItem) {
                                if (childItem.uuid == childId) {
                                    self.sendOfficeData(parentItem.name);
                                    returnValue = parentItem.code;
                                }
                            });
                        } else {
                            return parentItem.code;
                        }
                    });
                }
                return returnValue;
            }

            /**
             * Clear all input and switch to new mode.
             */
            onRegistNew(): void {
                var self = this;
                if (self.canOpenOfficeRegisterDialog()) {
                    self.OpenModalOfficeRegister();
                }
                self.isClickHistory(false);
                self.canOpenOfficeRegisterDialog(false);
            }

            isDirty(): boolean {
                var self = this;
                return self.dirty.isDirty();
            }

            public OpenModalOfficeRegisterWithDirtyCheck() {
                var self = this;
                if (self.dirty.isDirty()) {
                    nts.uk.ui.dialog.confirm(self.errorList()[4].message).ifYes(function() {
                        self.loadHealth(self.backupDataDirty());
                        self.OpenModalOfficeRegister();
                        self.dirty.reset();
                    }).ifNo(function() {
                    });
                }
                else {
                    self.OpenModalOfficeRegister();
                }
            }
            //open office register dialog
            public OpenModalOfficeRegister() {
                var self = this;
                // Set parent value
                nts.uk.ui.windows.sub.modal("/view/qmm/008/e/index.xhtml", { title: "会社保険事業所の登録＞事業所の登録", dialogClass: 'no-close' }).onClosed(() => {
                    //when close dialog -> reload office list
                    var codeOfNewOffice = nts.uk.ui.windows.getShared("codeOfNewOffice");
                    self.reloadMasterHistory(codeOfNewOffice);
                    //                    self.igGridSelectedHistoryUuid(codeOfNewOffice);
                });
            }

            public OpenModalStandardMonthlyPriceHealthWithDirtyCheck() {
                var self = this;
                if (self.dirty.isDirty()) {
                    nts.uk.ui.dialog.confirm(self.errorList()[4].message).ifYes(function() {
                        self.loadHealth(self.backupDataDirty());
                        self.OpenModalStandardMonthlyPriceHealth();
                        self.dirty.reset();
                    }).ifNo(function() {
                    });
                }
                else {
                    self.OpenModalStandardMonthlyPriceHealth();
                }
            }

            //open modal standard monthly price health
            public OpenModalStandardMonthlyPriceHealth() {
                var self = this;
                // Set parent value
                nts.uk.ui.windows.setShared("officeName", this.sendOfficeData());
                nts.uk.ui.windows.setShared("healthModel", this.healthModel());

                nts.uk.ui.windows.setShared("isTransistReturnData", this.isTransistReturnData());
                nts.uk.ui.windows.sub.modal("/view/qmm/008/h/index.xhtml", { title: "会社保険事業所の登録＞標準報酬月額保険料額表", dialogClass: 'no-close' }).onClosed(() => {
                    // Get child value
                    var returnValue = nts.uk.ui.windows.getShared("listOfficeOfChildValue");
                });
            }
            public goToPension() {
                nts.uk.request.jump("/view/qmm/008/c/index.xhtml");
            }
        }

        export class HealthInsuranceRateModel {
            historyId: string;
            companyCode: string;
            officeCode: KnockoutObservable<string>;
            startMonth: KnockoutObservable<string>;
            endMonth: KnockoutObservable<string>;
            autoCalculate: KnockoutObservable<number>;
            rateItems: KnockoutObservable<HealthInsuranceRateItemModel>;
            roundingMethods: KnockoutObservable<HealthInsuranceRoundingModel>;
            maxAmount: KnockoutObservable<number>;
            constructor() {
                this.startMonth = ko.observable("");
                this.endMonth = ko.observable("");
                this.officeCode = ko.observable('');
                this.autoCalculate = ko.observable(0);
                this.rateItems = ko.observable(new HealthInsuranceRateItemModel());
                this.roundingMethods = ko.observable(new HealthInsuranceRoundingModel());
                this.maxAmount = ko.observable(0);
            }

        }
        export class HealthInsuranceRateItemModel {
            healthSalaryPersonalGeneral: KnockoutObservable<number>;
            healthSalaryPersonalNursing: KnockoutObservable<number>;
            healthSalaryPersonalBasic: KnockoutObservable<number>;
            healthSalaryPersonalSpecific: KnockoutObservable<number>;
            healthSalaryCompanyGeneral: KnockoutObservable<number>;
            healthSalaryCompanyNursing: KnockoutObservable<number>;
            healthSalaryCompanyBasic: KnockoutObservable<number>;
            healthSalaryCompanySpecific: KnockoutObservable<number>;

            healthBonusPersonalGeneral: KnockoutObservable<number>;
            healthBonusPersonalNursing: KnockoutObservable<number>;
            healthBonusPersonalBasic: KnockoutObservable<number>;
            healthBonusPersonalSpecific: KnockoutObservable<number>;
            healthBonusCompanyGeneral: KnockoutObservable<number>;
            healthBonusCompanyNursing: KnockoutObservable<number>;
            healthBonusCompanyBasic: KnockoutObservable<number>;
            healthBonusCompanySpecific: KnockoutObservable<number>;
            constructor() {
                this.healthSalaryPersonalGeneral = ko.observable(0);
                this.healthSalaryCompanyGeneral = ko.observable(0);
                this.healthBonusPersonalGeneral = ko.observable(0);
                this.healthBonusCompanyGeneral = ko.observable(0);

                this.healthSalaryPersonalNursing = ko.observable(0);
                this.healthSalaryCompanyNursing = ko.observable(0);
                this.healthBonusPersonalNursing = ko.observable(0);
                this.healthBonusCompanyNursing = ko.observable(0);

                this.healthSalaryPersonalBasic = ko.observable(0);
                this.healthSalaryCompanyBasic = ko.observable(0);
                this.healthBonusPersonalBasic = ko.observable(0);
                this.healthBonusCompanyBasic = ko.observable(0);

                this.healthSalaryPersonalSpecific = ko.observable(0);
                this.healthSalaryCompanySpecific = ko.observable(0);
                this.healthBonusPersonalSpecific = ko.observable(0);
                this.healthBonusCompanySpecific = ko.observable(0);

            }
        }

        export class HealthInsuranceRoundingModel {
            healthSalaryPersonalComboBox: KnockoutObservableArray<Enum>;
            healthSalaryPersonalComboBoxItemName: KnockoutObservable<string>;
            healthSalaryPersonalComboBoxCurrentCode: KnockoutObservable<number>
            healthSalaryPersonalComboBoxSelectedCode: KnockoutObservable<string>;

            healthSalaryCompanyComboBox: KnockoutObservableArray<Enum>;
            healthSalaryCompanyComboBoxItemName: KnockoutObservable<string>;
            healthSalaryCompanyComboBoxCurrentCode: KnockoutObservable<number>
            healthSalaryCompanyComboBoxSelectedCode: KnockoutObservable<string>;

            healthBonusPersonalComboBox: KnockoutObservableArray<Enum>;
            healthBonusPersonalComboBoxItemName: KnockoutObservable<string>;
            healthBonusPersonalComboBoxCurrentCode: KnockoutObservable<number>
            healthBonusPersonalComboBoxSelectedCode: KnockoutObservable<string>;

            healthBonusCompanyComboBox: KnockoutObservableArray<Enum>;
            healthBonusCompanyComboBoxItemName: KnockoutObservable<string>;
            healthBonusCompanyComboBoxCurrentCode: KnockoutObservable<number>
            healthBonusCompanyComboBoxSelectedCode: KnockoutObservable<string>;
            constructor() {
                this.healthSalaryPersonalComboBox = ko.observableArray<Enum>(null);
                this.healthSalaryPersonalComboBoxItemName = ko.observable('');
                this.healthSalaryPersonalComboBoxCurrentCode = ko.observable(1);
                this.healthSalaryPersonalComboBoxSelectedCode = ko.observable('');

                this.healthSalaryCompanyComboBox = ko.observableArray<Enum>(null);
                this.healthSalaryCompanyComboBoxItemName = ko.observable('');
                this.healthSalaryCompanyComboBoxCurrentCode = ko.observable(3);
                this.healthSalaryCompanyComboBoxSelectedCode = ko.observable('002');

                this.healthBonusPersonalComboBox = ko.observableArray<Enum>(null);
                this.healthBonusPersonalComboBoxItemName = ko.observable('');
                this.healthBonusPersonalComboBoxCurrentCode = ko.observable(3);
                this.healthBonusPersonalComboBoxSelectedCode = ko.observable('002');

                this.healthBonusCompanyComboBox = ko.observableArray<Enum>(null);
                this.healthBonusCompanyComboBoxItemName = ko.observable('');
                this.healthBonusCompanyComboBoxCurrentCode = ko.observable(3);
                this.healthBonusCompanyComboBoxSelectedCode = ko.observable('002');
            }
        }
    }

    export class ChargeRateItem {
        companyRate: KnockoutObservable<number>;
        personalRate: KnockoutObservable<number>;
    }

    export class PaymentType {
        static SALARY = 'Salary';
        static BONUS = 'Bonus'
    }

    export class HealthInsuranceType {
        static GENERAL = 'General';
        static NURSING = 'Nursing';
        static BASIC = 'Basic';
        static SPECIAL = 'Special'
    }
    export class Rounding {
        static ROUNDUP = 'RoundUp';
        static TRUNCATION = 'Truncation';
        static ROUNDDOWN = 'RoundDown';
        static DOWN5_UP6 = 'Down5_Up6';
        static DOWN4_UP5 = 'Down4_Up5'
    }
    export class InsuranceGender {
        static MALE = "Male";
        static FEMALE = "Female";
        static UNKNOW = "Unknow";
    }
    export class AutoCalculate {
        static AUTO = "Auto";
        static MANUAL = "Manual";
    }
    export enum Number {
        Zero = 0,
        One = 1,
        Three = 3
    }
    export enum AutoCalculateType {
        Auto = 0,
        Manual = 1
    }
}
