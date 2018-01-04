module nts.uk.at.view.ksu001.d.viewmodel {
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    export class ScreenModel {
        itemList: KnockoutObservableArray<any> = ko.observableArray([
            new BoxModel(1, nts.uk.resource.getText("KSU001_79")),
            new BoxModel(2, nts.uk.resource.getText("KSU001_80"))
        ]);
        selectedId: KnockoutObservable<number> = ko.observable(1);
        text = nts.uk.resource.getText("KSU001_82");
        listEmployee = getShared("dataForScreenD").empItems;
        //KCP005
        listComponentOption: any;
        selectedCode: KnockoutObservable<string> = ko.observable('');
        multiSelectedCode: KnockoutObservableArray<string> = ko.observableArray([]);
        isShowAlreadySet: KnockoutObservable<boolean> = ko.observable(false);
        alreadySettingList: KnockoutObservableArray<UnitAlreadySettingModel> = ko.observableArray([]);
        isDialog: KnockoutObservable<boolean> = ko.observable(false);
        isShowNoSelectRow: KnockoutObservable<boolean> = ko.observable(false);
        isMultiSelect: KnockoutObservable<boolean> = ko.observable(true);
        isShowWorkPlaceName: KnockoutObservable<boolean> = ko.observable(false);
        isShowSelectAllButton: KnockoutObservable<boolean> = ko.observable(false);
        employeeList: KnockoutObservableArray<UnitModel> = ko.observableArray([]);

        //ExCalendar
        startDate: KnockoutObservable<Date> = ko.observable(getShared("dataForScreenD").startDate);
        endDate: KnockoutObservable<Date> = ko.observable(getShared("dataForScreenD").endDate);
        permissionHandCorrection: KnockoutObservable<boolean> = ko.observable(getShared("dataForScreenD").permissionHandCorrection);
        listColorOfHeader: any = ko.observableArray(getShared("dataForScreenD").listColorOfHeader);

        selectedIds: KnockoutObservableArray<any> = ko.observableArray([]);

        constructor() {
            let self = this;

            _.each(self.listEmployee, (x) => {
                self.employeeList.push({ code: x.empCd, name: x.empName });
            });

            self.listComponentOption = {
                isShowAlreadySet: self.isShowAlreadySet(),
                isMultiSelect: self.isMultiSelect(),
                listType: ListType.EMPLOYEE,
                employeeInputList: self.employeeList,
                selectType: SelectType.NO_SELECT,
                selectedCode: self.multiSelectedCode,
                isDialog: self.isDialog(),
                isShowNoSelectRow: self.isShowNoSelectRow(),
                alreadySettingList: self.alreadySettingList,
                isShowWorkPlaceName: self.isShowWorkPlaceName(),
                isShowSelectAllButton: self.isShowSelectAllButton(),
                maxRows: 15
            };

            $('#component-items-list').ntsListComponent(self.listComponentOption).done(function() {
                $('#component-items-list').focusComponent();
            });
        }
        /**
         * decision
         */
        decision(): void {
            let self = this;

            if (!self.multiSelectedCode() || self.multiSelectedCode().length == 0) {
                nts.uk.ui.dialog.alertError(nts.uk.resource.getMessage('Msg_499'));
                return;
            }
            if (self.selectedIds().length === 0) {
                nts.uk.ui.dialog.alertError(nts.uk.resource.getMessage('Msg_500'));
                return;
            }
            nts.uk.ui.block.grayout();
            let dates = _.map(self.selectedIds(), (date) => {
                return moment(date.replace(/-/g, ''), "YYYYMMDD")
            });
            let employees = _.filter(self.listEmployee, (v: any) => _.includes(self.multiSelectedCode(), v.empCd));
            let employeeIds = _.map(employees, 'empId');
            let confirmedAtr = (self.selectedId() == 1) ? 1 : 0;
            let checkedHandler = self.permissionHandCorrection();
            let command = {
                employeeIds: employeeIds,
                dates: dates,
                confirmedAtr: confirmedAtr,
                checkedHandler: checkedHandler
            };


            service.updateBasicSchedule(command).done(() => {
                nts.uk.ui.block.clear();
                setShared('dataFromScreenD', {
                    clickCloseDialog: false
                });
                nts.uk.ui.windows.close();
            });



        }

        /**
         * Close dialog
         */
        closeDialog(): void {
            setShared('dataFromScreenD', {
                clickCloseDialog: true
            });
            nts.uk.ui.windows.close();
        }
    }

    export class ListType {
        static EMPLOYMENT = 1;
        static Classification = 2;
        static JOB_TITLE = 3;
        static EMPLOYEE = 4;
    }

    export interface UnitModel {
        code: string;
        name: string;
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

    class BoxModel {
        id: number;
        name: string;
        constructor(id, name) {
            var self = this;
            self.id = id;
            self.name = name;
        }
    }
}