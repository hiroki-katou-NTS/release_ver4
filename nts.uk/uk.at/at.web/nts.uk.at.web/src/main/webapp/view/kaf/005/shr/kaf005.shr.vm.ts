module nts.uk.at.view.kaf005.share {
    export module common {
        /**
         * 
         */
        export class Application {
            applicationID: KnockoutObservable<string>; // 申請ID
            inputDate: KnockoutObservable<string>; // 入力日
            enteredPerson: KnockoutObservable<string>; // 入力者
            appDate: KnockoutObservable<string>; // 申請日
            titleReason: KnockoutObservable<string>;
            contentReason: KnockoutObservable<string>;
            employeeID: KnockoutObservable<string>; // 申請者
            constructor(
                applicationID: string,
                inputDate: string,
                enteredPerson: string,
                appDate: string,
                titleReason: string,
                contentReason: string,
                employeeID: string) {
                this.applicationID = ko.observable(applicationID);
                this.inputDate = ko.observable(inputDate);
                this.enteredPerson = ko.observable(enteredPerson);
                this.appDate = ko.observable(appDate);
                this.titleReason = ko.observable(titleReason);
                this.contentReason = ko.observable(contentReason);
                this.employeeID = ko.observable(employeeID);
            }
        }
        /**
         * 理由
         */
        export class ReasonDto {
            companyId: string;
            appType: number;
            reasonID: string;
            displayOrder: number;
            reasonTemp: string;
            constructor(companyId: string, appType: number, reasonID: string, displayOrder: number, reasonTemp: string) {
                var self = this;
                self.companyId = companyId;
                self.appType = appType;
                self.reasonID = reasonID;
                self.displayOrder = displayOrder;
                self.reasonTemp = reasonTemp;
            }

        };
        /**
         * 
         */
        export class ComboReason {
            reasonId: string;
            reasonName: string;
            constructor(reasonId: string, reasonName: string) {
                this.reasonId = reasonId;
                this.reasonName = reasonName;
            }
        }
        /**
         * Application detail
         */
        export class ApplicationCommand {
            applicationID: string;
            appReasonID: string;
            prePostAtr: number;
            inputDate: string;
            enteredPersonSID: string;
            reversionReason: string;
            applicationDate: string;
            applicationReason: string;
            applicationType: number;
            applicantSID: string;
            reflectPlanScheReason: number;
            reflectPlanTime: string;
            reflectPlanState: number;
            reflectPlanEnforce: number;
            reflectPerScheReason: number;
            reflectPerTime: string;
            reflectPerState: number;
            reflectPerEnforce: number;
            startDate: string;
            endDate: string;
            listPhase: any;
            constructor(
                appReasonID: string,
                prePostAtr: number,
                inputDate: string,
                enteredPersonSID: string,
                reversionReason: string,
                applicationDate: string,
                applicationReason: string,
                applicantSID: string,
                reflectPlanTime: string,
                reflectPerTime: string,
                startDate: string,
                endDate: string) {
                this.applicationID = "";
                this.appReasonID = appReasonID;
                this.prePostAtr = prePostAtr;
                this.inputDate = moment.utc(inputDate, "YYYY/MM/DD").toISOString();
                this.enteredPersonSID = enteredPersonSID;
                this.reversionReason = reversionReason;
                this.applicationDate = moment.utc(applicationDate, "YYYY/MM/DD").toISOString();
                this.applicationReason = applicationReason;
                this.applicationType = 4;
                this.applicantSID = applicantSID;
                this.reflectPlanScheReason = 1;
                this.reflectPlanTime = moment.utc(reflectPlanTime, "YYYY/MM/DD").toISOString();
                this.reflectPlanState = 1;
                this.reflectPlanEnforce = 1;
                this.reflectPerScheReason = 1;
                this.reflectPerTime = moment.utc(reflectPerTime, "YYYY/MM/DD").toISOString();
                this.reflectPerState = 1;
                this.reflectPerEnforce = 1;
                this.startDate = moment.utc(startDate, "YYYY/MM/DD").toISOString();
                this.endDate = moment.utc(endDate, "YYYY/MM/DD").toISOString();
                this.listPhase = null;
            }
        }
        /**
         * 
         */
        export class AppApprovalPhase {
            phaseID: string;
            approvalForm: number;
            dispOrder: number;
            approvalATR: number;
            approvalFrameCmds: Array<ApprovalFrame>;
            constructor(phaseID: string, approvalForm: number, dispOrder: number, approvalATR: number, approvalFrameCmds: Array<ApprovalFrame>) {
                this.phaseID = phaseID;
                this.approvalForm = approvalForm;
                this.dispOrder = dispOrder;
                this.approvalATR = approvalATR;
                this.approvalFrameCmds = approvalFrameCmds;
            }
        }
        /**
         * 
         */
        export class ApprovalFrame {
            frameID: string;
            dispOrder: number;
            approveAcceptedCmds: Array<ApproveAccepted>;
            constructor(frameID: string, dispOrder: number, approveAcceptedCmds: Array<ApproveAccepted>) {
                this.frameID = frameID;
                this.dispOrder = dispOrder;
                this.approveAcceptedCmds = approveAcceptedCmds;
            }
        }
        export class EmployeeOT{
            id: string;
            name: string;
            constructor(id: string, name: string){
                this.id = id;
                this.name = name;
            }    
        }
        /**
         * 
         */
        export class ApproveAccepted {
            appAcceptedID: string;
            approverSID: string;
            approvalATR: number;
            confirmATR: number;
            approvalDate: string;
            reason: string;
            representerSID: string;
            constructor(appAcceptedID: string, approverSID: string, approvalATR: number,
                confirmATR: number, approvalDate: string, reason: string, representerSID: string) {
                this.appAcceptedID = appAcceptedID;
                this.approverSID = approverSID;
                this.approvalATR = approvalATR;
                this.confirmATR = confirmATR;
                this.approvalDate = approvalDate;
                this.reason = reason;
                this.representerSID = representerSID;
            }
        }
        export class AppOverTime {
            companyID: string;
            appID: string;
            applicationDate: string;
            prePostAtr: number;
            applicantSID: string;
            applicationReason: string;
            workType: string;
            siftType: string;
            workClockFrom1: number;
            workClockTo1: number;
            workClockFrom2: number;
            workClockTo2: number;
            bonusTimes: Array<any>;
            breakTimes: Array<any>;
            overtimeHours: Array<any>;
            restTime: Array<any>;
            overtimeAtr: number;
            overTimeShiftNight: number;
            flexExessTime: number;
            divergenceReasonContent: string;
            sendMail:boolean;
            calculateFlag: number;
            appReasonID: string;
        }
        export class OverTimeInput {
            companyID: KnockoutObservable<string>;
            appID: KnockoutObservable<string>;
            attendanceID: KnockoutObservable<number>;
            attendanceName: KnockoutObservable<string>;
            frameNo: KnockoutObservable<number>;
            timeItemTypeAtr: KnockoutObservable<number>;
            frameName: KnockoutObservable<string>;
            startTime: KnockoutObservable<number>;
            endTime: KnockoutObservable<number>;
            applicationTime: KnockoutObservable<number>;
            nameID: KnockoutObservable<string>;
            
            constructor(
                companyID: string,
                appID: string,
                attendanceID: number,
                attendanceName: string,
                frameNo: number,
                timeItemTypeAtr: number,
                frameName: string,
                startTime: number,
                endTime: number,
                applicationTime: number,
                nameID: string) {
                this.companyID = ko.observable(companyID);
                this.appID = ko.observable(appID);
                this.attendanceID = ko.observable(attendanceID);
                this.attendanceName = ko.observable(attendanceName);
                this.frameNo = ko.observable(frameNo);
                this.timeItemTypeAtr = ko.observable(timeItemTypeAtr);
                this.frameName = ko.observable(frameName);
                this.startTime = ko.observable(startTime);
                this.endTime = ko.observable(endTime);
                this.applicationTime = ko.observable(applicationTime);
                this.nameID = ko.observable(nameID);
                
            }
        } 
        export class OvertimeCaculation{
            companyID: KnockoutObservable<string>;
            appID: KnockoutObservable<string>;
            attendanceID: KnockoutObservable<number>;
            attendanceName: KnockoutObservable<string>;
            frameNo: KnockoutObservable<number>;
            timeItemTypeAtr: KnockoutObservable<number>;
            frameName: KnockoutObservable<string>;
            applicationTime: KnockoutObservable<number>;
            preAppTime: KnockoutObservable<string>;
            caculationTime: KnockoutObservable<string>;
            nameID: KnockoutObservable<string>;
            itemName: string;
            color : KnockoutObservable<string>;
            constructor(
                companyID: string,
                appID: string,
                attendanceID: number,
                attendanceName: string,
                frameNo: number,
                timeItemTypeAtr: number,
                frameName: string,
                applicationTime: number,
                preAppTime: string,
                caculationTime: string,
                nameID: string,
                itemName: string,
                color :string) {
                this.companyID = ko.observable(companyID);
                this.appID = ko.observable(appID);
                this.attendanceID = ko.observable(attendanceID);
                this.attendanceName = ko.observable(attendanceName);
                this.frameNo = ko.observable(frameNo);
                this.timeItemTypeAtr = ko.observable(timeItemTypeAtr);
                this.frameName = ko.observable(frameName);
                this.applicationTime = ko.observable(applicationTime);
                this.preAppTime = ko.observable(preAppTime);
                this.caculationTime = ko.observable(caculationTime);
                this.nameID = ko.observable(nameID);
                this.itemName = nts.uk.resource.getText("KAF005_85",[frameName]);
                this.color = ko.observable(color);
            }
        }  
		export class OvertimeWork {
            yearMonth: KnockoutObservable<string>;
            limitTime: KnockoutObservable<number>;
            actualTime: KnockoutObservable<number>;
            appTime: KnockoutObservable<number>;
            totalTime: KnockoutObservable<number>;
            backgroundColor: KnockoutObservable<string>;
            textColor: KnockoutObservable<string>;
            constructor(yearMonth: string, limitTime: number, actualTime: number, appTime: number,  
                totalTime: number, backgroundColor: string, textColor: string) {
                this.yearMonth = ko.observable(yearMonth);
                this.limitTime = ko.observable(limitTime);
                this.actualTime = ko.observable(actualTime);
                this.appTime = ko.observable(appTime);
                this.totalTime = ko.observable(totalTime);
                this.backgroundColor = ko.observable(backgroundColor);
                this.textColor = ko.observable(textColor);
            }
        }
        
        export class AppOvertimePre{
           companyID: KnockoutObservable<string>;
            appID: KnockoutObservable<string>;
            attendanceID: KnockoutObservable<number>;
            attendanceName: KnockoutObservable<string>;
            frameNo: KnockoutObservable<number>;
            timeItemTypeAtr: KnockoutObservable<number>;
            frameName: KnockoutObservable<string>;
            startTime: KnockoutObservable<number>;
            endTime: KnockoutObservable<number>;
            applicationTime: KnockoutObservable<string>;
            nameID: KnockoutObservable<string>;
            constructor(
                companyID: string,
                appID: string,
                attendanceID: number,
                attendanceName: string,
                frameNo: number,
                timeItemTypeAtr: number,
                frameName: string,
                startTime: number,
                endTime: number,
                applicationTime: string,
                nameID: string) {
                this.companyID = ko.observable(companyID);
                this.appID = ko.observable(appID);
                this.attendanceID = ko.observable(attendanceID);
                this.attendanceName = ko.observable(attendanceName);
                this.frameNo = ko.observable(frameNo);
                this.timeItemTypeAtr = ko.observable(timeItemTypeAtr);
                this.frameName = ko.observable(frameName);
                this.startTime = ko.observable(startTime);
                this.endTime = ko.observable(endTime);
                this.applicationTime = ko.observable(applicationTime);
                this.nameID = ko.observable(nameID);
            }
        }
        /**
         * 勤務内容
         */
         export class WorkContent{
             //申請日
             applicationDate: string;
             //勤務種類
             workType: string;
             //就業時間帯
             siftType: string;
             //勤務時間
             workClockFrom1: number;
             workClockTo1: number;
             workClockFrom2: number;
             workClockTo2: number;
             //休憩時間
             breakTimes: Array<any>;
            constructor(
                applicationDate: string,
                workType: string,
                siftType: string,
                workClockFrom1: number,
                workClockTo1: number,
                workClockFrom2: number,
                workClockTo2: number,
                breakTimes: Array<any>) {
                this.applicationDate = applicationDate;
                this.workType = workType;
                this.breakTimes = breakTimes;
                this.workClockFrom1 = workClockFrom1;
                this.workClockTo1 = workClockTo1;
                this.workClockFrom2 = workClockFrom2;
                this.workClockTo2 = workClockTo2;
            }
        }
        
        export class OvertimeAgreement {
            detailCurrentMonth: AgreementTimeDetail;
            detailNextMonth: AgreementTimeDetail;
            currentMonth: string;
            nextMonth: string;
        }
        
        export class AgreementTimeDetail {
            employeeID: string;   
            confirmed: AgreementTimeOfMonthly;
            afterAppReflect: AgreementTimeOfMonthly;
            errorMessage: string;
        }
        
        export enum AgreementTimeStatusOfMonthly {
            /** 正常 */
            NORMAL = 0,
            /** 限度エラー時間超過 */
            EXCESS_LIMIT_ERROR =  1,
            /** 限度アラーム時間超過 */
            EXCESS_LIMIT_ALARM = 2,
            /** 特例限度エラー時間超過 */
            EXCESS_EXCEPTION_LIMIT_ERROR = 3,
            /** 特例限度アラーム時間超過 */
            EXCESS_EXCEPTION_LIMIT_ALARM = 4,
            /** 正常（特例あり） */
            NORMAL_SPECIAL = 5,
            /** 限度エラー時間超過（特例あり） */
            EXCESS_LIMIT_ERROR_SP = 6,
            /** 限度アラーム時間超過（特例あり） */
            EXCESS_LIMIT_ALARM_SP = 7,
        }
        
        export enum Color {
            // 36協定アラーム
            ALARM = "#F6F636",
            // 36協定アラーム文字
            ALARM_TEXT = "#ff0000",
            // 36協定エラー
            ERROR = "#FD4D4D",
            // 36協定エラー文字
            ERROR_TEXT = "#ffffff",
            // 36協定特例
            EXCEPTION = "#eb9152",
        }
        
        export class Process {
            public static setOvertimeWork(overtimeAgreement: common.OvertimeAgreement, self: any): void {
                let overtimeWork1 = new common.OvertimeWork("",0,0,0,0,"","");
                let overtimeWork2 = new common.OvertimeWork("",0,0,0,0,"","");
                
                overtimeWork1.yearMonth(overtimeAgreement.currentMonth);
                let exceptionLimitErrorTime1 = overtimeAgreement.detailCurrentMonth.confirmed.exceptionLimitErrorTime;   
                if(!nts.uk.util.isNullOrUndefined(exceptionLimitErrorTime1)){
                    overtimeWork1.limitTime(Process.convertTime(exceptionLimitErrorTime1));             
                } else {
                    let limitErrorTime1 = overtimeAgreement.detailCurrentMonth.confirmed.limitErrorTime;
                    overtimeWork1.limitTime(Process.convertTime(limitErrorTime1));        
                }
                let agreementTime1 = overtimeAgreement.detailCurrentMonth.confirmed.agreementTime;
                overtimeWork1.actualTime(Process.convertTime(agreementTime1));
                let appTime1 = 0;
                overtimeWork1.appTime(Process.convertTime(appTime1));
                overtimeWork1.totalTime(Process.convertTime(agreementTime1+appTime1));
                switch(overtimeAgreement.detailCurrentMonth.confirmed.status){
                    case common.AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ALARM: {
                        overtimeWork1.backgroundColor(common.Color.ALARM);
                        overtimeWork1.textColor(common.Color.ALARM_TEXT);
                        break;
                    }   
                    case common.AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ERROR: {
                        overtimeWork1.backgroundColor(common.Color.ERROR);
                        overtimeWork1.textColor(common.Color.ERROR_TEXT);
                        break;
                    } 
                    case common.AgreementTimeStatusOfMonthly.NORMAL_SPECIAL: {
                        break;    
                    }
                    case common.AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ALARM_SP: {
                        overtimeWork1.backgroundColor(common.Color.EXCEPTION);
                        break;
                    }
                    case common.AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ERROR_SP: {
                        overtimeWork1.backgroundColor(common.Color.EXCEPTION);
                        break;        
                    }
                    case common.AgreementTimeStatusOfMonthly.EXCESS_EXCEPTION_LIMIT_ALARM: {
                        overtimeWork1.backgroundColor(common.Color.ALARM);
                        overtimeWork1.textColor(common.Color.ALARM_TEXT);
                        break;
                    }     
                    case common.AgreementTimeStatusOfMonthly.EXCESS_EXCEPTION_LIMIT_ERROR: {
                        overtimeWork1.backgroundColor(common.Color.ERROR);
                        overtimeWork1.textColor(common.Color.ERROR_TEXT);
                        break;
                    }  
                    default: break;
                }
                
                overtimeWork2.yearMonth(overtimeAgreement.nextMonth);
                let exceptionLimitErrorTime2 = overtimeAgreement.detailNextMonth.confirmed.exceptionLimitErrorTime;   
                if(!nts.uk.util.isNullOrUndefined(exceptionLimitErrorTime2)){
                    overtimeWork2.limitTime(Process.convertTime(exceptionLimitErrorTime2));             
                } else {
                    let limitErrorTime2 = overtimeAgreement.detailNextMonth.confirmed.limitErrorTime;
                    overtimeWork2.limitTime(Process.convertTime(limitErrorTime2));        
                }
                let agreementTime2 = overtimeAgreement.detailNextMonth.confirmed.agreementTime;
                overtimeWork2.actualTime(Process.convertTime(agreementTime2));
                let appTime2 = 0;
                overtimeWork2.appTime(Process.convertTime(appTime2));
                overtimeWork2.totalTime(Process.convertTime(agreementTime2+appTime2));
                switch(overtimeAgreement.detailNextMonth.confirmed.status){
                    case common.AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ALARM: {
                        overtimeWork2.backgroundColor(common.Color.ALARM);
                        overtimeWork2.textColor(common.Color.ALARM_TEXT);
                        break;
                    }   
                    case common.AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ERROR: {
                        overtimeWork2.backgroundColor(common.Color.ERROR);
                        overtimeWork2.textColor(common.Color.ERROR_TEXT);
                        break;
                    } 
                    case common.AgreementTimeStatusOfMonthly.NORMAL_SPECIAL: {
                        break;    
                    }
                    case common.AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ALARM_SP: {
                        overtimeWork2.backgroundColor(common.Color.EXCEPTION);
                        break;
                    }
                    case common.AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ERROR_SP: {
                        overtimeWork2.backgroundColor(common.Color.EXCEPTION);
                        break;        
                    }
                    case common.AgreementTimeStatusOfMonthly.EXCESS_EXCEPTION_LIMIT_ALARM: {
                        overtimeWork2.backgroundColor(common.Color.ALARM);
                        overtimeWork2.textColor(common.Color.ALARM_TEXT);
                        break;
                    }     
                    case common.AgreementTimeStatusOfMonthly.EXCESS_EXCEPTION_LIMIT_ERROR: {
                        overtimeWork2.backgroundColor(common.Color.ERROR);
                        overtimeWork2.textColor(common.Color.ERROR_TEXT);
                        break;
                    }  
                    default: break;
                }
                
                self.overtimeWork.removeAll();
                self.overtimeWork.push(overtimeWork1);
                self.overtimeWork.push(overtimeWork2);
            } 
            
            public static setOvertimeWorkDetail(appOvertimeDetailDto: any, self: any, status: any): void {
                let overtimeWork = new common.OvertimeWork("",0,0,0,0,"","");
                
                overtimeWork.yearMonth(nts.uk.time.formatYearMonth(appOvertimeDetailDto.yearMonth));
                if(!nts.uk.util.isNullOrUndefined(appOvertimeDetailDto.exceptionLimitErrorTime)){
                    overtimeWork.limitTime(Process.convertTime(appOvertimeDetailDto.exceptionLimitErrorTime));    
                } else {
                    overtimeWork.limitTime(Process.convertTime(appOvertimeDetailDto.limitErrorTime));    
                }
                overtimeWork.actualTime(Process.convertTime(appOvertimeDetailDto.actualTime));
                overtimeWork.appTime(Process.convertTime(appOvertimeDetailDto.applicationTime));
                overtimeWork.totalTime(Process.convertTime(appOvertimeDetailDto.actualTime + appOvertimeDetailDto.applicationTime));
                switch(status){
                    case common.AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ALARM: {
                        overtimeWork.backgroundColor(common.Color.ALARM);
                        overtimeWork.textColor(common.Color.ALARM_TEXT);
                        break;
                    }   
                    case common.AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ERROR: {
                        overtimeWork.backgroundColor(common.Color.ERROR);
                        overtimeWork.textColor(common.Color.ERROR_TEXT);
                        break;
                    } 
                    case common.AgreementTimeStatusOfMonthly.NORMAL_SPECIAL: {
                        break;    
                    }
                    case common.AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ALARM_SP: {
                        overtimeWork.backgroundColor(common.Color.EXCEPTION);
                        break;
                    }
                    case common.AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ERROR_SP: {
                        overtimeWork.backgroundColor(common.Color.EXCEPTION);
                        break;        
                    }
                    case common.AgreementTimeStatusOfMonthly.EXCESS_EXCEPTION_LIMIT_ALARM: {
                        overtimeWork.backgroundColor(common.Color.ALARM);
                        overtimeWork.textColor(common.Color.ALARM_TEXT);
                        break;
                    }     
                    case common.AgreementTimeStatusOfMonthly.EXCESS_EXCEPTION_LIMIT_ERROR: {
                        overtimeWork.backgroundColor(common.Color.ERROR);
                        overtimeWork.textColor(common.Color.ERROR_TEXT);
                        break;
                    }  
                    default: break;
                }
                
                self.overtimeWork.removeAll();
                self.overtimeWork.push(overtimeWork);
            }
            
            public static convertTime(minutes: number){
                if(minutes < 0){
                    return "-" + nts.uk.time.format.byId("Time_Short_HM", -minutes);    
                } else {
                    return nts.uk.time.format.byId("Time_Short_HM", minutes);    
                }   
            }
            
            public static setNulltoZero(param: number){
                return param;        
            }
        }
    }
}