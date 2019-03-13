module nts.uk.at.view.kmw006.f.viewmodel {
    import block = nts.uk.ui.block;
    import getText = nts.uk.resource.getText;
    import alertError = nts.uk.ui.dialog.alertError;
    import getShared = nts.uk.ui.windows.getShared;
    import setShared = nts.uk.ui.windows.setShared;
    import kibanTimer = nts.uk.ui.sharedvm.KibanTimer;
    
    var tmp = null;
    
    export class ScreenModel {

        items: KnockoutObservableArray<MonthlyClosureErrorInfor>;
        taskId: KnockoutObservable<string> = ko.observable("");
        startTime: KnockoutObservable<string> = ko.observable("");
        dispStartTime: KnockoutObservable<string>;
        endTime: KnockoutObservable<string> = ko.observable("");
        dispEndTime: KnockoutObservable<string>;
        elapseTime: kibanTimer = new kibanTimer('elapseTime');
        isComplete: KnockoutObservable<boolean> = ko.observable(false);
        totalCount: KnockoutObservable<number> = ko.observable(0);
        processedCount: KnockoutObservable<number> = ko.observable(0);
        dispProcessCount: KnockoutObservable<string>;
        completeStatus: KnockoutObservable<number> = ko.observable(COMPLETE_STATUS.INCOMPLETE);
        params: any;
        duration: KnockoutObservable<number> = ko.observable(0);

        constructor() {
            var self = this;
            self.params = getShared("kmw006fParams");

            if (self.params.check != 2 || !self.params.check) {
                if (self.params)
                    self.totalCount(self.params.listEmployeeId.length);
                else
                    self.totalCount(localStorage.getItem("MonthlyClosureListEmpId") ? localStorage.getItem("MonthlyClosureListEmpId").split(',').length : 0);
            }
            
            
            self.items = ko.observableArray([]);
            self.initIGrid();
            self.dispProcessCount = ko.computed(() => {
                return getText("KMW006_62", [self.processedCount(), self.totalCount()]);
            });
            self.dispStartTime = ko.computed(() => {
                if (nts.uk.text.isNullOrEmpty(self.startTime()))
                    return "";
                else
                    return getText("KMW006_49", [moment.utc(self.startTime()).format("YYYY/MM/DD HH:mm:ss")]);
            });
            self.dispEndTime = ko.computed(() => {
                if (nts.uk.text.isNullOrEmpty(self.endTime()))
                    return "";
                else
                    return getText("KMW006_49", [moment.utc(self.endTime()).format("YYYY/MM/DD HH:mm:ss")]);
            });
            $("#fixed-table").ntsFixedTable({ height: 150 });
        }

        startPage(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred();
            block.invisible();
            service.getMonthlyClosureLog(self.params ? self.params.monthlyClosureUpdateLogId : localStorage.getItem("MonthlyClosureUpdateLogId")).done((result) => {
                self.startTime(result.executionDateTime);
                self.completeStatus(result.completeStatus);
                self.elapseTime.start();
                if (self.params.check != 2 || !self.params.check)
                    self.processMonthlyUpdate();
                else {
                    self.taskId(localStorage.getItem("MonthlyClosureTaskId"));
                    self.checkAsyncProcess();
                }
                dfd.resolve();
            }).fail((error) => {
                dfd.reject();
                alertError(error);
            }).always(() => {
                block.clear();
            });
            return dfd.promise();
        }

        private processMonthlyUpdate(): void {
            var self = this;
            var command = self.params;
            service.executeMonthlyClosure(command).done(res => {
                self.taskId(res.id);
                localStorage.setItem("MonthlyClosureTaskId", res.id);
                self.checkAsyncProcess();
            });
        }

        private checkAsyncProcess(): void {
            let self = this;
            service.getDurationTime(self.params.monthlyClosureUpdateLogId).done(data => {
                self.duration(data);
            });
            if (self.taskId() == null) {
                // get total from screen A
                self.totalCount(self.params.totalCount);
                tmp = setTimeout(self.getPersonCompleteNo(), 1000);
            } else {
                nts.uk.deferred.repeat(conf => conf
                    .task(() => {
                        return nts.uk.request.asyncTask.getInfo(self.taskId()).done(info => {
                            self.processedCount(self.getAsyncData(info.taskDatas, "processed").valueAsNumber);
                            if (!info.pending && !info.running) {
                                if (info.status == "COMPLETED" || localStorage.getItem("MCUdpStatus") == "COMPLETED") {
                                    localStorage.setItem("MCUdpStatus", info.status);
                                    self.processedCount(self.totalCount());
                                }
                                self.checkResult(info);
                                if (info.error) {
                                    alertError(info.error);
                                    self.completeStatus(COMPLETE_STATUS.COMPLETE_WITH_ERROR);
                                }
                        }});
                    })
                    .while(info => info.pending || info.running)
                    .pause(1000)
                );
            }
        }

        private getAsyncData(data: Array<any>, key: string): any {
            let result = _.find(data, (item) => {
                return item.key == key;
            });
            return result || { valueAsString: "", valueAsNumber: 0, valueAsBoolean: false };
        }

        private checkResult(taskInfor) {
            let self = this;
            block.invisible();
            service.getResults(self.params ? self.params.monthlyClosureUpdateLogId : localStorage.getItem("MonthlyClosureUpdateLogId")).done((result) => {
                let listErr: Array<MonthlyClosureErrorInfor> = [];
                for (let i = 0; i < result.listErrorInfor.length; i++) {
                    let item = result.listErrorInfor[i];
                    listErr.push(new MonthlyClosureErrorInfor(i + 1, item.employeeCode, item.employeeName, item.errorMessage, item.atr));
                }
                self.items(listErr);
                $("#single-list").igGrid("option", "dataSource", self.items());
                self.isComplete(true);
                $("#F3_2").focus();
                self.elapseTime.end();
                let time = self.duration() + self.elapseTime.elapsedSeconds;
                self.endTime(moment.utc(self.startTime()).add(time, 'second').format());
                localStorage.setItem("MonthlyClosureExecutionEndDate", self.endTime());
                $("#elapseTime").text(self.formatTime(time));
                self.completeStatus(result.updateLog.completeStatus);
            }).fail((error) => {
                alertError(error);
            }).always(() => {
                block.clear();
            });
        }

        private confirmComplete() {
            let self = this;
            block.invisible();
            nts.uk.at.view.kmw006.f.service.completeConfirm(self.params ? self.params.monthlyClosureUpdateLogId : localStorage.getItem("MonthlyClosureUpdateLogId")).done(() => {
                localStorage.removeItem("MonthlyClosureUpdateLogId");
                localStorage.removeItem("MonthlyClosureListEmpId");
                localStorage.removeItem("MonthlyClosureId");
                localStorage.removeItem("MonthlyClosureExecutionDateTime");
                localStorage.removeItem("MonthlyClosureTaskId");
                setShared("kmw006fConfirm", {check: true});
                nts.uk.ui.windows.close();
            }).fail((error) => {
                alertError(error);
            }).always(() => {
                block.clear();
            });
        }

        private initIGrid() {
            let self = this;
            $("#single-list").ntsGrid({
                height: '429px',
                dataSource: self.items(),
                primaryKey: 'employeeCode',
                columns: [
                    { headerText: getText('KMW006_13'), key: 'no', dataType: 'number', width: '40px' },
                    { headerText: getText('KMW006_16'), key: 'employeeCode', dataType: 'string', width: '160px' },
                    { headerText: getText('KMW006_17'), key: 'employeeName', dataType: 'string', width: '160px' },
                    { headerText: getText('KMW006_39'), key: 'atr', dataType: 'string', width: '120px' },
                    { headerText: getText('KMW006_40'), key: 'errorMessage', dataType: 'string', width: '400px' }
                ],
                features: [
                    {
                        name: 'Paging',
                        pageSize: 15,
                        currentPageIndex: 0,
                        showPageSizeDropDown: false,
                        pageCountLimit: 20
                    }
                ]
            });
        }
        
        formatTime(second: number) {
            let d = (s) => { f = Math.floor; g = (n) => ('00' + n).slice(-2); return f(s / 3600) + ':' + g(f(s / 60) % 60) + ':' + g(s % 60) };
            return d(second);
        }
        
        getPersonCompleteNo() {
            let self = this;
            let id = self.params.monthlyClosureUpdateLogId;
            service.getPersonCompleteNo(id).done(no => {
                self.processedCount(no);
                if (self.processedCount() == self.totalCount()) {
                    service.getListError(id).done(data => {
                        if (data.length == 0) {
                            self.completeStatus(COMPLETE_STATUS.COMPLETE);
                        } else {
                            // 
                            let listErr: Array<MonthlyClosureErrorInfor> = [];
                            for (let i = 0; i < data.length; i++) {
                                let item = data[i];
                                listErr.push(new MonthlyClosureErrorInfor(i + 1, item.employeeCode, item.employeeName, item.errorMessage, item.atr));
                            }
                            self.items(listErr);
                            $("#single-list").igGrid("option", "dataSource", self.items());
                            $("#F3_2").focus();
                            //
                            self.completeStatus(COMPLETE_STATUS.COMPLETE_WITH_ERROR);
                        }

                        self.elapseTime.end();
                        self.isComplete(true);
                        let time = self.duration() + self.elapseTime.elapsedSeconds;
                        self.endTime(moment.utc(self.startTime()).add(time, 'second').format());
                        localStorage.setItem("MonthlyClosureExecutionEndDate", self.endTime());
                        $("#elapseTime").text(self.formatTime(time));
                        
                        clearTimeout(tmp);
                    });
                } else {
                    tmp = setTimeout(self.getPersonCompleteNo(), 1000); // repeat myself
                }
            }).fail((error) => {
                clearTimeout(tmp);
                alertError(error);
            });
        }
    
    }

    class MonthlyClosureErrorInfor {
        no: number;
        employeeCode: string;
        employeeName: string;
        errorMessage: string;
        atr: string;

        constructor(no: number, employeeCode: string, employeeName: string, errorMessage: string, atr: number) {
            this.no = no;
            this.employeeCode = employeeCode;
            this.employeeName = employeeName;
            this.errorMessage = errorMessage;
            this.atr = atr == ERROR_ALARM_ATR.ALARM ? getText("Enum_MonthlyClosureUpdate_Alarm") : getText("Enum_MonthlyClosureUpdate_Error");
        }
    }

    export enum COMPLETE_STATUS {
        INCOMPLETE = 0,
        COMPLETE = 1,
        COMPLETE_WITH_ERROR = 2,
        COMPLETE_WITH_ALARM = 3
    }

    enum ERROR_ALARM_ATR {
        ALARM = 0,
        ERROR = 1
    }

}