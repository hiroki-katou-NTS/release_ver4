module nts.uk.at.view.daily.importing.viewmodel {
    export class ScreenModel {
        dateValue: KnockoutObservable<any>;
        processId: KnockoutObservable<string>;
        canProcess: KnockoutObservable<boolean>;
        elapseTime: nts.uk.ui.sharedvm.KibanTimer;
        totalEmp: KnockoutObservable<string>;
        processEmp: KnockoutObservable<string>;
        status: KnockoutObservable<string>;
        errorNumber: KnockoutObservable<string>;
        errors: KnockoutObservableArray<any>;
        canExportError: KnockoutObservable<boolean>;
        canStop: KnockoutObservable<boolean>;

        constructor() {
            let self = this;
            self.dateValue = ko.observable({startDate: "2018/10/01", endDate: "2018/10/31"});
            self.processId = ko.observable("");
            self.canProcess = ko.observable(true);
            self.totalEmp = ko.observable("");
            self.processEmp = ko.observable("");
            self.status = ko.observable("");
            self.errorNumber = ko.observable("");
            self.errors = ko.observableArray();
            self.canExportError = ko.computed(() => self.errors().length > 0);
            self.canStop = ko.computed(() => !self.canProcess());
            self.elapseTime = new nts.uk.ui.sharedvm.KibanTimer('elapseTime');
        }

        importData() {
            let self = this;
            self.elapseTime.start();
            self.errors([]); 
            self.canProcess(false);
            service.importData({startDate: moment(self.dateValue().startDate).utc().format(), 
                                endDate: moment(self.dateValue().endDate).utc().format()}).done((taskInfo) => {
                            self.processId(taskInfo.id);

                            nts.uk.deferred.repeat(conf => conf.task(() => {
                                    return nts.uk.request.asyncTask.getInfo(self.processId()).done(function(res: any) {
    
                                        self.totalEmp(self.getAsyncData(res.taskDatas, "totalEmpCount").valueAsNumber+ "人");
                                        let processed = self.getAsyncData(res.taskDatas, "processedEmpCount").valueAsNumber;
                                        let totalProcess = self.getAsyncData(res.taskDatas, "totalEmpCount").valueAsNumber;
                                        self.processEmp(processed + "人　/　" + totalProcess + "人");
                                        self.status(self.getAsyncData(res.taskDatas, "status").valueAsString);
                                        self.errorNumber(self.getAsyncData(res.taskDatas, "errorCount").valueAsNumber);

                                        if(res.succeeded || res.failed || res.cancelled) {
                                            self.canProcess(true);
                                            self.elapseTime.end();
                                            if (res.succeeded || res.cancelled) {
                                                self.processErrors(res.taskDatas);
                                                if(res.cancelled){
                                                    self.status("中断された。");
                                                }
                                            } else if (res.failed) {
                                                self.status("エラーで失敗しました。");
                                            }
                                        }
                                    });
                                }).while(infor => {
                                    return infor.pending || infor.running;
                                }).pause(1000));
                        });
        }

        private getAsyncData(data: Array<any>, key: string): any {
            let result = _.find(data, (item) => {
                return item.key == key;
            });
            return result || { valueAsString: "", valueAsNumber: 0, valueAsBoolean: false };
        }

        private processErrors(data: Array<any>){
            let self = this;
            let errors = _.groupBy(_.filter(data, item => item.key.indexOf("Error_") >= 0), item => item.key.slice(0, item.key.lastIndexOf("_")));
            let eTemp = [];
            _.forEach(_.values(errors), (error: Array<any>) => {
                if(error.length > 1){
                    eTemp = _.concat(eTemp, JSON.parse(_.join(_.map(_.sortBy(error, e => e.key), e => e.valueAsString), '')));  
                } else {
                    eTemp = _.concat(eTemp, JSON.parse(error[0].valueAsString));    
                }
            });
            self.errors(eTemp);
        }

        stop() {
            let self = this;
            if (_.isNil(self.processId())) {
                return;
            }
            nts.uk.ui.dialog.confirm({ messageId: "Msg_911" }).ifYes(() => {
                    nts.uk.request.asyncTask.requestToCancel(self.processId());
                    self.elapseTime.end();
                    self.canProcess(true);
                 }).ifNo(() => {
                     return;
                 });
        }

        exportErrors() {
            let self = this;
            if(self.errors().length <= 0){
                nts.uk.ui.dialog.alert("エラーがないため抽出できないです。");
            }
            /** TODO: test or check cach khac */
            nts.uk.ui.block.grayout();
            let errors = _.map(self.errors(), e => {
                if(e.ymd != null){
                    e.ymd = moment(e.ymd).utc().format();    
                }
                return e;    
            });
            service.exportErrors({errors: errors}).done((taskInfo) => {
                nts.uk.deferred.repeat(conf => conf.task(() => {
                        return nts.uk.request.asyncTask.getInfo(taskInfo.taskId).done(function(res: any) {
                            if (res.succeeded) {
                                nts.uk.request.specials.donwloadFile(taskInfo.taskId);
                                nts.uk.ui.block.clear();
                            } 
                        });
                    }).while(infor => {
                        return infor.pending || infor.running;
                    }).pause(1000));
            });
        }

        closeDialog() {
            nts.uk.ui.windows.close();
        }
    }
}