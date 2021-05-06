module nts.uk.at.view.kdw006.g.viewmodel {
    let __viewContext: any = window["__viewContext"] || {};

    export class ScreenModelG extends ko.ViewModel{
        // declare
        fullWorkTypeList: KnockoutObservableArray<any>;
        groups1: KnockoutObservableArray<any>;
        groups2: KnockoutObservableArray<any>;

        // template
        listComponentOption: any;
        selectedCode: KnockoutObservable<string>;
        isShowAlreadySet: KnockoutObservable<boolean>;
        alreadySettingList: KnockoutObservableArray<UnitAlreadySettingModel>;
        isShowNoSelectRow: KnockoutObservable<boolean>;
        isMultiSelect: KnockoutObservable<boolean>;
        employmentList: KnockoutObservableArray<UnitModel>;

        listSetting: KnockoutObservableArray<any>;

        mode: KnockoutObservable<MODE>;
        
        constructor() {
            super();

            let self = this;
            self.mode = ko.observable(MODE.NEW);
            self.fullWorkTypeList = ko.observableArray([]);
            self.groups1 = ko.observableArray([]);
            self.groups2 = ko.observableArray([]);
            self.listSetting = ko.observableArray([]);
            // template
            self.selectedCode = ko.observable('01');
            self.alreadySettingList = ko.observableArray([
                { code: '1', isAlreadySetting: true },
                { code: '2', isAlreadySetting: true }
            ]);
            self.listComponentOption = {
                isShowAlreadySet: false,
                isMultiSelect: false,
                listType: ListType.EMPLOYMENT,
                selectType: SelectType.SELECT_BY_SELECTED_CODE,
                selectedCode: self.selectedCode,
                isShowNoSelectRow: false,
                isDialog: false,
                alreadySettingList: self.alreadySettingList,
                maxRows: 12,
                tabindex: 4
            };
            self.employmentList = ko.observableArray<UnitModel>([]);

            self.selectedCode.subscribe(function(newValue) {
                if (nts.uk.text.isNullOrEmpty(newValue)) return; 
                self.getWorkType(newValue);
            });
        }

        start(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            $('#empt-list-setting').ntsListComponent(self.listComponentOption).done(function() {
                self.employmentList($('#empt-list-setting').getDataList());
                if (self.employmentList().length > 0) {
                    self.selectedCode(self.employmentList()[0].code);
                } else {
                    self.$dialog.alert({ messageId: "Msg_146" });
                }
            });
            self.getFullWorkTypeList().done(function() {
                self.getWorkType(self.selectedCode()).done(function() {
                    dfd.resolve();
                });
            });
            return dfd.promise();
        }
        
        jumpTo() {
            let self = this;
            nts.uk.request.jump("/view/kdw/006/a/index.xhtml");
        }


        getFullWorkTypeList(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            service.getAllWorkTypes().done(function(res) {
                let availabelList = _.filter(res, (item: any) => {
                    return item.abolishAtr == 0;
                });
                _.forEach(availabelList, (item: any) => {
                    self.fullWorkTypeList.push({
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

        getWorkType(newValue: any): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();

            let fullWorkTypeCodes = _.map(self.fullWorkTypeList(), function(item: any) { return item.workTypeCode; });
            service.getWorkTypes(newValue).done(function(res) {
                self.groups1.removeAll();
                self.groups2.removeAll();
                let data = [];
                if (_.isEmpty(res)) {
                    self.mode(MODE.NEW);
                    for (let i = 1; i <= 10; i++) {
                        data.push({no: i, name: '', workTypeList: []});
                    }
                } else {
                    self.mode(MODE.UPDATE);
                    data = res;    
                }
                _.forEach(data, function(item) {
                    let names = _(item.workTypeList.sort()).map((x: any) => (_.find(ko.toJS(self.fullWorkTypeList), (z: any) => z.workTypeCode == x) || {}).name).value();
                    let comment = '';
                    if (item.no == 1) {
                        item.name = self.$i18n('KDW006_76');
                    }
                    if (item.no == 2) {
                        item.name = self.$i18n('KDW006_72');
                        comment = self.$i18n("KDW006_59", ['法定内休日']);
                    }
                    if (item.no == 3) {
                        item.name = self.$i18n('KDW006_73');
                        comment = self.$i18n("KDW006_59", ['法定外休日']);
                    }
                    if (item.no == 4) {
                        item.name = self.$i18n('KDW006_74');
                        comment = self.$i18n("KDW006_59", ['法定外休日(祝)']);
                    }
                    let nameEnd = _.filter(names, undefined);
                    let group = new WorkTypeGroup(item.no, self.$i18n('KDW006_' + (252 + item.no)), item.name === " " ? '' : item.name, 
                        item.workTypeList, nameEnd.join("、"), fullWorkTypeCodes, comment);
                    if (group.no < 5) {
                        self.groups1.push(group);
                    } else {
                        self.groups2.push(group);
                    }
                });

                dfd.resolve();
            }).fail(function(res) {
                self.$dialog.alert(res.message);
            });
            return dfd.promise();
        }

        saveData() {
            let self = this;
            $('.name-group').trigger('validate');
            
            setTimeout(() => {
                if (nts.uk.ui.errors.hasError()) {
                    return;
                }
                //            if(self.validate()){
                //               return; 
                //            }
                self.$blockui("show");
                service.register(self.selectedCode(), self.groups1(), self.groups2()).done(function(res) {
                    if (self.groups1.length > 0 || self.groups2.length > 0) {
                        self.listSetting.push(self.selectedCode());
                    } else {
                        self.listSetting.remove(self.selectedCode());
                    }
                    self.$dialog.info({ messageId: "Msg_15" }).then(function() {
                        //self.selectedCode.valueHasMutated();
                        self.$blockui("hide");
                    });
                }).fail(() => {
                    self.$blockui("hide");
                }).always(() => {
                    self.$blockui("hide");
                });
            }, 100);
        }
        
        private validate(): boolean {
                let self = this;
                $("#test").ntsEditor('validate');
                return $('.nts-input').ntsError('hasError');
            }

        copyData() {
            let self = this;
            self.$blockui("show");
            let employmentName: UnitModel;
            employmentName = _.find(self.employmentList(), function(m) {
                return m.code === self.selectedCode();
            });
            let listCode = _.map(self.employmentList(), 'code');

            service.checkSetting(listCode).done(function(res) {
                self.listSetting(res);
                let param = {
                    code: self.selectedCode(),
                    name: employmentName.name,
                    targetType: 1,// 雇用
                    itemListSetting: self.listSetting(),
                };
                nts.uk.ui.windows.setShared("CDL023Input", param);
                nts.uk.ui.windows.sub.modal("com", "/view/cdl/023/a/index.xhtml").onClosed(() => {
                    self.$blockui("show");
                    let data = nts.uk.ui.windows.getShared("CDL023Output");
                    if (!nts.uk.util.isNullOrUndefined(data)) {
                        let command = {
                            targetEmploymentCodes: data,
                            employmentCode: self.selectedCode()
                        };
                        service.copy(command).done(() => {
                            self.$dialog.info({ messageId: "Msg_15" }).then(function() {
                                self.start();
                                self.$blockui("hide");
                            });
                        }).fail(function(res: any) {
                            self.$dialog.alert({ messageId: res.messageId, messageParams: res.parameterIds }).then(function() {
                                self.$blockui("hide");
                            });
                        }).always(() => {
                            self.$blockui("hide");
                        });
                    }
                    self.$blockui("hide");
                });
            });
        }
    }

    export class WorkTypeGroup extends ko.ViewModel {
        no: number;
        noText: string;
        name: KnockoutObservable<string>;
        workTypeCodes: string[];
        workTypeName: KnockoutObservable<string>;
        fullWorkTypeCodes: string[];
        comment: string;

        constructor(no: number, noText: string, name: string, workTypeCodes: string[], workTypeName: string, fullWorkTypeCodes: string[], comment: string) {
            super();

            this.no = no;
            this.noText = noText;
            this.name = ko.observable(name);
            this.workTypeCodes = workTypeCodes;
            this.workTypeName = ko.observable(workTypeName);
            this.fullWorkTypeCodes = fullWorkTypeCodes;
            this.comment = comment;
        }

        defaultValue() {
            let self = this;
            let listWorkType: number[];
            if (self.no == 1) {
                listWorkType = [WorkTypeClass.Attendance, WorkTypeClass.AnnualHoliday, WorkTypeClass.YearlyReserved,
                    WorkTypeClass.SpecialHoliday, WorkTypeClass.Absence, WorkTypeClass.SubstituteHoliday,
                    WorkTypeClass.Pause, WorkTypeClass.ContinuousWork, WorkTypeClass.Closure, WorkTypeClass.TimeDigestVacation];
            } else {
                listWorkType = [WorkTypeClass.Holiday, WorkTypeClass.HolidayWork, WorkTypeClass.Shooting];
            }
            let viewG = __viewContext.viewModel;
            service.defaultValue(listWorkType).done(function(res) {
                let workTypeCodess: string[] = _.map(res, 'workTypeCode');
                self.workTypeCodes = _.uniq(workTypeCodess);
                let fullCodeNameList = ko.toJS(viewG.fullWorkTypeList);
                let names: any[] = [];
                _.forEach(self.workTypeCodes, (code) => {
                    let foundWT = _.find(fullCodeNameList, (codeName: any) => {
                        return codeName.workTypeCode === code;
                    });
                    if (foundWT) {
                        names.push(foundWT.name);
                    }
                });

                self.workTypeName(names.join("、"));
            });
        }

        openKDL002Dialog() {
            let self = this;
            self.$blockui("show");

            nts.uk.ui.windows.setShared('KDL002_Multiple', true);
            nts.uk.ui.windows.setShared('KDL002_AllItemObj', self.fullWorkTypeCodes);
            nts.uk.ui.windows.setShared('KDL002_SelectedItemId', self.workTypeCodes);

            nts.uk.ui.windows.sub.modal('/view/kdl/002/a/index.xhtml', { title: '' }).onClosed(function(): any {
                self.$blockui("hide");
                var data = nts.uk.ui.windows.getShared('KDL002_SelectedNewItem');
                var isCancel = nts.uk.ui.windows.getShared('KDL002_IsCancel');
                if(isCancel != null && isCancel == true)
                return;
                var name: any[] = [];
                _.forEach(data, function(item: any) {
                    name.push(item.name);
                });
                self.workTypeName(name.join("、"));
                self.workTypeCodes = _.map(data, function(item: any) { return item.code; });
            });
        }

        clear() {
            let self = this;
            self.workTypeCodes = [];
            self.workTypeName("");
        }
        close(): void {
            nts.uk.ui.windows.close();
        }
    }

    export interface IWorkTypeModal {
        workTypeCode: string;
        name: string;
        memo: string;
    }

    export class ListType {
        static EMPLOYMENT = 1;
        static Classification = 2;
        static JOB_TITLE = 3;
        static EMPLOYEE = 4;
    }

    export class WorkTypeClass {
        static Attendance = 0;
        static Holiday = 1;
        static AnnualHoliday = 2;
        static YearlyReserved = 3;
        static SpecialHoliday = 4;
        static Absence = 5;
        static SubstituteHoliday = 6;
        static Shooting = 7;
        static Pause = 8;
        static TimeDigestVacation = 9;
        static ContinuousWork = 10;
        static HolidayWork = 11;
        static LeaveOfAbsence = 12;
        static Closure = 13;
    }

    export interface UnitModel {
        code: string;
        name?: string;
        workplaceName?: string;
        isAlreadySetting?: boolean;
    }

    export class SelectType {
        static SELECT_BY_SELECTED_CODE = 1;
        static SELECT_ALL = 2;
        static SELECT_FIRST_ITEM = 3;
        static NO_SELECT = 4;
    }

    export interface UnitAlreadySettingModel {
        code: string;
        isAlreadySetting: boolean;
    }

    export enum MODE {
        NEW,
        UPDATE
    }
}
