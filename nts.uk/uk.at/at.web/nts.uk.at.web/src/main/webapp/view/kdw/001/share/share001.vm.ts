module nts.uk.at.view.kdw001 {
    export module share.model {
                
        // ErrMessageInfoDto
        export interface PersonInfoErrMessageLogDto {
            personCode: string;
            personName: string;
            disposalDay: string;
            messageError: number;
        }
        
        export class PersonInfoErrMessageLog{
            GUID : string ;
            personCode: string;
            personName: string;
            disposalDay: string;
            messageError: number;
            constructor(data : PersonInfoErrMessageLogDto){
                this.GUID = nts.uk.util.randomId();
                this.personCode = data.personCode;
                this.personName = data.personName;
                this.disposalDay = data.disposalDay;
                this.messageError = data.messageError;
            }
        }
            
        
        // CheckProcessCommand
        export interface CheckProcessCommand {
            empCalAndSumExecLogID: string;
            periodStartDate: string;
            periodEndDate: string;
        }

        // AddEmpCalSumAndTargetCommandResult
        export interface AddEmpCalSumAndTargetCommandResult {
            enumComboBox: Array<any>;
            empCalAndSumExecLogID: string;
            periodStartDate: string;
            periodEndDate: string;
            startTime: string;
            
        }

        // AddEmpCalSumAndTargetCommand
        export class executionProcessingCommand implements paramScreenA, paramScreenB, paramScreenC, paramScreenJ {
            private screen: string;
            // Screen A
            closureID: number;
            // Screen C
            lstEmployeeID: Array<string>;
            periodStartDate: string;
            periodEndDate: string;
             // 当月
            processingMonth: string;
            // Screen B
            dailyCreation: boolean;
            creationType: number;
            resetClass: number;
            calClassReset: boolean;
            masterReconfiguration: boolean;
            specDateClassReset: boolean;
            resetTimeForChildOrNurseCare: boolean;
            refNumberFingerCheck: boolean;
            closedHolidays: boolean;
            resettingWorkingHours: boolean;
            resetTimeForAssig: boolean;
            dailyCalClass: boolean;
            calClass: number;
            refApprovalresult: boolean;
            refClass: number;
            alsoForciblyReflectEvenIfItIsConfirmed: boolean;
            monthlyAggregation: boolean;
            summaryClass: number;
            // Screen J
            caseSpecExeContentID: string;
            //
            startMonthResult : string;
            endMonthResult : string;
            //
            isCalWhenLock : number =null;
            constructor() {

            }

            setParamsScreenA(params: paramScreenA): void {
                this.closureID = params.closureID;
            }

            setParamsScreenC(params: paramScreenC): void {
                this.closureID = params.closureID;
                this.lstEmployeeID = params.lstEmployeeID;
                this.periodStartDate = params.periodStartDate;
                this.periodEndDate = params.periodEndDate;
                this.processingMonth = params.processingMonth;
            }

            setParamsScreenB(params: paramScreenB): void {
                this.screen = "B";
                this.dailyCreation = params.dailyCreation;
                this.creationType = params.creationType;
                this.resetClass = params.resetClass;
                this.calClassReset = params.calClassReset;
                this.masterReconfiguration = params.masterReconfiguration;
                this.specDateClassReset = params.specDateClassReset;
                this.resetTimeForChildOrNurseCare = params.resetTimeForChildOrNurseCare;
                this.refNumberFingerCheck = params.refNumberFingerCheck;
                this.closedHolidays = params.closedHolidays;
                this.resettingWorkingHours = params.resettingWorkingHours;
                this.resetTimeForAssig = params.resetTimeForAssig;
                this.dailyCalClass = params.dailyCalClass;
                this.calClass = params.calClass;
                this.refApprovalresult = params.refApprovalresult;
                this.refClass = params.refClass;
                this.alsoForciblyReflectEvenIfItIsConfirmed = params.alsoForciblyReflectEvenIfItIsConfirmed;
                this.monthlyAggregation = params.monthlyAggregation;
                this.summaryClass = params.summaryClass;
            }

            setParamsScreenJ(params: paramScreenJ): void {
                this.screen = "J";
                this.caseSpecExeContentID = params.caseSpecExeContentID;
            }
        }

        export interface paramScreenA {
            closureID: number;
        }

        export interface paramScreenB {
            /** 日別作成(打刻反映)実施区分 */
            dailyCreation: boolean;
            /** 作成区分 */
            creationType?: number;
            /** 再作成区分 */
            resetClass?: number;
            /** 計算区分を再度設定 */
            calClassReset?: boolean;
            /** マスタ情報を再度設定 */
            masterReconfiguration?: boolean;
            /** 特定日区分を再度設定 */
            specDateClassReset?: boolean;
            /** 育児・介護短時間再設定 */
            resetTimeForChildOrNurseCare?: boolean;
            /** 打刻のみを再度反映 */
            refNumberFingerCheck?: boolean;
            /** 休業再設定 */
            closedHolidays?: boolean;
            /** 就業時間帯再設定 */
            resettingWorkingHours?: boolean;
            /** 申し送り時間再設定 */
            resetTimeForAssig?: boolean;
            /** 日別計算実施区分 */
            dailyCalClass: boolean;
            /** 計算区分 */
            calClass?: number;
            /** 承認結果反映実施区分 */
            refApprovalresult: boolean;
            /** 反映区分 */
            refClass?: number;
            /** 確定済みの場合にも強制的に反映する */
            alsoForciblyReflectEvenIfItIsConfirmed?: boolean;
            /** 月別集計実施区分 */
            monthlyAggregation: boolean;
            /** 集計区分 */
            summaryClass?: number;
        }

        export interface paramScreenC {
            closureID : number;
            lstEmployeeID: Array<string>;
            /** 対象期間開始日 */
            periodStartDate: string;
            /** 対象期間終了日 */
            periodEndDate: string;
            processingMonth : string;
        }

        export interface paramScreenJ {
            caseSpecExeContentID: string;
        }
    }
}