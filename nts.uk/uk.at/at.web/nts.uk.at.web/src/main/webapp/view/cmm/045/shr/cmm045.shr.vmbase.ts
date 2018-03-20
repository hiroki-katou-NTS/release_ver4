module cmm045.shr {
    export module vmbase {
        import getText = nts.uk.resource.getText;
        export class ApplicationDisplayAtr{
            code: number;
            name: string;
            constructor(code: number, name: string){
                this.code = code;
                this.name = name;
            } 
        }
        //parameter filter
        export class AppListExtractConditionDto{
            /**期間開始日付*/
            startDate: string;
            /**期間終了日付*/
            endDate: string;
            /**申請一覧区分*/
            appListAtr: number;
            /**申請種類*/
            appType: number;
            /**承認状況＿未承認*/
            unapprovalStatus: boolean;
            /**承認状況＿承認済*/
            approvalStatus: boolean;
            /**承認状況＿否認*/
            denialStatus: boolean;
            /**承認状況＿代行承認済*/
            agentApprovalStatus: boolean;
            /**承認状況＿差戻*/
            remandStatus: boolean;
            /**承認状況＿取消*/
            cancelStatus: boolean;
            /**申請表示対象*/
            appDisplayAtr: number;
            /**社員IDリスト*/
            listEmployeeId: Array<string>;
            /**社員絞込条件*/
            empRefineCondition: string;
            constructor(startDate: string, endDate: string, appListAtr: number,
                appType: number, unapprovalStatus: boolean, approvalStatus: boolean,
                denialStatus: boolean, agentApprovalStatus: boolean, remandStatus: boolean,
                cancelStatus: boolean, appDisplayAtr: number, listEmployeeId: Array<string>,
                empRefineCondition: string){
                    this.startDate = startDate;
                    this.endDate =  endDate;
                    this.appListAtr =  appListAtr;
                    this.appType = appType;
                    this.unapprovalStatus = unapprovalStatus;
                    this.approvalStatus = approvalStatus;
                    this.denialStatus = denialStatus;
                    this.agentApprovalStatus = agentApprovalStatus;
                    this.remandStatus = remandStatus;
                    this.cancelStatus = cancelStatus;
                    this.appDisplayAtr = appDisplayAtr;
                    this.listEmployeeId = listEmployeeId;
                    this.empRefineCondition = empRefineCondition;
                
            }
            setAppType(appType: number){
                  this.appType = appType;
            }
        }
        //data fill grid list mode application
        export class DataModeApp{
            appId: string;
            appType: number;
            check: boolean;
            details: string;
            applicant: string;
            appName: string;
            appAtr: string;
            appDate: string;
            appContent: string;
            inputDate: string;
            appStatus: string;
            displayAppStatus: string;
            checkAtr: boolean;
            version: number;
            checkTimecolor: boolean;
            constructor(appId: string,appType: number,  details: string, applicant: string,
                appName: string, appAtr: string, appDate: string, appContent: string,
                inputDate: string, appStatus: string, displayAppStatus: string,
                checkAtr: boolean, version: number, checkTimecolor: boolean){
                this.appId = appId;
                this.appType = appType;
//                this.check = appType == 0 ? true : false;
                this.check = false;
                this.details = details;
                this.applicant = applicant;
                this.appName = appName;
                this.appAtr = appAtr;
                this.appDate = appDate;
                this.appContent = appContent;
                this.inputDate = inputDate;
                this.appStatus = appStatus;
                this.displayAppStatus = displayAppStatus;
                this.checkAtr = checkAtr;
                this.version = version;
                this.checkTimecolor = checkTimecolor;
            }
        }  
        
        export class AppMasterInfo {
            appID: string;
            appType: number;
            dispName: string;
            empName: string;
            workplaceName: string;
            statusFrameAtr: boolean;
            phaseStatus: string;
            //事前、事後の後ろに#CMM045_101(※)を追加
            checkAddNote: boolean;
            checkTimecolor: boolean;
            constructor(appID: string, appType: number, dispName: string, empName: string, workplaceName: string, 
            statusFrameAtr: boolean, phaseStatus: string, checkAddNote: boolean, checkTimecolor: boolean)
            {
                this.appID = appID;
                this.appType = appType;
                this.dispName = dispName;
                this.empName = empName;
                this.workplaceName = workplaceName;
                this.statusFrameAtr = statusFrameAtr;
                this.phaseStatus = phaseStatus;
                this.checkAddNote = checkAddNote;
                this.checkTimecolor = checkTimecolor;
            }
        }
        export class ApplicationDto_New{
            // 申請ID
            applicationID: string;
            // 事前事後区分
            prePostAtr: number; 
            // 入力日
            inputDate: string; 
            // 入力者
            enteredPersonSID: string;
            // 差戻し理由
            reversionReason: string; 
            // 申請日
            applicationDate: string; 
            // 申請理由
            applicationReason: string;
            // 申請種類
            applicationType: number;
            // 申請者
            applicantSID: string;
            // 予定反映不可理由
            reflectPlanScheReason: number;
            // 予定反映日時
            reflectPlanTime: string;
            // 予定反映状態
            reflectPlanState: number;
            // 予定強制反映
            reflectPlanEnforce: number;
            // 実績反映不可理由
            reflectPerScheReason: number;
            // 実績反映日時
            reflectPerTime: string;
            // 予定反映状態=comment line71???
            reflectPerState: number;
            // 実績強制反映
            reflectPerEnforce: number;
            startDate: string;
            endDate: string;
            version: number;
            constructor(applicationID: string,prePostAtr: number, inputDate: string, enteredPersonSID: string,
                reversionReason: string, applicationDate: string, applicationReason: string, applicationType: number,
                applicantSID: string, reflectPlanScheReason: number, reflectPlanTime: string, reflectPlanState: number,
                reflectPlanEnforce: number, reflectPerScheReason: number, reflectPerTime: string, reflectPerState: number,
                reflectPerEnforce: number, startDate: string, endDate: string, version: number)
            {
                this.applicationID = applicationID;
                this.prePostAtr = prePostAtr; 
                this.inputDate = inputDate; 
                this.enteredPersonSID = enteredPersonSID;
                this.reversionReason = reversionReason; 
                this.applicationDate = applicationDate; 
                this.applicationReason = applicationReason;
                this.applicationType = applicationType;
                this.applicantSID = applicantSID;
                this.reflectPlanScheReason = reflectPlanScheReason;
                this.reflectPlanTime = reflectPlanTime;
                this.reflectPlanState = reflectPlanState;
                this.reflectPlanEnforce = reflectPlanEnforce;
                this.reflectPerScheReason = reflectPerScheReason;
                this.reflectPerTime = reflectPerTime;
                this.reflectPerState = reflectPerState;
                this.reflectPerEnforce = reflectPerEnforce;
                this.startDate = startDate;
                this.endDate = endDate;
                this.version = version;
            }
    }
        export class AppOverTimeInfoFull{
            appID: string;
            /**勤務時間From1*/
            workClockFrom1: string;
            /**勤務時間To1*/
            workClockTo1: string;
            /**勤務時間From2*/
            workClockFrom2: string;
            /**勤務時間To2*/
            workClockTo2: string;
            /**残業時間合計 - wait loivt*/
            total: number;
            lstFrame: Array<OverTimeFrame>;
            /**就業時間外深夜時間*/
            overTimeShiftNight: number;
            /**フレックス超過時間*/
            flexExessTime: number;
            constructor(appID: string, workClockFrom1: string, workClockTo1: string, workClockFrom2: string,
                workClockTo2: string, total: number, lstFrame: Array<OverTimeFrame>,
                overTimeShiftNight: number, flexExessTime: number)
            {
                this.appID = appID;
                this.workClockFrom1 = workClockFrom1;
                this.workClockTo1 = workClockTo1;
                this.workClockFrom2 = workClockFrom2;
                this.workClockTo2 = workClockTo2;
                this.total = total;
                this.lstFrame = lstFrame;
                this.overTimeShiftNight = overTimeShiftNight;
                this.flexExessTime = flexExessTime;    
            }
        }
        
        export class OverTimeFrame {
            /** 勤怠種類 */
            attendanceType: number;
            /**勤怠項目NO ???*/
            frameNo: number;
            /**枠名称*/
            name: string;
            //加給申請時間設定.特定日加給時間 - loai 3 bonus moi co
            timeItemTypeAtr: number;
            /**申請時間  - phut*/
            applicationTime: number;
            constructor(attendanceType: number, frameNo: number, name: string,
                timeItemTypeAtr: number, applicationTime: number)
            {
                this.attendanceType = attendanceType;
                this.frameNo = frameNo;
                this.name = name;
                this.timeItemTypeAtr = timeItemTypeAtr;
                this.applicationTime = applicationTime;    
            }
        }
        export class AppGoBackInfoFull {
            appID: string;
            /**勤務直行1*/
            goWorkAtr1: number;
            /**勤務時間開始1*/
            workTimeStart1: string;
            /**勤務直帰1*/
            backHomeAtr1: number;
            /**勤務時間終了1*/
            workTimeEnd1: string;
            /**勤務直行2*/
            goWorkAtr2: number;
            /**勤務時間開始2*/
            workTimeStart2: string;
            /**勤務直帰2*/
            backHomeAtr2: number;
            /**勤務時間終了2*/
            workTimeEnd2: string;
            constructor(appID: string, goWorkAtr1: number, workTimeStart1: string, backHomeAtr1: number,
                workTimeEnd1: string, goWorkAtr2: number, workTimeStart2: string,
                backHomeAtr2: number, workTimeEnd2: string)
            {
                this.appID = appID;
                this.goWorkAtr1 = goWorkAtr1;
                this.workTimeStart1 = workTimeStart1;
                this.backHomeAtr1 = backHomeAtr1;
                this.workTimeEnd1 = workTimeEnd1;
                this.goWorkAtr2 = goWorkAtr2;
                this.workTimeStart2 = workTimeStart2;
                this.backHomeAtr2 = backHomeAtr2;
                this.workTimeEnd2 = workTimeEnd2;
            }
        }
        //display setting
        export class ApprovalListDisplaySetDto {
            /**事前申請の超過メッセージ*/
            advanceExcessMessDisAtr: number;
            /**休出の事前申請*/
            hwAdvanceDisAtr: number;
            /**休出の実績*/
            hwActualDisAtr: number;
            /**実績超過メッセージ*/
            actualExcessMessDisAtr: number;
            /**残業の事前申請*/
            otAdvanceDisAtr: number;
            /**残業の実績*/
            otActualDisAtr: number;
            /**申請対象日に対して警告表示*/
            warningDateDisAtr: number;
            /**申請理由*/
            appReasonDisAtr: number;
            constructor(advanceExcessMessDisAtr: number, hwAdvanceDisAtr: number,
                hwActualDisAtr: number, actualExcessMessDisAtr: number,
                otAdvanceDisAtr: number, otActualDisAtr: number,
                warningDateDisAtr: number, appReasonDisAtr: number)
            {
                this.advanceExcessMessDisAtr = advanceExcessMessDisAtr;
                this.hwAdvanceDisAtr = hwAdvanceDisAtr;
                this.hwActualDisAtr = hwActualDisAtr;
                this.actualExcessMessDisAtr = actualExcessMessDisAtr;
                this.otAdvanceDisAtr = otAdvanceDisAtr;
                this.otActualDisAtr = otActualDisAtr;
                this.warningDateDisAtr = warningDateDisAtr;
                this.appReasonDisAtr = appReasonDisAtr;
            }
        }
        export class ApplicationStatus {
            unApprovalNumber: string;
            approvalNumber: string;
            approvalAgentNumber: string;
            cancelNumber: string;
            remandNumner: string;
            denialNumber: string;
            constructor(unApprovalNumber: number, approvalNumber: number,
                approvalAgentNumber: number, cancelNumber: number,
                remandNumner: number,denialNumber: number)
            {
//                this.unApprovalNumber = getText('CMM045_12') + ' ' + getText('CMM045_18', [unApprovalNumber]); 
//                this.approvalNumber = getText('CMM045_13') + ' ' + getText('CMM045_18', [approvalNumber]);
//                this.approvalAgentNumber = getText('CMM045_14') + ' ' + getText('CMM045_18', [denialNumber]);
//                this.cancelNumber = getText('CMM045_15') + ' ' + getText('CMM045_18', [approvalAgentNumber]);
//                this.remandNumner = getText('CMM045_16') + ' ' + getText('CMM045_18', [remandNumner]);
//                this.denialNumber = getText('CMM045_17') + ' ' + getText('CMM045_18', [cancelNumber]); 
                this.unApprovalNumber = getText('CMM045_18', [unApprovalNumber]); 
                this.approvalNumber = getText('CMM045_18', [approvalNumber]);
                this.approvalAgentNumber = getText('CMM045_18', [denialNumber]);
                this.cancelNumber = getText('CMM045_18', [approvalAgentNumber]);
                this.remandNumner = getText('CMM045_18', [remandNumner]);
                this.denialNumber = getText('CMM045_18', [cancelNumber]);    
            }
        }
        export class ChoseApplicationList{
            appId: number;
            appName: string;
            constructor(appId: number, appName: string){
                this.appId = appId;
                this.appName = appName;
            }    
        }
        export interface Date{
            startDate: string;
            endDate: string;
        }
        export class AppPrePostGroup{
            //事前
            preAppID: string;
            //事後
            postAppID: string;
            //実績
            lstFrameRes: Array<vmbase.OverTimeFrame>;
            appPre: any;
            reasonAppPre: string;
            constructor(preAppID: string, postAppID: string, lstFrameRes: Array<vmbase.OverTimeFrame>,
                appPre: any, reasonAppPre: string){
                this.preAppID = preAppID;
                this.postAppID = postAppID;
                this.lstFrameRes = lstFrameRes;
                this.appPre = appPre;
                this.reasonAppPre = reasonAppPre;
            }
        }
        export class ApproveAgent{
            appID: string;
            agentId: string;
            constructor(appID: string, agentId: string){
                this.appID = appID;
                this.agentId = agentId;
            }
        }
        export class AppWorkChangeFull {
            appId: string;
            /**勤務種類名*/
            workTypeName: string;
            /**就業時間帯名*/
            workTimeName: string;
            /**勤務直行1*/
            goWorkAtr1: number;
            /**勤務時間開始1*/
            workTimeStart1: string;
            /**勤務直帰1*/
            backHomeAtr1: number;
            /**勤務時間終了1*/
            workTimeEnd1: string;
            /**勤務直行2*/
            goWorkAtr2: number;
            /**勤務時間開始2*/
            workTimeStart2: string;
            /**勤務直帰2*/
            backHomeAtr2: number;
            /**勤務時間終了2*/
            workTimeEnd2: string;
            /**休憩時間開始1*/
            breakTimeStart1: string;
            /**休憩時間終了1*/
            breakTimeEnd1: string;
            //0: する
            //1: しない
            constructor(appId: string, workTypeName: string, workTimeName: string,
                goWorkAtr1: number, workTimeStart1: string, backHomeAtr1: number,
                workTimeEnd1: string, goWorkAtr2: number, workTimeStart2: string,
                backHomeAtr2: number, workTimeEnd2: string, breakTimeStart1: string,breakTimeEnd1: string)
            {
                this.appId = appId;
                this.workTypeName = workTypeName;
                this.workTimeName = workTimeName;
                this.goWorkAtr1 = goWorkAtr1;
                this.workTimeStart1 = workTimeStart1;
                this.backHomeAtr1 = backHomeAtr1;
                this.workTimeEnd1 = workTimeEnd1;
                this.goWorkAtr2 = goWorkAtr2;
                this.workTimeStart2 = workTimeStart2;
                this.backHomeAtr2 = backHomeAtr2;
                this.workTimeEnd2 = workTimeEnd2;
                this.breakTimeStart1 = breakTimeStart1;
                this.breakTimeEnd1 = breakTimeEnd1;
            }
        }
        export class AppHolidayWorkFull {
            appId: string;
            /**勤務種類 name*/
            workTypeName: string;
            /**就業時間帯 name*/
            workTimeName: string;
            //勤務開始時刻1
            startTime1: string;
            //勤務終了時刻1
            endTime1: string;
            //勤務開始時刻2
            startTime2: string;
            //勤務終了時刻2
            endTime2: string;
            lstFrame: any;
            constructor(appId: string, workTypeName: string, workTimeName: string, startTime1: string,
                endTime1: string, startTime2: string, endTime2: string, lstFrame: any){
                this.appId = appId;
                this.workTypeName = workTypeName;
                this.workTimeName = workTimeName;
                this.startTime1 = startTime1;
                this.endTime1 = endTime1;
                this.startTime2 = startTime2;
                this.endTime2 = endTime2;
                this.lstFrame = lstFrame;    
            }
        }
            
            
        export class ProcessHandler {
            /**
             * sort by appType and appDate
             */
            static orderByList(lstData: Array<DataModeApp>): Array<DataModeApp>{
                let result: Array<DataModeApp> = [];
                let lstA0: Array<DataModeApp> = [];
                let lstA2: Array<DataModeApp> = [];
                let lstA4: Array<DataModeApp> = [];
                let lstA6: Array<DataModeApp> = [];
                _.each(lstData, function(obj){
                    if(obj.appType == 0){//overtime
                        lstA0.push(obj);
                    }
                    if(obj.appType == 4){//go back
                        lstA4.push(obj);
                    }
                    if(obj.appType == 2){//work change
                        lstA2.push(obj);
                    }
                    if(obj.appType == 6){//holiday work
                        lstA6.push(obj);
                    }
                });
                let sortByA0 =  _.orderBy(lstA0, ["appDate"], ["asc"]);
                let sortByA2 =  _.orderBy(lstA2, ["appDate"], ["asc"]);
                let sortByA4 =  _.orderBy(lstA4, ["appDate"], ["asc"]);
                let sortByA6 =  _.orderBy(lstA6, ["appDate"], ["asc"]);
                //push list A0 (残業申請)
                _.each(sortByA0, function(obj){
                    result.push(obj);
                });
                //push list A2 (勤務変更申請)
                _.each(sortByA2, function(obj){
                    result.push(obj);
                });
                //push list A4 (直行直帰申請)
                _.each(sortByA4, function(obj){
                    result.push(obj);
                });
                //push list A6 (休日出勤時間申請)
                _.each(sortByA6, function(obj){
                    result.push(obj);
                });
                return result;
            }
        }
    }
}