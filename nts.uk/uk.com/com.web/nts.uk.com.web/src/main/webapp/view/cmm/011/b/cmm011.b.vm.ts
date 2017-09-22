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
                self.endDate = ko.observable(nts.uk.resource.getText("CMM011_27"));
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
                    // find all new history
                    self.workplaceHistory().findAllHistory().done(() => {
                        self.shareData();
                    })
                }).fail((res: any) => {
                    self.showMessageError(res);
                });
            }
            
            /**
             * shareData
             */
            private shareData() {
                let self = this;
                nts.uk.ui.windows.setShared("ShareDateScreenParent", self.toJSonDate(true));
                nts.uk.ui.windows.close();
            }
            
            /**
             * toJSonCommand
             */
            private toJSonCommand(): any {
                let self = this;
                let command: any = {};
                
                let isAddMode: any = null;
                
                // check mode: add or update?
                if (self.workplaceHistory().screenMode() == ScreenMode.NewMode
                    || self.workplaceHistory().screenMode() == ScreenMode.AddMode) {
                    isAddMode =  true;
                }
                else if (self.workplaceHistory().screenMode() == ScreenMode.UpdateMode) {
                    isAddMode = false;
                }
                command.isAddMode = isAddMode;
                
                // information of history
                let wkpConfigHistory: any = {};
                wkpConfigHistory.historyId = null;
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
                let endDate: any = new Date("9999-12-31");
                
                // convert date respond parent screen
                if (isRespondParent) {
                    startDate = nts.uk.time.formatDate(startDate, 'yyyy/MM/dd');
                    endDate = nts.uk.time.formatDate(endDate, 'yyyy/MM/dd');
                }
                return {
                    startDate: startDate,
                    endDate: endDate //TODO: CMM011_27
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
                
                // subscribe
                self.lstWpkHistory.subscribe(newList => {
                    // list empty or null -> new mode
                    if (!newList || newList.length <= 0) {
                        self.screenMode(ScreenMode.NewMode);
                        self.parentModel.endDate(nts.uk.resource.getText("CMM011_27"));
                    } else {
                        self.screenMode(ScreenMode.SelectionMode);
                    }
                });
                self.selectedWpkHistory.subscribe(function(newCode) {
                    let detail: IHistory = self.getSelectedHistoryByHistId(newCode);
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
            }
            
            /**
             * findAllHistory
             */
            public findAllHistory(): JQueryPromise<void> {
                let self = this;
                let dfd = $.Deferred<void>();
                service.findLstWkpConfigHistory().done(function(data: any) {
                    if (data.wkpConfigHistory && data.wkpConfigHistory.length > 0) {
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
                data.forEach(function(item, index) {
                    //workplaceId not key => ""
                    lstWpkHistory.push({ workplaceId: "", historyId: item.historyId, startDate: item.period.startDate, endDate: item.period.endDate });
                });
                self.lstWpkHistory(lstWpkHistory);
                
                // if has data, select first
                if (data && data.length > 0) {
                    self.selectFirst();
                }
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