module nts.uk.com.view.cmm018.shr {
    import servicebase = cmm018.shr.servicebase;
    export module vmbase {
        //data register
        export class DataResigterDto{
            /**システム区分*/
            systemAtr: number;
            /**就業ルート区分: 会社(0)　－　職場(1)　－　社員(2)*/
            rootType: number;
            checkAddHist: boolean;//true: newHist, false: updateHist
            workpplaceId: string;
            employeeId: string;
            startDate: string;
            endDate: string;
            addHist: IData;
            lstAppType: Array<ApplicationType>;
            root: Array<CompanyAppRootADto>;
            checkMode: number;//0: まとめて登録モード, 1: 申請個別登録モード
            constructor(systemAtr: number, rootType: number, checkAddHist: boolean,
                workpplaceId: string,
                employeeId: string, startDate: string, endDate: string,
                addHist: IData,lstAppType: Array<ApplicationType>,
                root: Array<CompanyAppRootADto>,checkMode: number){
                    this.systemAtr = systemAtr;
                    this.rootType = rootType;
                    this.checkAddHist = checkAddHist;
                    this.workpplaceId = workpplaceId;
                    this.employeeId = employeeId;
                    this.startDate = startDate; 
                    this.endDate = endDate;
                    this.addHist = addHist;
                    this.lstAppType = lstAppType;
                    this.root = root;
                    this.checkMode = checkMode;
            }
        }
        //data root delete
        export class DataDeleteDto{
            approvalId: string;
            historyId: string;  
            constructor(approvalId: string, historyId: string){
                this.approvalId = approvalId;
                this.historyId = historyId;
            }  
        }
        //data dialog K
        export interface KData{
            appType: string; //設定する対象申請名 
            formSetting: number;//承認形態
            approverInfor: Array<ApproverDtoK>;//承認者一覧
            confirmedPerson: string; //確定者
            selectTypeSet: number;
            approvalFormName: string;
        }
        //data after grouping history (get from db)
        export class DataFullDto{
            workplaceId: string;
            lstCompany: Array<DataDisplayComDto> ;
            lstWorkplace: Array<DataDisplayWpDto> ;
            lstPerson: Array<DataDisplayPsDto> ;
        }
        //data after grouping history of company
        export class DataDisplayComDto{
            id: number;
            overLap: boolean;
            companyName: string;
            lstCompanyRoot: Array<CompanyAppRootDto> ;
        }
        //data after grouping history of work place
        export class DataDisplayWpDto{
            id: number;
            overLap: boolean;
            lstWorkplaceRoot: Array<WorkPlaceAppRootDto> ;
        }
        //data after grouping history of person
        export class DataDisplayPsDto{
            id: number;
            overLap: boolean;
            lstPersonRoot: Array<PersonAppRootDto>;
        }
        //app type
        export class ApplicationType{
            value: number;
            localizedName: string;
            employRootAtr: number;
            constructor(value: number, localizedName: string, employRootAtr: number){
                this.value = value;
                this.localizedName = localizedName;
                this.employRootAtr = employRootAtr;
            }
        }
        //screenA
        export class ListHistory {
            id: number;
            dateRange: string;
            startDate: string;
            endDate: string;
            overLap: any;
            constructor(id: number, dateRange: string, startDate: string, endDate: string, overLap: any) {
                this.id = id;
                this.dateRange = dateRange;
                this.startDate = startDate;
                this.endDate = endDate;
                this.overLap = overLap;
            }  
        }
        //screenA
        export class ListApproval {
            approvalId: string;
            name: string;
            lstApprover: Array<ApproverDto>;
            constructor(approvalId: string, name: string, lstApprover: Array<ApproverDto>) {
                var self = this;
                this.approvalId = approvalId;
                self.name = name;
                self.lstApprover = lstApprover;
            }  
        }
        //Screen I
        export interface IData_Param{
            /** name */
            name?: string;
            /**開始日*/
            startDate: string;
            /**check 申請承認の種類区分: 会社(1)　－　職場(2)　－　社員(3)*/
            check: number;
            /** まとめて設定モード(0) - 申請個別設定モード(1)*/
            mode: number;
            lstAppType: Array<ApplicationType>;
            overLap: boolean;
        }
        //ScreenI
        export class IData{
            /**開始日*/
            startDate: string;
            /**開始日 Old*/
            startDateOld: string;
            /**check 申請承認の種類区分: 会社(1)　－　職場(2)　－　社員(3)*/
            check: number;
            /** まとめて設定モード(0) - 申請個別設定モード(1)*/
            mode: number;
            /** 履歴から引き継ぐか、初めから作成するかを選択する*/
            copyDataFlag: boolean;
            lstAppType: Array<ApplicationType>;
            overLap: boolean;
            constructor(startDate: string,
                startDateOld: string,
                check: number,
                mode: number,
                copyDataFlag: boolean,
                lstAppType: Array<ApplicationType>,
                overLap: boolean){
                    this.startDate = startDate;
                    this.startDateOld = startDateOld;
                    this.check = check;
                    this.mode = mode;
                    this.copyDataFlag = copyDataFlag;
                    this.lstAppType = lstAppType;
                    this.overLap = overLap;
            }
        }
        //ScreenJ
        export class JData{
            /**開始日*/
            startDate: string;
            /**終了日*/
            endDate: string;
            /**履歴ID*/
            workplaceId: string;
            /**社員ID*/
            employeeId: string;
            /**check 申請承認の種類区分: 会社(0)　－　職場(1)　－　社員(2)*/
            check: number;
            /**「履歴を削除する」を選択する か(0)、「履歴を修正する」を選択する か(1)。*/
            editOrDelete: number;
            /**開始日 previous*/
            startDatePrevious: string;
            /** list history and approvalId */
            lstUpdate: Array<UpdateHistoryDto>;
            checkMode: number;
            constructor(startDate: string,
            endDate: string,
            workplaceId: string,
            employeeId: string,
            check: number,
            editOrDelete: number,
            startDatePrevious: string,
            lstUpdate: Array<UpdateHistoryDto>,
            checkMode: number){
                this.startDate = startDate;
                this.endDate = endDate;
                this.workplaceId = workplaceId;
                this.employeeId = employeeId;
                this.check = check;
                this.editOrDelete = editOrDelete;
                this.startDatePrevious = startDatePrevious;
                this.lstUpdate = lstUpdate;
                this.checkMode = checkMode;
            }
        }
        //ScrenJ
        export interface JData_Param{
            /** name */
            name?: string
            /**開始日*/
            startDate: string;
            /**終了日*/
            endDate: string;
            /**履歴ID*/
            workplaceId?: string;
            /**社員ID*/
            employeeId?: string;
            /**check 申請承認の種類区分: 会社(1)　－　職場(2)　－　社員(3)*/
            check: number;
            /** まとめて設定モード(0) - 申請個別設定モード(1)*/
            mode: number;
            /** 編集対象期間履歴が重なっているかチェックする*/
            overlapFlag?: boolean;
            /** list history and approvalId */
            lstUpdate: Array<UpdateHistoryDto>;
        }
        //ScreenJ
        export class UpdateHistoryDto{
            /**承認ID*/
            approvalId: string;
            /**履歴ID*/
            historyId: string;
            /**申請種類*/
            applicationType: number;
            /**就業ルート区分*/
            employRootAtr: number;
            constructor(approvalId: string, historyId: string, applicationType: number, employRootAtr: number){
                this.approvalId = approvalId;
                this.historyId = historyId; 
                this.applicationType = applicationType;
                this.employRootAtr = employRootAtr;
            }
        }
        //Param screen A,C,E
        export interface ParamDto {
            /**システム区分*/
            systemAtr: number;
            /**就業ルート区分: 会社(0)　－　職場(1)　－　社員(2)*/
            rootType: number;
            /**履歴ID*/
            workplaceId: string;
            /**社員ID*/
            employeeId: string;
            /**申請種類*/
            lstAppType: Array<number>;
            /**届出種類ID*/
            lstNoticeID: Array<string>;
            /**プログラムID(インベント)*/
            lstEventID: Array<string>;
        }
        //data screen A,C,E
        export class CommonApprovalRootDto{
            /**会社名*/
            companyName: string;
            workplaceId: string;
            employeeId: string;
            lstCompanyRoot: Array<CompanyAppRootDto>;
            lstWorkplaceRoot: Array<WorkPlaceAppRootDto>;
            lstPersonRoot: Array<PersonAppRootDto>;
        }
        //data screen A
        export class CompanyAppRootDto{
            company: ComApprovalRootDto;
            lstAppPhase: Array<ApprovalPhaseDto>;
        }
        export class DataTreeB{
            approvalId: string;
            nameAppType: string;
            lstbyApp: Array<DataTree>;
            constructor(approvalId: string, nameAppType: string,lstbyApp: Array<DataTree>){
                this.approvalId = approvalId;
                this.nameAppType = nameAppType;
                this.lstbyApp = lstbyApp;
            }
        }
        //data check list left view model B
        export class DataCheckModeB{
            approvalId: string;
            startDate: string;
            endDate: string;
            applicationType: number;
            employmentRootAtr: number;
            constructor(approvalId: string, startDate: string, endDate: string, 
                applicationType: number, employmentRootAtr: number){
                    this.approvalId = approvalId;
                    this.startDate = startDate;
                    this.endDate = endDate;
                    this.applicationType = applicationType;
                    this.employmentRootAtr = employmentRootAtr;
            }
        }
        export class DataTree{
            approvalId: string;
            nameAppType: string;
            employmentRootAtr: number;
            lstbyApp: Array<Com>;
            constructor(approvalId: string, nameAppType: string,employmentRootAtr: number,lstbyApp: Array<Com>){
                this.approvalId = approvalId;
                this.nameAppType = nameAppType;
                this.employmentRootAtr = employmentRootAtr;
                this.lstbyApp = lstbyApp;
            }
        }
        export class Com{
            approvalId: string;
            nameAppType: string;
            employmentRootAtr: number;
            constructor(approvalId: string, nameAppType: string, employmentRootAtr: number){
                this.nameAppType = nameAppType;
                this.approvalId = approvalId;
                this.employmentRootAtr = employmentRootAtr;
            }
        }
        //data display
        export class ComRootDto{
            name: string;
            company: ComApprovalRootDto;
            lstAppPhase: Array<ApprovalPhaseDto>;
            constructor(name: string, company: ComApprovalRootDto, lstAppPhase: Array<ApprovalPhaseDto>){
                this.name = name;
                this.company = company;
                this.lstAppPhase = lstAppPhase;
            }
        }
        //list display right
        export class CompanyAppRootADto{
            color: boolean;//true: data, false: not data
            employRootAtr: number;
            appTypeValue: number;
            appTypeName: string;
            approvalId: string;
            historyId: string;
            branchId: string;
            appPhase1: ApprovalPhaseDto;
            appPhase2: ApprovalPhaseDto;
            appPhase3: ApprovalPhaseDto;
            appPhase4: ApprovalPhaseDto;
            appPhase5: ApprovalPhaseDto;
            constructor(color: boolean, 
            employRootAtr: number,
            appTypeValue: number,
            appTypeName: string,
            approvalId: string,
            historyId: string,branchId: string,
            appPhase1: ApprovalPhaseDto, 
            appPhase2: ApprovalPhaseDto, 
            appPhase3: ApprovalPhaseDto,
            appPhase4: ApprovalPhaseDto, 
            appPhase5: ApprovalPhaseDto){
                this.color = color;
                this.employRootAtr = employRootAtr;
                this.appTypeValue = appTypeValue;
                this.appTypeName = appTypeName;
                this.approvalId =  approvalId;
                this.historyId = historyId;
                this.branchId = branchId;
                this.appPhase1 = appPhase1;
                this.appPhase2 = appPhase2;
                this.appPhase3 = appPhase3;
                this.appPhase4 = appPhase4;
                this.appPhase5 = appPhase5;
            }
        }
        //list check root < 14
        export class DataRootCheck{
            /**承認ID*/
            approvalId: string;
            /**履歴ID*/
            historyId: string;
            /**申請種類*/
            applicationType: number;
            /**就業ルート区分*/
            employmentRootAtr: number;
            branchId: string;
            lstAppPhase: Array<ApprovalPhaseDto>;
            constructor(approvalId: string, historyId: string,
                        applicationType: number, employmentRootAtr: number, branchId: string,
                        lstAppPhase: Array<ApprovalPhaseDto>){
                this.approvalId = approvalId;
                this.historyId = historyId;
                this.applicationType = applicationType;
                this.employmentRootAtr = employmentRootAtr;
                this.branchId =  branchId;
                this.lstAppPhase = lstAppPhase;
            }
        }
        //data screen C
        export class WorkPlaceAppRootDto{
            workplace: WpApprovalRootDto;
            lstAppPhase: Array<ApprovalPhaseDto>;
        }
        //data screen E
        export class PersonAppRootDto{
            person: PsApprovalRootDto;
            lstAppPhase: Array<ApprovalPhaseDto>;
        }
        export class ComApprovalRootDto{
            /**会社ID*/
            companyId: string;
            /**承認ID*/
            approvalId: string;
            /**履歴ID*/
            historyId: string;
            /**申請種類*/
            applicationType: number;
            /**開始日*/
            startDate: string;
            /**終了日*/
            endDate: string;
            /**分岐ID*/
            branchId: string;
            /**任意項目申請ID*/
            anyItemApplicationId: string;
            /**確認ルート種類*/
            confirmationRootType: number;
            /**就業ルート区分*/
            employmentRootAtr: number;
        }
        export class WpApprovalRootDto{
            /**会社ID*/
            companyId: string;
            /**承認ID*/
            approvalId: string;
            /**履歴ID*/
            workplaceId: string;
            /**履歴ID*/
            historyId: string;
            /**申請種類*/
            applicationType: number;
            /**開始日*/
            startDate: string;
            /**終了日*/
            endDate: string;
            /**分岐ID*/
            branchId: string;
            /**任意項目申請ID*/
            anyItemApplicationId: string;
            /**確認ルート種類*/
            confirmationRootType: number;
            /**就業ルート区分*/
            employmentRootAtr: number;
        }
        export class PsApprovalRootDto{
            /**会社ID*/
            companyId: string;
            /**承認ID*/
            approvalId: string;
            /**社員ID*/
            employeeId: string;
            /**履歴ID*/
            historyId: string;
            /**申請種類*/
            applicationType: number;
            /**開始日*/
            startDate: string;
            /**終了日*/
            endDate: string;
            /**分岐ID*/
            branchId: string;
            /**任意項目申請ID*/
            anyItemApplicationId: string;
            /**確認ルート種類*/
            confirmationRootType: number;
            /**就業ルート区分*/
            employmentRootAtr: number;
        }
        export class ApprovalPhaseDto{
            approver: Array<ApproverDto>;
            /**承認ID*/
            approvalId: string;
            /**承認形態*/
            approvalForm: number;
            /**承認形態 Name*/
            appFormName: string;
            /**閲覧フェーズ*/
            browsingPhase: number;
            /**順序*/
            phaseOrder: number;
            /**承認者指定区分*/
            approvalAtr: number;
            constructor(approver: Array<ApproverDto>, approvalId: string, approvalForm: number,
                appFormName: string, browsingPhase: number, phaseOrder: number, approvalAtr: number){
                this.approver = approver;
                this.approvalId = approvalId;
                this.approvalForm = approvalForm;
                this.appFormName = appFormName;
                this.browsingPhase = browsingPhase;
                this.phaseOrder = phaseOrder;
                this.approvalAtr = approvalAtr;
            }
        }
        export class ApproverDto{
            /**承認者Gコード*/
            jobGCD: string;
            /**社員ID*/
            employeeId: string;
            /**社員コード*/
            empCode: string;
            /**aapproverName*/
            name: string;
            /**順序*/
            approverOrder: number;
            /**区分*/
            approvalAtr: number;
            /**確定者*/
            confirmPerson: number;
            /**confirmPerson Name*/
            confirmName: string;
            /**特定職場ID*/
            specWkpId: string;
            constructor(jobGCD: string,
                employeeId: string, empCode: string, name: string, approverOrder: number, approvalAtr: number,
                confirmPerson: number, confirmName: string, specWkpId: string)
            {
                this.jobGCD = jobGCD;
                this.employeeId = employeeId;
                this.approverOrder = approverOrder;
                this.name = name;
                this.approvalAtr = approvalAtr;
                this.confirmPerson = confirmPerson;
                this.confirmName = confirmName;
                this.specWkpId = specWkpId;
                this.empCode = empCode;
            }
            
        }
        export class EmployeeKcp009{
                id: string;
                code: string;
                businessName: string;
                workplaceName: string;
                depName: string;
            constructor(id: string, code: string,
                businessName: string, workplaceName: string,depName: string)
            {
                this.id = id;
                this.code = code;
                this.businessName = businessName;
                this.workplaceName = workplaceName;
                this.depName = depName;
            }
            
        }
        export class ProcessHandler {
            /**
             * sort by list root
             */
            static orderByList(lstRoot: Array<CompanyAppRootADto>): Array<CompanyAppRootADto>{
                let result: Array<vmbase.CompanyAppRootADto> = [];
                let lstA: Array<vmbase.CompanyAppRootADto> = [];//common
                let lstB: Array<vmbase.CompanyAppRootADto> = [];//application
                let lstC: Array<vmbase.CompanyAppRootADto> = [];//confirmation
                let lstD: Array<vmbase.CompanyAppRootADto> = [];//anyItem
                _.each(lstRoot, function(obj){
                    if(obj.employRootAtr == 0){//common
                        lstA.push(obj);
                    }
                    if(obj.employRootAtr == 1){//application
                        lstB.push(obj);
                    }
                    if(obj.employRootAtr == 2){//confirmation
                        lstC.push(obj);
                    }
                    if(obj.employRootAtr == 3){//anyItem
                        lstD.push(obj);
                    }
                });
                let sortByA =  _.orderBy(lstA, ["appTypeValue"], ["asc"]);
                let sortByB =  _.orderBy(lstB, ["appTypeValue"], ["asc"]);
                let sortByC =  _.orderBy(lstC, ["appTypeValue"], ["asc"]);
                let sortByD =  _.orderBy(lstD, ["appTypeValue"], ["asc"]);
                //push list A (common)
                _.each(sortByA, function(obj){
                    result.push(obj);
                });
                //push list B (application)
                _.each(sortByB, function(obj){
                    result.push(obj);
                });
                //push list C (confirmation)
                _.each(sortByC, function(obj){
                    result.push(obj);
                });
                //push list D (anyItem)
                _.each(sortByD, function(obj){
                    result.push(obj);
                });
                return result;
            }
             /**
             * find appType by value
             */
            static findAppbyValue(applicationType: number, employRootAtr: number, lstNameAppType: Array<ApplicationType>): ApplicationType{
                let self = this;
                return _.find(lstNameAppType, function(obj: ApplicationType) {
                    return obj.value == applicationType && obj.employRootAtr == employRootAtr;
                });
            }
            
