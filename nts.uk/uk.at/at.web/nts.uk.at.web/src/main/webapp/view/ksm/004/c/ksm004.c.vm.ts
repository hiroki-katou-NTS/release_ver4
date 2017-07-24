module nts.uk.at.view.ksm004.c.viewmodel {
    import block = nts.uk.ui.block;
    export class ScreenModel {
        year: KnockoutObservable<number>;
        startDate: KnockoutObservable<string>;
        endDate: KnockoutObservable<string>;
        yearGetShare: KnockoutObservable<number>;
        // Grid list
        allHolidays: KnockoutObservableArray<PublicHoliday>;
        filterHolidays: KnockoutObservableArray<PublicHoliday>;
        columns: KnockoutObservableArray<any>;
        currentCode: KnockoutObservable<any>;

        // Holiday Details
        selectedPublicHoliday: KnockoutObservable<PublicHolidayObs>;

        // UI
        isCreate: KnockoutObservable<boolean>;
        constructor() {
            var self = this;
            self.year = ko.observable(null);
            self.startDate = ko.observable('');
            self.endDate = ko.observable('');
            self.yearGetShare = ko.observable(null);
            self.year.subscribe((newValue) => {
                self.startDate(newValue + "/01/01");
                self.endDate(newValue + "/12/31");
                self.findPublicHolidayByYear(newValue);
                self.selectHolidayByIndex(0);
            });
            self.yearGetShare = nts.uk.ui.windows.getShared('KSM004_C_PARAM');
            //Grid List
            self.allHolidays = ko.observableArray([]);
            self.filterHolidays = ko.observableArray([]);
            self.currentCode = ko.observable(null);
            self.currentCode.subscribe((value: string) => {
                self.findPublicHoliday(value);
            });
            self.columns = ko.observableArray([
                { headerText: nts.uk.resource.getText("KSM004_23"), key: 'date', hidden: true },
                { headerText: nts.uk.resource.getText("KSM004_23"), key: 'displayDate', width: 85 },
                { headerText: nts.uk.resource.getText("KSM004_24"), key: 'holidayName', width: 240 }
            ]);
            // Holiday Details
            self.selectedPublicHoliday = ko.observable(null);
            // UI
            self.isCreate = ko.observable(false);
            self.isCreate.subscribe((value) => {
                self.changeInitMode(value);
            });
        }

        /** Start Page */
        startPage(): JQueryPromise<any> {
            var dfd = this.getAllData();
            var self = this;
            block.invisible();
            dfd.done(() => {
                block.clear();
                self.year(self.yearGetShare.yearMonth);
                self.selectHolidayByIndex(0);
            });
            return dfd;
        } 

        /** Get all data from server */
        private getAllData(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            /** Get list TitleMenu*/
            service.getHolidayByDate().done(function(listPublicHoliday: Array<viewmodel.PublicHoliday>) {
                var allHoliday: Array<PublicHoliday> = [];
                _.forEach(listPublicHoliday, (item) => {
                    allHoliday.push(new PublicHoliday(item.date, item.holidayName));
                });
                allHoliday = _.orderBy(allHoliday, ["date"], ["asc"]);
                self.allHolidays(allHoliday);
                if (listPublicHoliday.length > 0) {
                    self.isCreate(false);
                }
                else {
                    self.findPublicHoliday(null);
                    self.isCreate(true);
                }
                dfd.resolve();
            }).fail(function(error) {
                dfd.fail();
                alert(error.message);
            });
            return dfd.promise();
        }

        private reloadData(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            self.getAllData().done(() => {
                self.findPublicHolidayByYear(self.year());
                dfd.resolve();
            });
            return dfd.promise();
        }

        private findPublicHolidayByYear(year: number) {
            var self = this;
            var filterHolidays = _.filter(self.allHolidays(), (item) => {
                return item.date.toString().substr(0, 4) == year.toString();
            });
            self.filterHolidays(filterHolidays);
        }


        /** Select Holiday by Index: Start & Delete case */
        private selectHolidayByIndex(index: number) {
            var self = this;
            var selectHolidayByIndex = _.nth(self.filterHolidays(), index);
            if (selectHolidayByIndex !== undefined)
                self.currentCode(selectHolidayByIndex.date);
            else
                self.currentCode(null);
        }

        /** Select Holiday by Code: Create & Update case*/
        private selectByCode(code: string) {
            this.currentCode(code);
        }

        /** Init Mode */
        private changeInitMode(isCreate: boolean) {
            var self = this;
            nts.uk.ui.errors.clearAll();
            if (isCreate === true) {
                self.currentCode(null);
            }
        }

        /** Create Button Click */
        createButtonClick() {
            var self = this;
            self.isCreate(true);
            self.currentCode(null);
            self.selectedPublicHoliday(new PublicHolidayObs("", ""));
            _.defer(() => { $("#date").focus(); });
            nts.uk.ui.errors.clearAll();
        }

        /** Registry Button Click */
        registryButtonClick() {
            var self = this;
            $(".nts-input").trigger("validate");
            nts.uk.ui.block.invisible();
            if (self.isCreate() === true) {
                service.createPublicHoliday(self.selectedPublicHoliday().toPublicHoliday()).done((data) => {
                    self.reloadData().done(() => {
                        self.selectByCode(self.selectedPublicHoliday().date());
                    });
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => {
                        $("#name").focus();
                    });
                }).fail((res) => {
                    nts.uk.ui.dialog.alert({ messageId: "Msg_132" }).then(() => {
                        $("#date").focus();
                    });
                }).always(() => {
                    nts.uk.ui.block.clear();
                });
            }
            else {
                service.updatePublicHoliday(self.selectedPublicHoliday().toPublicHoliday()).done((data) => {
                    self.reloadData();
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => {
                        $("#name").focus();
                    });
                }).always(() => {
                    nts.uk.ui.block.clear();
                });
            }
        }

        /**Delete Button Click */
        removePublicHoliday() {
            var self = this;
            if (self.currentCode() !== null) {
                nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(function() {
                    nts.uk.ui.block.invisible();
                    service.deletePublicHoliday({ date: self.selectedPublicHoliday().date() }).done(() => {
                        var index = _.findIndex(self.filterHolidays(), (item) => {
                            return item.date == self.currentCode();
                        });
                        index = _.min([self.filterHolidays().length - 2, index]);
                        self.reloadData().done(() => {
                            self.selectHolidayByIndex(index);
                            nts.uk.ui.dialog.info({ messageId: "Msg_16" }).then(() => {
                                self.focusByMode();
                            });
                            nts.uk.ui.errors.clearAll();
                        });
                    }).fail((res) => {
                        nts.uk.ui.dialog.alertError({ messageId: res.messageId });
                    }).always(() => {
                        nts.uk.ui.block.clear();
                    });
                })
            }
        }
        /** Cancle Dialog */
        cancel_Dialog(): any {
            nts.uk.ui.windows.close();
        }
        /*OpenDialog Rename holiday by Google API (KSM 004 E) */
        holidayData() {
            var self = this;
            nts.uk.ui.windows.setShared("yearPicker", self.year());
            nts.uk.ui.windows.setShared("filterHoliday", self.filterHolidays());
            nts.uk.ui.windows.sub.modal("/view/ksm/004/e/index.xhtml", { title: nts.uk.resource.getText("Googleデータ確認") }).onClosed(() => {
            });
        }

        /** Get Selected PublicHoliday */
        private findPublicHoliday(value: any) {
            var self = this;
            var selectedPublicHoliday = _.find(self.filterHolidays(), (item) => {
                return item.date == value;
            });
            if (selectedPublicHoliday !== undefined) {
                self.isCreate(false);
                self.selectedPublicHoliday(new PublicHolidayObs(selectedPublicHoliday.date, selectedPublicHoliday.holidayName));
                _.defer(() => { $("#name").focus(); });
            }
            else {
                self.isCreate(true);
                self.selectedPublicHoliday(new PublicHolidayObs("", ""));
                _.defer(() => { $("#date").focus(); });
            }
        }

        private focusByMode() {
            var self = this;
            if (self.isCreate() === true)
                $("#date").focus();
            else
                $("#name").focus();
        }

    }

    export class PublicHoliday {
        date: string;
        holidayName: string;
        displayDate: string;

        constructor(date: string, holidayName: string) {
            this.date = date;
            this.holidayName = holidayName;
            this.displayDate = moment(date, 'YYYYMMDD').format('MM月DD日');
        }
    }

    export class PublicHolidayObs {
        date: KnockoutObservable<string>;
        holidayName: KnockoutObservable<string>;

        constructor(date: string, holidayName: string) {
            this.date = ko.observable(date);
            this.holidayName = ko.observable(holidayName);
        }

        toPublicHoliday(): PublicHoliday {
            return new PublicHoliday(this.date(), this.holidayName());
        }
    }

} 