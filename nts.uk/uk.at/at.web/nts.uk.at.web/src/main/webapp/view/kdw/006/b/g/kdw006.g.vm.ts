module nts.uk.at.view.kdw006.g.viewmodel {
    export class ScreenModelG {
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

        constructor() {
            let self = this;
            self.fullWorkTypeList = ko.observableArray([]);
            self.groups1 = ko.observableArray([]);
            self.groups2 = ko.observableArray([]);
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
                maxRows: 12
            };
            self.employmentList = ko.observableArray<UnitModel>([]);

            self.selectedCode.subscribe(function(newValue) {
                self.getWorkType();
            });
        }

        start(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            $('#empt-list-setting').ntsListComponent(self.listComponentOption).done(function() {
                self.employmentList($('#empt-list-setting').getDataList());
                if (self.employmentList().length > 0) {
                    self.selectedCode(self.employmentList()[0].code);
                }
                dfd.resolve();
            });
            self.getFullWorkTypeList().done(function() {
                self.getWorkType().done(function() {
                    dfd.resolve();
                });
            });
            return dfd.promise();
        }

        getFullWorkTypeList() {
            let self = this;
            let dfd = $.Deferred();
            service.findWorkType().done(function(res) {
                _.forEach(res, function(item) {
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

        getWorkType(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            let fullWorkTypeCodes = _.map(self.fullWorkTypeList(), function(item: any) { return item.workTypeCode; });
            self.groups1.removeAll();
            self.groups2.removeAll();
            service.getWorkTypes(self.selectedCode()).done(function(res) {
                _.forEach(res, function(item) {
                    let names = _(item.workTypeList).map(x => (_.find(ko.toJS(self.fullWorkTypeList), z => z.workTypeCode == x) || {}).name).value();
                    let comment = '';
                    if (item.no == 2) { 
                        comment = nts.uk.resource.getText("#KDW006_59",['法定内休日']);
                    }
                    if (item.no == 3) { 
                        comment = nts.uk.resource.getText("#KDW006_59",['法定外休日']);
                    }
                    if (item.no == 4) { 
                        comment = nts.uk.resource.getText("#KDW006_59",['法定外休日(祝)']);
                    }
                    let group = new WorkTypeGroup(item.no, item.name, item.workTypeList, names.join("、　"),
                        fullWorkTypeCodes, comment);
                    if (group.no < 5) {
                        self.groups1.push(group);
                    } else {
                        self.groups2.push(group);
                    }
                });
                dfd.resolve();
            }).fail(function(res) {
                nts.uk.ui.dialog.alertError(res.message);
            });
            return dfd.promise();
        }

        saveData() {
            let self = this;
            service.register(self.selectedCode(), self.groups1(), self.groups2()).done(function(res) {
                nts.uk.ui.dialog.info({ messageId: "Msg_15" });
            });
        }

    }

    export class WorkTypeGroup {
        no: number;
        name: KnockoutObservable<string>;
        workTypeCodes: string[];
        workTypeName: KnockoutObservable<string>;
        fullWorkTypeCodes: string[];
        comment: string;

        constructor(no: number, name: string, workTypeCodes: string[], workTypeName: string, fullWorkTypeCodes: string[], comment: string) {
            this.no = no;
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
            service.defaultValue(listWorkType).done(function(res) {
                let workTypeCodess = _.map(res, 'workTypeCode');
                self.workTypeCodes = workTypeCodess;
                let names = _(workTypeCodess).map(x => (_.find(ko.toJS(self.fullWorkTypeList), z => z.workTypeCode == x) || {}).name).value();
                self.workTypeName(names.join("、　"));
            });
        }

        openKDL002Dialog() {
            let self = this;
            nts.uk.ui.block.invisible();

            nts.uk.ui.windows.setShared('KDL002_Multiple', true);
            nts.uk.ui.windows.setShared('KDL002_AllItemObj', self.fullWorkTypeCodes);
            nts.uk.ui.windows.setShared('KDL002_SelectedItemId', self.workTypeCodes);

            nts.uk.ui.windows.sub.modal('/view/kdl/002/a/index.xhtml', { title: '' }).onClosed(function(): any {
                nts.uk.ui.block.clear();
                var data = nts.uk.ui.windows.getShared('KDL002_SelectedNewItem');
                var name = [];
                _.forEach(data, function(item: any) {
                    name.push(item.name);
                });
                self.workTypeName(name.join("、　"));
                self.workTypeCodes = _.map(data, function(item: any) { return item.code; });
            });
        }

        clear() {
            let self = this;
            self.workTypeCodes = [];
            self.workTypeName("");
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

}
