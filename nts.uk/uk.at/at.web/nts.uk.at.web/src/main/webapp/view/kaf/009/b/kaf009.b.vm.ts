module nts.uk.at.view.kaf009.b {
    import common = nts.uk.at.view.kaf009.share.common;
    import model = nts.uk.at.view.kaf000.b.viewmodel.model;
    import appcommon = nts.uk.at.view.kaf000.shr.model;
    export module viewmodel{
        export class ScreenModel extends kaf000.b.viewmodel.ScreenModel {
            DATE_FORMAT: string = 'YYYY/MM/DD';
            screenModeNew: KnockoutObservable<boolean> = ko.observable(false);
            //画面モード(表示/編集)
            editable: KnockoutObservable<boolean> = ko.observable( true );
            //kaf000
            kaf000_a: kaf000.a.viewmodel.ScreenModel;
            //current Data
            curentGoBackDirect: KnockoutObservable<common.GoBackDirectData>;
            //申請者
            employeeName: KnockoutObservable<string> = ko.observable("");
            //Pre-POST
            prePostSelected: KnockoutObservable<number> = ko.observable(1);
            workState : KnockoutObservable<boolean> = ko.observable(true);;
            typeSiftVisible : KnockoutObservable<boolean> = ko.observable(true);
            // 申請日付
            appDate: KnockoutObservable<string> = ko.observable(moment().format(this.DATE_FORMAT));;
            //TIME LINE 1
            timeStart1: KnockoutObservable<number> = ko.observable(0);
            timeEnd1: KnockoutObservable<number> = ko.observable(0);   
            //場所名前 
            workLocationCD: KnockoutObservable<string> = ko.observable('');
            workLocationName: KnockoutObservable<string> = ko.observable('');
            //comment 
            commentGo1: KnockoutObservable<string> = ko.observable('');
            commentBack1: KnockoutObservable<string> = ko.observable('');
            //switch button selected
            selectedBack: any = ko.observable(1);
            selectedGo: any = ko.observable(1);
            //Back Home 2
            selectedBack2: any = ko.observable(1);
            //Go Work 2
            selectedGo2: any = ko.observable(1);
            //TIME LINE 2
            timeStart2: KnockoutObservable<number> = ko.observable(0);
            timeEnd2: KnockoutObservable<number> = ko.observable(0);
            //場所名前 
            workLocationCD2: KnockoutObservable<string> = ko.observable('');
            workLocationName2: KnockoutObservable<string> = ko.observable('');
            //comment
            commentGo2: KnockoutObservable<string> = ko.observable('');
            commentBack2: KnockoutObservable<string> = ko.observable('');
            //color, font Weight
            colorGo: KnockoutObservable<string> = ko.observable('#000000');
            colorBack: KnockoutObservable<string> = ko.observable('#000000');
            fontWeightGo: KnockoutObservable<number> = ko.observable(0);
            fontWeightBack: KnockoutObservable<number> = ko.observable(0);
            
            //勤務を変更する 
            workChangeAtr: KnockoutObservable<boolean> = ko.observable(true);
            //勤務種類
            workTypeCd: KnockoutObservable<string> = ko.observable('');
            workTypeName: KnockoutObservable<string> = ko.observable('');
            //勤務種類
            siftCD: KnockoutObservable<string> = ko.observable('');
            siftName: KnockoutObservable<string> = ko.observable('');
            //comboBox 定型理由
            reasonCombo: KnockoutObservableArray<common.ComboReason> = ko.observableArray([]);
            selectedReason: KnockoutObservable<string> = ko.observable('');
            displayTypicalReason: KnockoutObservable<boolean> = ko.observable(false);
            //MultilineEditor
            requiredReason : KnockoutObservable<boolean> = ko.observable(false);
            multilContent: KnockoutObservable<string> = ko.observable('');
            multiOption: any;
            displayReason: KnockoutObservable<boolean> = ko.observable(false);
            //Insert command
            command: KnockoutObservable<common.GoBackCommand> = ko.observable(null);
            //list Work Location 
            locationData: Array<common.IWorkLocation>= [];;
            //Approval 
            approvalSource: Array<common.AppApprovalPhase> = [];
            employeeID : string = "";
            //appID : KnockoutObservable<string> = ko.observable('');
            //menu-bar 
            enableSendMail :KnockoutObservable<boolean> = ko.observable(false); 
            prePostDisp: KnockoutObservable<boolean> = ko.observable(false);
            prePostEnable: KnockoutObservable<boolean> = ko.observable(false);
            useMulti : KnockoutObservable<boolean> = ko.observable(true);
            version : number  = 0;
            isDisplayOpenCmm018:  KnockoutObservable<boolean> = ko.observable(true);
            isWorkChange:   KnockoutObservable<boolean> = ko.observable(true);
            
            constructor(listAppMetadata: Array<model.ApplicationMetadata>, currentApp: model.ApplicationMetadata) {
                super(listAppMetadata, currentApp);
                let self = this;
                self.multiOption = ko.mapping.fromJS(new nts.uk.ui.option.MultilineEditorOption({
                    resizeable: false,
                    width: "500",
                    textalign: "left",
                }));
                //self.startPage(currentApp.appID);
                self.startPage(self.appID());
                //self.appID(currentApp.appID);                
            }

            /**
             * 
             */
            startPage(appId : string): JQueryPromise<any> {
                nts.uk.ui.block.invisible();
                var self = this;
                let dfd = $.Deferred();
                let notInitialSelection = 0; //0:申請時に決める（初期選択：勤務を変更しない）
                let initialSelection = 1; //1:申請時に決める（初期選択：勤務を変更する）
                let notChange = 2; //2:変更しない
                let change = 3; //3:変更する
                //get Common Setting
                service.getGoBackSetting().done(function(settingData: any) {
                    self.displayTypicalReason(settingData.appCommonSettingDto.appTypeDiscreteSettingDtos[0].typicalReasonDisplayFlg);
                    self.displayReason(settingData.appCommonSettingDto.appTypeDiscreteSettingDtos[0].displayReasonFlg);
                    self.employeeID = settingData.sid;
                    //get Reason
                    self.setReasonControl(settingData.listReasonDto);
                    //set employee Name
                    self.employeeName(settingData.employeeName);
                    //set Common Setting
                    self.setGoBackSetting(settingData.goBackSettingDto);
                    //申請制限設定.申請理由が必須
                    self.requiredReason(settingData.appCommonSettingDto.applicationSettingDto.requireAppReasonFlg == 1 ? true: false);
                    if(settingData.appCommonSettingDto.appTypeDiscreteSettingDtos.length>0){
                        //登録時にメールを送信する Visible
                        self.enableSendMail(settingData.appCommonSettingDto.appTypeDiscreteSettingDtos[0].sendMailWhenRegisterFlg == 1 ? true: false);  
                    }
                    //事前事後区分 ※A１
                    //申請表示設定.事前事後区分　＝　表示する　〇
                    //申請表示設定.事前事後区分　＝　表示しない ×
                    self.prePostDisp(settingData.appCommonSettingDto.applicationSettingDto.displayPrePostFlg == 1 ? true: false);
                    if(settingData.goBackSettingDto　!= undefined){
                        //事前事後区分 Enable ※A２
                        //直行直帰申請共通設定.勤務の変更　＝　申請種類別設定.事前事後区分を変更できる 〇
                        //直行直帰申請共通設定.勤務の変更　＝　申請種類別設定.事前事後区分を変更できない  ×
                        self.prePostEnable(settingData.goBackSettingDto.workChangeFlg == change ? true: false);
                        //条件：直行直帰申請共通設定.勤務の変更　＝　申請時に決める（初期選択：勤務を変更する）
                        //条件：直行直帰申請共通設定.勤務の変更　＝　申請時に決める（初期選択：勤務を変更しない）
                        if(settingData.goBackSettingDto.workChangeFlg == notInitialSelection 
                          || settingData.goBackSettingDto.workChangeFlg == initialSelection){
                            self.isWorkChange(true);
                            if(settingData.goBackSettingDto.workChangeFlg == notInitialSelection ){
                                self.workChangeAtr(false);
                            }else{
                                self.workChangeAtr(true);
                            }
                            
                        }else if(settingData.goBackSettingDto.workChangeFlg == notChange){//条件：直行直帰申請共通設定.勤務の変更　＝　変更しない
                            self.isWorkChange(false);
                            self.workChangeAtr(false);
                        }else{//条件：直行直帰申請共通設定.勤務の変更　＝　変更する
                            self.workChangeAtr(true);
                            self.isWorkChange(true);
                            self.workState(false);
                        }
                        
                    }
                    //共通設定.複数回勤務
                    self.useMulti(settingData.dutiesMulti);
                    //Get data 
                    service.getGoBackDirectDetail(appId).done(function(detailData: any) {
                        self.version = detailData.goBackDirectlyDto.version;
                        //get all Location 
                        self.getAllWorkLocation();
                        self.workTypeName(detailData.workTypeName);
                        self.siftName(detailData.workTimeName);
                        self.workLocationName(detailData.workLocationName1 == null ? '' : detailData.workLocationName1);
                        self.workLocationName2(detailData.workLocationName2 == null ? '' : detailData.workLocationName2);
                        self.prePostSelected(detailData.prePostAtr);
                        self.multilContent(detailData.appReason);
                        self.appDate(detailData.appDate);
                        self.employeeName(detailData.employeeName);
                        //Set Value of control
                        self.setValueControl(detailData.goBackDirectlyDto);
                        self.selectedGo.subscribe(value => { $("#inpStartTime1").ntsError("clear"); });
                        self.selectedBack.subscribe(value => { $("#inpEndTime1").ntsError("clear"); });
                        self.selectedGo2.subscribe(value => { $("#inpStartTime2").ntsError("clear"); });
                        self.selectedBack2.subscribe(value => { $("#inpEndTime2").ntsError("clear"); });
                        
                        //画面モード(表示/編集)
                        //self.editable = ko.observable(detailData.outMode == 0 ? true: false);
                        //Focus process
                        self.selectedReason.subscribe(value => {  $("#inpReasonTextarea").focus(); });
                        //フォーカス制御 => 勤務時間直行
                        $("#inpStartTime1").focus();
                    }).fail(function() {
                        dfd.resolve();
                    });
                    dfd.resolve();
                });                               
                return dfd.promise();
            }

            /**
             * 
             */
            update() {
                $("#inpStartTime1").ntsError("clear"); 
                $("#inpEndTime1").ntsError("clear"); 
                $("#inpStartTime2").ntsError("clear");
                $("#inpEndTime2").ntsError("clear");
                nts.uk.ui.block.invisible();
                let self = this;
                if(!appcommon.CommonProcess.checklenghtReason(!nts.uk.text.isNullOrEmpty(self.getCommand().appCommand.appReasonID) ? self.getCommand().appCommand.appReasonID + "\n" + self.multilContent() : self.multilContent(),"#inpReasonTextarea")){
                        return;
                }
                var promiseResult = self.checkUse();
                promiseResult.done((result) => {
                    if (result) {
                        service.updateGoBackDirect(self.getCommand()).done(function() {
                            nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function(){
                                location.reload();
                            });
                        })
                        .fail(function(res) { 
                            if(res.optimisticLock == true){
                                nts.uk.ui.dialog.alertError({ messageId: "Msg_197" }).then(function(){
                                    location.reload();
                                });    
                            } else {
                                nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function(){nts.uk.ui.block.clear();}); 
                            }
                        });
                    }
                });
            }
            /**
             * アルゴリズム「直行直帰するチェック」を実行する
             */
            checkUse(): JQueryPromise<boolean> {
                let self = this;
                let dfd = $.Deferred();

                if ((!self.useMulti() && self.selectedGo() == 0 && self.selectedBack() == 0)
                    || (self.useMulti() && self.selectedGo() == 0 && self.selectedBack() == 0 && self.selectedGo2() == 0 && self.selectedBack2() == 0)) {
                    //直行直帰区分＝なし
                    nts.uk.ui.dialog.confirm({ messageId: 'Msg_338' }).ifYes(function() {
                        self.checkUpdate().done(function(result) {
                            dfd.resolve(result);
                        }).fail(function(res) {
                            dfd.resolve(res);
                        });
                    }).ifNo(function() {
                        nts.uk.ui.block.clear();
                    });
                } else {
                    self.checkUpdate().done(function(result) {
                        dfd.resolve(result);
                    }).fail(function(res) {
                        dfd.resolve(res);
                    });
                }

                return dfd.promise();
            }
            checkUpdate(): JQueryPromise<boolean> {
                let self = this;
                let dfd = $.Deferred();
                service.checkBeforeChangeGoBackDirect(self.getCommand()).done(function() {
                    dfd.resolve(true);
                }).fail(function(res) {
                    if (res.messageId == "Msg_297") {
                        nts.uk.ui.dialog.confirm({ messageId: 'Msg_297' }).ifYes(function() {
                            dfd.resolve(true);
                        }).ifNo(function() {
                            let showMsg : boolean = true;
                            nts.uk.ui.block.clear();
                            //入力項目を警告「黄色」枠を表示する
                            if(self.selectedGo() == 1 && !nts.uk.util.isNullOrEmpty(self.timeStart1())){
                                $("#inpStartTime1").css("background", "#FFFF00");
                                showMsg = false;
                            }
                            if(self.selectedBack()== 1 && !nts.uk.util.isNullOrEmpty(self.timeEnd1())){
                                $("#inpEndTime1").css("background", "#FFFF00");
                                showMsg = false;
                            }
                            if(self.useMulti()){
                                if(self.selectedGo2()==1 && !nts.uk.util.isNullOrEmpty(self.timeStart2())){
                                    $("#inpStartTime2").css("background", "#FFFF00");
                                    showMsg = false;
                                }
                                if(self.selectedBack2()==1 && !nts.uk.util.isNullOrEmpty(self.timeEnd2())){
                                    $("#inpEndTime2").css("background", "#FFFF00");
                                    showMsg = false;
                                }
                            }
                            if(showMsg){
                                nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds });
                            }
                            dfd.resolve(false);
                        });
                    } else if (res.messageId == "Msg_298") {
                        dfd.reject();
                        let showMsg : boolean = true;
                        nts.uk.ui.block.clear();
                        if(self.selectedGo() == 1 && !nts.uk.util.isNullOrEmpty(self.timeStart1())){
                            $("#inpStartTime1").ntsError('set', {messageId:"Msg_298"});
                            showMsg = false;
                        }
                        if(self.selectedBack()== 1 && !nts.uk.util.isNullOrEmpty(self.timeEnd1())){
                            $("#inpEndTime1").ntsError('set', {messageId:"Msg_298"});
                            showMsg = false;
                        }
                        if(self.useMulti()){
                            if(self.selectedGo2()==1 && !nts.uk.util.isNullOrEmpty(self.timeStart2())){
                                $("#inpStartTime2").ntsError('set', {messageId:"Msg_298"});
                                showMsg = false;
                            }
                            if(self.selectedBack2()==1 && !nts.uk.util.isNullOrEmpty(self.timeEnd2())){
                                $("#inpEndTime2").ntsError('set', {messageId:"Msg_298"});
                                showMsg = false;
                            }
                        }
                        if(showMsg){
                            nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds });
                        }
                    } else{
                       nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function(){nts.uk.ui.block.clear();});    
                    }
                });
                return dfd.promise();
            }

            /**
             * get All Work Location
             */
            getAllWorkLocation() {
                let self = this;
                let arrTemp: Array<common.IWorkLocation> = [];
                service.getAllLocation().done(function(data: any) {
                    _.forEach(data, function(value) {
                        if(!nts.uk.util.isNullOrUndefined(value)){
                            arrTemp.push({ workLocationCode: value.workLocationCD == null ? '': value.workLocationCD, workLocationName: value.workLocationName == null ? '' : value.workLocationName});
                        }
                    });
                    self.locationData = arrTemp;
                }).fail(function() {

                })
            }
            /**
             * find Work Location Name from Work Location Code
             */
            findWorkLocationName(code: string) {
                let self = this;
                let locationName: string = "";
                let location: common.IWorkLocation = _.find(self.locationData, function(o) { return o.workLocationCode == code });
                locationName = location.workLocationName;
                return locationName;
            }

            /**
             * 2: update 
             */
            getCommand() {
                let self = this; 
                let goBackCommand: common.GoBackCommand = new common.GoBackCommand();
                goBackCommand.version = self.inputCommandEvent().version;
                goBackCommand.appID = self.appID();
                goBackCommand.workTypeCD = self.workTypeCd();
                goBackCommand.siftCD = self.siftCD();
                goBackCommand.workChangeAtr = self.workChangeAtr() == true ? 1 : 0;
                goBackCommand.goWorkAtr1 = self.selectedGo();
                goBackCommand.backHomeAtr1 = self.selectedBack();
                goBackCommand.workTimeStart1 = nts.uk.util.isNullOrEmpty(self.timeStart1()) ? -1 : self.timeStart1();
                goBackCommand.workTimeEnd1 = nts.uk.util.isNullOrEmpty(self.timeEnd1()) ? -1 : self.timeEnd1();
                goBackCommand.goWorkAtr2 = self.selectedGo2();
                goBackCommand.backHomeAtr2 = self.selectedBack2();
                goBackCommand.workTimeStart2 = nts.uk.util.isNullOrEmpty(self.timeStart2()) ? -1 : self.timeStart2();
                goBackCommand.workTimeEnd2 = nts.uk.util.isNullOrEmpty(self.timeEnd2()) ? -1 : self.timeEnd2();
                goBackCommand.workLocationCD1 = self.workLocationCD();
                goBackCommand.workLocationCD2 = self.workLocationCD2();
                
                let txtReasonTmp = self.selectedReason();
                if(!nts.uk.text.isNullOrEmpty(self.selectedReason())){
                    let reasonText = _.find(self.reasonCombo(),function(data){return data.reasonId == self.selectedReason()});;
                    txtReasonTmp = reasonText.reasonName;
                }
                if(!appcommon.CommonProcess.checklenghtReason(!nts.uk.text.isNullOrEmpty(txtReasonTmp) ? txtReasonTmp + "\n" + self.multilContent() : self.multilContent(),"#inpReasonTextarea")){
                        return;
                }
                let appCommand : common.ApplicationCommand  = new common.ApplicationCommand(
                    self.version,
                    self.appID(),
                    txtReasonTmp,
                    self.prePostSelected(),
                    self.appDate(),
                    self.employeeID,
                    self.multilContent(),
                    self.appDate(),
                    self.multilContent(),
                    self.employeeID,
                    self.appDate(),
                    self.appDate(),
                    self.appDate(),
                    self.appDate());
                
                let commandTotal = {
                    goBackCommand : goBackCommand,
                    appCommand : appCommand,
                    appApprovalPhaseCmds : self.approvalList
                    }
                return commandTotal;
            }

            /**
             * Set common Setting 
             */
            setGoBackSetting(data: common.GoBackDirectSetting) {
                let self = this;
                if (data != undefined) {
                    self.commentGo1(data.commentContent1);
                    self.commentGo2(data.commentContent1);
                    self.commentBack1(data.commentContent2);
                    self.commentBack2(data.commentContent2);
                    self.colorGo(data.commentFontColor1);
                    self.colorBack(data.commentFontColor2);
                    self.fontWeightGo(data.commentFontWeight1);
                    self.fontWeightBack(data.commentFontWeight2);
                    switch (data.workChangeFlg) {
                        //直行直帰申請共通設定.勤務の変更　＝　申請時に決める
                        case 2: {
                            self.workState(false);
                            self.typeSiftVisible(false);
                            break;
                        }
                        case 3: {
                            self.workState(false);
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                }
            }

            /**
             * set data from Server 
             */
            setValueControl(data: common.GoBackDirectData) {
                let self = this;
                if (!nts.uk.util.isNullOrEmpty(data)) {
                    //Line 1
                    self.timeStart1(data.workTimeStart1 == -1 ? null : data.workTimeStart1);
                    self.timeEnd1(data.workTimeEnd1 == -1 ? null : data.workTimeEnd1);
                    self.selectedGo(data.goWorkAtr1);
                    self.selectedBack(data.backHomeAtr1);
                    self.workLocationCD(data.workLocationCD1 == null ? '' : data.workLocationCD1);
                    //Line 2
                    self.timeStart2(data.workTimeStart2 == -1 ? null : data.workTimeStart2);
                    self.timeEnd2(data.workTimeEnd2 == -1 ? null : data.workTimeEnd2);
                    self.selectedGo2(data.goWorkAtr2);
                    self.selectedBack2(data.backHomeAtr2);
                    self.workLocationCD2(data.workLocationCD2 == null ? '' : data.workLocationCD2);
                    //workType, Sift
                    self.workChangeAtr(data.workChangeAtr == 1 ? true : false);
                    self.workTypeCd(data.workTypeCD);
                    self.siftCD(data.siftCD);
                }
            }

            /**
             * set reason 
             */
            setReasonControl(data: Array<common.ReasonDto>) {
                let self = this;
                let comboSource: Array<common.ComboReason> = [];
                _.forEach(data, function(value: common.ReasonDto) {
                    self.reasonCombo.push(new common.ComboReason(value.displayOrder, value.reasonTemp, value.reasonID));
                    if(value.defaultFlg === 1){
                        self.selectedReason(value.reasonID);
                    }
                });
            }

            /**
             * KDL010_勤務場所選択を起動する
             */
            openLocationDialog(line: number) {
                let self = this;
                nts.uk.ui.block.invisible();
                if (line == 1) {
                    nts.uk.ui.windows.setShared('KDL010SelectWorkLocation', self.workLocationCD());
                } else {
                    nts.uk.ui.windows.setShared('KDL010SelectWorkLocation', self.workLocationCD2());
                };
                nts.uk.ui.windows.sub.modal("/view/kdl/010/a/index.xhtml", { dialogClass: "no-close" }).onClosed(() => {
                    var self = this;
                var returnWorkLocationCD = nts.uk.ui.windows.getShared("KDL010workLocation");
                if (!nts.uk.util.isNullOrEmpty(returnWorkLocationCD)) {
                    if (line == 1) {
                        self.workLocationCD(returnWorkLocationCD);
                        self.workLocationName(self.findWorkLocationName(returnWorkLocationCD));
                        //フォーカス制御 => 直行区分1
                        $("#goWorkAtr1").focus();
                    } else {
                        self.workLocationCD2(returnWorkLocationCD);
                        self.workLocationName2(self.findWorkLocationName(returnWorkLocationCD));
                        //フォーカス制御 => 直行区分2
                        $("#goWorkAtr2").focus();
                    }                   
                }
                else {
                    if (line == 1) {
                        self.workLocationCD('');    
                        self.workLocationName('');
                    } else {
                        self.workLocationCD2('');    
                        self.workLocationName2('');    
                    }              
                }
                 nts.uk.ui.block.clear();
                });
            }

            /**
             * KDL003
             */
            openDialogKdl003() {
                let self = this;
                let workTypeCodes = [];

                let workTimeCodes = [];
                nts.uk.ui.windows.setShared('parentCodes', {
                    workTypeCodes: workTypeCodes,
                    selectedWorkTypeCode: self.workTypeCd(),
                    workTimeCodes: workTimeCodes,
                    selectedWorkTimeCode: self.siftCD()
                }, true);

                nts.uk.ui.windows.sub.modal('/view/kdl/003/a/index.xhtml').onClosed(function(): any {
                    //view all code of selected item 
                    let childData = nts.uk.ui.windows.getShared('childData');
                    if (childData) {
                        self.workTypeCd(childData.selectedWorkTypeCode);
                        self.workTypeName(childData.selectedWorkTypeName);
                        self.siftCD(childData.selectedWorkTimeCode);
                        self.siftName(childData.selectedWorkTimeName);
                        //フォーカス制御 => 定型理由
                        $("#combo-box").focus();
                    }
                })
            }
        }
    }
}