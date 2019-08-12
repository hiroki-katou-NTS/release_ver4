import { _, Vue } from '@app/provider';
import { component, Watch, Prop } from '@app/core/component';
import { Kafs05Model } from '../common/CommonClass';
@component({
    name: 'kafS05a3',
    template: require('./index.html'),
    resource: require('../../resources.json')
})
export class KafS05aStep3Component extends Vue {

    @Prop()
    public kafs05ModelStep3: Kafs05Model;

    private comboDivergenceReason: string = null;
    private comboBoxReason: string = null;
    private appReason: string = null;
    private divergenceReason: string = null;

    public created() {
        this.convertDisplayItem();
    }

    public registerClick() {
        this.$mask('show', { message: true });
        this.beforeRegisterColorConfirm();
    }

    public convertDisplayItem() {
        let self = this.kafs05ModelStep3;
        this.comboBoxReason = this.getComboBoxReason(self.selectedReason, self.reasonCombo, self.typicalReasonDisplayFlg);
        this.comboDivergenceReason = this.getComboBoxReason(self.selectedReason2, self.reasonCombo2, self.displayDivergenceReasonForm);

        if (this.comboBoxReason == '') {
            this.appReason = self.multilContent;
        } else {
            this.appReason = this.comboBoxReason + '</BR>' + self.multilContent;
        }

        if (this.comboDivergenceReason == '') {
            this.divergenceReason = self.multilContent2;
        } else {
            this.divergenceReason = this.comboDivergenceReason + '</BR>' + self.multilContent2;
        }
    }

    public beforeRegisterColorConfirm() {
        let self = this.kafs05ModelStep3;

        let overTimeShiftNightTmp: number = null;
        let flexExessTimeTmp: number = null;
        for (let i = 0; i < self.overtimeHours.length; i++) {
            if (self.overtimeHours[i].frameNo == 11) {
                overTimeShiftNightTmp = self.overtimeHours[i].applicationTime;
            }
            if (self.overtimeHours[i].frameNo == 12) {
                flexExessTimeTmp = self.overtimeHours[i].applicationTime;
            }
        }
        let overtime: any = {
            applicationDate: self.appDate,
            prePostAtr: self.prePostSelected,
            applicantSID: self.employeeID,
            applicationReason: self.multilContent,
            workTypeCode: self.workTypeCd,
            siftTypeCode: self.siftCD,
            workClockFrom1: self.workTimeInput.start,
            workClockTo1: self.workTimeInput.end,
            bonusTimes: self.bonusTimes,
            breakTimes: self.breakTimes,
            overtimeHours: self.overtimeHours,
            restTime: self.restTime,
            overTimeShiftNight: overTimeShiftNightTmp == null ? null : overTimeShiftNightTmp,
            flexExessTime: flexExessTimeTmp == null ? null : flexExessTimeTmp,
            divergenceReasonContent: this.comboDivergenceReason,
            sendMail: self.checkBoxValue,
            overtimeAtr: self.overtimeAtr,
            calculateFlag: self.calculateFlag,
            appReasonID: this.comboBoxReason,
            divergenceReasonArea: self.multilContent2,
            checkOver1Year: true,
            checkAppDate: false
        };

        if (null != self.appID) {
            overtime.version = self.version;
            overtime.appID = self.appID;
            // EDITMODE 設定
            overtime.user = self.user;
            overtime.reflectPerState = self.reflectPerState;
            delete overtime.checkOver1Year;
            delete overtime.checkAppDate;
        }

        //登録
        if (null == self.appID) {
            this.$http.post('at', servicePath.beforeRegisterColorConfirm, overtime).then((result: { data: any }) => {
                overtime.checkOver1Year = false;
                this.contentBefRegColorConfirmDone(overtime, result.data);
            }).catch((res: any) => {
                if (res.messageId == 'Msg_1518') {
                    this.$modal.confirm({ messageId: res.messageId }).then((value) => {
                        if (value == 'yes') {
                            overtime.checkOver1Year = false;
                            this.$http.post('at', servicePath.beforeRegisterColorConfirm, overtime).then((result: { data: any }) => {
                                this.contentBefRegColorConfirmDone(overtime, result.data);
                            }).catch((res: any) => {
                                this.$modal.error({ messageId: res.messageId, messageParams: res.parameterIds });
                                this.$mask('hide');
                            });
                        } else {
                            this.$mask('hide');
                        }
                    });
                } else {
                    this.$modal.error({ messageId: res.messageId, messageParams: res.parameterIds });
                    this.$mask('hide');
                }
            });
            //更新
        } else {
            this.$http.post('at', servicePath.beforeRegisterColorConfirm, overtime).then((result: { data: any }) => {
                let res = result.data;
                if (res.confirm) {
                    this.$modal.confirm({ messageId: res.messageId, messageParams: res.parameterIds }).then((value) => {
                        if (value == 'yes') {
                            this.beforeUpdateProcess(overtime);
                        } else {
                            this.$mask('hide');
                        }
                    });
                } else {
                    this.beforeUpdateProcess(overtime);
                }
            }).catch((res: any) => {
                this.$modal.error({ messageId: res.messageId, messageParams: res.parameterIds });
                this.$mask('hide');
            });
        }


    }

    public contentBefRegColorConfirmDone(overtime, data) {
        let self = this.kafs05ModelStep3;
        if (data.confirm) {
            this.$modal.confirm({ messageId: data.msgID, messageParams: data.params }).then((value) => {
                //登録処理を実行
                if (value == 'yes') {
                    this.beforeRegisterProcess(overtime);
                } else {
                    this.$mask('hide');
                }
            });
        } else {
            this.beforeRegisterProcess(overtime);
        }
    }