            /**
             * check input date before or equal date
             */
            static validateDateInput(inputDate: string, date: string){
                return moment(inputDate).isSameOrAfter(moment(date));
            }
            /**
             * check appType is exist in list root ?
             */
            static checkExist(lstRoot: Array<CompanyAppRootADto>,appType: number, employRootAtr: number): boolean{
                let check = false;
                _.each(lstRoot, function(root){
                    if(root.appTypeValue == appType && root.employRootAtr == employRootAtr){
                        check = true;
                        return true;
                    }
                });
                if(!check){
                    return false;
                }else{
                    return true;    
                }
                
            }
            //承認単位の設定を取得
            static getSettingApprovalUnit(sysAtr: number): JQueryPromise<any>{
                let dfd = $.Deferred();
                //会社単位の表示区分　＝　False、職場単位の表示区分　＝　False、社員単位の表示区分　＝False
                let setUnit: ApprDisSet = {
                    companyUnit: 0,
                    workplaceUnit: 0,
                    employeeUnit: 0
                }
                if(sysAtr == 0){//SHUUGYOU
                    servicebase.settingCas005().done(function(cas005){
                        //就業ロールを取得
                        let empRoleSet = cas005;
                        let result = null;
                        servicebase.settingKaf022().done(function(kaf022){
                            let appSet = kaf022;       
                    
                            let empAgent = empRoleSet.employeeRefSpecAgent;
                    //       0: 全員(ALL)
                    //       1: 社員参照範囲と同じ(ALL_EMPLOYEE_REF_RANGE)
                            //取得した「承認者・代行者指定時社員参照」をチェック
                            if(empAgent == 0){//「全社員」の場合
                                dfd.resolve(appSet);
                            } 
                            //「社員参照範囲と同じ」の場合
                            //取得した「社員参照範囲」をチェック
                            let empRef = empRoleSet.presentInqEmployeeRef;
                            //0: 全社員 ALL_EMPLOYEE
                            //1: 部門（配下含む） DEPARTMENT_AND_CHILD
                            //2: 部門（配下含まない） DEPARTMENT_ONLY
                            //3: 自分のみ ONLY_MYSELF
                            if(empRef == 0){//「全社員」の場合
                                dfd.resolve(appSet);
                            }else if(empRef == 3){//「自分のみ」の場合
                                //社員単位の表示区分　＝　申請承認設定．承認者の登録設定．社員単位の表示区分
                                setUnit.employeeUnit = appSet.employeeUnit;
                            }else {
                                //職場単位の表示区分　＝　申請承認設定．承認者の登録設定．職場単位の表示区分
                                setUnit.workplaceUnit = appSet.workplaceUnit;
                                //社員単位の表示区分　＝　申請承認設定．承認者の登録設定．社員単位の表示区分
                                setUnit.employeeUnit = appSet.employeeUnit;
                            }
    //                        result = this.checkDis(setUnit);
                            dfd.resolve(setUnit);
                            
                        });
                        
                    });
                }else{//JINJI
                    servicebase.setDisHR().done(function(disHr){
                        //COM_MODE
                        setUnit.companyUnit = disHr.comMode;
                        //DEV_MODE
                        setUnit.workplaceUnit = disHr.devMode;
                        //EMP_MODE
                        setUnit.employeeUnit = disHr.empMode;
                        dfd.resolve(setUnit);
                    });
                }
                return dfd.promise();
            }
            //単位の表示区分をチェック
            static checkDis(setUnit): any{
               if(setUnit.companyUnit==0 && setUnit.workplaceUnit==0 && setUnit.employeeUnit==0){
                        //エラーメッセージ「Msg_1607」を表示
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_1607" }).then(() => {
                            //トップページを戻す
                            nts.uk.request.jump("/view/ccg/008/a/index.xhtml");
                        });
                   return;
                    }
                return setUnit
            }
        }
        
        export class ApproverDtoK{
            id: string;
            code: string;
            name: string;
            /**区分  0: 個人(person) - 1: 職位(jobTitle)*/
            approvalAtr: number;
            dispOrder: number;
            constructor(id: string, code: string, name: string , approvalAtr: number,dispOrder: number){
                this.id = id;
                this.code = code;
                this.name = name;    
                this.approvalAtr = approvalAtr;
                this.dispOrder = dispOrder;
            }
        } 
        //__________KCP009_________
        export interface ComponentOption {
            systemReference: SystemType;
            isDisplayOrganizationName: boolean;
            employeeInputList: KnockoutObservableArray<EmployeeKcp009>;
            targetBtnText: string;
            selectedItem: KnockoutObservable<string>;
            tabIndex: number;
        }
        export interface EmployeeModel {
            id: string;
            code: string;
            businessName: string;
            depName?: string;
            workplaceName?: string;
        }
        export class SystemType {
            static EMPLOYMENT = 1;
            static SALARY = 2;
            static PERSONNEL = 3;
            static ACCOUNTING = 4;
            static OH = 6;
        }
        export interface GroupOption {
             /** Common properties */
                showEmployeeSelection: boolean; // 検索タイプ
                systemType: number; // システム区分
                showQuickSearchTab: boolean; // クイック検索
                showAdvancedSearchTab: boolean; // 詳細検索
                showBaseDate: boolean; // 基準日利用
                showClosure: boolean; // 就業締め日利用
                showAllClosure: boolean; // 全締め表示
                showPeriod: boolean; // 対象期間利用
                periodFormatYM: boolean; // 対象期間精度

                /** Required parameter */
                baseDate?: string; // 基準日
                periodStartDate?: string; // 対象期間開始日
                periodEndDate?: string; // 対象期間終了日
                inService: boolean; // 在職区分
                leaveOfAbsence: boolean; // 休職区分
                closed: boolean; // 休業区分
                retirement: boolean; // 退職区分

                /** Quick search tab options */
                showAllReferableEmployee: boolean; // 参照可能な社員すべて
                showOnlyMe: boolean; // 自分だけ
                showSameWorkplace: boolean; // 同じ職場の社員
                showSameWorkplaceAndChild: boolean; // 同じ職場とその配下の社員

                /** Advanced search properties */
                showEmployment: boolean; // 雇用条件
                showWorkplace: boolean; // 職場条件
                showClassification: boolean; // 分類条件
                showJobTitle: boolean; // 職位条件
                showWorktype: boolean; // 勤種条件
                isMutipleCheck: boolean; // 選択モード
                // showDepartment: boolean; // 部門条件 not covered
                // showDelivery: boolean; not covered

                /** Data returned */
                returnDataFromCcg001: (data: Ccg001ReturnedData) => void;
            }

            export interface Ccg001ReturnedData {
                baseDate: string; // 基準日
                closureId?: number; // 締めID
                periodStart: string; // 対象期間（開始)
                periodEnd: string; // 対象期間（終了）
                listEmployee: Array<EmployeeSearchDto>; // 検索結果
            }
        export interface EmployeeSearchDto {
            employeeId: string;
            
            employeeCode: string;
            
            employeeName: string;
            
            workplaceName: string;
        }

        export interface Root {
            typeRoot: string;
            appName:string;
            phase1:string;
            phase2:string;
            phase3:string;
            phase4:string;
            phase5:string;
            deleteRoot: string;    
        }
        export class CellState {
            rowId: number;
            columnKey: string;
            state: Array<any>
            constructor(rowId: any, columnKey: string, state: Array<any>) {
                this.rowId = rowId;
                this.columnKey = columnKey;
                this.state = state;
            }
        }
        export enum RootType {
            COMPANY = 0,
            WORKPLACE = 1,
            PERSON = 2
        }
        export enum ApprovalForm {
            /** 全員承認*/
            EVERYONE_APPROVED = 1,
            /** 誰か一人*/
            SINGLE_APPROVED = 2
        }
        
        export enum SystemAtr {
             /**就業*/
            WORK = 0,
            /**人事*/
            HUMAN_RESOURCES = 1
        }
        export enum MODE {
            /** まとめて設定モード(0)*/
            MATOME = 0,
            /**申請個別設定モード(1)*/
            SHINSEI = 1
        }
        export interface ApprDisSet{
            //会社単位
            companyUnit: number;
            //職場単位
            workplaceUnit: number;
            //社員単位
            employeeUnit: number; 
        }
    }
}