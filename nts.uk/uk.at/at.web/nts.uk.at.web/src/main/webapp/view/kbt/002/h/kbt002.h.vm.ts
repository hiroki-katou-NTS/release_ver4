module nts.uk.at.view.kbt002.h {
    export module viewmodel {
        import alert = nts.uk.ui.dialog.alert;
        import modal = nts.uk.ui.windows.sub.modal;
        import setShared = nts.uk.ui.windows.setShared;
        import getShared = nts.uk.ui.windows.getShared;
        import block = nts.uk.ui.block;
        import dialog = nts.uk.ui.dialog;
        import getText = nts.uk.resource.getText;
        import windows = nts.uk.ui.windows;

        export class ScreenModel {
            execLogList: KnockoutObservableArray<any>;
            gridListColumns: KnockoutObservableArray<any>;
            enable: KnockoutObservable<boolean>;
            required: KnockoutObservable<boolean>;
            dateValue: KnockoutObservable<any>;
            startDateString: KnockoutObservable<string>;
            endDateString: KnockoutObservable<string>;
            constructor() {
                let self = this;
                self.execLogList = ko.observableArray([]);

                self.enable = ko.observable(true);
                self.required = ko.observable(true);

                self.startDateString = ko.observable("");
                self.endDateString = ko.observable("");
                self.dateValue = ko.observable({});

                self.startDateString.subscribe(function(value) {
                    self.dateValue().startDate = value;
                    self.dateValue.valueHasMutated();
                });

                self.endDateString.subscribe(function(value) {
                    self.dateValue().endDate = value;
                    self.dateValue.valueHasMutated();
                });
                var today = new Date();
                var dd = today.getDate();
                var mm = today.getMonth() + 1;
                var yyyy = today.getFullYear();
                self.startDateString(yyyy + "/" + mm + "/" + dd);
                self.endDateString(yyyy + "/" + mm + "/" + dd);
            }

            // Start page
            start(): JQueryPromise<any> {
                let self = this;
                var dfd = $.Deferred();
                var sharedData = nts.uk.ui.windows.getShared('inputDialogH');
                if (sharedData) {
                    service.getProcExecList(sharedData.execItemCd).done(data => {
                        let execLogs = [];
                        _.forEach(data, function(item) {
                            execLogs.push(new ExecutionLog({ lastExecDateTime: item.lastExecDateTime, execItemCd: item.execItemCd, overallStatus: item.overallStatus, overallError: item.overallError, execId: item.execId }))
                        });
                        self.execLogList(execLogs);
                        self.loadIgrid();
                        dfd.resolve();
                    });
                }
                return dfd.promise();
            }


            closeDialog() {
                windows.close();
            }

            loadIgrid() {
                let self = this;
                var execItemCdHeader = '<button tabindex="5" class="setting small" style="margin-top: 0px !important;margin-bottom: 0px !important;"  {{if ${overallStatus}!= "異常終了" }}   disabled="true" {{/if}} onclick="openDialogG(this, \'${execItemCd}\',\'${execId}\')"  >' + getText("KBT002_159") + '</button>';
                // var execItemCdHeader = '<button tabindex="5" class="setting small" style="margin: 0px !important;" data-bind="click: function(data, event) { openDialogG(data, event)}" data-code="${execItemCd}" >' + getText("KBT002_159") + '</button>';
                $("#grid").igGrid({
                    primaryKey: "execItemCd",
                    height: 520,
                    dataSource: self.execLogList(),
                    autoGenerateColumns: false,
                    alternateRowStyles: false,
                    dataSourceType: "json",
                    // autoCommit: true,
                    //virtualization: true,
                    // rowVirtualization: false,
                    //virtualizationMode: "continuous",
                    //ntsControl: 'execItemCd'
                    //, template: execItemCdHeader
                    columns: [
                        //ProcessExecutionLogHistory
                        { key: "lastExecDateTime", width: "180px", height: "25px", headerText: nts.uk.resource.getText('KBT002_156'), dataType: "string", formatter: _.escape },
                        { key: "overallStatus", width: "120px", height: "25px", headerText: nts.uk.resource.getText('KBT002_157'), dataType: "string", formatter: _.escape },
                        { key: "execItemCd", width: "80px", height: "25px", headerText: '', dataType: "string", template: execItemCdHeader },
                        { key: "overallError", width: "450px", height: "25px", headerText: nts.uk.resource.getText('KBT002_158'), dataType: "string", formatter: _.escape },
                        { key: "execId", dataType: "string", formatter: _.escape, hidden: true }
                    ],
                    features: [
                        {
                            name: "Updating",
                            showDoneCancelButtons: false,
                            enableAddRow: false,
                            enableDeleteRow: false,
                            editMode: 'cell',
                            columnSettings: [
                                { columnKey: "execItemCd", readOnly: true },
                                { columnKey: "lastExecDateTime", readOnly: true },
                                { columnKey: "overallStatus", readOnly: true },
                                { columnKey: "overallError", readOnly: true },
                            ]
                        },
                        {
                            name: "Selection",
                            mode: "row",
                            multipleSelection: false,
                            //  touchDragSelect: false, // this is true by default
                            multipleCellSelectOnClick: false
                        }, {
                            name: 'Paging',
                            pageSize: 15,
                            currentPageIndex: 0
                        }
                    ],
                    ntsControls: [{ name: 'execItemCd', text: 'execItemCd', click: function() { alert("Button!!"); }, controlType: 'button' }]
                });

            }
            /*
            openDialogG(data, event) {
                let self = this;
                block.grayout();
                var execItemCd = $(event.target).data("code");
                var execLog = _.find(self.execLogList(), function(o) { return o.execItemCd == execItemCd; });
                setShared('inputDialogG', { execLog: execLog });
                modal("/view/kbt/002/g/index.xhtml").onClosed(function() {
                    block.clear();
                });
            }
    */
            search() {
                let self = this;
                if (!nts.uk.ui.errors.hasError()) {
                    var today = new Date();
                    var dd = today.getDate();
                    var mm = today.getMonth() + 1;
                    var yyyy = today.getFullYear();
                    var startDateSplit = self.dateValue().startDate.split("/");
                    var endDateSplit = self.dateValue().endDate.split("/");
                    var yearEndDate = new Number(endDateSplit[0]);
                    var monthEndDate = new Number(endDateSplit[1]);
                    var dayEndDate = new Number(endDateSplit[2]);
                    if (yearEndDate > yyyy || monthEndDate > mm || dayEndDate > dd) {
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_1077" });
                    } else {
                        var sharedData = nts.uk.ui.windows.getShared('inputDialogH');
                        if (sharedData) {
                            var startDate = new Date(startDateSplit[0],startDateSplit[1]-1,startDateSplit[2]);
                            var endDate = new Date(endDateSplit[0],endDateSplit[1]-1,endDateSplit[2]);
                            //ProcessExecutionDateParam
                            var param = new ProcessExecutionDateParam(sharedData.execItemCd,moment.utc(startDate,"YYYY/MM/DD"),moment.utc(endDate,"YYYY/MM/DD"));
                            service.findListDateRange(param).done(data => {
                                let execLogs = [];
                                _.forEach(data, function(item) {
                                    execLogs.push(new ExecutionLog({ lastExecDateTime: item.lastExecDateTime, execItemCd: item.execItemCd, overallStatus: item.overallStatus, overallError: item.overallError, execId: item.execId }))
                                });
                                $("#grid").igGrid("option", "dataSource",execLogs);
                            });
                        }
                    }
                }
            }

        }

    }
    export interface IExecutionLog {
        lastExecDateTime: string;
        execItemCd: string;
        overallStatus: string;
        overallError: string;
        execId: string;
    }

    //        export class ExecutionLog {
    //            lastExecDateTime:    KnockoutObservable<string> = ko.observable('');
    //            execItemCd:          KnockoutObservable<string> = ko.observable('');
    //            overallStatus:       KnockoutObservable<string> = ko.observable('');
    //            overallError:        KnockoutObservable<string> = ko.observable('');
    //            constructor(param: IExecutionLog) {
    //                let self = this;
    //                self.execItemCd(param.execItemCd || '');
    //                self.overallStatus(param.overallStatus || '');
    //                self.overallError(param.overallError);
    //                self.lastExecDateTime(param.lastExecDateTime || '');
    //            }
    //        }
    export class ExecutionLog {
        lastExecDateTime: string;
        execItemCd: string;
        overallStatus: string;
        overallError: string;
        execId: string;
        constructor(param: IExecutionLog) {
            let self = this;
            self.execItemCd = param.execItemCd;
            self.overallStatus = param.overallStatus;
            self.overallError = param.overallError;
            self.lastExecDateTime = param.lastExecDateTime;
            self.execId = param.execId;
        }
    }
    
    export class ProcessExecutionDateParam {
        execItemCd: string;
        startDate: any;
        endDate: any;
        constructor(execItemCd: string,startDate: any,endDate: any) {
            let self = this;
            self.execItemCd = execItemCd;
            self.startDate = startDate;
            self.endDate = endDate;
        }
    }


}


function openDialogG(element, execItemCd, execId) {
    nts.uk.ui.block.grayout();
    nts.uk.at.view.kbt002.h.service.getLogHistory(execItemCd, execId).done(function(x) {
        nts.uk.ui.windows.setShared('inputDialogG', { execLog: _.assign(x,{taskLogExecId: execId}) });
        nts.uk.ui.windows.sub.modal("/view/kbt/002/g/index.xhtml").onClosed(function() {
            nts.uk.ui.block.clear();
        });
    });
}

