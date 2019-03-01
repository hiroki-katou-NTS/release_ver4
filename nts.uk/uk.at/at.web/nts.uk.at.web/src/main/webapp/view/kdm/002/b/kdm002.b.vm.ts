module nts.uk.at.view.kdm002.b {
    import getText = nts.uk.resource.getText;
    import EmployeeSearchDto = nts.uk.com.view.ccg.share.ccg.service.model.EmployeeSearchDto;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import alertError = nts.uk.ui.dialog.alertError;

    export module viewmodel {

        export class ScreenModel {
            // param from resources
            CODE_EMP: string = getText("KDM002_25");
            NAME_EMP: string = getText("KDM002_26");
            ERROR_MESS: string = getText("KDM002_27")

            // params
            pempployeeList: KnockoutObservableArray<EmployeeSearchDto> = ko.observableArray([]);
            pstartDate: KnockoutObservable<string> = ko.observable('');
            pendDate: KnockoutObservable<string> = ko.observable('');
            pdate: KnockoutObservable<string> = ko.observable('');
            pmaxday: KnockoutObservable<number> = ko.observable(null);

            // table result
            timeStart: KnockoutObservable<string>;
            timeOver: KnockoutObservable<string> = ko.observable('00:00:00');
            status: KnockoutObservable<string> = ko.observable(getText("KDM002_28"));
            result: KnockoutObservable<string> = ko.observable('');
            resultMessage: string = getText("KDM002_31");
            total: KnockoutObservable<number> = ko.observable(0);
            pass: KnockoutObservable<number> = ko.observable(0);
            error: KnockoutObservable<number> = ko.observable(0);
            // gridList
            imErrorLog: KnockoutObservableArray<IErrorLog>;
            currentCode: KnockoutObservable<IErrorLog>;
            columns: KnockoutObservableArray<NtsGridListColumn>;

            // flag
            isStop: KnockoutObservable<boolean> = ko.observable(false);
            isComplete: KnockoutObservable<boolean> = ko.observable(false);
            isError: KnockoutObservable<boolean> = ko.observable(false);

            taskId: KnockoutObservable<string> = ko.observable('');
            timeStartt: any;
            timeNow: any;
            excelContent: KnockoutObservable<number> = ko.observable(0);
            exportTaskId: string;
            startExportExcel: KnockoutObservable<boolean>;
            constructor() {
                let self = this;
                self.timeStartt = new Date();
                let systemDate = Date.now();
                let convertdLocalTime = new Date(systemDate);
                let hourOffset = convertdLocalTime.getTimezoneOffset() / 60;
                convertdLocalTime.setHours(convertdLocalTime.getHours() - hourOffset);
                self.timeStart = ko.observable(moment.utc(convertdLocalTime).format("YYYY/MM/DD H:mm:ss"));
                // get params from KDM002-A
                let parrams = getShared('KDM002Params');
                self.pempployeeList(parrams.empployeeList);
                self.pstartDate(parrams.startDate);
                self.pendDate(parrams.endDate);
                self.pdate(parrams.date);
                self.pmaxday(parrams.maxday);
                self.resultMessage = getText("KDM002_31");
                //self.result = ko.observable('0 / '+self.pempployeeList().length +'人');
                self.result = ko.observable(self.resultMessage.replace('{0}', '0').replace('{1}', self.pempployeeList().length));
                let dataDump = {
                    employeeCode: '',
                    employeeName: '',
                    errorMessage: '',
                };
                self.total(self.pempployeeList().length);
                self.currentCode = ko.observable(dataDump);
                self.imErrorLog = ko.observableArray([]);
                self.columns = ko.observableArray([
                    { headerText: nts.uk.resource.getText("KDM002_25"), key: 'employeeCode', width: 140 },
                    { headerText: nts.uk.resource.getText("KDM002_26"), key: 'employeeName', width: 150 },
                    { headerText: nts.uk.resource.getText("KDM002_27"), key: 'errorMessage', width: 300 }
                ]);
                self.startExportExcel = ko.observable(false);
                self.startExportExcel.subscribe((data) => {
                    if (data) {
                        self.excelExport();
                    }
                });
            }

            /**
            * start page data 
            */
            public startPage(): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred();
                self.execution().done(() => {
                }).always(() => {
                    dfd.resolve();
                });;

                return dfd.promise();
            }

            /**
            * execution 
            */
            public execution(): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred();
                let command: CheckFuncDto = new CheckFuncDto({
                    total: self.total(),
                    error: 0,
                    pass: 0,
                    outputErrorList: null,
                    employeeList: self.pempployeeList(),
                    startTime: moment.utc(self.pstartDate()).toISOString(),
                    endTime: moment.utc(self.pendDate()).toISOString(),
                    date: moment.utc(self.pdate()).toISOString(),
                    maxDay: self.pmaxday()
                });

                // find task id
                service.execution(command).done(function(res: any) {
                    dfd.resolve(self);
                    self.taskId(res.taskInfor.id);
                    // update state
                    self.updateState();
                    $('#BTN_CLOSE').focus();
                }).fail(function(res: any) {
                    dfd.resolve(self);
                });

                return dfd.promise();
            }

            /**
             * updateState
             */
            private updateState() {
                let self = this;
                let exContent;
                // 1秒おきに下記を実行
                nts.uk.deferred.repeat(conf => conf
                    .task(() => {
                        return nts.uk.request.asyncTask.getInfo(self.taskId()).done(function(res: any) {
                            if (res.running || res.succeeded || res.cancelled) {
                                self.imErrorLog.removeAll();
                                _.forEach(res.taskDatas, item => {
                                    if (item.key.substring(0, 10) == "ERROR_LIST") {
                                        let error = JSON.parse(item.valueAsString);
                                        let errorContent: IErrorLog = {
                                            employeeCode: error.employeeCode,
                                            employeeName: error.employeeName,
                                            errorMessage: nts.uk.resource.getMessage(error.errorMessage, [self.pmaxday()])
                                        }
                                        self.imErrorLog.push(errorContent);
                                    } else if (item.key == 'NUMBER_OF_SUCCESS') {
                                        // 処理カウント
                                        self.result(self.resultMessage.replace("{0}", item.valueAsNumber).replace("{1}", self.total()));
                                    }
                                });

                                if (res.succeeded) {
                                    self.exportTaskId = _.find(res.taskDatas, i => i.key == "EXPORT_TASK_ID").valueAsString;
                                    self.excelContent(_.find(res.taskDatas, i => i.key == "EXPORT_SIZE").valueAsNumber)
                                }

                                if (res.running) {
                                    // 経過時間＝現在時刻－開始時刻
                                    self.timeNow = new Date();
                                    let over = (self.timeNow.getSeconds() + self.timeNow.getMinutes() * 60 + self.timeNow.getHours() * 60) - (self.timeStartt.getSeconds() + self.timeStartt.getMinutes() * 60 + self.timeStartt.getHours() * 60);
                                    let time = new Date(null);
                                    time.setSeconds(over); // specify value for SECONDS here
                                    let result = time.toISOString().substr(11, 8);

                                    self.timeOver(result);
                                }
                            }

                            if (res.succeeded || res.failed || res.cancelled) {
                                if (self.excelContent() == 0 && self.imErrorLog().length == 0) {
                                    self.status(getText("KDM002_29"));
                                    $('#BTN_CLOSE').focus();
                                }
                                if (self.imErrorLog().length > 0) {
                                    var windowSize = nts.uk.ui.windows.getSelf();
                                    windowSize.$dialog.dialog("option", "width", 670);
                                    windowSize.$dialog.dialog("option", "height", 570);

                                    self.isError(true);
                                    self.isComplete(true);
                                    self.status(getText("KDM002_30"));
                                    $('#BTN_ERROR_EXPORT').focus();
                                }
                                if (self.excelContent() > 0) {
                                    if (res.succeeded) {
                                        if (self.imErrorLog().length == 0) {
                                            self.status(getText("KDM002_29"));
                                            $('#BTN_CLOSE').focus();
                                        } else {
                                            self.status(getText("KDM002_30"));
                                            $('#BTN_ERROR_EXPORT').focus();
                                        }
                                        self.startExportExcel(true);
                                        $('#BTN_ERROR_EXPORT').focus();
                                    }
                                } else if (res.cancelled) {
                                    self.status(getText("KDM002_23"));
                                }

                                self.isStop(true);

                                if (self.excelContent() == 0 && self.imErrorLog().length == 0) {
                                    $('#BTN_CLOSE').focus();
                                }
                                if (self.imErrorLog().length > 0) {
                                    $('#BTN_ERROR_EXPORT').focus();
                                }
                                if (self.excelContent() > 0) {
                                    if (res.succeeded) {
                                        if (self.imErrorLog().length == 0) {
                                            $('#BTN_CLOSE').focus();
                                        } else {
                                            $('#BTN_ERROR_EXPORT').focus();
                                        }
                                        $('#BTN_ERROR_EXPORT').focus();
                                    }
                                } else if (res.cancelled) {
                                    $('#BTN_CLOSE').focus();
                                }
                            }
                        });
                    }).while(infor => {
                        return infor.pending || infor.running;
                    }).pause(1000));
            }

            // 中断ボタンをクリックする
            stop() {
                let self = this;
                self.isStop(true);
                if (nts.uk.text.isNullOrEmpty(self.taskId())) {
                    return;
                }
                nts.uk.request.asyncTask.requestToCancel(self.taskId());
                $('#BTN_CLOSE').focus();
            }

            //閉じるボタンをクリックする
            close() {
                nts.uk.ui.windows.close();
            }

            //エラー出力ボタンをクリックする
            errorExport() {
                let self = this;
                nts.uk.ui.block.invisible();
                service.exportDatatoCsv(ko.toJS(self.imErrorLog())).always(function() {
                    nts.uk.ui.block.clear();
                });
            }

            // Excel出力情報ListをもとにExcel出力をする (Xuất ra file excel)
            excelExport() {
                let self = this;
                nts.uk.ui.block.invisible();
                nts.uk.deferred.repeat(conf => conf.task(() => {
                    return nts.uk.request.asyncTask.getInfo(self.exportTaskId).done(function(res: any) {
                        if (res.status == "PENDING" || res.status == "RUNNING") {
                            console.log("running");
                        } else if (res.failed || res.status == "ABORTED") { 
                            console.log(res.error);
                            nts.uk.ui.block.clear();
                        } else {
                            nts.uk.request.specials.donwloadFile(res.id);
                            nts.uk.ui.block.clear();
                        }
                    });
                }).while(infor => {
                    return infor.pending || infor.running;
                }).pause(1000));
            }
        }

        interface IErrorLog {
            employeeCode: string;
            employeeName: string;
            errorMessage: string;
        }

        class ErrorLog {
            employeeCode: string;
            employeeName: string;
            errorMessage: string
            constructor(param: IErrorLog) {
                let self = this;
                self.employeeCode = param.employeeCode;
                self.employeeName = param.employeeName;
                self.errorMessage = param.errorMessage;
            }
        }

        interface ICheckFuncDto {
            total: number;
            error: number;
            pass: number;
            outputErrorList: IErrorLog[];
            employeeList: EmployeeSearchDto[];
            startTime: string;
            endTime: string;
            date: string;
            maxDay: number;
        }

        class CheckFuncDto {
            total: number;
            error: number;
            pass: number;
            outputErrorList: IErrorLog[];
            employeeList: EmployeeSearchDto[];
            startTime: string;
            endTime: string;
            date: string;
            maxDay: number;

            constructor(param: ICheckFuncDto) {
                let self = this;
                self.total = param.total;
                self.error = param.error;
                self.pass = param.pass;
                self.outputErrorList = param.outputErrorList;
                self.employeeList = param.employeeList;
                self.startTime = param.startTime;
                self.endTime = param.endTime;
                self.date = param.date;
                self.maxDay = param.maxDay;
            }
        }
    }
}