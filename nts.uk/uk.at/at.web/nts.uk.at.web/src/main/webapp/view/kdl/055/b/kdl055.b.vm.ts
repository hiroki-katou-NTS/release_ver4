/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />

import characteristics = nts.uk.characteristics;
import IScheduleImport = nts.uk.at.view.kdl055.a.viewmodel.IScheduleImport;

module nts.uk.at.view.kdl055.b.viewmodel {

    @bean()
    export class KDL055BViewModel extends ko.ViewModel {
        overwrite: boolean = false;
        filename: KnockoutObservable<string> = ko.observable(null);
        gridOptions = { dataSource: [], columns: [], features: [], ntsControls: [] };
        data: CaptureDataOutput = null;
        isOpenKDL053: boolean = false;
        isEnableRegister: KnockoutObservable<boolean> = ko.observable(true);

        created(params: any) {
            const vm = this;

            characteristics.restore('ScheduleImport').then((obj: IScheduleImport) => {
                if (obj) {
                    vm.overwrite = obj.overwrite;
                    vm.filename(obj.mappingFile);
                }
            });

            if (params) {
                vm.$blockui('show');
                vm.$ajax(paths.getCaptureData, { data: params, overwrite: vm.overwrite }).done((res: CaptureDataOutput) => {
                    if (res) {
                        console.log(res);
                        vm.data = res;
                        vm.convertToGrid(vm.data);
                        vm.loadGrid();
                        vm.loadError(vm.data);
                    }
                }).fail((err: any) => {
                    if (err) {
                        vm.$dialog.error({ messageId: err.messageId, messageParams: err.parameterIds });
                    }
                }).always(() => vm.$blockui('hide'));
            }

            setInterval(() => {
                let errors = $('#grid').mGrid('errors');
                if (errors.length > 0) {
                    this.isEnableRegister(false);
                } else {
                    this.isEnableRegister(true);
                }
            }, 100);
        }
        
        mounted() {
            const vm = this;

            // vm.loadGrid();
        }

        register() {
            const vm = this;
            let targets: ScheduleRegisterTarget[] = [];

            let dataSource: any[] = $("#grid").mGrid("dataSource");
            console.log(dataSource);

            _.forEach(dataSource, item => {
                _.forEach(vm.data.importableDates, date => {
                    let target: ScheduleRegisterTarget = {employeeId: item.employeeId, date: '', importCode: ''};
                    if (item[date]) {
                        target.date = date;
                        target.importCode = item[date];
                        targets.push(target);
                    }
                })
            });

            let command = {
                targets: targets,
                overwrite: vm.overwrite
            }

            vm.$blockui('show');
            vm.$ajax(paths.register, command).done((res) => {
                if (res) {
                    // reset list data fail
                    vm.data.mappingErrorList = [];

                    // close KDL053
                    let btnCloseKDL053 = $('#btnClose');
                    if (btnCloseKDL053) {
                        btnCloseKDL053.click();
                    }
                    if (res.length > 0) {
                        let request: any = {};
                        request.errorRegistrationList = [];
                        _.forEach(res, errorItem => {
                            // bind to list mappingErrorList
                            let empFilter = _.filter(vm.data.listPersonEmp, {'employeeCode': errorItem.employeeCode});
                            let empId = empFilter.length > 0 ? empFilter[0].employeeId : '';

                            let error: MappingErrorOutput = {employeeId: empId, employeeName: errorItem.employeename, date: errorItem.date, errorMessage: errorItem.errorMessage};
                            vm.data.mappingErrorList.push(error);
                            vm.$blockui("hide");

                        });
                        // open KDL053
                        let empList = vm.data.listPersonEmp;
                        for (let i = 0; i < vm.data.mappingErrorList.length; i++) {
                            let empFilter = _.filter(empList, {'employeeId': vm.data.mappingErrorList[i].employeeId});
                            let empCode = empFilter.length > 0 ? empFilter[0].employeeCode : '';

                            let item = {id: i, sid: vm.data.mappingErrorList[i].employeeId, scd: empCode, empName: vm.data.mappingErrorList[i].employeeName, 
                                date: vm.data.mappingErrorList[i].date, attendanceItemId: null, errorMessage: vm.data.mappingErrorList[i].errorMessage};
                            request.errorRegistrationList.push(item);
                        }
                        request.isRegistered = 0;
                        request.dispItemCol = true;
                        request.employeeIds = _.map(vm.data.listPersonEmp, (item) => item.employeeId);
                        if (!vm.isOpenKDL053) {
                            vm.$window.modeless('at', '/view/kdl/053/a/index.xhtml', request).then(() => {
                                vm.isOpenKDL053 = false;
                            });
                            vm.isOpenKDL053 = true;
                        }
                    } else {
                        vm.$dialog.info({ messageId: "Msg_15"}).then(() => vm.$blockui("hide"));                        
                        vm.close();
                    }
                }
            }).fail((err) => {
                if (err) {
                    vm.$dialog.error({ messageId: err.messageId, messageParams: err.parameterIds });
                }
            }).always(() => {
                vm.$blockui("hide");
            });
        }

