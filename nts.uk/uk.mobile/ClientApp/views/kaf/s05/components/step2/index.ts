import { _, Vue } from '@app/provider';
import { component, Prop, Watch } from '@app/core/component';
import { Kafs05Model } from '../common/CommonClass';

@component({
    name: 'KafS05_2',
    template: require('./index.html'),
    resource: require('../../resources.json'),
    validations: {
        kafs05ModelStep2: {
            overtimeHours: {
                check: {
                    test(value: any) {
                        if (this.kafs05ModelStep2.enableOvertimeInput) {
                            for (let i of value) {
                                if (!_.isNil(i.applicationTime)) {
                                    return true;
                                }
                            }

                            return false;
                        }

                        return true;
                    },
                    messageId: 'MsgB_30'
                },

            },
            selectedReason: {
                checkNull: {
                    test(value: any) {
                        if (this.kafs05ModelStep2.requiredReason && (this.kafs05ModelStep2.typicalReasonDisplayFlg || this.kafs05ModelStep2.displayAppReasonContentFlg)) {
                            if ((value + this.kafs05ModelStep2.multilContent).length == 0) {
                                return false;
                            }

                            return true;
                        }

                        return true;
                    },
                    messageId: 'Msg_115'
                },
                checkMaxLength: {
                    test(value: any) {
                        let comboBoxReason: string = this.getComboBoxReason(value, this.kafs05ModelStep2.reasonCombo, this.kafs05ModelStep2.typicalReasonDisplayFlg);
                        if (this.kafs05ModelStep2.typicalReasonDisplayFlg || this.kafs05ModelStep2.displayAppReasonContentFlg) {
                            if (this.countHalf(comboBoxReason + '\n' + this.kafs05ModelStep2.multilContent) > 400) {
                                return false;
                            }
                        }
                        
                        return true;
                    },
                    messageId: 'Msg_960'
                }
            },
            multilContent: {
                checkNull: {
                    test(value: any) {
                        if (this.kafs05ModelStep2.requiredReason && (this.kafs05ModelStep2.typicalReasonDisplayFlg || this.kafs05ModelStep2.displayAppReasonContentFlg)) {
                            if ((value + this.kafs05ModelStep2.selectedReason).length == 0) {
                                return false;
                            }

                            return true;
                        }

                        return true;
                    },
                    messageId: 'Msg_115'
                },
                checkMaxLength: {
                    test(value: any) {
                        let comboBoxReason: string = this.getComboBoxReason(this.kafs05ModelStep2.selectedReason, this.kafs05ModelStep2.reasonCombo, this.kafs05ModelStep2.typicalReasonDisplayFlg);
                        if (this.kafs05ModelStep2.typicalReasonDisplayFlg || this.kafs05ModelStep2.displayAppReasonContentFlg) {
                            if (this.countHalf(comboBoxReason + '\n' + value) > 400) {
                                return false;
                            }
                        }

                        return true;
                    },
                    messageId: 'Msg_960'
                }
            },
            selectedReason2: {
                checkMaxLength: {
                    test(value: any) {
                        let comboDivergenceReason = this.getComboBoxReason(value, this.kafs05ModelStep2.reasonCombo2, this.kafs05ModelStep2.displayDivergenceReasonForm);
                        if (this.kafs05ModelStep2.displayDivergenceReasonForm || this.kafs05ModelStep2.displayDivergenceReasonInput) {
                            if (this.countHalf(comboDivergenceReason + '\n' + this.kafs05ModelStep2.multilContent2) > 400) {
                                return false;
                            }
                        }

                        return true;
                    },
                    messageId: 'Msg_960'
                }
            },
            multilContent2: {
                checkMaxLength: {
                    test(value: any) {
                        let comboDivergenceReason = this.getComboBoxReason(this.kafs05ModelStep2.selectedReason2, this.kafs05ModelStep2.reasonCombo2, this.kafs05ModelStep2.displayDivergenceReasonForm);
                        if (this.kafs05ModelStep2.displayDivergenceReasonForm || this.kafs05ModelStep2.displayDivergenceReasonInput) {
                            if (this.countHalf(comboDivergenceReason + '\n' + value) > 400) {
                                return false;
                            }
                        }

                        return true;
                    },
                    messageId: 'Msg_960'
                }
            },
            constraint: 'AppReason'
        },

    },
    constraints: ['nts.uk.ctx.at.request.dom.application.AppReason'],
})
export class KafS05aStep2Component extends Vue {
    @Prop()
    public kafs05ModelStep2: Kafs05Model;