    public beforeRegisterProcess(overtime: any) {
        let self = this.kafs05ModelStep3;
        this.$http.post('at', servicePath.checkBeforeRegister, overtime).then((result: { data: any }) => {
            overtime.appOvertimeDetail = result.data.appOvertimeDetail;
            this.confirmInconsistency(overtime);
        }).catch((res: any) => {
            if (_.isEmpty(res.errors)) {
                this.$modal.error({ messageId: res.messageId, messageParams: res.parameterIds });
                this.$mask('hide');
            } else {
                let errors = res.errors;
                for (let i = 0; i < errors.length; i++) {
                    let error = errors[i];
                    if (error.messageId == 'Msg_1538') {
                        //error.kafs05ModeleterIds = [
                        //formatYearMonth(parseInt(error.kafs05ModeleterIds[4])), 
                        //formatYearMonth(parseInt(error.kafs05ModeleterIds[5])),
                        //format.byId('Clock_Short_HM', parseInt(error.kafs05ModeleterIds[6])), 
                        //.byId('Clock_Short_HM', parseInt(error.kafs05ModeleterIds[7]))
                        //];
                    } else {
                        //error.kafs05ModeleterIds = [
                        //.byId('Clock_Short_HM', parseInt(error.kafs05ModeleterIds[4])), 
                        //.byId('Clock_Short_HM', parseInt(error.kafs05ModeleterIds[5]))
                        //];
                    }
                    //error.message = resource.getMessage(error.messageId, error.kafs05ModeleterIds);
                }
                //nbundledErrors({ errors: errors })
                this.$mask('hide');
            }
        });
    }

    public beforeUpdateProcess(overtime: any) {
        this.$http.post('at', servicePath.checkBeforeRegister, overtime).then((result: { data: any }) => {
            overtime.appOvertimeDetail = result.data.appOvertimeDetail;
            this.updateOvertime(overtime);
        }).catch((res: any) => {
            if (_.isEmpty(res.errors)) {
                this.$modal.error({ messageId: res.messageId, messageParams: res.parameterIds });
                this.$mask('hide');
            } else {
                let errors = res.errors;
                for (let i = 0; i < errors.length; i++) {
                    let error = errors[i];
                    if (error.messageId == 'Msg_1538') {
                        //error.kafs05ModeleterIds = [
                        //formatYearMonth(parseInt(error.kafs05ModeleterIds[4])), 
                        //formatYearMonth(parseInt(error.kafs05ModeleterIds[5])),
                        //format.byId('Clock_Short_HM', parseInt(error.kafs05ModeleterIds[6])), 
                        //.byId('Clock_Short_HM', parseInt(error.kafs05ModeleterIds[7]))
                        //];
                    } else {
                        //error.kafs05ModeleterIds = [
                        //.byId('Clock_Short_HM', parseInt(error.kafs05ModeleterIds[4])), 
                        //.byId('Clock_Short_HM', parseInt(error.kafs05ModeleterIds[5]))
                        //];
                    }
                    //error.message = resource.getMessage(error.messageId, error.kafs05ModeleterIds);
                }
                //nbundledErrors({ errors: errors })
                this.$mask('hide');
            }
        });
    }

    public confirmInconsistency(overtime: any) {
        let self = this.kafs05ModelStep3;
        this.$http.post('at', servicePath.confirmInconsistency, overtime).then((result: { data: any }) => {
            if (!_.isEmpty(result.data)) {
                this.$modal.confirm({ messageId: result.data[0], messageParams: [result.data[1], result.data[2]] }).then((value) => {
                    if (value == 'yes') {
                        this.registerData(overtime);
                    } else {
                        this.$mask('hide');
                    }
                });
            } else {
                //登録処理を実行
                this.registerData(overtime);
            }
        }).catch((res: any) => {
            this.$modal.error({ messageId: res.messageId, messageParams: res.parameterIds });
            this.$mask('hide');
        });
    }

    public registerData(overtime: any) {
        let self = this.kafs05ModelStep3;

        this.$http.post('at', servicePath.createOvertime, overtime).then((result: { data: any }) => {
            this.kafs05ModelStep3.appID = result.data.appID;
            this.$emit('toStep4', this.kafs05ModelStep3);
            this.$mask('hide');

        }).catch((res: any) => {
            this.$mask('hide');
            this.$modal.error({ messageId: res.messageId, messageParams: res.parameterIds });
        });
    }

    public updateOvertime(overtime: any) {
        this.$http.post('at', servicePath.updateOvertime, overtime).then((result: { data: any }) => {
            this.$emit('toStep4', this.kafs05ModelStep3);
            this.$mask('hide');

        }).catch((res: any) => {
            if (res.optimisticLock == true) {
                this.$modal.error({ messageId: 'Msg_197' });
            } else {
                this.$modal.error({ messageId: res.messageId, messageParams: res.parameterIds });
            }
            this.$mask('hide');
        });
    }

    public getComboBoxReason(selectID: string, listID: Array<any>, displaySet: boolean): string {
        if (!displaySet || _.isNil(selectID) || selectID == '') {
            return '';
        }
        let reasonValue = _.find(listID, (o) => o.reasonId == selectID).reasonName;
        if (_.isNil(reasonValue)) {
            return '';
        }

        return reasonValue;
    }
}
const servicePath = {
    beforeRegisterColorConfirm: 'at/request/application/overtime/beforeRegisterColorConfirm',
    checkBeforeRegister: 'at/request/application/overtime/checkBeforeRegister',
    confirmInconsistency: 'at/request/application/holidaywork/confirmInconsistency',
    createOvertime: 'at/request/application/overtime/create',
    updateOvertime: 'at/request/application/overtime/update',
};