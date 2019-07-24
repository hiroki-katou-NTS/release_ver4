module nts.uk.at.view.kmw005.b {
    import blockUI = nts.uk.ui.block;
    import getText = nts.uk.resource.getText;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import ActualLockHistFindDto = service.model.ActualLockHistFindDto;
    import ActualLockHistFind = service.model.ActualLockHistFind;
    
    export module viewmodel {

        export class ScreenModel {
            closureList: KnockoutObservableArray<ClosureDto>;
            selectedClosure: KnockoutObservable<number>;
            closureColumn: KnockoutObservableArray<any>;
            yearMonth: KnockoutObservable<number>;
            
            lockHistList: KnockoutObservableArray<ActualLockHistFind>;
            lockHist: ActualLockHist;
            lockHistColumn: KnockoutObservableArray<any>;
            isFirstTime: boolean = true;
            
            constructor() {
                let self = this;
                self.closureColumn = ko.observableArray([
                    { headerText: getText(''), key: 'closureId', hide: true },
                    { headerText: getText('KMW005_18'), key: 'closureName', width: 100 }
                ]);
                let closures: ClosureDto[] = getShared('ActualLock');
                self.closureList = ko.observableArray(closures);
                self.selectedClosure = ko.observable(0);
                self.selectedClosure.subscribe(()=> {
                    self.bindLockHistByYM();
                });
                // Initialize yearMonth value: current system YearMonth.
                self.yearMonth = ko.observable(self.closureList()[0].yearMonth);
                self.yearMonth.subscribe(function(){
                    self.bindLockHistByYM();
                });
                self.lockHistList = ko.observableArray<ActualLockHistFind>([]);
                self.lockHist = new ActualLockHist;
                self.lockHistColumn = ko.observableArray([
                    { headerText: getText(''), key: 'closureId', hide: true },
                    { headerText: getText('KMW005_20'), key: 'lockDateTime', width: 170 },
                    { headerText: getText('KMW005_21'), key: 'updater', width: 130 },
                    { headerText: getText('KMW005_26'), key: 'targetMonth', width: 80 },
                    { headerText: getText('KMW005_22'), key: 'dailyLockState', width: 120, formatter: lockIcon },
                    { headerText: getText('KMW005_23'), key: 'monthlyLockState', width: 120, formatter: lockIcon }
                ]);
                
                
            }
            
            /**
             * startPage
             */
            public startPage(): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred<void>();
                blockUI.invisible();
                // Selected first Closure in list
                self.selectedClosure(self.closureList()[0].closureId);
                blockUI.clear();
                // Focus on Target YearMonth
                $('#targetYM').focus();
                dfd.resolve();
                return dfd.promise();
            }
            
            /**
             * Add LockIcon to columns DailyLock, MonthlyLock.
             */
            private addLockIcon() {
                var iconLink = nts.uk.request.location.siteRoot
                    .mergeRelativePath(nts.uk.request.WEB_APP_NAME["at"] + '/')
                    .mergeRelativePath('/view/kmw/005/a/images/2.png').serialize();
                $('.icon-2').attr('style', "background: url('" + iconLink + "'); width: 17px; height: 17px; background-size: 17px 17px; margin-left: 46px;")
            }
            
            /**
             * Bindding Lock History By Target YearMonth Selected
             */
            private bindLockHistByYM(): void {
                let self = this;
                if(!self.isFirstTime && nts.uk.ui.errors.hasError()){
                   return; 
                }
                self.isFirstTime = false;
                service.findHistByTargetYM(self.selectedClosure(),self.yearMonth()).done(function(data: Array<ActualLockHistFindDto>) {
                        self.setLockHistList(data);
                        self.addLockIcon();
                    }).fail(function() {
                        return;
                    });
            }
            
            /**
             * Convert ActualLockHistFindDto to ActualLockHistFind
             */
            private setLockHistList(list: Array<ActualLockHistFindDto>): void {
                let self = this;
                var convertList: ActualLockHistFind[] = [];
                
                _.forEach(list, function(item: ActualLockHistFindDto) {
                    var lockHist: ActualLockHistFind = new ActualLockHistFind();
                    lockHist.closureId = item.closureId;
                    lockHist.dailyLockState = item.dailyLockState;
                    lockHist.monthlyLockState = item.monthlyLockState;
                    lockHist.lockDateTime = item.lockDateTime;
                    lockHist.updater = item.updater;
                    lockHist.targetMonth = item.targetMonth.toString().substring(0, 4) + "/" 
                    + item.targetMonth.toString().substring(4);
                    convertList.push(lockHist);
                })
                
                self.lockHistList(convertList);
            }
            
            /**
             * Close dialog.
             */
            private closeDialog(): void {
                nts.uk.ui.windows.close();
            }

        }
        
        /**
         * lockIcon
         */
        function lockIcon(value, row) {
            if (value == '1')
                return "<i class='icon icon-2 icon-style'></i>";
            return '';
        }
        
        /**
         * ClosureDto
         */
        export class ClosureDto {
            closureId: number;
            closureName: string;

            constructor() {
                this.closureId = 0;
                this.closureName = "";
            }
        }
        
        /**
         * class ActualLockHist
         */
        export class ActualLockHist {
            closureId: KnockoutObservable<number>;
            dailyLockState: KnockoutObservable<number>;
            monthlyLockState: KnockoutObservable<number>;
            lockDateTime: KnockoutObservable<string>;
            targetMonth: KnockoutObservable<number>;
            updater: KnockoutObservable<string>;
            
            constructor() {
                this.closureId = ko.observable(0);
                this.dailyLockState = ko.observable(0);
                this.monthlyLockState = ko.observable(0);
                this.lockDateTime = ko.observable('');
                this.targetMonth = ko.observable(0);
                this.updater = ko.observable('');
            }
        }

    }
}