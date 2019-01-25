module nts.uk.at.view.kfp001.c {
    import getText = nts.uk.resource.getText;
    import Ccg001ReturnedData = nts.uk.com.view.ccg.share.ccg.service.model.Ccg001ReturnedData;
    import EmployeeSearchDto = nts.uk.com.view.ccg.share.ccg.service.model.EmployeeSearchDto;
    import GroupOption = nts.uk.com.view.ccg.share.ccg.service.model.GroupOption;
    export module viewmodel {
        export class ScreenModel {

            //Declare screenName flag to forward screen B or screen C
            screenName: string;

            //Declare kcp005 list properties
            listComponentOption: any;
            selectedCode: KnockoutObservable<string>;
            multiSelectedCode: KnockoutObservableArray<string>;
            isShowAlreadySet: KnockoutObservable<boolean>;
            alreadySettingList: KnockoutObservableArray<UnitAlreadySettingModel>;
            isDialog: KnockoutObservable<boolean>;
            isShowNoSelectRow: KnockoutObservable<boolean>;
            isMultiSelect: KnockoutObservable<boolean>;
            isShowWorkPlaceName: KnockoutObservable<boolean>;
            isShowSelectAllButton: KnockoutObservable<boolean>;
            employeeList: KnockoutObservableArray<UnitModel>;

            //Declare employee filter component
            ccg001ComponentOption: any;
            showinfoSelectedEmployee: KnockoutObservable<boolean>;
            // Options
            baseDate: KnockoutObservable<Date>;
            selectedEmployee: KnockoutObservableArray<EmployeeSearchDto>;

            //close period
            periodStartDate: any = ko.observable(null);
            dScreenmodel: any = ko.observable(null);
            periodEndDate: any = ko.observable(null);


            constructor() {

                var self = this;

                //Init kcp005 properties
                self.baseDate = ko.observable(new Date());
                self.selectedCode = ko.observable(null);
                self.multiSelectedCode = ko.observableArray([]);
                self.isShowAlreadySet = ko.observable(false);
                self.alreadySettingList = ko.observableArray([
                    { code: '1', isAlreadySetting: true },
                    { code: '2', isAlreadySetting: true }
                ]);
                self.isDialog = ko.observable(true);
                self.isShowNoSelectRow = ko.observable(false);
                self.isMultiSelect = ko.observable(false);
                self.isShowWorkPlaceName = ko.observable(true);
                self.isShowSelectAllButton = ko.observable(false);

                this.employeeList = ko.observableArray<UnitModel>([]);

                self.listComponentOption = {
                    isShowAlreadySet: self.isShowAlreadySet(),
                    isMultiSelect: true,
                    listType: ListType.EMPLOYEE,
                    employeeInputList: self.employeeList,
                    selectType: SelectType.SELECT_ALL,
                    selectedCode: self.multiSelectedCode,
                    isDialog: self.isDialog(),
                    isShowNoSelectRow: self.isShowNoSelectRow(),
                    alreadySettingList: self.alreadySettingList,
                    isShowWorkPlaceName: self.isShowWorkPlaceName(),
                    isShowSelectAllButton: self.isShowSelectAllButton(),
                    isSelectAllAfterReload: true
                };


                //Init employee filter component
                self.selectedEmployee = ko.observableArray([]);
                self.showinfoSelectedEmployee = ko.observable(false);
                self.baseDate = ko.observable(new Date());

                self.reloadCcg001();
                self.dScreenmodel = new nts.uk.at.view.kfp001.d.viewmodel.ScreenModel();

            }

            public reloadCcg001(): void {
                let self = this;
                if ($('.ccg-sample-has-error').ntsError('hasError')) {
                    return;
                }
                //                let dataB = nts.uk.ui.windows.getShared("KFP001_DATAB");
                //                if (dataB != null) {
                //                    self.periodStartDate(moment.utc(dataB.periodStartDate).toISOString());
                //                    self.periodEndDate(moment.utc(dataB.periodEndDate).toISOString());
                //                } else {
                //                    self.periodStartDate(moment.utc().toISOString());
                //                    self.periodEndDate(moment.utc().toISOString());
                //                }

                self.ccg001ComponentOption = {
                    /** Common properties */
                    systemType: 2, // システム区分
                    showEmployeeSelection: false, // 検索タイプ
                    showQuickSearchTab: true, // クイック検索
                    showAdvancedSearchTab: true, // 詳細検索
                    showBaseDate: false, // 基準日利用
                    showClosure: true, // 就業締め日利用
                    showAllClosure: true, // 全締め表示
                    showPeriod: true, // 対象期間利用
                    periodFormatYM: false, // 対象期間精度

                    /** Required parameter */
                    baseDate: moment().toISOString(), // 基準日
                    periodStartDate: self.periodStartDate(), // 対象期間開始日
                    periodEndDate: self.periodEndDate(), // 対象期間終了日
                    inService: true, // 在職区分
                    leaveOfAbsence: false, // 休職区分
                    closed: false, // 休業区分
                    retirement: false, // 退職区分

                    /** Quick search tab options */
                    showAllReferableEmployee: true, // 参照可能な社員すべて
                    showOnlyMe: true, // 自分だけ
                    showSameWorkplace: true, // 同じ職場の社員
                    showSameWorkplaceAndChild: true, // 同じ職場とその配下の社員

                    /** Advanced search properties */
                    showEmployment: false, // 雇用条件
                    showWorkplace: true, // 職場条件
                    showClassification: true, // 分類条件
                    showJobTitle: true, // 職位条件
                    showWorktype: true, // 勤種条件
                    isMutipleCheck: true, // 選択モード

                    /** Return data */
                    returnDataFromCcg001: function(data: Ccg001ReturnedData) {
                        self.showinfoSelectedEmployee(true);
                        self.selectedEmployee(data.listEmployee);

                        //Convert list Object from server to view model list
                        let items = _.map(data.listEmployee, item => {
                            return new UnitModel(item);
                        });
                        self.employeeList(items);

                        //Fix bug 42, bug 43
                        let selectList = _.map(data.listEmployee, item => {
                            return item.employeeCode;
                        });
                        self.multiSelectedCode([]);
                        self.multiSelectedCode(selectList);
                    }
                }

                // Start component
                //                $('#ccgcomponent').ntsGroupComponent(self.ccg001ComponentOption);
            }

            start() {
                var self = this;
                self.reloadCcg001();
                $('#ccgcomponent').ntsGroupComponent(self.ccg001ComponentOption);
                $('#component-items-list').ntsListComponent(self.listComponentOption);
                $("#button-2").focus();
            }


            opendScreenBorJ() {
                let self = this;
                if (self.selectedEmployee().length <= 0 || self.multiSelectedCode().length <= 0) {
                    nts.uk.ui.dialog.info({ messageId: "Msg_206" });
                    return;
                }
                $("#wizard").ntsWizard("next").done(function() {
                    $('#checkBox1').focus();
                });

                nts.uk.ui.windows.setShared("KFP001_DATAC", self.selectedEmployee());
                nts.uk.ui.windows.setShared("KFP001_DATAC_SELECT", self.multiSelectedCode().length);
                self.dScreenmodel.start();
            }
            navigateView() {
                $("#wizard").ntsWizard("prev").done(function() {
                    $('#checkBox1').focus();

                });
            }


        }

        //Object for kcp005
        export class ListType {
            static EMPLOYMENT = 1;
            static Classification = 2;
            static JOB_TITLE = 3;
            static EMPLOYEE = 4;
        }

        export class UnitModel {
            code: string;
            name: string;
            workplaceName: string;
            isAlreadySetting: boolean;
            constructor(x: EmployeeSearchDto) {
                let self = this;
                if (x) {
                    self.code = x.employeeCode;
                    self.name = x.employeeName;
                    self.workplaceName = x.workplaceName;
                    self.isAlreadySetting = false;
                } else {
                    self.code = "";
                    self.name = "";
                    self.workplaceName = "";
                    self.isAlreadySetting = false;
                }
            }
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

        //Object for filter component        
        export interface GroupOption {
            baseDate?: KnockoutObservable<Date>;
            // クイック検索タブ
            isQuickSearchTab: boolean;
            // 参照可能な社員すべて
            isAllReferableEmployee: boolean;
            //自分だけ
            isOnlyMe: boolean;
            //おなじ部門の社員
            isEmployeeOfWorkplace: boolean;
            //おなじ＋配下部門の社員
            isEmployeeWorkplaceFollow: boolean;


            // 詳細検索タブ
            isAdvancedSearchTab: boolean;
            //複数選択 
            isMutipleCheck: boolean;

            //社員指定タイプ or 全社員タイプ
            isSelectAllEmployee: boolean;

            onSearchAllClicked: (data: EmployeeSearchDto[]) => void;

            onSearchOnlyClicked: (data: EmployeeSearchDto) => void;

            onSearchOfWorkplaceClicked: (data: EmployeeSearchDto[]) => void;

            onSearchWorkplaceChildClicked: (data: EmployeeSearchDto[]) => void;

            onApplyEmployee: (data: EmployeeSearchDto[]) => void;
        }

        export interface EmployeeSearchDto {
            employeeId: string;

            employeeCode: string;

            employeeName: string;

            workplaceCode: string;

            workplaceId: string;

            workplaceName: string;
        }

    }
}
