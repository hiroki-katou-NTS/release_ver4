module nts.uk.at.view.kmw005.a {
    import blockUI = nts.uk.ui.block;
    import getText = nts.uk.resource.getText;
    import ActualLockFinderDto = service.model.ActualLockFinderDto;
    import ActualLockFindDto = service.model.ActualLockFindDto;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;

    export module viewmodel {

        export class ScreenModel {
            actualLockList: KnockoutObservableArray<ActualLockFind>;
            actualLockColumn: KnockoutObservableArray<any>;
            actualLock: ActualLock;
            dailyActualLockOpt: KnockoutObservableArray<any>;
            monthlyActualLockOpt: KnockoutObservableArray<any>;
            selectedClosureText: KnockoutObservable<string>;
            closureName: KnockoutObservable<string>;
            yearMonth: number = 0;

            constructor() {
                var self = this;
                self.actualLock = new ActualLock();
                self.actualLockList = ko.observableArray<ActualLockFind>([]);
                self.actualLock.closureId.subscribe(function(closureId: number) {
                    if (closureId) {
                        // ClosureName
                        let currentClosure = self.actualLockList().filter((item) => {
                            return item.closureId == closureId;
                        })[0];
                        self.closureName(currentClosure.closureName);
                        self.yearMonth = currentClosure.yearMonth;
                        self.bindActualLock(closureId);
                    }
                });
                self.actualLockColumn = ko.observableArray([
                    { headerText: getText(''), key: 'closureId', hide: true },
                    { headerText: getText('KMW005_3'), key: 'closureName', width: 105 },
                    { headerText: getText('KMW005_4'), key: 'period', width: 200 },
                    { headerText: getText('KMW005_5'), key: 'dailyLockState', width: 70, formatter: lockIcon },
                    { headerText: getText('KMW005_6'), key: 'monthlyLockState', width: 70, formatter: lockIcon }
                ]);

                self.dailyActualLockOpt = ko.observableArray([
                    { code: '1', name: getText("KMW005_11") },
                    { code: '0', name: getText("KMW005_12") }
                ]);

                self.monthlyActualLockOpt = ko.observableArray([
                    { code: '1', name: getText("KMW005_15") },
                    { code: '0', name: getText("KMW005_16") }
                ]);
                self.closureName = ko.observable("");
                self.selectedClosureText = ko.computed(function() {
                    return nts.uk.resource.getText("KMW005_7", [self.closureName()]);
                });
            }

            /**
             * Start page
             */
            startPage(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred();
                blockUI.invisible();
                service.findAllActualLock().done(function(data) {
                    blockUI.clear();
                    var dataRes: ActualLockFind[] = [];

                    _.forEach(data, function(item: ActualLockFinderDto) {
                        var actualLock: ActualLockFind = new ActualLockFind();
                        actualLock.closureId = item.closureId;
                        actualLock.closureName = item.closureName;
                        actualLock.dailyLockState = item.dailyLockState;
                        actualLock.monthlyLockState = item.monthlyLockState;
                        actualLock.startDate = item.startDate;
                        actualLock.endDate = item.endDate;
                        actualLock.period = item.startDate + " ~ " + item.endDate;
                        actualLock.yearMonth = item.yearMonth;
                        dataRes.push(actualLock);
                    })

                    self.actualLockList(dataRes);
                    self.actualLock.closureId(data[0].closureId);
                    dfd.resolve();
                }).fail(error => {
                    blockUI.clear();
                    self.actualLock.closureId(null);
                    self.actualLock.dailyLockState(0);
                    self.actualLock.monthlyLockState(0);
                    if (error.messageId == 'Msg_183') {
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_183" });
                    } else {
                        nts.uk.ui.dialog.alertError(error);
                    }
                });

                return dfd.promise();
            }


            /**
             * Binding ActualLock By Selected Closure
             */
            private bindActualLock(closureId: number): void {
                let self = this;
                service.findLockByClosureId(closureId).done(function(data: ActualLockFindDto) {
                    if (data) {
                        self.actualLock.updateLock(data);
                    } else {
                        self.actualLock.dailyLockState(0);
                        self.actualLock.monthlyLockState(0);
                    }
                    self.addLockIcon();
                });

            }


            /**
             * Collect ActualLockData 
             */
            private collectActualLockData(): void {
                let self = this;
                let command: any = {};
                command.closureId = self.actualLock.closureId();
                command.dailyLockState = self.actualLock.dailyLockState();
                command.monthlyLockState = self.actualLock.monthlyLockState();
            }

            /**
             * Save ActualLock when press Register
             */
            private saveActualLock(): void {
                let self = this;
                if (!self.actualLock.closureId()) {
                    return;
                }
                let command: any = {};
                command.closureId = self.actualLock.closureId();
                command.dailyLockState = self.actualLock.dailyLockState();
                command.monthlyLockState = self.actualLock.monthlyLockState();
                command.yearMonth = self.yearMonth;

                blockUI.invisible();
                service.saveActualLock(command).done(function(res) {
                    if (!res) {
                        blockUI.clear();
                        return;
                    }
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                    blockUI.clear();

                    service.findAllActualLock().done(function(data) {
                        var dataRes: ActualLockFind[] = [];
                        _.forEach(data, function(item: ActualLockFinderDto) {
                            var actualLock: ActualLockFind = new ActualLockFind();
                            actualLock.closureId = item.closureId;
                            actualLock.closureName = item.closureName;
                            actualLock.dailyLockState = item.dailyLockState;
                            actualLock.monthlyLockState = item.monthlyLockState;
                            actualLock.startDate = item.startDate;
                            actualLock.endDate = item.endDate;
                            actualLock.period = item.startDate + " ~ " + item.endDate;
                            actualLock.yearMonth = item.yearMonth;
                            dataRes.push(actualLock);
                        })

                        self.actualLockList(dataRes);
                        self.addLockIcon();
                    })
                }).fail(function(res) {
                    nts.uk.ui.dialog.alertError(res.message).then(() => { blockUI.clear(); });
                });
            }


            /**
             * Open Dialog: Confirm ActualLock 
             */
            private openDialog(): void {
                let self = this;
                if (!self.actualLock.closureId()) {
                    return;
                }
                let actualLocks = [];
                actualLocks = self.actualLockList().map(item => {
                    return item.toClosureDto();
                });

                setShared('ActualLock', actualLocks, true);

                nts.uk.ui.windows.sub.modal("/view/kmw/005/b/index.xhtml").onClosed(function() {
                    var output = getShared('childData');
                });
            }

            /**
             * Add LockIcon to columns DailyLock, MonthlyLock.
             */
            private addLockIcon() {
                var iconLink = nts.uk.request.location.siteRoot
                    .mergeRelativePath(nts.uk.request.WEB_APP_NAME["at"] + '/')
                    .mergeRelativePath('/view/kmw/005/a/images/2.png').serialize();
                $('.icon-2').attr('style', "background: url('" + iconLink + "'); width: 17.727px; height: 17.727px; background-size: 17.727px 17.727px; margin-left: 23px;")
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
         * class ActualLockFind
         */
        export class ActualLockFind {
            closureId: number;
            dailyLockState: number;
            monthlyLockState: number;
            closureName: string;
            startDate: string;
            endDate: string;
            period: string;
            yearMonth: number;

            constructor() {
                this.closureId = 0;
                this.dailyLockState = 0;
                this.monthlyLockState = 0;
                this.closureName = '';
                this.startDate = '';
                this.endDate = '';
                this.period = '';
                this.yearMonth = 0;
            }
            // convert to ClosureDto
            public toClosureDto(): ClosureDto {
                return new ClosureDto(this.closureId, this.closureName, this.yearMonth);
            }
        }

        /**
         * class ClosureDto
         */
        export class ClosureDto {
            closureId: number;
            closureName: string;
            yearMonth: number;

            constructor(closureId: number, closureName: string, yearMonth: number) {
                this.closureId = closureId;
                this.closureName = closureName;
                this.yearMonth = yearMonth;
            }
        }

        /**
         * class ActualLock
         */
        export class ActualLock {
            closureId: KnockoutObservable<number>;
            dailyLockState: KnockoutObservable<number>;
            monthlyLockState: KnockoutObservable<number>;

            constructor() {
                this.closureId = ko.observable(0);
                this.dailyLockState = ko.observable(0);
                this.monthlyLockState = ko.observable(0);
            }

            updateLock(dto: ActualLockFindDto): void {
                this.dailyLockState(dto.dailyLockState);
                this.monthlyLockState(dto.monthlyLockState);
            }
        }

    }
}