module nts.uk.at.view.kaf009.b {
    import common = nts.uk.at.view.kaf009.share.common;
    import model = nts.uk.at.view.kaf000.b.viewmodel.model;
    export module viewmodel{
        export class ScreenModel extends kaf000.b.viewmodel.ScreenModel {
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
            appDate: KnockoutObservable<string> = ko.observable(moment().format('YYYY/MM/DD'));;
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
            //MultilineEditor
            requiredReason : KnockoutObservable<boolean> = ko.observable(false);
            multilContent: KnockoutObservable<string> = ko.observable('');
            multiOption: any;
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
            //self.approvalSource = self.kaf000_a.approvalList;

            constructor(listAppMetadata: Array<model.ApplicationMetadata>, currentApp: model.ApplicationMetadata) {
                super(listAppMetadata, currentApp);
                let self = this;
                self.multiOption = ko.mapping.fromJS(new nts.uk.ui.option.MultilineEditorOption({
                    resizeable: false,
                    placeholder: "Placeholder for text editor",
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
                var self = this;
                let dfd = $.Deferred();
                //get Common Setting
                service.getGoBackSetting().done(function(settingData: any) {
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
                        //send Mail
                        self.enableSendMail(settingData.appCommonSettingDto.appTypeDiscreteSettingDtos[0].sendMailWhenRegisterFlg == 1 ? true: false); 
                        //pre Post Enable
                        self.prePostEnable(settingData.appCommonSettingDto.appTypeDiscreteSettingDtos[0].prePostCanChangeFlg == 1 ? true: false);   
                    }
                    //pre Post display
                    self.prePostDisp(settingData.appCommonSettingDto.applicationSettingDto.displayPrePostFlg == 1 ? true: false);
                    //共通設定.複数回勤務
                    self.useMulti(settingData.dutiesMulti);
                    //Get data 
                    service.getGoBackDirectDetail(appId).done(function(detailData: any) {
                        //get all Location 
                        self.getAllWorkLocation();
                        self.workTypeName(detailData.workTypeName);
                        self.siftName(detailData.workTimeName);
                        self.workLocationName(detailData.workLocationName1);
                        self.workLocationName2(detailData.workLocationName2);
                        self.prePostSelected(detailData.prePostAtr);
                        self.multilContent(detailData.appReason);
                        self.selectedReason(detailData.appReasonId);
                        self.appDate(detailData.appDate);
                        //Set Value of control
                        self.setValueControl(detailData.goBackDirectlyDto);
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
                let self = this;
                nts.uk.ui.dialog.confirm({ messageId: 'Msg_338' }).ifYes(function() {
                    service.updateGoBackDirect(self.getCommand(self.appID())).done(function() {
                        alert("Update Done");
                    }).fail(function(res) {
                        nts.uk.ui.dialog.alertError(res.message).then(function(){nts.uk.ui.block.clear();});
                    })
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
         * 1: insert 
         * 2: update 
         * 3: delete
         */
            getCommand(appId :string) {
//                let self = this;
//                let command: common.GoBackCommand = new common.GoBackCommand();
//                command.appID = appId;
//                command.appDate = self.appDate();
//                command.workTypeCD = self.workTypeCd();
//                command.siftCD = self.siftCD();
//                command.workChangeAtr = self.workChangeAtr() == true ? 1 : 0;
//                command.goWorkAtr1 = self.selectedGo();
//                command.backHomeAtr1 = self.selectedBack();
//                command.workTimeStart1 = self.timeStart1();
//                command.workTimeEnd1 = self.timeEnd1();
//                command.goWorkAtr2 = self.selectedGo2();
//                command.backHomeAtr2 = self.selectedBack2();
//                command.workTimeStart2 = self.timeStart2();
//                command.workTimeEnd2 = self.timeEnd2();
//                command.workLocationCD1 = self.workLocationCD();
//                command.workLocationCD2 = self.workLocationCD2();
//                command.appCommand = new common.ApplicationCommand(
//                    self.selectedReason(),
//                    self.prePostSelected(),
//                    self.appDate(),
//                    self.employeeName(),
//                    self.multilContent(),
//                    self.appDate(),
//                    self.multilContent(),
//                    self.employeeName(),
//                    self.appDate(),
//                    self.appDate(),
//                    self.appDate(),
//                    self.appDate());
//                
//                
//                
//                
//                return command;
                
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
            
            let appCommand : common.ApplicationCommand  = new common.ApplicationCommand(
                self.selectedReason(),
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
                appApprovalPhaseCmds : self.approvalSource
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
                    self.commentGo2(data.commentContent2);
                    self.commentBack1(data.commentContent1);
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
                if (!nts.uk.util.isNullOrUndefined(data)) {
                    //Line 1
                    self.timeStart1(data.workTimeStart1);
                    self.timeEnd1(data.workTimeEnd1);
                    self.selectedGo(data.goWorkAtr1);
                    self.selectedBack(data.backHomeAtr1);
                    self.workLocationCD(data.workLocationCD1);
                    //Line 2
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
            }

            /**
             * set reason 
             */
            setReasonControl(data: Array<common.ReasonDto>) {
                let self = this;
                let comboSource: Array<common.ComboReason> = [];
                comboSource.push(new common.ComboReason(0, '選択してください', ''));
                _.forEach(data, function(value: common.ReasonDto) {
                    comboSource.push(new common.ComboReason(value.displayOrder, value.reasonTemp, value.reasonID));
                });
                self.reasonCombo(_.orderBy(comboSource, 'reasonCode', 'asc'));
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
                    let self = this;
                    let returnWorkLocationCD = nts.uk.ui.windows.getShared("KDL010workLocation");
                    if (returnWorkLocationCD !== undefined) {
                        if (line == 1) {
                            self.workLocationCD(returnWorkLocationCD);
                            self.workLocationName(self.findWorkLocationName(returnWorkLocationCD));
                        } else {
                            self.workLocationCD2(returnWorkLocationCD);
                            self.workLocationName2(self.findWorkLocationName(returnWorkLocationCD));
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
                    }
                })
            }
        }
    }
}