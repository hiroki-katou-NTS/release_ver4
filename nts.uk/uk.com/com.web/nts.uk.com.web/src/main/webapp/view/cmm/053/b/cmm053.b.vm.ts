module nts.uk.com.view.cmm053.b.viewmodel {
    import close = nts.uk.ui.windows.close;
    import getText = nts.uk.resource.getText;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import block = nts.uk.ui.block;
    import dialog = nts.uk.ui.dialog;
    export class ScreenModel {
        employeeId: string;
        pastHistoryItems: KnockoutObservableArray<PastHistory> = ko.observableArray([]);
        selectedPastHistory: KnockoutObservable<string> = ko.observable('');
        startDate: KnockoutObservable<string> = ko.observable('');
        endDate: KnockoutObservable<string> = ko.observable('');
        codeB17: KnockoutObservable<string> = ko.observable('');
        nameB18: KnockoutObservable<string> = ko.observable('');
        codeB110: KnockoutObservable<string> = ko.observable('');
        nameB111: KnockoutObservable<string> = ko.observable('');
        codeB112: KnockoutObservable<string> = ko.observable('');
        nameB113: KnockoutObservable<string> = ko.observable('');
        constructor() {
            let self = this;
            self.initScreen();
            self.selectedPastHistory.subscribe(selected => {
                if (selected) {
                    let _pastHistory = _.find(self.pastHistoryItems(), x => { return x.startEndDate === selected });
                    if (_pastHistory) {
                        self.startDate(_pastHistory.startDate());
                        self.endDate(_pastHistory.endDate());
                        self.codeB17(_pastHistory.codeB17());
                        self.nameB18(_pastHistory.nameB18());
                        self.codeB110(_pastHistory.codeB110());
                        self.nameB111(_pastHistory.nameB111());
                        self.codeB112(_pastHistory.codeB112());
                        self.nameB113(_pastHistory.nameB113());
                    }
                }
            });
            self.selectedPastHistory.subscribe(value => {
                $('#B1_1').focus();
            });
        }

        start(): JQueryPromise<any> {
            let self = this;
            var dfd = $.Deferred();
            dfd.resolve();
            return dfd.promise();
        }

        //起動する
        initScreen() {
            let self = this;
            self.employeeId = getShared("CMM053A_employeeId");
            block.invisible();
            service.getPastHistory(self.employeeId).done((result: Array<any>) => {
                if (result && result.length > 0) {
                    result.sort(function compare(a, b) {
                        let dateA: any = new Date(a.startDate);
                        let dateB: any = new Date(b.startDate);
                        return dateB - dateA;
                    });
                    let _pastHistoryList: Array<PastHistory> = _.map(result, x => {
                        return new PastHistory(x.startDate, x.endDate, x.codeB17, x.nameB18, x.codeB110, x.nameB111, x.codeB112, x.nameB113)
                    });
                    self.pastHistoryItems(_pastHistoryList);
                    self.selectedPastHistory(_pastHistoryList[0].startEndDate);
                }
            }).fail(function(error) {
                dialog.alertError(error);
            }).always(function() {
                block.clear();
            });
        }

        //終了する
        closeDialog() {
            close();
        }
    }

    export class PastHistory {
        startEndDate: string;
        startDate: KnockoutObservable<string>;
        endDate: KnockoutObservable<string>;
        codeB17: KnockoutObservable<string>;
        nameB18: KnockoutObservable<string>;
        codeB110: KnockoutObservable<string>;
        nameB111: KnockoutObservable<string>;
        codeB112: KnockoutObservable<string>;
        nameB113: KnockoutObservable<string>;
        constructor(startDate: string, endDate: string, codeB17: string, nameB18: string,
            codeB110: string, nameB111: string, codeB112: string, nameB113: string) {
            this.startEndDate = startDate + '～' + endDate;
            this.startDate = ko.observable(startDate);
            this.endDate = ko.observable(endDate);
            this.codeB17 = ko.observable(codeB17);
            this.nameB18 = ko.observable(nameB18);
            this.codeB110 = ko.observable(codeB110);
            this.nameB111 = ko.observable(nameB111);
            this.codeB112 = ko.observable(codeB112);
            this.nameB113 = ko.observable(nameB113);
        }
    }
}