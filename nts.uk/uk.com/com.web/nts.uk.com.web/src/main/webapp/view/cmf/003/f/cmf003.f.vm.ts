module nts.uk.com.view.cmf003.f {
    
    import getShared = nts.uk.ui.windows.getShared;
    import getText = nts.uk.resource.getText;
    
    export module viewmodel {
        export class ScreenModel {
            // interval 1000ms request to server
            interval: any;
            
            // received storeProcessingId from E
            storeProcessingId: string;
            
            // time when start process
            timeStart: any;
            
            // F1_6
            timeOver: KnockoutObservable<string>;
            
            // F1_7
            status: KnockoutObservable<string>;
            categoryCount: KnockoutObservable<number>;
            categoryTotalCount: KnockoutObservable<number>;
            errorCount: KnockoutObservable<number>;
            
            // F2_2
            dataSaveSetName : string;
            dayStartValue : string;
            dayEndValue : string;
            monthStartValue : string;
            monthEndValue : string;
            yearStartValue : string;
            yearEndValue : string;
            
            // dialog mode
            dialogMode: KnockoutObservable<string>;
            
            // check file has been downloaded already
            isDownloaded: KnockoutObservable<boolean>;
            
            constructor() {
                let self = this;
                let params =  nts.uk.ui.windows.getShared("CMF001_E_PARAMS");
                
                self.timeStart = new Date();
                self.timeOver = ko.observable('00:00:00');
                self.storeProcessingId = params.storeProcessingId;
                self.dataSaveSetName = params.dataSaveSetName;
                self.dayStartValue = moment.utc(params.dayValue.startDate, 'YYYY/MM/DD').format("YYYY/MM/DD");
                self.dayEndValue = moment.utc(params.dayValue.endDate, 'YYYY/MM/DD').format("YYYY/MM/DD");
                self.monthStartValue = moment.utc(params.monthValue.startDate, 'YYYY/MM/DD').format("YYYY/MM");
                self.monthEndValue = moment.utc(params.monthValue.endDate, 'YYYY/MM/DD').format("YYYY/MM");
                self.yearStartValue = moment.utc(params.yearValue.startDate, 'YYYY/MM/DD').format("YYYY");
                self.yearEndValue = moment.utc(params.yearValue.endDate, 'YYYY/MM/DD').format("YYYY");
                
                // init F1_7
                self.status = ko.observable('');
                self.categoryCount = ko.observable(0);
                self.categoryTotalCount = ko.observable(0);
                self.errorCount = ko.observable(0);
                self.dialogMode = ko.observable("saving");
                self.isDownloaded = ko.observable(false);
            }
            
            //開始
            start(): JQueryPromise<any> {
                let self = this,
                dfd = $.Deferred();

                //データ保存監視処理: 
                self.interval = setInterval(self.confirmProcess, 1000, self);
                
                dfd.resolve();
                return dfd.promise();
            }
            
            /**
            * confirm process after 1s
            */
            public confirmProcess(self): void {
                let storeProcessingId = self.storeProcessingId;
                
                service.findDataStorageMng(storeProcessingId).done(function(res: any) {
                    let storageMng = res;

                    // F1_6 set time over 
                    let timeNow = new Date();
                    let over = (timeNow.getSeconds()+timeNow.getMinutes()*60+ timeNow.getHours()*60*60) - (self.timeStart.getSeconds()+self.timeStart.getMinutes()*60+ self.timeStart.getHours()*60*60);
                    let time = new Date(null);
                    time.setSeconds(over); // specify value for SECONDS here
                    let result = time.toISOString().substr(11, 8);
                    self.timeOver(result);
                    
                    // F1_7
                    self.status(self.getStatusEnum(storageMng.operatingCondition));
                    self.categoryCount(storageMng.categoryCount);
                    self.categoryTotalCount(storageMng.categoryTotalCount);
                    self.errorCount(storageMng.errorCount);
                    
                    // update mode when end: DONE, INTERRUPTION_END, ABNORMAL_TERMINATION
                    // 完了, 中断終了, 異常終了
                    if((storageMng.operatingCondition == 4) || (storageMng.operatingCondition == 5) || (storageMng.operatingCondition == 6)) {
                        // stop auto request to server
                        clearInterval(self.interval);
                        
                        // end: update dialog to complete mode
                        if(storageMng.operatingCondition == 4) {
                            self.dialogMode("done");
                            
                            // confirm down load when done
                            nts.uk.ui.dialog.confirm({ messageId: "Msg_334" })
                            .ifYes(() => {
                                service.findResultOfSaving(storeProcessingId).done(function(res: any) {
                                    let fileId = res.fileId;
                                    nts.uk.request.specials.donwloadFile(fileId);
                                    self.isDownloaded(true);
                                    $('#F3_3').focus();
                                }).fail(function(res: any) {
                                    console.log("Get fileId fail");
                                });                            
                            })
                            .ifNo(() => {
                                $('#F3_3').focus();
                                return;
                            });
                        }
                        
                        // end: update dialog to Error/Interrupt mode
                        if((storageMng.operatingCondition == 5) || (storageMng.operatingCondition == 6)) {
                            self.dialogMode("error_interrupt");
                            $('#F3_3').focus();
                        }
                        
                        // delete dataStorageMng of process when end
                        let dataStorageMng = new DataStorageMng(storeProcessingId, 0, 0, 0, 0, 0);
                        service.deleteDataStorageMng(dataStorageMng).done(function(res: any) {
                            console.log("delete success");
                        }).fail(function(res: any) {
                            console.log("delete fails");
                        });
                    }
                }).fail(function(res: any) {
                    console.log("findDataStorageMng fail");
                });
            }
            
            // interrupt process when click button
            public interrupt(): void {
                let self = this;
                let dataStorageMng = new DataStorageMng(self.storeProcessingId, 1, self.categoryCount(), self.categoryTotalCount(), self.errorCount(), 5);
                
                nts.uk.ui.dialog.confirm({ messageId: "Msg_387" })
                .ifYes(() => {
                    service.setInterruptSaving(dataStorageMng).done(function(res: any) {
                        console.log("interrupt success");
                    }).fail(function(res: any) {
                        console.log("interrupt fail");
                    });
                })
                .ifNo(() => {
                    return;
                });
            }
            
            public download(): void {
                let self = this;
                
                // confirm down load when click button
                nts.uk.ui.dialog.confirm({ messageId: "Msg_388" })
                .ifYes(() => {
                    service.findResultOfSaving(self.storeProcessingId).done(function(res: any) {
                        let fileId = res.fileId;
                        nts.uk.request.specials.donwloadFile(fileId);
                        self.isDownloaded(true);
                    }).fail(function(res: any) {
                        console.log("Get fileId fail");
                    });
                })
                .ifNo(() => {
                    return;
                });
            }
            
            // close popup
            public close(): void {
                 nts.uk.ui.windows.close();
                 $("#E1_1").focus();
            }
            
            public getStatusEnum(value): string {
                if (value && value === 0) {
                    return getText('Enum_OperatingCondition_INPREPARATION');
                } else if (value && value === 1) {
                    return getText('Enum_OperatingCondition_SAVING');
                } else if (value && value === 2) {
                    return getText('Enum_OperatingCondition_INPROGRESS');
                } else if (value && value === 3) {
                    return getText('Enum_OperatingCondition_DELETING');
                } else if (value && value === 4) {
                    return getText('Enum_OperatingCondition_DONE');
                } else if (value && value === 5) {
                    return getText('Enum_OperatingCondition_INTERRUPTION_END');
                } else if (value && value === 6) {
                    return getText('Enum_OperatingCondition_ABNORMAL_TERMINATION');
                }
            }
        }
        
        class DataStorageMng {
            storeProcessingId : string;
            doNotInterrupt: number;
            categoryCount: number;
            categoryTotalCount: number;
            errorCount: number;
            operatingCondition: number;
        
            constructor(storeProcessingId : string, doNotInterrupt: number, categoryCount: number,
                    categoryTotalCount: number, errorCount: number, operatingCondition: number) {
                this.storeProcessingId = storeProcessingId;
                this.doNotInterrupt = doNotInterrupt;
                this.categoryCount = categoryCount;
                this.categoryTotalCount = categoryTotalCount;
                this.errorCount = errorCount;
                this.operatingCondition = operatingCondition;
            }
        }
    }
}