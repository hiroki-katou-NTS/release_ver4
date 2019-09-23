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
            taskId: KnockoutObservable<string> = ko.observable("");
            isOnceMessage101: KnockoutObservable<boolean> = ko.observable(true);
            isOnceExecType: KnockoutObservable<boolean> = ko.observable(true);
            isOnceCurrentStatus: KnockoutObservable<boolean> = ko.observable(true);
            isOnceInterupt: KnockoutObservable<boolean> = ko.observable(true);
            isCreateSchedule: KnockoutObservable<boolean> = ko.observable(true);
            taskTerminate: KnockoutObservable<any> = ko.observable("");
            constructor() {
                var self = this;
                self.execLogList([]);
                
                self.gridListColumns = ko.observableArray([
                    { headerText: getText("KBT002_127"), key: 'execItemCd', width: 50 },
                    { headerText: getText("KBT002_128"), key: 'execItemName', width: 180, formatter: _.escape },
                    { headerText: '', key: 'currentStatusCd', width: 1, hidden: true},
                    { headerText: getText("KBT002_129"), key: 'currentStatus', width: 70, formatter: _.escape },
                    { headerText: getText("KBT002_208"), key: 'lastExecDateTime', width: 180, formatter: _.escape },
                    { headerText: getText("KBT002_209"), key: 'lastEndExecDateTime', width: 180, formatter: _.escape },
                    { headerText: getText("KBT002_210"), key: 'rangeDateTime', width: 80, formatter: _.escape },
                    { headerText: '', key: 'overallStatusCd', width: 1, hidden: true},
                    { headerText: getText("KBT002_211"), key: 'overallStatus', width: 68, formatter: _.escape },
                    { headerText: getText("KBT002_212"), key: 'errorSystemText', width: 90, formatter: _.escape },
                    { headerText: getText("KBT002_213"), key: 'errorBusinessText', width: 70, formatter: _.escape }, 
                    
                    {
                        headerText: "", key: 'execItemCd', width: 45, unbound: true, dataType: "string",
                        formatter: function(execItemCd, record) {
                        	let disable = true;                            
//                        	if ((record.overallStatusCd != '') && (record.overallStatusCd == 3) &&(record.currentStatus != 0)){
                            if((record.overallStatusCd != '') &&(record.currentStatus != 0) && (record.overallStatusCd != 3 && (record.overallStatusCd != 0))){
                        		disable = false;
                        	}
                        	
                         	let $button = $("<button>", { "class": "setting small button1", "tabindex": -1, "disabled": disable, "text" : getText("KBT002_144")  });
                            $button.attr("data-value", record["execItemCd"]);
                         	return $button[0].outerHTML;
                        	//return "<button tabindex='-1' class='setting small' id='A${execItemCd}' data-bind='click: function(data, event) { openDialogG(data, event)}, enable: {{if ((${overallStatusCd} != '') && (${overallStatusCd} == 3)) && (${currentStatus} !=0)  }}true{{else}} false {{/if}}' data-code='${execItemCd}' style='margin-left: 7px;'>" + getText("KBT002_144") + "</button>";
                    	} 
                        //template: '<button tabindex="-1" class="setting small" id="A${execItemCd}" data-bind="click: function(data, event) { openDialogG(data, event)}, enable: {{if ((${overallStatusCd} != "") && (${overallStatusCd} == 3)) && (${currentStatus} !=0)  }}true{{else}} false {{/if}}" data-code="${execItemCd}" style="margin-left: 7px;">' + getText("KBT002_144") + '</button>',
                    },
                    { headerText: getText("KBT002_131"), key: 'nextExecDateTime', width: 180, formatter: _.escape },
                    {
                        headerText: "", key: 'execItemCd', width: 85, unbound: true, dataType: "string",
                        formatter: function(nextExecDateTime, record) {
                        	let disable = true;
                        	if ((record.currentStatusCd != '') && (record.currentStatusCd != 0)){
                        		disable = false;
                        	}
                        	
                         	let $button = $("<button>", { "class": "setting small button2", "tabindex": -1, "disabled": disable, "text" : getText("KBT002_132")  });
                            $button.attr("data-value", record["execItemCd"]);
                            return $button[0].outerHTML;	                    
                        } 
                        //template: '<button tabindex="-1" class="setting small" data-bind="click: function(data, event) { execute(data, event, ${execItemCd})}, enable: {{if ((${currentStatusCd} != "") && (${currentStatusCd} != 0)) }}true{{else}} false {{/if}}" data-code="${execItemCd}" style="margin-left: 7px;">' + getText("KBT002_132") + '</button>',
                    },
                    {
                        headerText: "", key: 'execItemCd', width: 55, unbound: true, dataType: "string",
                        formatter: function(execItemCd, record) {
                        	let disable = true;
                        	if ((record.currentStatusCd != '') && (record.currentStatusCd == 0)){
                        		disable = false;
                        	}
                        	
                         	let $button = $("<button>", { "class": "setting small button3", "tabindex": -1, "disabled": disable, "text" : getText("KBT002_133") });
                            $button.attr("data-value", record["execItemCd"]);
                         	//"onclick" : function(data, event) { self.terminate(data, event)}
                            //, 'onclick': 'function() { kbt002FModel.terminate() }'
                         	return $button[0].outerHTML;
	                    } 
                        //template: '<button tabindex="-1" class="setting small" data-bind="click: function(data, event) { terminate(data, event)}, enable: {{if ((${currentStatusCd} != "") && (${currentStatusCd} == 0)) }}true{{else}} false {{/if}}" data-code="${execItemCd}" style="margin-left: 7px;">' + getText("KBT002_133") + '</button>',
                    },
                    {
                        headerText: "", key: 'execItemCd', width: 55, unbound: true, dataType: "string",
                        formatter: function(execItemCd, record) {
                        	
                         	let $button = $("<button>", { "class": "setting small button4", "tabindex": -1, "text" : getText("KBT002_147")  });
                            $button.attr("data-value", record["execItemCd"]);
                         	//"onclick" : function(data, event) { self.terminate(data, event)}
                         	return $button[0].outerHTML;
	                    } 
                        //template: '<button tabindex="-1" class="setting small" onclick="kbt002FModel.openDialogH(${execItemCd})" data-code="${execItemCd}" style="margin-left: 7px;">' + getText("KBT002_147") + '</button>',
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
            
            openDialogG(execItemCd){
                let self = this;
                block.grayout();
                var execLog = _.find(self.execLogList(), function(o) { return o.execItemCd == execItemCd; });
                setShared('inputDialogG', {execLog: execLog});
                modal("/view/kbt/002/g/index.xhtml").onClosed(function(){
                    block.clear();
                });    
            }
            openDialogH(execItemCd){
                let self = this;
                block.grayout();
//                let cd = execItemCd;
//                if(execItemCd<10)
//                    cd = "0"+cd;
                    
                setShared('inputDialogH', {execItemCd: execItemCd});
                modal("/view/kbt/002/h/index.xhtml").onClosed(function(){
                    block.clear();
                });    
            }
            
            updateInfo(){
                let self = this;
                 ko.cleanNode(igrid);
                  $.when(self.getProcExecLogList()).done(()=>{
                     ko.applyBindings(self,igrid);
                });
            }
            
             execute(idd){
                let self = this;
//                var dfd = $.Deferred();
                 let cd =""+idd;
                 if(idd<10)
                    cd = "0"+cd;
                //$("#A"+cd).prop('disabled',true);
                block.grayout();
                let command: any = self.toJsonObject();
                service.execute(command).done(function(x) {
                    self.taskId(x.id);
                    self.repeatCheckAsyncResult();
                     /* ko.cleanNode(igrid);
                        $.when(self.getProcExecLogList()).done(()=>{
                             ko.applyBindings(self,igrid);
                             block.clear();
                        }); 
                    */
                    block.clear();
                }).fail(function(res) {
                         nts.uk.ui.dialog.alertError({ messageId: res.messageId });
                        self.getProcExecLogList();
                    });
                 
                 let newExecLogList = [];
                 _.forEach(self.execLogList(),function(x) {
                       if(x.execItemCd==command.execItemCd){
                            newExecLogList.push( {execItemCd : x.execItemCd, 
                            companyId:           x.companyId,
                            execItemName:        x.execItemName,
                            currentStatusCd:     0,
                            currentStatus:       "実行中",
                            overallStatusCd:     x.overallStatusCd,
                            overallStatus:       " ",
                            overallError:        x.overallError,
                            prevExecDateTime:    x.prevExecDateTime,
                            schCreateStart:      x.schCreateStart,
                            schCreateEnd:        x.schCreateEnd,
                            dailyCreateStart:    x.dailyCreateStart,
                            dailyCreateEnd:      x.dailyCreateEnd,
                            dailyCalcStart:      x.dailyCalcStart,
                            dailyCalcEnd:        x.dailyCalcEnd,
                            execId:              x.execId,
                            prevExecDateTimeEx:  x.prevExecDateTimeEx,
                            taskLogList:         x.taskLogList,
//                            lastEndExecDateTime: x.lastEndExecDateTime,
//                            errorSystem:         x.errorSystem,
//                            errorBusiness:       x.errorBusiness
                        });
                     }else{
                        newExecLogList.push(x);     
                        }
  
                });
                 
                ko.cleanNode(igrid);
                self.execLogList(newExecLogList);
                ko.applyBindings(self,igrid);
                $("#A"+cd).prop('disabled',true);

//                return dfd.promise();
            }
            
            private repeatCheckAsyncResult(): void{
                var self = this;
                nts.uk.deferred.repeat(conf => conf
                .task(() => {
                 return nts.uk.request.asyncTask.getInfo(self.taskId()).done(info => {
                        //ExecuteProcessExecCommandHandler
                     var message101 = self.getAsyncData(info.taskDatas, "message101").valueAsString;
                    if(message101 =="Msg_1101" && self.isOnceMessage101()){
                        self.isOnceMessage101(false);
                       nts.uk.ui.dialog.alertError({ messageId: m });
                       ko.cleanNode(igrid);
                        $.when(self.getProcExecLogList()).done(()=>{
                             ko.applyBindings(self,igrid);
                        });
                    }
                    
                     //TerminateProcessExecutionCommandHandler
                     var currentStatusIsOneOrTwo = self.getAsyncData(info.taskDatas, "currentStatusIsOneOrTwo").valueAsString;
                     if(currentStatusIsOneOrTwo=="Msg_1102"&& self.isOnceCurrentStatus()){
                        self.isOnceCurrentStatus(false);
                        nts.uk.ui.dialog.alertError({ messageId: currentStatusIsOneOrTwo }); 
                        ko.cleanNode(igrid);
                        $.when(self.getProcExecLogList()).done(()=>{
                             ko.applyBindings(self,igrid);
                        });  
                     }
                     var interupt = self.getAsyncData(info.taskDatas, "interupt").valueAsString;
                     if(interupt=="true" && self.isOnceInterupt()){
                        self.isOnceInterupt(false);
                        ko.cleanNode(igrid);
                        $.when(self.getProcExecLogList()).done(()=>{
                             ko.applyBindings(self,igrid);
                        });
                     }
                     var createSchedule = self.getAsyncData(info.taskDatas, "createSchedule").valueAsString;
                     if(createSchedule=="done" && self.isCreateSchedule()){
                        self.isCreateSchedule(false);
                        ko.cleanNode(igrid);
                        $.when(self.getProcExecLogList()).done(()=>{
                             ko.applyBindings(self,igrid);
                        });
                     }
                     var task = self.getAsyncData(info.taskDatas, "taskId").valueAsString;
                     if(!nts.uk.text.isNullOrEmpty(task)){
                         self.taskTerminate(task);
                     }
                 });   
                }) .while(info => info.pending || info.running)
                .pause(1000)
            );    
            }
            
            private getAsyncData(data: Array<any>, key: string): any {
            var result = _.find(data, (item) => {
                return item.key == key;
            });
            return result || { valueAsString: "", valueAsNumber: 0, valueAsBoolean: false };
        }
            
            terminate(){
                let self = this;
                block.grayout();
//                var dfd = $.Deferred();
                let command: any = self.toJsonObject();
                service.terminate(command);
                ko.cleanNode(igrid);
                $.when(self.getProcExecLogList()).done(()=>{
                     ko.applyBindings(self,igrid);
                     block.clear();
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
                command.taskTerminate = self.taskTerminate();
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
            taskLogExecId:        string;   
            
            
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
            taskLogExecId:  string;
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
                self.taskLogExecId = param.taskLogExecId||'';
            }
        }
        
        export interface EnumConstantDto {
            value: number;
            fieldName: string;
            localizedName: string;
        }
    }
}