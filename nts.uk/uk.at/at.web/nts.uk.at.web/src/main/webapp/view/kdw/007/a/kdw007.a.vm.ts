module nts.uk.at.view.kdw007.a.viewmodel {
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    export class ScreenModel {
        isNewMode: KnockoutObservable<boolean> = ko.observable(false);

        listUseAtr: KnockoutObservableArray<any> = ko.observableArray([
            { code: '0', name: nts.uk.resource.getText("Enum_UseAtr_NotUse") },
            { code: '1', name: nts.uk.resource.getText("Enum_UseAtr_Use") }
        ]);
        listTypeAtr: KnockoutObservableArray<any> = ko.observableArray([
            { code: '0', name: nts.uk.resource.getText("Enum_ErrorAlarmClassification_Error") },
            { code: '1', name: nts.uk.resource.getText("Enum_ErrorAlarmClassification_Alarm") },
            { code: '2', name: nts.uk.resource.getText("Enum_ErrorAlarmClassification_Other") }
        ]);
        gridListColumns: KnockoutObservableArray<any> = ko.observableArray([
            { headerText: nts.uk.resource.getText("KDW007_6"), key: 'code', width: 45 },
            { headerText: nts.uk.resource.getText("KDW007_7"), key: 'name', width: 280 }
        ]);
        lstErrorAlarm: KnockoutObservableArray<any> = ko.observableArray([]);
        selectedErrorAlarm: KnockoutObservable<any> = ko.observable(new ErrorAlarmWorkRecord());
        selectedErrorAlarmCode: KnockoutObservable<string> = ko.observable(null);
        tabs: KnockoutObservableArray<nts.uk.ui.NtsTabPanelModel> = ko.observableArray([
            { id: 'tab-1', title: nts.uk.resource.getText("KDW007_9"), content: '.settingTab', enable: ko.observable(true), visible: ko.observable(true) },
            { id: 'tab-2', title: nts.uk.resource.getText("KDW007_83"), content: '.checkScopeTab', enable: ko.observable(true), visible: ko.observable(true) },
            { id: 'tab-3', title: nts.uk.resource.getText("KDW007_84"), content: '.conditionSettingTab1', enable: ko.observable(true), visible: ko.observable(true) },
            { id: 'tab-4', title: nts.uk.resource.getText("KDW007_85"), content: '.conditionSettingTab2', enable: ko.observable(true), visible: ko.observable(true) },
            { id: 'tab-5', title: nts.uk.resource.getText("KDW007_86"), content: '.applicationTab', enable: ko.observable(true), visible: ko.observable(true) }
        ]);
        selectedTab: KnockoutObservable<string> = ko.observable('tab-1');
        errorDisplayItemName: KnockoutObservable<string> = ko.observable("");
        //Tab3
        enumLogicalOperator: KnockoutObservableArray<any> = ko.observableArray([
            { code: 0, name: 'AND' },
            { code: 1, name: 'OR' }
        ]);
        enumFilterByCompare: KnockoutObservableArray<any> = ko.observableArray([
            { code: 0, name: nts.uk.resource.getText("Enum_FilterByCompare_NotCompare") },
            { code: 1, name: nts.uk.resource.getText("Enum_FilterByCompare_Extract_Same") },
            { code: 2, name: nts.uk.resource.getText("Enum_FilterByCompare_Extract_Different") }
        ]);
        // Tab 5
        lstApplicationType = ko.observableArray([
            { code: 0, name: "残業申請" },
            { code: 1, name: "休暇申請" },
            { code: 2, name: "勤務変更申請" },
            { code: 3, name: "出張申請" },
            { code: 4, name: "直行直帰申請" },
            { code: 6, name: "休出時間申請" },
            { code: 7, name: "打刻申請" },
            { code: 8, name: "時間年休申請" },
            { code: 9, name: "遅刻早退取消申請" },
            { code: 10, name: "振休振出申請" },
            { code: 11, name: "打刻申請（NR形式）" },
            { code: 12, name: "連続出張申請" },
            { code: 13, name: "出張申請オフィスヘルパー" },
            { code: 14, name: "３６協定時間申請" }
        ]);
        appTypeGridlistColumns = ko.observableArray([
            { headerText: 'コード', key: 'code', width: 100, hidden: true },
            { headerText: nts.uk.resource.getText("KDW007_82"), key: 'name', width: 300 },
        ]);

        constructor() {
            let self = this;
            self.selectedErrorAlarmCode.subscribe((code) => {
                if (code) {
                    let foundItem = _.find(self.lstErrorAlarm(), (item) => {
                        return item.code == code;
                    });
                    if (foundItem) {
                        self.isNewMode(false);
                        self.changeSelectedErrorAlarm(foundItem);
                    }
                }
            });
        }

        isExistedCode() {
            let self = this;
            let foundItem = _.find(self.lstErrorAlarm(), (item) => {
                return item.code == "U" + self.selectedErrorAlarm().code();
            });
            if (foundItem) {
                return true;
            } else {
                return false;
            }
        }

        changeSelectedErrorAlarm(foundItem) {
            let self = this;
            nts.uk.ui.errors.clearAll();
            self.reSetData(self.selectedErrorAlarm(), foundItem);
            self.selectedTab('tab-1');
            $("#errorAlarmWorkRecordName").focus();
        }

        startPage(code): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            nts.uk.ui.block.grayout();
            service.getAll().done((lstData) => {
                if (lstData && lstData.length > 0) {
                    let sortedData = _.orderBy(lstData, ['code'], ['asc']);
                    self.lstErrorAlarm(sortedData);
                    self.selectedErrorAlarmCode(code !== null ? code : sortedData[0].code);
                    self.isNewMode(false);
                    self.selectedTab('tab-1');
                } else {
                    self.lstErrorAlarm([]);
                    self.selectedErrorAlarmCode(null);
                    self.reSetData(self.selectedErrorAlarm(), null);
                    self.isNewMode(true);
                    self.selectedTab('tab-1');
                }
                nts.uk.ui.block.clear();
                dfd.resolve();
            });
            return dfd.promise();
        }

        /* Function Area */

        setNewMode() {
            let self = this;
            self.selectedErrorAlarmCode(null);
            self.reSetData(self.selectedErrorAlarm(), null);
            self.isNewMode(true);
            self.selectedTab('tab-1');
            nts.uk.ui.errors.clearAll();
            $("#errorAlarmWorkRecordCode").focus();
        }

        reSetData(selectedErrorAlarm, param) {
            selectedErrorAlarm.companyId(param && param.companyId ? param.companyId : '');
            selectedErrorAlarm.code(param && param.code ? param.code : '');
            selectedErrorAlarm.name(param && param.name ? param.name : '');
            selectedErrorAlarm.fixedAtr(param && param.fixedAtr ? param.fixedAtr : 0);
            selectedErrorAlarm.useAtr(param && param.useAtr ? param.useAtr : 0);
            selectedErrorAlarm.typeAtr(param && param.typeAtr ? param.typeAtr : 0);
            selectedErrorAlarm.displayMessage(param && param.displayMessage ? param.displayMessage : '');
            selectedErrorAlarm.boldAtr(param && param.boldAtr ? param.boldAtr : 0);
            selectedErrorAlarm.messageColor(param && param.messageColor ? param.messageColor : null);
            selectedErrorAlarm.cancelableAtr(param && param.cancelableAtr ? param.cancelableAtr : 0);
            selectedErrorAlarm.errorDisplayItem(param && param.errorDisplayItem ? param.errorDisplayItem : null);
            selectedErrorAlarm.errorDisplayItem.valueHasMutated();
            selectedErrorAlarm.errorDisplayItemName("");
            selectedErrorAlarm.alCheckTargetCondition.setData(param && param.alCheckTargetCondition ? param.alCheckTargetCondition : null);
            selectedErrorAlarm.workTypeCondition.setData(param && param.workTypeCondition ? param.workTypeCondition : null);
            selectedErrorAlarm.workTimeCondition.setData(param && param.workTimeCondition ? param.workTimeCondition : null);
            selectedErrorAlarm.operatorBetweenPlanActual(param && param.operatorBetweenPlanActual ? param.operatorBetweenPlanActual : 0);
            selectedErrorAlarm.lstApplicationTypeCode(param && param.lstApplicationTypeCode ? param.lstApplicationTypeCode : []);
            selectedErrorAlarm.operatorBetweenGroups(param && param.operatorBetweenGroups ? param.operatorBetweenGroups : 0);
            selectedErrorAlarm.operatorGroup1(param && param.operatorGroup1 ? param.operatorGroup1 : 0);
            selectedErrorAlarm.operatorGroup2(param && param.operatorGroup2 ? param.operatorGroup2 : 0);
            selectedErrorAlarm.group2UseAtr(param && param.group2UseAtr ? param.group2UseAtr : false);
            selectedErrorAlarm.erAlAtdItemConditionGroup1.forEach((condition) => {
                if (param && param.erAlAtdItemConditionGroup1 && param.erAlAtdItemConditionGroup1.length > 0) {
                    param.erAlAtdItemConditionGroup1.forEach((conditionParam) => {
                        if (conditionParam.targetNO == condition.targetNO()) {
                            condition.setData(conditionParam.targetNO, conditionParam);
                        }
                    });
                } else {
                    condition.setData(condition.targetNO(), null);
                }
            });
            selectedErrorAlarm.erAlAtdItemConditionGroup2.forEach((condition) => {
                if (param && param.erAlAtdItemConditionGroup2 && param.erAlAtdItemConditionGroup2.length > 0) {
                    param.erAlAtdItemConditionGroup2.forEach((conditionParam) => {
                        if (conditionParam.targetNO == condition.targetNO()) {
                            condition.setData(conditionParam.targetNO, conditionParam);
                        }
                    });
                } else {
                    condition.setData(condition.targetNO(), null);
                }
            });
        }

        update() {
            let self = this;
            $(".need-check").trigger("validate");
            if (!nts.uk.ui.errors.hasError()) {
                var data = ko.mapping.toJS(self.selectedErrorAlarm());
                data.boldAtr = data.boldAtr ? 1 : 0;
                data.alCheckTargetCondition.filterByBusinessType = data.alCheckTargetCondition.filterByBusinessType ? 1 : 0;
                data.alCheckTargetCondition.filterByEmployment = data.alCheckTargetCondition.filterByEmployment ? 1 : 0;
                data.alCheckTargetCondition.filterByJobTitle = data.alCheckTargetCondition.filterByJobTitle ? 1 : 0;
                data.alCheckTargetCondition.filterByClassification = data.alCheckTargetCondition.filterByClassification ? 1 : 0;
                data.workTypeCondition.planFilterAtr = data.workTypeCondition.planFilterAtr ? 1 : 0;
                data.workTypeCondition.actualFilterAtr = data.workTypeCondition.actualFilterAtr ? 1 : 0;
                data.workTimeCondition.planFilterAtr = data.workTimeCondition.planFilterAtr ? 1 : 0;
                data.workTimeCondition.actualFilterAtr = data.workTimeCondition.actualFilterAtr ? 1 : 0;
                data.alCheckTargetCondition.lstBusinessType = _.values(data.alCheckTargetCondition.lstBusinessType ? data.alCheckTargetCondition.lstBusinessType : []);
                data.alCheckTargetCondition.lstJobTitle = _.values(data.alCheckTargetCondition.lstJobTitle ? data.alCheckTargetCondition.lstJobTitle : []);
                data.alCheckTargetCondition.lstEmployment = _.values(data.alCheckTargetCondition.lstEmployment ? data.alCheckTargetCondition.lstEmployment : []);
                data.alCheckTargetCondition.lstClassification = _.values(data.alCheckTargetCondition.lstClassification ? data.alCheckTargetCondition.lstClassification : []);
                data.workTypeCondition.planLstWorkType = _.values(data.workTypeCondition.planLstWorkType ? data.workTypeCondition.planLstWorkType : []);
                data.workTypeCondition.actualLstWorkType = _.values(data.workTypeCondition.actualLstWorkType ? data.workTypeCondition.actualLstWorkType : []);
                data.workTimeCondition.planLstWorkTime = _.values(data.workTimeCondition.planLstWorkTime ? data.workTimeCondition.planLstWorkTime : []);
                data.workTimeCondition.actualLstWorkTime = _.values(data.workTimeCondition.actualLstWorkTime ? data.workTimeCondition.actualLstWorkTime : []);
                data.lstApplicationTypeCode = _.values(data.lstApplicationTypeCode ? data.lstApplicationTypeCode : []);
                data.erAlAtdItemConditionGroup1 = _.values(data.erAlAtdItemConditionGroup1 ? data.erAlAtdItemConditionGroup1 : []);
                data.erAlAtdItemConditionGroup2 = _.values(data.erAlAtdItemConditionGroup2 ? data.erAlAtdItemConditionGroup2 : []);
                _.forEach(data.erAlAtdItemConditionGroup1, (item) => {
                    item.countableAddAtdItems = _.values(item.countableAddAtdItems);
                    item.countableSubAtdItems = _.values(item.countableSubAtdItems);
                });
                _.forEach(data.erAlAtdItemConditionGroup2, (item) => {
                    item.countableAddAtdItems = _.values(item.countableAddAtdItems);
                    item.countableSubAtdItems = _.values(item.countableSubAtdItems);
                });
                if (self.isNewMode() && self.isExistedCode()) {
                    nts.uk.ui.dialog.alert({ messageId: "Msg_3" }).then(() => {
                        $("#errorAlarmWorkRecordCode").focus();
                    });
                } else {
                    service.update(data).done(() => {
                        nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => {
                            self.startPage(self.isNewMode() ? "U" + data.code : data.code);
                            if (self.lstErrorAlarm().length > 0) {
                                $("#errorAlarmWorkRecordName").focus();
                            } else {
                                $("#errorAlarmWorkRecordCode").focus();
                            }
                        });
                    });
                }
            }

        }

        remove() {
            let self = this;
            let data = self.selectedErrorAlarm().code();
            nts.uk.ui.dialog.confirm({ messageId: "Msg_618" }).ifYes(() => {
                service.remove(data).done(() => {
                    nts.uk.ui.dialog.info({ messageId: "Msg_16" }).then(() => {
                        self.startPage(null);
                        if (self.lstErrorAlarm().length > 0) {
                            $("#errorAlarmWorkRecordName").focus();
                        } else {
                            $("#errorAlarmWorkRecordCode").focus();
                        }
                    });
                });
            })
        }

        /* Tab 1 */
        openSelectAtdItemColorDialog() {
            let self = this;
            //Open dialog KDL021
            service.getAllAttendanceItem().done((lstItem) => {
                let lstItemCode = lstItem.map((item) => { return item.attendanceItemId; });
                nts.uk.ui.windows.setShared('Multiple', false);
                // example wait
                nts.uk.ui.windows.setShared('AllAttendanceObj', lstItemCode);
                nts.uk.ui.windows.setShared('SelectedAttendanceId', [self.selectedErrorAlarm().errorDisplayItem()]);
                nts.uk.ui.windows.sub.modal("at", "/view/kdl/021/a/index.xhtml").onClosed(() => {
                    let output = nts.uk.ui.windows.getShared("selectedChildAttendace");
                    if (!nts.uk.util.isNullOrUndefined(output) && output !== "") {
                        self.selectedErrorAlarm().errorDisplayItem(parseInt(output));
                    } else if (!nts.uk.util.isNullOrUndefined(output) && output == "") {
                        self.selectedErrorAlarm().errorDisplayItem(null);
                    }
                });
            });
        }

        /* Tab 2 */
        chooseEmployment() {
            let self = this;
            setShared('CDL002Params', {
                isMultiple: true,
                selectedCodes: self.selectedErrorAlarm().alCheckTargetCondition.lstEmployment(),
                showNoSelection: false,
            }, true);

            nts.uk.ui.windows.sub.modal("com", "/view/cdl/002/a/index.xhtml").onClosed(function() {
                var output = getShared('CDL002Output');
                if (output) {
                    output.sort();
                    self.selectedErrorAlarm().alCheckTargetCondition.lstEmployment(output);
                }
            });
        }

        chooseClassification() {
            let self = this;
            setShared('inputCDL003', {
                selectedCodes: self.selectedErrorAlarm().alCheckTargetCondition.lstClassification(),
                showNoSelection: false,
                isMultiple: true
            }, true);

            nts.uk.ui.windows.sub.modal("com", '/view/cdl/003/a/index.xhtml').onClosed(function(): any {
                var output = getShared('outputCDL003');
                if (output) {
                    output.sort();
                    self.selectedErrorAlarm().alCheckTargetCondition.lstClassification(output);
                }
            })
        }

        chooseJobTitle() {
            let self = this;
            nts.uk.ui.windows.setShared('inputCDL004', {
                baseDate: moment()._d,
                selectedCodes: self.selectedErrorAlarm().alCheckTargetCondition.lstJobTitle(),
                showNoSelection: false,
                isMultiple: true
            }, true);

            nts.uk.ui.windows.sub.modal("com", '/view/cdl/004/a/index.xhtml').onClosed(function(): any {
                let output = nts.uk.ui.windows.getShared('outputCDL004');
                if (output) {
                    output.sort();
                    self.selectedErrorAlarm().alCheckTargetCondition.lstJobTitle(output);
                }
            })
        }

        chooseBusinessType() {
            let self = this,
                param = {
                    codeList: self.selectedErrorAlarm().alCheckTargetCondition.lstBusinessType()
                };
            nts.uk.ui.windows.setShared("CDL024", param);
            nts.uk.ui.windows.sub.modal("com", "/view/cdl/024/index.xhtml").onClosed(() => {
                let output = getShared("currentCodeList");
                if (output) {
                    output.sort();
                    self.selectedErrorAlarm().alCheckTargetCondition.lstBusinessType(output);
                }
            });
        }
        /* End Tab 2 */
        /* Tab 3 */
        chooseWorkType(planOrActual) {
            service.getAllWorkType().done((lstWorkType: Array<WorkTypeDto>) => {
                let self = this;
                let workTypeCondition = self.selectedErrorAlarm().workTypeCondition;
                let lstSelectedCode = planOrActual === "plan" ? workTypeCondition.planLstWorkType() : workTypeCondition.actualLstWorkType();
                setShared('KDL002_Multiple', true);
                setShared('KDL002_AllItemObj', _.map(lstWorkType, (workType) => { return workType.workTypeCode; }));
                setShared('KDL002_SelectedItemId', lstSelectedCode);
                nts.uk.ui.windows.sub.modal('/view/kdl/002/a/index.xhtml', { title: '乖離時間の登録＞対象項目', }).onClosed(() => {
                    let results = getShared('KDL002_SelectedNewItem');
                    if (results) {
                        if (planOrActual === "plan") {
                            self.selectedErrorAlarm().workTypeCondition.planLstWorkType(results.map((result) => { return result.code; }));
                        } else {
                            self.selectedErrorAlarm().workTypeCondition.actualLstWorkType(results.map((result) => { return result.code; }));
                        }
                    }
                });
            });
        }

        chooseWorkTime(planOrActual) {
            let self = this;
            service.getAllWorkTime().done((lstWorkTime) => {
                let workTimeCondition = self.selectedErrorAlarm().workTimeCondition;
                let lstSelectedCode = planOrActual === "plan" ? workTimeCondition.planLstWorkTime() : workTimeCondition.actualLstWorkTime();
                setShared('kml001multiSelectMode', true);
                nts.uk.ui.windows.setShared('kml001selectAbleCodeList', lstWorkTime.map((worktime) => { return worktime.worktimeCode; }));
                nts.uk.ui.windows.setShared('kml001selectedCodeList', lstSelectedCode);
                nts.uk.ui.windows.sub.modal("/view/kdl/001/a/index.xhtml", { title: "割増項目の設定" }).onClosed(function() {
                    let results = getShared("kml001selectedCodeList");
                    if (results) {
                        if (planOrActual === "plan") {
                            self.selectedErrorAlarm().workTimeCondition.planLstWorkTime(results.sort());
                        } else {
                            self.selectedErrorAlarm().workTimeCondition.actualLstWorkTime(results.sort());
                        }
                    }
                });
            });
        }
        /* End Tab 3 */
        /* Tab 4 */
        openAtdItemConditionDialog() {
            let self = this,
                param = {
                };
            nts.uk.ui.windows.setShared("kdw007B", param);
            nts.uk.ui.windows.sub.modal("at", "/view/kdw/007/b/index.xhtml", { title: "計算式の設定" }).onClosed(() => {
                let output = getShared("kdw007BResult");
                if (output) {
                    debugger;
                }
            });
        }
        /* End Tab 4 */
    }

    export class ErrorAlarmWorkRecord {
        /* 会社ID */
        companyId: KnockoutObservable<string>;
        /* コード */
        code: KnockoutObservable<string>;
        /* 名称 */
        name: KnockoutObservable<string>;
        /* システム固定とする */
        fixedAtr: KnockoutObservable<number>;
        /* 使用する */
        useAtr: KnockoutObservable<number>;
        /* 区分 */
        typeAtr: KnockoutObservable<number>;
        /* 表示メッセージ */
        displayMessage: KnockoutObservable<string>;
        /* メッセージを太字にする */
        boldAtr: KnockoutObservable<number>;
        /* メッセージの色 */
        messageColor: KnockoutObservable<string>;
        /* エラーアラームを解除できる */
        cancelableAtr: KnockoutObservable<number>;
        /* エラー表示項目 */
        errorDisplayItem: KnockoutObservable<number>;
        errorDisplayItemName: KnockoutObservable<string>;
        /* チェック条件 */
        alCheckTargetCondition: AlarmCheckTargetCondition;
        /* 勤務種類の条件*/
        workTypeCondition: WorkTypeCondition;
        /* 就業時間帯の条件*/
        workTimeCondition: WorkTimeCondition;
        operatorBetweenPlanActual: KnockoutObservable<number>;
        lstApplicationTypeCode: KnockoutObservableArray<number>;
        operatorBetweenGroups: KnockoutObservable<number>;
        operatorGroup1: KnockoutObservable<number>;
        operatorGroup2: KnockoutObservable<number>;
        group2UseAtr: KnockoutObservable<boolean>;
        erAlAtdItemConditionGroup1: Array<ErAlAtdItemCondition>;
        erAlAtdItemConditionGroup2: Array<ErAlAtdItemCondition>;

        constructor(param?: ErrorAlarmWorkRecord) {
            this.companyId = param && param.companyId ? ko.observable(param.companyId) : ko.observable('');
            this.code = param && param.code ? ko.observable(param.code) : ko.observable('');
            this.name = param && param.name ? ko.observable(param.name) : ko.observable('');
            this.fixedAtr = param && param.fixedAtr ? ko.observable(param.fixedAtr) : ko.observable(0);
            this.useAtr = param && param.useAtr ? ko.observable(param.useAtr) : ko.observable(0);
            this.typeAtr = param && param.typeAtr ? ko.observable(param.typeAtr) : ko.observable(0);
            this.displayMessage = param && param.displayMessage ? ko.observable(param.displayMessage) : ko.observable('');
            this.boldAtr = param && param.boldAtr ? ko.observable(param.boldAtr) : ko.observable(0);
            this.messageColor = param && param.messageColor ? ko.observable(param.messageColor) : ko.observable('');
            this.cancelableAtr = param && param.cancelableAtr ? ko.observable(param.cancelableAtr) : ko.observable(0);
            this.errorDisplayItem = param && param.errorDisplayItem ? ko.observable(param.errorDisplayItem) : ko.observable(null);
            this.errorDisplayItemName = ko.observable("");
            this.alCheckTargetCondition = param && param.alCheckTargetCondition ? new AlarmCheckTargetCondition(param.alCheckTargetCondition) : new AlarmCheckTargetCondition(null);
            this.workTypeCondition = param && param.workTypeCondition ? new WorkTypeCondition(param.workTypeCondition) : new WorkTypeCondition(null);
            this.workTimeCondition = param && param.workTimeCondition ? new WorkTimeCondition(param.workTimeCondition) : new WorkTimeCondition(null);
            this.operatorBetweenPlanActual = param && param.operatorBetweenPlanActual ? ko.observable(param.operatorBetweenPlanActual) : ko.observable(0);
            this.lstApplicationTypeCode = param && param.lstApplicationTypeCode ? ko.observableArray(param.lstApplicationTypeCode) : ko.observableArray([]);
            this.operatorBetweenGroups = param && param.operatorBetweenGroups ? ko.observable(param.operatorBetweenGroups) : ko.observable(0);
            this.operatorGroup1 = param && param.operatorGroup1 ? ko.observable(param.operatorGroup1) : ko.observable(0);
            this.operatorGroup2 = param && param.operatorGroup2 ? ko.observable(param.operatorGroup2) : ko.observable(0);
            this.group2UseAtr = param && param.group2UseAtr ? ko.observable(param.group2UseAtr) : ko.observable(false);
            this.erAlAtdItemConditionGroup1 = param && param.erAlAtdItemConditionGroup1 ? param.erAlAtdItemConditionGroup1.map((con) => { return new ErAlAtdItemCondition(con.NO, con); }) : this.initListAtdItemCondition();
            this.erAlAtdItemConditionGroup2 = param && param.erAlAtdItemConditionGroup2 ? param.erAlAtdItemConditionGroup2.map((con) => { return new ErAlAtdItemCondition(con.NO, con); }) : this.initListAtdItemCondition();
            this.errorDisplayItem.subscribe((itemCode) => {
                if (itemCode) {
                    service.getAttendanceItemByCodes([itemCode]).done((lstItems) => {
                        if (lstItems && lstItems.length > 0) {
                            this.errorDisplayItemName(lstItems[0].attendanceItemName);
                        }
                    });
                } else {
                    this.errorDisplayItemName("");
                }
            });
            this.errorDisplayItem.valueHasMutated();
        }

        initListAtdItemCondition() {
            let resultList = [];
            resultList.push(new ErAlAtdItemCondition(0, null));
            resultList.push(new ErAlAtdItemCondition(1, null));
            resultList.push(new ErAlAtdItemCondition(2, null));
            return resultList;
        }
    }

    export class AlarmCheckTargetCondition {
        /* 勤務種別でしぼり込む */
        filterByBusinessType: KnockoutObservable<boolean>;
        /* 職位でしぼり込む */
        filterByJobTitle: KnockoutObservable<boolean>;
        /* 雇用でしぼり込む */
        filterByEmployment: KnockoutObservable<boolean>;
        /* 分類でしぼり込む */
        filterByClassification: KnockoutObservable<boolean>;
        /* 対象勤務種別*/
        lstBusinessType: KnockoutObservableArray<string>;
        /* 対象職位 */
        lstJobTitle: KnockoutObservableArray<string>;
        /* 対象雇用*/
        lstEmployment: KnockoutObservableArray<string>;
        /* 対象分類 */
        lstClassification: KnockoutObservableArray<string>;
        displayLstBusinessType: KnockoutObservable<string>;
        displayLstJobTitle: KnockoutObservable<string>;
        displayLstEmployment: KnockoutObservable<string>;
        displayLstClassification: KnockoutObservable<string>;

        constructor(param) {
            this.filterByBusinessType = param ? ko.observable(param.filterByBusinessType) : ko.observable(false);
            this.filterByJobTitle = param ? ko.observable(param.filterByJobTitle) : ko.observable(false);
            this.filterByEmployment = param ? ko.observable(param.filterByEmployment) : ko.observable(false);
            this.filterByClassification = param ? ko.observable(param.filterByClassification) : ko.observable(false);
            this.lstBusinessType = param ? ko.observableArray(param.lstBusinessType) : ko.observableArray([]);
            this.lstJobTitle = param ? ko.observableArray(param.lstJobTitle) : ko.observableArray([]);
            this.lstEmployment = param ? ko.observableArray(param.lstEmployment) : ko.observableArray([]);
            this.lstClassification = param ? ko.observableArray(param.lstClassification) : ko.observableArray([]);
            this.filterByBusinessType.subscribe((val) => {
                if (!val) {
                    $("#displayLstBusinessType").trigger("validate");
                }
            });
            this.filterByJobTitle.subscribe((val) => {
                if (!val) {
                    $("#displayLstJobTitle").trigger("validate");
                }
            });
            this.filterByEmployment.subscribe((val) => {
                if (!val) {
                    $("#displayLstEmployment").trigger("validate");
                }
            });
            this.filterByClassification.subscribe((val) => {
                if (!val) {
                    $("#displayLstClassification").trigger("validate");
                }
            });
            this.displayLstBusinessType = ko.observable("");
            this.displayLstJobTitle = ko.observable("");
            this.displayLstEmployment = ko.observable("");
            this.displayLstClassification = ko.observable("");
            this.lstBusinessType.subscribe((lstBussinessType) => {
                if (lstBussinessType && lstBussinessType.length > 0) {
                    let displayText = "";
                    let lstItem = [];
                    let dfd = $.Deferred();
                    for (let i = 0; i < lstBussinessType.length; i++) {
                        service.getBusinessTypeByCode(lstBussinessType[i]).done((businessType) => {
                            if (businessType) {
                                lstItem.push({ order: i, text: businessType.businessTypeName });
                            }
                            if (lstItem.length == lstBussinessType.length) {
                                dfd.resolve();
                            }
                        });
                    }
                    dfd.done(() => {
                        lstItem = _.orderBy(lstItem, ['order'], ['asc']);
                        for (let i = 0; i < lstItem.length; i++) {
                            if (displayText !== "") {
                                displayText = displayText + ", " + lstItem[i].text;
                            } else {
                                displayText = displayText + lstItem[i].text;
                            }
                            if (i === lstItem.length - 1) {
                                this.displayLstBusinessType(displayText);
                                $("#displayLstBusinessType").trigger('validate');
                            }
                        }
                    });
                } else {
                    this.displayLstBusinessType("");
                }
            });
            this.lstJobTitle.subscribe((lstJobTitle) => {
                let displayText = "";
                let allJobTitle = [];
                if (lstJobTitle && lstJobTitle.length > 0) {
                    service.findAllJobTitle().done((data) => {
                        if (data && data.length > 0) {
                            allJobTitle = data;
                        }
                    }).then(() => {
                        for (let i = 0; i < lstJobTitle.length; i++) {
                            for (let jobTitle of allJobTitle) {
                                if (lstJobTitle[i] === jobTitle.id) {
                                    if (displayText !== "") {
                                        displayText = displayText + ", " + jobTitle.name;
                                    } else {
                                        displayText = displayText + jobTitle.name;
                                    }
                                    if (i === lstJobTitle.length - 1) {
                                        this.displayLstJobTitle(displayText);
                                        $("#displayLstJobTitle").trigger('validate');
                                    }
                                }
                            }
                        }
                    });
                } else {
                    this.displayLstJobTitle("");
                }
            });
            this.lstEmployment.subscribe((lstEmpt) => {
                let displayText = "";
                if (lstEmpt && lstEmpt.length > 0) {
                    let lstItem = [];
                    let dfd = $.Deferred();
                    for (let i = 0; i < lstEmpt.length; i++) {
                        service.getEmploymentByCode(lstEmpt[i]).done((empt) => {
                            if (empt) {
                                lstItem.push({ order: i, text: empt.name });
                            }
                            if (lstItem.length == lstEmpt.length) {
                                dfd.resolve();
                            }
                        });
                    }
                    dfd.done(() => {
                        lstItem = _.orderBy(lstItem, ['order'], ['asc']);
                        for (let i = 0; i < lstItem.length; i++) {
                            if (displayText !== "") {
                                displayText = displayText + ", " + lstItem[i].text;
                            } else {
                                displayText = displayText + lstItem[i].text;
                            }
                            if (i === lstItem.length - 1) {
                                this.displayLstEmployment(displayText);
                                $("#displayLstEmployment").trigger('validate');
                            }
                        }
                    });
                } else {
                    this.displayLstEmployment("");
                }
            });
            this.lstClassification.subscribe((lstClss) => {
                let displayText = "";
                if (lstClss && lstClss.length > 0) {
                    let lstItem = [];
                    let dfd = $.Deferred();
                    for (let i = 0; i < lstClss.length; i++) {
                        service.getClassificationByCode(lstClss[i]).done((clss) => {
                            if (clss) {
                                lstItem.push({ order: i, text: clss.name });
                            }
                            if (lstItem.length == lstClss.length) {
                                dfd.resolve();
                            }
                        });
                    }
                    dfd.done(() => {
                        lstItem = _.orderBy(lstItem, ['order'], ['asc']);
                        for (let i = 0; i < lstItem.length; i++) {
                            if (displayText !== "") {
                                displayText = displayText + ", " + lstItem[i].text;
                            } else {
                                displayText = displayText + lstItem[i].text;
                            }
                            if (i === lstItem.length - 1) {
                                this.displayLstClassification(displayText);
                                $("#displayLstClassification").trigger('validate');
                            }
                        }
                    });
                } else {
                    this.displayLstClassification("");
                }
            });
            this.lstBusinessType.valueHasMutated();
            this.lstJobTitle.valueHasMutated();
            this.lstEmployment.valueHasMutated();
            this.lstClassification.valueHasMutated();
        }

        setData(param) {
            this.filterByBusinessType(param ? param.filterByBusinessType : false);
            this.filterByJobTitle(param ? param.filterByJobTitle : false);
            this.filterByEmployment(param ? param.filterByEmployment : false);
            this.filterByClassification(param ? param.filterByClassification : false);
            this.lstBusinessType(param ? param.lstBusinessType : []);
            this.lstJobTitle(param ? param.lstJobTitle : []);
            this.lstEmployment(param ? param.lstEmployment : []);
            this.lstClassification(param ? param.lstClassification : []);
        }
    }

    export class WorkTypeDto {
        workTypeCode: string;
        name: string;
    }

    export class WorkTypeCondition {

        useAtr: KnockoutObservable<boolean>;
        comparePlanAndActual: KnockoutObservable<number>;
        planFilterAtr: KnockoutObservable<boolean>;
        planLstWorkType: KnockoutObservableArray<string>;
        actualFilterAtr: KnockoutObservable<boolean>;
        actualLstWorkType: KnockoutObservableArray<string>;
        displayLstWorkTypePlan: KnockoutObservable<string>;
        displayLstWorkTypeActual: KnockoutObservable<string>;
        requireWorkTypeActual: any;

        constructor(param) {
            this.useAtr = param ? ko.observable(param.useAtr) : ko.observable(false);
            this.comparePlanAndActual = param ? ko.observable(param.comparePlanAndActual) : ko.observable(0);
            this.planFilterAtr = param ? ko.observable(param.planFilterAtr) : ko.observable(false);
            this.planLstWorkType = param ? ko.observable(param.planLstWorkType) : ko.observableArray([]);
            this.actualFilterAtr = param ? ko.observable(param.actualFilterAtr) : ko.observable(false);
            this.actualLstWorkType = param ? ko.observable(param.actualLstWorkType) : ko.observableArray([]);
            this.requireWorkTypeActual = ko.computed(() => {
                return this.comparePlanAndActual() != 1 && this.actualFilterAtr();
            });
            this.planFilterAtr.subscribe((val) => {
                if (!val) {
                    $("#displayLstWorkTypePlan").ntsError("clear");
                }
            });
            this.actualFilterAtr.subscribe((val) => {
                if (!val) {
                    $("#displayLstWorkTypeActual").ntsError("clear");
                }
            });
            this.displayLstWorkTypePlan = ko.observable("");
            this.displayLstWorkTypeActual = ko.observable("");
            this.planLstWorkType.subscribe((lstWorkTypeCode) => {
                if (lstWorkTypeCode && lstWorkTypeCode.length > 0) {
                    let displayText = "";
                    let lstWorkType = [];
                    service.getWorkTypeByListCode(lstWorkTypeCode).done((data) => {
                        if (data && data.length > 0) {
                            lstWorkType = data;
                        }
                    }).then(() => {
                        for (let i = 0; i < lstWorkTypeCode.length; i++) {
                            for (let workType of lstWorkType) {
                                if (lstWorkTypeCode[i] === workType.workTypeCode) {
                                    if (displayText !== "") {
                                        displayText = displayText + ", " + workType.name;
                                    } else {
                                        displayText = displayText + workType.name;
                                    }
                                    if (i === lstWorkTypeCode.length - 1) {
                                        this.displayLstWorkTypePlan(displayText);
                                    }
                                }
                            }
                        }
                    });
                } else {
                    this.displayLstWorkTypePlan("");
                }
            });
            this.actualLstWorkType.subscribe((lstWorkTypeCode) => {
                if (lstWorkTypeCode && lstWorkTypeCode.length > 0) {
                    let displayText = "";
                    let lstWorkType = [];
                    service.getWorkTypeByListCode(lstWorkTypeCode).done((data) => {
                        if (data && data.length > 0) {
                            lstWorkType = data;
                        }
                    }).then(() => {
                        for (let i = 0; i < lstWorkTypeCode.length; i++) {
                            for (let workType of lstWorkType) {
                                if (lstWorkTypeCode[i] === workType.workTypeCode) {
                                    if (displayText !== "") {
                                        displayText = displayText + ", " + workType.name;
                                    } else {
                                        displayText = displayText + workType.name;
                                    }
                                    if (i === lstWorkTypeCode.length - 1) {
                                        this.displayLstWorkTypeActual(displayText);
                                    }
                                }
                            }
                        }
                    });
                } else {
                    this.displayLstWorkTypeActual("");
                }
            });
            this.planLstWorkType.valueHasMutated();
            this.actualLstWorkType.valueHasMutated();
        }

        setData(param) {
            this.useAtr(param ? param.useAtr : false);
            this.comparePlanAndActual(param ? param.comparePlanAndActual : 0);
            this.planFilterAtr(param ? param.planFilterAtr : false);
            this.planLstWorkType(param ? param.planLstWorkType : []);
            this.actualFilterAtr(param ? param.actualFilterAtr : false);
            this.actualLstWorkType(param && param.actualLstWorkType ? param.actualLstWorkType : []);
        }
    }

    export class WorkTypeDto {
        code: string;
        name: string;
    }

    export class WorkTimeCondition {

        useAtr: KnockoutObservable<boolean>;
        comparePlanAndActual: KnockoutObservable<number>;
        planFilterAtr: KnockoutObservable<boolean>;
        planLstWorkTime: KnockoutObservableArray<string>;
        actualFilterAtr: KnockoutObservable<boolean>;
        actualLstWorkTime: KnockoutObservableArray<string>;
        displayLstWorkTimePlan: KnockoutObservable<string>;
        displayLstWorkTimeActual: KnockoutObservable<string>;
        requireWorkTimeActual: any;

        constructor(param) {
            this.useAtr = param ? ko.observable(param.useAtr) : ko.observable(false);
            this.comparePlanAndActual = param ? ko.observable(param.comparePlanAndActual) : ko.observable(0);
            this.planFilterAtr = param ? ko.observable(param.planFilterAtr) : ko.observable(false);
            this.planLstWorkTime = param ? ko.observable(param.planLstWorkTime) : ko.observableArray([]);
            this.actualFilterAtr = param ? ko.observable(param.actualFilterAtr) : ko.observable(false);
            this.actualLstWorkTime = param ? ko.observable(param.actualLstWorkTime) : ko.observableArray([]);
            this.requireWorkTimeActual = ko.computed(() => {
                return this.comparePlanAndActual() != 1 && this.actualFilterAtr();
            });
            this.planFilterAtr.subscribe((val) => {
                if (!val) {
                    $("#displayLstWorkTimePlan").ntsError("clear");
                }
            });
            this.actualFilterAtr.subscribe((val) => {
                if (!val) {
                    $("#displayLstWorkTimeActual").ntsError("clear");
                }
            });
            this.displayLstWorkTimePlan = ko.observable("");
            this.displayLstWorkTimeActual = ko.observable("");
            this.planLstWorkTime.subscribe((lstWorkTimeCode) => {
                if (lstWorkTimeCode && lstWorkTimeCode.length > 0) {
                    let displayText = "";
                    let lstWorkTime = [];
                    service.getAllWorkTime().done((data) => {
                        if (data && data.length > 0) {
                            lstWorkTime = data;
                        }
                    }).then(() => {
                        for (let i = 0; i < lstWorkTimeCode.length; i++) {
                            for (let workTime of lstWorkTime) {
                                if (lstWorkTimeCode[i] === workTime.worktimeCode) {
                                    if (displayText !== "") {
                                        displayText = displayText + ", " + workTime.workTimeName;
                                    } else {
                                        displayText = displayText + workTime.workTimeName;
                                    }
                                    if (i === lstWorkTimeCode.length - 1) {
                                        this.displayLstWorkTimePlan(displayText);
                                    }
                                }
                            }
                        }
                    });
                } else {
                    this.displayLstWorkTimePlan("");
                }
            });
            this.actualLstWorkTime.subscribe((lstWorkTimeCode) => {
                if (lstWorkTimeCode && lstWorkTimeCode.length > 0) {
                    let displayText = "";
                    let lstWorkTime = [];
                    service.getAllWorkTime().done((data) => {
                        if (data && data.length > 0) {
                            lstWorkTime = data;
                        }
                    }).then(() => {
                        for (let i = 0; i < lstWorkTimeCode.length; i++) {
                            for (let workTime of lstWorkTime) {
                                if (lstWorkTimeCode[i] === workTime.worktimeCode) {
                                    if (displayText !== "") {
                                        displayText = displayText + ", " + workTime.workTimeName;
                                    } else {
                                        displayText = displayText + workTime.workTimeName;
                                    }
                                    if (i === lstWorkTimeCode.length - 1) {
                                        this.displayLstWorkTimeActual(displayText);
                                    }
                                }
                            }
                        }
                    });
                } else {
                    this.displayLstWorkTimeActual("");
                }
            });
            this.planLstWorkTime.valueHasMutated();
            this.actualLstWorkTime.valueHasMutated();
        }

        setData(param) {
            this.useAtr(param ? param.useAtr : false);
            this.comparePlanAndActual(param ? param.comparePlanAndActual : 0);
            this.planFilterAtr(param ? param.planFilterAtr : false);
            this.planLstWorkTime(param ? param.planLstWorkTime : []);
            this.actualFilterAtr(param ? param.actualFilterAtr : false);
            this.actualLstWorkTime(param && param.actualLstWorkTime ? param.actualLstWorkTime : []);
        }
    }

    export class ErAlAtdItemCondition {

        targetNO: KnockoutObservable<number>;
        conditionAtr: KnockoutObservable<number>;
        useAtr: KnockoutObservable<boolean>;
        uncountableAtdItem: KnockoutObservable<number>;
        countableAddAtdItems: KnockoutObservableArray<number>;
        countableSubAtdItems: KnockoutObservableArray<number>;
        conditionType: KnockoutObservable<number>;
        compareOperator: KnockoutObservable<number>;
        singleAtdItem: KnockoutObservable<number>;
        compareStartValue: KnockoutObservable<number>;
        compareEndValue: KnockoutObservable<number>;

        displayLeftCompare: KnockoutObservable<string>;
        displayLeftOperator: KnockoutObservable<string>;
        displayTarget: KnockoutObservable<string>;
        displayRightCompare: KnockoutObservable<string>;
        displayRightOperator: KnockoutObservable<string>;

        constructor(NO, param) {
            let self = this;
            self.targetNO = ko.observable(NO);
            self.conditionAtr = param ? ko.observable(param.conditionAtr) : ko.observable(0);
            self.useAtr = param ? ko.observable(param.useAtr) : ko.observable(false);
            self.uncountableAtdItem = param ? ko.observable(param.uncountableAtdItem) : ko.observable(null);
            self.countableAddAtdItems = param && param.countableAddAtdItems ? ko.observableArray(param.countableAddAtdItems) : ko.observableArray([]);
            self.countableSubAtdItems = param && param.countableSubAtdItems ? ko.observableArray(param.countableSubAtdItems) : ko.observableArray([]);
            self.conditionType = param ? ko.observable(param.conditionType) : ko.observable(0);
            self.singleAtdItem = param ? ko.observable(param.singleAtdItem) : ko.observable(null);
            self.compareStartValue = param ? ko.observable(param.compareStartValue) : ko.observable(0);
            self.compareEndValue = param ? ko.observable(param.compareEndValue) : ko.observable(0);
            self.compareOperator = param ? ko.observable(param.compareOperator) : ko.observable(0);
            self.displayLeftCompare = ko.observable("");
            self.displayLeftOperator = ko.observable("");
            self.displayTarget = ko.observable("");
            self.displayRightCompare = ko.observable("");
            self.displayRightOperator = ko.observable("");
            self.setTextDisplay();
        }

        setTextDisplay() {
            let self = this;
            if (self.useAtr()) {
                self.setDisplayTarget();
                self.setDisplayOperator();
                self.setDisplayCompare();
            } else {
                self.displayLeftCompare("");
                self.displayLeftOperator("");
                self.displayTarget("");
                self.displayRightCompare("");
                self.displayRightOperator("");
            }
        }

        setDisplayOperator() {
            let self = this;
            self.displayLeftOperator("");
            self.displayRightOperator("");
            switch (self.compareOperator()) {
                case 0:
                    self.displayLeftOperator("＝");
                    break;
                case 1:
                    self.displayLeftOperator("≠");
                    break;
                case 2:
                    self.displayLeftOperator("＞");
                    break;
                case 3:
                    self.displayLeftOperator("≧");
                    break;
                case 4:
                    self.displayLeftOperator("＜");
                    break;
                case 5:
                    self.displayLeftOperator("≦");
                    break;
                case 6:
                    self.displayLeftOperator("＜");
                    self.displayRightOperator("＜");
                    break;
                case 7:
                    self.displayLeftOperator("≦");
                    self.displayRightOperator("≦");
                    break;
                case 8:
                    self.displayLeftOperator("＜");
                    self.displayRightOperator("＜");
                    break;
                case 9:
                    self.displayLeftOperator("≦");
                    self.displayRightOperator("≦");
                    break;
            }
        }

        setDisplayCompare() {
            let self = this;
            let conditionAtr = self.conditionAtr();
            if (self.compareOperator() > 5) {
                // Compare with a range
                let rawStartValue = self.compareStartValue();
                let rawEndValue = self.compareEndValue();
                let textDisplayLeftCompare = (conditionAtr === 0 || conditionAtr === 3) ? rawStartValue : nts.uk.time.parseTime(parseInt(rawStartValue), true).format();
                let textDisplayRightCompare = (conditionAtr === 0 || conditionAtr === 3) ? rawEndValue : nts.uk.time.parseTime(parseInt(rawEndValue), true).format();
                self.displayLeftCompare(textDisplayLeftCompare);
                self.displayRightCompare(textDisplayRightCompare);
            } else {
                // Compare with single value
                if (self.conditionType() === 0) {
                    // If is compare with a fixed value
                    let rawValue = self.compareStartValue();
                    let textDisplayLeftCompare = (conditionAtr === 0 || conditionAtr === 3) ? rawValue : nts.uk.time.parseTime(parseInt(rawValue), true).format();
                    self.displayLeftCompare(textDisplayLeftCompare);
                    self.displayRightCompare("");
                } else {
                    // If is compare with a attendance item
                    if (self.singleAtdItem()) {
                        service.getAttendanceItemByCodes([self.singleAtdItem()]).done((lstItems) => {
                            if (lstItems && lstItems.length > 0) {
                                self.displayLeftCompare(lstItems[0].attendanceItemName);
                                self.displayRightCompare("");
                            }
                        });
                    }
                }
            }
        }

        setDisplayTarget() {
            let self = this;
            self.displayTarget("");
            if (self.conditionAtr() === 2) {
                if (self.uncountableAtdItem()) {
                    service.getAttendanceItemByCodes([self.uncountableAtdItem()]).done((lstItems) => {
                        if (lstItems && lstItems.length > 0) {
                            self.displayTarget(lstItems[0].attendanceItemName);
                        }
                    });
                }
            } else {
                if (self.countableAddAtdItems().length > 0) {
                    service.getAttendanceItemByCodes(self.countableAddAtdItems()).done((lstItems) => {
                        if (lstItems && lstItems.length > 0) {
                            for (let i = 0; i < lstItems.length; i++) {
                                let operator = (i === (lstItems.length - 1)) ? "" : " + ";
                                self.displayTarget(self.displayTarget() + lstItems[i].attendanceItemName + operator);
                            }
                        }
                    }).then(() => {
                        if (self.countableSubAtdItems().length > 0) {
                            service.getAttendanceItemByCodes(self.countableSubAtdItems()).done((lstItems) => {
                                if (lstItems && lstItems.length > 0) {
                                    for (let i = 0; i < lstItems.length; i++) {
                                        let operator = (i === (lstItems.length - 1)) ? "" : " - ";
                                        let beforeOperator = (i === 0) ? " - " : "";
                                        self.displayTarget(self.displayTarget() + beforeOperator + lstItems[i].attendanceItemName + operator);
                                    }
                                }
                            })
                        }
                    });
                } else if (self.countableSubAtdItems().length > 0) {
                    service.getAttendanceItemByCodes(self.countableSubAtdItems()).done((lstItems) => {
                        if (lstItems && lstItems.length > 0) {
                            for (let i = 0; i < lstItems.length; i++) {
                                let operator = (i === (lstItems.length - 1)) ? "" : " - ";
                                let beforeOperator = (i === 0) ? " - " : "";
                                self.displayTarget(self.displayTarget() + beforeOperator + lstItems[i].attendanceItemName + operator);
                            }
                        }
                    })
                }

            }
        }

        openAtdItemConditionDialog() {
            let self = this;
            let param = ko.mapping.toJS(this);
            nts.uk.ui.windows.setShared("KDW007BParams", param, true);
            nts.uk.ui.windows.sub.modal("at", "/view/kdw/007/b/index.xhtml", { title: "計算式の設定" }).onClosed(() => {
                let output = getShared("KDW007BResult");
                if (output) {
                    self.targetNO(output.targetNO);
                    self.conditionAtr(output.conditionAtr);
                    self.useAtr(true);
                    self.uncountableAtdItem(output.uncountableAtdItem);
                    self.countableAddAtdItems(output.countableAddAtdItems);
                    self.countableSubAtdItems(output.countableSubAtdItems);
                    self.conditionType(output.conditionType);
                    self.singleAtdItem(output.singleAtdItem);
                    self.compareStartValue(output.compareStartValue);
                    self.compareEndValue(output.compareEndValue);
                    self.compareOperator(output.compareOperator);
                }
                self.setTextDisplay();
            });
        }

        setData(NO, param) {
            let self = this;
            self.targetNO(NO);
            self.conditionAtr(param ? param.conditionAtr : 0);
            self.useAtr(param ? param.useAtr : false);
            self.uncountableAtdItem(param ? param.uncountableAtdItem : null);
            self.countableAddAtdItems(param && param.countableAddAtdItems ? param.countableAddAtdItems : []);
            self.countableSubAtdItems(param && param.countableSubAtdItems ? param.countableSubAtdItems : []);
            self.conditionType(param ? param.conditionType : 0);
            self.singleAtdItem(param ? param.singleAtdItem : null);
            self.compareStartValue(param && param.compareStartValue ? param.compareStartValue : 0);
            self.compareEndValue(param && param.compareEndValue ? param.compareEndValue : 0);
            self.compareOperator(param ? param.compareOperator : 0);
            self.setTextDisplay();
        }
    }

}