    @Watch('kafs05ModelStep2.selectedReason')
    public validateSelectedReason() {
        this.$validate('kafs05ModelStep2.multilContent');
        this.$validate('kafs05ModelStep2.selectedReason');
    }

    @Watch('kafs05ModelStep2.multilContent')
    public validateMultilContent() {
        this.$validate('kafs05ModelStep2.multilContent');
        this.$validate('kafs05ModelStep2.selectedReason');
    }

    @Watch('kafs05ModelStep2.selectedReason2')
    public validateSelectedReason2() {
        this.$validate('kafs05ModelStep2.multilContent2');
        this.$validate('kafs05ModelStep2.selectedReason2');
    }

    @Watch('kafs05ModelStep2.multilContent2')
    public validateMultilContent2() {
        this.$validate('kafs05ModelStep2.multilContent2');
        this.$validate('kafs05ModelStep2.selectedReason2');
    }

    @Watch('kafs05ModelStep2.overtimeHours', { deep: true, immediate: false })
    public validateOvertimeHours(value: any, oldValue: any) {
        this.$validate('kafs05ModelStep2.overtimeHours');
    }

    private hasPreAppError: boolean = false;
    private hasActualError: boolean = false;

    public created() {
        this.kafs05ModelStep2.step1Start = false;
        this.kafs05ModelStep2.overtimeHours.forEach((overtimeHour) => {
            if (overtimeHour.preAppExceedState) {
                this.hasPreAppError = true;
            }
            if (overtimeHour.actualExceedState) {
                this.hasActualError = true;
            }
        });
    }

