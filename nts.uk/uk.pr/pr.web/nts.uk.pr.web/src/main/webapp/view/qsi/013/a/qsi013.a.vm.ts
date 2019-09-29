module nts.uk.pr.view.qsi013.a.viewmodel {
    import dialog = nts.uk.ui.dialog;
    import errors = nts.uk.ui.errors;
    import block = nts.uk.ui.block;
    import model = nts.uk.pr.view.qsi013.share.model;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;


    export class ScreenModel {
        ccg001ComponentOption: GroupOption;
        startDate: KnockoutObservable<string> = ko.observable('');
        startDateJp: KnockoutObservable<string> = ko.observable('');
        endDate: KnockoutObservable<string> = ko.observable('');
        endDateJp: KnockoutObservable<string> = ko.observable('');
        filingDate: KnockoutObservable<string> = ko.observable('');
        filingDateJp: KnockoutObservable<string> = ko.observable('');

        officeInformations: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getBusinessDivision());
        businessArrSymbols: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getBussEsimateClass());
        outputOrders: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getSocialInsurOutOrder());
        printPersonNumbers: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getPersonalNumClass());
        submittedNames: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getSubNameClass());
        insuredNumbers: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getInsurPersonNumDivision());
        textPersonNumbers: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getTextPerNumberClass());
        outputFormats: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getOutputFormatClass());
        lineFeedCodes: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getLineFeedCode());


        /* kcp005 */
        baseDate: any;
        listComponentOption: any;
        selectedCode: KnockoutObservable<string> = ko.observable('');
        multiSelectedCode: KnockoutObservableArray<string>;
        isShowAlreadySet: KnockoutObservable<boolean>;
        alreadySettingList: KnockoutObservableArray<UnitAlreadySettingModel>;
        isDialog: KnockoutObservable<boolean>;
        isShowNoSelectRow: KnockoutObservable<boolean>;
        isMultiSelect: KnockoutObservable<boolean>;
        isShowWorkPlaceName: KnockoutObservable<boolean>;
        isShowSelectAllButton: KnockoutObservable<boolean>;
        disableSelection : KnockoutObservable<boolean>;
        employeeList: KnockoutObservableArray<UnitModel> = ko.observableArray<UnitModel>([]);

        socInsurNotiCreSet : KnockoutObservable<SocInsurNotiCreSet> = ko.observable(new SocInsurNotiCreSet({
                officeInformation: 0,
                printPersonNumber: 0,
                businessArrSymbol: 0,
                outputOrder: 0,
                submittedName: 0,
                insuredNumber: 0,
                fdNumber: null,
                textPersonNumber: 0,
                outputFormat: 0,
                lineFeedCode: 0
                }));
        constructor() {
            let self = this;
            let end  = new Date();
            let start = new Date();

            start.setMonth(start.getMonth() - 1);
            start.setDate(start.getDate() + 2);
            end.setDate(end.getDate() + 1);
            self.startDate(start.toISOString());
            self.endDate(end.toISOString());
            self.filingDate(end.toISOString());
            self.loadKCP005();
            self.loadCCG001();
            self.startDate.subscribe((data) =>{
                if(nts.uk.util.isNullOrEmpty(data)){
                    return;
                }
                self.startDateJp(" (" + nts.uk.time.dateInJapanEmpire(data) + ")");
            });

            self.endDate.subscribe((data) =>{
                if(nts.uk.util.isNullOrEmpty(data)){
                    return;
                }
                self.endDateJp(" (" + nts.uk.time.dateInJapanEmpire(data) + ")");
            });

            self.filingDate.subscribe((data)=>{
                if(nts.uk.util.isNullOrEmpty(data)){
                    return;
                }
                self.filingDateJp(" (" + nts.uk.time.dateInJapanEmpire(data) + ")");
            });
            self.initScreen();
        }



        initScreen(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            block.invisible();

            service.getSocialInsurNotiCreateSet().done(function(data: ISocInsurNotiCreSet) {
                if(data != null) {
                    self.socInsurNotiCreSet().officeInformation(data.officeInformation);
                    self.socInsurNotiCreSet().printPersonNumber(data.printPersonNumber);
                    self.socInsurNotiCreSet().businessArrSymbol(data.businessArrSymbol);
                    self.socInsurNotiCreSet().outputOrder(data.outputOrder);
                    self.socInsurNotiCreSet().submittedName(data.submittedName);
                    self.socInsurNotiCreSet().insuredNumber(data.insuredNumber);
                    self.socInsurNotiCreSet().fdNumber(data.fdNumber);
                    self.socInsurNotiCreSet().textPersonNumber(data.textPersonNumber == null ? 0 : data.textPersonNumber);
                    self.socInsurNotiCreSet().outputFormat(data.outputFormat == null ? 0 : data.outputFormat);
                    self.socInsurNotiCreSet().lineFeedCode(data.lineFeedCode == null ? 0 : data.lineFeedCode);
                }
            }).fail(function(result) {
                dialog.alertError(result.errorMessage);
                dfd.reject();
            });
            block.clear();
            return dfd.promise();
        }

        openBScreen() {
            let self = this;
            let params = {
                employeeList: self.getListEmployee(self.selectedCode(), self.employeeList())
            };
            setShared("QSI013_PARAMS_B", params);
            modal("/view/qsi/013/b/index.xhtml");
        }

        getListEmployee(empCode: Array, listEmp: Array){
            let listEmployee: any = [];
            _.each(empCode, (item) =>{
                let emp = _.find(listEmp, function(itemEmp) { return itemEmp.code == item; });
                listEmployee.push(emp);
            });
            return listEmployee;
        }

        loadKCP005(){
            let self = this;
            self.baseDate = ko.observable(new Date());
            self.multiSelectedCode = ko.observableArray(['0', '1', '4']);
            self.isShowAlreadySet = ko.observable(false);
            self.alreadySettingList = ko.observableArray([
                {code: '1', isAlreadySetting: true},
                {code: '2', isAlreadySetting: true}
            ]);
            self.isDialog = ko.observable(true);
            self.isShowNoSelectRow = ko.observable(false);
            self.isMultiSelect = ko.observable(true);
            self.isShowWorkPlaceName = ko.observable(true);
            self.isShowSelectAllButton = ko.observable(false);
            self.disableSelection = ko.observable(false);

            self.listComponentOption = {
                isShowAlreadySet: self.isShowAlreadySet(),
                isMultiSelect: self.isMultiSelect(),
                listType: ListType.EMPLOYEE,
                employeeInputList: self.employeeList,
                selectType: SelectType.SELECT_ALL,
                selectedCode: self.selectedCode,
                isDialog: self.isDialog(),
                isShowNoSelectRow: self.isShowNoSelectRow(),
                alreadySettingList: self.alreadySettingList,
                isShowWorkPlaceName: self.isShowWorkPlaceName(),
                isShowSelectAllButton: self.isShowSelectAllButton(),
                disableSelection : self.disableSelection(),
                maxRows: 18
            };
            $('#component-items-list').ntsListComponent(self.listComponentOption);
        }

        setEmployee(item){
            let listEmployee = [];
            _.each(item, (item) => {
                let employee: Employee = new Employee(item.employeeId,
                    item.employeeCode,
                    item.employeeName,
                    item.workplaceName);
                listEmployee.push(employee);
            });
            return listEmployee;
        }

        exportFile(exportPDF: any): void {
            let self = this;
            if(self.validate()){
                return;
            }
            let employList = self.getListEmpId(self.selectedCode(), self.employeeList());
            let data: any = {
                socialInsurNotiCreateSet: {
                    officeInformation: self.socInsurNotiCreSet().officeInformation(),
                    printPersonNumber: self.socInsurNotiCreSet().printPersonNumber(),
                    businessArrSymbol: self.socInsurNotiCreSet().businessArrSymbol(),
                    outputOrder: self.socInsurNotiCreSet().outputOrder(),
                    submittedName: self.socInsurNotiCreSet().submittedName(),
                    insuredNumber: self.socInsurNotiCreSet().insuredNumber(),
                    fdNumber: self.socInsurNotiCreSet().fdNumber().trim() == '' ? null : self.socInsurNotiCreSet().fdNumber(),
                    textPersonNumber: self.socInsurNotiCreSet().textPersonNumber(),
                    outputFormat: self.socInsurNotiCreSet().outputFormat(),
                    lineFeedCode: self.socInsurNotiCreSet().lineFeedCode()
                },
                empIds: employList,
                startDate: moment.utc(self.startDate(), "YYYY/MM/DD"),
                endDate: moment.utc(self.endDate(), "YYYY/MM/DD"),
                reference: moment.utc(self.filingDate(), "YYYY/MM/DD")
            };

            if(exportPDF == 0) {
                nts.uk.ui.block.grayout();
                service.exportFilePDF(data).done(function() {
                }).fail(function(error) {
                    nts.uk.ui.dialog.alertError(error);
                }).always(function() {
                    nts.uk.ui.block.clear();
                });
            }

            if(exportPDF == 1) {
                nts.uk.ui.block.grayout();
                service.exportFileCSV(data).done(function() {
                }).fail(function(error) {
                nts.uk.ui.dialog.alertError(error);
                }).always(function() {
                nts.uk.ui.block.clear();
                });
            }

        }

        getListEmpId(empCode: Array, listEmp: Array){
            let listEmpId =[];
            _.each(empCode, (item) =>{
                let emp = _.find(listEmp, function(itemEmp) { return itemEmp.code == item; });
                listEmpId.push(emp.id);
            });
            return listEmpId;
        }

        validate(){
            errors.clearAll();
            $("#A2_4").trigger("validate");
            $("#A2_7").trigger("validate");
            $("#A4_4").trigger("validate");
            return errors.hasError();
        }

        loadCCG001(){
            let self = this;
            self.ccg001ComponentOption = {
                /** Common properties */
                showEmployeeSelection: true,
                showQuickSearchTab: false,
                showAdvancedSearchTab: true,
                showBaseDate: false,
                showClosure: false,
                showAllClosure: false,
                showPeriod: false,
                periodFormatYM: false,
                tabindex: 9,
                /** Required parameter */
                baseDate: moment().toISOString(),
                periodStartDate: moment().toISOString(),
                periodEndDate: moment().toISOString(),
                inService: true,
                leaveOfAbsence: true,
                closed: true,
                retirement: true,

                /** Quick search tab options */
                showAllReferableEmployee: true,
                showOnlyMe: true,
                showSameWorkplace: false,
                showSameWorkplaceAndChild: false,

                /** Advanced search properties */
                showEmployment: true,
                showWorkplace: true,
                showClassification: true,
                showJobTitle: true,
                showWorktype: true,
                isMutipleCheck: true,
                /**
                 * Self-defined function: Return data from CCG001
                 * @param: data: the data return from CCG001
                 */
                returnDataFromCcg001: function(data: Ccg001ReturnedData) {
                    self.employeeList(self.setEmployee(data.listEmployee));
                    self.loadKCP005();
                }
            };

            $('#com-ccg001').ntsGroupComponent(self.ccg001ComponentOption);

        }
    }
    export interface GroupOption {
        /** Common properties */
        showEmployeeSelection?: boolean; // 検索タイプ
        showQuickSearchTab?: boolean; // クイック検索
        showAdvancedSearchTab?: boolean; // 詳細検索
        showBaseDate?: boolean; // 基準日利用
        showClosure?: boolean; // 就業締め日利用
        showAllClosure?: boolean; // 全締め表示
        showPeriod?: boolean; // 対象期間利用
        periodFormatYM?: boolean; // 対象期間精度
        isInDialog?: boolean;
        tabindex: number;

        /** Required parameter */
        baseDate?: string; // 基準日
        periodStartDate?: string; // 対象期間開始日
        periodEndDate?: string; // 対象期間終了日
        inService: boolean; // 在職区分
        leaveOfAbsence: boolean; // 休職区分
        closed: boolean; // 休業区分
        retirement: boolean; // 退職区分

        /** Quick search tab options */
        showAllReferableEmployee?: boolean; // 参照可能な社員すべて
        showOnlyMe?: boolean; // 自分だけ
        showSameWorkplace?: boolean; // 同じ職場の社員
        showSameWorkplaceAndChild?: boolean; // 同じ職場とその配下の社員

        /** Advanced search properties */
        showEmployment?: boolean; // 雇用条件
        showWorkplace?: boolean; // 職場条件
        showClassification?: boolean; // 分類条件
        showJobTitle?: boolean; // 職位条件
        showWorktype?: boolean; // 勤種条件
        isMutipleCheck?: boolean; // 選択モード
        isTab2Lazy?: boolean;

        /** Data returned */
        returnDataFromCcg001: (data: Ccg001ReturnedData) => void;
    }

    export interface EmployeeSearchDto {
        employeeId: string;
        employeeCode: string;
        employeeName: string;
        workplaceId: string;
        workplaceName: string;
    }
    export interface Ccg001ReturnedData {
        baseDate: string; // 基準日
        closureId?: number; // 締めID
        periodStart: string; // 対象期間（開始)
        periodEnd: string; // 対象期間（終了）
        listEmployee: Array<EmployeeSearchDto>; // 検索結果
    }

    /*kcp005*/
    export class ListType {
        static EMPLOYMENT = 1;
        static Classification = 2;
        static JOB_TITLE = 3;
        static EMPLOYEE = 4;
    }

    export interface UnitModel {
        id?: string;
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

    //Enum


    export interface ISocInsurNotiCreSet {
        officeInformation: number;
        printPersonNumber: number;
        businessArrSymbol: number;
        outputOrder: number;
        submittedName: number;
        insuredNumber: number;
        fdNumber: string;
        textPersonNumber:number;
        outputFormat: number;
        lineFeedCode: number;
    }

    export class SocInsurNotiCreSet {
        officeInformation: KnockoutObservable<number>;
        printPersonNumber: KnockoutObservable<number>;
        businessArrSymbol: KnockoutObservable<number>;
        outputOrder: KnockoutObservable<number>;
        submittedName: KnockoutObservable<number>;
        insuredNumber: KnockoutObservable<number>;
        fdNumber: KnockoutObservable<string>;
        textPersonNumber: KnockoutObservable<number>;
        outputFormat: KnockoutObservable<number>;
        lineFeedCode: KnockoutObservable<number>;
        constructor(params: ISocInsurNotiCreSet) {
            this.officeInformation = ko.observable(params.officeInformation);
            this.printPersonNumber = ko.observable(params.printPersonNumber);
            this.businessArrSymbol = ko.observable(params.businessArrSymbol);
            this.outputOrder = ko.observable(params.outputOrder);
            this.submittedName = ko.observable(params.submittedName);
            this.insuredNumber = ko.observable(params.insuredNumber);
            this.fdNumber = ko.observable(params ? params.fdNumber : null);
            this.textPersonNumber = ko.observable(params ? params.textPersonNumber : null);
            this.outputFormat = ko.observable(params ? params.outputFormat : null);
            this.lineFeedCode = ko.observable(params ? params.lineFeedCode : null);
        }
    }

    export class Employee {
        id: string;
        code: string;
        name: string;
        workplaceName: string;

        constructor(employeeId: string, employeeCode: string, employeeName: string, workplaceName: string) {
            this.id = employeeId;
            this.code = employeeCode;
            this.name = employeeName;
            this.workplaceName = workplaceName;
        }
    }

}
