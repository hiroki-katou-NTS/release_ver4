module nts.uk.at.view.kaf005.b {
    import common = nts.uk.at.view.kaf005.share.common;
    import model = nts.uk.at.view.kaf000.b.viewmodel.model;
    import service = nts.uk.at.view.kaf005.shr.service;
    import dialog = nts.uk.ui.dialog;
    import appcommon = nts.uk.at.view.kaf000.shr.model;
    export module viewmodel {
        export class ScreenModel extends kaf000.b.viewmodel.ScreenModel {
            
            screenModeNew: KnockoutObservable<boolean> = ko.observable(false);
            //current Data
            //        curentGoBackDirect: KnockoutObservable<common.GoBackDirectData>;
            //manualSendMailAtr
            manualSendMailAtr: KnockoutObservable<boolean> = ko.observable(false);
            displayBreakTimeFlg: KnockoutObservable<boolean> = ko.observable(false);
            //申請者
            employeeName: KnockoutObservable<string> = ko.observable("");
            //Pre-POST
            prePostSelected: KnockoutObservable<number> = ko.observable(0);
            workState: KnockoutObservable<boolean> = ko.observable(true);;
            typeSiftVisible: KnockoutObservable<boolean> = ko.observable(true);
            // 申請日付
            appDate: KnockoutObservable<string> = ko.observable(moment().format('YYYY/MM/DD'));
            //TIME LINE 1
            timeStart1: KnockoutObservable<number> = ko.observable(null);
            timeEnd1: KnockoutObservable<number> = ko.observable(null);
            //TIME LINE 2
            timeStart2: KnockoutObservable<number> = ko.observable(null);
            timeEnd2: KnockoutObservable<number> = ko.observable(null);
            //勤務種類
            workTypeCd: KnockoutObservable<string> = ko.observable('');
            workTypeName: KnockoutObservable<string> = ko.observable('');
            //勤務種類
            siftCD: KnockoutObservable<string> = ko.observable('');
            siftName: KnockoutObservable<string> = ko.observable('');
            workTypecodes: KnockoutObservableArray<string> = ko.observableArray([]);
            workTimecodes: KnockoutObservableArray<string> = ko.observableArray([]);
            //comboBox 定型理由
            reasonCombo: KnockoutObservableArray<common.ComboReason> = ko.observableArray([]);
            selectedReason: KnockoutObservable<string> = ko.observable('');
            //MultilineEditor
            requiredReason: KnockoutObservable<boolean> = ko.observable(false);
            multilContent: KnockoutObservable<string> = ko.observable('');
            //comboBox 定型理由
            reasonCombo2: KnockoutObservableArray<common.ComboReason> = ko.observableArray([]);
            selectedReason2: KnockoutObservable<string> = ko.observable('');
            //MultilineEditor
            requiredReason2: KnockoutObservable<boolean> = ko.observable(false);
            multilContent2: KnockoutObservable<string> = ko.observable('');
            //Approval 
            approvalSource: Array<common.AppApprovalPhase> = [];
            employeeID: KnockoutObservable<string> = ko.observable('');
            //休憩時間
            restTime: KnockoutObservableArray<common.OverTimeInput> = ko.observableArray([]);
            //残業時間
            overtimeHours: KnockoutObservableArray<common.OvertimeCaculation> = ko.observableArray([]);
            //休出時間
            breakTimes: KnockoutObservableArray<common.OvertimeCaculation> = ko.observableArray([]);
            //加給時間
            bonusTimes: KnockoutObservableArray<common.OvertimeCaculation> = ko.observableArray([]);
            //menu-bar 
            enableSendMail: KnockoutObservable<boolean> = ko.observable(true);
            prePostDisp: KnockoutObservable<boolean> = ko.observable(true);
            prePostEnable: KnockoutObservable<boolean> = ko.observable(true);
            useMulti: KnockoutObservable<boolean> = ko.observable(true);
    
            displayBonusTime: KnockoutObservable<boolean> = ko.observable(false);
            displayCaculationTime: KnockoutObservable<boolean> = ko.observable(true);
            displayPrePostFlg: KnockoutObservable<boolean> = ko.observable(true);
            displayRestTime: KnockoutObservable<boolean> = ko.observable(false);
            restTimeDisFlg: KnockoutObservable<boolean> = ko.observable(false); // RequestAppDetailSetting 
            typicalReasonDisplayFlg: KnockoutObservable<boolean> = ko.observable(false);
            displayAppReasonContentFlg: KnockoutObservable<boolean> = ko.observable(false);
            displayDivergenceReasonForm: KnockoutObservable<boolean> = ko.observable(false);
            displayDivergenceReasonInput: KnockoutObservable<boolean> = ko.observable(false);
    
            // 参照
            referencePanelFlg: KnockoutObservable<boolean> = ko.observable(false);
            preAppPanelFlg: KnockoutObservable<boolean> = ko.observable(false);
            
            instructInforFlag: KnockoutObservable <boolean> = ko.observable(true);
            instructInfor : KnockoutObservable <string> = ko.observable('');
    
            overtimeWork: KnockoutObservableArray<common.overtimeWork> = ko.observableArray([]);
            indicationOvertimeFlg: KnockoutObservable<boolean> = ko.observable(true);
            
    
            // preAppOvertime
            appDatePre: KnockoutObservable<string> = ko.observable(moment().format('YYYY/MM/DD'));
            workTypeCodePre:  KnockoutObservable<string> = ko.observable("");
            workTypeNamePre:  KnockoutObservable<string> = ko.observable("");
            siftCodePre:  KnockoutObservable<string> = ko.observable("");
            siftNamePre:  KnockoutObservable<string> = ko.observable("");
            //TIME LINE 1
            workClockFrom1Pre: KnockoutObservable<number> = ko.observable(null);
            workClockTo1Pre: KnockoutObservable<number> = ko.observable(null);
            //TIME LINE 2
            workClockFrom2Pre: KnockoutObservable<number> = ko.observable(null);
            workClockTo2Pre: KnockoutObservable<number> = ko.observable(null);
            overtimeHoursPre: KnockoutObservableArray<common.OverTimeInput> = ko.observableArray([]);
            overTimeShiftNightPre: KnockoutObservable<number> = ko.observable(null);
            flexExessTimePre: KnockoutObservable<number> = ko.observable(null);
            //　初期起動時、計算フラグ=1とする。
            //calculateFlag: KnockoutObservable<number> = ko.observable(1);
            //TODO: test-setting calculateFlag = 0
            calculateFlag: KnockoutObservable<number> = ko.observable(1);
            version: number = 0;
            constructor(listAppMetadata: Array<model.ApplicationMetadata>, currentApp: model.ApplicationMetadata) {
                super(listAppMetadata, currentApp);
                var self = this;
                $("#fixed-overtime-hour-table").ntsFixedTable({ height: 216 });
                $("#fixed-break_time-table").ntsFixedTable({ height: 120 });
                $("#fixed-bonus_time-table").ntsFixedTable({ height: 120 });
                $("#fixed-table-indicate").ntsFixedTable({ height: 120 });
                $("#fixed-table").ntsFixedTable({ height: 120 });
                self.startPage(self.appID());
            }
            
            startPage(appID: string): JQueryPromise<any> {
                nts.uk.ui.block.invisible();
                var self = this;
                var dfd = $.Deferred();
                service.findByAppID(appID).done((data) => { 
                    self.initData(data);
                    dfd.resolve(); 
                })
                .fail(function(res) {
                    if(res.messageId == 'Msg_426'){
                        dialog.alertError(res.message).then(function(){
                            nts.uk.ui.block.clear();
                        });
                    }else{ 
                        nts.uk.ui.dialog.alertError(res.message).then(function(){
                            nts.uk.request.jump("com", "/view/ccg/008/a/index.xhtml");
                            nts.uk.ui.block.clear();
                        });
                    }
                    dfd.reject(res);  
                });
                return dfd.promise();
            }
            
            initData(data: any) {
                var self = this;
                self.version = data.version;
                self.manualSendMailAtr(data.manualSendMailAtr);
                self.prePostSelected(data.application.prePostAtr);
                self.typicalReasonDisplayFlg(data.typicalReasonDisplayFlg);
                self.displayAppReasonContentFlg(data.displayAppReasonContentFlg);
                self.displayDivergenceReasonForm(data.displayDivergenceReasonForm);
                self.displayDivergenceReasonInput(data.displayDivergenceReasonInput);
                self.displayBonusTime(data.displayBonusTime);
                self.restTimeDisFlg(data.displayRestTime);
                self.employeeName(data.employeeName);
                self.employeeID(data.employeeID);
                if (data.siftType != null) {
                    self.siftCD(data.siftType.siftCode);
                    self.siftName(data.siftType.siftName);
                }
                if (data.workType != null) {
                    self.workTypeCd(data.workType.workTypeCode);
                    self.workTypeName(data.workType.workTypeName);
                }
                
                self.workTypecodes(data.workTypes);
                self.workTimecodes(data.siftTypes);
                
                self.timeStart1(data.workClockFrom1 == -1 ? null : data.workClockFrom1);
                self.timeEnd1(data.workClockTo1 == -1 ? null : data.workClockTo1);
                self.timeStart2(data.workClockFrom2 == -1 ? null : data.workClockFrom2);
                self.timeEnd2(data.workClockTo2 == -1 ? null : data.workClockTo2);
                if(data.applicationReasonDtos != null){
                    let reasonID = _.find(data.applicationReasonDtos, o => { return o.defaultFlg == 1 }).reasonID;
                    self.selectedReason(reasonID);
                    
                    let lstReasonCombo = _.map(data.applicationReasonDtos, o => { return new common.ComboReason(o.reasonID, o.reasonTemp); });
                    self.reasonCombo(lstReasonCombo);
                    
                    self.multilContent(data.application.applicationReason);
                }
                
                if(data.divergenceReasonDtos != null){
                    self.reasonCombo2(_.map(data.divergenceReasonDtos, o => { return new common.ComboReason(o.divergenceReasonID, o.reasonTemp); }));
                    let reasonID = _.find(data.divergenceReasonDtos, o => { return o.divergenceReasonIdDefault == 1 }).divergenceReasonID;
                    self.selectedReason2(reasonID);
                    self.multilContent2(data.divergenceReasonContent);
                }
                
                self.instructInforFlag(data.displayOvertimeInstructInforFlg);
                self.instructInfor(data.overtimeInstructInformation);
                self.referencePanelFlg(data.referencePanelFlg);
                self.preAppPanelFlg(data.preAppPanelFlg);
                // preAppOvertime
                if(data.preAppOvertimeDto != null){
                    self.appDatePre(data.preAppOvertimeDto.appDatePre);
                    if(data.preAppOvertimeDto.workTypePre != null){
                        self.workTypeCodePre(data.preAppOvertimeDto.workTypePre.workTypeCode);
                        self.workTypeNamePre(data.preAppOvertimeDto.workTypePre.workTypeName);
                    }
                    if(data.siftTypePre != null){
                        self.siftCodePre(data.preAppOvertimeDto.siftTypePre.siftCode);
                        self.siftNamePre(data.preAppOvertimeDto.siftTypePre.siftName);
                    }
                    self.workClockFrom1Pre(data.preAppOvertimeDto.workClockFrom1Pre);
                    self.workClockTo1Pre(data.preAppOvertimeDto.workClockTo1Pre);
                    self.workClockFrom2Pre(data.preAppOvertimeDto.workClockFrom2Pre);
                    self.workClockTo2Pre(data.preAppOvertimeDto.workClockTo2Pre);
                    if(data.preAppOvertimeDto.overTimeInputsPre != null){
                        for (let i = 0; i < data.preAppOvertimeDto.overTimeInputsPre.length; i++) {
                            self.overtimeHoursPre.push(new common.OverTimeInput("", "", data.preAppOvertimeDto.overTimeInputsPre[i].attendanceID, "", data.preAppOvertimeDto.overTimeInputsPre[i].frameNo,0, data.preAppOvertimeDto.overTimeInputsPre[i].frameName, data.preAppOvertimeDto.overTimeInputsPre[i].startTime, data.preAppOvertimeDto.overTimeInputsPre[i].endTime,data.preAppOvertimeDto.overTimeInputsPre[i].applicationTime,null));
                        }
                    }
                }
                
                let dataRestTime = _.filter(data.overTimeInputs, {'attendanceID': 0});
                let dataOverTime = _.filter(data.overTimeInputs, {'attendanceID': 1});
                let dataBreakTime = _.filter(data.overTimeInputs, {'attendanceID': 2});
                let dataBonusTime = _.filter(data.overTimeInputs, {'attendanceID': 3});
                self.restTime.removeAll();
                self.overtimeHours.removeAll();
                self.breakTimes.removeAll();
                self.bonusTimes.removeAll();
                if(nts.uk.util.isNullOrEmpty(dataRestTime)){
                    for (let i = 0; i < 11; i++) {
                        self.restTime.push(new common.OverTimeInput("", "", 0, "", i,0, i.toString(), null, null, null,""));
                    }    
                } else {
                    _.forEach(dataRestTime, (item) => { 
                        
                            self.restTime.push(new common.OverTimeInput(
                                item.companyID, 
                                item.appID, 
                                item.attendanceID, 
                                "", 
                                item.frameNo, 
                                item.timeItemTypeAtr, 
                                item.frameName, 
                                item.startTime, 
                                item.endTime, 
                                item.applicationTime, ""));
                    });
                };
                _.forEach(dataOverTime, (item) => { 
                    if(item.frameNo == 11){
                                self.overtimeHours.push(new common.OvertimeCaculation(
                                    item.companyID, 
                                    item.appID, 
                                    item.attendanceID, 
                                    "", 
                                    item.frameNo, 
                                    item.timeItemTypeAtr, 
                                    nts.uk.resource.getText("KAF005_63"), 
                                    item.applicationTime, 
                                    null, 
                                    null,"#[KAF005_64]"));
                            }else if(item.frameNo == 12){
                                self.overtimeHours.push(new common.OvertimeCaculation(
                                    item.companyID, 
                                    item.appID, 
                                    item.attendanceID, 
                                    "", 
                                    item.frameNo, 
                                    item.timeItemTypeAtr, 
                                    nts.uk.resource.getText("KAF005_65"), 
                                    item.applicationTime, 
                                    null, 
                                    null,"#[KAF005_66]"));
                            }else{
                                self.overtimeHours.push(new common.OvertimeCaculation(
                                    item.companyID, 
                                    item.appID, 
                                    item.attendanceID, 
                                    "", 
                                    item.frameNo, 
                                    item.timeItemTypeAtr, 
                                    item.frameName, 
                                    item.applicationTime, 
                                    null, 
                                    null, "#[KAF005_55]"));
                        }
                }); 
                _.forEach(dataBreakTime, (item) => { 
                    self.breakTimes.push(new common.OvertimeCaculation(
                        item.companyID, 
                        item.appID, 
                        item.attendanceID, 
                        "", 
                        item.frameNo, 
                        item.timeItemTypeAtr, 
                        item.frameName, 
                        item.applicationTime, 
                        null, 
                        null, ""));
                }); 
                _.forEach(dataBonusTime, (item) => { 
                    self.bonusTimes.push(new common.OvertimeCaculation(
                        item.companyID, 
                        item.appID, 
                        item.attendanceID, 
                        "", 
                        item.frameNo, 
                        item.timeItemTypeAtr, 
                        item.frameName, 
                        item.applicationTime, 
                        null, 
                        null, ""));
                }); 
            }
            
            update(): JQueryPromise<any> {
                nts.uk.ui.block.invisible();
                let self = this,
                appReason: string,
                divergenceReason: string;
                appReason = self.getReason(
                    self.typicalReasonDisplayFlg(),
                    self.selectedReason(),
                    self.reasonCombo(),
                    self.displayAppReasonContentFlg(),
                    self.multilContent()
                );
                let appReasonError = !appcommon.CommonProcess.checkAppReason(true, self.typicalReasonDisplayFlg(), self.displayAppReasonContentFlg(), appReason);
                if(appReasonError){
                    nts.uk.ui.dialog.alertError({ messageId: 'Msg_115' }).then(function(){nts.uk.ui.block.clear();});    
                    return;    
                }
                divergenceReason = self.getReason(
                    self.displayDivergenceReasonForm(),
                    self.selectedReason2(),
                    self.reasonCombo2(),
                    self.displayDivergenceReasonInput(),
                    self.multilContent2()
                );
                let command = {
                    version: self.version,
                    appID: self.appID(),
                    applicationDate: new Date(self.appDate()),
                    prePostAtr: self.prePostSelected(),
                    applicantSID: self.employeeID,
                    applicationReason: appReason,
                    appApprovalPhaseCmds: self.approvalList,
                    workTypeCode: self.workTypeCd(),
                    siftTypeCode: self.siftCD(),
                    workClockFrom1: self.timeStart1(),
                    workClockTo1: self.timeEnd1(),
                    workClockFrom2: self.timeStart2(),
                    workClockTo2: self.timeEnd2(),
                    breakTimes: ko.mapping.toJS(_.map(self.breakTimes(), item => self.convertOvertimeCaculationToOverTimeInput(item))),
                    overtimeHours: ko.mapping.toJS(_.map(self.overtimeHours(), item => self.convertOvertimeCaculationToOverTimeInput(item))),
                    restTime: ko.mapping.toJS(self.restTime()),
                    bonusTimes: ko.mapping.toJS(_.map(self.bonusTimes(), item => self.convertOvertimeCaculationToOverTimeInput(item))),
                    overTimeShiftNight: 100,
                    flexExessTime: 100,
                    overtimeAtr: 0,
                    divergenceReasonContent: divergenceReason,
                    sendMail: self.manualSendMailAtr(),
                    calculateFlag: self.calculateFlag()
                }
                
                service.checkBeforeRegister(command).done((data) => {                
                    if (data.errorCode == 0) {
                        if (data.confirm) {
                            //メッセージNO：829
                            dialog.confirm({ messageId: "Msg_829" }).ifYes(() => {
                                //登録処理を実行
                                service.updateOvertime(command)
                                .done(() => {
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
                            }).ifNo(() => {
                                //終了状態：処理をキャンセル
                                return;
                            });
                        } else {
                            //登録処理を実行
                            service.updateOvertime(command)
                            .done(() => {
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
                    } else if (data.errorCode == 1){
                        if(data.frameNo == -1){
                            //Setting color for item error
                            for (let i = 0; i < self.overtimeHours().length; i++) {
                                self.changeColor( self.overtimeHours()[i].attendanceID(), self.overtimeHours()[i].frameNo());
                            }
                        }else{
                          //Change background color
                            self.changeColor( data.attendanceId, data.frameNo);
                        }
                        
                    }
                }).fail((res) => {
                    dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds })
                    .then(function() { nts.uk.ui.block.clear(); });
                });
            }
            
            convertOvertimeCaculationToOverTimeInput(param: common.OverTimeInput): common.OverTimeInput{
                return new common.OverTimeInput(
                    param.companyID(),
                    param.appID(),
                    param.attendanceID(),
                    param.attendanceName(),
                    param.frameNo(),
                    param.timeItemTypeAtr(),
                    param.frameName(),
                    null,
                    null,
                    param.applicationTime(),
                    param.nameID()    
                );        
            } 
            
            /**
             * KDL003
             */
            openDialogKdl003() {
                let self = this;
                nts.uk.ui.windows.setShared('parentCodes', {
                   workTypeCodes: self.workTypecodes(),
                selectedWorkTypeCode: self.workTypeCd(),
                workTimeCodes: self.workTimecodes(),
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
                        service.getRecordWork(
                            {
                                employeeID: self.employeeID(), 
                                appDate: moment(self.appDate()).format("YYYY/MM/DD"),
                                siftCD: self.siftCD()
                            }
                        ).done(data => {
                            self.timeStart1(data.startTime1 == -1 ? null : data.startTime1);
                            self.timeEnd1(data.endTime1 == -1 ? null : data.endTime1);
                            self.timeStart2(data.startTime2 == -1 ? null : data.startTime2);
                            self.timeEnd2(data.endTime2 == -1 ? null : data.endTime2);   
                        });
                    }
                })
            }
            /**
             * Jump to CMM018 Screen
             */
            openCMM018() {
                let self = this;
                nts.uk.request.jump("com", "/view/cmm/018/a/index.xhtml", { screen: 'Application', employeeId: self.employeeID });
            }
            
            CaculationTime(){
                let self = this;
                let dfd = $.Deferred();
                //TODO: for test
                self.calculateFlag(0);
                let param : any ={
                    overtimeHours: ko.toJS(self.overtimeHours()),
                    bonusTimes: ko.toJS(self.bonusTimes()),
                    prePostAtr : self.prePostSelected(),
                    appDate : moment(self.appDate()).format("YYYY/MM/DD")
                }
                
                service.getCaculationResult(param).done(function(data){
                   self.overtimeHours.removeAll();
                   self.bonusTimes.removeAll();
                    if(data != null){
                     for(let i =0; i < data.length; i++){
                       if(data[i].attendanceID == 1){
                            
                           if(data[i].frameNo != 11 && data[i].frameNo != 12){
                               self.overtimeHours.push(new common.OvertimeCaculation("", "",
                                data[i].attendanceID,
                                 "", 
                                 data[i].frameNo,
                                 0, 
                                 data[i].frameName,
                                  data[i].applicationTime,
                                  self.convertIntToTime(data[i].preAppTime),
                                  self.convertIntToTime(data[i].caculationTime),"#[KAF005_55]"));
                           }else if(data[i].frameNo == 11){
                                self.overtimeHours.push(new common.OvertimeCaculation("", "",
                                data[i].attendanceID,
                                 "", 
                                 data[i].frameNo,
                                 0, 
                                 nts.uk.resource.getText("KAF005_63"),
                                 data[i].applicationTime,
                                 self.convertIntToTime(data[i].preAppTime),
                                 self.convertIntToTime(data[i].caculationTime),"#[KAF005_64]"));
                           }else if(data[i].frameNo == 12){
                                self.overtimeHours.push(new common.OvertimeCaculation("", "",
                                data[i].attendanceID,
                                 "", 
                                 data[i].frameNo,
                                 0, 
                                 nts.uk.resource.getText("KAF005_65"),
                                 data[i].applicationTime,
                                 self.convertIntToTime(data[i].preAppTime),
                                 self.convertIntToTime(data[i].caculationTime),"#[KAF005_66]"));
                           }
                           
                       }else if(data[i].attendanceID == 3){
                           self.bonusTimes.push(new common.OvertimeCaculation("", "", data[i].attendanceID,
                            "", data[i].frameNo,
                            data[i].timeItemTypeAtr ,
                            data[i].frameName, data[i].applicationTime,
                           self.convertIntToTime(data[i].preAppTime), null,""));
                       }   
                     }   
                    }
                     dfd.resolve(data);
                }).fail(function(res){
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            
            convertIntToTime(data : number) : string{
                let hourMinute : string = "";
                if(data == -1 || data == ""){
                    return null;
                }else if (data == 0) {
                    hourMinute = "00:00";
                }else if(data != null){
                    let hour = Math.floor(data/60);
                    let minutes = Math.floor(data%60);
                    hourMinute = (hour < 10 ? ("0" + hour) : hour ) + ":"+ (minutes < 10 ? ("0" + minutes) : minutes);
                }
                return hourMinute;
            }
            
            changeColor(attendanceId, frameNo){
                /*//休憩時間
                if(attendanceId == 0){
                    $('td#restTime_'+attendanceId+'_'+frameNo).css('background', 'pink')
                }*/
                // 残業時間
                if(attendanceId == 1){
                    $('td#overtimeHoursCheck_'+attendanceId+'_'+frameNo).css('background', '#FD4D4D')
                    $('input#overtimeHoursCheck_'+attendanceId+'_'+frameNo).css('background', '#FD4D4D')
                }
                /*// 休出時間
                if(attendanceId == 2){
                    $('td#breakTimesCheck_'+attendanceId+'_'+frameNo).css('background', 'pink')
                }
                //加給時間
                if(attendanceId == 3){
                    $('td#breakTimesCheck_'+attendanceId+'_'+frameNo).css('background', 'pink')
                }*/
            }
            
            getReason(inputReasonDisp: boolean, inputReasonID: string, inputReasonList: Array<common.ComboReason>, detailReasonDisp: boolean, detailReason: string): string{
                let appReason = '';
                let inputReason: string = '';
                if(!nts.uk.util.isNullOrEmpty(inputReasonID)){
                    inputReason = _.find(inputReasonList, o => { return o.reasonId == inputReasonID; }).reasonName;    
                }    
                if(inputReasonDisp==true&&detailReasonDisp==true){
                    if(!nts.uk.util.isNullOrEmpty(inputReason)&&!nts.uk.util.isNullOrEmpty(detailReason)){
                        appReason = inputReason + ":" + detailReason;
                    } else if(!nts.uk.util.isNullOrEmpty(inputReason)&&nts.uk.util.isNullOrEmpty(detailReason)){
                        appReason = inputReason; 
                    } else if(nts.uk.util.isNullOrEmpty(inputReason)&&!nts.uk.util.isNullOrEmpty(detailReason)){
                        appReason = detailReason;             
                    }                
                } else if(inputReasonDisp==true&&detailReasonDisp==false){
                    appReason = inputReason;                 
                } else if(inputReasonDisp==false&&detailReasonDisp==true){
                    appReason = detailReason;     
                } 
                return appReason;
            }
        }
    }
}