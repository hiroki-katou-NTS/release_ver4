import { Vue, _ } from '@app/provider';

export class KafS00ShrComponent extends Vue {

    public appDispInfoStartupOutput: any = null;

    public loadCommonSetting(
        appType: number,
        employeeID?: string,
        holidayAppType?: number,
        dateLst?: Array<string>, 
        overtimeAppAtr?: number) {
        const self = this;
        let command = { employeeID, appType, holidayAppType, dateLst, overtimeAppAtr };

        return self.$http.post('at', API.getStartNewMob, command)
        .then((successData: any) => {
            self.appDispInfoStartupOutput = successData.data;
            let useDivision = self.appDispInfoStartupOutput.appDispInfoWithDateOutput.approvalFunctionSet.appUseSetLst[0].useDivision,
                recordDate = self.appDispInfoStartupOutput.appDispInfoNoDateOutput.applicationSetting.recordDate,
                opErrorFlag = self.appDispInfoStartupOutput.appDispInfoWithDateOutput.opErrorFlag,
                msgID = '';
            if (useDivision == 0) {
                self.$modal.error('Msg_323').then(() => {
                    if (recordDate == 0) {
                        self.$goto('ccg008a');   
                    }
                });
                if (recordDate == 0) {
                    return false;
                }

                return true;
            }
            
            if (_.isNull(opErrorFlag)) {
                return true;    
            }
            switch (opErrorFlag) {
                case 1:
                    msgID = 'Msg_324';
                    break;
                case 2: 
                    msgID = 'Msg_238';
                    break;
                case 3:
                    msgID = 'Msg_237';
                    break;
                default: 
                    break;
            }  
            if (_.isEmpty(msgID)) { 
                return true;
            }
            self.$modal.error({ messageId: msgID }).then(() => {
                if (recordDate == 0) {
                    self.$goto('ccg008a');    
                }    
            });

            return false;
        }).catch((res: any) => {
            if (res.messageId == 'Msg_426') {
                self.$modal.error('Msg_426').then(() => {
                    self.$goto('ccg008a');
                });    
            } else {
                self.$modal.error(res.message).then(() => {
                    self.$goto('ccg008a');
                }); 
            }

            return false;
        });
    }

}

export enum AppType {
    OVER_TIME_APPLICATION = 0, // 残業申請
    ABSENCE_APPLICATION = 1,  // 休暇申請
    WORK_CHANGE_APPLICATION = 2, // 勤務変更申請
    BUSINESS_TRIP_APPLICATION = 3, // 出張申請
    GO_RETURN_DIRECTLY_APPLICATION = 4, // 直行直帰申請
    LEAVE_TIME_APPLICATION = 6, // 休出時間申請
    STAMP_APPLICATION = 7, // 打刻申請
    ANNUAL_HOLIDAY_APPLICATION = 8, // 時間休暇申請
    EARLY_LEAVE_CANCEL_APPLICATION = 9, // 遅刻早退取消申請
    COMPLEMENT_LEAVE_APPLICATION = 10, // 振休振出申請
    OPTIONAL_ITEM_APPLICATION = 15, // 任意項目申請
}

export enum AppTypeName {
    OVER_TIME_APPLICATION = '残業申請',
    ABSENCE_APPLICATION = '休暇申請',
    WORK_CHANGE_APPLICATION = '勤務変更申請',
    BUSINESS_TRIP_APPLICATION = '出張申請',
    GO_RETURN_DIRECTLY_APPLICATION = '直行直帰申請',
    LEAVE_TIME_APPLICATION = '休出時間申請',
    STAMP_APPLICATION = '打刻申請',
    ANNUAL_HOLIDAY_APPLICATION = '時間休暇申請',
    EARLY_LEAVE_CANCEL_APPLICATION = '遅刻早退取消申請',
    COMPLEMENT_LEAVE_APPLICATION = '振休振出申請',
    OPTIONAL_ITEM_APPLICATION = '任意項目申請'
}

const API = {
    getStartNewMob: 'at/request/app/smartphone/getStartNewMob'
};