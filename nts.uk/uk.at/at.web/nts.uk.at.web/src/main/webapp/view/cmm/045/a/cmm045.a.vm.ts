module cmm045.a.viewmodel {
    import vmbase = cmm045.shr.vmbase;
    import getText = nts.uk.resource.getText;
    import block = nts.uk.ui.block;
    import character = nts.uk.characteristics;
    import request = nts.uk.request;
    import getShared = nts.uk.ui.windows.getShared;
    export class ScreenModel {
        roundingRules: KnockoutObservableArray<vmbase.ApplicationDisplayAtr> = ko.observableArray([]);
        //delete switch button - ver35
//        selectedRuleCode: KnockoutObservable<any> = ko.observable(0);// switch button
        //lst fill in grid list
        items: KnockoutObservableArray<vmbase.DataModeApp> = ko.observableArray([]);
        //lst full data get from db
        lstApp: KnockoutObservableArray<vmbase.DataModeApp> = ko.observableArray([]);
        lstAppCommon: KnockoutObservableArray<vmbase.ApplicationDto_New> = ko.observableArray([]);
        lstAppMaster: KnockoutObservableArray<vmbase.AppMasterInfo> = ko.observableArray([]);
        lstAppOt: KnockoutObservableArray<vmbase.AppOverTimeInfoFull> = ko.observableArray([]);
        lstAppGoBack: KnockoutObservableArray<vmbase.AppGoBackInfoFull> = ko.observableArray([]);
        lstListAgent: KnockoutObservableArray<vmbase.ApproveAgent> = ko.observableArray([]);
        lstAppHdWork: KnockoutObservableArray<vmbase.AppHolidayWorkFull> = ko.observableArray([]);
        lstAppWorkChange: KnockoutObservableArray<vmbase.AppWorkChangeFull> = ko.observableArray([]);
        lstAppAbsence: KnockoutObservableArray<vmbase.AppAbsenceFull> = ko.observableArray([]);
        lstAppCompltSync: KnockoutObservableArray<vmbase.AppCompltLeaveSync> = ko.observableArray([]);
        hdAppSet: KnockoutObservable<vmbase.HdAppSet> = ko.observable(null);
        
        displaySet: KnockoutObservable<vmbase.ApprovalListDisplaySetDto> = ko.observable(null);
        approvalMode: KnockoutObservable<boolean> = ko.observable(false);
        approvalCount: KnockoutObservable<vmbase.ApplicationStatus> = ko.observable(new vmbase.ApplicationStatus(0, 0, 0, 0, 0, 0));
        itemList: KnockoutObservableArray<any>;
        selectedIds: KnockoutObservableArray<any> = ko.observableArray([1]);// check box
        dateValue: KnockoutObservable<vmbase.Date> = ko.observable({ startDate: '', endDate: '' });
        itemApplication: KnockoutObservableArray<vmbase.ChoseApplicationList> = ko.observableArray([]);
        selectedCode: KnockoutObservable<number> = ko.observable(-1);// combo box
        mode: KnockoutObservable<number> = ko.observable(1);
        startDateString: KnockoutObservable<string> = ko.observable("");
        endDateString: KnockoutObservable<string> = ko.observable("");
        
        //spr
        isSpr: KnockoutObservable<boolean> = ko.observable(false);
        extractCondition: KnockoutObservable<number> = ko.observable(0);
        //ver33
        isHidden: KnockoutObservable<boolean> = ko.observable(false);
        //_______CCG001____
        ccgcomponent: any;
        showinfoSelectedEmployee: KnockoutObservable<boolean>;
        baseDate: KnockoutObservable<Date>;
        selectedEmployee: KnockoutObservableArray<any>;
        workplaceId: KnockoutObservable<string> = ko.observable("");
        employeeId: KnockoutObservable<string> = ko.observable("");
        //ver35
        lstSidFilter: KnockoutObservableArray<string> = ko.observableArray([]);
        constructor() {
            let self = this;
            self.itemList = ko.observableArray([
                { id: 1, name: getText('CMM045_20') },
                { id: 2, name: getText('CMM045_21') },
                { id: 3, name: getText('CMM045_22') },
                { id: 4, name: getText('CMM045_23') },
                { id: 5, name: getText('CMM045_24') },
                { id: 6, name: getText('CMM045_25') }
            ]);
            
            self.selectedCode.subscribe(function(codeChanged) {
                self.filterByAppType(codeChanged);
            });
            
            //_____CCG001________
            self.selectedEmployee = ko.observableArray([]);
            self.showinfoSelectedEmployee = ko.observable(false);
            self.ccgcomponent = {
           
            showEmployeeSelection: false, // 検索タイプ
            systemType: 2, // システム区分 - 就業
            showQuickSearchTab: true, // クイック検索
            showAdvancedSearchTab: true, // 詳細検索
            showBaseDate: false, // 基準日利用
            showClosure: false, // 就業締め日利用
            showAllClosure: false, // 全締め表示
            showPeriod: false, // 対象期間利用
            periodFormatYM: true, // 対象期間精度

            /** Required parameter */
            baseDate: moment.utc().toISOString(), // 基準日
            inService: true, // 在職区分
            leaveOfAbsence: true, // 休職区分
            closed: true, // 休業区 
            retirement: true, // 退職区分

            /** Quick search tab options */
            showAllReferableEmployee: true, // 参照可能な社員すべて
            showOnlyMe: true, // 自分だけ
            showSameWorkplace: true, // 同じ職場の社員
            showSameWorkplaceAndChild: true, // 同じ職場とその配下の社員

            /** Advanced search properties */
            showEmployment: true, // 雇用条件
            showWorkplace: true, // 職場条件
            showClassification: true, // 分類条件
            showJobTitle: true, // 職位条件
            showWorktype: true, // 勤種条件
            isMutipleCheck: true,
            /**  
            * @param dataList: list employee returned from component.
            * Define how to use this list employee by yourself in the function's body.
            */
            returnDataFromCcg001: function(data: any){
                self.showinfoSelectedEmployee(true);
                self.selectedEmployee(data.listEmployee);
                console.log(data.listEmployee);
                self.lstSidFilter([]);
                _.each(data.listEmployee, function(emp){
                    self.lstSidFilter.push(emp.employeeId);
                });
                self.filter();
             }
            }
        }

        start(): JQueryPromise<any> {
            block.invisible();
            let self = this;
            var dfd = $.Deferred();
            //get param url
            let url = $(location).attr('search');
            let urlParam: number = url.split("=")[1];
            let characterData = null;
            let appCHeck = null;
            if (urlParam !== undefined) {
                character.save('AppListExtractCondition', null);
            }
            //get param spr
            let paramSprCmm045: vmbase.IntefaceSPR = __viewContext.transferred.value == null ? 
                    null : __viewContext.transferred.value.PARAM_SPR_CMM045;
            //spr call
            if(paramSprCmm045 !== undefined && paramSprCmm045 !== null){
                character.save('AppListExtractCondition', null);
                let date: vmbase.Date = { startDate: paramSprCmm045.startDate, endDate: paramSprCmm045.endDate }
                self.dateValue(date);
                self.mode(paramSprCmm045.mode);
                self.isSpr(true);
                self.extractCondition(paramSprCmm045.extractCondition);
            }
            character.restore("AppListExtractCondition").done((obj) => {
                characterData = obj;
                if (obj !== undefined && obj !== null && !self.isSpr()) {
                    let date: vmbase.Date = { startDate: obj.startDate, endDate: obj.endDate }
                    self.dateValue(date);
                    self.selectedIds([]);
                    if (obj.unapprovalStatus) {//未承認
                        self.selectedIds.push(1);
                    }
                    if (obj.approvalStatus) {//承認済み
                        self.selectedIds.push(2);
                    }
                    if (obj.denialStatus) {//否認
                        self.selectedIds.push(3);
                    }
                    if (obj.agentApprovalStatus) {//代行承認済み
                        self.selectedIds.push(4);
                    }
                    if (obj.remandStatus) {//差戻
                        self.selectedIds.push(5);
                    }
                    if (obj.cancelStatus) {//取消
                        self.selectedIds.push(6);
                    }
//                    self.selectedRuleCode(obj.appDisplayAtr);
                    //combo box
                    appCHeck = obj.appType;
                    self.lstSidFilter(obj.listEmployeeId);
                }
                if (urlParam === undefined && !self.isSpr()) {
                    self.mode(characterData.appListAtr);
                } 
                if(urlParam !== undefined && !self.isSpr()){
                    self.mode(urlParam);
                }
                //write log
                let paramLog = {programId: 'CMM045',
                                screenId: 'A', 
                                queryString: 'a='+self.mode()};
                service.writeLog(paramLog);
                let condition: vmbase.AppListExtractConditionDto = new vmbase.AppListExtractConditionDto(self.dateValue().startDate, self.dateValue().endDate, self.mode(),
                    self.selectedCode(), self.findcheck(self.selectedIds(), 1), self.findcheck(self.selectedIds(), 2), self.findcheck(self.selectedIds(), 3),
                    self.findcheck(self.selectedIds(), 4), self.findcheck(self.selectedIds(), 5), self.findcheck(self.selectedIds(), 6), 0, self.lstSidFilter(), '');
                let param = new vmbase.AppListParamFilter(condition, self.isSpr(), self.extractCondition());
                service.getApplicationDisplayAtr().done(function(data1) {
                    _.each(data1, function(obj) {
                        self.roundingRules.push(new vmbase.ApplicationDisplayAtr(obj.value, obj.localizedName));
                    });
                    service.getApplicationList(param).done(function(data) {
                        console.log(data);
                        let isHidden = data.isDisPreP == 1 ? false : true;
                        self.isHidden(isHidden);
//                        self.selectedRuleCode.subscribe(function(codeChanged) {
//                            self.filter();
//                        });
                        //luu param
                        if (self.dateValue().startDate == '' || self.dateValue().endDate == '') {
                            let date: vmbase.Date = { startDate: data.startDate, endDate: data.endDate }
                            self.dateValue(date);
                        }
                        let paramSave: vmbase.AppListExtractConditionDto = new vmbase.AppListExtractConditionDto(self.dateValue().startDate, self.dateValue().endDate, self.mode(),
                            self.selectedCode(), self.findcheck(self.selectedIds(), 1), self.findcheck(self.selectedIds(), 2), self.findcheck(self.selectedIds(), 3),
                            self.findcheck(self.selectedIds(), 4), self.findcheck(self.selectedIds(), 5), self.findcheck(self.selectedIds(), 6), 0, self.lstSidFilter(), '');
                        character.save('AppListExtractCondition', paramSave);
                        let lstGoBack: Array<vmbase.AppGoBackInfoFull> = [];
                        let lstAppGroup: Array<vmbase.AppPrePostGroup> = [];
                        self.displaySet(new vmbase.ApprovalListDisplaySetDto(data.displaySet.advanceExcessMessDisAtr,
                            data.displaySet.hwAdvanceDisAtr, data.displaySet.hwActualDisAtr,
                            data.displaySet.actualExcessMessDisAtr, data.displaySet.otAdvanceDisAtr,
                            data.displaySet.otActualDisAtr, data.displaySet.warningDateDisAtr, data.displaySet.appReasonDisAtr));
                        _.each(data.lstApp, function(app) {
                            self.lstAppCommon.push(new vmbase.ApplicationDto_New(app.applicationID, app.prePostAtr, app.inputDate, app.enteredPersonSID,
                                app.reversionReason, app.applicationDate, app.applicationReason, app.applicationType, app.applicantSID,
                                app.reflectPlanScheReason, app.reflectPlanTime, app.reflectPlanState, app.reflectPlanEnforce,
                                app.reflectPerScheReason, app.reflectPerTime, app.reflectPerState, app.reflectPerEnforce,
                                app.startDate, app.endDate, app.version));
                        });
                        _.each(data.lstMasterInfo, function(master) {
                            self.lstAppMaster.push(new vmbase.AppMasterInfo(master.appID, master.appType, master.dispName, master.empName,master.inpEmpName,
                                master.workplaceName, master.statusFrameAtr, master.phaseStatus, master.checkAddNote, master.checkTimecolor, master.detailSet));
                        });
                        _.each(data.lstAppGoBack, function(goback) {
                            lstGoBack.push(new vmbase.AppGoBackInfoFull(goback.appID, goback.goWorkAtr1, goback.workTimeStart1,
                                goback.backHomeAtr1, goback.workTimeEnd1, goback.goWorkAtr2, goback.workTimeStart2, goback.backHomeAtr2, goback.workTimeEnd2));
                        });
                        _.each(data.lstAppOt, function(overTime) {
                            let lstFrame: Array<vmbase.OverTimeFrame> = []
                            _.each(overTime.lstFrame, function(frame) {
                                lstFrame.push(new vmbase.OverTimeFrame(frame.attendanceType, frame.frameNo, frame.name,
                                    frame.timeItemTypeAtr, frame.applicationTime));
                            });
                             let timeNo417 = overTime.timeNo417 == null ? null : 
                            new vmbase.TimeNo417(overTime.timeNo417.totalOv, overTime.timeNo417.time36, overTime.timeNo417.numOfYear36Over, overTime.timeNo417.lstOverMonth);
                            self.lstAppOt.push(new vmbase.AppOverTimeInfoFull(overTime.appID, overTime.workClockFrom1, overTime.workClockTo1, overTime.workClockFrom2,
                                overTime.workClockTo2, overTime.total, lstFrame, overTime.overTimeShiftNight, overTime.flexExessTime, timeNo417));
                        });
                        _.each(data.lstAppGroup, function(group) {
                            lstAppGroup.push(new vmbase.AppPrePostGroup(group.preAppID, group.postAppID, group.time,group.strTime1,group.endTime1,group.strTime2,group.endTime2, group.appPre, group.reasonAppPre, group.appPreHd));
                        });
                        self.itemApplication([]);
                        self.itemApplication.push(new vmbase.ChoseApplicationList(-1, '全件表示'));
                        _.each(data.lstAppInfor, function(appInfo){
                            self.itemApplication.push(new vmbase.ChoseApplicationList(appInfo.appType, appInfo.appName));                          
                        });
                        //spr
//                        if(self.isSpr()==1 && self.selectedCode() == 0){
//                            let over = self.findAppOverTime(self.itemApplication());
//                            if(over == undefined){
////                                self.itemApplication.push(new vmbase.ChoseApplicationList(appInfo.appType, appInfo.appName)); 
//                            }    
//                        }
                        self.lstListAgent([]);
                        _.each(data.lstAgent, function(agent){
                            self.lstListAgent.push(new vmbase.ApproveAgent(agent.appID, agent.agentId));
                        });
                        _.each(data.lstAppHdWork, function(hdwork) {
                            let lstFrame: Array<vmbase.OverTimeFrame> = []
                            _.each(hdwork.lstFrame, function(frame) {
                                lstFrame.push(new vmbase.OverTimeFrame(frame.attendanceType, frame.frameNo, frame.name,
                                    frame.timeItemTypeAtr, frame.applicationTime));
                            });
                            let timeNo417 = hdwork.timeNo417 == null ? null : 
                            new vmbase.TimeNo417(hdwork.timeNo417.totalOv, hdwork.timeNo417.time36, hdwork.timeNo417.numOfYear36Over, hdwork.timeNo417.lstOverMonth);
                            self.lstAppHdWork.push(new vmbase.AppHolidayWorkFull(hdwork.appId, hdwork.workTypeName, hdwork.workTimeName, hdwork.startTime1, 
                                hdwork.endTime1, hdwork.startTime2, hdwork.endTime2 ,lstFrame, timeNo417));
                        });
                        _.each(data.lstAppWorkChange, function(wkChange) {
                            self.lstAppWorkChange.push(new vmbase.AppWorkChangeFull(wkChange.appId, wkChange.workTypeName, wkChange.workTimeName,
                                wkChange.goWorkAtr1, wkChange.workTimeStart1, wkChange.backHomeAtr1, wkChange.workTimeEnd1, wkChange.goWorkAtr2,
                                wkChange.workTimeStart2, wkChange.backHomeAtr2, wkChange.workTimeEnd2, wkChange.breakTimeStart1, wkChange.breakTimeEnd1));
                        });
                        _.each(data.lstAppAbsence, function(absence) {
                            self.lstAppAbsence.push(new vmbase.AppAbsenceFull(absence.appID, absence.holidayAppType, absence.day, absence.workTimeName,
                                absence.allDayHalfDayLeaveAtr, absence.startTime1, absence.endTime1, absence.startTime2,
                                absence.endTime2, absence.relationshipCode, absence.relationshipName, absence.mournerFlag, absence.workTypeName));
                        });
                        if(data.hdAppSet != null){
                            self.hdAppSet(new vmbase.HdAppSet(data.hdAppSet.obstacleName, data.hdAppSet.hdName, data.hdAppSet.yearHdName, data.hdAppSet.furikyuName,
                                data.hdAppSet.timeDigest, data.hdAppSet.absenteeism, data.hdAppSet.specialVaca, data.hdAppSet.yearResig));
                        }else{
                            self.hdAppSet(new vmbase.HdAppSet('', '', '', '', '', '', '', ''));    
                        }
                        _.each(data.lstAppCompltLeaveSync, function(complt){
                            let appMain = new vmbase.AppCompltLeaveFull(complt.appMain.appID, complt.appMain.workTypeName, complt.appMain.startTime, complt.appMain.endTime);
                            let appSub = complt.appSub == null ? null : new vmbase.AppCompltLeaveFull(complt.appSub.appID, complt.appSub.workTypeName, complt.appSub.startTime, complt.appSub.endTime);
                            self.lstAppCompltSync.push(new vmbase.AppCompltLeaveSync(complt.typeApp, complt.sync, appMain, appSub, complt.appDateSub, complt.appInputSub));
                        });  
                        let lstData = self.mapData(self.lstAppCommon(), self.lstAppMaster(), lstGoBack, self.lstAppOt(), 
                            lstAppGroup, self.lstAppHdWork(), self.lstAppWorkChange(), self.lstAppAbsence(), self.lstAppCompltSync());
                        self.lstApp(lstData);
                        self.items(lstData);
                        //mode approval - count
                        if (data.appStatusCount != null) {
                            self.approvalCount(new vmbase.ApplicationStatus(data.appStatusCount.unApprovalNumber, data.appStatusCount.approvalNumber,
                                data.appStatusCount.approvalAgentNumber, data.appStatusCount.cancelNumber, data.appStatusCount.remandNumner,
                                data.appStatusCount.denialNumber));
                        }
                        if (self.mode() == 1) {
                            let colorBackGr = self.fillColorbackGrAppr();
                             let lstHidden: Array<any> = self.findRowHidden(self.items());
                             self.reloadGridApproval(lstHidden,colorBackGr, self.isHidden());
                        } else {
                            let colorBackGr = self.fillColorbackGr();
                            self.reloadGridApplicaion(colorBackGr, self.isHidden());
                        }
                        if(appCHeck != null){
                            self.selectedCode(appCHeck);
                        }
                        if(self.isSpr()){
                            let selectedType = paramSprCmm045.extractCondition == 0 ? -1 : 0;
                            self.selectedCode(selectedType);    
                        }
                        if(self.mode() == 0){
                            $('#ccgcomponent').ntsGroupComponent(self.ccgcomponent);
                        }
                        block.clear();
                        dfd.resolve();
                    });
                }).fail(()=>{
                    block.clear();    
                });
            });
            return dfd.promise();
        }

        reloadGridApplicaion(colorBackGr: any, isHidden: boolean) {
            var self = this;
            let widthAuto = isHidden == false ? '1110px' : '1045px';
            $("#grid2").ntsGrid({
                width: widthAuto,
                height: '500px',
                dataSource: self.items(),
                primaryKey: 'appId',
                virtualization: true,
                rows: 8,
                hidePrimaryKey: true,
                rowVirtualization: true,
                virtualizationMode: 'continuous',
                columns: [
                    { headerText: 'ID', key: 'appId', dataType: 'string', width: '0px', hidden: true },
                    { headerText: getText('CMM045_50'), key: 'details', dataType: 'string', width: '55px', unbound: false, ntsControl: 'Button' },
                    { headerText: getText('CMM045_51'), key: 'applicant', dataType: 'string', width: '120px' },
                    { headerText: getText('CMM045_52'), key: 'appName', dataType: 'string', width: '90px'},
                    { headerText: getText('CMM045_53'), key: 'appAtr', dataType: 'string', width: '65px', hidden: isHidden},
                    { headerText: getText('CMM045_54'), key: 'appDate', dataType: 'string', width: '157px'},
                    { headerText: getText('CMM045_55'), key: 'appContent', dataType: 'string', width: '408px', 
                        formatter: (v) => (v.replace(/(<|<)script>/gi, '&lt;script&gt;').replace(/(<\/)script>/gi, '&lt;/script&gt;'))},
                    { headerText: getText('CMM045_56'), key: 'inputDate', dataType: 'string', width: '120px'},
                    { headerText: getText('CMM045_57'), key: 'appStatus', dataType: 'string', width: '75px'}
                ],
                features: [
                    { name: 'Resizing' },
                    {
                        name: 'Selection',
                        mode: 'row',
                        multipleSelection: true
                    }
                ],
                ntsFeatures:[
                    {
                        name: 'CellState',
                        rowId: 'rowId',
                        columnKey: 'columnKey',
                        state: 'state',
                        states: colorBackGr
                    },
                ],
                ntsControls: [{ name: 'Checkbox', options: { value: 1, text: '' }, optionsValue: 'value', optionsText: 'text', controlType: 'CheckBox', enable: true },
                    { name: 'Button', text: getText('CMM045_50'), controlType: 'Button', enable: true },
                ]
            });
            $("#grid2").on("click", ".ntsButton", function(evt, ui) {
                let _this = $(this);
                let id = _this.parents('tr').data('id');
                //Bug #97203 - EA2540
//                let a = self.findDataModeAppByID(id, self.items());
//                let lstAppId = self.findListAppType(a.appType);
                let lstAppId = [];
                _.each(self.items(), function(app){
                    lstAppId.push(app.appId);
                });
                nts.uk.localStorage.setItem('UKProgramParam', 'a=0');
                nts.uk.request.jump("/view/kaf/000/b/index.xhtml", { 'listAppMeta': lstAppId, 'currentApp': id });
            });
        }
        findDataModeAppByID(appId: string, lstAppCommon: Array<vmbase.DataModeApp>){
            return _.find(lstAppCommon, function(app) {
                return app.appId == appId;
            });
        }
        findListAppType(appType: number){
            let self = this;
            let lstAppId = [];
            _.each(self.items(), function(item){
                if(item.appType == appType){
                    lstAppId.push(item.appId);
                }
            });
            return lstAppId;
        }

        fillColorbackGr(): Array<vmbase.CellState>{
            let self = this;
            let result = [];
            _.each(self.items(), function(item) {
                let rowId = item.appId;
                //fill color in 承認状況
                if (item.appStatusNo == 0) {//0 下書き保存/未反映　=　未
                    result.push(new vmbase.CellState(rowId,'appStatus',['unapprovalCell']));
                }
                if (item.appStatusNo == 1) {//1 反映待ち　＝　承認済み
                    result.push(new vmbase.CellState(rowId,'appStatus',['approvalCell']));
                }
                if (item.appStatusNo == 2) {//2 反映済　＝　反映済み
                    result.push(new vmbase.CellState(rowId,'appStatus',['reflectCell']));
                }
                if (item.appStatusNo == 3 || item.appStatusNo == 4) {//3,4 取消待ち/取消済　＝　取消
                    result.push(new vmbase.CellState(rowId,'appStatus',['cancelCell']));
                }
                if (item.appStatusNo == 5) {//5 差し戻し　＝　差戻
                    result.push(new vmbase.CellState(rowId,'appStatus',['remandCell']));
                }
                if (item.appStatusNo == 6) {//6 否認　=　否
                    result.push(new vmbase.CellState(rowId,'appStatus',['denialCell']));
                }
                //fill color in 申請内容
                if (item.checkTimecolor == 1) {//1: xin truoc < xin sau; k co xin truoc; xin truoc bi denail
                    result.push(new vmbase.CellState(rowId,'appContent',['preAppExcess']));
                }
                if (item.checkTimecolor == 2) {////2: thuc te < xin sau
                    result.push(new vmbase.CellState(rowId,'appContent',['workingResultExcess']));
                }
            });
            return result;
        }
        fillColorbackGrAppr(): Array<vmbase.CellState>{
            let self = this;
            let result = [];
            _.each(self.items(), function(item) {
                let rowId = item.appId;
                //fill color in 承認状況
                if (item.appStatusNo == 5) {//5 -UNAPPROVED 未
                    result.push(new vmbase.CellState(rowId,'appStatus',['unapprovalCell']));
                }
                if (item.appStatusNo == 4) {//4 APPROVED 承認済み
                    result.push(new vmbase.CellState(rowId,'appStatus',['approvalCell']));
                }
                if (item.appStatusNo == 3) {//3 CANCELED 取消
                    result.push(new vmbase.CellState(rowId,'appStatus',['cancelCell']));
                }
                if (item.appStatusNo == 2) {//2 REMAND 差戻
                    result.push(new vmbase.CellState(rowId,'appStatus',['remandCell']));
                }
                if (item.appStatusNo == 1) {//1 DENIAL 否
                    result.push(new vmbase.CellState(rowId,'appStatus',['denialCell']));
                }
                //fill color in 申請内容
                if (item.checkTimecolor == 1) {//1: xin truoc < xin sau; k co xin truoc; xin truoc bi denail
                    result.push(new vmbase.CellState(rowId,'appContent',['preAppExcess']));
                }
                if (item.checkTimecolor == 2) {////2: thuc te < xin sau
                    result.push(new vmbase.CellState(rowId,'appContent',['workingResultExcess']));
                }
            });
            return result;
        }
        fillColorText(): Array<vmbase.TextColor>{
            //fill color text
            let self = this;
            let result = [];
            _.each(self.items(), function(item) {
                if(item.appType == 10){
                    return;
                }
                //color text appDate
                let color = item.appDate.substring(11,12);
                if (color == '土') {//土
                    result.push(new vmbase.TextColor(item.appId,'appDate','saturdayCell'));
                }
                if (color == '日') {//日 
                    result.push(new vmbase.TextColor(item.appId,'appDate','sundayCell'));
                }
                //fill color text input date
                let colorIn = item.inputDate.substring(11,12);
                if (colorIn == '土') {//土
                    result.push(new vmbase.TextColor(item.appId,'inputDate','saturdayCell'));
                }
                if (colorIn == '日') {//日
                    result.push(new vmbase.TextColor(item.appId,'inputDate','sundayCell'));
                } 
             });
            return result;
        }
        reloadGridApproval(lstHidden: Array<any>, colorBackGr: any, isHidden: boolean) {
            var self = this;
            let widthAuto = isHidden == false ? '1175px' : '1110px';
            $("#grid1").ntsGrid({
                width: widthAuto,
                height: '530px',
                dataSource: self.items(),
                primaryKey: 'appId',
                rowVirtualization: true,
                virtualization: true,
                hidePrimaryKey: true,
                rows: 8,
                virtualizationMode: 'continuous',
                columns: [
                    { headerText: getText('CMM045_49'), key: 'check', dataType: 'boolean', width: '35px', 
                            showHeaderCheckbox: true, ntsControl: 'Checkbox',  hiddenRows: lstHidden},
                    { headerText: getText('CMM045_50'), key: 'details', dataType: 'string', width: '55px', unbound: false, ntsControl: 'Button' },
                    { headerText: getText('CMM045_51'), key: 'applicant', dataType: 'string', width: '120px' },
                    { headerText: getText('CMM045_52'), key: 'appName', dataType: 'string', width: '90px'},
                    { headerText: getText('CMM045_53'), key: 'appAtr', dataType: 'string', width: '65px', hidden: isHidden},
                    { headerText: getText('CMM045_54'), key: 'appDate', dataType: 'string', width: '157px'},
                    { headerText: getText('CMM045_55'), key: 'appContent', dataType: 'string', width: '341px',
                        formatter: (v) => (v.replace(/(<|<)script>/gi, '&lt;script&gt;').replace(/(<\/)script>/gi, '&lt;/script&gt;'))},
                    { headerText: getText('CMM045_56'), key: 'inputDate', dataType: 'string', width: '120px'},
                    { headerText: getText('CMM045_57'), key: 'appStatus', dataType: 'string', width: '75px'},
                    { headerText: getText('CMM045_58'), key: 'displayAppStatus', dataType: 'string', width: '95px' },
                    { headerText: 'ID', key: 'appId', dataType: 'string', width: '0px', ntsControl: 'Label', hidden: true }
                ],
                features: [{ name: 'Resizing' },
                    {
                        name: 'Selection',
                        mode: 'row',
                        multipleSelection: true
                    }
                ],
                 ntsFeatures:[
                    {
                        name: 'CellState',
                        rowId: 'rowId',
                        columnKey: 'columnKey',
                        state: 'state',
                        states: colorBackGr
                    },
//                    {
//                        name: 'TextColor',
//                        rowId: 'rowId',
//                        columnKey: 'columnKey',
//                        color: 'color',
//                        colorsTable: colorsText
//                    }
                 ],
                ntsControls: [{ name: 'Checkbox', options: { value: 1, text: '' }, optionsValue: 'value', optionsText: 'text', controlType: 'CheckBox' },
                    { name: 'Button', text: getText('CMM045_50'), controlType: 'Button', enable: true }],
            });

            $("#grid1").on("click", ".ntsButton", function(evt, ui) {
                let _this = $(this);
                let id = _this.parents('tr').data('id');
                //Bug #97203 - EA2540
//                let a = self.findDataModeAppByID(id, self.items());
//                let lstAppId = self.findListAppType(a.appType);
                let lstAppId = [];
                _.each(self.items(), function(app){
                    lstAppId.push(app.appId);
                });
                nts.uk.localStorage.setItem('UKProgramParam', 'a=1');
                nts.uk.request.jump("/view/kaf/000/b/index.xhtml", { 'listAppMeta': lstAppId, 'currentApp': id });
            });

            $("#grid1").setupSearchScroll("igGrid", true);
        }
        /**
         * 休日出勤時間申請
         * kaf010 - appTYpe = 6
         * format data: holiday work before
         * ※申請モード、承認モード(事前)用レイアウト
         */
        formatHdWorkBf(app: vmbase.ApplicationDto_New, hdWork: vmbase.AppHolidayWorkFull, masterInfo: vmbase.AppMasterInfo): vmbase.DataModeApp {
            let self = this;
            let reason = self.displaySet().appReasonDisAtr == 0 || app.applicationReason == '' ? '' : '<br/>' + app.applicationReason;
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
            let applicant: string = masterInfo.workplaceName == '' ? empNameFull : masterInfo.workplaceName + '<br/>' + empNameFull;
            let ca1 = hdWork.startTime1 == '' ? '' : hdWork.startTime1 + getText('CMM045_100') + hdWork.endTime1;
            let ca2 = hdWork.startTime2 == '' ? '' : hdWork.startTime2 + getText('CMM045_100') + hdWork.endTime2;
            let appContent010: string = hdWork.workTypeName + hdWork.workTimeName + ca1 + ca2 + self.convertFrameTimeHd(hdWork.lstFrame);
            //No.417
            let timeNo417 = self.displayTimeNo417(hdWork.timeNo417);
            let appCt010 = appContent010 + timeNo417 + reason;
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, self.appDateColor(self.convertDateMDW(app.startDate), '',''), appCt010, self.inputDateColor(self.convertDateTime(app.inputDate), ''),
                self.mode() == 0 ? self.convertStatus(app.reflectPerState) : self.convertStatusAppv(app.reflectPerState), masterInfo.phaseStatus,
                masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor, null, app.reflectPerState);
            return a;
        }
        displayTimeNo417(timeNo417: vmbase.TimeNo417){
            let self = this;
            if(timeNo417 == null){
                return '';
            }
            
            if(timeNo417.time36 <= 0){//「時間外時間の詳細」．36時間　> 0 の場合　追加
                return '';
            }
            if(timeNo417.numOfYear36Over == 0){//未超過の場合
                return '<br/>' + getText('CMM045_282') + self.convertTime_Short_HM(timeNo417.totalOv) + '　、' + getText('CMM045_283') + getText('CMM045_284', [timeNo417.numOfYear36Over]);
            }
            //超過した場合
            let a1 = getText('CMM045_282') + self.convertTime_Short_HM(timeNo417.totalOv) + '、' + getText('CMM045_283') + getText('CMM045_284', [timeNo417.numOfYear36Over]);
            let lstMonth: Array<number> = [];
            //#102100
            let listMY = _.sortBy(timeNo417.lstOverMonth);
            _.each(listMY, function(month){
                lstMonth.push(month % 100);
            });
//            lstMonth = _.sortBy(lstMonth);
            let a2 = '';
            _.each(lstMonth, function(mon){
                a2 = a2 == '' ? getText('CMM045_285', [mon]) : a2 + '、' + getText('CMM045_285', [mon]);
            });
            return a2 == '' ? '<br/>' + a1 : '<br/>' + a1 + '(' + a2 + ')';
        }
        /**
         * 残業申請
         * kaf005 - appType = 0
         * format data: over time before
         * ※申請モード、承認モード(事前)用レイアウト
         */
        formatOverTimeBf(app: vmbase.ApplicationDto_New, overTime: vmbase.AppOverTimeInfoFull, masterInfo: vmbase.AppMasterInfo): vmbase.DataModeApp {
            let self = this;
            let reason = self.displaySet().appReasonDisAtr == 0 || app.applicationReason == '' ? '' : '<br/>' + app.applicationReason;
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
//            let applicant: string = masterInfo.workplaceName + '<br/>' + empNameFull;
            let applicant: string = masterInfo.workplaceName == '' ? empNameFull : masterInfo.workplaceName + '<br/>' + empNameFull;
            let time1 = overTime.workClockFrom1  == '' ? '' : overTime.workClockFrom1 + getText('CMM045_100') + overTime.workClockTo1;
            let time2 = overTime.workClockFrom2  == '' ? '' : overTime.workClockFrom2 + getText('CMM045_100') + overTime.workClockTo2;
            let contentv4 = time1 + time2;
            let contentv42 = self.convertFrameTime(overTime.lstFrame);
            //No.417
            let timeNo417 = self.displayTimeNo417(overTime.timeNo417);
            let appCt005 = contentv42 + timeNo417 + reason
            let appContent005Bf: string = masterInfo.detailSet == 1 ? contentv4 + appCt005 : appCt005;
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, self.appDateColor(self.convertDateMDW(app.startDate), '',''), appContent005Bf, self.inputDateColor(self.convertDateTime(app.inputDate), ''),
                self.mode() == 0 ? self.convertStatus(app.reflectPerState) : self.convertStatusAppv(app.reflectPerState), masterInfo.phaseStatus,
                masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor,null, app.reflectPerState);
            return a;
        }
        /**
         * convert frame time over time
         */
        convertFrameTime(lstFrame: Array<vmbase.OverTimeFrame>): any {
            let self = this;
            let framName = '';
            let framName11 = '';
            let framName12 = '';
            let time = 0;
            let count = 0;
            let lstSort = self.sortFrameTime(lstFrame, 0);
            _.each(lstSort, function(item) {
                if (item.applicationTime != 0) {//時間外深夜時間
                    if (count < 3) {
                        //ver42
                        framName += '　' + item.name + self.convertTime_Short_HM(item.applicationTime);
                    }
                    time += item.applicationTime;
                    count += 1;
                }
            });
            let other = count > 3 ? count - 3 : 0;
            let otherInfo = other > 0 ? getText('CMM045_231', [other]) : '';
            let result = framName + '　' + otherInfo;
            return result;
        }
        sortFrameTime(lstFrame: Array<vmbase.OverTimeFrame>, appType: number): any {
            let result: Array<vmbase.OverTimeFrame> = [];
            let lstA0: Array<vmbase.OverTimeFrame> = [];
            let lstA1: Array<vmbase.OverTimeFrame> = [];
            let lstA2: Array<vmbase.OverTimeFrame> = [];
            let lstA3: Array<vmbase.OverTimeFrame> = [];
            let lstA4: Array<vmbase.OverTimeFrame> = [];
            _.each(lstFrame, function(obj){
                if(obj.attendanceType == 0){//RESTTIME
                    lstA0.push(obj);
                }
                if(obj.attendanceType == 1){//NORMALOVERTIME
                    lstA1.push(obj);
                }
                if(obj.attendanceType == 2){//BREAKTIME
                    lstA2.push(obj);
                }
                if(obj.attendanceType == 3){//BONUSPAYTIME
                    lstA3.push(obj);
                }
                if(obj.attendanceType == 4){//BONUSSPECIALDAYTIME
                    lstA4.push(obj);
                }
            });
            let sortByA0 =  _.orderBy(lstA0, ["frameNo"], ["asc"]);
            let sortByA1 =  _.orderBy(lstA1, ["frameNo"], ["asc"]);
            let sortByA2 =  _.orderBy(lstA2, ["frameNo"], ["asc"]);
            let sortByA3 =  _.orderBy(lstA3, ["frameNo"], ["asc"]);
            let sortByA4 =  _.orderBy(lstA4, ["frameNo"], ["asc"]);
            if(appType == 0){//overtime
                //push list A0 (休憩時間)
//                _.each(sortByA0, function(obj){
//                    result.push(obj);
//                });
                //push list A1 (残業時間)
                _.each(sortByA1, function(obj){
                    result.push(obj);
                });
                //push list A2 (休出時間)
                _.each(sortByA2, function(obj){
                    result.push(obj);
                });
                //push list A3 (加給時間)
                _.each(sortByA3, function(obj){
                    result.push(obj);
                });
                //push list A4 (特定日加給時間)
                _.each(sortByA4, function(obj){
                    result.push(obj);
                });
            }else{//holiday
               
                //push list A2 (休出時間)
                _.each(sortByA2, function(obj){
                    result.push(obj);
                });
                //push list A1 (残業時間)
                _.each(sortByA1, function(obj){
                    result.push(obj);
                });
                //push list A3 (加給時間)
                _.each(sortByA3, function(obj){
                    result.push(obj);
                });
            }
            return result;
        }
        /**
         * convert frame time over time
         */
        convertFrameTimeHd(lstFrame: Array<vmbase.OverTimeFrame>): any {
            let self = this;
            let framName = '';
            let time = 0;
            let count = 0;
            let lstSort = self.sortFrameTime(lstFrame, 6);
            _.each(lstSort, function(item, index) {
                if (item.applicationTime != 0) {
                    if (count < 3) {
                        framName += '　' + item.name + self.convertTime_Short_HM(item.applicationTime);
                    }
                    time += item.applicationTime;
                    count += 1;
                }
            });
            let other = count > 3 ? count - 3 : 0;
            let otherInfo = other > 0 ? getText('CMM045_231', [other]) : '';
            //#102010
            let result = framName + '　' + otherInfo;
            return result;
        }
        /**
         * find frame by frame no
         */
        findFrameByNo(lstFrame: Array<vmbase.OverTimeFrame>, frameNo: number): any {
            return _.find(lstFrame, function(frame) {
                return frame.frameNo == frameNo;
            });
        }
        /**
         * ※承認モード(事後)用レイアウト
         * format data: over time after
         */
        formatHdWorkAf(app: vmbase.ApplicationDto_New, hdWork: vmbase.AppHolidayWorkFull, masterInfo: vmbase.AppMasterInfo, lstAppGroup: Array<vmbase.AppPrePostGroup>): vmbase.DataModeApp {
            let self = this;
            let contentPre = '';
            let contentResult = '';
            //find don xin truoc, thuc te
            let check: vmbase.AppPrePostGroup = self.findAppPre(lstAppGroup, app.applicationID);
            if (check !== undefined) {
//                if (check.preAppID != '') {
                let prRes = self.findContentPreHd(check);
                contentPre = prRes.appPre == '' ? '' : '<br/>' + prRes.appPre;
                contentResult = prRes.appRes == '' ? '' :'<br/>' + prRes.appRes;
//                }
            }
            //reason application
            let reason = self.displaySet().appReasonDisAtr == 0 || app.applicationReason == '' ? '' : '<br/>' + app.applicationReason;
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
            let applicant: string = masterInfo.workplaceName == '' ? empNameFull : masterInfo.workplaceName + '<br/>' + empNameFull;
            let ca1 = hdWork.startTime1 == '' ? '' : hdWork.startTime1 + getText('CMM045_100') + hdWork.endTime1;
            let ca2 = hdWork.startTime2 == '' ? '' : hdWork.startTime2 + getText('CMM045_100') + hdWork.endTime2;
            let appContentPost: string = getText('CMM045_272') + hdWork.workTypeName + hdWork.workTimeName + ca1 + ca2 + self.convertFrameTimeHd(hdWork.lstFrame);
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            //No.417
            let timeNo417 = self.displayTimeNo417(hdWork.timeNo417);
            let contentFull = '<div class = "appContent-' + app.applicationID + '">'+ appContentPost + contentPre + contentResult + timeNo417 + reason + '</div>';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, self.appDateColor(self.convertDateMDW(app.startDate), '',''), contentFull, self.inputDateColor(self.convertDateTime(app.inputDate), ''),
                self.mode() == 0 ? self.convertStatus(app.reflectPerState) : self.convertStatusAppv(app.reflectPerState), masterInfo.phaseStatus,
                masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor,null, app.reflectPerState);
            return a;
        }

        /**
         * ※承認モード(事後)用レイアウト
         * format data: over time after
         */
        formatOverTimeAf(app: vmbase.ApplicationDto_New, overTime: vmbase.AppOverTimeInfoFull, masterInfo: vmbase.AppMasterInfo, lstAppGroup: Array<vmbase.AppPrePostGroup>): vmbase.DataModeApp {
            let self = this;
            let contentPre = '';
            let contentResult = '';
            //find don xin truoc, thuc te
            let check: vmbase.AppPrePostGroup = self.findAppPre(lstAppGroup, app.applicationID);
            if (check !== undefined) {
                let prRes = self.findContentPreOt(check, masterInfo.detailSet);
                contentPre = prRes.appPre == '' ? '' : '<br/>' + prRes.appPre;
                contentResult = prRes.appRes == '' ? '' :'<br/>' + prRes.appRes;
            }
            let reason = self.displaySet().appReasonDisAtr == 0 || app.applicationReason == '' ? '' : '<br/>' + app.applicationReason;
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
            let applicant: string = masterInfo.workplaceName == '' ? empNameFull : masterInfo.workplaceName + '<br/>' + empNameFull;
            let time1 = overTime.workClockFrom1  == '' ? '' : overTime.workClockFrom1 + getText('CMM045_100') + overTime.workClockTo1;
            let time2 = overTime.workClockFrom2  == '' ? '' : overTime.workClockFrom2 + getText('CMM045_100') + overTime.workClockTo2;
            
            //ver14
            let contentv4 = time1 + time2;
            let contentv42 = self.convertFrameTime(overTime.lstFrame);
            let appContentPost: string = masterInfo.detailSet == 1 ? contentv4 + contentv42 : contentv42;
            
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            //No.417
            let timeNo417 = self.displayTimeNo417(overTime.timeNo417);
            let contentFull = '<div class = "appContent-' + app.applicationID + '">'+ getText('CMM045_272') + appContentPost + contentPre + contentResult + timeNo417 + reason + '</div>';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, self.appDateColor(self.convertDateMDW(app.startDate), '',''), contentFull, self.inputDateColor(self.convertDateTime(app.inputDate), ''),
                self.mode() == 0 ? self.convertStatus(app.reflectPerState) : self.convertStatusAppv(app.reflectPerState), masterInfo.phaseStatus,
                masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor,null, app.reflectPerState);
            return a;
        }
        findAppPre(lstAppGroup: Array<vmbase.AppPrePostGroup>, appId: String): any {
            return _.find(lstAppGroup, function(app) {
                return app.postAppID == appId;
            });
        }
        /**
         * find content pre and result
         * 残業申請 - over time
         */
        findContentPreOt(groups: vmbase.AppPrePostGroup, detailSet: number): any {
            let self = this;
            let appPre = null;
            if(groups.appPre != null){
                let time1 = groups.appPre.workClockFrom1  == '' ? '' : groups.appPre.workClockFrom1 + getText('CMM045_100') + groups.appPre.workClockTo1;
                let time2 = groups.appPre.workClockFrom2  == '' ? '' : groups.appPre.workClockFrom2 + getText('CMM045_100') + groups.appPre.workClockTo2;
                //ver14
//                let reasonOtPre = self.displaySet().appReasonDisAtr == 0 || groups.reasonAppPre == '' ? '' : '<br/>' + groups.reasonAppPre;
                let content1 = time1 + time2;
                let content2 = self.convertFrameTime(groups.appPre.lstFrame);
                appPre = detailSet == 1 ? content1 + content2 : content2;
            }
            let appResContent = '';
            //thuc te
            let timeRes1 = groups.strTime1  == '' ? '' : groups.strTime1 + getText('CMM045_100') + groups.endTime1;
            let timeRes2 = groups.strTime2  == '' ? '' : groups.strTime2 + getText('CMM045_100') + groups.endTime2;
            //ver14
            let contentRes1 =  timeRes1 + timeRes2;
            let contentRes2 = self.convertFrameTime(groups.lstFrameRes);
            let appRes = detailSet == 1 ? contentRes1 + contentRes2 : contentRes2;
            appResContent = getText('CMM045_274') + appRes;
            let appInfor = {
                appPre: appPre == null ? '' : getText('CMM045_273') + appPre,
                appRes: groups.lstFrameRes == null || groups.lstFrameRes.length == 0 ? '' : appResContent
            }
            return appInfor;
        }
        
        /**
         * find content pre and result
         * 休日出勤時間申請 - holiday work
         * TO DO
         */
        findContentPreHd(groups: vmbase.AppPrePostGroup): any {
            let self = this;
            let appPre = null;
            if(groups.appPreHd != null){
                let ca1 = groups.appPreHd.startTime1 == '' ? '' : groups.appPreHd.startTime1 + getText('CMM045_100') + groups.appPreHd.endTime1;
                let ca2 = groups.appPreHd.startTime2 == '' ? '' : groups.appPreHd.startTime2 + getText('CMM045_100') + groups.appPreHd.endTime2;
                appPre = groups.appPreHd.workTypeName + groups.appPreHd.workTimeName + ca1 + ca2 + self.convertFrameTimeHd(groups.appPreHd.lstFrame);
            }
            let appResContent = '';
            //thuc te
            let appRes = self.convertFrameTimeHd(groups.lstFrameRes);
            appResContent = getText('CMM045_274') + appRes;


            let appInfor = {
                appPre: appPre == null ? '' : getText('CMM045_273') + appPre,
                appRes: groups.lstFrameRes == null || groups.lstFrameRes.length == 0 ? '' : appResContent
            }
            return appInfor;
        }
        findCommon(lstAppCommon: Array<vmbase.ApplicationDto_New>, appId: string): any {
            return _.find(lstAppCommon, function(app) {
                return app.applicationID == appId;
            });
        }
        /**
         * 直行直帰申請
         * kaf009 - appType = 4
         */
        formatGoBack(app: vmbase.ApplicationDto_New, goBack: vmbase.AppGoBackInfoFull, masterInfo: vmbase.AppMasterInfo): vmbase.DataModeApp {
            let self = this;
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
            let applicant: string = masterInfo.workplaceName == '' ? empNameFull : masterInfo.workplaceName + '<br/>' + empNameFull;
            let go1 = goBack.goWorkAtr1 == 1 ?  getText('CMM045_259') + goBack.workTimeStart1 : '';
            let back1 = goBack.backHomeAtr1 == 1 ? getText('CMM045_260') + goBack.workTimeEnd1 : '';
            let go2 = goBack.goWorkAtr2 == 1 ? getText('CMM045_259') + goBack.workTimeStart2 : '';
            let back2 = goBack.backHomeAtr2 == 1 ? getText('CMM045_260') + goBack.workTimeEnd2 : '';
            let reason = self.displaySet().appReasonDisAtr == 0 || app.applicationReason == '' ? '' : '<br/>' + app.applicationReason;
            let appContent2222 = go1 + back1 + go2 + back2 + reason;
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, self.appDateColor(self.convertDateMDW(app.startDate), '',''), appContent2222, self.inputDateColor(self.convertDateTime(app.inputDate), ''),
                self.mode() == 0 ? self.convertStatus(app.reflectPerState) : self.convertStatusAppv(app.reflectPerState), masterInfo.phaseStatus,
                masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor,null, app.reflectPerState);
            return a;
        }
        /**
         * 勤務変更申請
         * kaf007 - appType = 2
         */
        formatWorkChange(app: vmbase.ApplicationDto_New, wkChange: vmbase.AppWorkChangeFull, masterInfo: vmbase.AppMasterInfo): vmbase.DataModeApp {
            let self = this;
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
            let applicant: string = masterInfo.workplaceName == '' ? empNameFull : masterInfo.workplaceName + '<br/>' + empNameFull;
            let go1 = wkChange.goWorkAtr1 == 0 ? '' + getText('CMM045_252') + wkChange.workTimeStart1 : wkChange.workTimeStart1;
            let back1 = wkChange.backHomeAtr1 == 0 ? getText('CMM045_252') + wkChange.workTimeEnd1 : wkChange.workTimeEnd1;
            let time1 = go1 == '' ? '' : go1 + getText('CMM045_100') + back1;
            let go2 = (wkChange.goWorkAtr2 == 1 || wkChange.goWorkAtr2 == null) ? wkChange.workTimeStart2 : '' + getText('CMM045_252') + wkChange.workTimeStart2;
            let back2 = (wkChange.backHomeAtr2 == 1 || wkChange.backHomeAtr2 == null) ? wkChange.workTimeEnd2 : getText('CMM045_252') + wkChange.workTimeEnd2;
            let time2 = go2 == '' ? '' : go2 + getText('CMM045_100') + back2;
            let breakTime = wkChange.breakTimeStart1 == '' ? '' : getText('CMM045_251') + wkChange.breakTimeStart1 + getText('CMM045_100') + wkChange.breakTimeEnd1;
            let reason = self.displaySet().appReasonDisAtr == 1 ? '<br/>' + app.applicationReason : '';
            let appContent007 = wkChange.workTypeName + wkChange.workTimeName + time1 + time2 + breakTime + reason;
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;
            let dateRange = app.startDate == app.endDate ? self.appDateColor(self.convertDateMDW(app.applicationDate), '','') : self.appDateRangeColor(self.convertDateMDW(app.startDate), self.convertDateMDW(app.endDate));
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, dateRange, appContent007, self.inputDateColor(self.convertDateTime(app.inputDate), ''),
                self.mode() == 0 ? self.convertStatus(app.reflectPerState) : self.convertStatusAppv(app.reflectPerState), masterInfo.phaseStatus,
                masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor,null, app.reflectPerState);
            return a;
        }
        /**
         * 休暇申請
         * kaf006 - appType = 1
         * DOING
         */
        formatAbsence(app: vmbase.ApplicationDto_New, absence: vmbase.AppAbsenceFull, masterInfo: vmbase.AppMasterInfo): vmbase.DataModeApp {
            let self = this;
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
            let applicant: string = masterInfo.workplaceName == '' ? empNameFull : masterInfo.workplaceName + '<br/>' + empNameFull;
            let reason = self.displaySet().appReasonDisAtr == 1 ? '<br/>' + app.applicationReason : '';
            let appContent006 = '';
            //ver46
            if(absence.holidayAppType != 3){//休暇申請.休暇種類　≠ 特別休暇
                appContent006 = self.convertAbsenceNotSpecial(absence);
            }
            
            if(absence.holidayAppType == 3){//休暇申請.休暇種類　＝ 特別休暇 ver39
                appContent006 = self.convertAbsenceSpecial(absence);
            }
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;
            let dateRange = app.startDate == app.endDate ? self.appDateColor(self.convertDateMDW(app.applicationDate), '','') : self.appDateRangeColor(self.convertDateMDW(app.startDate), self.convertDateMDW(app.endDate));
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, dateRange, appContent006 + reason, self.inputDateColor(self.convertDateTime(app.inputDate), ''),
                self.mode() == 0 ? self.convertStatus(app.reflectPerState) : self.convertStatusAppv(app.reflectPerState), masterInfo.phaseStatus,
                masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor, null, app.reflectPerState);
            return a;
        }
        //休暇申請.休暇種類　≠ 特別休暇 ver46
        convertAbsenceNotSpecial(absence: vmbase.AppAbsenceFull): string{
            return absence.workTypeName;
        }
        //※休暇申請.休暇種類　＝ 特別休暇 ver39
        convertAbsenceSpecial(absence: vmbase.AppAbsenceFull): string{
            let self = this;
            let day = absence.mournerFlag == true ? getText('CMM045_277') + absence.day + getText('CMM045_278') : absence.day + getText('CMM045_278');
            let result = absence.workTypeName + absence.relationshipName + day;
            return result;
        }
        //ver46
        //※休暇申請.終日半日休暇区分　＝　半日休暇
