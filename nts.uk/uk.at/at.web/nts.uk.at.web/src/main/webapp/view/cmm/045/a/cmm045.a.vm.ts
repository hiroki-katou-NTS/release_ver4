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
        lstAppCommon: KnockoutObservableArray<vmbase.ApplicationDataOutput> = ko.observableArray([]);
        lstAppMaster: KnockoutObservableArray<vmbase.AppMasterInfo> = ko.observableArray([]);
        lstListAgent: KnockoutObservableArray<vmbase.ApproveAgent> = ko.observableArray([]);
        lstAppCompltSync: KnockoutObservableArray<vmbase.AppAbsRecSyncData> = ko.observableArray([]);
        
        approvalMode: KnockoutObservable<boolean> = ko.observable(false);
        approvalCount: KnockoutObservable<vmbase.ApplicationStatus> = ko.observable(new vmbase.ApplicationStatus(0, 0, 0, 0, 0, 0));
        itemList: KnockoutObservableArray<any>;
        selectedIds: KnockoutObservableArray<any> = ko.observableArray([1,5]);// check box - ver50
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
        lstContentApp: KnockoutObservableArray<any> = ko.observableArray([]);
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

            window.onresize = function(event) {
                if($('#grid1').length){//approval
                    $("#grid1").igGrid("option", "height", window.innerHeight - 350  + "px");
                }
                if($('#grid2').length){//application
                    $("#grid2").igGrid("option", "height", window.innerHeight - 270  + "px");  
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
                let param = {   condition: condition,
                                spr: self.isSpr(),
                                extractCondition: self.extractCondition(),
                                device: 0,
                                lstAppType: []
                            }
                service.getApplicationDisplayAtr().done(function(data1) {
                    _.each(data1, function(obj) {
                        self.roundingRules.push(new vmbase.ApplicationDisplayAtr(obj.value, obj.localizedName));
                    });
                    service.getApplicationList(param).done(function(data) {
                        self.lstContentApp(data.lstContentApp);
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
                        _.each(data.lstApp, function(app) {
                            self.lstAppCommon.push(new vmbase.ApplicationDataOutput(app.applicationID, app.prePostAtr, app.inputDate,
                                app.enteredPersonSID, app.applicationDate, app.applicationType, app.applicantSID, app.reflectPerState,
                                app.startDate, app.endDate, app.version, app.reflectStatus));
                        });
                        _.each(data.lstMasterInfo, function(master) {
                            self.lstAppMaster.push(new vmbase.AppMasterInfo(master.appID, master.appType, master.dispName, master.empName,master.inpEmpName,
                                master.workplaceName, master.statusFrameAtr, master.phaseStatus, master.checkAddNote, master.checkTimecolor, master.detailSet));
                        });
                        self.itemApplication([]);
                        _.each(data.lstAppInfor, function(appInfo){
                            self.itemApplication.push(new vmbase.ChoseApplicationList(appInfo.appType, appInfo.appName));                          
                        });
                        self.lstListAgent([]);
                        _.each(data.lstAgent, function(agent){
                            self.lstListAgent.push(new vmbase.ApproveAgent(agent.appID, agent.agentId));
                        });
                        _.each(data.lstSyncData, function(complt){
                            self.lstAppCompltSync.push(new vmbase.AppAbsRecSyncData(complt.typeApp, complt.appMainID, complt.appSubID, complt.appDateSub));
                        });  
                        let lstData = self.mapData(self.lstAppCommon(), self.lstAppMaster(), self.lstAppCompltSync());
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

        setupGrid(options: {
            withCcg001: boolean,
            width: any,
            height: any,
            columns: Array<{
                headerText: string,
                width: string,
                key: string,
                extraClassProperty?: string,
                checkbox?: { visible: Function, applyToProperty: string },
                button?: { text: string, click: Function }
            }>
        }) {

            let $container = $("#app-grid-container");
            $container.hide();

            if ($container.children("#not-constructed").length === 1) {

                $container.empty();

                if (options.withCcg001) {
                    $container.addClass("with-ccg001");
                }

                // header
                let $colgroup = $("<colgroup/>");
                let $trHead = $("<tr/>");
                options.columns.forEach(column => {
                    $("<col/>")
                        .attr("width", column.width)
                        .appendTo($colgroup);

                    let $th = $("<th/>")
                        .addClass("ui-widget-header");
                    
                    // batch check
                    if (column.checkbox !== undefined) {
                        let items = this.items();
                        let checkableItems = items.filter(item => item.checkAtr === true);
                        if (checkableItems.length > 0) {
                            $("<label/>")
                                .addClass("ntsCheckBox")
                                .append($("<input/>")
                                    .attr("id", "batch-check")
                                    .attr("type", "checkbox")
                                    .addClass(column.key))
                                .append($("<span/>").addClass("box"))
                                .change((e) => {
                                    let checked = $(e.target).prop("checked");
                                    $appGrid.find("input[type=checkbox]." + column.key)
                                        .prop("checked", checked);
                                    items
                                        .filter(item => item.checkAtr === true)
                                        .forEach(item => item.check = checked);
                                })
                                .appendTo($th);
                        }
                    }
                    else {
                        $th.text(column.headerText);
                    }
                    
                    $th.appendTo($trHead);
                });
                let $thead = $("<thead/>")
                    .append($trHead);

                // body
                let $tbody = $("<tbody/>");
                

                // build grid
                let $appGrid = $("<table/>")
                    .attr("id", "app-grid")
                    .data("size", { width: options.width, height: options.height })
                    .append($colgroup)
                    .append($thead)
                    .append($tbody);

                // event handler
                options.columns.forEach(column => {
                    if (column.button !== undefined) {
                        $appGrid.on("click", "." + column.key, column.button.click);
                    }

                    if (column.checkbox !== undefined) {
                        $appGrid.on("change", "input." + column.key, e => {
                            let appId = $(e.target).closest("td").data("app-id");
                            let checked = $(e.target).prop("checked");
                            let items = this.items();
                            items.filter(item => item.appId === appId)[0].check = checked;

                            // sync with batch check
                            let allChecked = true;
                            for (let i = 0; i < items.length; i++) {
                                let item = items[i];
                                if (item.checkAtr === false) continue;
                                if (item.check === false) {
                                    allChecked = false;
                                    break;
                                }
                            }
                            $("#batch-check").prop("checked", allChecked);
                        });
                    }
                });
                
                $container.append($appGrid).show();
                
                let size = $appGrid.data("size");
                fixedTable.init($appGrid, {
                    height: size.height,
                    width: size.width
                });
                
                dragResize(
                    $container.find(".nts-fixed-header-container table"),
                    $container.find(".nts-fixed-body-container table"));
            }

            this.loadGridData(options.columns);

            $container.show();
        }

        loadGridData(
            columns: Array<{
                headerText: string,
                width: string,
                key: string,
                extraClassProperty?: string,
                checkbox?: { visible: Function, applyToProperty: string },
                button?: { text: string, click: Function }
            }>) {

            let $container = $("#app-grid-container");
            let $tbody = $container.find(".nts-fixed-body-wrapper tbody");
            $tbody.empty();
            
            this.items().forEach(item => {
                let $tr = $("<tr/>");
                columns.forEach(column => {

                    let $td = $("<td/>")
                        .data("app-id", item.appId)
                        .addClass(column.key);

                    if (column.extraClassProperty !== undefined) {
                        $td.addClass(item[column.extraClassProperty]);
                    }

                    if (column.checkbox !== undefined) {
                        if (column.checkbox.visible(item) === true) {
                            $("<label/>")
                                .addClass("ntsCheckBox")
                                .append($("<input/>")
                                    .attr("type", "checkbox")
                                    .addClass(column.key))
                                .append($("<span/>").addClass("box"))
                                .appendTo($td);
                        }
                    }
                    else if (column.button !== undefined) {
                        $("<button/>")
                            .addClass(column.key)
                            .text(column.button.text)
                            .appendTo($td);
                    }
                    else {
                        $td.html(item[column.key]);
                    }

                    $td.appendTo($tr);
                });
                $tr.appendTo($tbody);
            });

            resetColumnsSize(
                $container.find("colgroup").eq(0).children(),
                $container.find(".nts-fixed-header-wrapper table"),
                $container.find(".nts-fixed-body-wrapper table"));
        }

        reloadGridApplicaion(colorBackGr: any, isHidden: boolean) {

            var self = this;
            let widthAuto = isHidden == false ? 1110 : 1045;

            let columns = [
                { headerText: getText('CMM045_50'), key: 'details', width: '55px', button: {
                    text: getText('CMM045_50'),
                    click: (e) => {
                        let targetAppId = $(e.target).closest("td").data("app-id");
                        let lstAppId = self.items().map(app => app.appId);
                        nts.uk.localStorage.setItem('UKProgramParam', 'a=0');
                        nts.uk.request.jump("/view/kaf/000/b/index.xhtml", { 'listAppMeta': lstAppId, 'currentApp': targetAppId });
                    }
                } },
                { headerText: getText('CMM045_51'), key: 'applicant', width: '120px' },
                { headerText: getText('CMM045_52'), key: 'appName', width: '90px'},
                { headerText: getText('CMM045_53'), key: 'appAtr', width: '65px', hidden: isHidden},
                { headerText: getText('CMM045_54'), key: 'appDate', width: '157px'},
                { headerText: getText('CMM045_55'), key: 'appContent', width: '408px'},
                { headerText: getText('CMM045_56'), key: 'inputDate', width: '120px'},
                { headerText: getText('CMM045_57'), key: 'appStatus', width: '75px', extraClassProperty: "appStatusName"}
            ];

            this.setupGrid({
                withCcg001: true,
                width: widthAuto,
                height: 500,
                columns: columns.filter(c => c.hidden !== true)
            });
            
/*
            $("#grid2").ntsGrid({
                width: widthAuto,
                height: window.innerHeight -250,
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
                    { headerText: getText('CMM045_55'), key: 'appContent', dataType: 'string', width: '408px'},
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
            */
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
                    item.appStatusName = 'unapprovalCell';
                    result.push(new vmbase.CellState(rowId,'appStatus',['unapprovalCell']));
                }
                if (item.appStatusNo == 1) {//1 反映待ち　＝　承認済み
                    item.appStatusName = 'approvalCell';
                    result.push(new vmbase.CellState(rowId,'appStatus',['approvalCell']));
                }
                if (item.appStatusNo == 2) {//2 反映済　＝　反映済み
                    item.appStatusName = 'reflectCell';
                    result.push(new vmbase.CellState(rowId,'appStatus',['reflectCell']));
                }
                if (item.appStatusNo == 3 || item.appStatusNo == 4) {//3,4 取消待ち/取消済　＝　取消
                    item.appStatusName = 'cancelCell';
                    result.push(new vmbase.CellState(rowId,'appStatus',['cancelCell']));
                }
                if (item.appStatusNo == 5) {//5 差し戻し　＝　差戻
                    item.appStatusName = 'remandCell';
                    result.push(new vmbase.CellState(rowId,'appStatus',['remandCell']));
                }
                if (item.appStatusNo == 6) {//6 否認　=　否
                    item.appStatusName = 'denialCell';
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
                    item.appStatusName = 'unapprovalCell';
                    result.push(new vmbase.CellState(rowId,'appStatus',['unapprovalCell']));
                }
                if (item.appStatusNo == 4) {//4 APPROVED 承認済み
                    item.appStatusName = 'approvalCell';
                    result.push(new vmbase.CellState(rowId,'appStatus',['approvalCell']));
                }
                if (item.appStatusNo == 3) {//3 CANCELED 取消
                    item.appStatusName = 'cancelCell';
                    result.push(new vmbase.CellState(rowId,'appStatus',['cancelCell']));
                }
                if (item.appStatusNo == 2) {//2 REMAND 差戻
                    item.appStatusName = 'remandCell';
                    result.push(new vmbase.CellState(rowId,'appStatus',['remandCell']));
                }
                if (item.appStatusNo == 1) {//1 DENIAL 否
                    item.appStatusName = 'denialCell';
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
            let widthAuto = isHidden == false ? 1175 : 1110;

            let columns = [
                { headerText: getText('CMM045_49'), key: 'check', dataType: 'boolean', width: '35px', checkbox: {
                    visible: item => item.checkAtr === true,
                    applyToProperty: "check"
                } },
                { headerText: getText('CMM045_50'), key: 'details', width: '55px', button: {
                    text: getText('CMM045_50'),
                    click: (e) => {
                        let targetAppId = $(e.target).closest("td").data("app-id");
                        let lstAppId = self.items().map(app => app.appId);
                        nts.uk.localStorage.setItem('UKProgramParam', 'a=1');
                        nts.uk.request.jump("/view/kaf/000/b/index.xhtml", { 'listAppMeta': lstAppId, 'currentApp': targetAppId });
                    }
                } },
                { headerText: getText('CMM045_51'), key: 'applicant', width: '120px' },
                { headerText: getText('CMM045_52'), key: 'appName', width: '90px'},
                { headerText: getText('CMM045_53'), key: 'appAtr', width: '65px', hidden: isHidden},
                { headerText: getText('CMM045_54'), key: 'appDate', width: '157px'},
                { headerText: getText('CMM045_55'), key: 'appContent', width: '341px'},
                { headerText: getText('CMM045_56'), key: 'inputDate', width: '120px'},
                { headerText: getText('CMM045_57'), key: 'appStatus', width: '75px', extraClassProperty: "appStatusName"},
                { headerText: getText('CMM045_58'), key: 'displayAppStatus', width: '95px' },
            ]

            this.setupGrid({
                withCcg001: false,
                width: widthAuto,
                height: 530,
                columns: columns.filter(c => c.hidden !== true)
            });
/*
            $("#grid1").ntsGrid({
                width: widthAuto,
                height: window.innerHeight - 330,
                dataSource: self.items(),
                primaryKey: 'appId',
                rowVirtualization: true,
                virtualization: true,
                hidePrimaryKey: true,
                rows: 8,
                virtualizationMode: 'continuous',
                columns: [
                    { headerText: getText('CMM045_49'), key: 'check', dataType: 'boolean', width: '35px', 
                            showHeaderCheckbox: lstHidden.length < self.items().length, ntsControl: 'Checkbox',  hiddenRows: lstHidden},
                    { headerText: getText('CMM045_50'), key: 'details', dataType: 'string', width: '55px', unbound: false, ntsControl: 'Button' },
                    { headerText: getText('CMM045_51'), key: 'applicant', dataType: 'string', width: '120px' },
                    { headerText: getText('CMM045_52'), key: 'appName', dataType: 'string', width: '90px'},
                    { headerText: getText('CMM045_53'), key: 'appAtr', dataType: 'string', width: '65px', hidden: isHidden},
                    { headerText: getText('CMM045_54'), key: 'appDate', dataType: 'string', width: '157px'},
                    { headerText: getText('CMM045_55'), key: 'appContent', dataType: 'string', width: '341px'},
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
            */
        }
        /**
         * 休日出勤時間申請
         * kaf010 - appTYpe = 6
         * format data: holiday work before
         * ※申請モード、承認モード(事前)用レイアウト
         */
        formatHdWorkBf(app: vmbase.ApplicationDataOutput, masterInfo: vmbase.AppMasterInfo): vmbase.DataModeApp {
            let self = this;
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
            let applicant: string = masterInfo.workplaceName == '' ? empNameFull : masterInfo.workplaceName + '<br/>' + empNameFull;
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;

            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, self.appDateColor(self.convertDateMDW(app.startDate), '',''),
                self.findContent(app.applicationID).content, self.inputDateColor(self.convertDateTime(app.inputDate), ''),
                app.reflectStatus, masterInfo.phaseStatus, masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor,
                null, app.reflectPerState);
            return a;
        }
        /**
         * 残業申請
         * kaf005 - appType = 0
         * format data: over time before
         * ※申請モード、承認モード(事前)用レイアウト
         */
        formatOverTimeBf(app: vmbase.ApplicationDataOutput, masterInfo: vmbase.AppMasterInfo): vmbase.DataModeApp {
            let self = this;
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
//            let applicant: string = masterInfo.workplaceName + '<br/>' + empNameFull;
            let applicant: string = masterInfo.workplaceName == '' ? empNameFull : masterInfo.workplaceName + '<br/>' + empNameFull;
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, self.appDateColor(self.convertDateMDW(app.startDate), '',''),
                self.findContent(app.applicationID).content, self.inputDateColor(self.convertDateTime(app.inputDate), ''),
                app.reflectStatus, masterInfo.phaseStatus,
                masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor,null, app.reflectPerState);
            return a;
        }
        /**
         * ※承認モード(事後)用レイアウト
         * format data: over time after
         */
        formatHdWorkAf(app: vmbase.ApplicationDataOutput, masterInfo: vmbase.AppMasterInfo): vmbase.DataModeApp {
            let self = this;
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
            let applicant: string = masterInfo.workplaceName == '' ? empNameFull : masterInfo.workplaceName + '<br/>' + empNameFull;
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, self.appDateColor(self.convertDateMDW(app.startDate), '',''), 
                self.findContent(app.applicationID).content, self.inputDateColor(self.convertDateTime(app.inputDate), ''),
                app.reflectStatus, masterInfo.phaseStatus, masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor,
                null, app.reflectPerState);
            return a;
        }

        /**
         * ※承認モード(事後)用レイアウト
         * format data: over time after
         */
        formatOverTimeAf(app: vmbase.ApplicationDataOutput, masterInfo: vmbase.AppMasterInfo): vmbase.DataModeApp {
            let self = this;
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
            let applicant: string = masterInfo.workplaceName == '' ? empNameFull : masterInfo.workplaceName + '<br/>' + empNameFull;
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, self.appDateColor(self.convertDateMDW(app.startDate), '',''), 
                self.findContent(app.applicationID).content, self.inputDateColor(self.convertDateTime(app.inputDate), ''),
                app.reflectStatus, masterInfo.phaseStatus, masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor,
                null, app.reflectPerState);
            return a;
        }
        /**
         * 直行直帰申請
         * kaf009 - appType = 4
         */
        formatGoBack(app: vmbase.ApplicationDataOutput, masterInfo: vmbase.AppMasterInfo): vmbase.DataModeApp {
            let self = this;
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
            let applicant: string = masterInfo.workplaceName == '' ? empNameFull : masterInfo.workplaceName + '<br/>' + empNameFull;
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, self.appDateColor(self.convertDateMDW(app.startDate), '',''),
                self.findContent(app.applicationID).content, self.inputDateColor(self.convertDateTime(app.inputDate), ''),
                app.reflectStatus, masterInfo.phaseStatus, masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor,
                null, app.reflectPerState);
            return a;
        }
        /**
         * 勤務変更申請
         * kaf007 - appType = 2
         */
        formatWorkChange(app: vmbase.ApplicationDataOutput, masterInfo: vmbase.AppMasterInfo): vmbase.DataModeApp {
            let self = this;
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
            let applicant: string = masterInfo.workplaceName == '' ? empNameFull : masterInfo.workplaceName + '<br/>' + empNameFull;
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;
            let dateRange = app.startDate == app.endDate ? self.appDateColor(self.convertDateMDW(app.applicationDate), '','') :
                self.appDateRangeColor(self.convertDateMDW(app.startDate), self.convertDateMDW(app.endDate));
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, dateRange, self.findContent(app.applicationID).content,
                self.inputDateColor(self.convertDateTime(app.inputDate), ''), app.reflectStatus, masterInfo.phaseStatus,
                masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor, null, app.reflectPerState);
            return a;
        }
        /**
         * 休暇申請
         * kaf006 - appType = 1
         * DOING
         */
        formatAbsence(app: vmbase.ApplicationDataOutput, masterInfo: vmbase.AppMasterInfo): vmbase.DataModeApp {
            let self = this;
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
            let applicant: string = masterInfo.workplaceName == '' ? empNameFull : masterInfo.workplaceName + '<br/>' + empNameFull;
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let prePostApp = masterInfo.checkAddNote == true ? prePost + getText('CMM045_101') : prePost;
            let dateRange = app.startDate == app.endDate ? self.appDateColor(self.convertDateMDW(app.applicationDate), '','') :
                self.appDateRangeColor(self.convertDateMDW(app.startDate), self.convertDateMDW(app.endDate));
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePostApp, dateRange, self.findContent(app.applicationID).content,
                self.inputDateColor(self.convertDateTime(app.inputDate), ''), app.reflectStatus, masterInfo.phaseStatus,
                masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor, null, app.reflectPerState);
            return a;
        }
        /**
         * 振休振出申請
         * kaf011 - appType = 10
         */
        formatCompltLeave(app: vmbase.ApplicationDataOutput, masterInfo: vmbase.AppMasterInfo): vmbase.DataModeApp{
            let self = this;
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
            let applicant: string = masterInfo.workplaceName == '' ? empNameFull : masterInfo.workplaceName + '<br/>' + empNameFull;
            
            //振出 rec typeApp = 1
            //振休 abs typeApp = 0
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let appDate = self.appDateColor(self.convertDateMDW(app.applicationDate), '','');
            let inputDate = self.inputDateColor(self.convertDateTime(app.inputDate), '');
            
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePost, appDate, self.findContent(app.applicationID).content, inputDate,
                app.reflectStatus, masterInfo.phaseStatus, masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor,
                null, app.reflectPerState);
            return a; 
        }
        /**
         * 振休振出申請
         * 同期
         * kaf011 - appType = 10
         */
        formatCompltSync(app: vmbase.ApplicationDataOutput, complt: vmbase.AppAbsRecSyncData, masterInfo: vmbase.AppMasterInfo): vmbase.DataModeApp{
            let self = this;
            let empNameFull = masterInfo.inpEmpName == null ? masterInfo.empName : masterInfo.empName + getText('CMM045_230', [masterInfo.inpEmpName]);
            let applicant: string = masterInfo.workplaceName == '' ? empNameFull : masterInfo.workplaceName + '<br/>' + empNameFull;
            
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            
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
                inputDateRec = self.inputDateColor(self.convertDateTime(app.inputDate), 'rec');
            }else{
                appDateRec = self.appDateColor(self.convertDateMDW(app.applicationDate), 'rec','');
                appDateAbs = self.appDateColor(self.convertDateMDW(complt.appDateSub), 'abs','');
                inputDateRec = self.inputDateColor(self.convertDateTime(app.inputDate), 'rec');
                inputDateAbs = self.inputDateColor(self.convertDateTime(app.inputDate), 'abs');
            }
            let a: vmbase.DataModeApp = new vmbase.DataModeApp(app.applicationID, app.applicationType, 'chi tiet', applicant,
                masterInfo.dispName, prePost, appDateRec + '<br/>' + appDateAbs, self.findContent(app.applicationID).content,
                inputDateRec + '<br/>' + inputDateAbs,
                '<div class = "rec" >' + app.reflectStatus + '</div>' + '<br/>' + '<div class = "abs" >' + app.reflectStatus + '</div>',
                masterInfo.phaseStatus, masterInfo.statusFrameAtr, app.version, masterInfo.checkTimecolor, complt.appSubID, app.reflectPerState);
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
        /**
         * map data -> fill in grid list
         */
        mapData(lstApp: Array<vmbase.ApplicationDataOutput>, lstMaster: Array<vmbase.AppMasterInfo>, lstCompltLeave: Array<vmbase.AppAbsRecSyncData>): Array<vmbase.DataModeApp> {
            let self = this;
            let lstData: Array<vmbase.DataModeApp> = [];
            _.each(lstApp, function(app: vmbase.ApplicationDataOutput) {
                let masterInfo = self.findMasterInfo(lstMaster, app.applicationID);
                let data: vmbase.DataModeApp;
                if (app.applicationType == 0) {//over time
                    if (self.mode() == 1 && app.prePostAtr == 1) {
                        data = self.formatOverTimeAf(app, masterInfo);
                    } else {
                        data = self.formatOverTimeBf(app, masterInfo);
                    }
                }
                if (app.applicationType == 4) {//goback
                    data = self.formatGoBack(app, masterInfo);
                }
                if(app.applicationType == 6){//holiday work
                    if(self.mode() == 1 && app.prePostAtr == 1){
                        data = self.formatHdWorkAf(app, masterInfo);
                    }else{
                        data = self.formatHdWorkBf(app, masterInfo);
                    }
                }
                if(app.applicationType == 2){//work change
                    data = self.formatWorkChange(app, masterInfo);
                }
                if(app.applicationType == 1){//absence
                    data = self.formatAbsence(app, masterInfo);
                }
                if(app.applicationType == 10){//Complement Leave
                    let complt = self.checkSync(app.applicationID, lstCompltLeave);
                    if(complt !== undefined){
                        data = self.formatCompltSync(app, complt, masterInfo);
                    }else{
                        data = self.formatCompltLeave(app, masterInfo);
                    }
                }
                lstData.push(data);
            });
            return lstData;
        }
        /**
         * find application holiday work by id
         */
        checkSync(appId: string, lstCompltLeave: Array<vmbase.AppAbsRecSyncData>):vmbase.AppAbsRecSyncData{
            return _.find(lstCompltLeave, function(complt){
                return complt.appMainID == appId;
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
            let param = {   condition: condition,
                            spr: false,
                            extractCondition: 0,
                            device: 0,
                            lstAppType: []
                        }
            service.getApplicationList(param).done(function(data) {
                self.lstContentApp([]);
                self.lstContentApp(data.lstContentApp);
                //reset data
                self.lstAppCommon([]);
                self.lstAppMaster([]);
                self.lstApp([]);
                self.lstListAgent([]);
                self.lstAppCompltSync([]);
                //luu
                character.save('AppListExtractCondition', condition);
                _.each(data.lstApp, function(app) {
                    self.lstAppCommon.push(new vmbase.ApplicationDataOutput(app.applicationID, app.prePostAtr, app.inputDate,
                        app.enteredPersonSID, app.applicationDate, app.applicationType, app.applicantSID,
                        app.reflectPerState, app.startDate, app.endDate, app.version, app.reflectStatus));
                });
                _.each(data.lstMasterInfo, function(master) {
                    self.lstAppMaster.push(new vmbase.AppMasterInfo(master.appID, master.appType, master.dispName, master.empName, master.inpEmpName, master.workplaceName,
                        master.statusFrameAtr, master.phaseStatus, master.checkAddNote, master.checkTimecolor, master.detailSet));
                });
                self.itemApplication([]);
                _.each(data.lstAppInfor, function(appInfo){
                    self.itemApplication.push(new vmbase.ChoseApplicationList(appInfo.appType, appInfo.appName));                          
                });
                self.lstListAgent([]);
                _.each(data.lstAgent, function(agent){
                    self.lstListAgent.push(new vmbase.ApproveAgent(agent.appID, agent.agentId));
                });
                _.each(data.lstSyncData, function(complt){
                    self.lstAppCompltSync.push(new vmbase.AppAbsRecSyncData(complt.typeApp, complt.appMainID, complt.appSubID, complt.appDateSub));
                });
                let lstData = self.mapData(self.lstAppCommon(), self.lstAppMaster(), self.lstAppCompltSync());
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
                if(item.appStatusNo != 5){
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
                let add = self.checkSync(app.appId, self.lstAppCompltSync()) !== undefined ? 2 : 1;
                if(app.appStatusNo == 5){ unApprovalNumber += add; }//UNAPPROVED:5
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
        findAgent(appId: string): any{
            return _.find(this.lstListAgent(), function(agent){
                return agent.appID == appId;    
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
        //find content app
        findContent(appId: string): any{
            let self = this;
            return _.find(self.lstContentApp(), function(app) {
                return app.appId == appId;
            });
        }
    }
}
