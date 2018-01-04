module nts.uk.at.view.kaf009.a.viewmodel {
    import common = nts.uk.at.view.kaf009.share.common;
    export class ScreenModel {
        dateType: string = 'YYYY/MM/DD';
        isDisplayOpenCmm018:  KnockoutObservable<boolean> = ko.observable(true);
        isWorkChange:   KnockoutObservable<boolean> = ko.observable(true);
        //kaf000
        kaf000_a: kaf000.a.viewmodel.ScreenModel;
        //current Data
        curentGoBackDirect: KnockoutObservable<common.GoBackDirectData>;
        //申請者
        employeeName: KnockoutObservable<string> = ko.observable("");
        //Pre-POST
        prePostSelected: KnockoutObservable<number> = ko.observable(0);
        workState : KnockoutObservable<boolean> = ko.observable(true);
        typeSiftVisible : KnockoutObservable<boolean> = ko.observable(true);
        // 申請日付
        appDate: KnockoutObservable<string> = ko.observable('');
        //TIME LINE 1
        timeStart1: KnockoutObservable<number> = ko.observable(null);
        timeEnd1: KnockoutObservable<number> = ko.observable(null);   
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
        timeStart2: KnockoutObservable<number> = ko.observable(null);
        timeEnd2: KnockoutObservable<number> = ko.observable(null);
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
        workChangeAtr: KnockoutObservable<boolean> = ko.observable(false);
        //勤務種類
        workTypeCd: KnockoutObservable<string> = ko.observable('');
        workTypeName: KnockoutObservable<string> = ko.observable('');
        //勤務種類
        siftCD: KnockoutObservable<string> = ko.observable('');
        siftName: KnockoutObservable<string> = ko.observable('');
        //comboBox 定型理由
        reasonCombo: KnockoutObservableArray<common.ComboReason> = ko.observableArray([]);
        selectedReason: KnockoutObservable<string> = ko.observable('');
        //MultilineEditor
        requiredReason : KnockoutObservable<boolean> = ko.observable(false);
        multilContent: KnockoutObservable<string> = ko.observable('');
        multiOption: any;
        //Insert command
        command: KnockoutObservable<common.GoBackCommand> = ko.observable(null);
        //list Work Location 
        locationData: Array<common.IWorkLocation>= [];
        //Approval 
        approvalSource: Array<common.AppApprovalPhase> = [];
        employeeID : string ="";
        //menu-bar 
        enableSendMail :KnockoutObservable<boolean> = ko.observable(false); 
        prePostDisp: KnockoutObservable<boolean> = ko.observable(false);
        prePostEnable: KnockoutObservable<boolean> = ko.observable(false);
        useMulti : KnockoutObservable<boolean> = ko.observable(true);
        
        //data work
        workTypeCodes: KnockoutObservableArray<string> = ko.observableArray([]);
        workTimeCodes: KnockoutObservableArray<string> = ko.observableArray([]);
        constructor() {
            let self = this;
            //KAF000_A
            self.kaf000_a = new kaf000.a.viewmodel.ScreenModel();
            //MultilineEditor 
            self.multiOption = ko.mapping.fromJS(new nts.uk.ui.option.MultilineEditorOption({
                resizeable: false,
                width: "500",
                textalign: "left",
            }));
            self.startPage().done(function() {
                self.kaf000_a.start(self.employeeID,1,4,moment(new Date()).format(self.dateType)).done(function(){
                    nts.uk.ui.block.clear();
                    self.appDate.subscribe(value => {
                        self.kaf000_a.getAppDataDate(4, moment(value).format(self.dateType), false);
                    });
                    //フォーカス制御=>申請日付
                    $('#inputdate').focus();
                })                
            });
        }
        /**
         * 
         */
        startPage(): JQueryPromise<any> {
            nts.uk.ui.block.invisible();
            var self = this;
            var dfd = $.Deferred();
            let notInitialSelection = 0; //0:申請時に決める（初期選択：勤務を変更しない）
            let initialSelection = 1; //1:申請時に決める（初期選択：勤務を変更する）
            let notChange = 2; //2:変更しない
            let change = 3; //3:変更する
            
            //get Common Setting
            service.getGoBackSetting().done(function(settingData: any) {
                if(!nts.uk.util.isNullOrEmpty(settingData)){
                    //申請制限設定.申請理由が必須
                    self.requiredReason(settingData.appCommonSettingDto.applicationSettingDto.requireAppReasonFlg == 1 ? true: false);
                    if(settingData.appCommonSettingDto.appTypeDiscreteSettingDtos.length>0){
                        //登録時にメールを送信する Visible
                        self.enableSendMail(settingData.appCommonSettingDto.appTypeDiscreteSettingDtos[0].sendMailWhenRegisterFlg == 1 ? false: true); 
                        //事前事後区分 Enable ※A２
                        //申請種類別設定.事前事後区分を変更できる 〇
                        //申請種類別設定.事前事後区分を変更できない  ×
                        self.prePostEnable(settingData.goBackSettingDto.workChangeFlg == change ? true: false);
                    }
                    //事前事後区分 ※A１
                    //申請表示設定.事前事後区分　＝　表示する　〇
                    //申請表示設定.事前事後区分　＝　表示しない ×
                    self.prePostDisp(settingData.appCommonSettingDto.applicationSettingDto.displayPrePostFlg == 1 ? true: false);
                    if(settingData.goBackSettingDto　!= undefined){
                        
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
                    //場所選択
                    self.getAllWorkLocation();
                    //申請者 ID
                    self.employeeID = settingData.sid;
                    //勤務を変更する
                    //self.workChangeAtr(settingData.goBackSettingDto.workChangeFlg == 1 ? true : false);
                    //定型理由
                    self.setReasonControl(settingData.listReasonDto);
                    //申請者
                    self.employeeName(settingData.employeeName);
                    //直行直帰申請共通設定
                    self.setGoBackCommonSetting(settingData.goBackSettingDto);
                    //Setting data works
                    //勤務種類
                    self.workTypeCd(settingData.dataWorkDto.selectedWorkTypeCd);
                    self.workTypeName(settingData.dataWorkDto.selectedWorkTypeName);
                    //勤務種類
                    self.siftCD(settingData.dataWorkDto.selectedWorkTimeCd);
                    self.siftName(settingData.dataWorkDto.selectedWorkTimeName);
                    //dataWorkDto
                    self.workTypeCodes = settingData.dataWorkDto.workTypeCodes;
                    self.workTimeCodes = settingData.dataWorkDto.workTimeCodes;
                    
                    self.selectedGo.subscribe(value => { $("#inpStartTime1").ntsError("clear"); });
                    self.selectedBack.subscribe(value => { $("#inpEndTime1").ntsError("clear"); });
                    self.selectedGo2.subscribe(value => { $("#inpStartTime2").ntsError("clear"); });
                    self.selectedBack2.subscribe(value => { $("#inpEndTime2").ntsError("clear"); });
                }
                dfd.resolve();
            }).fail((res) => {
                nts.uk.ui.dialog.alertError({messageId: res.messageId}).then(function(){ 
                    nts.uk.request.jump("com", "view/ccg/008/a/index.xhtml");  
                });
                dfd.reject();
            });
            return dfd.promise();
        }
        /**
         * insert//登録ボタンをクリックする
         */
        insert() {
            let self = this;            
            //直行直帰登録前チェック (Kiểm tra trước khi đăng ký)
            //直行直帰するチェック
            self.checkBeforeInsert();     
            
        }
        
        registry(){
            let self = this;
            nts.uk.ui.block.invisible();
            service.insertGoBackDirect(self.getCommand()).done(function() {
                nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function(){
                    location.reload();
                });
            }).fail(function(res: any) {
                nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function(){nts.uk.ui.block.clear();});
            }).then(function(){
                nts.uk.ui.block.clear();    
            })    
        }
        
        /**
         * Clean Screen
         */
        cleanScreen(){
            let self = this;
            self.prePostSelected(0);
            self.appDate(moment().format(self.dateType));
            self.timeStart1(0);   
            self.timeEnd1(0);
            self.timeStart2(0);
            self.timeEnd2(0);
            self.workLocationCD('');
            self.workLocationName('');
            self.workLocationCD2('');
            self.workLocationName2('');
            self.siftCD('');
            self.siftName(''); 
            self.workTypeCd('');
            self.workTypeName('');
            self.selectedReason(null);
            self.multilContent('');
        }
        /**
         * //直行直帰登録前チェック (Kiểm tra trước khi đăng ký)
            //直行直帰するチェック
         */
        checkBeforeInsert(): JQueryPromise<boolean> {
            let self = this;
            let dfd = $.Deferred();
            //check before Insert 
            let errorFlag = self.kaf000_a.errorFlag;
            let errorMsg = self.kaf000_a.errorMsg;
            if(errorFlag!=0){
                nts.uk.ui.dialog.alertError({ messageId: errorMsg }).then(function(){nts.uk.ui.block.clear();});    
            } else {
                self.checkUse();
            }
            return dfd.promise();
        }
        checkRegister(){
            nts.uk.ui.block.invisible();
            let self = this;
            let dfd = $.Deferred();
            service.checkInsertGoBackDirect(self.getCommand()).done(function(){
                    self.registry();
                    dfd.resolve(true);
                }).fail(function(res: any){
                    if(res.messageId =="Msg_297"){
                        nts.uk.ui.dialog.confirm({ messageId: 'Msg_297' }).ifYes(function() {
                            self.registry();
                            dfd.resolve(true);
                        }).ifNo(function() {
                            nts.uk.ui.block.clear();
                            dfd.resolve(false);
                        });
                    } else if(res.messageId == "Msg_298"){
                        dfd.reject();
                        nts.uk.ui.block.clear();
                        //Chưa có thoi gian thuc nên chưa chưa so sánh các giá trị nhập vào được
                        //khi có so sánh trên server thì gửi thêm vị trí giá trị giờ nhập sai nữa
                        $('#inpStartTime1').ntsError('set', {messageId:"Msg_298"});
                        $('#inpEndTime1').ntsError('set', {messageId:"Msg_298"});
                        if(self.selectedGo2()==1){
                            $('#inpStartTime2').ntsError('set', {messageId:"Msg_298"});
                        }
                        if(self.selectedBack2()==1){
                            $('#inpEndTime2').ntsError('set', {messageId:"Msg_298"});
                        }
                    }else{
                       nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function(){nts.uk.ui.block.clear();});
                    }
                })
            return dfd.promise();
        }
        
        /**
         * アルゴリズム「直行直帰するチェック」を実行する
         */
        checkUse(){
            let self = this;
            if ((!self.useMulti() && self.selectedGo() == 0 && self.selectedBack()== 0)
                || (self.useMulti() && self.selectedGo() == 0 && self.selectedBack()== 0 && self.selectedGo2() == 0 && self.selectedBack2()== 0)) {
                //直行直帰区分＝なし
                nts.uk.ui.dialog.confirm({ messageId: 'Msg_338' }).ifYes(function() {
                    self.checkRegister();
                }).ifNo(function() {
                    nts.uk.ui.block.clear();
                });
            } else {
                 self.checkRegister();
            }
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
                        arrTemp.push({ workLocationCode: value.workLocationCD, workLocationName: value.workLocationName });
                    };
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
         * 1: insert 
         */
        getCommand() {
            let self = this; 
            let goBackCommand: common.GoBackCommand = new common.GoBackCommand();
            goBackCommand.workTypeCD = self.workTypeCd();
            goBackCommand.siftCD = self.siftCD();
            goBackCommand.workChangeAtr = self.workChangeAtr() == true ? 1 : 0;
            goBackCommand.goWorkAtr1 = self.selectedGo();
            goBackCommand.backHomeAtr1 = self.selectedBack();
            goBackCommand.workTimeStart1 = self.timeStart1();
            goBackCommand.workTimeEnd1 = self.timeEnd1();
            goBackCommand.goWorkAtr2 = self.selectedGo2();
            goBackCommand.backHomeAtr2 = self.selectedBack2();
            goBackCommand.workTimeStart2 = self.timeStart2();
            goBackCommand.workTimeEnd2 = self.timeEnd2();
            goBackCommand.workLocationCD1 = self.workLocationCD();
            goBackCommand.workLocationCD2 = self.workLocationCD2();
            let txtReasonTmp = self.selectedReason();
            if(!nts.uk.text.isNullOrEmpty(self.selectedReason())){
                let reasonText = _.find(self.reasonCombo(),function(data){return data.reasonId == self.selectedReason()});;
                txtReasonTmp = reasonText.reasonName;
            }
            let appCommand : common.ApplicationCommand  = new common.ApplicationCommand(
                txtReasonTmp,
                self.prePostSelected(),
                moment().format(self.dateType),
                self.employeeID,
                "",
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
                appApprovalPhaseCmds : self.approvalSource
                }
            return commandTotal;
            
        }

        /**
         * Set common Setting 
         */
        setGoBackCommonSetting(data: common.GoBackDirectSetting) {
            let self = this;
            if (!nts.uk.util.isNullOrUndefined(data)) {
                self.commentGo1(data.commentContent1);
                self.commentGo2(data.commentContent1);
                self.commentBack1(data.commentContent2);
                self.commentBack2(data.commentContent2);
                self.colorGo(data.commentFontColor1);
                self.colorBack(data.commentFontColor2);
                self.fontWeightGo(data.commentFontWeight1);
                self.fontWeightBack(data.commentFontWeight2);
                switch(data.workChangeFlg){
                    //直行直帰申請共通設定.勤務の変更　＝　申請時に決める
                    case 2:{
                        self.workState(false);
                        self.typeSiftVisible(false);
                        break;
                    }
                    case 3:{
                        self.workState(false);
                        break;
                    }
                    default :{
                        break;
                    }
                }
                //self.workState(data.workChangeFlg == 1 ? true : false);
            }
        }
        /**
         * set data from Server 
         */
        setValueControl(data: common.GoBackDirectData) {
            var self = this;
            self.prePostSelected(data.workChangeAtr);
            //Line 1
            self.timeStart1(data.workTimeStart1);
            self.timeEnd1(data.workTimeEnd1);
            self.selectedGo(data.goWorkAtr1);
            self.selectedBack(data.backHomeAtr1);
            self.workLocationCD(data.workLocationCD1);
            //LINe 2
            self.timeStart2(data.workTimeStart2);
            self.timeEnd2(data.workTimeEnd2);
            self.selectedGo2(data.goWorkAtr2);
            self.selectedBack2(data.backHomeAtr2);
            self.workLocationCD2(data.workLocationCD2);
            //workType, Sift
            self.workChangeAtr(data.workChangeAtr == 1 ? true : false);
            self.workTypeCd(data.workTypeCD);
            self.siftCD(data.siftCD);
        }
        /**
         * set reason 
         */
        setReasonControl(data: Array<common.ReasonDto>) {
            var self = this;
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
            var self = this;
            nts.uk.ui.block.invisible();
            if (line == 1) {
                nts.uk.ui.windows.setShared('KDL010SelectWorkLocation', self.workLocationCD());
            } else {
                nts.uk.ui.windows.setShared('KDL010SelectWorkLocation', self.workLocationCD2());
            };
            nts.uk.ui.windows.sub.modal("/view/kdl/010/a/index.xhtml", { dialogClass: "no-close" }).onClosed(() => {
                var self = this;
                var returnWorkLocationCD = nts.uk.ui.windows.getShared("KDL010workLocation");
                if (returnWorkLocationCD !== undefined) {
                    if (line == 1) {
                        self.workLocationCD(returnWorkLocationCD);
                        self.workLocationName(self.findWorkLocationName(returnWorkLocationCD));
                        //フォーカス制御 => 直行区分1
                        $('#goWorkAtr1').focus();
                    } else {
                        self.workLocationCD2(returnWorkLocationCD);
                        self.workLocationName2(self.findWorkLocationName(returnWorkLocationCD));
                        //フォーカス制御 => 直行区分2
                        $('#goWorkAtr2').focus();
                    };
                    nts.uk.ui.block.clear();
                }
                else {
                    self.workLocationCD = ko.observable("");
                    nts.uk.ui.block.clear();
                }
            });
        }
        /**
         * KDL003
         */
        openDialogKdl003() {
            let self = this;
            let workTypeCodes = self.workTypeCodes;
            let workTimeCodes = self.workTimeCodes;
            nts.uk.ui.windows.setShared('parentCodes', {
                workTypeCodes: workTypeCodes,
                selectedWorkTypeCode: self.workTypeCd(),
                workTimeCodes: workTimeCodes,
                selectedWorkTimeCode: self.siftCD()
            }, true);

            nts.uk.ui.windows.sub.modal('/view/kdl/003/a/index.xhtml').onClosed(function(): any {
                //view all code of selected item 
                var childData = nts.uk.ui.windows.getShared('childData');
                if (childData) {
                    self.workTypeCd(childData.selectedWorkTypeCode);
                    self.workTypeName(childData.selectedWorkTypeName);
                    self.siftCD(childData.selectedWorkTimeCode);
                    self.siftName(childData.selectedWorkTimeName);
                    //フォーカス制御 => 定型理由
                    $('#combo-box').focus();
                }
            })
        }
        
        /**
         * Jump to CMM018 Screen
         */
        openCMM018(){
            let self = this;
            nts.uk.request.jump("com", "/view/cmm/018/a/index.xhtml", {screen: 'Application', employeeId: self.employeeID()});  
        }
    }
    
}

