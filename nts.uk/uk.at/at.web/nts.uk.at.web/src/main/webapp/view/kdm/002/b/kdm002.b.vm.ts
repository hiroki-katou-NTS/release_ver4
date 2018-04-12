module nts.uk.at.view.kdm002.b {
    import getText = nts.uk.resource.getText;
    import EmployeeSearchDto = nts.uk.com.view.ccg.share.ccg.service.model.EmployeeSearchDto;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    export module viewmodel {

        export class ScreenModel {
            // param from resources
            CODE_EMP: string = getText("KDM002_25");
            NAME_EMP: string = getText("KDM002_26");
            ERROR_MESS: string = getText("KDM002_27")
            
            // params
            pempployeeList:  KnockoutObservable<EmployeeSearchDto>;
            pstartDate:  KnockoutObservable<string>;
            pendDate: KnockoutObservable<string>;
            pdate:  KnockoutObservable<string>;
            pmaxday:  KnockoutObservable<number>;   
            
            // table result
            timeStart: KnockoutObservable<string> = ko.observable('2018/01/01 13:25:16');
            timeOver: KnockoutObservable<string> = ko.observable('00:00:00');
            status: KnockoutObservable<string> = ko.observable('処理中');
            result: KnockoutObservable<string> = ko.observable('0 / 25人');
            total: KnockoutObservable<number>;
            pass: KnockoutObservable<number> = ko.observable(0);
            error: KnockoutObservable<number> = ko.observable(0);
            // gridList
            imErrorLog: KnockoutObservableArray<IErrorLog>;
            currentCode: KnockoutObservable<IErrorLog>;
            columns: KnockoutObservableArray<NtsGridListColumn>;
            
            // flag
            isStop: KnockoutObservable<boolean> = ko.observable(false);
            isComplete: KnockoutObservable<boolean> = ko.observable(false);
            
            taskId: KnockoutObservable<string> =  ko.observable('');
            constructor() {
                let self = this;
                // get params from KDM002-A
                let parrams = getShared('KDM002Params');
                self.pempployeeList  = ko.observable(parrams.empployeeList);
                self.pstartDate = ko.observable(parrams.startDate);
                self.pendDate = ko.observable(parrams.endDate);
                self.pdate =  ko.observable(parrams.date);
                self.pmaxday=  ko.observable(parrams.maxday);   
                
                self.result = ko.observable('0 / '+self.pempployeeList().length +'人');
                let dataDump = {
                    employeeCode: '',
                    employeeName: '',
                    errorMessage: '',
                };
                self.total = ko.observable(self.pempployeeList().length);
                self.currentCode = ko.observable(dataDump);
                self.imErrorLog =  ko.observableArray([]);
                self.columns = ko.observableArray([
                    { headerText: '会社コード', key: 'employeeCode', width: 100 },
                    { headerText: '会社名', key: 'employeeName', width: 150 },
                    { headerText: 'イラー内容', key: 'errorMessage', width: 300 }
                ]);
            }
            
            /**
            * start page data 
            */
            public startPage(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred();
                var self = this;
                self.execution();
                dfd.resolve();
                return dfd.promise();
            }
            
            /**
            * execution 
            */
            public execution(): void {
                var self = this;
                let command: CheckFuncDto = new CheckFuncDto({
                    total: self.total(),
                    error: 0,
                    pass: 0,
                    outputErrorList: null,
                    employeeList: self.pempployeeList(),
                    startTime: moment.utc(self.pstartDate()).toISOString(),
                    endTime: moment.utc(self.pendDate()).toISOString()
                });
                
                // find task id
                service.execution(command).done(function(res: any) {
                    self.taskId(res.taskInfor.id);
                    // update state
                    self.updateState();
                }).fail(function(res: any) {
                    console.log(res);
                });
            }
            
            /**
             * updateState
             */
            private updateState() {
                let self = this;
                // 1秒おきに下記を実行
                nts.uk.deferred.repeat(conf => conf
                    .task(() => {
                        return nts.uk.request.asyncTask.getInfo(self.taskId()).done(function(res: any) {
                            // update state on screen
                            if (res.running || res.succeeded || res.cancelled) {
                                _.forEach(res.taskDatas, item => {
                                    // 「非同期タスク情報」を取得する
                                    let serverData = JSON.parse(item.valueAsString);
                                    console.log(serverData);
                                    // 処理カウント
                                    if (item.key == 'NUMBER_OF_SUCCESS') {
                                        self.result(item.valueAsNumber+" / " + self.total() + "人");
                                    }
                                });
                                
                                if (res.running) {
                                    // 経過時間＝現在時刻－開始時刻
                                    self.timeNow = new Date();
                                    let over = (self.timeNow.getSeconds()+self.timeNow.getMinutes()*60+ self.timeNow.getHours()*60) - (self.timeStart.getSeconds()+self.timeStart.getMinutes()*60+ self.timeStart.getHours()*60);
                                    let time = new Date(null);
                                    time.setSeconds(over); // specify value for SECONDS here
                                    let result = time.toISOString().substr(11, 8);

                                    self.timeOver(result);
                                }
                            }

                            if (res.succeeded || res.failed || res.cancelled) {
                                if (res.succeeded) {
                                    self.status('完了');  
                                }
                                self.isStop(true);
                            }
                        });
                    }).while(infor => {
                        return infor.pending || infor.running;
                    }).pause(1000));
            }
            
            // 中断ボタンをクリックする
            stop(){
                let self = this;
                self.isStop(true);
                self.isComplete(true);
                if (nts.uk.text.isNullOrEmpty(self.taskId())) {
                    return;
                }
                nts.uk.request.asyncTask.requestToCancel(self.taskId());
                ('#BTN_CLOSE').focus();
            }
            
            //閉じるボタンをクリックする
            close(){
                let self = this;
                nts.uk.ui.windows.close();
            }
            
            //エラー出力ボタンをクリックする
            errorExport(){
                let self = this;
                
            }
        }
        
        interface IErrorLog{
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
        }
        
        class CheckFuncDto {
            total: number;
            error: number;
            pass: number;
            outputErrorList: IErrorLog[];
            employeeList: EmployeeSearchDto[];
            startTime: string;
            endTime: string;
            
             constructor(param: ICheckFuncDto) {
                let self = this;
                self.total = param.total;
                self.error = param.error;
                self.pass = param.pass;
                self.outputErrorList = param.outputErrorList;
                self.employeeList = param.employeeList;
                self.startTime = param.startTime;
                self.endTime = param.endTime;
             }
        }
    }
}