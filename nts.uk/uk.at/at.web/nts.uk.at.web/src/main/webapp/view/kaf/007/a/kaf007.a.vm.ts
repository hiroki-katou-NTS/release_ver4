module nts.uk.at.view.kaf007.a.viewmodel {
    import common = nts.uk.at.view.kaf007.share.common;
    import service = nts.uk.at.view.kaf007.share.service;
    import dialog = nts.uk.ui.dialog;
    import appcommon = nts.uk.at.view.kaf000.shr.model;
    import setShared = nts.uk.ui.windows.setShared;
    import text = nts.uk.resource.getText;
    import isNullOrEmpty = nts.uk.util.isNullOrEmpty;
    export class ScreenModel {
        multiDate: KnockoutObservable<boolean> = ko.observable(false);
        screenModeNew: KnockoutObservable<boolean> = ko.observable(true);
        appWorkChange: KnockoutObservable<common.AppWorkChangeCommand> = ko.observable(new common.AppWorkChangeCommand());
        recordWorkInfo: KnockoutObservable<common.RecordWorkInfo> = ko.observable(new common.RecordWorkInfo());
        //A3 事前事後区分:表示/活性
        prePostDisp: KnockoutObservable<boolean> = ko.observable(false);
        prePostEnable: KnockoutObservable<boolean> = ko.observable(false);
        requiredPrePost: KnockoutObservable<boolean> = ko.observable(false);
        //A5 勤務を変更する:表示/活性
        isWorkChange: KnockoutObservable<boolean> = ko.observable(true);
        workChangeAtr: KnockoutObservable<boolean> = ko.observable(false);
        //A8 勤務時間２
        isMultipleTime: KnockoutObservable<boolean> = ko.observable(false);
        //kaf000
        kaf000_a: kaf000.a.viewmodel.ScreenModel;
        employeeID: string = "";
        //申請者
        employeeName: KnockoutObservable<string> = ko.observable("");
        //comboBox 定型理由
        typicalReasonDisplayFlg: KnockoutObservable<boolean> = ko.observable(false);
        displayAppReasonContentFlg: KnockoutObservable<boolean> = ko.observable(false);
        reasonCombo: KnockoutObservableArray<common.ComboReason> = ko.observableArray([]);
        selectedReason: KnockoutObservable<string> = ko.observable('');
        //MultilineEditor
        requiredReason: KnockoutObservable<boolean> = ko.observable(false);
        multilContent: KnockoutObservable<string> = ko.observable('');
        //A10_1 休日は除く
        excludeHolidayAtr: KnockoutObservable<boolean> = ko.observable(true);
        //Approval 
        approvalSource: Array<common.AppApprovalPhase> = [];
        //menu-bar 
        enableSendMail: KnockoutObservable<boolean> = ko.observable(false);
        checkBoxValue: KnockoutObservable<boolean> = ko.observable(false);
        dateFormat: string = 'YYYY/MM/DD';
        //画面モード(表示/編集)
        editable: KnockoutObservable<boolean> = ko.observable(true);

        // Valid Period
        datePeriod: KnockoutObservable<any> = ko.observable({});
        appChangeSetting: KnockoutObservable<common.AppWorkChangeSetting> = ko.observable(new common.AppWorkChangeSetting());
        employeeList = ko.observableArray([]);
        selectedEmployee = ko.observable(null);
        totalEmployeeText = ko.observable("");
        
        dateSingle: KnockoutObservable<any> = ko.observable(null);
        targetDate: any = moment(new Date()).format("YYYY/MM/DD");
        requiredCheckTime: KnockoutObservable<boolean> = ko.observable(this.isWorkChange() && true);
        timeRequired: KnockoutObservable<boolean> = ko.observable(false);
        showExcludeHoliday: KnockoutObservable<boolean> = ko.observable(false);
        constructor() {
            let self = this,
                application = self.appWorkChange().application();
            __viewContext.transferred.ifPresent(data => {
                if(!nts.uk.util.isNullOrUndefined(data.appDate)){
                    self.targetDate = moment(data.appDate).format("YYYY/MM/DD");
                    self.dateSingle(self.targetDate);
                }
                if(!nts.uk.util.isNullOrEmpty(data.employeeIds)){
                    self.employeeID = data.employeeIds[0];
                }
                return null;
            });

            // 申請日を変更する          
            //Start Date
            self.datePeriod.subscribe(value => {
                if ($("#daterangepicker").find(".ntsDateRangeComponent").ntsError("hasError")) {
                    return;
                }
                let startDate, endDate;
                startDate = value.startDate;
                endDate = value.endDate;
                if(!startDate && !endDate){
                    return;
                }
                 nts.uk.ui.block.grayout();
                self.changeApplicationDate(startDate, endDate).done(() => {
                    nts.uk.ui.block.clear();
                });
            });

            self.dateSingle.subscribe(value => {
                nts.uk.ui.errors.clearAll();
                let startDate, endDate;
                $("#singleDate").trigger("validate");
                if (nts.uk.ui.errors.hasError() || !value) {
                    return;
                }
                startDate = endDate = value;
                nts.uk.ui.block.grayout();
                self.changeApplicationDate(startDate, endDate).done(() => {
                    nts.uk.ui.block.clear();
                });
            });

           

            self.employeeList.subscribe((datas) => {
                if (datas.length) {
                    self.totalEmployeeText(text('KAF011_79', [datas.length]));
                    self.selectedEmployee(datas[0]);
                }
            });
        }
        
        showReasonText(){
            let self =this;
                if (self.screenModeNew()) {
                return self.displayAppReasonContentFlg();
            } else {
                return self.displayAppReasonContentFlg() != 0 || self.typicalReasonDisplayFlg() != 0;
            }    
            }
        showRightContent(){
        let self =this;
         return   self.appChangeSetting().displayResultAtr() && self.appWorkChange().application().prePostAtr() == 1   ; 
        }
        /**
         * 起動する
         */
        startPage(): JQueryPromise<any> {

            let self = this,
                dfd = $.Deferred(),
                employeeIDs = isNullOrEmpty(self.employeeID)? []:[self.employeeID];

            //get Common Setting
            nts.uk.ui.block.invisible();
        //get Common Setting
            service.getWorkChangeCommonSetting({
                sIDs: employeeIDs,
                appDate: self.targetDate
            }).done(function(settingData: any) {

                self.setData(settingData);

                //Focus process
                self.selectedReason.subscribe(value => { $("#inpReasonTextarea").focus(); });
                //フォーカス制御
                self.changeFocus('#singleDate');
                //phải để subscribe ở đây khi change multiDate focus mới ăn
                self.registerEvent();
               
                dfd.resolve();
            }).fail((res) => {
                if (res.messageId == 'Msg_426') {
                    dialog.alertError({ messageId: res.messageId });
                } else {
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId }).then(function() {
                        nts.uk.request.jump("com", "view/ccg/008/a/index.xhtml");
                    });
                }
                dfd.reject();
            }).always(() => {
                nts.uk.ui.block.clear();
            });
            return dfd.promise();
        }
        
        registerEvent() {
            let self = this;

            self.multiDate.subscribe(value => {
                nts.uk.ui.errors.clearAll();
                if (value) {
                    self.datePeriod({ startDate: self.dateSingle(), endDate: "" });
                    $(".ntsStartDatePicker").focus();
                } else {
                    self.dateSingle(self.datePeriod().startDate);
                    $("#singleDate").focus();
                }

            });
        }

        setData(settingData) {
            let self = this;
            if (!nts.uk.util.isNullOrEmpty(settingData)) {
                self.employeeList(_.map(settingData.employees, (emp) => { return { sid: emp.sid, code: emp.scd, name: emp.bussinessName } }));
                self.checkBoxValue(settingData.appCommonSettingDto.applicationSettingDto.manualSendMailAtr == 1 ? true : false);
                //申請共通設定
                let appCommonSettingDto = settingData.appCommonSettingDto;

                //勤務変更申請設定
                let appWorkChangeCommonSetting = settingData.workChangeSetDto;

                self.appChangeSetting(new common.AppWorkChangeSetting(appWorkChangeCommonSetting));
                //A2_申請者 ID
                self.employeeID = settingData.sid;
                //A2_1 申請者
                self.employeeName(settingData.employeeName);

                //A3 事前事後区分
                //事前事後区分 ※A１
                self.prePostDisp(appCommonSettingDto.applicationSettingDto.displayPrePostFlg == 1 ? true : false);
                self.showExcludeHoliday(appWorkChangeCommonSetting.excludeHoliday);
                if (!nts.uk.util.isNullOrEmpty(appCommonSettingDto.appTypeDiscreteSettingDtos) &&
                    appCommonSettingDto.appTypeDiscreteSettingDtos.length > 0) {
                    //事前事後区分 Enable ※A２
                    self.prePostEnable(appCommonSettingDto.appTypeDiscreteSettingDtos[0].prePostCanChangeFlg == 1 ? true : false);
                    self.appWorkChange().application().prePostAtr(settingData.appCommonSettingDto.appTypeDiscreteSettingDtos[0].prePostInitFlg);
                    //「申請種類別設定．定型理由の表示」  ※A10
                    self.typicalReasonDisplayFlg(appCommonSettingDto.appTypeDiscreteSettingDtos[0].typicalReasonDisplayFlg == 1 ? true : false);
                    //「申請種類別設定．申請理由の表示」  ※A11
                    self.displayAppReasonContentFlg(appCommonSettingDto.appTypeDiscreteSettingDtos[0].displayReasonFlg == 1 ? true : false);
                    //登録時にメールを送信する Visible
                    self.enableSendMail(appCommonSettingDto.appTypeDiscreteSettingDtos[0].sendMailWhenRegisterFlg == 1 ? false : true);
                    //self.enableSendMail(appCommonSettingDto.appTypeDiscreteSettingDtos[0].sendMailWhenRegisterFlg == 1 ? false: true);
                }
                // self.manualSendMailAtr(appCommonSettingDto.applicationSettingDto.manualSendMailAtr == 1 ? true: false);
                //A5 勤務を変更する ※A4                    
                if (appWorkChangeCommonSetting != undefined) {
                    //勤務変更申請設定.勤務時間を変更できる　＝　出来る
                    self.isWorkChange(appWorkChangeCommonSetting.workChangeTimeAtr == 1 ? true : false);
                }
                //定型理由
                self.setReasonControl(settingData.listReasonDto);
                //申請制限設定.申請理由が必須
                self.requiredReason(settingData.appCommonSettingDto.applicationSettingDto.requireAppReasonFlg == 1 ? true : false);
                //A8 勤務時間２ ※A7
                //共通設定.複数回勤務
                 self.isMultipleTime(settingData.multipleTime);
            }
            //Setting default value data work:
            self.appWorkChange().dataWork(settingData.dataWorkDto);
            self.appWorkChange().workChange().workTypeCd(settingData.dataWorkDto.selectedWorkTypeCd === null ? '' : settingData.dataWorkDto.selectedWorkTypeCd);
            self.appWorkChange().workChange().workTypeName(settingData.dataWorkDto.selectedWorkTypeName === null ? '' : settingData.dataWorkDto.selectedWorkTypeName);
            self.appWorkChange().workChange().workTimeCd(settingData.dataWorkDto.selectedWorkTimeCd === null ? '' : settingData.dataWorkDto.selectedWorkTimeCd);
            self.appWorkChange().workChange().workTimeName(settingData.dataWorkDto.selectedWorkTimeName === null ? '' : settingData.dataWorkDto.selectedWorkTimeName);
            if(!nts.uk.util.isNullOrUndefined(settingData.dataWorkDto.startTime1)){
                self.appWorkChange().workChange().workTimeStart1(settingData.dataWorkDto.startTime1);    
            }
            if(!nts.uk.util.isNullOrUndefined(settingData.dataWorkDto.endTime1)){
                self.appWorkChange().workChange().workTimeEnd1(settingData.dataWorkDto.endTime1);    
            }
            self.requiredCheckTime(self.isWorkChange() && settingData.timeRequired);
            self.timeRequired(settingData.timeRequired);
        }

        /**
         * 「登録」ボタンをクリックする
         * 勤務変更申請の登録を実行する
         */
        registerClick() {
            let self = this;
            nts.uk.ui.block.invisible();
            let appReason: string;
            if (!self.validateInputTime()) { return; }
            appReason = self.getReason(
                self.typicalReasonDisplayFlg(),
                self.selectedReason(),
                self.reasonCombo(),
                self.displayAppReasonContentFlg(),
                self.multilContent().trim()
            );
            if (!appcommon.CommonProcess.checklenghtReason(appReason, "#inpReasonTextarea")) {
                return;
            }
            let appReasonError = !appcommon.CommonProcess.checkAppReason(self.requiredReason(), self.typicalReasonDisplayFlg(), self.displayAppReasonContentFlg(), appReason);
            if (appReasonError) {
                nts.uk.ui.dialog.alertError({ messageId: 'Msg_115' }).then(function() { nts.uk.ui.block.clear(); });
                return;
            }
            //申請日付
            self.appWorkChange().application().applicationDate(self.getStartDate());

            self.appWorkChange().application().startDate(self.getStartDate());
            self.appWorkChange().application().endDate(self.getEndDate());
            //申請理由
            self.appWorkChange().application().applicationReason(appReason);
            //勤務を変更する
            self.appWorkChange().workChange().workChangeAtr(self.workChangeAtr() == true ? 1 : 0);
            // 休日に関して
            self.appWorkChange().workChange().excludeHolidayAtr(self.excludeHolidayAtr() == true ? 1 : 0);
            self.appWorkChange().employeeID(self.employeeList()[0] ? self.employeeList()[0].sid : null);
            //Change null to unregister value:
            self.changeUnregisterValue();

            let workChange = ko.toJS(self.appWorkChange());

            service.addWorkChange(workChange).done((data) => {
                //Success
                nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                    if (data.autoSendMail) {
                        appcommon.CommonProcess.displayMailResult(data);
                    } else {
                        if (self.checkBoxValue()) {
                            appcommon.CommonProcess.openDialogKDL030(data.appID);
                        } else {
                            location.reload();
                        }
                    }
                });
            }).fail((res) => {
                dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds });
                nts.uk.ui.block.clear();
            });
        }

        getStartDate() {
            let self = this,
                dateValue = self.multiDate() ? self.datePeriod().startDate : self.dateSingle();

            return moment.utc(dateValue, self.dateFormat).toISOString();
        }

        getEndDate() {
            let self = this,
                dateValue = self.multiDate() ? self.datePeriod().endDate : self.dateSingle();
            return moment.utc(dateValue, self.dateFormat).toISOString();
        }
        enableTime() {
            let self = this;
            let result = self.editable() && self.requiredCheckTime();
            if (!result) {
                self.appWorkChange().workChange().workTimeStart1(null);
                self.appWorkChange().workChange().workTimeEnd1(null);
            }
            return result;
        }




        /**
         * Validate input time
         */
        private validateInputTime(): boolean {
            let self = this,
                workchange = self.appWorkChange().workChange();
            nts.uk.ui.errors.clearAll();
            if (self.multiDate()) {
                $('#daterangepicker').find(".nts-input").trigger('validate');
            } else {
                $("#singleDate").trigger("validate");
            }
            $("#inpStartTime1").trigger("validate");
            $("#inpEndTime1").trigger("validate");
            $("#pre-post").trigger("validate");
            

            //return if has error
            if (nts.uk.ui.errors.hasError()) {
                nts.uk.ui.block.clear();
                return false;
            }
            //申請日付（開始日：終了日）大小チェック
            if (workchange.workTimeStart1() > workchange.workTimeEnd1()) {
                dialog.alertError({ messageId: "Msg_579" }).then(function() { nts.uk.ui.block.clear(); });
                $('#inpStartTime1').focus();
                return false;
            }

            //１．就業時間１（開始時刻：終了時刻） 大小チェック
            if (workchange.workTimeStart1() > workchange.workTimeEnd1()) {
                dialog.alertError({ messageId: "Msg_579" }).then(function() { nts.uk.ui.block.clear(); });
                $('#inpStartTime1').focus();
                return false;
            }
            //２．就業時間２（開始時刻：終了時刻）
            //共通設定.複数回勤務　＝　利用する
            if (self.isMultipleTime()) {
                //has input time 2
                if (!nts.uk.util.isNullOrEmpty(workchange.workTimeStart2())) {
                    //開始時刻　＞　終了時刻
                    if (workchange.workTimeStart2() > workchange.workTimeEnd2()) {
                        dialog.alertError({ messageId: "Msg_580" }).then(function() { nts.uk.ui.block.clear(); });
                        $('#inpStartTime2').focus();
                        return false;
                    }
                    //就業時間（終了時刻）　>　就業時刻２（開始時刻）
                    if (workchange.workTimeEnd1() > workchange.workTimeStart2()) {
                        dialog.alertError({ messageId: "Msg_581" }).then(function() { nts.uk.ui.block.clear(); });
                        $('#workTimeEnd1').focus();
                        return false;
                    }
                }
            }
            //３．休憩時間１（開始時刻：終了時刻）大小チェック
            if (!nts.uk.util.isNullOrEmpty(workchange.breakTimeStart1())) {
                //開始時刻　＞　終了時刻
                if (workchange.breakTimeStart1() > workchange.breakTimeEnd1()) {
                    dialog.alertError({ messageId: "Msg_582" }).then(function() { nts.uk.ui.block.clear(); });
                    $('#breakTimeStart1').focus();
                    return false;
                }
            }
            return true;
        }
        /**
         * 共通アルゴリズム「申請日を変更する」を実行する
         * @param date: 申請日
         * @param dateType (Start or End type)
         */
        private changeApplicationDate(startDate: any, endDate: any): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred(),
                tmpStartDate: string = moment(startDate).format(self.dateFormat),
                tmpEndDate: string = moment(endDate).format(self.dateFormat),
                application = self.appWorkChange().application();
            //申請日付開始日を基準に共通アルゴリズム「申請日を変更する」を実行する
            //self.checkChangeAppDate(date);
            //申請日付分　（開始日～終了日）
            //基準日　≦　終了日
            while (moment(tmpStartDate, self.dateFormat).isSameOrBefore(moment(tmpEndDate, self.dateFormat))) {
                self.checkChangeAppDate(tmpStartDate);
                //基準日　＝　基準日　＋　１
                tmpStartDate = moment(tmpStartDate).add(1, 'day').format(self.dateFormat);
            }
            //実績の内容
            service.getRecordWorkInfoByDate({appDate : moment(!endDate ? startDate : endDate).format(self.dateFormat), employeeID : null}).done((recordWorkInfo) => {
                //Binding data
                ko.mapping.fromJS(recordWorkInfo, {}, self.recordWorkInfo);
                if(self.appChangeSetting().initDisplayWorktime()===0 && self.enableTime()){
                    self.appWorkChange().workChange().workTimeStart1(recordWorkInfo.startTime1);
                    self.appWorkChange().workChange().workTimeEnd1(recordWorkInfo.endTime1);
                    //self.appWorkChange().workChange().workTimeStart2(recordWorkInfo.startTime2);
                    //self.appWorkChange().workChange().workTimeEnd2(recordWorkInfo.endTime2);
                }
                dfd.resolve();
            }).fail((res) => {
                dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds });
                dfd.reject();
            });
            return dfd.promise();
        }
        /**
         * 申請日を変更する
         * @param date in yyyy/MM/DD format
         */
        private checkChangeAppDate(date: string) {
            let self = this;
            date = moment(date).format(self.dateFormat);
            self.kaf000_a.getAppDataDate(2, date, false, self.employeeID, null);
        }
        /**
         * フォーカス制御
         * @param element(申請日付/勤務時間直行)
         */
        private changeFocus(element: string) {
            $(element).focus();
        }

        /**
         * Get application reason contains "CodeName + input reason"
         */
        private getReason(inputReasonDisp: boolean, inputReasonID: string, inputReasonList: Array<common.ComboReason>, detailReasonDisp: boolean, detailReason: string): string {
            let appReason = '';
            let inputReason: string = '';
            if (!nts.uk.util.isNullOrEmpty(inputReasonID)) {
                inputReason = _.find(inputReasonList, o => { return o.reasonId == inputReasonID; }).reasonName;
            }
            if (inputReasonDisp == true && detailReasonDisp == true) {
                if (!nts.uk.util.isNullOrEmpty(inputReason) && !nts.uk.util.isNullOrEmpty(detailReason)) {
                    appReason = inputReason + ":" + detailReason;
                } else if (!nts.uk.util.isNullOrEmpty(inputReason) && nts.uk.util.isNullOrEmpty(detailReason)) {
                    appReason = inputReason;
                } else if (nts.uk.util.isNullOrEmpty(inputReason) && !nts.uk.util.isNullOrEmpty(detailReason)) {
                    appReason = detailReason;
                }
            } else if (inputReasonDisp == true && detailReasonDisp == false) {
                appReason = inputReason;
            } else if (inputReasonDisp == false && detailReasonDisp == true) {
                appReason = detailReason;
            }
            return appReason;
        }
        /**
         * setting reason data to combobox
         */
        setReasonControl(data: Array<common.ReasonDto>) {
            var self = this;
            let comboSource: Array<common.ComboReason> = [];
            _.forEach(data, function(value: common.ReasonDto) {
                self.reasonCombo.push(new common.ComboReason(value.displayOrder, value.reasonTemp, value.reasonID));
                if (value.defaultFlg === 1) {
                    self.selectedReason(value.reasonID);
                }
            });
        }
        public convertIntToTime(data: any): string {
            if(nts.uk.util.isNullOrUndefined(data)||nts.uk.util.isNullOrEmpty(data)){
                return null;   
            }
            return nts.uk.time.format.byId("ClockDay_Short_HM", data);
        }
        private changeUnregisterValue() {
            let self = this,
                workchange = self.appWorkChange().workChange();
            //
            if (!self.isMultipleTime()
                || nts.uk.util.isNullOrEmpty(workchange.workTimeStart2())) {
                workchange.goWorkAtr2(null);
                workchange.backHomeAtr2(null);
                workchange.workTimeStart2(null);
                workchange.workTimeEnd2(null);
            }
        }
        /**
         * 「勤務就業選択」ボタンをクリックする
         * KDL003_勤務就業ダイアログを起動する
         */
        openKDL003Click() {
            let self = nts.uk.ui._viewModel.content,
                dataWork = self.appWorkChange().dataWork(),
                workChange = self.appWorkChange().workChange();
            $("#inpStartTime1").ntsError('clear');
            $("#inpEndTime1").ntsError('clear');
            nts.uk.ui.windows.setShared('parentCodes', {
                workTypeCodes: dataWork.workTypeCodes,
                selectedWorkTypeCode: dataWork.selectedWorkTypeCd,
                workTimeCodes: dataWork.workTimeCodes,
                selectedWorkTimeCode: dataWork.selectedWorkTimeCd,
            }, true);

            nts.uk.ui.windows.sub.modal('/view/kdl/003/a/index.xhtml').onClosed(function(): any {
                //view all code of selected item 
                var childData = nts.uk.ui.windows.getShared('childData');
                if (childData) {
                    dataWork.selectedWorkTypeCd = childData.selectedWorkTypeCode;
                    dataWork.selectedWorkTimeCd = childData.selectedWorkTimeCode;
                    workChange.workTypeCd(childData.selectedWorkTypeCode);
                    workChange.workTypeName(childData.selectedWorkTypeName);
                    workChange.workTimeCd(childData.selectedWorkTimeCode);
                    workChange.workTimeName(childData.selectedWorkTimeName);
                    service.isTimeRequired( workChange.workTypeCd()).done((rs) =>{
                        self.requiredCheckTime(self.isWorkChange() && rs);    
                        if(self.requiredCheckTime()){
                            workChange.workTimeStart1(childData.first.start);
                            workChange.workTimeEnd1(childData.first.end); 
                        }
                    });
                }
                //フォーカス制御
                //self.changeFocus('#inpStartTime1');
                $('#inpStartTime1').focus();
            });
        }

        /**
         * 「実績参照」ボタンをクリックする
         * 「CMM018_承認者の登録（就業）」画面に遷移する
         */
        openCMM018Click() {
            let self = this;
            nts.uk.request.jump("com", "/view/cmm/018/a/index.xhtml", { screen: 'Application', employeeId: self.employeeID });
        }
        /**
         * 「承認者変更」ボタンをクリックする
         * 「KDL004 実績参照」ダイアログを起動する
         */
        openKDL004Click() {
            let self = this;
            return;
        }
    }

}

