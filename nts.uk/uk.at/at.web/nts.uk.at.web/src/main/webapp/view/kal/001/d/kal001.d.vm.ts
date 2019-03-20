module nts.uk.com.view.kal001.d.viewmodel {

    import close = nts.uk.ui.windows.close;
    import getText = nts.uk.resource.getText;
    import alertError = nts.uk.ui.dialog.alertError;
    import service = nts.uk.at.view.kal001.d.service;
    import block = nts.uk.ui.block;

    export class ScreenModel {
        // dialog mode
        dialogMode: KnockoutObservable<number>;
        isInterrupt: KnockoutObservable<boolean>;
        isExtracting: KnockoutObservable<boolean>;
        extractingFlg: KnockoutObservable<boolean>;

        // interval 1000ms request to server
        interval: any;
        
        // time when start process
        
        timeStart: any;
        timeEnd: any;
        timeNow: any;
        timeProcess: KnockoutObservable<string>;
        // D1_12
        timeStartStr: KnockoutObservable<string>;

        numberEmpSuccess: KnockoutObservable<number>;
        totalEmployees: KnockoutObservable<number>;
        employeeErrors: KnockoutObservableArray<String>;
        strPersion: KnockoutObservable<String> = ko.observable("人");
        strSlash: KnockoutObservable<String> = ko.observable("/");
        strFinish: KnockoutObservable<String> = ko.observable("完了");
        displayStrFinish: KnockoutObservable<boolean> = ko.observable(false);

        currentAlarmCode: KnockoutObservable<string>;
        listSelectedEmpployee: Array<UnitModel>;
        listPeriodByCategory: KnockoutObservableArray<PeriodByCategory>;
        selectedEmpployee: Array<UnitModel>;
        listAlarmExtraValueWkReDto: KnockoutObservableArray<AlarmExtraValueWkReDto>;
        countLoop: number = 0;
        taskId : KnockoutObservable<string> = ko.observable("");
        processExtraId : KnockoutObservable<string>;

        constructor() {
            var self = this;
            var params = nts.uk.ui.windows.getShared("KAL001_A_PARAMS");
            self.timeStart = new Date();
            self.timeEnd = ko.observable("");
            self.timeProcess = ko.observable("");
            self.timeStartStr = ko.observable("");
            self.totalEmployees = params.totalEmpProcess;
            self.numberEmpSuccess = ko.observable(0);
            self.dialogMode = ko.observable(AlarmExtraStatus.PROCESSING);
            
            self.timeStartStr = ko.observable(moment(new Date()).format("YYYY/MM/DD H:mm"));

            self.currentAlarmCode = params.currentAlarmCode;
            self.listSelectedEmpployee = params.listSelectedEmpployee;
            self.listPeriodByCategory = params.listPeriodByCategory;
            self.listAlarmExtraValueWkReDto = ko.observableArray([]);
            self.employeeErrors = ko.observableArray([]);
            self.isInterrupt = ko.observable(false);
            self.extractingFlg = ko.observable(false);
            self.isExtracting = ko.observable(false);
            self.processExtraId = ko.observable("");
            //process alam list 
            //code process screen A
            let start = performance.now();
            block.invisible();
            service.isExtracting().done((isExtracting: boolean) => {
                if (isExtracting) {
                    self.extractingFlg = ko.observable(isExtracting);
                    nts.uk.ui.dialog.info({ messageId: "Msg_993" }).then(function() {
                        nts.uk.ui.windows.close();
                    });
                    block.clear();
                    return;
                }
                service.extractStarting().done((statusId: string) => {
                    console.log("time service 2  : "+(performance.now() -start).toString());
                    self.processExtraId(statusId);
                    service.extractAlarm(self.taskId, self.numberEmpSuccess, statusId, self.listSelectedEmpployee, self.currentAlarmCode, self.listPeriodByCategory).done((dataExtractAlarm: service.ExtractedAlarmDto)=>{
                        for(let i = 0;i<dataExtractAlarm.extractedAlarmData.length;i++){
                            if(dataExtractAlarm.extractedAlarmData[i].workplaceID == null){
                                for(let j = 0;j<self.listSelectedEmpployee.length;j++){
                                   if( dataExtractAlarm.extractedAlarmData[i].employeeID == self.listSelectedEmpployee[j].id){
                                       dataExtractAlarm.extractedAlarmData[i].workplaceID = self.listSelectedEmpployee[j].workplaceId;
                                       break;
                                   }
                                }
                            }
                        }
                        console.log("time service 3  : "+(performance.now() -start).toString());
                        let status =  dataExtractAlarm.extracting == true ? AlarmExtraStatus.END_ABNORMAL : AlarmExtraStatus.END_NORMAL;
                        // Update status into domain (ドメインモデル「アラームリスト抽出処理状況」を更新する)
                        let extraParams = {
                            processStatusId: statusId,
                            status: status
                        };
                        service.extractFinished(extraParams);
                        if(dataExtractAlarm.extracting) {
                           self.isExtracting = ko.observable(dataExtractAlarm.isExtracting);
                        } 
                        if (!dataExtractAlarm.nullData) {
                            self.listAlarmExtraValueWkReDto = dataExtractAlarm.extractedAlarmData;
                        }
                        self.dialogMode(status);
                        self.setFinished();
                    }).fail((errorExtractAlarm)=>{
                        alertError(errorExtractAlarm);
                    }).always(()=>{
                        block.clear();    
                    });
                }).fail((errorExtractAlarm) => {
                    block.clear();
                    alertError(errorExtractAlarm);
                }).always(() => {
                    block.clear();
                });
            }).fail((errorExtractAlarm) => {
                block.clear();
                alertError(errorExtractAlarm);
            }).always(() => {
                block.clear();
            });
        }
        
         start(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred();
            // Management deletion monitoring process 
            self.interval = setInterval(self.countTime, 1000, self);
            $("#F10_2").focus();
            dfd.resolve();
            return dfd.promise();
        }
        
         public countTime(self): void {
            // F2_1_2 set time over 
            let timeNow = new Date();
            let over = (timeNow.getSeconds()+timeNow.getMinutes()*60+ timeNow.getHours()*60*60) - (self.timeStart.getSeconds()+self.timeStart.getMinutes()*60+ self.timeStart.getHours()*60*60); 
            let time = new Date(null);
            time.setSeconds(over); // setting value for SECONDS here
            let result = time.toISOString().substr(11, 8);
            self.timeProcess(result);
        }
 
        // process when click button interrupt
        public interrupt(): void {
            let self = this;
            nts.uk.ui.dialog.confirm({ messageId: "Msg_1412" })
                .ifYes(() => {
                    nts.uk.request.asyncTask.requestToCancel(self.taskId());
                    // Update status into domain (ドメインモデル「アラームリスト抽出処理状況」を更新する)
                    let extraParams = {
                        processStatusId: self.processExtraId(),
                        status: AlarmExtraStatus.INTERRUPT
                    };
                    service.extractFinished(extraParams);
                    self.dialogMode(AlarmExtraStatus.INTERRUPT);
                    self.setFinished();
                })
                .ifNo(() => {
                    return;
                });
        }

        // set time now
        public setFinished(): void {
            let self = this;
            if (self.numberEmpSuccess() == self.totalEmployees) {
                self.displayStrFinish(true);
            }
            window.clearInterval(self.interval);
            self.timeEnd(moment(new Date()).format("YYYY/MM/DD H:mm"));
        }
        //  Process when click button Confirm process 
        confirmProcess() {
            let self = this;
            let params = {
                extractingFlg : self.extractingFlg(),
                isExtracting : self.isExtracting(),
                listAlarmExtraValueWkReDto : self.listAlarmExtraValueWkReDto,
                currentAlarmCode : self.currentAlarmCode
            };
            nts.uk.ui.windows.setShared("KAL001_D_PARAMS",params);
            close();
        }
        //  process when click button closePopup 
        closePopup() {
            close();
        }
    }

    export class AlarmExtraValueWkReDto {
        guid: string;
        workplaceID: string;
        hierarchyCd: string;
        workplaceName: string;
        employeeID: string;
        employeeCode: string;
        employeeName: string;
        alarmValueDate: string;
        category: number;
        categoryName: string;
        alarmItem: string;
        alarmValueMessage: string;
        comment: string;
    }

    export class DateValue {
        startDate: string;
        endDate: string;
        constructor(startDate: string, endDate: string) {
            this.startDate = (startDate);
            this.endDate = (endDate);
        }
    }

    export class PeriodByCategory {
        ategoryName: string;
        startDate: string;
        endDate: string;
        checkBox: boolean;
        typeInput: string;
        required: boolean;
        year: number;
        visible: boolean;
        id: number;
    }



    // employee list component
    export class ListType {
        static EMPLOYMENT = 1;
        static Classification = 2;
        static JOB_TITLE = 3;
        static EMPLOYEE = 4;
    }

    export interface UnitModel {
        id: string;
        code: string;
        name?: string;
        workplaceCode?: string;
        workplaceId?: string;
        workplaceName?: string;
        isAlreadySetting?: boolean;
    }
    export class UnitModelDto implements UnitModel {
        id: string;
        code: string;
        name: string;
        workplaceCode: string;
        workplaceId: string;
        workplaceName: string;
        isAlreadySetting: boolean;

        constructor(employee: EmployeeSearchDto) {
            this.id = employee.employeeId;
            this.code = employee.employeeCode;
            this.name = employee.employeeName;
            this.workplaceId = employee.workplaceId;
            this.workplaceCode = employee.workplaceCode;
            this.workplaceName = employee.workplaceName;
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


    // search component
    export interface EmployeeSearchDto {
        employeeId: string;
        employeeCode: string;
        employeeName: string;
        workplaceCode: string;
        workplaceId: string;
        workplaceName: string;
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
    export enum AlarmExtraStatus {
        END_NORMAL = 0,   /**正常終了*/
        END_ABNORMAL = 1, /**異常終了*/
        PROCESSING = 2,   /**処理中*/
        INTERRUPT = 3,    /**中断*/
    }
}






