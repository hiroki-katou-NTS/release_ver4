module nts.uk.at.view.kdw001.f {
    import getText = nts.uk.resource.getText;
    export module viewmodel {
        export class ScreenModel {
            nameClosure : string;
            enable: KnockoutObservable<boolean>;
            required: KnockoutObservable<boolean>;
            dateValue: KnockoutObservable<any>;
            startDateString: KnockoutObservable<any>;
            endDateString: KnockoutObservable<any>;
            //table
            columns: Array<any>;//nts.uk.ui.NtsGridListColumn
            currentSelectedRow: KnockoutObservable<any>;
    
            //InputEmpCalAndSumByDate
            inputEmpCalAndSumByDate: KnockoutObservable<model.InputEmpCalAndSumByDate>;
            //list obj EmpCalAndSumExeLog 
            empCalAndSumExeLog: KnockoutObservableArray<model.EmpCalAndSumExeLog>;
            //list caseSpecExeContent
            listCaseSpecExeContent : KnockoutObservableArray<model.CaseSpecExeContent>;
            //listClosure
            listClosure : KnockoutObservableArray<any>;

            constructor() {
                let self = this;
                self.nameClosure = " test";
                //
                self.enable = ko.observable(true);
                self.required = ko.observable(true);
                self.dateValue = ko.observable({});
                self.dateValue().startDate = moment.utc().subtract(1, "y").add(1, "d").format("YYYY/MM/DD");
                self.dateValue().endDate = moment.utc().format("YYYY/MM/DD");
                self.startDateString = ko.observable('');
                self.endDateString = ko.observable(new Date());
                //table
                self.currentSelectedRow = ko.observable(null);

                //inputEmpCalAndSumByDate (startDate and endDate)
                self.inputEmpCalAndSumByDate = ko.observable(
                    new model.InputEmpCalAndSumByDate(self.dateValue().startDate, self.dateValue().endDate));
                // list obj EmpCalAndSumExeLog
                self.empCalAndSumExeLog = ko.observableArray([]);
                //list obj CaseSpecExeContent
                self.listCaseSpecExeContent =  ko.observableArray([]);
                //list obj listClosure
                self.listClosure =  ko.observableArray([]);
                

                self.columns = [
                    { headerText: getText('KDW001_73'), key: 'executionDate', width: 100 },
                    { headerText: getText('KDW001_74'), key: 'empCalAndSumExecLogID', width: 120 },
                    { headerText: getText('KDW001_75'), key: 'caseSpecExeContentID', width: 100 },
                    { headerText: getText('KDW001_76'), key: 'processingMonthName', width: 150 },
                    { headerText: getText('KDW001_77'), key: 'executedMenuName', width: 200 },
                    { headerText: getText('KDW001_78'), key: 'executionStatusName', width: 160 },
                    {
                        headerText: getText('KDW001_79'), key: 'executionStatus', width: 100,
                        template: '<button class="open-dialog-i" data-id="${empCalAndSumExecLogID}">参照</button>',
                        columnCssClass: "colStyleButton",
                    }
                ];
            }

            /**
             * functiton start page
             */
            startPage(): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred();
                // get all CaseSpecExeContent
                let dfdAllCaseSpecExeContent = self.getAllCaseSpecExeContent();
                let dfdAllClosure = self.getAllClosure();
                $.when(dfdAllCaseSpecExeContent,dfdAllClosure).done((dfdAllCaseSpecExeContentData,dfdAllClosureData) => {
                     //get all EmpCalAndSumExeLog by date
                    self.getAllEmpCalAndSumExeLog(self.inputEmpCalAndSumByDate());
                    dfd.resolve();
                });

                return dfd.promise();
            }//end start page

            /**
             * function get all EmpCalAndSumExeLog by startDate and endDate
             */
            getAllEmpCalAndSumExeLog(inputEmpCalAndSumByDate: model.InputEmpCalAndSumByDate) {
                let self = this;
                let dfd = $.Deferred<any>();
                service.getAllEmpCalAndSumExeLog(inputEmpCalAndSumByDate).done(function(data: Array<model.IEmpCalAndSumExeLog>) {
                    //_.sortBy(self.empCalAndSumExeLog(data), 'executionDate');
                    data = _.orderBy(data, ['executionDate'], ['desc']);
                    let temp = [];
                    _.each(data, (value) => {
                        
                        let item = new model.EmpCalAndSumExeLog(value);
                        //executedMenuName
                        if( item.executedMenu == 1) {
                            item.executedMenuName = _.find(self.listCaseSpecExeContent(), function(o) { 
                                return o.caseSpecExeContentID == item.caseSpecExeContentID; }).useCaseName ;  
                        }
                        // get name closure by date
                        _.find(self.listClosure(), function(o) { 
                            if (o.closureId == item.closureID) {
                                item.changeName(_.find(o.listClosureHistoryForLog, (his: any) => {
                                    return item.processingMonth >= his.startYearMonth && item.processingMonth <= his.endYearMonth;
                                }).closureName);
                            }
                        });
                        temp.push(item);
                    });
                    
                    self.empCalAndSumExeLog(temp);
                    dfd.resolve(data);
                }).fail(function(res: any) {
                    dfd.reject();
                    nts.uk.ui.dialog.alertError(res.message).then(function() { nts.uk.ui.block.clear(); });
                });
                return dfd.promise();
            }//end function getAllEmpCalAndSumExeLog
            
            /**
             * function get all caseSpecExeContent
             */
            getAllCaseSpecExeContent(){
                let self = this;
                let dfd = $.Deferred<any>();
                service.getAllCaseSpecExeContent().done(function(data){
                    self.listCaseSpecExeContent(data);
                    dfd.resolve(data);
                }).fail(function(res: any) {
                    dfd.reject();
                    nts.uk.ui.dialog.alertError(res.message).then(function() { nts.uk.ui.block.clear(); });
                });
                return dfd.promise();
            }
            
            
            
            /**
             * get caseSpecExeContent by id
             */
            getCaseSpecExeContent(caseSpecExeContentID:string){
                let self = this;
                let dfd = $.Deferred<any>();
                service.getCaseSpecExeContentById(caseSpecExeContentID).done(function(data){
                    dfd.resolve(data);
                }).fail(function(res: any) {
                    dfd.reject();
                    nts.uk.ui.dialog.alertError(res.message).then(function() { nts.uk.ui.block.clear(); });
                });
                return dfd.promise();
            }
            
            /**
             * get all Closure
             */
            getAllClosure(){
                let self = this;
                let dfd = $.Deferred<any>();
                service.getAllClosure().done(function(data){
                     self.listClosure(data);
                    dfd.resolve(data);
                }).fail(function(res: any) {
                    dfd.reject();
                    nts.uk.ui.dialog.alertError(res.message).then(function() { nts.uk.ui.block.clear(); });
                });
                return dfd.promise();
            }
            

            //button search
            search() {
                let self = this;
                self.inputEmpCalAndSumByDate(new model.InputEmpCalAndSumByDate(self.dateValue().startDate, self.dateValue().endDate));
                self.getAllEmpCalAndSumExeLog(self.inputEmpCalAndSumByDate());
            }

            //open dialog I
            openDialogI() {
                let self = this;
                var param = {
                    nameClosure : self.nameClosure,
                    empCalAndSumExecLogID : this.currentSelectedRow()
                    
                };
                nts.uk.ui.windows.setShared("openI", param);
                nts.uk.ui.windows.sub.modal("/view/kdw/001/i/index.xhtml");
            }
            
            openScreenA() {
                nts.uk.request.jump("/view/kdw/001/a/index.xhtml");
            }
            
        }//end screenModel
    }//end viewmodel

    //module model
    export module model {
        export interface IEmpCalAndSumExeLog {
            empCalAndSumExecLogID: string;
            processingMonth: number;
            processingMonthName: string;
            executedMenu: number;
            executedMenuName: string;
            executedMenuJapan: string;
            executionDate: string;
            executionStatus: number;
            executionStatusName : string;
            employeeID: string;
            closureID: number;
            closureName : string;
            caseSpecExeContentID: string;
            executionLogs: Array<IExecutionLog>;
        }

        export interface IExecutionLog {
            empCalAndSumExecLogID: string;
            executionContent: number;
            executionContentName :string;
            existenceError: number;
            executionTime: ExecutionTime;
            processStatus: number;
            objectPeriod: ObjectPeriod;
            calExecutionSetInfoID :string;
            reflectApprovalSetInfo : SetInforReflAprResult;
            dailyCreationSetInfo : SettingInforForDailyCreation;
            dailyCalSetInfo : CalExeSettingInfor;
            numberPersonErr : number;
        }

        export interface IExecutionTime {
            startTime: string;
            endTime: string;
        }

        /**
         * class EmpCalAndSumExeLog
         */
        export class EmpCalAndSumExeLog {
            empCalAndSumExecLogID: string;
            processingMonth: number;
            processingMonthName: string;
            executedMenu: number;
            executedMenuName: string;
            executedMenuJapan: string;
            executionDate: string;
            executionStatus: number;
            executionStatusName : string;
            employeeID: string;
            closureID: number;
            closureName : string;
            caseSpecExeContentID: string;
            executionLogs: Array<ExecutionLog>;
            constructor(data: IEmpCalAndSumExeLog) {
                this.empCalAndSumExecLogID = data.empCalAndSumExecLogID;
                this.processingMonth = data.processingMonth;
                this.processingMonthName = data.processingMonth%100 + "月度" + data.closureName;
                this.executedMenu = data.executedMenu;
                if (data.executedMenu == 0) {
                    this.executedMenuName = "詳細実行";
                }

                if (data.executedMenu == 0) {
                    this.executedMenuJapan = "選択して実行";
                } else {
                    this.executedMenuJapan = "ケース別実行";
                }
                this.executionDate = data.executionDate;
                this.executionStatus = data.executionStatus;
                this.executionStatusName = data.executionStatusName;
                this.employeeID = data.employeeID;
                this.closureID = data.closureID;
                this.closureName = data.closureName;
                this.caseSpecExeContentID = data.caseSpecExeContentID;
                this.executionLogs = data.executionLogs;
            }
            
            public changeName(name: string): void {
                this.closureName = name;
                this.processingMonthName = this.processingMonth%100 + "月度     " + name;
            }
        }//end class EmpCalAndSumExeLog

        /**
         * class ExecutionLog
         */
        export class ExecutionLog {
            empCalAndSumExecLogID: string;
            executionContent: number;
            executionContentName :string;
            existenceError: number;
            executionTime: ExecutionTime;
            processStatus: number;
            objectPeriod: ObjectPeriod;
            calExecutionSetInfoID :string;
            reflectApprovalSetInfo : SetInforReflAprResult;
            dailyCreationSetInfo : SettingInforForDailyCreation;
            dailyCalSetInfo : CalExeSettingInfor;
            monlyAggregationSetInfo : CalExeSettingInfor;
            numberPersonErr : number;
            constructor(data: IExecutionLog) {
                this.empCalAndSumExecLogID = data.empCalAndSumExecLogID;
                this.executionContent = data.executionContent;
                this.executionContentName = data.executionContentName;   
                this.existenceError = data.existenceError;
                this.executionTime = new ExecutionTime(data.executionTime);
                this.processStatus = data.processStatus;
                this.objectPeriod = data.objectPeriod;
                this.calExecutionSetInfoID = data.calExecutionSetInfoID;
                this.reflectApprovalSetInfo = data.reflectApprovalSetInfo;
                this.dailyCreationSetInfo = data.dailyCreationSetInfo;
                this.dailyCalSetInfo = data.dailyCalSetInfo;
                this.monlyAggregationSetInfo = data.monlyAggregationSetInfo;
                this.numberPersonErr = data.numberPersonErr;
            }
        }//end class ExecutionLog
        
        /**
         * class SetInforReflAprResult 
         */
        export class SetInforReflAprResult{
            executionType: number;
            executionTypeName : string;
            forciblyReflect : boolean;
            constructor(executionType: number,executionTypeName : string,forciblyReflect : boolean){
                this.executionType = executionType;
                this.executionTypeName = executionTypeName;
                this.forciblyReflect = forciblyReflect;    
            }
        }//end classSetInforReflAprResult
        
        /**
         * class SettingInforForDailyCreation
         */
        export class SettingInforForDailyCreation{
            executionType: number;
            executionTypeName : string;
            creationType : number;
            partResetClassification : PartResetClassification;
            constructor(executionType: number,executionTypeName : string,creationType : number,partResetClassification : PartResetClassification){
                this.executionType = executionType;
                this.executionTypeName = executionTypeName; 
                this.creationType = creationType; 
                this.partResetClassification = partResetClassification; 
            }
            
        }//end class SettingInforForDailyCreation
        
        /**
         * class PartResetClassification
         */
        export class PartResetClassification{
            //マスタ再設定
            masterReconfiguration : boolean;
            //休業再設定
            closedHolidays : boolean ;
            // 就業時間帯再設定
            resettingWorkingHours : boolean ;
            // 打刻のみ再度反映
            reflectsTheNumberOfFingerprintChecks : boolean ;
            // 特定日区分再設定
            specificDateClassificationResetting :  boolean ;
            // 申し送り時間再設定
            resetTimeAssignment :  boolean ;
            // 育児・介護短時間再設定
            resetTimeChildOrNurseCare : boolean ;
            // 計算区分再設定
            calculationClassificationResetting : boolean ;
            constructor(
                masterReconfiguration : boolean,
                closedHolidays : boolean,
                resettingWorkingHours : boolean ,
                reflectsTheNumberOfFingerprintChecks : boolean ,
                specificDateClassificationResetting :  boolean ,
                resetTimeAssignment :  boolean,
                resetTimeChildOrNurseCare : boolean,
                calculationClassificationResetting : boolean ){
                
                this.masterReconfiguration = masterReconfiguration;
                this.closedHolidays = closedHolidays;
                this.resettingWorkingHours = resettingWorkingHours;
                this.reflectsTheNumberOfFingerprintChecks = reflectsTheNumberOfFingerprintChecks;
                this.specificDateClassificationResetting = specificDateClassificationResetting;
                this.resetTimeAssignment = resetTimeAssignment;
                this.resetTimeChildOrNurseCare = resetTimeChildOrNurseCare;
                this.calculationClassificationResetting = calculationClassificationResetting;
                
            }
            
            
        }//end class PartResetClassification
        
        /**
         * class ExecutionTime
         */
        export class ExecutionTime {
            startTime: string;
            endTime: string;
            timeSpan: string;
            constructor(data: IExecutionTime) {
                var momentStart = moment.utc(data.startTime, "YYYY/MM/DD HH:mm:ss");
                var momentEnd = moment.utc(data.endTime, "YYYY/MM/DD HH:mm:ss");
                
                this.startTime = momentStart.format("YYYY/MM/DD HH:mm:ss");
                this.endTime = momentEnd.format("YYYY/MM/DD HH:mm:ss");
                var ms = moment(momentEnd,"DD/MM/YYYY HH:mm:ss").diff(moment(momentStart,"DD/MM/YYYY HH:mm:ss"));
                var d = moment.duration(ms);
                this.timeSpan = Math.floor(d.asHours()) + moment.utc(ms).format(":mm:ss");
            }
        }//end class ExecutionTime

        /**
         * class CalExeSettingInfor
         */
        export class CalExeSettingInfor {
            executionContent: number;
            executionType: number;
            executionTypeName : string;
            calExecutionSetInfoID :  string;
            caseSpecExeContentID : string ;
            constructor(executionContent: number, executionType: number,executionTypeName : string,
            calExecutionSetInfoID :  string,
            caseSpecExeContentID : string ) {
                this.executionContent = executionContent;
                this.executionType = executionType;
                this.executionTypeName = executionTypeName;
                this.calExecutionSetInfoID = calExecutionSetInfoID;
                this.caseSpecExeContentID = caseSpecExeContentID;
            }//end class ExecutionTime
        }//end class CalExeSettingInfor

        /**
         * class ObjectPeriod
         */
        export class ObjectPeriod {
            startDate: string;
            endDate: string;
            constructor(startDate: string, endDate: string) {
                this.startDate = moment.utc(startDate, "YYYY/MM/DD").toISOString();
                this.endDate = moment.utc(endDate, "YYYY/MM/DD").toISOString();
            }
        }//end class ObjectPeriod

        /**
         * class InputEmpCalAndSumByDate 
         */
        export class InputEmpCalAndSumByDate {
            startDate: string;
            endDate: string;
            constructor(startDate: string, endDate: string) {
                this.startDate = moment.utc(startDate, "YYYY/MM/DD").toISOString();
                this.endDate = moment.utc(endDate, "YYYY/MM/DD").toISOString();
            }
        }//end class InputEmpCalAndSumByDate
        
        /**
         * class CaseSpecExeContent
         */
        export class CaseSpecExeContent{
            caseSpecExeContentID :string;
            orderNumber : number;
            useCaseName :string;
            constructor (caseSpecExeContentID :string,orderNumber : number,useCaseName :string){
                this.caseSpecExeContentID =caseSpecExeContentID ;
                this.orderNumber = orderNumber;
                this.useCaseName = useCaseName;
                
            }
        }//end class CaseSpecExeContent
        

    }//end module model

}//end module