        openKDL053() {
            const vm = this;

            let errorList = $('#grid').mGrid('errors');
            let empList = vm.data.listPersonEmp;
            let empIds: any[] = [];
            let request: any = {};
            request.errorRegistrationList = [];

            _.forEach(errorList, (error: any) => {
                let empId = error.rowId;
                empIds.push(empId);
                let empFilter = _.filter(empList, {'employeeId': empId});
                let empCode = empFilter.length > 0 ? empFilter[0].employeeCode : '';
                let empname = empFilter.length > 0 ? empFilter[0].businessName : '';
                let item = {id: error.index, sid: empId, scd: empCode, empName: empname, 
                            date: error.columnKey, attendanceItemId: null, errorMessage: error.message};
                request.errorRegistrationList.push(item);
            });
            request.isRegistered = 0;
            request.dispItemCol = false;
            request.employeeIds = empIds;

            if (!vm.isOpenKDL053) {
                vm.$window.modeless('at', '/view/kdl/053/a/index.xhtml', request).then(() => {
                    vm.isOpenKDL053 = false;
                });
                vm.isOpenKDL053 = true;
            }
        }

        close() {
            this.$window.close();
        }

        loadGrid() {
            let vm = this;

            if ($("#grid").data("mGrid")) $("#grid").mGrid("destroy");
            new nts.uk.ui.mgrid.MGrid($("#grid")[0], {
                width: '1200px',
                height: '600px',
                headerHeight: "40px",
                subHeight: "140px",
                subWidth: "100px",
                dataSource: vm.gridOptions.dataSource,
                columns: vm.gridOptions.columns,
                primaryKey: 'employeeId',
                virtualization: true,
                virtualizationMode: "continuous",
                enter: "right",
                autoFitWindow: false,
                features: vm.gridOptions.features,
                ntsControls: vm.gridOptions.ntsControls
            }).create();
        }

        loadError(param: CaptureDataOutput) {
            const vm = this;

            let mappingErrorList = param.mappingErrorList;
            let errors = [];
            
            _.forEach(mappingErrorList, (error: MappingErrorOutput) => {
                let err = { columnKey: 'nameHeader', id: null, index: null, message: error.errorMessage };
                
                if (error.employeeId) {
                    err.id = error.employeeId;
                    for (let i = 0; i < param.listPersonEmp.length; i++) {
                        if (param.listPersonEmp[i].employeeId === error.employeeId) {
                            err.index = i;
                        }
                    }
                }
                if (error.date) {
                    err.columnKey = error.date;
                }
                
                errors.push(err);
            });

            $("#grid").mGrid("setErrors", errors);

            if (mappingErrorList.length > 0) {
                let request: any = {};
                request.errorRegistrationList = [];
                for (let i = 0; i < mappingErrorList.length; i++) {
                    let empList = vm.data.listPersonEmp;
                    let empFilter = _.filter(empList, {'employeeId': mappingErrorList[i].employeeId});
                    let empCode = empFilter.length > 0 ? empFilter[0].employeeCode : '';


                    let item = {id: i, sid: mappingErrorList[i].employeeId, scd: empCode, empName: mappingErrorList[i].employeeName, 
                        date: mappingErrorList[i].date, attendanceItemId: null, errorMessage: mappingErrorList[i].errorMessage};
                    request.errorRegistrationList.push(item);
                }
                request.isRegistered = 0;
                request.dispItemCol = true;
                request.employeeIds = _.map(param.listPersonEmp, (item) => item.employeeId);
                vm.$window.modeless('at', '/view/kdl/053/a/index.xhtml', request).then(() => {
                    vm.isOpenKDL053 = false;
                });
                // vm.isOpenKDL053 = true;
            }
        }

