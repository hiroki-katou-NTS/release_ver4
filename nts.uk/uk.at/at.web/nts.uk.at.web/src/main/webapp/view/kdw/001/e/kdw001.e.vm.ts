module nts.uk.at.view.kdw001.e.viewmodel {
    import getText = nts.uk.resource.getText;
    import shareModel = nts.uk.at.view.kdw001.share.model;
    import kibanTimer = nts.uk.ui.sharedvm.KibanTimer;

    export class ScreenModel {

        numberEmployee: KnockoutObservable<number> = ko.observable(0);

        // Time data
        isComplete: KnockoutObservable<boolean> = ko.observable(false);
        taskId: KnockoutObservable<string> = ko.observable("");
        startTime: KnockoutObservable<string> = ko.observable("");
        endTime: KnockoutObservable<string> = ko.observable("");
        endTimeTemp : KnockoutObservable<string> = ko.observable("");
        elapseTime: kibanTimer = new kibanTimer('elapseTime', 100);
        empCalAndSumExecLogID: KnockoutObservable<string> = ko.observable("");

        // dailyCreate data
        dailyCreateCount: KnockoutObservable<number> = ko.observable(0);
        dailyCreateTotal: KnockoutObservable<number> = ko.observable(0);
        dailyCreateStatus: KnockoutObservable<string> = ko.observable("");
        dailyCreateHasError: KnockoutObservable<string> = ko.observable("");

        // daily calculation
        dailyCalculateCount: KnockoutObservable<number> = ko.observable(0);
        dailyCalculateStatus: KnockoutObservable<string> = ko.observable("");
        dailyCalculateHasError: KnockoutObservable<string> = ko.observable("");

        // monthly aggregation data
        monthlyAggregateCount: KnockoutObservable<number> = ko.observable(0);
        monthlyAggregateStatus: KnockoutObservable<string> = ko.observable("");
        monthlyAggregateHasError: KnockoutObservable<string> = ko.observable("");

        //承認反映
        reflectApprovalCount: KnockoutObservable<number> = ko.observable(0);
        reflectApprovalStatus: KnockoutObservable<string> = ko.observable("");
        reflectApprovalHasError: KnockoutObservable<string> = ko.observable("");

        // Period Date
        startPeriod: KnockoutObservable<string> = ko.observable("");
        endPeriod: KnockoutObservable<string> = ko.observable("");

        // Combo box 
        executionContents: KnockoutObservableArray<any> = ko.observableArray([]);
        selectedExeContent: KnockoutObservable<any> = ko.observable(null);
        contents: any;

        // GridList
        errorMessageInfo: KnockoutObservableArray<any> = ko.observableArray([]);
        dataExport: KnockoutObservableArray<shareModel.PersonInfoErrMessageLog> = ko.observableArray([]); 
        columns: KnockoutObservableArray<any>;
        currentCode: KnockoutObservable<any> = ko.observable();

        //enable enableCancelTask
        enableCancelTask: KnockoutObservable<boolean> = ko.observable(true);

        //disappear
        visibleDailiCreate: KnockoutObservable<boolean> = ko.observable(false);
        visibleDailiCalculation: KnockoutObservable<boolean> = ko.observable(false);
        visibleApproval: KnockoutObservable<boolean> = ko.observable(false);
        visibleMonthly: KnockoutObservable<boolean> = ko.observable(false);

        closureId: KnockoutObservable<number> = ko.observable(1);
        
        // endTime
        // dailyCreate
        dailyCreateStartTime : KnockoutObservable<string> = ko.observable("");
        dailyCreateEndTime : KnockoutObservable<string> = ko.observable("");
        
        // dailyCalculate
        dailyCalculateStartTime : KnockoutObservable<string> = ko.observable("");
        dailyCalculateEndTime : KnockoutObservable<string> = ko.observable("");
        
        // approval
        reflectApprovalStartTime : KnockoutObservable<string> = ko.observable("");
        reflectApprovalEndTime : KnockoutObservable<string> = ko.observable("");
        
        // monthly
        monthlyAggregateStartTime : KnockoutObservable<string> = ko.observable("");
        monthlyAggregateEndTime : KnockoutObservable<string> = ko.observable("");
        employeeIDS : KnockoutObservableArray<any> = ko.observableArray([]);

        textHearderDisposalDay : KnockoutObservable<string> = ko.observable("");
        //
        startMonthResult : KnockoutObservable<string> = ko.observable("");
        endMonthResult :  KnockoutObservable<string> = ko.observable("");
        disableMonthResult :KnockoutObservable<boolean> = ko.observable(false);
        disableDayResult :KnockoutObservable<boolean> = ko.observable(false);
        constructor() {
            var self = this;
            self.elapseTime.start();

            self.numberEmployee(0);

            self.closureId(1);
            
            
            self.columns = ko.observableArray([
                { headerText: getText('KDW001_33'), key: 'personCode', width: 110 },
                { headerText: getText('KDW001_35'), key: 'personName', width: 150 },
                { headerText: self.textHearderDisposalDay(), key: 'disposalDay', width: 150 },
                { headerText: getText('KDW001_37'), key: 'messageError', class: "limited-label", width: 290 },
                { headerText: '', key: 'id', width: 1, hidden: true }, 
            ]);

            self.selectedExeContent.subscribe((value) => {
                if(value != undefined && value.length == undefined) {
                    if(self.selectedExeContent() == 3){
                       self.textHearderDisposalDay(getText('KDW001_116'));
                    }else{
                        self.textHearderDisposalDay(getText('KDW001_36'));
                    }
                    $('#single-list_disposalDay .ui-iggrid-headertext').text(self.textHearderDisposalDay());
                    self.getLogData();    
                }
                
            });
            
            self.employeeIDS.subscribe(value => {
                if(value != null && value.length > 0) {
                    self.getLogDataAgain(value).done(employeeIDS => {
                        if(employeeIDS.length > 0) {
                             self.employeeIDS(employeeIDS);
                            } 
                        });
                    }
                });
        }

        startPage(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            var params: shareModel.executionProcessingCommand = nts.uk.ui.windows.getShared("KDWL001E");
            self.startMonthResult(params.startMonthResult);
            self.endMonthResult(params.endMonthResult);
            self.startPeriod(params.periodStartDate);
            self.endPeriod(params.periodEndDate);
            self.numberEmployee(params.lstEmployeeID.length);
            self.closureId(params.closureID);
            

            $('#closeDialogButton').focus();
            service.insertData(params).done((res: shareModel.AddEmpCalSumAndTargetCommandResult) => {
                self.empCalAndSumExecLogID(res.empCalAndSumExecLogID);
                if (params.dailyCreation == false) {
                    _.remove(res.enumComboBox, function(n) {
                        return n.value == 0;
                    });
                    self.visibleDailiCreate(true);
                }
                if (params.dailyCalClass == false) {
                    _.remove(res.enumComboBox, function(n) {
                        return n.value == 1;
                    });
                    self.visibleDailiCalculation(true);
                }
                if (params.refApprovalresult == false) {
                    _.remove(res.enumComboBox, function(n) {
                        return n.value == 2;
                    });
                    self.visibleApproval(true);
                }
                if (params.monthlyAggregation == false) {
                    _.remove(res.enumComboBox, function(n) {
                        return n.value == 3;
                    });
                    self.visibleMonthly(true);
                }
                
                self.contents = res.enumComboBox;
                self.disableDayResult(false);
                self.disableMonthResult(false);
                for(let i =0;i<self.contents.length;i++){
                   if(self.contents[i].value ==0 || self.contents[i].value ==1 ||self.contents[i].value ==2){
                       self.disableDayResult(true);
                   }else if(self.contents[i].value ==3){
                       self.disableMonthResult(true);
                   } 
                }
                if(self.contents[0].value == 3){
                   self.textHearderDisposalDay(getText('KDW001_116')); 
                }else{
                    self.textHearderDisposalDay(getText('KDW001_36')); 
                }
                self.columns([
                    { headerText: getText('KDW001_33'), key: 'personCode', width: 110 },
                    { headerText: getText('KDW001_35'), key: 'personName', width: 150 },
                    { headerText: self.textHearderDisposalDay(), key: 'disposalDay', width: 150 },
                    { headerText: getText('KDW001_37'), key: 'messageError', class: "limited-label", width: 290 },
                    { headerText: '', key: 'id', width: 1, hidden: true }, 
                ]);
                $('#single-list_disposalDay .ui-iggrid-headertext').text(self.textHearderDisposalDay());
                //self.executionContents(res.enumComboBox);
                self.startTime(moment.utc(res.startTime).format("YYYY/MM/DD HH:mm:ss"));
                self.startAsyncTask(res.empCalAndSumExecLogID);

                dfd.resolve();
            });

            return dfd.promise();
        }

        exportLog(): void {
            var self = this;
            if (self.dataExport().length > 0)
                service.saveAsCsv(self.dataExport());
        }

        cancelTask(): void {
            var self = this;
            nts.uk.request.asyncTask.requestToCancel(self.taskId());
            self.enableCancelTask(false);
            service.updateLogState(self.empCalAndSumExecLogID()).done(result =>{
               self.endTimeTemp(result); 
               self.elapseTime.end(); 
            });
        }

        closeDialog(): void {
            nts.uk.ui.windows.close();
        }

        private startAsyncTask(empCalAndSumExecLogID): void {
            var self = this;
            var data: shareModel.CheckProcessCommand = {
                empCalAndSumExecLogID: self.empCalAndSumExecLogID(),
                periodStartDate: self.startPeriod(),
                periodEndDate: self.endPeriod()
            };
            service.checkTask(data).done(res => {
                self.taskId(res.id);
                self.repeatCheckAsyncResult(empCalAndSumExecLogID);
            });
        }

        private repeatCheckAsyncResult(empCalAndSumExecLogID): void {
            var self = this;

            self.enableCancelTask(true);
            nts.uk.deferred.repeat(conf => conf
                .task(() => {
                    return nts.uk.request.asyncTask.getInfo(self.taskId()).done(info => {
                        self.processData(self, info, empCalAndSumExecLogID, false);
                    });
                })
                .while(info => info.pending || info.running)
                .pause(1000)
            );

            //                var paramsUpdate = {
            //                    empCalAndSumExecLogID : empCalAndSumExecLogID,
            //                    executionStartDate: moment.utc(self.startTime()).toISOString(),
            //                    executionEndDate: self.endTime()
            //                };
            //                service.updateExcutionTime(paramsUpdate);
        }
        
        private processData(self: any, info: any, empCalAndSumExecLogID: string, flag: boolean): void {
            // DailyCreate
            self.dailyCreateCount(self.getAsyncData(info.taskDatas, "dailyCreateCount").valueAsNumber);
            self.dailyCreateTotal(self.getAsyncData(info.taskDatas, "dailyCreateTotal").valueAsNumber);

            // daily calculation
            self.dailyCalculateCount(self.getAsyncData(info.taskDatas, "dailyCalculateCount").valueAsNumber);

            // monthly aggregation 
            self.monthlyAggregateCount(self.getAsyncData(info.taskDatas, "monthlyAggregateCount").valueAsNumber);

            self.dailyCreateStatus(self.getAsyncData(info.taskDatas, "dailyCreateStatus").valueAsString);
            self.dailyCalculateStatus(self.getAsyncData(info.taskDatas, "dailyCalculateStatus").valueAsString);
            self.monthlyAggregateStatus(self.getAsyncData(info.taskDatas, "monthlyAggregateStatus").valueAsString);

            //承認反映
            self.reflectApprovalCount(self.getAsyncData(info.taskDatas, "reflectApprovalCount").valueAsNumber);
            self.reflectApprovalStatus(self.getAsyncData(info.taskDatas, "reflectApprovalStatus").valueAsString);


            if (!info.pending && !info.running) {
                if(!flag){
                    nts.uk.request.asyncTask.getInfo(self.taskId()).done(info => {
                        if(!nts.uk.util.isNullOrEmpty(info.taskDatas)){
                            self.processData(self, info, empCalAndSumExecLogID, true);    
                        }
                        
                    });
                }
                
                let stopped = 0;
                    if(info.status === "CANCELLED"){
                        self.endTime(self.endTimeTemp());
                        stopped = 1;
                    } else {
                       // Get EndTime from server, fallback to client
                        self.endTime(self.getAsyncData(info.taskDatas, "endTime").valueAsString); 
                    }
                
                self.isComplete(true);
                self.executionContents(self.contents);
                self.selectedExeContent(self.executionContents().length > 0 ? self.executionContents()[0].value : null);

                // End count time
                self.elapseTime.end();
                
                //                            if (nts.uk.text.isNullOrEmpty(endTime))
                //                                endTime = moment.utc().add(9,"h").format("YYYY/MM/DD HH:mm:ss")
                //                            self.endTime(endTime);
                //9: {key: "monthlyAggregateStatus", valueAsString: "処理中
                // DailyCreate
                if (info.status === "CANCELLED" && self.getAsyncData(info.taskDatas, "dailyCreateStatus").valueAsString === "処理中") {
                    self.dailyCreateStatus("実行中止");
                } else {
                    self.dailyCreateStatus(self.getAsyncData(info.taskDatas, "dailyCreateStatus").valueAsString);
                }
                self.dailyCreateHasError(self.getAsyncData(info.taskDatas, "dailyCreateHasError").valueAsString);

                // daily calculation
                if (info.status === "CANCELLED" && self.getAsyncData(info.taskDatas, "dailyCalculateStatus").valueAsString === "処理中") {
                    self.dailyCalculateStatus("実行中止");
                } else {
                    self.dailyCalculateStatus(self.getAsyncData(info.taskDatas, "dailyCalculateStatus").valueAsString);
                }
                self.dailyCalculateHasError(self.getAsyncData(info.taskDatas, "dailyCalculateHasError").valueAsString);

                // monthly aggregation
                if (info.status === "CANCELLED" && self.getAsyncData(info.taskDatas, "monthlyAggregateStatus").valueAsString === "処理中") {
                    self.monthlyAggregateStatus("実行中止");
                } else {
                    self.monthlyAggregateStatus(self.getAsyncData(info.taskDatas, "monthlyAggregateStatus").valueAsString);
                }
                self.monthlyAggregateHasError(self.getAsyncData(info.taskDatas, "monthlyAggregateHasError").valueAsString);

                //承認反映
                if (info.status === "CANCELLED" && self.getAsyncData(info.taskDatas, "reflectApprovalStatus").valueAsString === "処理中") {
                    self.reflectApprovalStatus("実行中止");
                } else {
                    self.reflectApprovalStatus(self.getAsyncData(info.taskDatas, "reflectApprovalStatus").valueAsString);
                }
                self.reflectApprovalHasError(self.getAsyncData(info.taskDatas, "reflectApprovalHasError").valueAsString);

                //daily create
                self.dailyCreateStartTime(self.getAsyncData(info.taskDatas, "dailyCreateStartTime").valueAsString);                            
                self.dailyCreateEndTime(self.getAsyncData(info.taskDatas, "dailyCreateEndTime").valueAsString);
                
                // daily calculate
                self.dailyCalculateStartTime(self.getAsyncData(info.taskDatas, "dailyCalculateStartTime").valueAsString);
                self.dailyCalculateEndTime(self.getAsyncData(info.taskDatas, "dailyCalculateEndTime").valueAsString);
                
                // approval
                self.reflectApprovalStartTime(self.getAsyncData(info.taskDatas, "reflectApprovalStartTime").valueAsString);
                self.reflectApprovalEndTime(self.getAsyncData(info.taskDatas, "reflectApprovalEndTime").valueAsString);
                
                // monthly
                self.monthlyAggregateStartTime(self.getAsyncData(info.taskDatas, "monthlyAggregateStartTime").valueAsString);
                self.monthlyAggregateEndTime(self.getAsyncData(info.taskDatas, "monthlyAggregateEndTime").valueAsString);
                
                // Get Log data
                //self.getLogData();
                self.enableCancelTask(false);
                
                if (self.endTime() != null && self.endTime() != undefined && self.endTime() != "") {
                    
                    var paramsUpdate = {
                        empCalAndSumExecLogID: empCalAndSumExecLogID,
                        executionStartDate: self.startTime(),
                        executionEndDate: self.endTime(),
                        dailyCreateStartTime : self.dailyCreateStartTime(),
                        dailyCreateEndTime : self.dailyCreateEndTime(),
                        dailyCalculateStartTime : self.dailyCalculateStartTime(),
                        dailyCalculateEndTime : self.dailyCalculateEndTime(),
                        reflectApprovalStartTime : self.reflectApprovalStartTime(),
                        reflectApprovalEndTime : self.reflectApprovalEndTime(),
                        monthlyAggregateStartTime : self.monthlyAggregateStartTime(),
                        monthlyAggregateEndTime : self.monthlyAggregateEndTime(),
                        stopped : stopped
                    };
                    service.updateExcutionTime(paramsUpdate);
                }
            }   
        }

        private getAsyncData(data: Array<any>, key: string): any {
            var result = _.find(data, (item) => {
                return item.key == key;
            });
            return result || { valueAsString: "", valueAsNumber: 0, valueAsBoolean: false };
        }

        private getLogData(): void {
            var self = this;
            var params = {
                empCalAndSumExecLogID: self.empCalAndSumExecLogID(),
                executionContent: self.selectedExeContent()
            };
            service.getErrorMessageInfo(params).done((res) => {
                let i = 0;
                let data = _.map(res.listResult,function(item:any){
                   return {id: i++, disposalDay:item.disposalDay,messageError:item.messageError,personCode:item.personCode,personName:item.personName}; 
                });
                data = _.orderBy(data, ["personCode", "disposalDay"], ["asc", "asc"]);
                self.errorMessageInfo(data);
                self.errorMessageInfo.valueHasMutated();
                self.dataExport(data);
                self.dataExport.valueHasMutated();
                if(res.listEmployee.length > 0) {
                   self.employeeIDS(res.listEmployee);
                   self.errorMessageInfo.valueHasMutated();
//                   LabelGetLogDataAgain : getLogDataAgain(res.listEmployee).done((EmpIDS) => {
//                       if(EmpIDS.lenght > 0){
//                           self.employeeIDS(EmpIDS);
//                           continue LabelGetLogDataAgain;
//                       } 
//                    });
                }
            });
        }

        getLogDataAgain(employeeIDS : any) : JQueryPromise<any> {
            var self = this;
            let dfd = $.Deferred<any>();
            var params = {
                empCalAndSumExecLogID: self.empCalAndSumExecLogID(),
                executionContent: self.selectedExeContent(),
                employeeID : self.employeeIDS()
            };
            service.getErrorMessageInfo(params).done((res) => {
                let i = self.errorMessageInfo().length;
                let data = _.map(res.listResult,function(item:any){
                   return {id: i++, disposalDay:item.disposalDay,messageError:item.messageError,personCode:item.personCode,personName:item.personName}; 
                });
                data = _.orderBy(data, ["personCode", "disposalDay"], ["asc", "asc"]);
                ko.utils.arrayPushAll(self.errorMessageInfo, data);
                self.errorMessageInfo.valueHasMutated();
                self.dataExport.push(data);
                self.dataExport.valueHasMutated();
                dfd.resolve(res.listEmployee);
            });
            return dfd.promise();
        }

    }

    class Gridlist {
        empCD: string;
        code: string;
        disposalDay: string;
        errContents: string;
        constructor(empCD: string, code: string, disposalDay: string, errContents: string) {
            this.empCD = empCD;
            this.code = code;
            this.disposalDay = disposalDay;
            this.errContents = errContents;
        }
    }

}