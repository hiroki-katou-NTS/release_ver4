module nts.uk.at.view.kbt002.f {
    export module viewmodel {
        import alert = nts.uk.ui.dialog.alert;
        import modal = nts.uk.ui.windows.sub.modal;
        import setShared = nts.uk.ui.windows.setShared;
        import getShared = nts.uk.ui.windows.getShared;
        import block = nts.uk.ui.block;
        import dialog = nts.uk.ui.dialog;
        import getText = nts.uk.resource.getText;
        
        export class ScreenModel {
            execLogList: KnockoutObservableArray<any> = ko.observableArray([]);
            gridListColumns: KnockoutObservableArray<any>;
            selectedExecCd: KnockoutObservable<string> = ko.observable('');
            currentExecLog : KnockoutObservable<any> = ko.observable({});
            constructor() {
                var self = this;
                self.execLogList([]);
                
                self.gridListColumns = ko.observableArray([
                    { headerText: getText("KBT002_127"), key: 'execItemCd', width: 50 },
                    { headerText: getText("KBT002_128"), key: 'execItemName', width: 180, formatter: _.escape },
                    { headerText: '', key: 'currentStatusCd', width: 1, hidden: true},
                    { headerText: getText("KBT002_129"), key: 'currentStatus', width: 70, formatter: _.escape },
                    { headerText: getText("KBT002_130"), key: 'lastExecDateTime', width: 180, formatter: _.escape },
                    { headerText: '', key: 'overallStatusCd', width: 1, hidden: true},
                    { headerText: getText("KBT002_143"), key: 'overallStatus', width: 100, formatter: _.escape },
                    {
                        headerText: "", key: 'execItemCd', width: 55, unbound: true, dataType: "string",
                        template: '<button class="setting small" data-bind="click: function(data, event) { openDialogG(data, event)}, enable: {{if ((${overallStatusCd} != "") && (${overallStatusCd} == 3)) }}true{{else}} false {{/if}}" data-code="${execItemCd}" style="margin-left: 7px;">' + getText("KBT002_144") + '</button>',
                    },
                    { headerText: getText("KBT002_131"), key: 'nextExecDateTime', width: 180, formatter: _.escape },
                    {
                        headerText: "", key: 'execItemCd', width: 85, unbound: true, dataType: "string",
                        template: '<button class="setting small" data-bind="click: function(data, event) { execute(data, event)}, enable: {{if ((${currentStatusCd} != "") && (${currentStatusCd} != 0)) }}true{{else}} false {{/if}}" data-code="${execItemCd}" style="margin-left: 7px;">' + getText("KBT002_132") + '</button>',
                    },
                    {
                        headerText: "", key: 'execItemCd', width: 55, unbound: true, dataType: "string",
                        template: '<button class="setting small" data-bind="click: function(data, event) { terminate(data, event)}, enable: {{if ((${currentStatusCd} != "") && (${currentStatusCd} == 0)) }}true{{else}} false {{/if}}" data-code="${execItemCd}" style="margin-left: 7px;">' + getText("KBT002_133") + '</button>',
                    },
                    {
                        headerText: "", key: 'execItemCd', width: 55, unbound: true, dataType: "string",
                        template: '<button class="setting small" onclick="openDlg(this)" data-code="${execItemCd}" style="margin-left: 7px;">' + getText("KBT002_147") + '</button>',
                    },
                ]);
                
                self.selectedExecCd.subscribe(execItemCd => {
                    if (!nts.uk.text.isNullOrEmpty(execItemCd)) {
                        let data = _.filter(self.execLogList(), function(o) { return o.execItemCd == execItemCd; });
                        if (data[0]) {
                            self.currentExecLog(new ExecutionLog(data[0]));
                        }
                    }
                });
            }
            
            // Start page
            start() {
                let self = this;
                var dfd = $.Deferred();
                
                $.when(self.getProcExecLogList()).done(()=>{
                    dfd.resolve();
                });
                return dfd.promise();
            }
            
            private getProcExecLogList(savedExecItemCd? : string) : JQueryPromise<void> {
                let self = this;
                let dfd = $.Deferred<void>();
                
                self.execLogList([]);
                service.getProcExecLogList().done(function(execLogList) {
                    if (execLogList && execLogList.length > 0) {
                        self.execLogList(execLogList);
                        if (nts.uk.text.isNullOrEmpty(savedExecItemCd)) {
                            self.selectedExecCd(execLogList[0].execItemCd);
                        } else {
                            self.selectedExecCd(savedExecItemCd);
                        }
                        
                    } else {
                        alert({ messageId: "Msg_851" });
                    }
                    dfd.resolve();
                });
                return dfd.promise();
            }
            
            openDialogG(data, event){
                let self = this;
                block.grayout();
                var execItemCd = $(event.target).data("code");
                var execLog = _.find(self.execLogList(), function(o) { return o.execItemCd == execItemCd; });
                setShared('inputDialogG', {execLog: execLog});
                modal("/view/kbt/002/g/index.xhtml").onClosed(function(){
                    block.clear();
                });    
            }
            
             execute(data, event){
                let self = this;
//                var dfd = $.Deferred();
                block.grayout();
                let command: any = self.toJsonObject();
                service.execute(command).done(function() {
//                    $.when(self.getProcExecLogList()).done(()=>{
                        block.clear();
//                        dfd.resolve();
//                    });
                });
//                return dfd.promise();
            }
            
            terminate(data, event){
                let self = this;
                block.grayout();
//                var dfd = $.Deferred();
                let command: any = self.toJsonObject();
                service.terminate(command).done(function() {
//                    $.when(self.getProcExecLogList()).done(()=>{
                        block.clear();
//                        dfd.resolve();
//                    });
                });
//                return dfd.promise();
            }
            
            /**
             * toJsonObject
             */
            private toJsonObject(): any {
                let self = this;

                // to JsObject
                let command: any = {};
                command.execType = 1;
                command.companyId = self.currentExecLog().companyId();
                command.execItemCd = self.currentExecLog().execItemCd();
                command.execItemName = self.currentExecLog().execItemName();
                command.currentStatusCd = self.currentExecLog().currentStatusCd();
                command.currentStatus = self.currentExecLog().currentStatus();
                command.overallStatusCd = self.currentExecLog().overallStatusCd();
                command.overallStatus = self.currentExecLog().overallStatus();
                command.overallError = self.currentExecLog().overallError();
                command.prevExecDateTime = self.currentExecLog().prevExecDateTime();
                command.schCreateStart = self.currentExecLog().schCreateStart();
                command.schCreateEnd = self.currentExecLog().schCreateEnd();
                command.dailyCreateStart = self.currentExecLog().dailyCreateStart();
                command.dailyCreateEnd = self.currentExecLog().dailyCreateEnd();
                command.dailyCalcStart = self.currentExecLog().dailyCalcStart();
                command.dailyCalcEnd = self.currentExecLog().dailyCalcEnd();
                command.execId = self.currentExecLog().execId();

                return command;
            }
        }
        
        export interface ITaskLog {
            taskId: string;
            status: number;
        }
        
        export class TaskLog {
            taskId: KnockoutObservable<string> = ko.observable('');
            status: KnockoutObservable<number> = ko.observable(null);
            constructor(param: ITaskLog) {
                let self = this;
                self.taskId(param.taskId || '');
                self.status(param.status);
            }
        }
        
        export interface IExecutionLog {
            execItemCd:          string;
            companyId:           string;
            execItemName:        string;
            currentStatusCd:     number;
            currentStatus:       string;
            overallStatusCd:     number;
            overallStatus:       string;
            overallError:        number;
            prevExecDateTime:    string;
            schCreateStart:      string;
            schCreateEnd:        string;
            dailyCreateStart:    string;
            dailyCreateEnd:      string;
            dailyCalcStart:      string;
            dailyCalcEnd:        string;
            execId:              string;
            prevExecDateTimeEx:  string;
            taskLogList:         Array<TaskLog>;
            
        }
        
        export class ExecutionLog {
            execItemCd:          KnockoutObservable<string> = ko.observable('');
            companyId:           KnockoutObservable<string> = ko.observable('');
            execItemName:        KnockoutObservable<string> = ko.observable('');
            currentStatusCd:     KnockoutObservable<number> = ko.observable(null);
            currentStatus:       KnockoutObservable<string> = ko.observable('');
            overallStatusCd:     KnockoutObservable<number> = ko.observable(null);
            overallStatus:       KnockoutObservable<string> = ko.observable('');
            overallError:        KnockoutObservable<number> = ko.observable(null);
            prevExecDateTime:    KnockoutObservable<string> = ko.observable('');
            schCreateStart:      KnockoutObservable<string> = ko.observable('');
            schCreateEnd:        KnockoutObservable<string> = ko.observable('');
            dailyCreateStart:    KnockoutObservable<string> = ko.observable('');
            dailyCreateEnd:      KnockoutObservable<string> = ko.observable('');
            dailyCalcStart:      KnockoutObservable<string> = ko.observable('');
            dailyCalcEnd:        KnockoutObservable<string> = ko.observable('');
            execId:              KnockoutObservable<string> = ko.observable('');
            prevExecDateTimeEx:  KnockoutObservable<string> = ko.observable('');
            taskLogList:         KnockoutObservableArray<TaskLog> = ko.observableArray([]);
            constructor(param: IExecutionLog) {
                let self = this;
                self.execItemCd(param.execItemCd || '');
                self.companyId(param.companyId || '');
                self.execItemName(param.execItemName || '');
                self.currentStatusCd(param.currentStatusCd);
                self.currentStatus(param.currentStatus || '');
                self.overallStatusCd(param.overallStatusCd);
                self.overallStatus(param.overallStatus || '');
                self.overallError(param.overallError);
                self.prevExecDateTime(param.prevExecDateTime || '');
                self.schCreateStart(param.schCreateStart || '');
                self.schCreateEnd(param.schCreateEnd || '');
                self.dailyCreateStart(param.dailyCreateStart || '');
                self.dailyCreateEnd(param.dailyCreateEnd || '');
                self.dailyCalcStart(param.dailyCalcStart || '');
                self.dailyCalcEnd(param.dailyCalcEnd || '');
                self.execId(param.execId || '');
                self.prevExecDateTimeEx(param.prevExecDateTimeEx || '');
                self.taskLogList(param.taskLogList || []);
            }
        }
        
        export interface EnumConstantDto {
            value: number;
            fieldName: string;
            localizedName: string;
        }
    }
}