        convertToGrid(data: CaptureDataOutput) {
            const vm = this;

            let headerStyle = { name: 'HeaderStyles', columns: [{ key: 'nameHeader', colors: ['weekday', 'align-center'] }] };
            let cellStates = [];

            // columns
            let importableDates: string[] = data.importableDates;

            vm.gridOptions.columns.push({ headerText: vm.$i18n('KDL055_26') , itemId: 'nameHeader', key: 'nameHeader', dataType: 'string', width: '150px', columnCssClass: 'halign-left limited-label', headerCssClass: 'halign-center valign-center', ntsControl: 'Label' });

            _.forEach(importableDates, (dateString: string) => {
                let item = { headerText: vm.convertDateHeader(dateString), itemId: dateString, key: dateString, dataType: 'string', width: '70px', columnCssClass: 'center-align', headerCssClass: 'center-align', constraint: {primitiveValue: 'ShiftMasterImportCode'} };
                if (_.filter(data.holidays, {'date': dateString}).length > 0 || new Date(dateString).getDay() === 0) {
                    headerStyle.columns.push({ key: dateString, colors: ['sunday', 'align-center'] });
                } else if (new Date(dateString).getDay() === 6) {
                    headerStyle.columns.push({ key: dateString, colors: ['saturday', 'align-center'] });
                } else {
                    headerStyle.columns.push({ key: dateString, colors: ['weekday', 'align-center'] });
                }

                vm.gridOptions.columns.push(item);
                // vm.gridOptions.ntsControls.push({name: dateString, constraint: {primitiveValue: 'ShiftMasterImportCode'}});
            });

            // dataSources
            let listPersonEmp = data.listPersonEmp;
            let results = data.importResult.results;

            _.forEach(listPersonEmp, (emp) => {
                let record: any = { employeeId: emp.employeeId, employeeCode: emp.employeeCode, employeeName: emp.businessName, nameHeader: emp.employeeCode + ' ' + emp.businessName };
                cellStates.push({rowId: emp.employeeId, columnKey: 'nameHeader', state: ['limited-label']});
                _.forEach(results, (result: ImportResultDetail) => {
                    if (result.employeeId === emp.employeeId) {
                        // record[result.ymd] = result.importCode;
                        record[result.ymd] = result.importCode;
                        
                        if ([2, 3, 4, 5, 9].includes(result.status) || (result.status === 9 && !vm.overwrite)) {
                            cellStates.push({ rowId: emp.employeeId, columnKey: result.ymd, state: ["mgrid-disable", "align-center"]});
                        } else {
                            cellStates.push({ rowId: emp.employeeId, columnKey: result.ymd, state: ["align-center"]});
                        }
                    }
                });
                vm.gridOptions.dataSource.push(record);
            });

            _.forEach(listPersonEmp, (emp) => {
                _.forEach(importableDates, (date: string) => {
                    if (_.filter(results, { 'employeeId': emp.employeeId, 'ymd': date}).length == 0) {
                        cellStates.push({ rowId: emp.employeeId, columnKey: date, state: ["mgrid-disable", "align-center"]});
                    }
                });
            })

            // features
            vm.gridOptions.features = [{ name: 'Copy' }, { name: 'Tooltip', error: true }];
            let columnFixing = { name: 'ColumnFixing', columnSettings: [{ columnKey: 'nameHeader', isFixed: true }]};

            vm.gridOptions.features.push(columnFixing);
            vm.gridOptions.features.push(headerStyle);
            vm.gridOptions.features.push({ name: 'CellStyles', states: cellStates });
        }

