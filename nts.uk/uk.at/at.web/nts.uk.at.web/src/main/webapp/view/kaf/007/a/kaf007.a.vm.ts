module nts.uk.at.view.kaf007.a.viewmodel {
    import common = nts.uk.at.view.kaf007.share.common;
    import service = nts.uk.at.view.kaf007.share.service;
    import dialog = nts.uk.ui.dialog;
    import appcommon = nts.uk.at.view.kaf000.shr.model;
    export class ScreenModel {
        screenModeNew: KnockoutObservable<boolean> = ko.observable(true);
        appWorkChange: KnockoutObservable<common.AppWorkChangeCommand> = ko.observable(new common.AppWorkChangeCommand());
        recordWorkInfo: KnockoutObservable<common.RecordWorkInfo> = ko.observable(new common.RecordWorkInfo());
        //A3 事前事後区分:表示/活性
        prePostDisp: KnockoutObservable<boolean> = ko.observable(false);
        prePostEnable: KnockoutObservable<boolean> = ko.observable(false);
        requiredPrePost: KnockoutObservable<boolean> = ko.observable(false);
        //A5 勤務を変更する:表示/活性
        isWorkChange:   KnockoutObservable<boolean> = ko.observable(true);
        workChangeAtr: KnockoutObservable<boolean> = ko.observable(false);
        //A8 勤務時間２
        isMultipleTime: KnockoutObservable<boolean> = ko.observable(false);
        //kaf000
        kaf000_a: kaf000.a.viewmodel.ScreenModel;
        employeeID : string ="";
        //申請者
        employeeName: KnockoutObservable<string> = ko.observable("");
        //comboBox 定型理由
        typicalReasonDisplayFlg: KnockoutObservable<boolean> = ko.observable(false);
        displayAppReasonContentFlg: KnockoutObservable<boolean> = ko.observable(false);
        reasonCombo: KnockoutObservableArray<common.ComboReason> = ko.observableArray([]);
        selectedReason: KnockoutObservable<string> = ko.observable('');
        //MultilineEditor
        requiredReason : KnockoutObservable<boolean> = ko.observable(false);
        multilContent: KnockoutObservable<string> = ko.observable('');
        //A10_1 休日は除く
        excludeHolidayAtr: KnockoutObservable<boolean> = ko.observable(false);
        //Approval 
        approvalSource: Array<common.AppApprovalPhase> = [];        
        //menu-bar 
        enableSendMail :KnockoutObservable<boolean> = ko.observable(false); 
        manualSendMailAtr: KnockoutObservable<boolean> = ko.observable(false);    
        dateFormat: string = 'YYYY/MM/DD';
        //画面モード(表示/編集)
        editable: KnockoutObservable<boolean> = ko.observable( true );
        constructor() {
            let self = this,
            application = self.appWorkChange().application();
            //KAF000_A
            self.kaf000_a = new kaf000.a.viewmodel.ScreenModel();            
            self.startPage().done(function(){
                self.kaf000_a.start(self.employeeID, 1, 2, moment(new Date()).format(self.dateFormat)).done(function(){                    
                    nts.uk.ui.block.clear();
                })    
            }).fail((res) => {
                nts.uk.ui.dialog.alertError({messageId: res.messageId}).then(function(){ 
                    nts.uk.request.jump("com", "view/ccg/008/a/index.xhtml");  
                });
                nts.uk.ui.block.clear();
            });
            
            // 申請日を変更する          
            //Start Date
            application.startDate.subscribe(value => {
                $("#inputdatestart").trigger("validate");
                if (nts.uk.ui.errors.hasError()) {
                    return;
                }
                self.changeApplicationDate(value, common.AppDateType.StartDate);
            });
            //End Date
            application.endDate.subscribe(value => {
                $("#inputdateend").trigger("validate");
                if (nts.uk.ui.errors.hasError()) {
                    return;
                }
                self.changeApplicationDate(value, common.AppDateType.EndDate);
            });
        }
        /**
         * 起動する
         */
        startPage(): JQueryPromise<any> {
            nts.uk.ui.block.invisible();
            let self = this,
            dfd = $.Deferred();
            
            //get Common Setting
            service.getWorkChangeCommonSetting().done(function(settingData: any) {
                if(!nts.uk.util.isNullOrEmpty(settingData)){                    
                    //申請共通設定
                    let appCommonSettingDto = settingData.appCommonSettingDto;
                    //勤務変更申請設定
                    let appWorkChangeCommonSetting = settingData.workChangeSetDto;                
                    
                    //A2_申請者 ID
                    self.employeeID = settingData.sid;
                    //A2_1 申請者
                    self.employeeName(settingData.employeeName);
                    
                    //A3 事前事後区分
                    //事前事後区分 ※A１
                    self.prePostDisp(appCommonSettingDto.applicationSettingDto.displayPrePostFlg == 1 ? true: false);
                    if( !nts.uk.util.isNullOrEmpty(appCommonSettingDto.appTypeDiscreteSettingDtos) && 
                            appCommonSettingDto.appTypeDiscreteSettingDtos.length > 0){                         
                        //事前事後区分 Enable ※A２
                        self.prePostEnable(appCommonSettingDto.appTypeDiscreteSettingDtos[0].prePostCanChangeFlg == 1 ? true: false);
                        self.appWorkChange().application().prePostAtr(appCommonSettingDto.appTypeDiscreteSettingDtos[0].prePostCanChangeFlg);
                        //「申請種類別設定．定型理由の表示」  ※A10
                        self.typicalReasonDisplayFlg(appCommonSettingDto.appTypeDiscreteSettingDtos[0].typicalReasonDisplayFlg == 1 ? true : false );
                        //「申請種類別設定．申請理由の表示」  ※A11
                        self.displayAppReasonContentFlg(appCommonSettingDto.appTypeDiscreteSettingDtos[0].displayReasonFlg == 1 ? true : false );
                        //登録時にメールを送信する Visible
                        self.enableSendMail(appCommonSettingDto.appTypeDiscreteSettingDtos[0].sendMailWhenRegisterFlg == 1 ? true: false);
                        //self.enableSendMail(appCommonSettingDto.appTypeDiscreteSettingDtos[0].sendMailWhenRegisterFlg == 1 ? false: true);
                    }
                    self.manualSendMailAtr(appCommonSettingDto.applicationSettingDto.manualSendMailAtr == 1 ? true: false);
                    //A5 勤務を変更する ※A4                    
                    if(appWorkChangeCommonSetting　!= undefined){ 
                        //勤務変更申請設定.勤務時間を変更できる　＝　出来る
                        self.isWorkChange(appWorkChangeCommonSetting.workChangeTimeAtr == 1? true : false);                                     
                    }
                    //定型理由
                    self.setReasonControl(settingData.listReasonDto);
                    //申請制限設定.申請理由が必須
                    self.requiredReason(settingData.appCommonSettingDto.applicationSettingDto.requireAppReasonFlg == 1 ? true: false);
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
                //Focus process
                self.selectedReason.subscribe(value => {  $("#inpReasonTextarea").focus(); });
                //フォーカス制御
                self.changeFocus('#inputdatestart'); 
                              
                dfd.resolve();
            }).fail((res) => {
                if(res.messageId == 'Msg_426'){
                       dialog.alertError({messageId : res.messageId}).then(function(){
                           
                        });
                }else{
                    nts.uk.ui.dialog.alertError({messageId: res.messageId}).then(function(){ 
                        nts.uk.request.jump("com", "view/ccg/008/a/index.xhtml");  
                    });
                }
                dfd.reject();
                nts.uk.ui.block.clear();
            });
            return dfd.promise();
        }       
        
        /**
         * 「登録」ボタンをクリックする
         * 勤務変更申請の登録を実行する
         */
        registerClick(){
            let self =this;
            nts.uk.ui.block.invisible();
            let appReason: string;
            if(!self.validateInputTime()){return;}
            appReason = self.getReason(
                self.typicalReasonDisplayFlg(),
                self.selectedReason(),
                self.reasonCombo(),
                self.displayAppReasonContentFlg(),
                self.multilContent()
            );
            if(!appcommon.CommonProcess.checklenghtReason(appReason,"#inpReasonTextarea")){
                        return;
            }
            let appReasonError = !appcommon.CommonProcess.checkAppReason(true, self.typicalReasonDisplayFlg(), self.displayAppReasonContentFlg(), appReason);
            if(appReasonError){
                nts.uk.ui.dialog.alertError({ messageId: 'Msg_115' }).then(function(){nts.uk.ui.block.clear();});    
                return;    
            }
            //申請日付
            self.appWorkChange().application().applicationDate(self.appWorkChange().application().startDate());
            //申請理由
            self.appWorkChange().application().applicationReason(appReason);
            //勤務を変更する
            self.appWorkChange().workChange().workChangeAtr(self.workChangeAtr() == true ? 1 : 0);
            // 休日に関して
            self.appWorkChange().workChange().excludeHolidayAtr(self.excludeHolidayAtr() == true ? 1 : 0);
            
            //Change null to unregister value:
            self.changeUnregisterValue();
            
            let workChange = ko.toJS(self.appWorkChange());
            service.addWorkChange(workChange).done(() => {
                //Success
                dialog.info({ messageId: "Msg_15" }).then(function() {
                    location.reload();
                });
            }).fail((res) => {
                dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds });
                nts.uk.ui.block.clear();
            });
        }
        
        /**
         * Validate input time
         */
        private validateInputTime(): boolean{
            let self = this,
            workchange = self.appWorkChange().workChange();
            
            $("#inpStartTime1").trigger("validate");
            $("#inpEndTime1").trigger("validate");
            
            //return if has error
            if (nts.uk.ui.errors.hasError()){
                nts.uk.ui.block.clear();
                return false;} 
            
            //１．就業時間１（開始時刻：終了時刻） 大小チェック
            if(workchange.workTimeStart1() > workchange.workTimeEnd1()){
                dialog.alertError({messageId:"Msg_579"}).then(function(){nts.uk.ui.block.clear();});
                 $('#inpStartTime1').focus();
                return false;
            }
            //２．就業時間２（開始時刻：終了時刻）
            //共通設定.複数回勤務　＝　利用する
            if(self.isMultipleTime()){
                //has input time 2
                if ( !nts.uk.util.isNullOrEmpty(workchange.workTimeStart2())) {
                    //開始時刻　＞　終了時刻
                    if(workchange.workTimeStart2() > workchange.workTimeEnd2()){
                        dialog.alertError({messageId:"Msg_580"}).then(function(){nts.uk.ui.block.clear();});
                         $('#inpStartTime2').focus();
                        return false;
                    }
                    //就業時間（終了時刻）　>　就業時刻２（開始時刻）
                    if(workchange.workTimeEnd1() > workchange.workTimeStart2()){
                        dialog.alertError({messageId:"Msg_581"}).then(function(){nts.uk.ui.block.clear();});
                         $('#workTimeEnd1').focus();
                        return false;
                    }
                }
            }
            //３．休憩時間１（開始時刻：終了時刻）大小チェック
            if ( !nts.uk.util.isNullOrEmpty(workchange.breakTimeStart1())) {
                //開始時刻　＞　終了時刻
                if(workchange.breakTimeStart1() > workchange.breakTimeEnd1()){
                    dialog.alertError({messageId:"Msg_582"}).then(function(){nts.uk.ui.block.clear();});
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
        private changeApplicationDate(date:any, dateType: common.AppDateType){
            let self = this,
            startDate: string,
            endDate: string,
            application = self.appWorkChange().application();
            switch(dateType){
                case common.AppDateType.StartDate:
                    startDate = date;
                    endDate = application.endDate();
                    break;
                case common.AppDateType.EndDate:
                    startDate = application.startDate();
                    endDate = date;
                    break;
            }
            //申請日付開始日を基準に共通アルゴリズム「申請日を変更する」を実行する
            //self.checkChangeAppDate(date);
            //申請日付分　（開始日～終了日）
            //基準日　≦　終了日
            while(moment(startDate, self.dateFormat).isSameOrBefore(moment(endDate, self.dateFormat))){
                self.checkChangeAppDate(startDate);
                //基準日　＝　基準日　＋　１
                startDate = moment(startDate).add(1, 'day');
            }
            //実績の内容
            service.getRecordWorkInfoByDate(moment(date).format(self.dateFormat)).done((recordWorkInfo) => {
                //Binding data
                ko.mapping.fromJS( recordWorkInfo, {}, self.recordWorkInfo );
            }).fail((res) => {
                dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function(){nts.uk.ui.block.clear();});
            });
        }
        /**
         * 申請日を変更する
         * @param date in yyyy/MM/DD format
         */
        private checkChangeAppDate(date: string){
            let self = this;
            date = moment(date).format(self.dateFormat);
            nts.uk.ui.block.invisible();
            self.kaf000_a.getAppDataDate(2, date, false)
            .done(()=>{
                nts.uk.ui.block.clear();         
            }).fail(()=>{
                nts.uk.ui.block.clear();    
            });
        }
        /**
         * フォーカス制御
         * @param element(申請日付/勤務時間直行)
         */
        private changeFocus(element:string){
            $(element).focus();
        }
        
        /**
         * Get application reason contains "CodeName + input reason"
         */
        private getReason( inputReasonDisp: boolean, inputReasonID: string, inputReasonList: Array<common.ComboReason>, detailReasonDisp: boolean, detailReason: string ): string {
            let appReason = '';
            let inputReason: string = '';
            if ( !nts.uk.util.isNullOrEmpty( inputReasonID ) ) {
                inputReason = _.find( inputReasonList, o => { return o.reasonId == inputReasonID; } ).reasonName;
            }
            if ( inputReasonDisp == true && detailReasonDisp == true ) {
                if ( !nts.uk.util.isNullOrEmpty( inputReason ) && !nts.uk.util.isNullOrEmpty( detailReason ) ) {
                    appReason = inputReason + ":" + detailReason;
                } else if ( !nts.uk.util.isNullOrEmpty( inputReason ) && nts.uk.util.isNullOrEmpty( detailReason ) ) {
                    appReason = inputReason;
                } else if ( nts.uk.util.isNullOrEmpty( inputReason ) && !nts.uk.util.isNullOrEmpty( detailReason ) ) {
                    appReason = detailReason;
                }
            } else if ( inputReasonDisp == true && detailReasonDisp == false ) {
                appReason = inputReason;
            } else if ( inputReasonDisp == false && detailReasonDisp == true ) {
                appReason = detailReason;
            }
            return appReason;
        }
        /**
         * setting reason data to combobox
         */
        setReasonControl( data: Array<common.ReasonDto> ) {
            var self = this;
            let comboSource: Array<common.ComboReason> = [];
            _.forEach( data, function( value: common.ReasonDto ) {
                self.reasonCombo.push( new common.ComboReason( value.displayOrder, value.reasonTemp, value.reasonID ) );
                if ( value.defaultFlg === 1 ) {
                    self.selectedReason( value.reasonID );
                }
            } );
        }
        public convertIntToTime( data: any ): string {
            let hourMinute: string = "";
            if ( data == -1 || data === "" ) {
                return null;
            } else if ( data == 0 ) {
                hourMinute = "00:00";
            } else if ( data != null ) {
                let hour = Math.floor( data / 60 );
                let minutes = Math.floor( data % 60 );
                hourMinute = ( hour < 10 ? ( "0" + hour ) : hour ) + ":" + ( minutes < 10 ? ( "0" + minutes ) : minutes );
            }
            return hourMinute;
        } 
        private changeUnregisterValue(){
            let self = this,
            workchange = self.appWorkChange().workChange();
            //
            if ( !self.isMultipleTime() 
                    ||  nts.uk.util.isNullOrEmpty( workchange.workTimeStart2() )) {
                workchange.goWorkAtr2( null );
                workchange.backHomeAtr2( null );
                workchange.workTimeStart2( null );
                workchange.workTimeEnd2( null );
            }
        }
        /**
         * 「勤務就業選択」ボタンをクリックする
         * KDL003_勤務就業ダイアログを起動する
         */
        openKDL003Click() {
            let self = this,
                dataWork = self.dataWork(),
                workChange = self.workChange();
            //dataWork = self.appWorkChange().dataWork(),
            //    workChange = self.appWorkChange().workChange();
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

                    workChange.workTypeCd(childData.selectedWorkTypeCode);
                    workChange.workTypeName(childData.selectedWorkTypeName);
                    workChange.workTimeCd(childData.selectedWorkTimeCode);
                    workChange.workTimeName(childData.selectedWorkTimeName);
                }
                //フォーカス制御
                //self.changeFocus('#inpStartTime1');
                $('#inpStartTime1').focus();
            })
        }
        
        /**
         * 「実績参照」ボタンをクリックする
         * 「CMM018_承認者の登録（就業）」画面に遷移する
         */
        openCMM018Click(){
            let self = this;
            nts.uk.request.jump("com", "/view/cmm/018/a/index.xhtml", {screen: 'Application', employeeId: self.employeeID});  
        }
        /**
         * 「承認者変更」ボタンをクリックする
         * 「KDL004 実績参照」ダイアログを起動する
         */
        openKDL004Click(){
            let self = this;
            return;
        }
    }
    
}

