module nts.uk.com.view.cmm011.b {
    export module viewmodel {
        
        import WorkplaceHistory = base.WorkplaceHistoryAbstract;
        import IHistory = nts.uk.com.view.cmm011.base.IHistory;
        
        export class ScreenModel {
            
            
            workplaceHistory: KnockoutObservable<WorkplaceHistoryModel>;
            startDate: KnockoutObservable<string>;
            endDate: KnockoutObservable<string>;
            
            constructor() {
                let self = this;
                
                self.workplaceHistory = ko.observable(new WorkplaceHistoryModel(self));
                self.startDate = ko.observable('');
                self.endDate = ko.observable("9999/12/31"); //nts.uk.resource.getText("CMM011_27")
            }
            
            /**
             * startPage
             */
            public startPage(): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred<any>();
                self.workplaceHistory().findAllHistory().done(function() {
                    dfd.resolve();
                });
                return dfd.promise();
            }
            
            /**
             * execution
             */
            public execution() {
                let self = this;
                //check screen mode
                if (self.workplaceHistory().screenMode() == ScreenMode.SelectionMode) {
                    self.shareData();
                    return;
                }
                // save history
                self.save();
            }
            
            /**
             * save
             */
            private save(): void {
                let self = this;
                service.saveWkpConfig(self.toJSonCommand()).done(function() {
                    
                    if (self.workplaceHistory().screenMode() != ScreenMode.UpdateMode) {
                        // find all new history
                        self.workplaceHistory().findAllHistory().done(() => {
                            self.shareData();
                        });
                    }
                    // mode update
                    else {
                        nts.uk.ui.dialog.info({ messageId: "Msg_81", messageParams: [] }).then(() => {
                            // find all new history
                            self.workplaceHistory().findAllHistory().done(() => {
                                self.shareData();
                            });
                        });
                    }
                }).fail((res: any) => {
                    self.showMessageError(res);
                });
            }
            
            /**
             * shareData
             */
            private shareData() {
                let self = this;
                let shareData: any = self.toJSonDate(true);
                shareData.isWkpConfigHistLatest = self.workplaceHistory().isSelectedLatestHistory();
                shareData.historyId = self.workplaceHistory().getSelectedHistoryByHistId().historyId;
                
                nts.uk.ui.windows.setShared("ShareDateScreenParent", shareData);
                nts.uk.ui.windows.close();
            }
            
            /**
             * toJSonCommand
             */
            private toJSonCommand(): any {
                let self = this;
                let command: any = {};
                
                let isAddMode: any = null;
                let historyId: string = null;
                // check mode: add or update?
                if (self.workplaceHistory().screenMode() == ScreenMode.NewMode
                    || self.workplaceHistory().screenMode() == ScreenMode.AddMode) {
                    isAddMode =  true;
                }
                else if (self.workplaceHistory().screenMode() == ScreenMode.UpdateMode) {
                    isAddMode = false;
                    historyId = self.workplaceHistory().getSelectedHistoryByHistId().historyId;
                }
                command.isAddMode = isAddMode;
                
                // information of history
                let wkpConfigHistory: any = {};
                wkpConfigHistory.historyId = historyId;
                wkpConfigHistory.period = self.toJSonDate();
                command.wkpConfigHistory = wkpConfigHistory;
                
                return command;
            }
            
            /**
             * toJSonDate
             */
            private toJSonDate(isRespondParent?: boolean): any {
                let self = this;
                
                let startDate: any = new Date(self.startDate());
                let endDate: any = new Date(self.endDate());
                
                // if selection mode, reset start date (startDate cann't be initial)
                if (self.workplaceHistory().screenMode() == ScreenMode.SelectionMode) {
                    startDate = new Date(self.workplaceHistory().getSelectedHistoryByHistId().startDate);
                }
                
                // convert date respond parent screen
                if (isRespondParent) {
                    startDate = nts.uk.time.formatDate(startDate, 'yyyy/MM/dd');
                    endDate = nts.uk.time.formatDate(endDate, 'yyyy/MM/dd');
                }
                return {
                    startDate: startDate,
                    endDate: endDate
                }
            }
            
