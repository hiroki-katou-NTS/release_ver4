module nts.uk.com.view.cmf004.i.viewmodel {
    import getText = nts.uk.resource.getText;
    import getShared = nts.uk.ui.windows.getShared;
    export class ScreenModel {
        elapsedTime: KnockoutObservable<string> = ko.observable("00:00:00");
        //I2_1
        statusProcess: KnockoutObservable<string> = ko.observable('');
        numberCategory: KnockoutObservable<string> = ko.observable('');
        numberTotalCategory: KnockoutObservable<string> = ko.observable('');
        employeeProcess: KnockoutObservable<string> = ko.observable('');
        datetimeProcess: KnockoutObservable<string> = ko.observable('');
        numberError: KnockoutObservable<string> = ko.observable('');
        //I4_1
        code: KnockoutObservable<string> = ko.observable('');
        saveName: KnockoutObservable<string> = ko.observable('');
        recoverySourceCode: KnockoutObservable<string> = ko.observable('');

        //Send to Service
        recoveryProcessingId: KnockoutObservable<string> = ko.observable('');
        employeeList: KnockoutObservableArray<any> = ko.observableArray([]);
        recoveryCategoryList: KnockoutObservableArray<any> = ko.observableArray([]);
        recoveryFile: KnockoutObservable<string> = ko.observable('');
        recoverySourceName: KnockoutObservable<string> = ko.observable('');
        supplementaryExplanation: KnockoutObservable<string> = ko.observable('');
        recoveryMethodOptions: KnockoutObservable<string> = ko.observable('');
        saveForm: KnockoutObservable<string> = ko.observable('');
        
        store_del_ProcessingId: KnockoutObservable<string> = ko.observable('');

        //status follow
        isEnding: KnockoutObservable<boolean> = ko.observable(false);

        //time start
        timeStart: any;
        // interval 1000ms request to server
        interval: any;
        constructor() {
            let self = this;
            self.timeStart = new Date();
            if (getShared("CMF004IParams")) {
                let recoveryInfo = getShared("CMF004IParams");
                if (recoveryInfo) {
                    let self = this;
                    self.saveForm(recoveryInfo.saveForm);
                    self.recoveryProcessingId(recoveryInfo.recoveryProcessingId);
                    self.employeeList(recoveryInfo.employeeList);
                    self.recoveryCategoryList(recoveryInfo.recoveryCategoryList);
                    self.recoveryFile(recoveryInfo.recoveryFile);
                    self.recoverySourceCode(recoveryInfo.recoverySourceCode);
                    self.recoverySourceName(recoveryInfo.recoverySourceName);
                    self.supplementaryExplanation(recoveryInfo.supplementaryExplanation);
                    self.recoveryMethodOptions(recoveryInfo.recoveryMethodOptions);
                    self.store_del_ProcessingId(recoveryInfo.store_del_ProcessingId);
                }
            }
        }

        start(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            dfd.resolve(self);
            let command = {
                saveSetCode: self.recoverySourceCode(),
                saveForm: self.saveForm(),
                saveName: self.recoverySourceName(),
                recoveryProcessingId: self.recoveryProcessingId(),
                employeeList: self.employeeList(),
                recoveryCategoryList: self.recoveryCategoryList(),
                recoveryFile: self.recoveryFile(),
                supplementaryExplanation: self.supplementaryExplanation(),
                recoveryMethodOptions: self.recoveryMethodOptions(),
                store_del_ProcessingId: self.store_del_ProcessingId()
            };
            console.log(command);
            service.performDataRecover(command).done(() => {
            });
            return dfd.promise();
        }

        //// 1秒おきに下記を実行
        //データ保存監視処理: 
        startFollow() {
            let self = this;
            self.interval = setInterval(self.followProsessing, 1000, self);
        }

        public followProsessing(self): void {
            let recoveryProcessingId = self.recoveryProcessingId();

            service.followProsess(recoveryProcessingId).done(function(res: any) {
                let recoveryProcessing = res;

                // 経過時間＝現在時刻－開始時刻
                let startTime = self.timeStart;
                let timeNow = new Date();
                let result = moment.utc(moment(timeNow, "HH:mm:ss").diff(moment(startTime, "HH:mm:ss"))).format("HH:mm:ss");
                self.elapsedTime(result);
                //init I2_1
                self.statusProcess(self.getStatusEnum(recoveryProcessing.operatingCondition));
                self.numberCategory(recoveryProcessing.categoryCnt);
                self.numberTotalCategory(recoveryProcessing.categoryTotalCount);
                self.employeeProcess(recoveryProcessing.processTargetEmpCode);
                self.datetimeProcess(recoveryProcessing.recoveryDate);
                self.numberError(recoveryProcessing.errorCount);
                //init I4_1
                self.code(self.recoverySourceCode);
                self.saveName(self.recoverySourceName);

                // 完了, 中断終了, 異常終了
                if ((recoveryProcessing.operatingCondition == 3) || (recoveryProcessing.operatingCondition == 1) || (recoveryProcessing.operatingCondition == 5)) {
                    // stop auto request to servers
                    service.deletePerformDataRecover(recoveryProcessingId);
                    clearInterval(self.interval);
                    self.isEnding(true);
                    $('#I5_2').focus();
                }

            });
        }

        // breakFollow popup
        public breakFollow(): void {
            let self = this;
            // stop auto request to server
            //clearInterval(self.interval);
            //update status end 
            nts.uk.ui.dialog.confirm({ messageId: "Msg_612" })
                .ifYes(function() {
                    let paramBreakFollowProcessing = {
                        dataRecoveryProcessId: self.recoveryProcessingId()
                    };
                    service.breakFollowProcessing(paramBreakFollowProcessing).done(function(res: any) {
                        $('#I5_2').focus();
                    });
                });
        }

        // close popup
        close(): void {
            nts.uk.ui.windows.close();
        }

        public getStatusEnum(value): string {
            if (value === 0) {
                return getText('CMF004_311');
            } else if (value === 1) {
                return getText('CMF004_315');
            } else if (value === 2) {
                return getText('CMF004_312');
            } else if (value === 3) {
                return getText('CMF004_313');
            } else if (value === 4) {
                return getText('CMF004_310');
            } else if (value === 5) {
                return getText('CMF004_314');
            }
        }
    }
}


