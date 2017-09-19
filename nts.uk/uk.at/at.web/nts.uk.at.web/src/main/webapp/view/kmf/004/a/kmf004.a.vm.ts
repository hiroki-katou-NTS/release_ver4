module nts.uk.at.view.kmf004.a.viewmodel {
    export class ScreenModel {
        items: KnockoutObservableArray<model.SpecialHolidayDto>;
        sphdList: KnockoutObservableArray<any>;
        columns: KnockoutObservableArray<any>;
        currentCode: KnockoutObservable<any>;

        currentItem: KnockoutObservable<model.SpecialHolidayDto>
        tabs: KnockoutObservableArray<any>;
        selectedTab: KnockoutObservable<string>;
        selectedValue: KnockoutObservable<any>;
        enable: KnockoutObservable<boolean>;

        //Tab Regular
        option1: KnockoutObservable<any>; 
        option2: KnockoutObservable<any>;
   
        //
        date: KnockoutObservable<string>;
        roundingSplitAcquisition: KnockoutObservableArray<any>;
        roundingCarryForward: KnockoutObservableArray<any>;
        roundingGenderAtr: KnockoutObservableArray<any>;
        roundingMakeInvitation: KnockoutObservableArray<any>;
        selectedCode: KnockoutObservable<string>;
        isEnable: KnockoutObservable<boolean>;
        isEditable: KnockoutObservable<boolean>;

        //Input Screen
        roundingPeriodic: KnockoutObservableArray<any>;
        workTypeList: KnockoutObservableArray<any>;
        workTypeNames: KnockoutObservable<string>;
        isEnableCode: KnockoutObservable<boolean>;
        inp_grantMethod: KnockoutObservable<number>;

        //Combobox
        itemAgeBaseYearAtr: KnockoutObservableArray<any>;
        visibleGrantSingle: KnockoutObservable<boolean>;
        visibleGrant: KnockoutObservable<boolean>;

        constructor() {
            var self = this;

            self.visibleGrantSingle = ko.observable(false);
            self.visibleGrant = ko.observable(true);
            self.inp_grantMethod = ko.observable(0);

            self.items = ko.observableArray([]);
            self.sphdList = ko.observableArray([]);
            self.enable = ko.observable(true);

            self.date = ko.observable('');
            self.selectedCode = ko.observable('0002')
            self.isEnable = ko.observable(true);
            self.isEnableCode = ko.observable(false);
            self.isEditable = ko.observable(true);

            self.currentItem = ko.observable(new model.SpecialHolidayDto({}));
            self.workTypeList = ko.observableArray([]);
            self.workTypeNames = ko.observable("");

            self.itemAgeBaseYearAtr = ko.observableArray([
                { code: 0, name: nts.uk.resource.getText('Enum_AgeBaseYearAtr_NEXT_MONTH') },
                { code: 1, name: nts.uk.resource.getText('Enum_AgeBaseYearAtr_THIS_MONTH') }
            ]);
            self.roundingPeriodic = ko.observableArray([
                { code: 0, name: nts.uk.resource.getText('KMF004_15') },
                { code: 1, name: nts.uk.resource.getText('KMF004_14') },
            ]);

            self.columns = ko.observableArray([
                { headerText: nts.uk.resource.getText('KMF004_7'), key: 'specialHolidayCode', width: 100 },
                { headerText: nts.uk.resource.getText('KMF004_8'), key: 'specialHolidayName', width: 150 }
            ]);

            self.roundingSplitAcquisition = ko.observableArray([
                { code: 0, name: nts.uk.resource.getText('KMF004_38') },
                { code: 1, name: nts.uk.resource.getText('KMF004_39') }
            ]);

            self.roundingCarryForward = ko.observableArray([
                { code: 0, name: nts.uk.resource.getText('KMF004_51') },
                { code: 1, name: nts.uk.resource.getText('KMF004_52') }
            ]);

            self.roundingGenderAtr = ko.observableArray([
                { code: 0, name: nts.uk.resource.getText('KMF004_125') },
                { code: 1, name: nts.uk.resource.getText('KMF004_126') }
            ]);

            self.roundingMakeInvitation = ko.observableArray([
                { code: 0, name: nts.uk.resource.getText('KMF004_61') },
                { code: 1, name: nts.uk.resource.getText('KMF004_62') }
            ]);

            //Tab1
            self.selectedValue = ko.observable(false);
            self.option1 = ko.observable({ value: 0, text: nts.uk.resource.getText('KMF004_23') });

            //Tab2
            self.option2 = ko.observable({ value: 1, text: nts.uk.resource.getText('KMF004_24') });

            self.currentCode = ko.observable();
            self.currentCode.subscribe(function(codeChanged) {
                if (codeChanged !== null && codeChanged !== undefined) {
                    self.changedCode(codeChanged);
                    self.isEnableCode(false);
                    $("#code-text2").focus();
                    nts.uk.ui.errors.clearAll();
                }
            });

            self.inp_grantMethod.subscribe(function(value) {
                if (value == 0) {
                    self.visibleGrantSingle(false);
                    self.visibleGrant(true);
                    self.selectedTab('tab-5');
                    nts.uk.ui.errors.clearAll();
                } else {
                    self.visibleGrantSingle(true);
                    self.visibleGrant(false);
                    self.selectedTab('tab-1');
                    nts.uk.ui.errors.clearAll();
                }
            })

            self.tabs = ko.observableArray([
                { id: 'tab-1', title: nts.uk.resource.getText('KMF004_17'), content: '.tab-content-1', enable: ko.observable(true), visible: self.visibleGrantSingle },
                { id: 'tab-2', title: nts.uk.resource.getText('KMF004_18'), content: '.tab-content-2', enable: ko.observable(true), visible: self.visibleGrantSingle },
                { id: 'tab-3', title: nts.uk.resource.getText('KMF004_19'), content: '.tab-content-3', enable: ko.observable(true), visible: self.visibleGrantSingle },
                { id: 'tab-4', title: nts.uk.resource.getText('KMF004_20'), content: '.tab-content-4', enable: ko.observable(true), visible: self.visibleGrantSingle },
                { id: 'tab-5', title: nts.uk.resource.getText('KMF004_21'), content: '.tab-content-5', enable: ko.observable(true), visible: self.visibleGrant }
            ]);
            self.selectedTab = ko.observable('tab-5');
            self.selectedTab.subscribe(function(value) {
                nts.uk.ui.errors.clearAll();
            })
        }

        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            $.when(self.getAllSpecialHoliday(), self.getWorkTypeList()).done(function() {
                if (self.items().length > 0) {
                    self.currentCode(self.items()[0].specialHolidayCode());
                    $("#code-text2").focus();
                } else {
                    self.initSpecialHoliday();
                }
                dfd.resolve();
            }).fail(function() {
                dfd.reject();
            });

            return dfd.promise();
        }

        getAllSpecialHoliday(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            self.items.removeAll();
            //self.sphdList.removeAll();
            service.findAllSpecialHoliday().done(function(specialHoliday_arr: Array<model.ISpecialHolidayDto>) {
                self.sphdList(specialHoliday_arr);
                _.forEach(specialHoliday_arr, function(specialHolidayRes: model.ISpecialHolidayDto) {
                    var specialHoliday: model.ISpecialHolidayDto = {
                        specialHolidayCode: specialHolidayRes.specialHolidayCode,
                        specialHolidayName: specialHolidayRes.specialHolidayName,
                        grantMethod: specialHolidayRes.grantMethod,
                        memo: specialHolidayRes.memo,
                        workTypeList: specialHolidayRes.workTypeList,
                        grantRegular: specialHolidayRes.grantRegular,
                        grantPeriodic: specialHolidayRes.grantPeriodic,
                        sphdLimit: specialHolidayRes.sphdLimit,
                        subCondition: self.toSubConditionDto(specialHolidayRes.subCondition),
                        grantSingle: specialHolidayRes.grantSingle
                    };
                    self.items.push(new model.SpecialHolidayDto(specialHoliday));
                    
                });
                $("#code-text2").focus();
                dfd.resolve();
            }).fail(function(error) {
                alert(error.message);
                dfd.reject(error);
            });
            $("#code-text2").focus();
            return dfd.promise();
        }

        toGrantRegularDto(grantRegular: model.IGrantRegularDto): model.GrantRegularDto {
            if (!grantRegular) {
                return new model.GrantRegularDto({});
            }
            return new model.GrantRegularDto(grantRegular);
        }

        toGrantPeriodicDto(grantPeriodic: model.IGrantPeriodic): model.GrantPeriodicDto {
            if (!grantPeriodic) {
                return new model.GrantPeriodicDto({});
            }
            return new model.GrantPeriodicDto(grantPeriodic);
        }

        toSphdLimitDto(sphdLimit: model.ISphdLimitDto): model.SphdLimitDto {
            if (!sphdLimit) {
                return new model.SphdLimitDto({});
            }
            return new model.SphdLimitDto(sphdLimit);
        }

        toSubConditionDto(subCondition: model.ISubConditionDto): model.ISubConditionDto {
            if (!subCondition) {
                return subCondition;
            }

            subCondition.useGender = Number(subCondition.useGender) == 1;
            subCondition.useEmployee = Number(subCondition.useEmployee) == 1;
            subCondition.useCls = Number(subCondition.useCls) == 1;
            subCondition.useAge = Number(subCondition.useAge) == 1;

            // TODO--

            return subCondition;
        }

        toGrantSingleDto(grantSingle: model.IGrantSingleDto): model.GrantSingleDto {
            if (!grantSingle) {
                return new model.GrantSingleDto({});
            }
            return new model.GrantSingleDto(grantSingle);
        }

        addSpecialHoliday(): JQueryPromise<any> {
            var self = this;
            $(".nts-input").trigger("validate");
            $(".nts-editor").trigger("validate");
            $(".ntsDatepicker").trigger("validate");
            if (nts.uk.ui.errors.hasError()) {
                return;
            }
            var specialHoliday = ko.toJS(self.currentItem());
            if (self.inp_grantMethod() == 0) {
                specialHoliday.grantRegular = null;
                specialHoliday.grantPeriodic = null;
                specialHoliday.sphdLimit = null;
                specialHoliday.subCondition = null;
            } else {
                var useGender = specialHoliday.subCondition.useGender;
                var useEmployee = specialHoliday.subCondition.useEmployee;
                var useCls = specialHoliday.subCondition.useCls;
                var useAge = specialHoliday.subCondition.useAge;

                specialHoliday.subCondition.useGender = useGender ? 1 : 0;
                specialHoliday.subCondition.useEmployee = useEmployee ? 1 : 0;
                specialHoliday.subCondition.useCls = useCls ? 1 : 0;
                specialHoliday.subCondition.useAge = useAge ? 1 : 0;
                
                specialHoliday.grantSingle = null;
                specialHoliday.grantRegular.grantStartDate = new Date(specialHoliday.grantRegular.grantStartDate);
            }
            specialHoliday.grantMethod = self.inp_grantMethod();
            
            if (self.isEnableCode()) {
                var emptyObjectRegular: model.IGrantRegularDto = {};
                var emptyObjectPeriodic: model.IGrantPeriodic = {};
                var emptyObjectSphdLimit: model.ISphdLimitDto = {};
                var emptyObjectSubCondition: model.ISubConditionDto = {};
                var emptyObjectGrantSingle: model.IGrantSingleDto = {};

                specialHoliday["grantMethod"] = self.inp_grantMethod();
                
                service.addSpecialHoliday(specialHoliday).done(function(errors) {
                    if (errors && errors.length > 0) {
                        self.addListError(errors);    
                    } else {                    
                        if (self.currentCode) {
                            nts.uk.ui.dialog.info(nts.uk.resource.getMessage("Msg_15"));
                            self.getAllSpecialHoliday().done(function() {
                                self.currentCode(self.currentItem().specialHolidayCode());
                                self.isEnableCode(false);
                            });
                        }
                    }
                }).fail(function(res) {
                    nts.uk.ui.dialog.alertError(res.message);
                }).always(function() {
                    nts.uk.ui.block.clear();
                })
            }
            else {
                service.updateSpecialHoliday(specialHoliday).done(function(res) {
                    nts.uk.ui.dialog.info(nts.uk.resource.getMessage("Msg_15"));
                    self.getAllSpecialHoliday().done(function() {
                        self.currentCode(self.currentItem().specialHolidayCode());
                    });
                }).fail(function(res) {
                    nts.uk.ui.dialog.alertError(res.message);
                }).always(function() {
                    nts.uk.ui.block.clear();
                })
            }
        }

        getWorkTypeList() {
            var self = this;
            var dfd = $.Deferred();
            service.findWorkType().done(function(res) {
                _.forEach(res, function(item) {
                    self.workTypeList.push({
                        workTypeCode: item.workTypeCode,
                        name: item.name,
                        memo: item.memo
                    });
                });
                dfd.resolve();
            }).fail(function(error) {
                alert(error.message);
                dfd.reject(error);
            });
            return dfd.promise();
        }

        deleteSpecialHoliday() {
            let self = this;
            var index_of_itemDelete = _.findIndex(self.items(), function(item) {
                return item.specialHolidayCode() == self.currentCode();
            });
            nts.uk.ui.dialog.confirm("データを削除します。\r\nよろしいですか？").ifYes(function() {
                var specialholiday = {
                    specialHolidayCode: self.currentItem().specialHolidayCode()
                };
                service.deleteSpecialHoliday(specialholiday).done(function() {
                    $.when(self.getAllSpecialHoliday()).done(function() {
                        var holidayId: number = null;
                        if (self.items().length == 0) {
                            self.initSpecialHoliday();
                        } else if (self.items().length == 1) {
                            holidayId = self.items()[0].specialHolidayCode();
                        } else if (index_of_itemDelete == self.items().length) {
                            holidayId = self.items()[index_of_itemDelete - 1].specialHolidayCode();
                        } else {
                            holidayId = self.items()[index_of_itemDelete].specialHolidayCode();
                        }
                        self.currentCode(holidayId);
                        nts.uk.ui.dialog.info(nts.uk.resource.getMessage("Msg_16"));
                    });
                }).fail(function(res) {
                    nts.uk.ui.dialog.alertError(res.message);
                })
            }).ifNo(function() {
            });
        }

        initSpecialHoliday(): void {
            let self = this;
            var emptyObject: model.ISpecialHolidayDto = {};
            self.currentItem(new model.SpecialHolidayDto(emptyObject))
            self.currentCode("");
            self.inp_grantMethod(0);
            self.visibleGrantSingle(false);
            self.visibleGrant(true);
            self.workTypeNames("");
            nts.uk.ui.errors.clearAll();
            self.isEnableCode(true);
            $("#code-text").focus();

        }

        changedCode(value) {
            var self = this;
            self.currentItem(self.findSpecialHoliday(value));
            if (self.currentItem() != null) {
                var names = self.getNames(self.workTypeList(), self.currentItem().workTypeList());
                self.workTypeNames(names);
                self.inp_grantMethod(self.currentItem().grantMethod());
            }
        }

        findSpecialHoliday(value: number): any {
            let self = this;
            var result = _.find(self.items(), function(obj: model.SpecialHolidayDto) {
                return obj.specialHolidayCode() == value;
            });
            if (result) {
                self.inp_grantMethod(result.grantMethod());
                return result;
            }
            return new model.SpecialHolidayDto({});
        }

        openKDL002Dialog() {
            let self = this;
            nts.uk.ui.block.invisible();
            var workTypeCodes = _.map(self.workTypeList(), function(item: IWorkTypeModal) { return item.workTypeCode });
            nts.uk.ui.windows.setShared('KDL002_Multiple', true);
            nts.uk.ui.windows.setShared('KDL002_AllItemObj', workTypeCodes);
            nts.uk.ui.windows.setShared('KDL002_SelectedItemId', self.currentItem().workTypeList());

            nts.uk.ui.windows.sub.modal('/view/kdl/002/a/index.xhtml', { title: '' }).onClosed(function(): any {
                nts.uk.ui.block.clear();
                var data = nts.uk.ui.windows.getShared('KDL002_SelectedNewItem');
                var name = [];
                _.forEach(data, function(item: IWorkTypeModal) {
                    name.push(item.name);
                });
                self.workTypeNames(name.join(" + "));

                var workTypeCodes = _.map(data, function(item: any) { return item.code; });
                self.currentItem().workTypeList(workTypeCodes);
            });

        }

        openBDialog() {
            let self = this;
            nts.uk.ui.windows.setShared('KMF004B_SPHD_CD', self.currentItem().specialHolidayCode());
            nts.uk.ui.windows.sub.modal('/view/kmf/004/b/index.xhtml').onClosed(function(): any {
            });

        }

        openDDialog() {
            let self = this;
            nts.uk.ui.windows.setShared('KMF004D_SPHD_CD', self.currentItem().specialHolidayCode());
            nts.uk.ui.windows.sub.modal('/view/kmf/004/d/index.xhtml').onClosed(function(): any {
            });

        }

        openGDialog() {
            let self = this;
            nts.uk.ui.windows.setShared('KMF004G_SPHD_CD', self.currentItem().specialHolidayCode());
            nts.uk.ui.windows.sub.modal('/view/kmf/004/g/index.xhtml').onClosed(function(): any {
            });

        }

        openHDialog() {
            let self = this;
            nts.uk.ui.windows.sub.modal('/view/kmf/004/h/index.xhtml').onClosed(function(): any {
            });

        }
        
        /**
         * CDL002：雇用選択ダイアログ
         */
        openCDL002(): void {
            let self = this;
            nts.uk.ui.windows.setShared('selectedCodes', []);
            nts.uk.ui.windows.setShared('isMultipleSelection', true);
            nts.uk.ui.windows.setShared('isDisplayUnselect', false);
            nts.uk.ui.windows.sub.modal("com", "/view/cdl/002/a/index.xhtml").onClosed(() => {
                var selected = nts.uk.ui.windows.getShared('selectedCodes');
                self.currentItem().subCondition().clsCodes(selected);
            });
        }
        
        /**
         * CDL003：分類選択ダイアログ
         */
        openCDL003(): void {
            let self = this;
            nts.uk.ui.windows.setShared('selectedCodes', []);
            nts.uk.ui.windows.setShared('isMultipleSelection', true);
            nts.uk.ui.windows.setShared('isDisplayUnselect', false);
            nts.uk.ui.windows.sub.modal("com", "/view/cdl/003/a/index.xhtml").onClosed(() => {
                var selected = nts.uk.ui.windows.getShared('selectedCodes');
                self.currentItem().subCondition().empCodes(selected);
            });
        }

        getNames(data: Array<IWorkTypeModal>, workTypeCodesSelected: Array<string>) {
            var name = [];
            if (workTypeCodesSelected && workTypeCodesSelected.length > 0) {
                _.forEach(data, function(item: IWorkTypeModal) {
                    if (_.includes(workTypeCodesSelected, item.workTypeCode)) {
                        name.push(item.name);
                    }
                });
            }
            return name.join(" + ");
        }

        /**
         * Set error
         */
        addListError(errorsRequest: Array<string>) {
            var messages = {};
            _.forEach(errorsRequest, function(err) {
                messages[err] = nts.uk.resource.getMessage(err);
            });

            var errorVm = {
                messageId: errorsRequest,
                messages: messages
            };

            nts.uk.ui.dialog.bundledErrors(errorVm);
        }
    }

    class ItemModelSpecialHoliday {
        specialHolidayCode: string;
        specialHolidayName: string;
        constructor(specialHolidayCode: string, specialHolidayName: string) {
            this.specialHolidayCode = specialHolidayCode;
            this.specialHolidayName = specialHolidayName;
        }
    }

    class ItemModel {
        code: string;
        name: string;
        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
        }
    }

    export class WorkTypeModal {
        workTypeCode: string;
        name: string;
        memo: string;
        constructor(param: IWorkTypeModal) {
            this.workTypeCode = param.workTypeCode;
            this.name = param.name;
            this.memo = param.memo;
        }
    }

    export interface IWorkTypeModal {
        workTypeCode: string;
        name: string;
        memo: string;
    }

    export module model {
        export interface ISpecialHolidayDto {
            specialHolidayCode?: string;
            specialHolidayName?: string;
            grantMethod?: number;
            memo?: string;
            workTypeList?: Array<string>;
            grantRegular?: IGrantRegularDto;
            grantPeriodic?: IGrantPeriodic;
            sphdLimit?: ISphdLimitDto;
            subCondition?: ISubConditionDto;
            grantSingle?: IGrantSingleDto;
        }
        export class SpecialHolidayDto {
            specialHolidayCode: KnockoutObservable<any>;
            specialHolidayName: KnockoutObservable<string>;
            grantMethod: KnockoutObservable<number>;
            memo: KnockoutObservable<string>;
            workTypeList: KnockoutObservableArray<any>;
            grantRegular: KnockoutObservable<GrantRegularDto>;
            grantPeriodic: KnockoutObservable<GrantPeriodicDto>;
            sphdLimit: KnockoutObservable<SphdLimitDto>;
            subCondition: KnockoutObservable<SubConditionDto>;
            grantSingle: KnockoutObservable<GrantSingleDto>;

            constructor(param: ISpecialHolidayDto) {
                this.specialHolidayCode = ko.observable(param.specialHolidayCode || '');
                this.specialHolidayName = ko.observable(param.specialHolidayName || '');
                this.grantMethod = ko.observable(param.grantMethod || 0);
                this.memo = ko.observable(param.memo || '');
                this.workTypeList = ko.observableArray(param.workTypeList || null);
                this.grantRegular = ko.observable(param.grantRegular ? new GrantRegularDto(param.grantRegular) : new GrantRegularDto({}));
                this.grantPeriodic = ko.observable(param.grantPeriodic? new GrantPeriodicDto(param.grantPeriodic) : new GrantPeriodicDto({}));
                this.sphdLimit = ko.observable(param.sphdLimit ? new SphdLimitDto(param.sphdLimit) : new SphdLimitDto({}));
                this.subCondition = ko.observable(param.subCondition ? new SubConditionDto(param.subCondition) : new SubConditionDto({}));
                this.grantSingle = ko.observable(param.grantSingle ? new GrantSingleDto(param.grantSingle) : new GrantSingleDto({}));
            }
        }

        export interface IGrantRegularDto {
            specialHolidayCode?: string;
            grantStartDate?: string;
            months?: number;
            years?: number;
            grantRegularMethod?: number;
        }
        export class GrantRegularDto {
            specialHolidayCode: KnockoutObservable<any>;
            grantStartDate: KnockoutObservable<string>;
            months: KnockoutObservable<number>;
            years: KnockoutObservable<number>;
            grantRegularMethod: KnockoutObservable<number>;
            constructor(param: IGrantRegularDto) {
                this.specialHolidayCode = ko.observable(param.specialHolidayCode || '');
                this.grantStartDate = ko.observable(param.grantStartDate || (new Date).toString()); //TODO PENDING KIBAN FIX
                this.months = ko.observable(param.months || null);
                this.years = ko.observable(param.years || null);
                this.grantRegularMethod = ko.observable(param.grantRegularMethod || 0);
            }
        }

        export interface IGrantPeriodic {
            specialHolidayCode?: string;
            grantDay?: number;
            splitAcquisition?: number;
            grantPeriodicMethod?: number;
        }
        export class GrantPeriodicDto {
            specialHolidayCode: KnockoutObservable<any>;
            grantDay: KnockoutObservable<number>;
            splitAcquisition: KnockoutObservable<number>;
            grantPeriodicMethod: KnockoutObservable<number>;
            constructor(param: IGrantPeriodic) {
                this.specialHolidayCode = ko.observable(param.specialHolidayCode || '');
                this.grantDay = ko.observable(param.grantDay || null);
                this.splitAcquisition = ko.observable(param.splitAcquisition || 0);
                this.grantPeriodicMethod = ko.observable(param.grantPeriodicMethod || 0);
            }
        }

        export interface ISphdLimitDto {
            specialHolidayCode?: string;
            specialVacationMonths?: number;
            specialVacationYears?: number;
            grantCarryForward?: number;
            limitCarryoverDays?: number;
            specialVacationMethod?: number;
        }
        export class SphdLimitDto {
            specialHolidayCode: KnockoutObservable<any>;
            specialVacationMonths: KnockoutObservable<number>;
            specialVacationYears: KnockoutObservable<number>;
            grantCarryForward: KnockoutObservable<number>;
            limitCarryoverDays: KnockoutObservable<number>;
            specialVacationMethod: KnockoutObservable<number>;
            constructor(param: ISphdLimitDto) {
                this.specialHolidayCode = ko.observable(param.specialHolidayCode || '');
                this.specialVacationMonths = ko.observable(param.specialVacationMonths || null);
                this.specialVacationYears = ko.observable(param.specialVacationYears || null);
                this.grantCarryForward = ko.observable(param.grantCarryForward || 0);
                this.limitCarryoverDays = ko.observable(param.limitCarryoverDays || null);
                this.specialVacationMethod = ko.observable(param.specialVacationMethod || 0);
            }
        }

        export interface ISubConditionDto {
            specialHolidayCode?: string;
            useGender?: boolean;
            useEmployee?: boolean;
            useCls?: boolean;
            useAge?: boolean;
            genderAtr?: number;
            limitAgeFrom?: number;
            limitAgeTo?: number;
            ageCriteriaAtr?: number;
            ageBaseYearAtr?: number;
            ageBaseDates?: number;
            clsCodes?: Array<string>;
            empCodes?: Array<string>;
        }
        
        export class SubConditionDto {
            specialHolidayCode: KnockoutObservable<string>;
            useGender: KnockoutObservable<boolean>;
            useEmployee: KnockoutObservable<boolean>;
            useCls: KnockoutObservable<boolean>;
            useAge: KnockoutObservable<boolean>;
            genderAtr: KnockoutObservable<number>;
            limitAgeFrom: KnockoutObservable<number>;
            limitAgeTo: KnockoutObservable<number>;
            ageCriteriaAtr: KnockoutObservable<number>;
            ageBaseYearAtr: KnockoutObservable<number>;
            ageBaseDates: KnockoutObservable<number>;
            clsCodes: KnockoutObservableArray<string>;
            empCodes: KnockoutObservableArray<string>;
            
            constructor(param: ISubConditionDto) {
                this.specialHolidayCode = ko.observable(param.specialHolidayCode || '');
                this.useGender = ko.observable(param.useGender || false);
                this.useEmployee = ko.observable(param.useEmployee || false);
                this.useCls = ko.observable(param.useCls || false);
                this.useAge = ko.observable(param.useAge || false);
                this.genderAtr = ko.observable(param.genderAtr || 0);
                this.limitAgeFrom = ko.observable(param.limitAgeFrom || null);
                this.limitAgeTo = ko.observable(param.limitAgeTo || null);
                this.ageCriteriaAtr = ko.observable(param.ageCriteriaAtr || 0);
                this.ageBaseYearAtr = ko.observable(param.ageBaseYearAtr || 0);
                this.ageBaseDates = ko.observable(param.ageBaseDates || null);
                this.clsCodes = ko.observableArray(param.clsCodes || null);
                this.empCodes = ko.observableArray(param.empCodes || null);
            }
        }

        export interface IGrantSingleDto {
            specialHolidayCode?: string;
            grantDaySingleType?: number;
            fixNumberDays?: number;
            makeInvitation?: number;
            holidayExclusionAtr?: number;
        }
        export class GrantSingleDto {
            specialHolidayCode: KnockoutObservable<string>;
            grantDaySingleType: KnockoutObservable<number>;
            fixNumberDays: KnockoutObservable<number>;
            makeInvitation: KnockoutObservable<number>;
            holidayExclusionAtr: KnockoutObservable<number>;
            constructor(param: IGrantSingleDto) {
                this.specialHolidayCode = ko.observable(param.specialHolidayCode || '');
                this.grantDaySingleType = ko.observable(param.grantDaySingleType || 0);
                this.fixNumberDays = ko.observable(param.fixNumberDays || null);
                this.makeInvitation = ko.observable(param.makeInvitation || 0);
                this.holidayExclusionAtr = ko.observable(param.holidayExclusionAtr || 0);
            }
        }
    }
}

