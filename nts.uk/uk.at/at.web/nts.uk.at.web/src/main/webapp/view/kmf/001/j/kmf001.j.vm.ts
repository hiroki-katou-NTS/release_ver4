module nts.uk.pr.view.kmf001.j {
    export module viewmodel {
        import Enum = service.model.Enum;
        import SixtyHourVacationSettingDto = service.model.SixtyHourVacationSettingDto;
        import Emp60HourVacationDto = service.model.Emp60HourVacationDto;
        import EmploymentSettingDto = service.model.EmploymentSettingDto;
        import EmploymentSettingFindDto = service.model.EmploymentSettingFindDto;

        export class ScreenModel {
            //list component option
            selectedItem: KnockoutObservable<string>;
            listComponentOption: KnockoutObservable<any>;
            alreadySettingList: KnockoutObservableArray<any>;
            // Service.
            service: service.Service;

            // Switch button data source
            timeDigestiveUnitEnums: KnockoutObservableArray<Enum>;
            sixtyHourExtraEnums: KnockoutObservableArray<Enum>;
            manageDistinctEnums: KnockoutObservableArray<Enum>;

            // Temp
            employmentList: KnockoutObservableArray<ItemModel>;
            selectedContractTypeCode: KnockoutObservable<string>;

            // Model
            settingModel: KnockoutObservable<SixtyHourVacationSettingModel>;
            empSettingModel: KnockoutObservable<Emp60HVacationModel>;

            // 
            isComManaged: KnockoutObservable<boolean>;
            hasEmp: KnockoutObservable<boolean>;
            isEmpManaged: KnockoutObservable<boolean>;
            saveDisable : KnockoutObservable<boolean>;

            // Dirty checker
            dirtyChecker: nts.uk.ui.DirtyChecker;

            constructor() {
                var self = this;
                self.service = service.instance;
                self.selectedItem = ko.observable(null);
                self.alreadySettingList = ko.observableArray([]);

                self.timeDigestiveUnitEnums = ko.observableArray([]);
                self.sixtyHourExtraEnums = ko.observableArray([]);
                self.manageDistinctEnums = ko.observableArray([]);

                self.settingModel = ko.observable(null);
                self.empSettingModel = ko.observable(null);
                self.settingModel = ko.observable(
                    new SixtyHourVacationSettingModel({
                        isManage: 0,
                        digestiveUnit: 0,
                        sixtyHourExtra: 0
                    }));
                self.empSettingModel = ko.observable(
                    new Emp60HVacationModel({
                        contractTypeCode: "",
                        isManage: 0,
                        digestiveUnit: 0,
                        sixtyHourExtra: 0
                    }));

                self.isComManaged = ko.computed(function() {
                    return self.settingModel().isManage() == 1;
                });

                self.hasEmp = ko.observable(true);
                self.saveDisable = ko.observable(true);
                self.isEmpManaged = ko.computed(function() {
                    return self.hasEmp() && self.empSettingModel().isManage() == 1;
                });

                self.selectedContractTypeCode = ko.observable('');
                self.selectedItem.subscribe(function(data: string) {
                    if (data) {
                        self.empSettingModel().contractTypeCode(data);
                        self.loadEmpSettingDetails(data);
                        self.hasEmp(true);
                        self.saveDisable(true);
                    } else {
                        self.hasEmp(false);
                        self.saveDisable(false);
                    }
                });
                //list Emp
                self.listComponentOption = {
                    isShowAlreadySet: true,
                    isMultiSelect: false,
                    listType: ListType.EMPLOYMENT,
                    selectType: SelectType.SELECT_FIRST_ITEM,
                    selectedCode: this.selectedItem,
                    isDialog: false,
                    alreadySettingList: self.alreadySettingList
                };
            }

            /**
             * Start page.
             */
            private startPage(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred();
                self.employmentList = ko.observableArray<ItemModel>([]);
                for (let i = 1; i < 9; i++) {
                    self.employmentList.push(new ItemModel('0' + i, '基本給', i % 3 === 0));
                }
                // Load enums
                $.when(self.loadTimeDigestiveUnitEnums(), self.loadSixtyHourExtraEnums(), self.loadManageDistinctEnums(), self.loadEmploymentList()).done(function() {
                    self.loadComSettingDetails();
                    self.selectedItem(self.employmentList()[0].code);
                    dfd.resolve();
                });
                return dfd.promise();
            }

            private checkComManaged(): void {
                let self = this;
                let dfd = $.Deferred<any>();
                $('#left-content').ntsListComponent(this.listComponentOption).done(function() {
                    if (!$('#left-content').getDataList() || $('#left-content').getDataList().length == 0) {
                        nts.uk.ui.dialog.info({ messageId: "Msg_146", messageParams: [] });
                        self.hasEmp(false);
                        dfd.resolve();
                    }
                });
            }
            private loadEmploymentList(): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred<any>();
                //get list employment
                self.alreadySettingList([]);
                self.service.findAllByEmployment().done((data: Array<string>) => {
                    for (let empContractTypeCode of data) {
                        self.alreadySettingList.push({ code: empContractTypeCode, isAlreadySetting: true });
                    }
                    dfd.resolve();
                });
                return dfd.promise();
            }

            private loadTimeDigestiveUnitEnums(): JQueryPromise<Array<Enum>> {
                let self = this;
                let dfd = $.Deferred();
                this.service.getTimeDigestiveUnitEnum().done(function(res: Array<Enum>) {
                    self.timeDigestiveUnitEnums(res);
                    dfd.resolve();
                }).fail(function(res) {
                    nts.uk.ui.dialog.alertError(res.message);
                });
                return dfd.promise();
            }

            private loadSixtyHourExtraEnums(): JQueryPromise<Array<Enum>> {
                let self = this;
                let dfd = $.Deferred();
                this.service.getSixtyHourExtraEnum().done(function(res: Array<Enum>) {
                    self.sixtyHourExtraEnums(res);
                    dfd.resolve();
                }).fail(function(res) {
                    nts.uk.ui.dialog.alertError(res.message);
                });
                return dfd.promise();
            }

            private loadManageDistinctEnums(): JQueryPromise<Array<Enum>> {
                let self = this;
                let dfd = $.Deferred();
                this.service.getManageDistinctEnum().done(function(res: Array<Enum>) {
                    self.manageDistinctEnums(res);
                    dfd.resolve();
                }).fail(function(res) {
                    nts.uk.ui.dialog.alertError(res.message);
                });
                return dfd.promise();
            }

            private loadComSettingDetails(): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred();
                self.clearErrorComSetting();
                this.service.findComSetting().done(function(res: SixtyHourVacationSettingDto) {
                    if (res) {
                        self.settingModel().isManage(res.isManage);
                        self.settingModel().digestiveUnit(res.digestiveUnit);
                        self.settingModel().sixtyHourExtra(res.sixtyHourExtra);
                    } else {
                        self.settingModel().isManage(self.manageDistinctEnums()[0].value);
                        self.settingModel().digestiveUnit(self.timeDigestiveUnitEnums()[0].value);
                        self.settingModel().sixtyHourExtra(self.sixtyHourExtraEnums()[0].value);
                    }
                    dfd.resolve();
                }).fail(function(res) {
                    nts.uk.ui.dialog.alertError(res.message);
                });
                return dfd.promise();
            }

            private loadEmpSettingDetails(contractTypeCode: string): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred();
                self.clearErrorEmpSetting();
                this.service.findEmpSetting(contractTypeCode).done(function(res: Emp60HourVacationDto) {
                    if (res) {
//                        self.empSettingModel().contractTypeCode(res.contractTypeCode);
                        self.empSettingModel().isManage(res.isManage);
                        self.empSettingModel().digestiveUnit(res.digestiveUnit);
                        self.empSettingModel().sixtyHourExtra(res.sixtyHourExtra);
                        //re select item
                        self.selectedItem(contractTypeCode);
                    } else {
                        self.empSettingModel().isManage(self.manageDistinctEnums()[0].value);
                        self.empSettingModel().digestiveUnit(self.timeDigestiveUnitEnums()[0].value);
                        self.empSettingModel().sixtyHourExtra(self.sixtyHourExtraEnums()[0].value);
                    }
                    dfd.resolve();
                }).fail(function(res) {
                    nts.uk.ui.dialog.alertError(res.message);
                });
                return dfd.promise();
            }
            public back(): void {
                nts.uk.request.jump("/view/kmf/001/a/index.xhtml", {});
            }

            public saveComSetting(): void {
                let self = this;
                let dfd = $.Deferred();
                if (!self.validateComSetting()) {
                    return;
                }
                this.service.saveComSetting(self.settingModel().toSixtyHourVacationSettingDto()).done(function() {
                    // Msg_15
                    nts.uk.ui.dialog.alert(nts.uk.ui.dialog.info({ messageId: "Msg_15" }));
                    dfd.resolve();
                }).fail(function(res) {
                    nts.uk.ui.dialog.alertError(res.message);
                });
            }
            public saveEmpSetting(): void {
                let self = this;
                if (!self.validateEmpSetting()) {
                    return;
                }
                var backUp = self.selectedItem();
                this.service.saveEmpSetting(self.empSettingModel().toEmp60HourVacationDto()).done(function() {
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                    self.loadEmploymentList().done(() => {
                        //reload list employment
                        $('#left-content').ntsListComponent(self.listComponentOption).done(() => {
                            self.loadEmpSettingDetails(backUp);
                        });
                    });
                }
            }

            private validateComSetting(): boolean {
                let self = this;
                return true;
            }

            private clearErrorComSetting(): void {
                if (!$('.nts-input').ntsError('hasError')) {
                    return;
                }
                $('input').ntsError('clear');
            }

            private validateEmpSetting(): boolean {
                let self = this;
                return true;
            }
            private clearErrorEmpSetting(): void {
                if (!$('.nts-input').ntsError('hasError')) {
                    return;
                }

                $('input').ntsError('clear');
            }


        // Model class
        export class SixtyHourVacationSettingModel {
            isManage: KnockoutObservable<number>;
            digestiveUnit: KnockoutObservable<number>;
            sixtyHourExtra: KnockoutObservable<number>;

            constructor(dto: SixtyHourVacationSettingDto) {
                this.isManage = ko.observable(dto.isManage);
                this.digestiveUnit = ko.observable(dto.digestiveUnit);
                this.sixtyHourExtra = ko.observable(dto.sixtyHourExtra);
            }

            public toSixtyHourVacationSettingDto(): SixtyHourVacationSettingDto {
                return new SixtyHourVacationSettingDto(this.isManage(), this.digestiveUnit(), this.sixtyHourExtra());
            }
        }

        export class Emp60HVacationModel extends SixtyHourVacationSettingModel {
            contractTypeCode: KnockoutObservable<string>;

            constructor(dto: Emp60HourVacationDto) {
                super(new SixtyHourVacationSettingDto(dto.isManage, dto.digestiveUnit, dto.sixtyHourExtra));
                this.contractTypeCode = ko.observable(dto.contractTypeCode);
            }

            public toEmp60HourVacationDto(): Emp60HourVacationDto {
                return new Emp60HourVacationDto(this.contractTypeCode(),
                    new SixtyHourVacationSettingDto(this.isManage(), this.digestiveUnit(), this.sixtyHourExtra()));
            }
        }
        export class SelectType {
            static SELECT_BY_SELECTED_CODE = 1;
            static SELECT_ALL = 2;
            static SELECT_FIRST_ITEM = 3;
            static NO_SELECT = 4;
        }
        export class ListType {
            static EMPLOYMENT = 1;
            static Classification = 2;
            static JOB_TITLE = 3;
            static EMPLOYEE = 4;
        }
        export interface UnitModel {
            code: string;
            name?: string;
            workplaceName?: string;
            isAlreadySetting?: boolean;
        }
        // Temp
        export class ItemModel {
            code: string;
            name: string;
            alreadySet: boolean;

            constructor(code: string, name: string, alreadySet: boolean) {
                this.code = code;
                this.name = name;
                this.alreadySet = alreadySet;
            }
        }
    }
}