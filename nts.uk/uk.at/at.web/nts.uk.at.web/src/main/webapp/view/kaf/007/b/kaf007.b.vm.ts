module nts.uk.at.view.kaf007.b {
    import common = nts.uk.at.view.kaf007.share.common;
    import service = nts.uk.at.view.kaf007.share.service;
    import dialog = nts.uk.ui.dialog;
    import model = nts.uk.at.view.kaf000.b.viewmodel.model;
    import appcommon = nts.uk.at.view.kaf000.shr.model;
    import text = nts.uk.resource.getText;
    export module viewmodel {
        export class ScreenModel extends kaf000.b.viewmodel.ScreenModel {
            screenModeNew: KnockoutObservable<boolean> = ko.observable(false);
            appWorkChange: KnockoutObservable<common.AppWorkChangeCommand> = ko.observable( new common.AppWorkChangeCommand() );
            recordWorkInfo: KnockoutObservable<common.RecordWorkInfo> = ko.observable(new common.RecordWorkInfo());
            //A3 事前事後区分:表示/活性
            prePostDisp: KnockoutObservable<boolean> = ko.observable( false );
            prePostEnable: KnockoutObservable<boolean> = ko.observable( false );
            requiredPrePost: KnockoutObservable<boolean> = ko.observable(false);
            //A5 勤務を変更する:表示/活性
            isWorkChange: KnockoutObservable<boolean> = ko.observable( true );
            workChangeAtr: KnockoutObservable<boolean> = ko.observable( false );          
            //A8 勤務時間２
            isMultipleTime: KnockoutObservable<boolean> = ko.observable( false );
            //kaf000
            kaf000_a: kaf000.a.viewmodel.ScreenModel;
            employeeID: string = "";
            //申請者
            employeeName: KnockoutObservable<string> = ko.observable( "" );
            //comboBox 定型理由
            typicalReasonDisplayFlg: KnockoutObservable<boolean> = ko.observable(false);
            displayAppReasonContentFlg: KnockoutObservable<boolean> = ko.observable(false);
            reasonCombo: KnockoutObservableArray<common.ComboReason> = ko.observableArray( [] );
            selectedReason: KnockoutObservable<string> = ko.observable( '' );
            //MultilineEditor
            requiredReason: KnockoutObservable<boolean> = ko.observable( false );
            multilContent: KnockoutObservable<string> = ko.observable( '' );
            //A10_1 休日は除く
            excludeHolidayAtr: KnockoutObservable<boolean> = ko.observable( false );
            //Approval 
            approvalSource: Array<common.AppApprovalPhase> = [];            
            //menu-bar 
            enableSendMail: KnockoutObservable<boolean> = ko.observable( false );
            dateFormat: string = 'YYYY/MM/DD';
            dateTimeFormat: string = 'YYYY/MM/DD hh:mm:ss';
            //勤務就業ダイアログ用データ取得
            workTypeCodes:KnockoutObservableArray<string> = ko.observableArray( [] );
            workTimeCodes:KnockoutObservableArray<string> = ko.observableArray( [] );
            //画面モード(表示/編集)
            editable: KnockoutObservable<boolean> = ko.observable( true );
            appChangeSetting: KnockoutObservable<common.AppWorkChangeSetting> = ko.observable(new common.AppWorkChangeSetting());
            targetDate: any = moment(new Date()).format("YYYY/MM/DD");
            requiredCheckTime: KnockoutObservable<boolean> = ko.observable(this.isWorkChange() && true);
            timeRequired: KnockoutObservable<boolean> = ko.observable(false);
            //screen B default hidden
            showExcludeHoliday: KnockoutObservable<boolean> = ko.observable(false);
            appCur: any = null;
            appWorkChangeDispInfoDto: any = null;
            constructor( listAppMetadata: Array<model.ApplicationMetadata>, currentApp: model.ApplicationMetadata ) {
                super( listAppMetadata, currentApp );
                let self = this;
                self.appCur = currentApp;
                self.targetDate = currentApp.appDate;
                self.startPage( self.appID() );               
            }
            
            getName(code, name) {
                let result = "";
                if (code) {
                    result = name || text("KAL003_120");
                }
                return result;
            }
            /**
             * 起動する
             */
            startPage( appID: string ): JQueryPromise<any> {
                nts.uk.ui.block.invisible();
                var self = this;
                let dfd = $.Deferred();
                //get Common Setting
                service.getWorkchangeByAppID(appID).done( function( settingData: any ) {
                    let appWorkChangeDispInfo = settingData.appWorkChangeDispInfo,
                        appDispInfoNoDateOutput = appWorkChangeDispInfo.appDispInfoStartupOutput.appDispInfoNoDateOutput,
                        appDispInfoWithDateOutput = appWorkChangeDispInfo.appDispInfoStartupOutput.appDispInfoWithDateOutput,
                        appWorkChangeSet = appWorkChangeDispInfo.appWorkChangeSet,
                        listAppTypeSet = appDispInfoNoDateOutput.requestSetting.applicationSetting.listAppTypeSetting,
                        appTypeSet = _.find(listAppTypeSet, o => o.appType == 2),
                        appWorkChangeDto = settingData.appWorkChange,
                        applicationDto = appWorkChangeDispInfo.appDispInfoStartupOutput.appDetailScreenInfo.application;
                    self.appWorkChangeDispInfoDto = appWorkChangeDispInfo;
                    //A2_申請者 ID
                    self.employeeID = appDispInfoNoDateOutput.employeeInfoLst[0].sid;
                    //A3 事前事後区分
                    //事前事後区分 ※A１
                    self.prePostDisp(appDispInfoNoDateOutput.requestSetting.applicationSetting.appDisplaySetting.prePostAtrDisp == 1 ? true : false);
                    //事前事後区分 Enable ※A２
                    self.prePostEnable(appTypeSet.canClassificationChange);
                    //「申請種類別設定．定型理由の表示」 ※A10
                    self.typicalReasonDisplayFlg(appTypeSet.displayFixedReason == 1 ? true : false);
                    //「申請種類別設定．申請理由の表示」 ※A11
                    self.displayAppReasonContentFlg(appTypeSet.displayAppReason == 1 ? true : false);
                    //A5 勤務を変更する ※A4                    
                    //勤務変更申請設定.勤務時間を変更できる　＝　出来る
                    self.isWorkChange(appWorkChangeSet.workChangeTimeAtr == 1 ? true : false);     
                    //定型理由
                    self.setReasonControl(appDispInfoNoDateOutput.appReasonLst);
                    //申請制限設定.申請理由が必須
                    self.requiredReason(appDispInfoNoDateOutput.requestSetting.applicationSetting.appLimitSetting.requiredAppReason);
                    //A8 勤務時間２ ※A7
                    //共通設定.複数回勤務
                     self.isMultipleTime(settingData.multipleTime);
                    //勤務変更申請基本データ（更新）
                    //workChangeDto
                    ko.mapping.fromJS(appWorkChangeDto, {}, self.appWorkChange().workChange);
                    self.workChangeAtr(self.appWorkChange().workChange().workChangeAtr() == 1 ? true : false );
                    self.excludeHolidayAtr(self.appWorkChange().workChange().excludeHolidayAtr() == 1 ? true : false );
                    let goWorkAtr2 = self.appWorkChange().workChange().goWorkAtr2;
                    let backHomeAtr2 = self.appWorkChange().workChange().backHomeAtr2;
                    goWorkAtr2(nts.uk.util.isNullOrUndefined(goWorkAtr2()) ? 1: goWorkAtr2());
                    backHomeAtr2(nts.uk.util.isNullOrUndefined(backHomeAtr2()) ? 1: backHomeAtr2());
                    //A2_1 申請者
                    self.employeeName(appDispInfoNoDateOutput.employeeInfoLst[0].bussinessName);
                    //就業時間帯名(A6_4、B6_4)で、「null」の場合は空白にしてください
                    let typeCd = self.appWorkChange().workChange().workTypeCd;
                    let typeName = self.appWorkChange().workChange().workTypeName;                
                    let timeCd = self.appWorkChange().workChange().workTimeCd;
                    let timeName = self.appWorkChange().workChange().workTimeName;
                    typeCd(typeCd() === null ? '' : typeCd());
                    typeName(self.getWorkTypeName(typeCd(), appWorkChangeDispInfo.workTypeLst));
                    timeCd(timeCd() === null ? '' : timeCd());
                    timeName(self.getWorkTimeName(timeCd(), appDispInfoWithDateOutput.workTimeLst));
                    //application data
                    ko.mapping.fromJS(applicationDto, {}, self.appWorkChange().application );
                    //setting reason content
                    self.multilContent( self.appWorkChange().application().applicationReason());
                    self.workTypeCodes = _.map(appWorkChangeDispInfo.workTypeLst, o => o.workTypeCode);
                    self.workTimeCodes = _.map(appDispInfoWithDateOutput.workTimeLst, o => o.worktimeCode);
                    self.requiredCheckTime(appWorkChangeDispInfo.setupType == 0 && appWorkChangeDispInfo.appWorkChangeSet.workChangeTimeAtr == 1);
                    self.timeRequired(appWorkChangeDispInfo.setupType == 0);
//                            //画面モード(表示/編集)
//                            //self.editable = ko.observable(detailData.OutMode == 0 ? true: false);                            
//                            
//                            //実績の内容
//                            service.getRecordWorkInfoByDate({
//                                appDate : moment(self.appWorkChange().application().applicationDate()).format(self.dateFormat),
//                                employeeID : self.appWorkChange().application().applicantSID()
//                            }).done((recordWorkInfo) => {
//                                //Binding data
//                                recordWorkInfo.workTypeName = self.getName(recordWorkInfo.workTypeCode, recordWorkInfo.workTypeName);
//                                recordWorkInfo.workTimeName = self.getName(recordWorkInfo.workTimeCode, recordWorkInfo.workTimeName);
//                                ko.mapping.fromJS( recordWorkInfo, {}, self.recordWorkInfo );
//                                 //Focus process
//                                self.selectedReason.subscribe(value => {  $("#inpReasonTextarea").focus(); });
//                                //フォーカス制御
//                                self.changeFocus('#inpStartTime1'); 
//                                
//                                dfd.resolve();
//                            }).fail((res) => {
//                                dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds });
//                                nts.uk.ui.block.clear();
//                                dfd.reject();
//                            });
//                            
//                            dfd.resolve();
//                            nts.uk.ui.block.clear();
//                        
//                        dfd.resolve();
                    self.changeFocus('#inpStartTime1'); 
                }).fail((res) => {
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function() {
                        nts.uk.ui.block.clear();
                        if (res.messageId === "Msg_198" || res.messageId == 'Msg_426') {
                            appcommon.CommonProcess.callCMM045();
                        } else {
                            nts.uk.request.jump("com", "view/ccg/008/a/index.xhtml");        
                        }
                    });
                    dfd.reject();
                });
                return dfd.promise();
            }
            
            getWorkTypeName(code, workTypeLst) {
                let currentWorkType = _.find(workTypeLst, o => o.workTypeCode == code);
                if(nts.uk.util.isNullOrUndefined(currentWorkType)) {
                    return text("KAF007_79");
                } else {
                    return currentWorkType.name;     
                }      
            }
            
            getWorkTimeName(code, workTimeLst) {
                let currentWorkTime = _.find(workTimeLst, o => o.worktimeCode == code);
                if(nts.uk.util.isNullOrUndefined(currentWorkTime)) {
                    return text("KAF007_79");
                } else {
                    return currentWorkTime.workTimeDisplayName.workTimeName;     
                }      
            }
            
            enableTime() {
                let self = this;
                let result = self.editable() && self.requiredCheckTime();
                return result;
            }
            
            showReasonText() {
                let self = this;
                if (self.screenModeNew()) {
                    return self.displayAppReasonContentFlg();
                } else {
                    return self.displayAppReasonContentFlg() != 0 || self.typicalReasonDisplayFlg() != 0;
                }
            }
            
            showRightContent() {
                let self = this;
                return self.appChangeSetting().displayResultAtr() == 1 && self.appWorkChange().application().prePostAtr() == 1;
            }

            /**
             * 「登録」ボタンをクリックする
             * 勤務変更申請の登録を実行する
             */
            update() {
                let self =this,
                appReason: string;    
                nts.uk.ui.block.invisible();
                if(!self.validateInputTime()){
                    nts.uk.ui.block.clear();
                    return;
                }  
                
                let comboBoxReason: string = appcommon.CommonProcess.getComboBoxReason(self.selectedReason(), self.reasonCombo(), self.typicalReasonDisplayFlg());
                let textAreaReason: string = appcommon.CommonProcess.getTextAreaReason(self.multilContent(), self.displayAppReasonContentFlg(), true);
                
                if(!appcommon.CommonProcess.checklenghtReason(comboBoxReason+":"+textAreaReason,"#inpReasonTextarea")){
                    return;
                }
                
                //Approval phase
                self.appWorkChange().appApprovalPhases = self.approvalList;                
                //申請理由
                self.appWorkChange().application().appReasonID = comboBoxReason;
                self.appWorkChange().application().applicationReason(textAreaReason);              
                //勤務を変更する
                self.appWorkChange().workChange().workChangeAtr(self.workChangeAtr() == true ? 1 : 0);
                // 休日に関して
                self.appWorkChange().workChange().excludeHolidayAtr(self.excludeHolidayAtr() == true ? 1 : 0);
                //Change null to unregister value:
                self.changeUnregisterValue();
                
                let workChange = ko.toJS(self.appWorkChange());
                //application change date format
                self.changeDateFormat(workChange);
//                workChange.user = self.user;
//                workChange.reflectPerState = self.reflectPerState;
                workChange.appWorkChangeDispInfoCmd = self.appWorkChangeDispInfoDto;
                service.checkBeforeUpdate(workChange).done((data) => {
                    self.processConfirmMsg(workChange, data, 0);
                 }).fail((res) =>{
                    if (res.optimisticLock == true) {
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_197" }).then(function() {
                            location.reload();
                        });
                    } else {
                        if(nts.uk.util.isNullOrEmpty(res.errors)){
                            dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function() { 
                                nts.uk.ui.block.clear(); 
                                if (res.messageId === "Msg_197") {
                                    location.reload();
                                }
                            });       
                        } else {
                            let errors = res.errors;
                            nts.uk.ui.dialog.bundledErrors({ errors: errors })    
                            .then(function() { nts.uk.ui.block.clear(); });      
                        }  
                    }    
                 });

            }
            
            getBoxReason(){
                var self = this;
                return appcommon.CommonProcess.getComboBoxReason(self.selectedReason(), self.reasonCombo(), self.typicalReasonDisplayFlg());
            
            }
        
            getAreaReason(){
                var self = this;
                return appcommon.CommonProcess.getTextAreaReason(self.multilContent(), self.displayAppReasonContentFlg(), true);   
            }
            
            resfreshReason(appReason: string){
                var self = this;
                self.selectedReason('');   
                self.multilContent(appReason); 
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
                
//                //１．就業時間１（開始時刻：終了時刻） 大小チェック
//                if(workchange.workTimeStart1() > workchange.workTimeEnd1()){
//                    dialog.alertError({messageId:"Msg_579"}).then(function(){nts.uk.ui.block.clear();});
//                     $('#inpStartTime1').focus();
//                    return false;
//                }
//                //２．就業時間２（開始時刻：終了時刻）
//                //共通設定.複数回勤務　＝　利用する
//                if(self.isMultipleTime()){
//                    //has input time 2
//                    if ( !nts.uk.util.isNullOrEmpty(workchange.workTimeStart2())) {
//                        //開始時刻　＞　終了時刻
//                        if(workchange.workTimeStart2() > workchange.workTimeEnd2()){
//                            dialog.alertError({messageId:"Msg_580"}).then(function(){nts.uk.ui.block.clear();});
//                             $('#inpStartTime2').focus();
//                            return false;
//                        }
//                        //就業時間（終了時刻）　>　就業時刻２（開始時刻）
//                        if(workchange.workTimeEnd1() > workchange.workTimeStart2()){
//                            dialog.alertError({messageId:"Msg_581"}).then(function(){nts.uk.ui.block.clear();});
//                             $('#workTimeEnd1').focus();
//                            return false;
//                        }
//                    }
//                }
//                //３．休憩時間１（開始時刻：終了時刻）大小チェック
//                if ( !nts.uk.util.isNullOrEmpty(workchange.breakTimeStart1())) {
//                    //開始時刻　＞　終了時刻
//                    if(workchange.breakTimeStart1() > workchange.breakTimeEnd1()){
//                        dialog.alertError({messageId:"Msg_582"}).then(function(){nts.uk.ui.block.clear();});
//                         $('#breakTimeStart1').focus();
//                        return false;
//                    }
//                }
                return true;
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
             * Convert client date string to server GeneralDate
             */
            private changeDateFormat(data) {
                let self = this,
                    app = data.application;
                //Change application input date                
                app.inputDate = moment.utc( app.inputDate, self.dateTimeFormat ).toISOString() ;
                app.applicationDate = moment.utc( app.applicationDate, self.dateFormat ).toISOString() ;
                app.startDate = moment.utc( app.startDate, self.dateFormat ).toISOString() ;
                app.endDate = moment.utc( app.endDate, self.dateFormat ).toISOString() ;
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
                        appReason = inputReason + "\n" + detailReason;
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
                    
                } );
            }
            public convertIntToTime( data: any ): string {
                if(nts.uk.util.isNullOrUndefined(data)||nts.uk.util.isNullOrEmpty(data)){
                    return null;   
                }
                return nts.uk.time.format.byId("ClockDay_Short_HM", data);
            } 
            /**
             * 「勤務就業選択」ボタンをクリックする
             * KDL003_勤務就業ダイアログを起動する
             */
            openKDL003Click(vm: any)  {
                let self = vm,
                    workChange = vm.appWorkChange().workChange();
                $("#inpStartTime1").ntsError('clear');
                $("#inpEndTime1").ntsError('clear');                
                nts.uk.ui.windows.setShared('parentCodes', {
                    workTypeCodes: self.workTypeCodes,                    
                    selectedWorkTypeCode: workChange.workTypeCd(),
                    workTimeCodes: self.workTimeCodes,
                    selectedWorkTimeCode: workChange.workTimeCd(),
                }, true);

                nts.uk.ui.windows.sub.modal('/view/kdl/003/a/index.xhtml').onClosed(function(): any {
                    //view all code of selected item 
                    var childData = nts.uk.ui.windows.getShared('childData');
                    if (childData) {
                        
                        workChange.workTypeCd(childData.selectedWorkTypeCode);
                        workChange.workTypeName(childData.selectedWorkTypeName);
                        workChange.workTimeCd(childData.selectedWorkTimeCode);
                        workChange.workTimeName(childData.selectedWorkTimeName);
                        self.appWorkChangeDispInfoDto.workTypeCD = childData.selectedWorkTypeCode;
                        self.appWorkChangeDispInfoDto.workTimeCD = childData.selectedWorkTimeCode;
                        service.changeWorkSelection({
                            appWorkChangeDispInfoCmd: self.appWorkChangeDispInfoDto             
                        }).done((rs) =>{
                            self.requiredCheckTime(rs.setupType == 0 && rs.appWorkChangeSet.workChangeTimeAtr == 1);   
                            if(self.requiredCheckTime()){
                                workChange.workTimeStart1(childData.first.start);
                                workChange.workTimeEnd1(childData.first.end); 
                            }    
                        });
                    }
                    //フォーカス制御
                    self.changeFocus('#inpStartTime1'); 
                })
            }
            
            processConfirmMsg(paramInsert: any, result: any, confirmIndex: number) {
                let self = this;
                let confirmMsgLst = result.confirmMsgLst;
                let confirmMsg = confirmMsgLst[confirmIndex];
                if(_.isUndefined(confirmMsg)) {
                    paramInsert.holidayDateLst = result.holidayDateLst;
                    service.updateWorkChange(paramInsert).done((data) => {
                        nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                            if(data.autoSendMail){
                                appcommon.CommonProcess.displayMailResult(data);   
                            } else {
                                self.reBinding(self.listAppMeta, self.appCur, false);
                            }
                        });
                    }).fail((res) => {
                        dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds })
                            .then(function() { nts.uk.ui.block.clear(); });
                    });
                    return;
                }
                
                dialog.confirm({ messageId: confirmMsg.msgID, messageParams: confirmMsg.paramLst }).ifYes(() => {
                    self.processConfirmMsg(paramInsert, result, confirmIndex + 1);
                }).ifNo(() => {
                    nts.uk.ui.block.clear();
                });
            }
        }
    }
}