            /**
             * close
             */
            public close() {
                nts.uk.ui.windows.close();
            }
            
            /**
             * showMessageError
             */
            public showMessageError(res: any) {
                if (res.businessException) {
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds });
                }
            }
        }
        
        /**
         * WorkplaceHistoryModel
         */
        class WorkplaceHistoryModel extends WorkplaceHistory {
            
            parentModel: ScreenModel;
            
           // mode
            screenMode: KnockoutObservable<number>;
            addBtnControl: KnockoutObservable<boolean>;
            updateBtnControl: KnockoutObservable<boolean>;
            
            constructor(parentModel: ScreenModel) {
                super();
                let self = this;
                
                self.parentModel = parentModel;
                
                // mode
                self.screenMode = ko.observable(null);

                self.addBtnControl = ko.computed(function() {
                    if (self.screenMode() == ScreenMode.SelectionMode || self.screenMode() == ScreenMode.UpdateMode) {
                        return self.isSelectedLatestHistory();
                    }
                    return false;
                });
                self.updateBtnControl = ko.computed(function() {
                    if (self.screenMode() == ScreenMode.SelectionMode) {
                        return self.isSelectedLatestHistory();
                    }
                    return false;
                });
                
                self.selectedHistoryId.subscribe(function(newCode) {
                    let detail: IHistory = self.getSelectedHistoryByHistId();
                    self.parentModel.startDate(detail.startDate);
                    self.parentModel.endDate(detail.endDate);
                });
            }
            
            /**
             * addHistory
             */
            public addHistory() {
                let self = this;
                self.screenMode(ScreenMode.AddMode);
            }
            
            /**
             * updateHistory
             */
            public updateHistory() {
                let self = this;
                self.screenMode(ScreenMode.UpdateMode);
            }
            
            /**
             * removeHistory
             */
            public removeHistory() {
                let self = this;
                nts.uk.ui.dialog.confirm({messageId: 'Msg_18'}).ifYes(() => {
                    let command: any = {};
                    command.historyId = self.selectedHistoryId();
                    
                    service.removeWkpConfig(command).done(() => {
                        self.findAllHistory();
                    }).fail((res: any) => {
//                        self.parentModel.showMessageError(res);
                        nts.uk.ui.dialog.bundledErrors(res);
                    });
                });
            }
            
            /**
             * findAllHistory
             */
            public findAllHistory(): JQueryPromise<void> {
                let self = this;
                let dfd = $.Deferred<void>();
                service.findLstWkpConfigHistory().done(function(data: any) {
                    if (data) {
                        self.init(data.wkpConfigHistory);
                    }
                    dfd.resolve();
                }).fail((res: any) => {
                    self.parentModel.showMessageError(res);
                });
                return dfd.promise();
            }
            
            /**
             * init
             */
            private init(data: Array<any>) {
                let self = this;
                let lstWpkHistory: Array<IHistory> = [];
                // if has data, select first
                if (data && data.length < 1) {
                    self.screenMode(ScreenMode.NewMode);
                    self.parentModel.endDate("9999/12/31");//(nts.uk.resource.getText("CMM011_27"));
                    return;
                }
                data.forEach(function(item, index) {
                    //workplaceId not key => ""
                    lstWpkHistory.push({
                        workplaceId: "", historyId: item.historyId, startDate: item.period.startDate,
                        endDate: item.period.endDate
                    });
                });
                self.lstWpkHistory(lstWpkHistory);
                self.selectFirst();
                self.screenMode(ScreenMode.SelectionMode);
            }
        }
        
        //Screen mode define
        enum ScreenMode {
            SelectionMode = 0,
            NewMode = 1,
            AddMode = 2,
            UpdateMode = 3
        }
    }
}