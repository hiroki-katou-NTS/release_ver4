module nts.uk.at.view.kaf002.cm {
    export module viewmodel {
        import vmbase = nts.uk.at.view.kaf002.shr.vmbase;
        import service = nts.uk.at.view.kaf002.shr.service;
        import kaf002 = nts.uk.at.view.kaf002;
        import appcommon = nts.uk.at.view.kaf000.shr.model;
        export class ScreenModel {
            m1: kaf002.m1.viewmodel.ScreenModel;
            m2: kaf002.m2.viewmodel.ScreenModel;
            m3: kaf002.m3.viewmodel.ScreenModel;
            m4: kaf002.m4.viewmodel.ScreenModel;
            m5: kaf002.m5.viewmodel.ScreenModel;
            stampRequestMode: KnockoutObservable<number> = ko.observable(0);
            screenMode: KnockoutObservable<number> = ko.observable(0);
            resultDisplay: KnockoutObservable<number> = ko.observable(0);
            application: KnockoutObservable<vmbase.Application> = ko.observable(new vmbase.Application('',moment(new Date()).format("YYYY/MM/DD"),'','','','','',0));
            inputReasons: KnockoutObservableArray<vmbase.InputReason> = ko.observableArray([new vmbase.InputReason('','')]);
            currentReason: KnockoutObservable<string> = ko.observable('');
            currentReasonText: KnockoutObservable<string> = ko.observable('');
            inputReasonsDisp: KnockoutObservable<number> = ko.observable(0);
            detailReasonDisp: KnockoutObservable<number> = ko.observable(0);
            topComment: KnockoutObservable<vmbase.CommentUI> = ko.observable(new vmbase.CommentUI('','',false)); 
            botComment: KnockoutObservable<vmbase.CommentUI> = ko.observable(new vmbase.CommentUI('','',false));
            employeeName: KnockoutObservable<string> = ko.observable("");
            empEditable: KnockoutObservable<boolean> = ko.observable(true);
            textC3_2: KnockoutObservable<string> = ko.observable("");
            textC4_2: KnockoutObservable<string> = ko.observable("");
            editable: KnockoutObservable<boolean> = ko.observable(true);
            employeeID: string;
            appDate: any;
            constructor(stampRequestMode: number, screenMode: number){
                var self = this;
                self.stampRequestMode(stampRequestMode);
                self.screenMode(screenMode);
                switch(self.stampRequestMode()){
                    case 0: self.m1 = new kaf002.m1.viewmodel.ScreenModel(); break;
                    case 1: self.m2 = new kaf002.m2.viewmodel.ScreenModel(); break;
                    case 2: self.m3 = new kaf002.m3.viewmodel.ScreenModel(); break;
                    case 3: self.m4 = new kaf002.m4.viewmodel.ScreenModel(); break;
                    case 4: self.m5 = new kaf002.m5.viewmodel.ScreenModel(); break;
                    default: break;
                }    
            }
            start(commonSet: vmbase.AppStampNewSetDto, appStampData: any, editable: any, employeeID: string, appDate: any){
                var self = this;
                self.appDate = appDate;
                self.employeeID = employeeID;
                self.editable(editable);
                self.inputReasonsDisp(commonSet.appCommonSettingDto.appTypeDiscreteSettingDtos[0].typicalReasonDisplayFlg);
                self.detailReasonDisp(commonSet.appCommonSettingDto.appTypeDiscreteSettingDtos[0].displayReasonFlg);
                self.resultDisplay(commonSet.appStampSetDto.stampRequestSettingDto.resultDisp);
                self.inputReasons.removeAll();
                _.forEach(commonSet.appStampSetDto.applicationReasonDtos, o => {
                    self.inputReasons.push(new vmbase.InputReason(o.reasonID, o.reasonTemp)); 
                    if(o.defaultFlg == 1){
                        self.currentReason(o.reasonID);
                    }          
                });
                self.topComment().text(commonSet.appStampSetDto.stampRequestSettingDto.topComment);
                self.topComment().color(commonSet.appStampSetDto.stampRequestSettingDto.topCommentFontColor);
                self.topComment().fontWeight(commonSet.appStampSetDto.stampRequestSettingDto.topCommentFontWeight);
                self.botComment().text(commonSet.appStampSetDto.stampRequestSettingDto.bottomComment);
                self.botComment().color(commonSet.appStampSetDto.stampRequestSettingDto.bottomCommentFontColor);
                self.botComment().fontWeight(commonSet.appStampSetDto.stampRequestSettingDto.bottomCommentFontWeight);
                service.findAllWorkLocation().done((listWorkLocation: Array<vmbase.IWorkLocation>)=>{
                    $('.cm-memo').focus();
                    switch(self.stampRequestMode()){
                        case 0: 
                            self.m1.start(appStampData.appStampGoOutPermitCmds, commonSet, listWorkLocation, self.editable(), self.screenMode(), self.employeeID, self.appDate);
                            break;    
                        case 1: 
                            self.m2.start(appStampData.appStampWorkCmds, commonSet, listWorkLocation, self.editable(), self.screenMode(), self.employeeID, self.appDate);
                            break;  
                        case 2: 
                            self.m3.start(appStampData, commonSet, listWorkLocation, self.editable(), self.screenMode(), self.employeeID, self.appDate);
                            break; 
                        case 3: 
                            self.m4.start(appStampData.appStampOnlineRecordCmd, commonSet.appStampSetDto.stampRequestSettingDto, listWorkLocation, self.editable(), self.screenMode(), self.appDate);
                            break; 
                        case 4: 
                            self.m5.start(appStampData.appStampWorkCmds, commonSet, listWorkLocation, self.editable(), self.screenMode(), self.employeeID, self.appDate);
                            break; 
                        default: 
                            break;
                    }     
                });
                if(self.screenMode()==0){//detail screen
                    if(!nts.uk.util.isNullOrUndefined(appStampData)) {
                        self.application(new vmbase.Application(
                            appStampData.appID,
                            appStampData.inputDate,
                            appStampData.enteredPerson,
                            appStampData.applicationDate,
                            appStampData.titleReason,
                            appStampData.detailReason,
                            appStampData.employeeID,
                            appStampData.version
                        ));
                        self.employeeID = appStampData.employeeID;
                    }
                    self.stampRequestMode(appStampData.stampRequestMode);
                    self.employeeName(appStampData.employeeName);
                    self.textC3_2(appStampData.employeeName);
                    if(appStampData.employeeID == appStampData.inputEmpID){
                        self.textC3_2(appStampData.employeeName);        
                    } else {
                        self.textC3_2(nts.uk.resource.getText("KAF002_38",[appStampData.employeeName,appStampData.inputEmpName]));    
                    }
                    self.textC4_2(nts.uk.resource.getText("KAF002_39",[appStampData.appDate,appStampData.inputDate]));
                } else {//new screen
                    self.application().appDate(appDate);    
                    self.employeeName(commonSet.employeeName);
                }
            }
            
            register(errorFlag: any, errorMsg: any, checkBoxValue: boolean){
                var self = this;
                if (nts.uk.ui.errors.hasError()){return;}
                if(errorFlag!=0){
                    nts.uk.ui.dialog.alertError({ messageId: errorMsg }).then(function(){nts.uk.ui.block.clear();});    
                } else {
                    if(!nts.uk.text.isNullOrEmpty(self.currentReason())){
                        var reasonText = _.find(self.inputReasons(),function(data){return data.id == self.currentReason()});
                        self.application().titleReason(reasonText.content);    
                    }else{
                        self.application().titleReason("");
                    }
                    if(!appcommon.CommonProcess.checklenghtReason(!nts.uk.text.isNullOrEmpty(self.application().titleReason()) ? self.application().titleReason() + "\n" + self.application().contentReason() : self.application().contentReason(),"#appReason")){
                        return;
                    }
                    switch(self.stampRequestMode()){
                        case 0: self.m1.register(self.application(), checkBoxValue);break;    
                        case 1: self.m2.register(self.application(), checkBoxValue);break;  
                        case 2: self.m3.register(self.application(), checkBoxValue);break; 
                        case 3: self.m4.register(self.application(), checkBoxValue);break; 
                        case 4: self.m5.register(self.application(), checkBoxValue);break; 
                        default: break;
                    }    
                }
            }
            
            update(approvalList: Array<vmbase.AppApprovalPhase>){
                var self = this;
                self.application().version = nts.uk.ui._viewModel.content.version;
                if(!nts.uk.text.isNullOrEmpty(self.currentReason())){
                    var reasonText = _.find(self.inputReasons(),function(data){return data.id == self.currentReason()});
                    self.application().titleReason(reasonText.content);    
                }else{
                    self.application().titleReason("");
                }
                 if(!appcommon.CommonProcess.checklenghtReason(!nts.uk.text.isNullOrEmpty(self.application().titleReason()) ? self.application().titleReason() + "\n" + self.application().contentReason() : self.application().contentReason(),"#appReason")){
                        return;
                    }          
                switch(self.stampRequestMode()){
                    case 0: self.m1.update(self.application());break;    
                    case 1: self.m2.update(self.application());break;  
                    case 2: self.m3.update(self.application());break; 
                    case 3: self.m4.update(self.application());break; 
                    case 4: self.m5.update(self.application());break;  
                    default: break;
                }    
            }
            
            getBoxReason(){
                var self = this;
                return appcommon.CommonProcess.getComboBoxReason(self.currentReason(), self.inputReasons(), self.inputReasonsDisp() != 0);
            }
        
            getAreaReason(){
                var self = this;
                return appcommon.CommonProcess.getTextAreaReason(self.application().contentReason(), self.detailReasonDisp() != 0, true);   
            }
            
            resfreshReason(appReason: string){
                var self = this;
                self.currentReason(''); 
                self.application().contentReason(appReason);   
            }
            
            getAttendanceItem(date: any, employeeList: Array<any>){
                var self = this;
                switch(self.stampRequestMode()){
                    case 0: self.m1.getAttendanceItem(date, employeeList);break;   
                    case 1: self.m2.getAttendanceItem(date, employeeList);break; 
                    case 2: self.m3.getAttendanceItem(date, employeeList);break;
                    case 4: self.m5.getAttendanceItem(date, employeeList);break;
                    default: break;
                }             
            }
        }
    }
}