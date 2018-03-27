module cmm045.a.viewmodel {
    import vmbase = cmm045.shr.vmbase;
    import getText = nts.uk.resource.getText;
    import block = nts.uk.ui.block;
    import character = nts.uk.characteristics;
    import request = nts.uk.request;
    export class ScreenModel {
        roundingRules: KnockoutObservableArray<vmbase.ApplicationDisplayAtr> = ko.observableArray([]);
        selectedRuleCode: KnockoutObservable<any> = ko.observable(0);// switch button
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
        
        displaySet: KnockoutObservable<vmbase.ApprovalListDisplaySetDto> = ko.observable(null);
        approvalMode: KnockoutObservable<boolean> = ko.observable(false);
        approvalCount: KnockoutObservable<vmbase.ApplicationStatus> = ko.observable(new vmbase.ApplicationStatus(0, 0, 0, 0, 0, 0));
        itemList: KnockoutObservableArray<any>;
        selectedIds: KnockoutObservableArray<any> = ko.observableArray([1, 2, 3, 4, 5, 6]);// check box
        dateValue: KnockoutObservable<vmbase.Date> = ko.observable({ startDate: '', endDate: '' });
        itemApplication: KnockoutObservableArray<vmbase.ChoseApplicationList> = ko.observableArray([]);
        selectedCode: KnockoutObservable<number> = ko.observable(-1);// combo box
        mode: KnockoutObservable<number> = ko.observable(1);
        startDateString: KnockoutObservable<string> = ko.observable("");
        endDateString: KnockoutObservable<string> = ko.observable("");
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
            character.restore("AppListExtractCondition").done((obj) => {
//                console.log(obj);
                characterData = obj;
                if (obj !== undefined && obj !== null) {
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
                    self.selectedRuleCode(obj.appDisplayAtr);
                    //combo box
                    appCHeck = obj.appType;
                }
                if (urlParam === undefined) {
                    self.mode(characterData.appListAtr);
                } else {
                    self.mode(urlParam);
                }

                let param: vmbase.AppListExtractConditionDto = new vmbase.AppListExtractConditionDto(self.dateValue().startDate, self.dateValue().endDate, self.mode(),
                    self.selectedCode(), self.findcheck(self.selectedIds(), 1), self.findcheck(self.selectedIds(), 2), self.findcheck(self.selectedIds(), 3),
                    self.findcheck(self.selectedIds(), 4), self.findcheck(self.selectedIds(), 5), self.findcheck(self.selectedIds(), 6), self.selectedRuleCode(), [], '');

                service.getApplicationDisplayAtr().done(function(data1) {
                    _.each(data1, function(obj) {
                        self.roundingRules.push(new vmbase.ApplicationDisplayAtr(obj.value, obj.localizedName));
                    });
                    service.getApplicationList(param).done(function(data) {
                        self.selectedRuleCode.subscribe(function(codeChanged) {
                            self.filter();
                        });
                        //luu param
                        if (self.dateValue().startDate == '' || self.dateValue().endDate == '') {
                            let date: vmbase.Date = { startDate: data.startDate, endDate: data.endDate }
                            self.dateValue(date);
                        }
                        let paramSave: vmbase.AppListExtractConditionDto = new vmbase.AppListExtractConditionDto(self.dateValue().startDate, self.dateValue().endDate, self.mode(),
                            self.selectedCode(), self.findcheck(self.selectedIds(), 1), self.findcheck(self.selectedIds(), 2), self.findcheck(self.selectedIds(), 3),
                            self.findcheck(self.selectedIds(), 4), self.findcheck(self.selectedIds(), 5), self.findcheck(self.selectedIds(), 6), self.selectedRuleCode(), [], '');
                        character.save('AppListExtractCondition', paramSave);
//                        console.log(data);
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
                                master.workplaceName, master.statusFrameAtr, master.phaseStatus, master.checkAddNote, master.checkTimecolor));
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
                            self.lstAppOt.push(new vmbase.AppOverTimeInfoFull(overTime.appID, overTime.workClockFrom1, overTime.workClockTo1, overTime.workClockFrom2,
                                overTime.workClockTo2, overTime.total, lstFrame, overTime.overTimeShiftNight, overTime.flexExessTime));
                        });
                        _.each(data.lstAppGroup, function(group) {
                            lstAppGroup.push(new vmbase.AppPrePostGroup(group.preAppID, group.postAppID, group.time, group.appPre, group.reasonAppPre, group.appPreHd));
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
                            self.lstAppHdWork.push(new vmbase.AppHolidayWorkFull(hdwork.appId, hdwork.workTypeName, hdwork.workTimeName, hdwork.startTime1, 
                                hdwork.endTime1, hdwork.startTime2, hdwork.endTime2 ,lstFrame));
                        });
                        _.each(data.lstAppWorkChange, function(wkChange) {
                            self.lstAppWorkChange.push(new vmbase.AppWorkChangeFull(wkChange.appId, wkChange.workTypeName, wkChange.workTimeName,
                                wkChange.goWorkAtr1, wkChange.workTimeStart1, wkChange.backHomeAtr1, wkChange.workTimeEnd1, wkChange.goWorkAtr2,
                                wkChange.workTimeStart2, wkChange.backHomeAtr2, wkChange.workTimeEnd2, wkChange.breakTimeStart1, wkChange.breakTimeEnd1));
                        });
                        let lstData = self.mapData(self.lstAppCommon(), self.lstAppMaster(), lstGoBack, self.lstAppOt(), 
                            lstAppGroup, self.lstAppHdWork(), self.lstAppWorkChange());
//                        let lstData = self.mapData(self.lstAppCommon(), self.lstAppMaster(), lstGoBack, self.lstAppOt(), lstAppGroup);
                        self.lstApp(lstData);
                        self.items(vmbase.ProcessHandler.orderByList(lstData));
                        //mode approval - count
                        if (data.appStatusCount != null) {
                            self.approvalCount(new vmbase.ApplicationStatus(data.appStatusCount.unApprovalNumber, data.appStatusCount.approvalNumber,
                                data.appStatusCount.approvalAgentNumber, data.appStatusCount.cancelNumber, data.appStatusCount.remandNumner,
                                data.appStatusCount.denialNumber));
                        }
                        let colorBackGr = self.fillColorbackGr();
                        let colorsText = self.fillColorText();
                        if (self.mode() == 1) {
                             let lstHidden: Array<any> = self.findRowHidden(self.items());
                             self.reloadGridApproval(lstHidden,colorBackGr,colorsText);
                        } else {
                            self.reloadGridApplicaion(colorBackGr, colorsText);
                        }
                        if(appCHeck != null){
                            self.selectedCode(appCHeck);
                        }
                        dfd.resolve();
                    });
                }).always(() => {
                    block.clear();
                });
            });
            return dfd.promise();
        }

        reloadGridApplicaion(colorBackGr: any, colorsText: any) {
            var self = this;
            $("#grid2").ntsGrid({
                width: '1120px',
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
                    { headerText: getText('CMM045_50'), key: 'details', dataType: 'string', width: '70px', unbound: false, ntsControl: 'Button' },
                    { headerText: getText('CMM045_51'), key: 'applicant', dataType: 'string', width: '120px' },
                    { headerText: getText('CMM045_52'), key: 'appName', dataType: 'string', width: '120px' },
                    { headerText: getText('CMM045_53'), key: 'appAtr', dataType: 'string', width: '80px' },
                    { headerText: getText('CMM045_54'), key: 'appDate', dataType: 'string', width: '150px', ntsControl: 'Label'},
                    { headerText: getText('CMM045_55'), key: 'appContent', dataType: 'string', width: '280px' },
                    { headerText: getText('CMM045_56'), key: 'inputDate', dataType: 'string', width: '180px', ntsControl: 'Label'},
                    { headerText: getText('CMM045_57'), key: 'appStatus', dataType: 'string', width: '100px', ntsControl: 'Label' }
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
                    {
                        name: 'TextColor',
                        rowId: 'rowId',
                        columnKey: 'columnKey',
                        color: 'color',
                        colorsTable: colorsText
                    }
                ],
                ntsControls: [{ name: 'Checkbox', options: { value: 1, text: '' }, optionsValue: 'value', optionsText: 'text', controlType: 'CheckBox', enable: true },
                    { name: 'Button', text: getText('CMM045_50'), controlType: 'Button', enable: true },
                ]
            });
            $("#grid2").on("click", ".ntsButton", function(evt, ui) {
                let _this = $(this);
                let id = _this.parents('tr').data('id');
                nts.uk.request.jump("/view/kaf/000/b/index.xhtml", { appID: id });
            });
        }

        fillColorbackGr(): Array<vmbase.CellState>{
            let self = this;
            let result = [];
            _.each(self.items(), function(item) {
                let rowId = item.appId;
                //fill color in 承認状況
                if (item.appStatus == '未') {
                    result.push(new vmbase.CellState(rowId,'appStatus',['unapprovalCell']));
                }
                if (item.appStatus == '承認済み') {
                    result.push(new vmbase.CellState(rowId,'appStatus',['approvalCell']));
                }
                if (item.appStatus == '反映済み') {
                    result.push(new vmbase.CellState(rowId,'appStatus',['reflectCell']));
                }
                if (item.appStatus == '取消') {
                    result.push(new vmbase.CellState(rowId,'appStatus',['cancelCell']));
                }
                if (item.appStatus == '差戻') {
                    result.push(new vmbase.CellState(rowId,'appStatus',['remandCell']));
                }
                if (item.appStatus == '否') {
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
        reloadGridApproval(lstHidden: Array<any>, colorBackGr: any, colorsText: any) {
            var self = this;
            $("#grid1").ntsGrid({
                width: '1320px',
                height: '700px',
                dataSource: self.items(),
                primaryKey: 'appId',
                rowVirtualization: true,
                virtualization: true,
                hidePrimaryKey: true,
                rows: 8,
                virtualizationMode: 'continuous',
                columns: [
                    { headerText: getText('CMM045_49'), key: 'check', dataType: 'boolean', width: '90px', 
                            showHeaderCheckbox: true, ntsControl: 'Checkbox',  hiddenRows: lstHidden},
                    { headerText: getText('CMM045_50'), key: 'details', dataType: 'string', width: '70px', unbound: false, ntsControl: 'Button' },
                    { headerText: getText('CMM045_51'), key: 'applicant', dataType: 'string', width: '120px' },
                    { headerText: getText('CMM045_52'), key: 'appName', dataType: 'string', width: '120px' },
                    { headerText: getText('CMM045_53'), key: 'appAtr', dataType: 'string', width: '90px' },
                    { headerText: getText('CMM045_54'), key: 'appDate', dataType: 'string', width: '150px', ntsControl: 'Label'},
                    { headerText: getText('CMM045_55'), key: 'appContent', dataType: 'string', width: '240px'},
                    { headerText: getText('CMM045_56'), key: 'inputDate', dataType: 'string', width: '180px', ntsControl: 'Label'},
                    { headerText: getText('CMM045_57'), key: 'appStatus', dataType: 'string', width: '120px', ntsControl: 'Label' },
                    { headerText: getText('CMM045_58'), key: 'displayAppStatus', dataType: 'string', width: '120px' },
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
                    {
                        name: 'TextColor',
                        rowId: 'rowId',
                        columnKey: 'columnKey',
                        color: 'color',
                        colorsTable: colorsText
                    }
                 ],
                ntsControls: [{ name: 'Checkbox', options: { value: 1, text: '' }, optionsValue: 'value', optionsText: 'text', controlType: 'CheckBox' },
                    { name: 'Button', text: getText('CMM045_50'), controlType: 'Button', enable: true }],
            });

            $("#grid1").on("click", ".ntsButton", function(evt, ui) {
                let _this = $(this);
                let id = _this.parents('tr').data('id');
                nts.uk.request.jump("/view/kaf/000/b/index.xhtml", { appID: id });
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
            let reason = self.displaySet().appReasonDisAtr == 1 ? ' ' + app.applicationReason : '';
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
            let applicant: string = masterInfo.workplaceName + '<br/>' + empNameFull;
            let ca1 = hdWork.startTime1 == '' ? '' : hdWork.startTime1 + getText('CMM045_100') + hdWork.endTime1;
            let ca2 = hdWork.startTime2 == '' ? '' : hdWork.startTime2 + getText('CMM045_100') + hdWork.endTime2;
            let appContent010: string = getText('CMM045_275') + ' ' + hdWork.workTypeName + hdWork.workTimeName + ca1 + ca2 + getText('CMM045_276') + self.convertFrameTimeHd(hdWork.lstFrame) + reason;
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, self.convertDate(app.applicationDate), appContent010, self.convertDateTime(app.inputDate),
                self.mode() == 0 ? self.convertStatus(app.reflectPerState) : self.convertStatusAppv(app.reflectPerState), masterInfo.phaseStatus,
                masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor);
            return a;
        }
        /**
         * 残業申請
         * kaf005 - appType = 0
         * format data: over time before
         * ※申請モード、承認モード(事前)用レイアウト
         */
        formatOverTimeBf(app: vmbase.ApplicationDto_New, overTime: vmbase.AppOverTimeInfoFull, masterInfo: vmbase.AppMasterInfo): vmbase.DataModeApp {
            let self = this;
            let reason = self.displaySet().appReasonDisAtr == 1 ? ' ' + app.applicationReason : '';
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
//            let applicant: string = masterInfo.workplaceName + '<br/>' + empNameFull;
            let applicant: string = masterInfo.workplaceName == '' ? empNameFull : masterInfo.workplaceName + '<br/>' + empNameFull;
            let appContent1111: string = getText('CMM045_268') + ' ' + overTime.workClockFrom1 + getText('CMM045_100') + overTime.workClockTo1 + ' 残業合計' + self.convertFrameTime(overTime.lstFrame) + reason;
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, self.convertDate(app.applicationDate), appContent1111, self.convertDateTime(app.inputDate),
                self.mode() == 0 ? self.convertStatus(app.reflectPerState) : self.convertStatusAppv(app.reflectPerState), masterInfo.phaseStatus,
                masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor);
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
            let lstSort = _.sortBy(lstFrame, ["frameNo"], ["asc"]);
            //時間外深夜時間
            let frame11 = self.findFrameByNo(lstFrame, 11);
            if (frame11 !== undefined && frame11.applicationTime != 0) {
                framName11 = frame11.name + self.convertTime_Short_HM(frame11.applicationTime);
                time += frame11.applicationTime;
                count += 1;
            }
            //ﾌﾚｯｸｽ超過
            let frame12 = self.findFrameByNo(lstFrame, 12);
            if (frame12 !== undefined && frame12.applicationTime != 0) {
                framName12 = frame12.name + self.convertTime_Short_HM(frame12.applicationTime);
                time += frame12.applicationTime;
                count += 1;
            }
            _.each(lstSort, function(item) {
                if (item.frameNo != 11 && item.frameNo != 12 && item.applicationTime != 0) {//時間外深夜時間
                    if (count < 3) {
                        framName += item.name + self.convertTime_Short_HM(item.applicationTime);
                    }
                    time += item.applicationTime;
                    count += 1;
                }
            });
            let other = count > 3 ? count - 3 : 0;
            let otherInfo = other > 0 ? '他' + other + '枠' : '';
            let result = self.convertTime_Short_HM(time) + '(' + framName11 + framName12 + framName + otherInfo + ')';
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
            let lstSort = _.sortBy(lstFrame, ["frameNo"], ["asc"]);
            _.each(lstSort, function(item, index) {
                if (item.applicationTime != 0) {
                    if (count <= 1) {
                        framName += item.name + self.convertTime_Short_HM(item.applicationTime);
                    }
                    time += item.applicationTime;
                    count += 1;
                }
            });
            let other = count > 2 ? count - 2 : 0;
            let otherInfo = other > 0 ? '他' + other + '枠' : '';
            let result = self.convertTime_Short_HM(time) + '(' + framName + otherInfo + ')';
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
                if (check.preAppID != '') {
                    let prRes = self.findContentPreHd(check.preAppID, check.lstFrameRes, check.appPreHd, check.reasonAppPre);
                    contentPre = prRes.appPre == '' ? '' : '<br/>' + prRes.appPre;
                    contentResult = prRes.appRes == '' ? '' :'<br/>' + prRes.appRes;
                }
            }
            //reason application
            let reason = self.displaySet().appReasonDisAtr == 1 ? '<br/>' + app.applicationReason : '';
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
            let applicant: string = masterInfo.workplaceName + '<br/>' + empNameFull;
            let ca1 = hdWork.startTime1 == '' ? '' : hdWork.startTime1 + getText('CMM045_100') + hdWork.endTime1;
            let ca2 = hdWork.startTime2 == '' ? '' : hdWork.startTime2 + getText('CMM045_100') + hdWork.endTime2;
            let appContentPost: string = getText('CMM045_272') + getText('CMM045_275') + ' ' + hdWork.workTypeName + hdWork.workTimeName + ca1 + ca2 + getText('CMM045_276') + self.convertFrameTimeHd(hdWork.lstFrame) + reason;
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let contentFull = '<div class = "appContent-' + app.applicationID + '">'+ appContentPost + contentPre + contentResult + '</div>';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, self.convertDate(app.applicationDate), contentFull, self.convertDateTime(app.inputDate),
                self.mode() == 0 ? self.convertStatus(app.reflectPerState) : self.convertStatusAppv(app.reflectPerState), masterInfo.phaseStatus,
                masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor);
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
                if (check.preAppID != '') {
                    let prRes = self.findContentPreOt(check.preAppID, check.lstFrameRes, check.appPre, check.reasonAppPre);
                    contentPre = prRes.appPre == '' ? '' : '<br/>' + prRes.appPre;
                    contentResult = prRes.appRes == '' ? '' :'<br/>' + prRes.appRes;
                }
            }
            let reason = self.displaySet().appReasonDisAtr == 1 ? '<br/>' + app.applicationReason : '';
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
//            let applicant: string = masterInfo.workplaceName + '<br/>' + empNameFull;
            let applicant: string = masterInfo.workplaceName == '' ? empNameFull : masterInfo.workplaceName + '<br/>' + empNameFull;
            let appContentPost: string = getText('CMM045_272') + getText('CMM045_268') + ' ' + overTime.workClockFrom1 + getText('CMM045_100') + overTime.workClockTo1 + ' 残業合計' + self.convertFrameTime(overTime.lstFrame) + reason;
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let contentFull = '<div class = "appContent-' + app.applicationID + '">'+ appContentPost + contentPre + contentResult + '</div>';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, self.convertDate(app.applicationDate), contentFull, self.convertDateTime(app.inputDate),
                self.mode() == 0 ? self.convertStatus(app.reflectPerState) : self.convertStatusAppv(app.reflectPerState), masterInfo.phaseStatus,
                masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor);
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
        findContentPreOt(appId: string, lstFrameRes: Array<vmbase.OverTimeFrame>, appPreDB: any, reasonAppPre: string): any {
            let self = this;
            let appPre = '';
            if(appPreDB != null){
                appPre = getText('CMM045_268') + ' ' + appPreDB.workClockFrom1 + getText('CMM045_100') + appPreDB.workClockTo1 + ' 残業合計' + self.convertFrameTime(appPreDB.lstFrame) + '<br/>' + reasonAppPre;
            }
            let appResContent = '';
            //thuc te
            let appRes = self.convertFrameTime(lstFrameRes);
            appResContent = getText('CMM045_274') + appRes;


            let appInfor = {
                appPre: appPre == null ? '' : getText('CMM045_273') + appPre,
                appRes: lstFrameRes.length == 0 ? '' : appResContent
            }
            return appInfor;
        }
        
        /**
         * find content pre and result
         * 休日出勤時間申請 - holiday work
         * TO DO
         */
        findContentPreHd(appId: string, lstFrameRes: Array<vmbase.OverTimeFrame>, appPreDB: any, reasonAppPre: string): any {
            let self = this;
            let appPre = '';
            if(appPreDB != null){
                let ca1 = appPreDB.startTime1 == '' ? '' : appPreDB.startTime1 + getText('CMM045_100') + appPreDB.endTime1;
                let ca2 = appPreDB.startTime2 == '' ? '' : appPreDB.startTime2 + getText('CMM045_100') + appPreDB.endTime2;
                appPre = getText('CMM045_275') + ' ' + appPreDB.workTypeName + appPreDB.workTimeName + ca1 + ca2 + getText('CMM045_276') + self.convertFrameTimeHd(appPreDB.lstFrame) + '<br/>' + reasonAppPre;
            }
            let appResContent = '';
            //thuc te
            let appRes = self.convertFrameTimeHd(lstFrameRes);
            appResContent = getText('CMM045_274') + appRes;


            let appInfor = {
                appPre: appPre == null ? '' : getText('CMM045_273') + appPre,
                appRes: lstFrameRes.length == 0 ? '' : appResContent
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
            let go1 = goBack.goWorkAtr1 == 0 ? '' : ' ' + getText('CMM045_259') + goBack.workTimeStart1;
            let back1 = goBack.backHomeAtr1 == 0 ? '' : ' ' + getText('CMM045_260') + goBack.workTimeEnd1;
            let go2 = goBack.goWorkAtr2 == 0 ? '' : ' ' + getText('CMM045_259') + goBack.workTimeStart2;
            let back2 = goBack.backHomeAtr2 == 0 ? '' : ' ' + getText('CMM045_260') + goBack.workTimeEnd2;
            let reason = self.displaySet().appReasonDisAtr == 1 ? '<br/>' + app.applicationReason : '';
            let appContent2222 = getText('CMM045_258') + go1 + back1 + go2 + back2 + reason;
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, self.convertDate(app.applicationDate), appContent2222, self.convertDateTime(app.inputDate),
                self.mode() == 0 ? self.convertStatus(app.reflectPerState) : self.convertStatusAppv(app.reflectPerState), masterInfo.phaseStatus,
                masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor);
            return a;
        }
        /**
         * 勤務変更申請
         * kaf007 - appType = 2
         */
        formatWorkChange(app: vmbase.ApplicationDto_New, wkChange: vmbase.AppWorkChangeFull, masterInfo: vmbase.AppMasterInfo): vmbase.DataModeApp {
            let self = this;
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
            let applicant: string = masterInfo.workplaceName + '<br/>' + empNameFull;
            let go1 = wkChange.goWorkAtr1 == 0 ? '' : ' ' + getText('CMM045_252') + wkChange.workTimeStart1;
            let back1 = wkChange.backHomeAtr1 == 0 ? '' : getText('CMM045_100') + getText('CMM045_252') + wkChange.workTimeEnd1;
            let go2 = (wkChange.goWorkAtr2 == 0 || wkChange.goWorkAtr2 == null) ? '' : ' ' + getText('CMM045_252') + wkChange.workTimeStart2;
            let back2 = (wkChange.backHomeAtr2 == 0 || wkChange.backHomeAtr2 == null) ? '' : getText('CMM045_100') + getText('CMM045_252') + wkChange.workTimeEnd2;
            let breakTime = getText('CMM045_251') + wkChange.breakTimeStart1 + getText('CMM045_100') + wkChange.breakTimeEnd1;
            let reason = self.displaySet().appReasonDisAtr == 1 ? '<br/>' + app.applicationReason : '';
            let appContent007 = getText('CMM045_250') + wkChange.workTypeName + wkChange.workTimeName + go1 + back1 + go2 + back2 + reason;
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, self.convertDate(app.applicationDate), appContent007, self.convertDateTime(app.inputDate),
                self.mode() == 0 ? self.convertStatus(app.reflectPerState) : self.convertStatusAppv(app.reflectPerState), masterInfo.phaseStatus,
                masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor);
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
            let applicant: string = masterInfo.workplaceName + '<br/>' + empNameFull;
            let reason = self.displaySet().appReasonDisAtr == 1 ? '<br/>' + app.applicationReason : '';
            let appContent006 = '';
            if(absence.allDayHalfDayLeaveAtr == 1 && absence.relationshipCode == null){//終日休暇 (ALL_DAY_LEAVE) 且 特別休暇申請.続柄コード　＝　未入力（NULL)
                appContent006 = self.convertAbsenceAllDay(absence, reason);
            }
            if(absence.relationshipCode != null){//特別休暇申請.続柄コード　＝　入力ありの場合
                appContent006 = self.convertAbsenceSpecial(absence, reason);
            }
            if(absence.allDayHalfDayLeaveAtr == 0){//休暇申請.終日半日休暇区分　＝　半日休暇
                appContent006 = self.convertAbsenceHalfDay(absence, reason);
            }
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, self.convertDate(app.applicationDate), appContent006, self.convertDateTime(app.inputDate),
                self.mode() == 0 ? self.convertStatus(app.reflectPerState) : self.convertStatusAppv(app.reflectPerState), masterInfo.phaseStatus,
                masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor);
            return a;
        }
        //※休暇申請.終日半日休暇区分　＝　終日休暇 且 特別休暇申請.続柄コード　＝　未入力（NULL)
        convertAbsenceAllDay(absence: vmbase.AppAbsenceFull, reasonApp: string): string{
            let self = this;
            let reason = reasonApp == '' ? '' : '<br/>' + reasonApp;
            return getText('CMM045_279') + getText('CMM045_248') + getText('CMM045_248', [self.convertNameHoliday(absence.holidayAppType)]) + reason;
        }
        //※特別休暇申請.続柄コード　＝　入力ありの場合
        convertAbsenceSpecial(absence: vmbase.AppAbsenceFull, reasonApp: string): string{
            let hdAppSet = null;
            let reason = reasonApp == '' ? '' : '<br/>' + reasonApp;
            let day = absence.mournerFlag == true ? getText('CMM045_277') + absence.day + getText('CMM045_278') : '';
            let result = getText('CMM045_279') + getText('CMM045_248') + hdAppSet.specialVaca  
            + absence.relationshipName + day + reason;
            return result;
        }
        //※休暇申請.終日半日休暇区分　＝　半日休暇
        convertAbsenceHalfDay(absence: vmbase.AppAbsenceFull, reasonApp: string): string{
            let self = this;
            let reason = reasonApp == '' ? '' : '<br/>' + reasonApp;
            let time1 = absence.startTime1 == '' ? '' : absence.startTime1 + getText('CMM045_100') +  absence.endTime1;
            let time2 =  absence.startTime2 == '' ? '' : ' ' + absence.startTime2 + getText('CMM045_100') + absence.endTime2;
            let result = getText('CMM045_279') + getText('CMM045_249') + getText('CMM045_230', [self.convertNameHoliday(absence.holidayAppType)])  + time1 + time2 + reason;
            return result;
        }
        convertNameHoliday(holidayType: number): string{
            let hdAppSet = null;
            switch(holidayType){
                case 0:// 年休名称 - 0
                    return hdAppSet.yearHdName;
                case 1:// 代表者名 - 1
                    return hdAppSet.obstacleName;
                case 2:// 欠勤名称 - 2
                    return hdAppSet.absenteeism;
                case 3:// 特別休暇名称 - 3
                    return hdAppSet.specialVaca;
                case 4:// 積立年休名称  - 4
                    return hdAppSet.yearResig;
                case 5:// 休日名称 - 5
                    return hdAppSet.hdName;
                case 6:// 時間消化名称 - 6
                    return hdAppSet.timeDigest;
                case 7:// 振休名称 - 7
                    return hdAppSet.furikyuName;
                default:
                    return "";
            }
        }
        /**
         * map data -> fill in grid list
         */
        mapData(lstApp: Array<vmbase.ApplicationDto_New>, lstMaster: Array<vmbase.AppMasterInfo>, lstGoBack: Array<vmbase.AppGoBackInfoFull>,
            lstOverTime: Array<vmbase.AppOverTimeInfoFull>, lstAppGroup: Array<vmbase.AppPrePostGroup>, lstHdWork: Array<vmbase.AppHolidayWorkFull>,
            lstWorkChange: Array<vmbase.AppWorkChangeFull>): Array<vmbase.DataModeApp> {
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
                lstData.push(data);
            });
            return lstData;
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
                    return '未';//下書き保存/未反映　=　未
                case 1:
                    return '承認済み';//反映待ち　＝　承認済み
                case 2:
                    return '反映済み';//反映済　＝　反映済み
                case 5:
                    return '差戻';//差し戻し　＝　差戻
                case 6:
                    return '否';//否認　=　否
                default:
                    return '取消';//取消待ち/取消済　＝　取消
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
                    return '否';
                case 2: //REMAND: 2
                    return '差戻';
                case 3: //CANCELED: 3
                    return '取消';
                case 4: //APPROVED: 4
                    return '承認済み';
                case 5: //UNAPPROVED:5
                    return '未';
                default: //-: 0
                    return '-';
            }
        }
        //yyyy/MM/dd
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
        //yyyy/MM/dd hh:mm
        convertDateTime(dateTime: string) {
            let a: number = moment(dateTime, 'YYYY/MM/DD hh:mm').isoWeekday();
            let date = dateTime.split(" ")[0];
            let time = dateTime.split(" ")[1];
            return this.convertDate(date) + ' ' + time;
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
            if (self.dateValue().startDate == null || self.dateValue().endDate == null) {//期間開始日付または期間終了日付が入力されていない
//                $('.ntsStartDate').set
                $('.ntsDatepicker.nts-input.ntsStartDatePicker.ntsDateRange_Component').ntsError('set', {messageId:"Msg_359"});
//                nts.uk.ui.dialog.error({ messageId: "Msg_359" });
                block.clear();
                return;
            }
            if (self.mode() == 1 && self.selectedIds().length == 0) {//承認状況のチェックの確認
                nts.uk.ui.dialog.error({ messageId: "Msg_360" });
                block.clear();
                return;
            }
            let param: vmbase.AppListExtractConditionDto = new vmbase.AppListExtractConditionDto(self.dateValue().startDate, self.dateValue().endDate, self.mode(),
                self.selectedCode(), self.findcheck(self.selectedIds(), 1), self.findcheck(self.selectedIds(), 2), self.findcheck(self.selectedIds(), 3),
                self.findcheck(self.selectedIds(), 4), self.findcheck(self.selectedIds(), 5), self.findcheck(self.selectedIds(), 6), self.selectedRuleCode(), [], '');
            service.getApplicationList(param).done(function(data) {
//                console.log(data);
                //reset data
                self.lstAppCommon([]);
                self.lstAppMaster([]);
                self.lstAppOt([]);
                //luu
                character.save('AppListExtractCondition', param);
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
                        master.statusFrameAtr, master.phaseStatus, master.checkAddNote, master.checkTimecolor));
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
                    self.lstAppOt.push(new vmbase.AppOverTimeInfoFull(overTime.appID, overTime.workClockFrom1, overTime.workClockTo1, overTime.workClockFrom2,
                        overTime.workClockTo2, overTime.total, lstFrame, overTime.overTimeShiftNight, overTime.flexExessTime));
                });
                _.each(data.lstAppGroup, function(group) {
                    lstAppGroup.push(new vmbase.AppPrePostGroup(group.preAppID, group.postAppID, group.time, group.appPre, group.reasonAppPre, group.appPreHd));
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
                    self.lstAppHdWork.push(new vmbase.AppHolidayWorkFull(hdwork.appId, hdwork.workTypeName, hdwork.workTimeName, hdwork.startTime1, 
                        hdwork.endTime1, hdwork.startTime2, hdwork.endTime2 ,lstFrame));
                });
                _.each(data.lstAppWorkChange, function(wkChange) {
                    self.lstAppWorkChange.push(new vmbase.AppWorkChangeFull(wkChange.appId, wkChange.workTypeName, wkChange.workTimeName,
                        wkChange.goWorkAtr1, wkChange.workTimeStart1, wkChange.backHomeAtr1, wkChange.workTimeEnd1, wkChange.goWorkAtr2,
                        wkChange.workTimeStart2, wkChange.backHomeAtr2, wkChange.workTimeEnd2, wkChange.breakTimeStart1, wkChange.breakTimeEnd1));
                });
                let lstData = self.mapData(self.lstAppCommon(), self.lstAppMaster(), lstGoBack, self.lstAppOt(), 
                    lstAppGroup, self.lstAppHdWork(), self.lstAppWorkChange());
                self.lstApp(lstData);
                if (self.selectedCode() != -1) {
                    self.filterByAppType(self.selectedCode());
                } else {
                    self.items(vmbase.ProcessHandler.orderByList(lstData));
                    //mode approval - count
                    if (data.appStatusCount != null) {
                        self.approvalCount(new vmbase.ApplicationStatus(data.appStatusCount.unApprovalNumber, data.appStatusCount.approvalNumber,
                            data.appStatusCount.approvalAgentNumber, data.appStatusCount.cancelNumber, data.appStatusCount.remandNumner,
                            data.appStatusCount.denialNumber));
                    }
                    let colorBackGr = self.fillColorbackGr();
                    let colorsText = self.fillColorText();
                    if (self.mode() == 1) {
                        $("#grid1").ntsGrid("destroy");
                        let lstHidden: Array<any> = self.findRowHidden(self.items());
                        self.reloadGridApproval(lstHidden,colorBackGr,colorsText);
                    } else {
                        $("#grid2").ntsGrid("destroy");
                        self.reloadGridApplicaion(colorBackGr,colorsText);
                    }
                }
            }).always(() => {
                block.clear();
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
                    lstApp.push({ appId: item.appId, version: item.version });
                }
            });
            if(lstApp.length == 0){
                block.clear();
                return;
            }
            service.approvalListApp(lstApp).done(function() {
                nts.uk.ui.dialog.info({ messageId: "Msg_220" });
                self.filter();
            }).fail(function(res) {
                nts.uk.ui.dialog.alertError({ messageId: res.messageId });
            }).always(() => {
                block.clear();
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
                self.findcheck(self.selectedIds(), 4), self.findcheck(self.selectedIds(), 5), self.findcheck(self.selectedIds(), 6), self.selectedRuleCode(), [], '');
            }
            //luu
                character.save('AppListExtractCondition', paramNew);
            if (appType == -1) {//全件表示
                self.items(vmbase.ProcessHandler.orderByList(self.lstApp()));
            } else {
                let lstAppFitler: Array<vmbase.DataModeApp> = _.filter(self.lstApp(), function(item) {
                    return item.appType == appType;
                });
                self.items([]);
                self.items(vmbase.ProcessHandler.orderByList(lstAppFitler));
            }
            let colorBackGr = self.fillColorbackGr();
            let colorsText = self.fillColorText();
            if (self.mode() == 1) {
                self.approvalCount(self.countStatus(self.items()));
                if($("#grid1").data("igGrid") !== undefined){
                    $("#grid1").ntsGrid("destroy");
                }
                 let lstHidden: Array<any> = self.findRowHidden(self.items());
                 self.reloadGridApproval(lstHidden,colorBackGr,colorsText);
            } else {
                if($("#grid2").data("igGrid") !== undefined){
                    $("#grid2").ntsGrid("destroy");
                }
                self.reloadGridApplicaion(colorBackGr,colorsText);
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
                if(app.appStatus == '未'){ unApprovalNumber += 1; }//UNAPPROVED:5
                if(app.appStatus == '承認済み'){//APPROVED: 4
                    let agent = self.findAgent(app.appId);
                    if(agent != undefined && agent.agentId != null && agent.agentId != ''){
                        approvalAgentNumber += 1;
                    }else{
                        approvalNumber += 1;
                    }
                }
//                if(app.appStatus == '-'){ approvalAgentNumber += 1; }//-: 0 
                if(app.appStatus == '取消'){ cancelNumber += 1; }//CANCELED: 3
                if(app.appStatus == '差戻'){ remandNumner += 1; }//REMAND: 2
                if(app.appStatus == '否'){ denialNumber += 1; }//DENIAL: 1
            })
            return new vmbase.ApplicationStatus(unApprovalNumber, approvalNumber,
                approvalAgentNumber, cancelNumber, remandNumner,denialNumber);
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
    }

}