//        convertAbsenceHalfDay(absence: vmbase.AppAbsenceFull): string{
//            let self = this;
//            let time1 = absence.startTime1 == '' ? '' : absence.startTime1 + getText('CMM045_100') +  absence.endTime1;
//            let time2 =  absence.startTime2 == '' ? '' : absence.startTime2 + getText('CMM045_100') + absence.endTime2;
//            let result = getText('CMM045_249') + getText('CMM045_230', [self.convertNameHoliday(absence.holidayAppType)])  + time1 + time2;
//            return result;
//        }
        convertNameHoliday(holidayType: number): string{
            let self = this;
            switch(holidayType){
                case 0:// 年休名称 - 0
                    return self.hdAppSet().yearHdName;
                case 1:// 代表者名 - 1
                    return self.hdAppSet().obstacleName;
                case 2:// 欠勤名称 - 2
                    return self.hdAppSet().absenteeism;
                case 3:// 特別休暇名称 - 3
                    return self.hdAppSet().specialVaca;
                case 4:// 積立年休名称  - 4
                    return self.hdAppSet().yearResig;
                case 5:// 休日名称 - 5
                    return self.hdAppSet().hdName;
                case 6:// 時間消化名称 - 6
                    return self.hdAppSet().timeDigest;
                case 7:// 振休名称 - 7
                    return self.hdAppSet().furikyuName;
                default:
                    return "";
            }
        }
        /**
         * 振休振出申請
         * kaf011 - appType = 10
         * TO DO
         */
        formatCompltLeave(app: vmbase.ApplicationDto_New, complt: vmbase.AppCompltLeaveSync, masterInfo: vmbase.AppMasterInfo): vmbase.DataModeApp{
            let self = this;
            let reason = self.displaySet().appReasonDisAtr == 1 ? '<br/>' + app.applicationReason : '';
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
            let applicant: string = masterInfo.workplaceName == '' ? empNameFull : masterInfo.workplaceName + '<br/>' + empNameFull;
            
            //振出 rec typeApp = 1
            //振休 abs typeApp = 0
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let content010 = '';
            //振出 rec typeApp = 1
            //振休 abs typeApp = 0
            if(complt.typeApp == 0){
                content010 = self.convertB(complt.appMain, app.applicationDate, app.applicationReason);
            }else{
                content010 = self.convertA(complt.appMain, app.applicationDate, app.applicationReason);
            }
            let appDate = self.appDateColor(self.convertDateMDW(app.applicationDate), '','');
            let inputDate = self.inputDateColor(self.convertDateTime(app.inputDate), '');
            
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePost, appDate, content010, inputDate,
                self.mode() == 0 ? self.convertStatus(app.reflectPerState) : self.convertStatusAppv(app.reflectPerState), masterInfo.phaseStatus,
                masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor, null, app.reflectPerState);
            return a; 
        }
        /**
         * 振休振出申請
         * 同期
         * kaf011 - appType = 10
         * TO DO
         */
        formatCompltSync(app: vmbase.ApplicationDto_New, complt: vmbase.AppCompltLeaveSync, masterInfo: vmbase.AppMasterInfo): vmbase.DataModeApp{
            let self = this;
            let reason = self.displaySet().appReasonDisAtr == 1 ? '<br/>' + app.applicationReason : '';
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
            let applicant: string = masterInfo.workplaceName == '' ? empNameFull : masterInfo.workplaceName + '<br/>' + empNameFull;
            
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let content010 = '';
            content010 = self.convertC(app, complt);
            
            let appDateAbs = '';
            let appDateRec = '';
            let inputDateAbs = '';
            let inputDateRec = '';
            //振出 rec typeApp = 1
            //振休 abs typeApp = 0
            if(complt.typeApp == 0){
                appDateAbs = self.appDateColor(self.convertDateMDW(app.applicationDate), 'abs','');
                appDateRec = self.appDateColor(self.convertDateMDW(complt.appDateSub), 'rec','');
                inputDateAbs = self.inputDateColor(self.convertDateTime(app.inputDate), 'abs');
                inputDateRec = self.inputDateColor(self.convertDateTime(complt.appInputSub), 'rec');
            }else{
                appDateRec = self.appDateColor(self.convertDateMDW(app.applicationDate), 'rec','');
                appDateAbs = self.appDateColor(self.convertDateMDW(complt.appDateSub), 'abs','');
                inputDateRec = self.inputDateColor(self.convertDateTime(app.inputDate), 'rec');
                inputDateAbs = self.inputDateColor(self.convertDateTime(complt.appInputSub), 'abs');
            }
            let appStatus = self.mode() == 0 ? self.convertStatus(app.reflectPerState) : self.convertStatusAppv(app.reflectPerState);
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant, masterInfo.dispName,
                prePost, appDateRec + '<br/>' + appDateAbs, content010,
                inputDateRec + '<br/>' + inputDateAbs, '<div class = "rec" >' + appStatus + '</div>' + '<br/>' + '<div class = "abs" >' + appStatus + '</div>',
                masterInfo.phaseStatus, masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor, complt.appSub.appID, app.reflectPerState);
            return a;
        }
        inputDateColor_Old(input: string, classApp: string): string{
            let inputDate = '<div class = "' + classApp + '" >' + input + '</div>';
            //fill color text input date
            let colorIn = input.substring(11,12);
            if (colorIn == '土') {//土
                inputDate = '<div class = "saturdayCell ' + classApp + '" >' + input + '</div>';
            }
            if (colorIn == '日') {//日
                inputDate = '<div class = "sundayCell ' + classApp + '" >' + input + '</div>';
            }
            return inputDate;
        }
        //ver41
        inputDateColor(input: string, classApp: string): string{
            let inputDate = '<div class = "' + classApp + '" >' + input + '</div>';
            //fill color text input date
            let a = input.split("(")[1];
            let colorIn = a.substring(0,1);
            if (colorIn == '土') {//土
                inputDate = '<div class = "saturdayCell ' + classApp + '" >' + input + '</div>';
            }
            if (colorIn == '日') {//日
                inputDate = '<div class = "sundayCell ' + classApp + '" >' + input + '</div>';
            }
            return inputDate;
        }
        appDateColor(date: string, classApp: string, priod: string): string{
            let appDate = '<div class = "' + classApp + '" >' + date + priod + '</div>';;
            //color text appDate
            let a = date.split("(")[1];
            let color = a.substring(0,1);
            if (color == '土') {//土
                appDate = '<div class = "saturdayCell  ' + classApp + '" >' + date + priod +'</div>';
            }
            if (color == '日') {//日 
                appDate = '<div class = "sundayCell  ' + classApp + '" >' + date + priod + '</div>';
            }
            return appDate;
        }
        //doi ung theo y amid-mizutani さん
        appDateRangeColor(startDate: string, endDate: string): string{
            let sDate = '<div class = "dateRange" >' + startDate + '</div>';;
            let eDate =  '<div class = "dateRange" >' + endDate + '</div>';
            //color text appDate
            let a = startDate.split("(")[1];
            let b = endDate.split("(")[1];
            let color1 = a.substring(0,1);
            if (color1 == '土') {//土
                sDate = '<div class = "saturdayCell  dateRange" >' + startDate +  '</div>';
            }
            if (color1 == '日') {//日 
                sDate = '<div class = "sundayCell  dateRange" >' + startDate + '</div>';
            }
            let color2 = b.substring(0,1);
            if (color2 == '土') {//土
                eDate = '<div class = "saturdayCell  dateRange" >' + endDate + '</div>';
            }
            if (color2 == '日') {//日 
                eDate = '<div class = "sundayCell  dateRange" >' + endDate +  '</div>';
            }
            return sDate + '<div class = "dateRange" >' + '－' +  '</div>' +  eDate;
        }
        //※振出申請のみ同期なし・紐付けなし
        //申請/承認モード
        //申請日付(A6_C2_6)、入力日(A6_C2_8)、承認状況(A6_C2_9)の表示はない（１段）
        convertA(compltLeave: vmbase.AppCompltLeaveFull, date: string, reason: string){
            let self = this;
            let time = compltLeave.startTime + getText('CMM045_100') + compltLeave.endTime;
            let reasonApp = self.displaySet().appReasonDisAtr == 1 ? '<br/>' + reason : '';
            return getText('CMM045_262') + self.convertDateShort_MD(date) + getText('CMM045_230', [compltLeave.workTypeName]) + time + reasonApp;
        }
        //※振休申請のみ同期なし・紐付けなし
        //申請/承認モード
        //申請日付(A6_C2_6)、入力日(A6_C2_8)、承認状況(A6_C2_9)の表示はない（１段）
        convertB(compltLeave: vmbase.AppCompltLeaveFull, date: string, reason: string){
            let self = this;
            let eTime = compltLeave.endTime == '' ? '' : getText('CMM045_100') + compltLeave.endTime;
            let time = compltLeave.startTime + eTime;
            let reasonApp = self.displaySet().appReasonDisAtr == 1 ? '<br/>' + reason : '';
            return getText('CMM045_263') + self.convertDateShort_MD(date) + getText('CMM045_230', [compltLeave.workTypeName]) + time + reasonApp;
        }
        //※振休振出申請　同期（あり/なし）・紐付けあり
        //申請モード/承認モード merge convert C + D
        //申請日付(A6_C2_6)、入力日(A6_C2_8)、承認状況(A6_C2_9)表示（２段）
        //振出(rec) -> 振休(abs)
        convertC(app: vmbase.ApplicationDto_New, compltSync: vmbase.AppCompltLeaveSync){
            let self = this;
            let abs = null;
            let rec = null;
            let recContent = '';
            let absContent = '';
            if(compltSync.typeApp == 0){
                abs = compltSync.appMain;
                rec = compltSync.appSub;
                recContent = self.convertA(rec, compltSync.appDateSub, '');
                absContent = self.convertB(abs, app.applicationDate, app.applicationReason);
                
            }else{
                rec = compltSync.appMain;
                abs = compltSync.appSub;
                absContent = self.convertB(abs, compltSync.appDateSub, '');
                recContent = self.convertA(rec, app.applicationDate, app.applicationReason);
            }
            return  '<div class = "rec" >' + recContent + '</div>' + '<div class = "abs" >' + absContent + '</div>';
        }
        //※振休振出申請　同期あり・紐付けあり
        //承認モード
        //申請日付(A6_C2_6)、入力日(A6_C2_8)、承認状況(A6_C2_9)の表示はない（１段）
        //※同じ承認状態
