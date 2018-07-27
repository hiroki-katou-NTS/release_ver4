module nts.uk.at.view.kaf018.share.model {
    
    export function showMsgSendEmail(result) {
        if (result.ok) {
            nts.uk.ui.dialog.info({ messageId: "Msg_792" });
        }
        else {
            let msg = nts.uk.resource.getMessage("Msg_793");
            _.each(result.listError, function(err) {
                msg += "\n" + err;
            })
            nts.uk.ui.dialog.error({ messageId: "Msg_793", message: msg });
        }
    }

    export class ItemModel {
        code: string;
        name: string;

        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
        }
    }
    export class CellState {
        rowId: any;
        columnKey: string;
        state: Array<any>
        constructor(rowId: any, columnKey: string, state: Array<any>) {
            this.rowId = rowId;
            this.columnKey = columnKey;
            this.state = state;
        }
    }
    export enum REFLECTEDSTATUS {
        //未反映        
        NOTREFLECTED = 0,

        //反映待ち
        WAITREFLECTION = 1,

        //反映済
        REFLECTED = 2,
        //取消待ち
        WAITCANCEL = 3,
        //取消済
        CANCELED = 4,
        //差し戻し
        REMAND = 5,

        //否認 
        DENIAL = 6
    }
    export enum ApplicationType {
        //残業申請
        OVER_TIME_APPLICATION = 0,
        //休暇申請
        ABSENCE_APPLICATION = 1,
        //勤務変更申請
        WORK_CHANGE_APPLICATION = 2,
        //出張申請
        BUSINESS_TRIP_APPLICATION = 3,
        //直行直帰申請
        GO_RETURN_DIRECTLY_APPLICATION = 4,
        //休出時間申請
        BREAK_TIME_APPLICATION = 6,
        //打刻申請
        STAMP_APPLICATION = 7,
        //時間年休申請
        ANNUAL_HOLIDAY_APPLICATION = 8,
        //遅刻早退取消申請
        EARLY_LEAVE_CANCEL_APPLICATION = 9,
        //振休振出申請
        COMPLEMENT_LEAVE_APPLICATION = 10,
        //打刻申請（NR形式）
        STAMP_NR_APPLICATION = 11,
        //連続出張申請
        LONG_BUSINESS_TRIP_APPLICATION = 12,
        //出張申請オフィスヘルパー
        BUSINESS_TRIP_APPLICATION_OFFICE_HELPER = 13,
        //３６協定時間申請
        APPLICATION_36 = 14
    }
    export class MailTemp {
        mailSubject: KnockoutObservable<string>;
        mailContent: KnockoutObservable<string>;
        urlApprovalEmbed: KnockoutObservable<boolean>;
        urlDayEmbed: KnockoutObservable<boolean>;
        urlMonthEmbed: KnockoutObservable<boolean>;
        mailType: number;
        editMode: KnockoutObservable<boolean>;

        constructor(mailType: number, mailSubject: string, mailContent: string, urlApprovalEmbed: number, urlDayEmbed: number, urlMonthEmbed: number, editMode: number) {
            this.mailType = mailType;
            this.mailSubject = ko.observable(mailSubject);
            this.mailContent = ko.observable(mailContent);
            this.urlApprovalEmbed = ko.observable(urlApprovalEmbed == 0 ? false : true);
            this.urlDayEmbed = ko.observable(urlDayEmbed == 0 ? false : true);
            this.urlMonthEmbed = ko.observable(urlMonthEmbed == 0 ? false : true);
            this.editMode = ko.observable(editMode == 0 ? false : true);
        }
    }

    export class IdentityProcessUseSet {
        useIdentityOfMonth: KnockoutObservable<boolean>;

        constructor(useIdentityOfMonth: boolean) {
            this.useIdentityOfMonth = ko.observable(useIdentityOfMonth);
        }
    }

    export class ApprovalProcessingUseSetting {
        useMonthApproverComfirm: KnockoutObservable<boolean>;
        useDayApproverConfirm: KnockoutObservable<boolean>;

        constructor(useDayApproverConfirm: boolean, useMonthApproverComfirm: boolean) {
            this.useMonthApproverComfirm = ko.observable(useMonthApproverComfirm);
            this.useDayApproverConfirm = ko.observable(useDayApproverConfirm);
        }
    }

    export class UseSetting {
        //月別確認を利用する
        monthlyConfirm: boolean;
        //上司確認を利用する
        useBossConfirm: boolean;
        //本人確認を利用する
        usePersonConfirm: boolean;
    }

    export class CellColor {
        columnKey: any;
        rowId: any;
        innerIdx: any;
        clazz: any;
        constructor(columnKey: any, rowId: any, clazz: any, innerIdx?: any) {
            this.columnKey = columnKey;
            this.rowId = rowId;
            this.innerIdx = innerIdx;
            this.clazz = clazz;
        }
    }

    export class Time {
        year: string;
        month: string;
        day: string;
        weekDay: string;
        yearMonthDay: string;

        constructor(ymd: Date) {
            this.year = moment(ymd).format('YYYY');
            this.month = moment(ymd).format('M');
            this.day = moment(ymd).format('D');
            this.weekDay = moment(ymd).format('dd');
            this.yearMonthDay = this.year + moment(ymd).format('MM') + moment(ymd).format('DD');
        }
    }

}