    public next() {
        let self = this.kafs05ModelStep2;

        // 打刻漏れ 超過エラー
        if ((self.actualStatus == 1 && this.hasActualError && self.performanceExcessAtr == 2)) {
            window.scrollTo({ top: 0, behavior: 'smooth' });

            return;
        } 

        this.$validate();
        if (!this.$valid) {
            window.scrollTo({ top: 0, behavior: 'smooth' });

            return;
        }

        if (!self.displayCaculationTime) {
            this.$emit('toStep3', self);

            return;
        }

        this.$mask('show', { message: true });
        let param: any = {
            overtimeHours: _.map(self.overtimeHours, (item) => this.initCalculateData(item)),
            bonusTimes: _.map(self.bonusTimes, (item) => this.initCalculateData(item)),
            prePostAtr: self.prePostSelected,
            appDate: _.isNil(self.appDate) ? null : this.$dt(self.appDate),
            siftCD: self.siftCD,
            workTypeCode: self.workTypeCd,
            startTimeRests: _.isEmpty(self.restTime) ? [] : _.map(self.restTime, (x) => x.restTimeInput.start),
            endTimeRests: _.isEmpty(self.restTime) ? [] : _.map(self.restTime, (x) => x.restTimeInput.end),
            startTime: _.isNil(self.workTimeInput.start) ? null : self.workTimeInput.start,
            endTime: _.isNil(self.workTimeInput.end) ? null : self.workTimeInput.end,
            displayCaculationTime: self.displayCaculationTime,
            isFromStepOne: false
        };

        let overtimeHoursResult: Array<any>;
        let overtimeHoursbk = self.overtimeHours.slice().concat(self.bonusTimes.slice());

        this.$http.post('at', servicePath.getCalculationResultMob, param).then((result: { data: any }) => {
            _.remove(self.overtimeHours);
            _.remove(self.bonusTimes);
            self.beforeAppStatus = result.data.preActualColorResult.beforeAppStatus;
            self.actualStatus = result.data.preActualColorResult.actualStatus;
            self.preExcessDisplaySetting = result.data.preExcessDisplaySetting;
            overtimeHoursResult = result.data.preActualColorResult.resultLst;
            if (overtimeHoursResult != null) {
                for (let i = 0; i < overtimeHoursResult.length; i++) {
                    //残業時間
                    if (overtimeHoursResult[i].attendanceID == 1) {
                        if (overtimeHoursResult[i].frameNo != 11 && overtimeHoursResult[i].frameNo != 12) {
                            self.overtimeHours.push({
                                companyID: '',
                                appID: '',
                                attendanceID: overtimeHoursResult[i].attendanceID,
                                attendanceName: '',
                                frameNo: overtimeHoursResult[i].frameNo,
                                timeItemTypeAtr: 0,
                                frameName: overtimeHoursbk[i].frameName,
                                applicationTime: overtimeHoursResult[i].appTime,
                                preAppTime: overtimeHoursResult[i].preAppTime,
                                caculationTime: overtimeHoursResult[i].actualTime,
                                nameID: '#[KAF005_55]',
                                itemName: 'KAF005_55',
                                color: '',
                                preAppExceedState: overtimeHoursResult[i].preAppError,
                                actualExceedState: overtimeHoursResult[i].actualError,
                            });
                        } else if (overtimeHoursResult[i].frameNo == 11) {
                            self.overtimeHours.push({
                                companyID: '',
                                appID: '',
                                attendanceID: overtimeHoursResult[i].attendanceID,
                                attendanceName: '',
                                frameNo: overtimeHoursResult[i].frameNo,
                                timeItemTypeAtr: 0,
                                frameName: 'KAF005_63',
                                applicationTime: overtimeHoursResult[i].appTime,
                                preAppTime: overtimeHoursResult[i].preAppTime,
                                caculationTime: overtimeHoursResult[i].actualTime,
                                nameID: '#[KAF005_64]',
                                itemName: 'KAF005_55',
                                color: '',
                                preAppExceedState: overtimeHoursResult[i].preAppError,
                                actualExceedState: overtimeHoursResult[i].actualError,
                            });
                        } else if (overtimeHoursResult[i].frameNo == 12) {
                            self.overtimeHours.push({
                                companyID: '',
                                appID: '',
                                attendanceID: overtimeHoursResult[i].attendanceID,
                                attendanceName: '',
                                frameNo: overtimeHoursResult[i].frameNo,
                                timeItemTypeAtr: 0,
                                frameName: 'KAF005_65',
                                applicationTime: overtimeHoursResult[i].appTime,
                                preAppTime: overtimeHoursResult[i].preAppTime,
                                caculationTime: overtimeHoursResult[i].actualTime,
                                nameID: '#[KAF005_66]',
                                itemName: 'KAF005_55',
                                color: '',
                                preAppExceedState: overtimeHoursResult[i].preAppError,
                                actualExceedState: overtimeHoursResult[i].actualError,
                            });
                        }
                    }
                    //加給時間
                    if (overtimeHoursResult[i].attendanceID == 3) {
                        self.bonusTimes.push({
                            companyID: '',
                            appID: '',
                            attendanceID: overtimeHoursResult[i].attendanceID,
                            attendanceName: '',
                            frameNo: overtimeHoursResult[i].frameNo,
                            timeItemTypeAtr: overtimeHoursbk[i].timeItemTypeAtr,
                            frameName: overtimeHoursbk[i].frameName,
                            applicationTime: overtimeHoursResult[i].appTime,
                            preAppTime: overtimeHoursResult[i].preAppTime,
                            caculationTime: null,
                            nameID: '',
                            itemName: '',
                            color: '',
                            preAppExceedState: overtimeHoursResult[i].preAppError,
                            actualExceedState: overtimeHoursResult[i].actualError,
                        });
                    }
                }
            }
            this.$emit('toStep3', this.kafs05ModelStep2);
            this.$mask('hide');
        }).catch((res: any) => {
            if (res.messageId == 'Msg_424') {
                this.$modal.error({ messageId: 'Msg_424', messageParams: [res.parameterIds[0], res.parameterIds[1], res.parameterIds[2]] });
            } else if (res.messageId == 'Msg_1508') {
                this.$modal.error({ messageId: 'Msg_1508', messageParams: [res.parameterIds[0]] });
            } else if (res.messageId == 'Msg_426') {
                this.$modal.error({ messageId: 'Msg_426', messageParams: [res.parameterIds[0]] }).then(() => {
                    this.$goto('ccg007b');
                    this.$auth.logout();
                });
            } else {
                this.$modal.error({ messageId: res.messageId }).then(() => {
                    this.$goto('ccg008a');
                });
            }
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

    public countHalf(text: string) {
        let count = 0;
        for (let i = 0; i < text.length; i++) {
            let c = text.charCodeAt(i);

            // 0x20 ～ 0x80: 半角記号と半角英数字
            // 0xff61 ～ 0xff9f: 半角カタカナ
            if ((0x20 <= c && c <= 0x7e) || (0xff61 <= c && c <= 0xff9f)) {
                count += 1;
            } else {
                count += 2;
            }
        }

        return count;
    }

    public initCalculateData(item: any): any {
        return {
            companyID: item.companyID,
            appID: item.appID,
            attendanceID: item.attendanceID,
            attendanceName: item.attendanceName,
            frameNo: item.frameNo,
            timeItemTypeAtr: item.timeItemTypeAtr,
            frameName: item.frameName,
            applicationTime: item.applicationTime,
            preAppTime: null,
            caculationTime: null,
            nameID: item.nameID,
            itemName: item.itemName
        };
    }
}

const servicePath = {
    getCalculationResultMob: 'at/request/application/overtime/getCalculationResultMob',
};