//        convertD(compltLeave: vmbase.AppCompltLeaveFull, date: string, reason: string){
//            let time = compltLeave.startTime + getText('CMM045_262') + compltLeave.endTime;
//            
//        }
        //※振休振出申請　同期なし・紐付けあり
        //承認モード（振出の場合）
        //申請日付(A6_C2_6)、入力日(A6_C2_8)、承認状況(A6_C2_9)の表示はない（１段）
//        convertE(compltLeave: vmbase.AppCompltLeaveFull, date: string, reason: string){
//            let time = compltLeave.startTime + getText('CMM045_262') + compltLeave.endTime;
//        }
        //※振休振出申請　同期なし・紐付けあり
        //承認モード（振休の場合）
        //申請日付(A6_C2_6)、入力日(A6_C2_8)、承認状況(A6_C2_9)の表示はない（１段）
//        convertF(compltLeave: vmbase.AppCompltLeaveFull, date: string, reason: string){
//            let time = compltLeave.startTime + getText('CMM045_262') + compltLeave.endTime;
//        }
        /**
         * map data -> fill in grid list
         */
        mapData(lstApp: Array<vmbase.ApplicationDto_New>, lstMaster: Array<vmbase.AppMasterInfo>, lstGoBack: Array<vmbase.AppGoBackInfoFull>,
            lstOverTime: Array<vmbase.AppOverTimeInfoFull>, lstAppGroup: Array<vmbase.AppPrePostGroup>, lstHdWork: Array<vmbase.AppHolidayWorkFull>,
            lstWorkChange: Array<vmbase.AppWorkChangeFull>, lstAppAbsence: Array<vmbase.AppAbsenceFull>,
            lstCompltLeave: Array<vmbase.AppCompltLeaveSync>): Array<vmbase.DataModeApp> {
            let self = this;
            let lstData: Array<vmbase.DataModeApp> = [];
            _.each(lstApp, function(app: vmbase.ApplicationDto_New) {
                let masterInfo = self.findMasterInfo(lstMaster, app.applicationID);
                let data: vmbase.DataModeApp;
                if (app.applicationType == 0) {//over time
                    let overtTime = self.findOverTimeById(app.applicationID, lstOverTime);
                    if (self.mode() == 1 && app.prePostAtr == 1) {
                        data = self.formatOverTimeAf(app, overtTime, masterInfo, lstAppGroup);
                    } else {
                        data = self.formatOverTimeBf(app, overtTime, masterInfo);
                    }
                }
                if (app.applicationType == 4) {//goback
                    let goBack = self.findGoBack(app.applicationID, lstGoBack);
                    data = self.formatGoBack(app, goBack, masterInfo);
                }
                if(app.applicationType == 6){//holiday work
                    let hdWork = self.findHdWork(app.applicationID, lstHdWork);
                    if(self.mode() == 1 && app.prePostAtr == 1){
                        data = self.formatHdWorkAf(app, hdWork, masterInfo, lstAppGroup);
                    }else{
                        data = self.formatHdWorkBf(app, hdWork, masterInfo);
                    }
                }
                if(app.applicationType == 2){//work change
                    let wkChange = self.findWorkChange(app.applicationID, lstWorkChange);
                    data = self.formatWorkChange(app, wkChange, masterInfo);
                }
                if(app.applicationType == 1){//absence
                    let absence = self.findAbsence(app.applicationID, lstAppAbsence);
                    data = self.formatAbsence(app, absence, masterInfo);
                }
                if(app.applicationType == 10){//Complement Leave
                    let complt = self.findCompltLeave(app.applicationID, lstCompltLeave);
                    if(complt.sync){
                        data = self.formatCompltSync(app, complt, masterInfo);
                    }else{
                        data = self.formatCompltLeave(app, complt, masterInfo);
                    }
                }
                lstData.push(data);
            });
            return lstData;
        }
        /**
         * find application holiday work by id
         */
        findCompltLeave(appId: string, lstCompltLeave: Array<vmbase.AppCompltLeaveSync>):vmbase.AppCompltLeaveSync{
            return _.find(lstCompltLeave, function(complt){
                return complt.appMain.appID == appId;
        });
        }
        /**
         * find application holiday work by id
         */
        findHdWork(appId: string, lstHdWork: Array<vmbase.AppHolidayWorkFull>){
            return _.find(lstHdWork, function(hdWork) {
                return hdWork.appId == appId;
            });
        }
        /**
         * find application work change by id
         */
        findWorkChange(appId: string, lstWorkChange: Array<vmbase.AppWorkChangeFull>){
            return _.find(lstWorkChange, function(workChange) {
                return workChange.appId == appId;
            });
        }
        /**
         * find application work change by id
         */
        findAbsence(appId: string, lstAppAbsence: Array<vmbase.AppAbsenceFull>){
            return _.find(lstAppAbsence, function(absence) {
                return absence.appID == appId;
            });
        }
        /**
         * find application over time by id
         */
        findOverTimeById(appID: string, lstOverTime: Array<vmbase.AppOverTimeInfoFull>) {
            return _.find(lstOverTime, function(master) {
                return master.appID == appID;
            });
        }
        /**
         * find application go back by id
         */
        findGoBack(appID: string, lstGoBack: Array<vmbase.AppGoBackInfoFull>) {
            return _.find(lstGoBack, function(master) {
                return master.appID == appID;
            });
        }
        /**
         * find master info by id
         */
        findMasterInfo(lstMaster: Array<vmbase.AppMasterInfo>, appId: string) {
            return _.find(lstMaster, function(master) {
                return master.appID == appId;
            });
        }
        /**
         * convert status from number to string
         */
        convertStatus(status: number): string {
            switch (status) {
                case 0:
                    return getText('CMM045_62');//下書き保存/未反映　=　未
                case 1:
                    return getText('CMM045_63');//反映待ち　＝　承認済み
                case 2:
                    return getText('CMM045_64');//反映済　＝　反映済み
                case 5:
                    return getText('CMM045_66');//差し戻し　＝　差戻
                case 6:
                    return getText('CMM045_65');//否認　=　否
                default:
                    return getText('CMM045_67');//取消待ち/取消済　＝　取消
            }
        }
        //UNAPPROVED:5
        //APPROVED: 4
        //CANCELED: 3
        //REMAND: 2
        //DENIAL: 1
        //-: 0
        convertStatusAppv(status: number): string {
            switch (status) {
                case 1:  //DENIAL: 1
                    return getText('CMM045_65');
                case 2: //REMAND: 2
                    return getText('CMM045_66');
                case 3: //CANCELED: 3
                    return getText('CMM045_67');
                case 4: //APPROVED: 4
                    return getText('CMM045_63');
                case 5: //UNAPPROVED:5
                    return getText('CMM045_62');
                default: //-: 0
                    return '-';
            }
        }
        //yyyy/MM/dd(W)
        convertDate(date: string) {
            let a: number = moment(date, 'YYYY/MM/DD').isoWeekday();
            switch (a) {
                case 1://Mon
                    return date + '(月)';
                case 2://Tue
                    return date + '(火)';
                case 3://Wed
                    return date + '(水)';
                case 4://Thu
                    return date + '(木)';
                case 5://Fri
                    return date + '(金)';
                case 6://Sat
                    return date + '(土)';
                default://Sun
                    return date + '(日)';
            }
        }
        //MM/dd(W) ver24
        convertDateMDW(date: string) {
            let a: number = moment(date, 'YYYY/MM/DD').isoWeekday();
            let toDate = moment(date, 'YYYY/MM/DD').toDate();
            let dateMDW = (toDate.getMonth()+1) + '/'+ toDate.getDate();
            switch (a) {
                case 1://Mon
                    return dateMDW + '(月)';
                case 2://Tue
                    return dateMDW + '(火)';
                case 3://Wed
                    return dateMDW + '(水)';
                case 4://Thu
                    return dateMDW + '(木)';
                case 5://Fri
                    return dateMDW + '(金)';
                case 6://Sat
                    return dateMDW + '(土)';
                default://Sun
                    return dateMDW + '(日)';
            }
        }
        //Short_MD
        convertDateShort_MD(date: string) {
            let a: number = moment(date, 'YYYY/MM/DD').isoWeekday();
            let toDate = moment(date, 'YYYY/MM/DD').toDate();
            let dateMDW = (toDate.getMonth()+1) + '/'+ toDate.getDate();
            return dateMDW;
        }
        //yyyy/MM/dd(W) hh:mm
        convertDateTime_Old(dateTime: string) {
            let a: number = moment(dateTime, 'YYYY/MM/DD hh:mm').isoWeekday();
            let date = dateTime.split(" ")[0];
            let time = dateTime.split(" ")[1];
            return this.convertDate(date) + ' ' + time;
        }
        //ver41
        //Short_MDW  hh:mm : MM/dd(W) hh:mm
        convertDateTime(dateTime: string) {
            let a: number = moment(dateTime, 'YYYY/MM/DD hh:mm').isoWeekday();
            let date = dateTime.split(" ")[0];
            let time = dateTime.split(" ")[1];
            return this.convertDateMDW(date) + ' ' + time;
        }
        /**
         * when click button 検索
         */
        filter() {
            block.invisible();
            if (nts.uk.ui.errors.hasError()) {
                block.clear();
                return;
            }
            let self = this;
            //check filter
            //check startDate
            if (self.dateValue().startDate == null || self.dateValue().startDate == '') {//期間開始日付または期間終了日付が入力されていない
                $('.ntsDatepicker.nts-input.ntsStartDatePicker.ntsDateRange_Component').ntsError('set', {messageId:"Msg_359"});
                block.clear();
                return;
            }
            //check endDate
            if (self.dateValue().endDate == null || self.dateValue().endDate == '') {//期間開始日付または期間終了日付が入力されていない
                $('.ntsDatepicker.nts-input.ntsEndDatePicker.ntsDateRange_Component').ntsError('set', {messageId:"Msg_359"});
                block.clear();
                return;
            }
            if (self.mode() == 1 && self.selectedIds().length == 0) {//承認状況のチェックの確認
                nts.uk.ui.dialog.error({ messageId: "Msg_360" });
                block.clear();
                return;
            }
            let condition: vmbase.AppListExtractConditionDto = new vmbase.AppListExtractConditionDto(self.dateValue().startDate, self.dateValue().endDate, self.mode(),
                self.selectedCode(), self.findcheck(self.selectedIds(), 1), self.findcheck(self.selectedIds(), 2), self.findcheck(self.selectedIds(), 3),
                self.findcheck(self.selectedIds(), 4), self.findcheck(self.selectedIds(), 5), self.findcheck(self.selectedIds(), 6), 0, self.lstSidFilter(), '');
            let param = new vmbase.AppListParamFilter(condition, false, 0);
            service.getApplicationList(param).done(function(data) {
                console.log(data);
                //reset data
                self.lstAppCommon([]);
                self.lstAppMaster([]);
                self.lstAppOt([]);
                self.lstApp([]);
                self.lstAppGoBack([]);
                self.lstListAgent([]);
                self.lstAppHdWork([]);
                self.lstAppWorkChange([]);
                self.lstAppAbsence([]);
                self.lstAppCompltSync([]);
                //luu
                character.save('AppListExtractCondition', condition);
                let lstGoBack: Array<vmbase.AppGoBackInfoFull> = [];
                let lstAppGroup: Array<vmbase.AppPrePostGroup> = [];
                self.displaySet(new vmbase.ApprovalListDisplaySetDto(data.displaySet.advanceExcessMessDisAtr,
                    data.displaySet.hwAdvanceDisAtr, data.displaySet.hwActualDisAtr,
                    data.displaySet.actualExcessMessDisAtr, data.displaySet.otAdvanceDisAtr,
                    data.displaySet.otActualDisAtr, data.displaySet.warningDateDisAtr, data.displaySet.appReasonDisAtr));
                _.each(data.lstApp, function(app) {
                    self.lstAppCommon.push(new vmbase.ApplicationDto_New(app.applicationID, app.prePostAtr, app.inputDate, app.enteredPersonSID,
                        app.reversionReason, app.applicationDate, app.applicationReason, app.applicationType, app.applicantSID,
                        app.reflectPlanScheReason, app.reflectPlanTime, app.reflectPlanState, app.reflectPlanEnforce,
                        app.reflectPerScheReason, app.reflectPerTime, app.reflectPerState, app.reflectPerEnforce,
                        app.startDate, app.endDate, app.version));
                });
                _.each(data.lstMasterInfo, function(master) {
                    self.lstAppMaster.push(new vmbase.AppMasterInfo(master.appID, master.appType, master.dispName, master.empName, master.inpEmpName, master.workplaceName,
                        master.statusFrameAtr, master.phaseStatus, master.checkAddNote, master.checkTimecolor, master.detailSet));
                });
                _.each(data.lstAppGoBack, function(goback) {
                    lstGoBack.push(new vmbase.AppGoBackInfoFull(goback.appID, goback.goWorkAtr1, goback.workTimeStart1,
                        goback.backHomeAtr1, goback.workTimeEnd1, goback.goWorkAtr2, goback.workTimeStart2, goback.backHomeAtr2, goback.workTimeEnd2));
                });
                _.each(data.lstAppOt, function(overTime) {
                    let lstFrame: Array<vmbase.OverTimeFrame> = []
                    _.each(overTime.lstFrame, function(frame) {
                        lstFrame.push(new vmbase.OverTimeFrame(frame.attendanceType, frame.frameNo, frame.name,
                            frame.timeItemTypeAtr, frame.applicationTime));
                    });
                    let timeNo417 = overTime.timeNo417 == null ? null : 
                            new vmbase.TimeNo417(overTime.timeNo417.totalOv, overTime.timeNo417.time36, overTime.timeNo417.numOfYear36Over, overTime.timeNo417.lstOverMonth);
                    self.lstAppOt.push(new vmbase.AppOverTimeInfoFull(overTime.appID, overTime.workClockFrom1, overTime.workClockTo1, overTime.workClockFrom2,
                        overTime.workClockTo2, overTime.total, lstFrame, overTime.overTimeShiftNight, overTime.flexExessTime, timeNo417));
                });
                _.each(data.lstAppGroup, function(group) {
                    lstAppGroup.push(new vmbase.AppPrePostGroup(group.preAppID, group.postAppID, group.time,group.strTime1,group.endTime1,group.strTime2,group.endTime2, group.appPre, group.reasonAppPre, group.appPreHd));
                });
                self.itemApplication([]);
                self.itemApplication.push(new vmbase.ChoseApplicationList(-1, '全件表示'));
                _.each(data.lstAppInfor, function(appInfo){
                    self.itemApplication.push(new vmbase.ChoseApplicationList(appInfo.appType, appInfo.appName));                          
                });
                self.lstListAgent([]);
                _.each(data.lstAgent, function(agent){
                    self.lstListAgent.push(new vmbase.ApproveAgent(agent.appID, agent.agentId));
                });
                _.each(data.lstAppHdWork, function(hdwork) {
                    let lstFrame: Array<vmbase.OverTimeFrame> = []
                    _.each(hdwork.lstFrame, function(frame) {
                        lstFrame.push(new vmbase.OverTimeFrame(frame.attendanceType, frame.frameNo, frame.name,
                            frame.timeItemTypeAtr, frame.applicationTime));
                    });
                    let timeNo417 = hdwork.timeNo417 == null ? null : 
                            new vmbase.TimeNo417(hdwork.timeNo417.totalOv, hdwork.timeNo417.time36, hdwork.timeNo417.numOfYear36Over, hdwork.timeNo417.lstOverMonth);
                    self.lstAppHdWork.push(new vmbase.AppHolidayWorkFull(hdwork.appId, hdwork.workTypeName, hdwork.workTimeName, hdwork.startTime1, 
                        hdwork.endTime1, hdwork.startTime2, hdwork.endTime2 ,lstFrame, timeNo417));
                });
                _.each(data.lstAppWorkChange, function(wkChange) {
                    self.lstAppWorkChange.push(new vmbase.AppWorkChangeFull(wkChange.appId, wkChange.workTypeName, wkChange.workTimeName,
                        wkChange.goWorkAtr1, wkChange.workTimeStart1, wkChange.backHomeAtr1, wkChange.workTimeEnd1, wkChange.goWorkAtr2,
                        wkChange.workTimeStart2, wkChange.backHomeAtr2, wkChange.workTimeEnd2, wkChange.breakTimeStart1, wkChange.breakTimeEnd1));
                });
                _.each(data.lstAppAbsence, function(absence) {
                    self.lstAppAbsence.push(new vmbase.AppAbsenceFull(absence.appID, absence.holidayAppType, absence.day, absence.workTimeName,
                        absence.allDayHalfDayLeaveAtr, absence.startTime1, absence.endTime1, absence.startTime2,
                        absence.endTime2, absence.relationshipCode, absence.relationshipName, absence.mournerFlag, absence.workTypeName));
                });
                _.each(data.lstAppCompltLeaveSync, function(complt){
                    let appMain = new vmbase.AppCompltLeaveFull(complt.appMain.appID, complt.appMain.workTypeName, complt.appMain.startTime, complt.appMain.endTime);
                    let appSub = complt.appSub == null ? null : new vmbase.AppCompltLeaveFull(complt.appSub.appID, complt.appSub.workTypeName, complt.appSub.startTime, complt.appSub.endTime);
                    self.lstAppCompltSync.push(new vmbase.AppCompltLeaveSync(complt.typeApp, complt.sync, appMain, appSub, complt.appDateSub, complt.appInputSub));
                });
                let lstData = self.mapData(self.lstAppCommon(), self.lstAppMaster(), lstGoBack, self.lstAppOt(), 
                    lstAppGroup, self.lstAppHdWork(), self.lstAppWorkChange(), self.lstAppAbsence(), self.lstAppCompltSync());
                self.lstApp(lstData);
                //check list app new exist selectedCode???
                let check = self.findExist();
                if(check === undefined){
                    self.selectedCode(-1);
                }
                if (self.selectedCode() != -1) {
                    self.filterByAppType(self.selectedCode());
                } else {
                    self.items(lstData);
                    //mode approval - count
                    if (data.appStatusCount != null) {
                        self.approvalCount(new vmbase.ApplicationStatus(data.appStatusCount.unApprovalNumber, data.appStatusCount.approvalNumber,
                            data.appStatusCount.approvalAgentNumber, data.appStatusCount.cancelNumber, data.appStatusCount.remandNumner,
                            data.appStatusCount.denialNumber));
                    }
                    
                    if (self.mode() == 1) {
                        $("#grid1").ntsGrid("destroy");
                        let colorBackGr = self.fillColorbackGrAppr();
                        let lstHidden: Array<any> = self.findRowHidden(self.items());
                        self.reloadGridApproval(lstHidden,colorBackGr, self.isHidden());
                    } else {
                        let colorBackGr = self.fillColorbackGr();
                        $("#grid2").ntsGrid("destroy");
                        self.reloadGridApplicaion(colorBackGr, self.isHidden());
                    }
                }
                block.clear();
            }).fail(() => {
                block.clear();
            });
        }
        findExist(): any{
            let self = this;
            return _.find(self.itemApplication(), function(item){
                return item.appId == self.selectedCode();
            });
        }
        /**
         * find row hidden
         */
        findRowHidden(lstItem: Array<vmbase.DataModeApp>): any{
            let lstHidden = []
            _.each(lstItem, function(item){
                if(item.checkAtr == false){
                    lstHidden.push(item.appId);
                }
            });
            return lstHidden;
        }
        /**
         * find check box
         */
        findcheck(selectedIds: Array<any>, idCheck: number): boolean {
            let check = false;
            _.each(selectedIds, function(id) {
                if (id == idCheck) {
                    check = true;
                }
            });
            return check;
        }
        /**
         * When click button 承認
         */
        approval() {
            block.invisible();
            let self = this;
            let data = null;
            let lstApp = [];
            _.each(self.items(), function(item) {
                if (item.check && item.checkAtr) {
                    if(item.appType == 10 && item.appIdSub != null){
                        lstApp.push({ appId: item.appId, version: item.version });
                        lstApp.push({ appId: item.appIdSub, version: item.version });
                    }else{
                        lstApp.push({ appId: item.appId, version: item.version });
                    }
                }
            });
//            console.log(lstApp);
            if(lstApp.length == 0){
                block.clear();
                return;
            }
            service.approvalListApp(lstApp).done(function(data) {
                if(data.length > 0){
                    service.reflectListApp(data);
                }
                nts.uk.ui.dialog.info({ messageId: "Msg_220" });
                self.filter();
                block.clear();
            }).fail(function(res) {
                block.clear();
                nts.uk.ui.dialog.alertError({ messageId: res.messageId });
            });
        }
        /**
         * When select combo box 申請種類
         */
        filterByAppType(appType: number) {
            let self = this;
            let paramOld = null;
            let paramNew = null;
            character.restore("AppListExtractCondition").done((obj) => {
                 if (obj !== undefined) {
                    paramOld = obj;
                }
            });
            if(paramOld != null){
                paramNew = paramOld.setAppType(appType);
            }else{
                paramNew = new vmbase.AppListExtractConditionDto(self.dateValue().startDate, self.dateValue().endDate, self.mode(),
                self.selectedCode(), self.findcheck(self.selectedIds(), 1), self.findcheck(self.selectedIds(), 2), self.findcheck(self.selectedIds(), 3),
                self.findcheck(self.selectedIds(), 4), self.findcheck(self.selectedIds(), 5), self.findcheck(self.selectedIds(), 6), 0, self.lstSidFilter(), '');
            }
            //luu
                character.save('AppListExtractCondition', paramNew);
            if (appType == -1) {//全件表示
                self.items(self.lstApp());
            } else {
                let lstAppFitler: Array<vmbase.DataModeApp> = _.filter(self.lstApp(), function(item) {
                    return item.appType == appType;
                });
                self.items([]);
                self.items(lstAppFitler);
            }
            if (self.mode() == 1) {
                self.approvalCount(self.countStatus(self.items()));
                if($("#grid1").data("igGrid") !== undefined){
                    $("#grid1").ntsGrid("destroy");
                }
                let colorBackGr = self.fillColorbackGrAppr();
                let lstHidden: Array<any> = self.findRowHidden(self.items());
                self.reloadGridApproval(lstHidden,colorBackGr, self.isHidden());
            } else {
                if($("#grid2").data("igGrid") !== undefined){
                    $("#grid2").ntsGrid("destroy");
                }
                let colorBackGr = self.fillColorbackGr();
                self.reloadGridApplicaion(colorBackGr, self.isHidden());
            }
        }
        /**
         * count status when filter by appType
         */
        countStatus(lstApp: Array<vmbase.DataModeApp>): vmbase.ApplicationStatus{
            var self = this;
            let unApprovalNumber = 0;
            let approvalNumber = 0;
            let approvalAgentNumber = 0;
            let cancelNumber = 0;
            let remandNumner = 0;
            let denialNumber = 0;
            _.each(lstApp, function(app){
                let add = self.checkSync(self.lstAppCompltSync(), app.appId) ? 2 : 1;
                if(app.appStatusNo == 5){ unApprovalNumber += 1; }//UNAPPROVED:5
                if(app.appStatusNo == 4){//APPROVED: 4
                    let agent = self.findAgent(app.appId);
                    if(agent != undefined && agent.agentId != null && agent.agentId != '' && agent.agentId.match(/^\s+$/) == null){
                        approvalAgentNumber += add;
                    }else{
                        approvalNumber += add;
                    }
                }
                if(app.appStatusNo == 3){ cancelNumber += add; }//CANCELED: 3
                if(app.appStatusNo == 2){ remandNumner += add; }//REMAND: 2
                if(app.appStatusNo == 1){ denialNumber += add; }//DENIAL: 1
            })
            return new vmbase.ApplicationStatus(unApprovalNumber, approvalNumber,
                approvalAgentNumber, cancelNumber, remandNumner,denialNumber);
        }
        checkSync(lstSync: Array<vmbase.AppCompltLeaveSync>, appId: string): boolean{
            let check: boolean = false;
            _.each(lstSync, function(appSync){
                if(appSync.appMain.appID == appId && appSync.sync){
                    check = true;
                    return;
                }
            });
            return check;
        }
        findAgent(appId: string): any{
            return _.find(this.lstListAgent(), function(agent){
                return agent.appID = appId;    
            });
        }
        convertTime_Short_HM(time: number): string {
            let hh = Math.floor(time / 60);
            let min1 = Math.floor(time % 60);
            let min = '';
            if (min1 >= 10) {
                min = min1;
            } else {
                min = '0' + min1;
            }
            return hh + ':' + min;
        }
        findAppOverTime(lstItem: Array<any>){
            return _.find(lstItem, function(item){
                return item.appId == 0;
            });
        }
    }

}
