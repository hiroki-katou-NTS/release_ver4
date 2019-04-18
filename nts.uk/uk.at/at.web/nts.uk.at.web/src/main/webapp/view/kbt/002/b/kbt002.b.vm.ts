module nts.uk.at.view.kbt002.b {
    export module viewmodel {
        import alert = nts.uk.ui.dialog.alert;
        import modal = nts.uk.ui.windows.sub.modal;
        import setShared = nts.uk.ui.windows.setShared;
        import getShared = nts.uk.ui.windows.getShared;
        import block = nts.uk.ui.block;
        import dialog = nts.uk.ui.dialog;
        import getText = nts.uk.resource.getText;
        
        export class ScreenModel {
            execItemList: KnockoutObservableArray<any> = ko.observableArray([]);
            workplaceList: KnockoutObservableArray<any> = ko.observableArray([]);
            selectedExecCd: KnockoutObservable<string> = ko.observable('');
            currentExecItem: KnockoutObservable<ExecutionItem> = ko.observable(new ExecutionItem(null));

            // Screen mode
            isNewMode: KnockoutObservable<boolean> = ko.observable(false);

            // Display text
            wkpListText: KnockoutObservable<string> = ko.observable('');
            targetDateText: KnockoutObservable<string> = ko.observable('');

            // Enumeration list
            targetMonthList: KnockoutObservableArray<EnumConstantDto> = ko.observableArray([]);
            
            targetDailyPerfItemList: KnockoutObservableArray<EnumConstantDto> = ko.observableArray([]);
            repeatContentItemList: KnockoutObservableArray<EnumConstantDto> = ko.observableArray([]);
            monthDaysList: KnockoutObservableArray<EnumConstantDto> = ko.observableArray([]);
            listDesignatedYear: KnockoutObservableArray<EnumConstantDto> = ko.observableArray([]);

            settingEnum: ExecItemEnumDto;
            
            //list GetAlarmByUser
            listGetAlarmByUser :  KnockoutObservableArray<any> = ko.observableArray([]);

            //targetGroupClass: KnockoutObservableArray<any>;
            constructor() {
                var self = this;
                /*
                  self.targetGroupClass = ko.observableArray([
                 new TargetGroupClass(0, getText('KBT002_162')),
                new TargetGroupClass(1, getText('KBT002_163'))
            ]);
                */
                self.selectedExecCd.subscribe(execItemCd => {
                    self.initProcExec();
                    if (nts.uk.text.isNullOrEmpty(execItemCd)) {
                        self.isNewMode(true);
                    } else {
                        // set update mode
                        
                        let data = _.filter(self.execItemList(), function(o) { return o.execItemCd == execItemCd; });
                        if (data[0]) {
                            self.currentExecItem().createData(data[0]);
                            //                            self.wkpListText(self.buildWorkplaceStr(_.sortBy(self.currentExecItem().workplaceList())));
                            self.buildWorkplaceStr(self.currentExecItem().workplaceList());
                            if (self.currentExecItem().perScheduleCls()) {
                                self.targetDateText(self.buildTargetDateStr(self.currentExecItem()));
                            }
                        }
                        self.isNewMode(false);
                    }
                    
                    setTimeout(function() { self.focusInput(); }, 100);
                    //                    self.currentExecItem().refDate(moment(new Date()).toDate());
                    nts.uk.ui.errors.clearAll();
                });
                
               
            }

            // Start page
            start() {
                let self = this;
                let dfd = $.Deferred<void>();
                let dfdGetEnumDataList = self.getEnumDataList();
                let dfdGetAlarmByUser= self.getAlarmByUser();
                $.when(dfdGetEnumDataList,dfdGetAlarmByUser).done((dfdGetEnumDataListData,dfdGetAlarmByUserData) => {
                    dfd.resolve();
                });
                return dfd.promise();
            }
            
            getEnumDataList(){
                let self = this;
                let dfd = $.Deferred<void>();
                service.getEnumDataList().done(function(setting) {
                    self.settingEnum = setting;
                    self.targetMonthList(setting.targetMonth);
                    self.targetDailyPerfItemList(setting.dailyPerfItems);
                    self.repeatContentItemList(setting.repeatContentItems);
                    self.monthDaysList(setting.monthDays);
                    self.listDesignatedYear(setting.designatedYear);
                    service.findWorkplaceTree(moment(new Date()).toDate(), 2).done(function(dataList: Array<WorkplaceSearchData>) {
                        //                        self.workplaceList(_.sortBy(self.convertTreeToArray(dataList), function(wkp) {
                        //                            return parseInt(wkp.hierarchyCode);
                        //                        }));
                        self.workplaceList(self.convertTreeToArray(dataList));
                        $.when(self.getProcExecList()).done(() => {
                            dfd.resolve();
                        });
                    }).fail(function(res) {
                        nts.uk.ui.dialog.alert({ messageId: "Msg_7" });
                    });
                });

                // set ntsFixedTable style
                return dfd.promise();
            }
            
            getAlarmByUser(){
                let self = this;
                let dfd = $.Deferred<void>();
                service.getAlarmByUser().done(function(data) {
                        self.listGetAlarmByUser(data);
                        dfd.resolve();
                    }).fail(function(res) {
                        dfd.reject();
                        nts.uk.ui.dialog.alertError(res).then(function() { nts.uk.ui.block.clear(); });
                    });
                return dfd.promise();
            }

            /**
             * Button on screen
             */
            // 新規 button
            createProcExec() {
                let self = this;
                nts.uk.ui.errors.clearAll();
                self.isNewMode(true);
                self.selectedExecCd('');
            }

            // 登録 button
            saveProcExec() {
                let self = this;
  
                // validate
                if (self.validate()) {
                    return;
                }
                if (nts.uk.ui.errors.hasError()) {
                    return;
                }
                if((self.currentExecItem().execScopeCls() == 1) && (self.currentExecItem().workplaceList().length == 0)) {
                    nts.uk.ui.dialog.alertError({ messageId: "Msg_1294" }); 
                } else {
                    // get JsObject
                    //                let command: any = ko.toJS(self.currentExecItem);
                    let command: any = self.toJsonObject();
                    //                command.refDate = command.refDate == '' ? null : command.refDate;
                    nts.uk.ui.block.grayout();
    
                    // insert or update process execution
                    service.saveProcExec(command).done(function(savedProcExecCd) {
                        nts.uk.ui.block.clear();
    
                        // notice success
                        nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => {
                            // Get process execution list
                            self.getProcExecList(savedProcExecCd);
                            setTimeout(function() { self.focusInput(); }, 100);
                        });
                    }).fail((res: any) => {
                        nts.uk.ui.block.clear();
                        self.showMessageError(res);
                    });
                } 
            }

            // 削除 button
            delProcExec() {
                let self = this,
                    currentItem = self.currentExecItem(),
                    oldIndex = _.findIndex(self.execItemList(), x => x.execItemCd == currentItem.execItemCd()),
                    lastIndex = self.execItemList().length - 1;

                // show message confirm
                nts.uk.ui.dialog.confirm({ messageId: 'Msg_18' }).ifYes(() => {
                    let command: any = {};
                    command.execItemCd = currentItem.execItemCd();

                    nts.uk.ui.block.grayout();
                    service.deleteProcExec(command).done(() => {
                        // Get process exection list
                        nts.uk.ui.dialog.info({ messageId: "Msg_16" }).then(function() {
                            $.when(self.getProcExecList()).done(() => {
                                if (self.execItemList().length > 0) {
                                    if (oldIndex == lastIndex) {
                                        oldIndex--;
                                    }
                                    self.selectedExecCd(self.execItemList()[oldIndex].execItemCd);
                                }
                            });
                        });
                    }).fail((res: any) => {
                        //                        nts.uk.ui.dialog.bundledErrors(res);
                        self.showMessageError(res);
                    })
                    nts.uk.ui.block.clear();
                });
            }

            // 実行タスク設定 button
            openDialogC() {
                let self = this;
                block.grayout();
                setShared('inputDialogC',
                    {
                        execItemCd: self.currentExecItem().execItemCd(),
                        execItemName: self.currentExecItem().execItemName(),
                        //                            repeatContent: self.repeatContentItemList(),
                        //                            monthDays: self.monthDaysList()
                    });
                modal("/view/kbt/002/c/index.xhtml").onClosed(function() {
                    block.clear();
                });
            }

            // 職場選択 button
            openDialogCDL008() {
                let self = this;
                block.grayout();
                let canSelected = self.currentExecItem().workplaceList() ? self.currentExecItem().workplaceList() : [];
                setShared('inputCDL008', { 
                baseDate: moment(new Date()).toDate(), 
                isMultiple: true, 
                selectedCodes: canSelected,
                selectedSystemType: 2,
                isrestrictionOfReferenceRange: true,
                showNoSelection:  false,
                isShowBaseDate:  false });
                modal("com", "/view/cdl/008/a/index.xhtml").onClosed(function() {
                    block.clear();
                    let data = getShared('outputCDL008');
                    if (data == null || data === undefined) {
                        return;
                    } else {
                        self.currentExecItem().workplaceList(data);
                        self.buildWorkplaceStr(data);
                    }
                });
            }

            /*
             * Common functions
             */
            private getProcExecList(savedExecItemCd?: string): JQueryPromise<void> {
                let self = this;
                let dfd = $.Deferred<void>();

                self.execItemList([]);
                service.getProcExecList().done(function(procExecList) {
                    if (procExecList && procExecList.length > 0) {
                        self.execItemList(procExecList);
                        if (nts.uk.text.isNullOrEmpty(savedExecItemCd)) {
                            self.selectedExecCd(procExecList[0].execItemCd);
                        } else {
                            self.selectedExecCd(savedExecItemCd);
                        }

                    } else {
                        self.createProcExec();
                    }
                    dfd.resolve();
                });
                return dfd.promise();
            }

            private initProcExec() {
                let self = this;
                nts.uk.ui.errors.clearAll();
                self.currentExecItem(new ExecutionItem(null));
                self.wkpListText('');
                self.targetDateText('');
                
            }

            private buildWorkplaceStr(wkpIdList: any) {
                let self = this;
                var wkpText = '';
                //                wkpList = _.sortBy(wkpList, 'hierarchyCode');
                if (wkpIdList) {
                    if (wkpIdList.length == 0) {
                        wkpText = '';
                    } else if (wkpIdList.length == 1) {
                        var wkpId = wkpIdList[0];
                        var wkp = _.find(self.workplaceList(), function(o) { return o.workplaceId == wkpId; });
                        if(_.isNil(wkp)){
                            wkpText = getText('KBT002_193');
                        }else{
                            wkpText = wkp.hierarchyCode + ' ' + wkp.name;
                        }
                    } else {
                        var workplaceList = [];
                        //                        var firstWkpId = wkpIdList[0];
                        //                        var lastWkpId = wkpIdList[wkpIdList.length - 1];
                        //                        var firstWkp = _.find(self.workplaceList(), function(o) { return o.workplaceId == firstWkpId; });
                        //                        var lastWkp = _.find(self.workplaceList(), function(o) { return o.workplaceId == lastWkpId; });
                        _.each(wkpIdList, wkpId => {
                            let workplace = _.find(self.workplaceList(), function(o) { return o.workplaceId == wkpId; });
                            if(!_.isNil(workplace)){
                                workplaceList.push(workplace);
                            }
                            
                        });
                        if(workplaceList.length >1){
                            workplaceList = _.sortBy(workplaceList, function(wkp) {
                                return parseInt(wkp.hierarchyCode);
                            });
                            let firstWkp = workplaceList[0];
                            let lastWkp = workplaceList[workplaceList.length - 1];    
                            wkpText = firstWkp.hierarchyCode + ' ' + firstWkp.name + ' ～ ' + lastWkp.hierarchyCode + ' ' + lastWkp.name;
                        }else if(workplaceList.length ==1){
                            wkpText = workplaceList[0].hierarchyCode + ' ' + workplaceList[0].name + ' ～ ' + getText('KBT002_193');
                        }else{
                            wkpText = getText('KBT002_193') + ' ～ ' + getText('KBT002_193');
                        }
                    }
                }
                self.wkpListText(wkpText);
                //                self.wkpListText(self.buildWorkplaceStr(_.sortBy(self.currentExecItem().workplaceList())));
                //                return '';
            }
            
            private buildTargetDateStr(execItem : any) {
                let self = this;
                var startTargetDate;
                var endTargetDate;
                let today = moment();
                var currentDate = moment([today.year(), today.month(), today.date()]);
                let targetDateStr = getText('KBT002_25') + today.format("YYYY/MM/DD") + getText('KBT002_26');

                // Calculate start target date
                if (execItem.targetMonth() == 0) {
                    // If target date < current date then set date by current date
                    startTargetDate = moment([today.year(), today.month(), execItem.targetDate()]);
                    /*
                    if (startTargetDate.diff(currentDate, 'days') < 0) {
                        startTargetDate = currentDate;
                    }
                    */
                } else if (execItem.targetMonth() == 1) {
                    startTargetDate = moment([today.year(), today.month(), execItem.targetDate()]).add(1, 'months');
                    
                } else if (execItem.targetMonth() == 2) {
                    startTargetDate = moment([today.year(), today.month(), execItem.targetDate()]).add(2, 'months');
                } else if (execItem.targetMonth() == 3) {
                    startTargetDate = moment([today.year(), today.month(), execItem.targetDate()]);
                }
                if(startTargetDate._isValid){
                    targetDateStr += startTargetDate.format("YYYY/MM/DD");
                }
                

                // Calculate end target date
                if (self.currentExecItem().targetDate() == 1) {
                    if(execItem.creationPeriod() == 1){
                        endTargetDate = startTargetDate.endOf('month');
                    }else{
                        endTargetDate = startTargetDate.add(execItem.creationPeriod()-1, 'months').endOf('month');
                    }
                } else {
                    endTargetDate = startTargetDate.add(execItem.creationPeriod(), 'months');
                }
                targetDateStr += '～';
                 if(startTargetDate._isValid){
                    targetDateStr += endTargetDate.format("YYYY/MM/DD");
                }
                 targetDateStr += 'です';
                
                return targetDateStr;
            }

            private validate(): boolean {
                let self = this;

                // clear error
                self.clearError();
                // validate
                //                $(".nts-input ").ntsEditor('validate');
                $("#execItemCd").ntsEditor('validate');
                $("#execItemName").ntsEditor('validate');
                $(".ntsDatepicker").ntsEditor('validate');
                if (self.currentExecItem().targetMonth() != 3 && self.currentExecItem().perScheduleClsNomarl() && self.currentExecItem().processExecType() == 0) {
                    $("#targetDate").trigger("validate");
                    $("#creationPeriod").trigger("validate");
                }
                if (self.currentExecItem().perScheduleCls()) {
                    $("#targetDate").ntsEditor('validate');
                    $("#creationPeriod").ntsEditor('validate');
                }
                return $('.nts-input').ntsError('hasError');
            }

            /**
             * clearError
             */
            private clearError() {
                nts.uk.ui.errors.clearAll();
            }

            /**
             * toJsonObject
             */
            private toJsonObject(): any {
                let self = this;

                
                // to JsObject
                let command: any = {};
                command.newMode = self.isNewMode();
                if(self.currentExecItem().processExecType()==0){//通常実行
                    self.currentExecItem().creationTarget(0);
                    command.companyId = self.currentExecItem().companyId();
                    command.execItemCd = self.currentExecItem().execItemCd();
                    command.execItemName = self.currentExecItem().execItemName();
                    command.perScheduleCls = self.currentExecItem().perScheduleClsNomarl();
                     if(!self.currentExecItem().perScheduleClsNomarl() || self.currentExecItem().targetMonth() == 3){ 
                        command.targetDate = 1;                        
                        command.creationPeriod = 1;
                    }else{
                        command.creationPeriod = self.currentExecItem().creationPeriod();
                        command.targetDate = self.currentExecItem().targetDate();
                    }
                    command.targetMonth = self.currentExecItem().targetMonth();
                    command.creationTarget = self.currentExecItem().creationTarget();
                    command.recreateWorkType = false;//B15_3
                    command.manualCorrection = false;//B15_4
                    command.createEmployee = self.currentExecItem().createEmployee();
                    command.recreateTransfer = false;//B15_2(1)
                    command.dailyPerfCls = self.currentExecItem().dailyPerfClsNormal();//B8_1
                    command.dailyPerfItem = self.currentExecItem().dailyPerfItem();
                    command.midJoinEmployee = self.currentExecItem().midJoinEmployee();
                    command.reflectResultCls = self.currentExecItem().reflectResultCls();
                    command.monthlyAggCls = self.currentExecItem().monthlyAggCls();
                    command.execScopeCls = self.currentExecItem().execScopeCls();
                    command.refDate = nts.uk.text.isNullOrEmpty(self.currentExecItem().refDate()) ? null : new Date(self.currentExecItem().refDate());
                    command.workplaceList = self.currentExecItem().workplaceList();
                    command.recreateTypeChangePerson = false;
                    command.recreateTransfers =  false;//B15_2(2)
                    command.appRouteUpdateAtr =  self.currentExecItem().appRouteUpdateAtrNormal()
                    command.createNewEmp =  self.currentExecItem().createNewEmp();
                    command.appRouteUpdateMonthly =  self.currentExecItem().appRouteUpdateMonthly();
                    command.processExecType = self.currentExecItem().processExecType();
                    command.alarmCode = _.isNil(self.currentExecItem().alarmCode())?null : self.currentExecItem().alarmCode();
                    command.alarmAtr = self.currentExecItem().alarmAtr();
                    command.mailPrincipal = self.currentExecItem().mailPrincipal();
                    command.mailAdministrator = self.currentExecItem().mailAdministrator();
                    command.designatedYear = self.currentExecItem().designatedYear();
                    command.startMonthDay = self.currentExecItem().startMonthDay();
                    command.endMonthDay = self.currentExecItem().endMonthDay();
                }else{//再作成
                    self.listGetAlarmByUser()[0] = undefined;
                    self.currentExecItem().creationTarget(1);
                    command.companyId = self.currentExecItem().companyId();
                    command.execItemCd = self.currentExecItem().execItemCd();
                    command.execItemName = self.currentExecItem().execItemName();
                    command.perScheduleCls = self.currentExecItem().perScheduleClsReCreate();//B7_1
                    command.targetMonth = 0;
                    command.targetDate =  1;  
                    command.creationPeriod = 1;
                    command.creationTarget = 1;
                    command.recreateWorkType = self.currentExecItem().recreateWorkType();//B15_3
                    command.manualCorrection = self.currentExecItem().manualCorrection();//B15_4
                    command.createEmployee = false;
                    command.recreateTransfer = self.currentExecItem().recreateTransfer();//B15_2(1)
                    command.dailyPerfCls = self.currentExecItem().dailyPerfClsReCreate();//B14_3
                    command.dailyPerfItem = 0;
                    command.midJoinEmployee = false;
                    command.reflectResultCls = self.currentExecItem().dailyPerfClsReCreate();
                    command.monthlyAggCls = self.currentExecItem().dailyPerfClsReCreate();
                    command.execScopeCls = self.currentExecItem().execScopeCls();
                    command.refDate = nts.uk.text.isNullOrEmpty(self.currentExecItem().refDate()) ? null : new Date(self.currentExecItem().refDate());
                    command.workplaceList = self.currentExecItem().workplaceList();
                    command.recreateTypeChangePerson = self.currentExecItem().recreateTypeChangePerson();//B14_2
                    command.recreateTransfers =  self.currentExecItem().recreateTransfer();//B15_2(2)
                    command.appRouteUpdateAtr =  self.currentExecItem().appRouteUpdateAtrReCreate();
                    command.createNewEmp =  false;
                    command.appRouteUpdateMonthly =  false;
                    command.processExecType = self.currentExecItem().processExecType();
                    command.alarmCode = _.isNil(self.listGetAlarmByUser()[0])?null : self.listGetAlarmByUser()[0].alarmCode;
                    command.alarmAtr = false; 
                    command.mailPrincipal = false;
                    command.mailAdministrator = false;
                    command.designatedYear = 0;
                    command.startMonthDay = 101;
                    command.endMonthDay = 101;
                }
                
                
                return command;
            }

            /**
             * showMessageError
             */
            private showMessageError(res: any) {
                if (res.businessException) {
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds });
                }
            }

            private focusInput() {
                let self = this;
                if (self.isNewMode()) {
                    $('#execItemCd').focus();
                } else {
                    $('#execItemName').focus();
                }
            }

            /**
             * Convert tree data to array.
             */
            private convertTreeToArray(dataList: Array<any>): Array<any> {
                let self = this;
                let res = [];
                _.forEach(dataList, function(item) {
                    if (item.childs && item.childs.length > 0) {
                        res = res.concat(self.convertTreeToArray(item.childs));
                    }
                    res.push({ workplaceId: item.workplaceId, hierarchyCode: item.code, name: item.name });
                })
                return res;
            }
        }

        export interface IExecutionItem {
            companyId: string;
            execItemCd: string;
            execItemName: string;
            perScheduleCls: boolean;
            perScheduleClsNomarl :boolean;
            perScheduleClsReCreate :boolean;
            targetMonth: number;
            targetDate: number;
            creationPeriod: number;
            creationTarget: number;
            recreateWorkType: boolean;
            manualCorrection: boolean;
            createEmployee: boolean;
            recreateTransfer: boolean;
            dailyPerfCls: boolean;
            dailyPerfItem: number;
            //lastProcDate: string;
            midJoinEmployee: boolean;
            reflectResultCls: boolean;
            monthlyAggCls: boolean;
            execScopeCls: number;
            refDate: string;
            workplaceList: Array<string>;
            recreateTypeChangePerson: boolean;
            recreateTransfers: boolean;
            appRouteUpdateAtr : boolean;
            createNewEmp :boolean;
            appRouteUpdateMonthly :boolean;
            processExecType : number;
            appRouteUpdateAtrNormal : boolean;
            appRouteUpdateAtrReCreate : boolean;
            //dailyPerfCls
            dailyPerfClsNormal : boolean;
            dailyPerfClsReCreate : boolean;
            //alarmCode
            alarmCode :string;
            alarmAtr : boolean;
            mailPrincipal : boolean;
            mailAdministrator : boolean;
            //ver29
            designatedYear : number;
            startMonthDay : number;
            endMonthDay : number;
        }

        export class ExecutionItem {
            companyId: KnockoutObservable<string> = ko.observable('');
            execItemCd: KnockoutObservable<string> = ko.observable('');
            execItemName: KnockoutObservable<string> = ko.observable('');
            perScheduleCls: KnockoutObservable<boolean> = ko.observable(false);
            perScheduleClsNomarl :KnockoutObservable<boolean> = ko.observable(false);
            perScheduleClsReCreate :KnockoutObservable<boolean> = ko.observable(false);
            targetMonth: KnockoutObservable<number> = ko.observable(null);
            targetDate: KnockoutObservable<number> = ko.observable(null);
            creationPeriod: KnockoutObservable<number> = ko.observable(null);
            creationTarget: KnockoutObservable<number> = ko.observable(null);
            recreateWorkType: KnockoutObservable<boolean> = ko.observable(false);
            manualCorrection: KnockoutObservable<boolean> = ko.observable(false);
            createEmployee: KnockoutObservable<boolean> = ko.observable(false);
            recreateTransfer: KnockoutObservable<boolean> = ko.observable(false);
            dailyPerfCls: KnockoutObservable<boolean> = ko.observable(false);
            dailyPerfItem: KnockoutObservable<number> = ko.observable(null);
            midJoinEmployee: KnockoutObservable<boolean> = ko.observable(false);
            reflectResultCls: KnockoutObservable<boolean> = ko.observable(false);
            monthlyAggCls: KnockoutObservable<boolean> = ko.observable(false);
            execScopeCls: KnockoutObservable<number> = ko.observable(null);
            refDate: KnockoutObservable<string> = ko.observable('');
            workplaceList: KnockoutObservableArray<string> = ko.observableArray([]);
            recreateTypeChangePerson: KnockoutObservable<boolean> = ko.observable(false);
            recreateTransfers: KnockoutObservable<boolean> = ko.observable(false);
            appRouteUpdateAtr :KnockoutObservable<boolean> = ko.observable(false);
            createNewEmp: KnockoutObservable<boolean> = ko.observable(false);
            appRouteUpdateMonthly: KnockoutObservable<boolean> = ko.observable(false);
            checkCreateNewEmp :KnockoutObservable<boolean> = ko.observable(false);
            processExecType : KnockoutObservable<number> = ko.observable(null);
            appRouteUpdateAtrNormal :KnockoutObservable<boolean> = ko.observable(false);
            appRouteUpdateAtrReCreate :KnockoutObservable<boolean> = ko.observable(false);
            dailyPerfClsNormal :KnockoutObservable<boolean> = ko.observable(false);
            dailyPerfClsReCreate :KnockoutObservable<boolean> = ko.observable(false);
            alarmCode : KnockoutObservable<string> = ko.observable('');
            alarmAtr :KnockoutObservable<boolean> = ko.observable(false);
            mailPrincipal :KnockoutObservable<boolean> = ko.observable(false);
            mailAdministrator :KnockoutObservable<boolean> = ko.observable(false);
            //ver29
            designatedYear : KnockoutObservable<number> = ko.observable(null);
            startMonthDay : KnockoutObservable<number> = ko.observable(101);
            endMonthDay : KnockoutObservable<number> = ko.observable(101);
            disableYearMonthDate :KnockoutObservable<boolean> = ko.observable(false);
            enableMonthDay :KnockoutObservable<boolean> = ko.observable(false);
            constructor(param: IExecutionItem) {
                let self = this;
                if (param && param != null) {
                    self.companyId(param.companyId);
                    self.execItemCd(param.execItemCd || '');
                    self.execItemName(param.execItemName || '');
                    self.perScheduleCls(param.perScheduleCls || false);
                    self.perScheduleClsNomarl(param.perScheduleCls || false);
                    self.perScheduleClsReCreate(param.perScheduleCls || false);
                    self.targetMonth(param.targetMonth);
                    self.targetDate(param.targetDate);
                    self.creationPeriod(param.creationPeriod);
                    self.creationTarget(param.creationTarget);
                    self.recreateWorkType(param.recreateWorkType || false);
                    self.manualCorrection(param.manualCorrection || false);
                    self.createEmployee(param.createEmployee || false);
                    self.recreateTransfer(param.recreateTransfer || false);
                    self.dailyPerfCls(param.dailyPerfCls || false);
                    self.dailyPerfItem(param.dailyPerfItem);
                    self.midJoinEmployee(param.midJoinEmployee || false);
                    self.reflectResultCls(param.reflectResultCls || false);
                    self.monthlyAggCls(param.monthlyAggCls || false);
                    self.execScopeCls(param.execScopeCls);
                    self.refDate(param.refDate || moment().format("YYYY/MM/DD"));
                    self.workplaceList(param.workplaceList || []);
                    self.recreateTypeChangePerson(param.recreateTypeChangePerson||false);
                    self.recreateTransfers(param.recreateTransfers||false);
                    self.appRouteUpdateAtr(param.appRouteUpdateAtr||false);
                    self.createNewEmp(param.createNewEmp||false);
                    self.appRouteUpdateMonthly(param.appRouteUpdateMonthly||false);
                    self.checkCreateNewEmp((param.appRouteUpdateAtr==true && param.appRouteUpdateAtr == true)?true:false);
                    self.processExecType(param.processExecType);
                    self.appRouteUpdateAtrNormal(param.appRouteUpdateAtr||false);
                    self.appRouteUpdateAtrReCreate(param.appRouteUpdateAtr||false);
                    self.dailyPerfClsNormal(param.dailyPerfCls||false);
                    self.dailyPerfClsReCreate(param.dailyPerfCls||false);
                    self.alarmCode(param.alarmCode || '');
                    self.alarmAtr(param.alarmAtr||false);
                    self.mailPrincipal(param.mailPrincipal||false);
                    self.mailAdministrator(param.mailAdministrator||false);
                    self.designatedYear(param.designatedYear);
                    self.startMonthDay(param.startMonthDay==null?101:param.startMonthDay);
                    self.endMonthDay(param.endMonthDay==null?101:param.endMonthDay);
                    if(self.targetMonth() == 3){
                        self.disableYearMonthDate(true);    
                    }else{
                        self.disableYearMonthDate(false);    
                    }
                    if(self.processExecType()==0){
                        self.creationTarget(0);
                        self.appRouteUpdateAtrNormal(self.appRouteUpdateAtr());
                        self.appRouteUpdateAtrReCreate(false);
                        
                        self.dailyPerfClsNormal(self.dailyPerfCls());
                        self.dailyPerfClsReCreate(false);
                        
                        self.perScheduleClsNomarl(self.perScheduleCls());
                        self.perScheduleClsReCreate(false);
                        
                        if(self.perScheduleClsNomarl()){
                            self.enableMonthDay(true);
                        }
                    }else{
                        self.creationTarget(1);
                        self.appRouteUpdateAtrNormal(false);
                        self.appRouteUpdateAtrReCreate(self.appRouteUpdateAtr());
                        
                        self.dailyPerfClsNormal(false);
                        self.dailyPerfClsReCreate(self.dailyPerfCls());
                        
                        self.perScheduleClsNomarl(false);
                        self.perScheduleClsReCreate(self.perScheduleCls());
                        
                        if(self.perScheduleClsNomarl()){
                            self.enableMonthDay(false);
                        }
                    }
                    
                } else {
                    self.companyId('');
                    self.execItemCd('');
                    self.execItemName('');
                    self.perScheduleCls(false);
                    self.targetMonth(0);
                    self.targetDate(null);
                    self.creationPeriod(null);
                    self.creationTarget(0);
                    self.recreateWorkType(false);
                    self.manualCorrection(false);
                    self.createEmployee(false);
                    self.recreateTransfer(false);
                    self.dailyPerfCls(false);
                    self.dailyPerfItem(0);
                    self.midJoinEmployee(false);
                    self.reflectResultCls(false);
                    self.monthlyAggCls(false);
                    self.execScopeCls(0);
                    self.refDate(moment().format("YYYY/MM/DD"));
                    self.workplaceList([]);
                    self.recreateTypeChangePerson(false);
                    self.recreateTransfers(false);
                    self.appRouteUpdateAtr(false);
                    self.createNewEmp(false);
                    self.appRouteUpdateMonthly(false);
                    self.checkCreateNewEmp(false);
                    self.processExecType(0);
                    self.appRouteUpdateAtrNormal(false);
                    self.appRouteUpdateAtrReCreate(false);
                    self.dailyPerfClsNormal(false);
                    self.dailyPerfClsReCreate(false);
                    self.alarmCode('');
                    self.alarmAtr(false);
                    self.perScheduleClsNomarl(false);
                    self.perScheduleClsReCreate(false);
                    self.mailPrincipal(false);
                    self.mailAdministrator(false);
                    self.processExecType(0);
                    self.designatedYear(0);
                    self.startMonthDay(101);
                    self.endMonthDay(101);
                } 
                /** self.targetMonth() : 
                 * 0: システム日付の月
                    1: システム日付の翌月
                    2: システム日付の翌々月
                    3: 開始月を指定する
                 */
                self.targetMonth.subscribe(x=>{
                    nts.uk.ui.errors.clearAll();
                    if(x!=3){
                        self.designatedYear(0);
                        self.startMonthDay(101);
                        self.endMonthDay(101);
                        self.disableYearMonthDate(false);    
                    }else{
                        self.disableYearMonthDate(true);     
                    }    
                });
                
                
                
//                self.startMonthDay.subscribe(x=>{
//                    nts.uk.ui.errors.clearAll();
//                    _.defer(()=>{
//                        if(self.targetMonth() == 3){
//                            if(x > self.endMonthDay()){
//                                $('#start-month-day-id .ntsMonthPicker, #start-month-day-id .ntsDayPicker').addClass('error');
//                                $('#start-month-day-id').ntsError('set', { messageId: "Msg_927" });
//                            }    
//                        }
//                    });
//                });
//                
//                self.endMonthDay.subscribe(x=>{
//                   nts.uk.ui.errors.clearAll();
//                    _.defer(()=>{
//                        if(self.targetMonth() == 3){
//                            nts.uk.ui.errors.clearAll();
//                            if(x < self.startMonthDay()){
//                                $('#end-month-day-id .ntsMonthPicker, #end-month-day-id .ntsDayPicker').addClass('error');
//                                $('#end-month-day-id').ntsError('set', { messageId: "Msg_927" });
//                            }    
//                        }
//                        
//                    });
//                });
//                
                self.perScheduleClsNomarl.subscribe(x=>{
                    if(x ==true && self.perScheduleClsNomarl() ==true){
                        self.enableMonthDay(true);
                    }else{
                        self.enableMonthDay(false);
                    }
                });
                
                self.appRouteUpdateAtr.subscribe(x=>{
                    if(x==true && self.perScheduleCls()==true){
                        self.checkCreateNewEmp(true);
                    }else{
                        self.checkCreateNewEmp(false); 
                    }
                });
                self.perScheduleCls.subscribe(x=>{
                    if(x==true && self.appRouteUpdateAtr()==true){
                        self.checkCreateNewEmp(true);
                    }else{
                        self.checkCreateNewEmp(false);
                    }
                });
                
                self.processExecType.subscribe(x=>{
                    if(x==0){
                        self.creationTarget(0);
                        self.appRouteUpdateAtrNormal(self.appRouteUpdateAtr());
                        self.appRouteUpdateAtrReCreate(false);
                        
                        self.dailyPerfClsNormal(self.dailyPerfCls());
                        self.dailyPerfClsReCreate(false);
                        if(self.perScheduleClsNomarl()){
                            self.enableMonthDay(true);
                        }
                    }else{
                        self.creationTarget(1);
                        self.appRouteUpdateAtrNormal(false);
                        self.appRouteUpdateAtrReCreate(self.appRouteUpdateAtr());
                        
                        self.dailyPerfClsNormal(false);
                        self.dailyPerfClsReCreate(self.dailyPerfCls());
                        
                        if(self.perScheduleClsNomarl()){
                            self.enableMonthDay(false);
                        }
                    }
                });
                
                  self.targetDate.subscribe(x=>{
                    var data =  this;
                       __viewContext.viewModel.targetDateText(__viewContext.viewModel.buildTargetDateStr(data)); 
                });
                   self.creationPeriod.subscribe(x=>{
                    var data =  this;
                       __viewContext.viewModel.targetDateText(__viewContext.viewModel.buildTargetDateStr(data)); 
                });
                 self.targetMonth.subscribe(x=>{
                    var data =  this;
                       __viewContext.viewModel.targetDateText(__viewContext.viewModel.buildTargetDateStr(data)); 
                });
            }
            
            createData(param: IExecutionItem){
                let self = this;
                
                 self.companyId(param.companyId);
                    self.execItemCd(param.execItemCd || '');
                    self.execItemName(param.execItemName || '');
                    self.perScheduleCls(param.perScheduleCls || false);
                    self.perScheduleClsNomarl(param.perScheduleCls || false);
                    self.perScheduleClsReCreate(param.perScheduleCls || false);
                    self.targetMonth(param.targetMonth);
                    self.targetDate(param.targetDate);
                    self.creationPeriod(param.creationPeriod);
                    self.creationTarget(param.creationTarget);
                    self.recreateWorkType(param.recreateWorkType || false);
                    self.manualCorrection(param.manualCorrection || false);
                    self.createEmployee(param.createEmployee || false);
                    self.recreateTransfer(param.recreateTransfer || false);
                    self.dailyPerfCls(param.dailyPerfCls || false);
                    self.dailyPerfItem(param.dailyPerfItem);
                    self.midJoinEmployee(param.midJoinEmployee || false);
                    self.reflectResultCls(param.reflectResultCls || false);
                    self.monthlyAggCls(param.monthlyAggCls || false);
                    self.execScopeCls(param.execScopeCls);
                    self.refDate(param.refDate || moment().format("YYYY/MM/DD"));
                    self.workplaceList(param.workplaceList || []);
                    self.recreateTypeChangePerson(param.recreateTypeChangePerson||false);
                    self.recreateTransfers(param.recreateTransfers||false);
                    self.appRouteUpdateAtr(param.appRouteUpdateAtr||false);
                    self.createNewEmp(param.createNewEmp||false);
                    self.appRouteUpdateMonthly(param.appRouteUpdateMonthly||false);
                    self.checkCreateNewEmp((param.appRouteUpdateAtr==true && param.appRouteUpdateAtr == true)?true:false);
                    self.processExecType(param.processExecType);
                    self.appRouteUpdateAtrNormal(param.appRouteUpdateAtr||false);
                    self.appRouteUpdateAtrReCreate(param.appRouteUpdateAtr||false);
                    self.dailyPerfClsNormal(param.dailyPerfCls||false);
                    self.dailyPerfClsReCreate(param.dailyPerfCls||false);
                    self.alarmCode(param.alarmCode || '');
                    self.alarmAtr(param.alarmAtr||false);
                    self.mailPrincipal(param.mailPrincipal||false);
                    self.mailAdministrator(param.mailAdministrator||false);
                    self.designatedYear(param.designatedYear);
                    self.startMonthDay(param.startMonthDay==null?101:param.startMonthDay);
                    self.endMonthDay(param.endMonthDay==null?101:param.endMonthDay);
                    if(self.targetMonth() == 3){
                        self.disableYearMonthDate(true);    
                    }else{
                        self.disableYearMonthDate(false);    
                    }
                    if(self.processExecType()==0){
                        self.creationTarget(0);
                        self.appRouteUpdateAtrNormal(self.appRouteUpdateAtr());
                        self.appRouteUpdateAtrReCreate(false);
                        
                        self.dailyPerfClsNormal(self.dailyPerfCls());
                        self.dailyPerfClsReCreate(false);
                        
                        self.perScheduleClsNomarl(self.perScheduleCls());
                        self.perScheduleClsReCreate(false);
                        
                        if(self.perScheduleClsNomarl()){
                            self.enableMonthDay(true);
                        }
                    }else{
                        self.creationTarget(1);
                        self.appRouteUpdateAtrNormal(false);
                        self.appRouteUpdateAtrReCreate(self.appRouteUpdateAtr());
                        
                        self.dailyPerfClsNormal(false);
                        self.dailyPerfClsReCreate(self.dailyPerfCls());
                        
                        self.perScheduleClsNomarl(false);
                        self.perScheduleClsReCreate(self.perScheduleCls());
                        
                        if(self.perScheduleClsNomarl()){
                            self.enableMonthDay(false);
                        }
                    }
            }
        }

        export interface ExecItemEnumDto {
            targetMonth: Array<EnumConstantDto>;
            dailyPerfItems: Array<EnumConstantDto>;
            repeatContentItems: Array<EnumConstantDto>;
            monthDays: Array<EnumConstantDto>;
            designatedYear: Array<EnumConstantDto>;
        }

        export interface EnumConstantDto {
            value: number;
            fieldName: string;
            localizedName: string;
        }

        export class WorkplaceSearchData {
            workplaceId: string;
            code: string;
            name: string;
        }

        export class TargetGroupClass {
            targetId: number;
            targetName: string;
            constructor(targetId, targetName) {
                var self = this;
                self.targetId = targetId;
                self.targetName = targetName;
            }
        }

    }
}