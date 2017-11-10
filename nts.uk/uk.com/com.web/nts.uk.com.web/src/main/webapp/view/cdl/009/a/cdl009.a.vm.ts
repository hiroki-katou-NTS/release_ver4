module nts.uk.com.view.cdl009.a {
    import close = nts.uk.ui.windows.close;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    export module viewmodel {
        export class ScreenModel {
            multiSelectedTree: KnockoutObservableArray<string>;
//            selectedWorkplace: KnockoutObservable<string>;
            isMultiSelect: KnockoutObservable<boolean>;
            selecType: KnockoutObservable<SelectType>;
            baseDate: KnockoutObservable<Date>;
            target: KnockoutObservable<TargetClassification>;
            treeGrid: any;
            listComponentOpt: any;
            employeeList: KnockoutObservableArray<any>;
            selectedEmployeeId: KnockoutObservable<string>;
            selectedEmps: KnockoutObservableArray<string>;
            
            isIncumbent: KnockoutObservable<boolean>;
            isLeaveOfAbsence: KnockoutObservable<boolean>;
            isHoliday: KnockoutObservable<boolean>;
            isRetirement: KnockoutObservable<boolean>;
            
            enrollmentStatusList: KnockoutObservableArray<number>;

            constructor() {
                let self = this;
                var params = getShared('CDL009Params');
                self.multiSelectedTree = ko.observableArray([]);
                self.multiSelectedTree(params.selectedIds ? params.selectedIds : []);
//                self.selectedWorkplace = ko.observable('');
                // isMultiSelect For Employee List KCP005
                self.isMultiSelect = ko.observable(params.isMultiSelect);
                // Select Type for Workplace List kcp004
                self.selecType = ko.observable(params.selecType ? params.selecType : SelectType.NO_SELECT);
//                if (self.isMultiSelect()) {
//                    self.multiSelectedTree(params.selectedIds ? params.selectedIds : []);
////                    self.selected = ko.observable(self.multiSelectedTree());
//                }
//                else {
//                    self.selectedWorkplace(params.selectedIds);
////                    self.selected = ko.observable(self.selectedWorkplace());
//                }
                self.baseDate = ko.observable(params.baseDate ? params.baseDate : moment(new Date()).toDate());
                self.target = ko.observable(params.target ? params.target : TargetClassification.WORKPLACE);

                self.employeeList = ko.observableArray<any>();
//                self.employeeList.subscribe(function(data) {
//                    self.listComponentOpt.employeeInputList = data;
//                });
                self.selectedEmployeeId = ko.observable('');
                self.selectedEmps = ko.observableArray([]);
                // Initial listComponentOption
                self.treeGrid = {
                    isMultiSelect: true,
                    treeType: TreeType.WORK_PLACE,
                    selectType: self.selecType(),
                    baseDate: self.baseDate,
                    selectedWorkplaceId: self.multiSelectedTree,
                    isShowSelectButton: true,
                    isDialog: true,
                    maxRows: 12,
                    tabindex: 1
                };
                self.listComponentOpt = {
                    isMultiSelect: self.isMultiSelect(),
                    listType: ListType.EMPLOYEE,
                    selectType: SelectType.NO_SELECT,
                    selectedCode: ko.observable(),
                    isDialog: true,
                    employeeInputList: self.employeeList,
                    maxRows: 12,
                    tabindex: 3,
                };
                // Set SelectedCode to listComponentOpt (Depend on isMultiSelect)
                if (self.isMultiSelect()) {
                    self.listComponentOpt.selectedCode = self.selectedEmps;
                } else {
                    self.listComponentOpt.selectedCode = self.selectedEmployeeId;
                }
                
                self.enrollmentStatusList = ko.observableArray<number>();
                self.isIncumbent = ko.observable(true);
                self.isLeaveOfAbsence = ko.observable(false);
                self.isHoliday = ko.observable(false);
                self.isRetirement = ko.observable(false);
                
            }

            /**
             * Search Employee
             */
            private searchEmp(): void {
                let self = this;
//                console.log(self.multiSelectedTree() + "search");
                if (!self.multiSelectedTree() || self.multiSelectedTree().length == 0) {
                    if (self.target() == TargetClassification.WORKPLACE) {
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_643" });
                    } else {
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_647" });
                    }
                    
//                    .then(() => nts.uk.ui.windows.close());
                    return;
                }

//                if (!self.isMultiSelect() && !self.selectedWorkplace()) {
//                    nts.uk.ui.dialog.alertError({ messageId: "Msg_643" }).then(() => nts.uk.ui.windows.close());
//                    return;
//                }

                // Search Employees
                self.findEmployee();
                $('#emp-component').focus();
            }
            
            /**
             * Find Employee
             */
            findEmployee(): JQueryPromise<any> {
                var self = this,
                    dfd = $.Deferred();
                let empStatusList: Array<number> = [];
                // Enrollment is INCUMBENT
                if (self.isIncumbent()) {
                    empStatusList.push(EnrollmentStatus.INCUMBENT);
                } 
                // Enrollment is LEAVE_OF_ABSENCE
                if (self.isLeaveOfAbsence()) {
                    empStatusList.push(EnrollmentStatus.LEAVE_OF_ABSENCE);
                }
                // Enrollment is HOLIDAY
                if (self.isHoliday()) {
                    empStatusList.push(EnrollmentStatus.HOLIDAY);
                } 
                // Enrollment is RETIREMENT
                if (self.isRetirement()) {
                    empStatusList.push(EnrollmentStatus.RETIREMENT);
                } 
                var query = {
                    workplaceIdList: self.multiSelectedTree(),
                    referenceDate: self.baseDate(),
                    empStatus: empStatusList
                };
                service.findEmployees(query).done(function(res: Array<service.model.EmployeeResult>) {
                    // Set Employee List
                    let empList: Array<any> = [];
                    res.forEach(item => {
                        empList.push({ id: item.employeeId, code: item.employeeCode, name: item.employeeName, workplaceName: item.workplaceName });
                    });
                    self.employeeList(empList);
                    dfd.resolve();
                }).fail(function(error) {
                    dfd.reject(error);
                });
                return dfd.promise();
            }

            /**
             * Close dialog.
             */
            closeDialog(): void {
                setShared('CDL009Cancel', true);
                nts.uk.ui.windows.close();
            }

            /**
             * Decide Employee
             */
            decideData(): void {
                let self = this;
//                if (self.isMultiSelect() && self.multiSelectedTree().length == 0) {
//                    nts.uk.ui.dialog.alertError({ messageId: "Msg_640" }).then(() => nts.uk.ui.windows.close());
//                    return;
//                }
//                var isNoSelectRowSelected = $("#jobtitle").isNoSelectRowSelected();
//                if (!self.isMultiSelect() && !self.selectedWorkplace() && !isNoSelectRowSelected) {
//                    nts.uk.ui.dialog.alertError({ messageId: "Msg_640" }).then(() => nts.uk.ui.windows.close());
//                    return;
//                }
                var isNoSelectRowSelected = $("#emp-component").isNoSelectRowSelected();
                if (self.isMultiSelect()) {
                    if ((!self.selectedEmps() || self.selectedEmps().length == 0)) {
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_644" });
//                        return;
                    } else {
                        setShared('CDL009Output', self.selectedEmps());
                        close();
                    }
                    
                } else if (!self.selectedEmployeeId() && !isNoSelectRowSelected) {
                    nts.uk.ui.dialog.alertError({ messageId: "Msg_644" });
//                    return;
                } else {
                    setShared('CDL009Output', self.selectedEmployeeId());
                    close();
                }
                
            }

            /**
             * function check employment
             */
            public checkExistWorkplace(code: string, data: any[]): boolean {
                for (var item of data) {
                    if (code === item.workplaceId) {
                        return true;
                    }
                }
                return false;
            }
        }

        /**
        * Tree Type
        */
        export class TreeType {
            static WORK_PLACE = 1;
            static DEPARTMENT = 2;
        }
        /**
        * List Type
        */
        export class ListType {
            static EMPLOYMENT = 1;
            static Classification = 2;
            static JOB_TITLE = 3;
            static EMPLOYEE = 4;
        }

        /**
         * class SelectType
         */
        export class SelectType {
            static SELECT_BY_SELECTED_CODE = 1;
            static SELECT_ALL = 2;
            static SELECT_FIRST_ITEM = 3;
            static NO_SELECT = 4;
        }

        /**
     * Class TargetClassification
     */
        export class TargetClassification {
            static WORKPLACE = 1;
            static DEPARTMENT = 2;
        }

        export interface UnitModel {
            workplaceId: string;
            code: string;
            name: string;
            nodeText?: string;
            level: number;
            heirarchyCode: string;
            isAlreadySetting?: boolean;
            childs: Array<UnitModel>;
        }
        
        export class EnrollmentStatus {
            static INCUMBENT = 1;
            static LEAVE_OF_ABSENCE = 2;
            static HOLIDAY = 3;
            static RETIREMENT = 6;
        }
    }
}