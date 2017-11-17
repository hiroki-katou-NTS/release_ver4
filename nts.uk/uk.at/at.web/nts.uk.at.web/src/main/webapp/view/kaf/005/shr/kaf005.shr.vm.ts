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
            companyID: string
            appID: string
            applicationDate: string
            prePostAtr: number
            applicantSID: string
            applicationReason: string
            appApprovalPhaseCmds: Array<any>
            workType: string
            siftType: string
            workClockFrom1: number
            workClockTo1: number
            workClockFrom2: number
            workClockTo2: number
            bonusTimes: Array<any>
            breakTimes: Array<any>
            overtimeHours: Array<any>
            restTime: Array<any>
            overtimeAtr: number
            overTimeShiftNight: number
            flexExessTime: number
            divergenceReasonContent: string
            sendMail:boolean
            calculateFlag: number
        }
        export class OverTimeInput {
            companyID: KnockoutObservable<string>
            appID: KnockoutObservable<string>
            attendanceID: KnockoutObservable<number>
            attendanceName: KnockoutObservable<string>
            frameNo: KnockoutObservable<number>
            timeItemTypeAtr: KnockoutObservable<number>
            frameName: KnockoutObservable<string>
            startTime: KnockoutObservable<number>
            endTime: KnockoutObservable<number>
            applicationTime: KnockoutObservable<number>
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
                applicationTime: number) {
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
            }
        }   
		export class overtimeWork {
            yearMonth: KnockoutObservable<string>;
            limitTime: KnockoutObservable<string>;
            actualTime: KnockoutObservable<string>;
            appTime: KnockoutObservable<string>;
            totalTime: KnockoutObservable<string>;
            constructor(yearMonth: string, limitTime: string, actualTime: string, appTime: string,  totalTime: string) {
                this.yearMonth = ko.observable(yearMonth);
                this.limitTime = ko.observable(limitTime);
                this.actualTime = ko.observable(actualTime);
                this.appTime = ko.observable(appTime);
                this.totalTime = ko.observable(totalTime);
            }
        }		

    }
}