        convertDateHeader(text: string): string {
            let date = new Date(text);

            return "<div>" + moment(date).format('M/D') + "<div><div>" + moment(date).format('dd') + "<div>"
        }
    }

    const paths = {
        getCaptureData: 'wpl/schedule/report/getCaptureData',
        register: 'at/schedule/workschedulestate/register'
    }

    export interface ColumnItem {
        headerText: string;

        itemName: string;

        key: string;

        dataType: string;

        width: string;
    }

    export interface Record {
        id: string;

        employeeCode: string;

        ymd: string;

        importCode: string;

        status: number;
    }

    export interface CapturedRawDataDto {
        /** 取り込み内容 **/
        contents: CapturedRawDataOfCellDto[];

        /** 社員の並び順(OrderdList) **/
        employeeCodes: string[];
    }

    export interface CapturedRawDataOfCellDto {
        /** 社員コード **/
        employeeCode: string;
        /** 年月日 **/
        ymd: string;
        /** 取り込みコード **/
        importCode: string;
    }

    export interface CaptureDataOutput {
        // 社員リスト　：OrderedList<社員ID, 社員コード, ビジネスネーム>
        listPersonEmp: PersonEmpBasicInfoImport[];
        // 年月日リスト：OrderedList<年月日, 曜日>
        importableDates: string[];
        // 祝日リスト　：List<祝日>
        holidays: PublicHoliday[];
        // 取り込み結果
        importResult: ImportResult;
        // エラーリスト：List<取り込みエラーDto>
        mappingErrorList: MappingErrorOutput[];
    }

    export interface PersonEmpBasicInfoImport {
        // 個人ID
        personId: string;

        // 社員ID
        employeeId: string;

        // ビジネスネーム
        businessName: string;

        // 性別
        gender: number;

        // 生年月日
        birthday: string;

        // 社員コード
        employeeCode: string;

        // 入社年月日
        jobEntryDate: string;

        // 退職年月日
        retirementDate: string;
    }

    export interface PublicHoliday {
        companyId: string;

        date: string;

        holidayName: string;
    }

    export interface ImportResult {
        /** 1件分の取り込み結果 **/
        results: ImportResultDetail[];
        /** 取り込み不可日 **/
        unimportableDates: string[];
        /** 存在しない社員 **/
        unexistsEmployees: string[];
        /** 社員の並び順(OrderdList) **/
        orderOfEmployees: string[];
    }

    export interface ImportResultDetail {
        /** 社員ID **/
        employeeId: string;
        /** 年月日 **/
        ymd: string;
        /** 取り込みコード **/
        importCode: string;
        /** 状態 **/
        status: number;
    }

    export interface MappingErrorOutput {
        // 社員コード
        employeeId: string;
        // 社員名
        employeeName: string;
        // 年月日
        date: string;
        // エラーメッセージ
        errorMessage: string;
    }

    export enum ImportStatus {
        // 未チェック
        UNCHECKED = 0,
        // 取込可能
        IMPORTABLE = 1,
        // 参照範囲外
        OUT_OF_REFERENCE = 2,
        // 個人情報不備
        EMPLOYEEINFO_IS_INVALID = 3,
        // 在職していない
        EMPLOYEE_IS_NOT_ENROLLED = 4,
        // スケジュール管理しない 
        SCHEDULE_IS_NOTUSE = 5,
        // シフトが存在しない 
        SHIFTMASTER_IS_NOTFOUND = 6,
        // シフトが不正
        SHIFTMASTER_IS_ERROR = 7,
        // 確定済み
        SCHEDULE_IS_COMFIRMED = 8,
        // すでに勤務予定が存在する
        SCHEDULE_IS_EXISTS = 9
    }

    export interface CellState {
        rowId: string,

        columnKey: string,

        state: any[]
    }

    export interface ScheduleRegisterTarget {
        employeeId: string,

        date: string,

        importCode: string
    }
}