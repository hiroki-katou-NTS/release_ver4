module nts.uk.at.view.ksc001.f {

import ScheduleExecutionLogSaveRespone = nts.uk.at.view.ksc001.b.service.model.ScheduleExecutionLogSaveRespone;
import ScheduleExecutionLogDto = service.model.ScheduleExecutionLogDto;
import ScheduleErrorLogDto = service.model.ScheduleErrorLogDto;
import kibanTimer = nts.uk.ui.sharedvm.KibanTimer;
    export module viewmodel {

        export class ScreenModel {
            errorLogs: KnockoutObservableArray<any>;
            columns: KnockoutObservableArray<any>;
            currentCode: KnockoutObservable<any>;
            currentCodeList: KnockoutObservableArray<any>;
            count: number = 100;
            scheduleExecutionLogModel: ScheduleExecutionLogModel;
            executionStartDate: string;
            executionTotal: KnockoutObservable<string>;
            executionError: KnockoutObservable<string>;
            periodInfo: string;
            taskId: KnockoutObservable<string>;
            totalRecord: KnockoutObservable<number>;
            numberSuccess: KnockoutObservable<number>;
            numberFail: KnockoutObservable<number>;
            isError: KnockoutObservable<boolean>;
            isFinish: KnockoutObservable<boolean>;
            inputData: ScheduleExecutionLogSaveRespone;
			elapseTime: kibanTimer = new kibanTimer('elapseTime', 100);
            
            constructor() {
                var self = this;
                self.errorLogs = ko.observableArray([]);

                self.columns = ko.observableArray([
                    { headerText: '', key: 'noID', hidden: true },
                    { headerText: nts.uk.resource.getText("KSC001_56"), key: 'employeeCode', width: 150 },
                    { headerText: nts.uk.resource.getText("KSC001_57"), key: 'employeeName', width: 150 },
                    { headerText: nts.uk.resource.getText("KSC001_58"), key: 'date', width: 120 },
                    { headerText: nts.uk.resource.getText("KSC001_59"), key: 'errorContent', width: 250 },
                ]);

                self.currentCode = ko.observable();
                self.currentCodeList = ko.observableArray([]);
                self.taskId = ko.observable('');
                self.totalRecord = ko.observable(0);
                self.numberSuccess = ko.observable(0);
                self.numberFail = ko.observable(0);
                self.isError = ko.observable(false);
                self.isFinish = ko.observable(false);
                self.scheduleExecutionLogModel = new ScheduleExecutionLogModel();
            }

            /**
            * start page data 
            */
            public startPage(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred();
                var self = this;
                var inputData: ScheduleExecutionLogSaveRespone = nts.uk.ui.windows.getShared('inputData');
                if (inputData) {
                    service.findScheduleExecutionLogById(inputData.executionId).done(function(data) {
                        self.scheduleExecutionLogModel.updateStatus(data.completionStatus);
                        self.executionTotal = ko.observable('0');
                        self.executionError = ko.observable('0');
                        self.executionStartDate = moment.utc(data.executionDateTime.executionStartDate)
                            .format("YYYY/MM/DD HH:mm:ss");
                        self.periodInfo = nts.uk.resource.getText("KSC001_46",
                            [data.period.startDate,
                            data.period.endDate])
                        self.inputData = inputData;
                        dfd.resolve();
                    });
                }
                return dfd.promise();
            }
            /**
             * execution
             */
            public execution(): void {
                var self = this;
                // focus stop button
                $("#btn-f-stop").focus();
                // find task id
                service.executionScheduleExecutionLog(self.inputData).done(function(res: any) {
                    self.taskId(res.taskInfor.id);
                    // updateState
                    self.updateState();
                }).fail(function(res: any) {
                    console.log(res);
                });
            }
            
            /**
             * function on click save CommonGuidelineSetting
             */
            public saveCommonGuidelineSetting(): void {
                var self = this;
                nts.uk.ui.windows.close();
            }

            /**
             * Event on click cancel button.
             */
            public cancelSaveCommonGuidelineSetting(): void {
                nts.uk.ui.windows.close();
            }
            
            
            /**
             * reload page by action stop execution
             */
            private reloadPage(): void {
                let self = this;
                nts.uk.ui.block.invisible();
                service.findScheduleExecutionLogById(self.inputData.executionId).done(function(data) {
                    service.findAllScheduleErrorLog(self.inputData.executionId).done(function(errorLogs){
                        nts.uk.ui.block.clear();
                        self.scheduleExecutionLogModel.updateStatus(data.completionStatus);
                        // check error log
                        if (errorLogs && errorLogs.length > 0) {
                            self.isError(true);
                            
                            // resize windows
                            let windowSize = nts.uk.ui.windows.getSelf();
                            windowSize.$dialog.dialog('option', {
                                width: 750,
                                height: 640
                            });
                            $("#exportButton").focus();
//                            windowSize.$dialog.resize();
                            // update error to view
                            let order = _.orderBy(errorLogs,[self.compareCode,self.compareDate],['asc','asc']);
                            let array = _.forEach(order,(object,index)=>{ object.noID = index+1;});
                            self.errorLogs(array); 
                        } else {
                            // focus on close button if has no errors
                            $('#btn-f-close').focus();
                        }
                    });
                });
            }
            /**
             * define function to sort
             */
            private compareCode(a: any) {
                return a.employeeCode.toUpperCase();
               
            }
            /**
             * define function to sort
             */
            private compareDate(a: any) {
                return new Date(a.date);
              
            }
            
            /**
             * updateState
             */
            private updateState() {
                let self = this;
                // start count time
                self.elapseTime.start(); 
                
                nts.uk.deferred.repeat(conf => conf
                .task(() => {
                    return nts.uk.request.asyncTask.getInfo(self.taskId()).done(function(res: any) {
                        // update state on screen
                        if (res.running || res.succeeded || res.cancelled) {
                            self.updateInfoStatus();
                        }
                        // finish task
                        if (res.succeeded || res.failed || res.cancelled) {
                            self.elapseTime.end(); 
                            self.updateInfoStatus();
                            self.isFinish(true);
                            self.reloadPage();
                        }
                    });
                }).while(infor => {
                    return infor.pending || infor.running;
                }).pause(1000));
            }
            
            /**
             * update info status execution
             */
            private updateInfoStatus(): void {
                var self = this;
                service.findScheduleExecutionLogInfoById(self.inputData.executionId).done(function(data) {
                    self.totalRecord(data.totalNumber);
                    self.numberSuccess(data.totalNumberCreated);
                    self.numberFail(data.totalNumberError);
                    self.executionTotal(nts.uk.resource.getText("KSC001_84", [self.numberSuccess(), self.totalRecord()]));
                    self.executionError(nts.uk.resource.getText("KSC001_85", [self.numberFail()]));
                });
            }
            
            /**
             * function cancel execution
             */
            private stopExecution(): void {
                let self = this;
                
                if (nts.uk.text.isNullOrEmpty(self.taskId())) {
                    return;
                }
                // interrupt process import then close dialog
                nts.uk.request.asyncTask.requestToCancel(self.taskId()).done(function() {
                    self.elapseTime.end(); 
                    self.updateInfoStatus();
                    self.isFinish(true);
                    self.reloadPage();
                });
            }

            /**
             * function export file error by client click
             */
            private exportFileError(): void{
                var self = this;
                service.exportScheduleErrorLog(self.inputData.executionId);    
            }

        }     
        
        
        export class ScheduleExecutionLogModel{
            completionStatus: KnockoutObservable<string>;
            
            constructor(){
                this.completionStatus = ko.observable('');    
            }
            updateStatus(completionStatus: string) {
                this.completionStatus(completionStatus);
            }
        }
    }
}