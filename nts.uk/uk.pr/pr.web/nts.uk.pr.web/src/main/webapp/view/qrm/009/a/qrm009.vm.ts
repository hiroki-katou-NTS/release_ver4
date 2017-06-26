module qrm009.vm {
    export class ScreenModel {
        startDate: KnockoutObservable<string>;
        endDate: KnockoutObservable<string>;
        columnPersonGridList: any;
        lstPerson: KnockoutObservableArray<any>;
        selectedPersons: KnockoutObservableArray<any>;

        constructor() {
            var self = this;
            self.startDate = ko.observable(moment().format("YYYY/MMM/DD"));
            self.endDate = ko.observable(moment().format("YYYY/MMM/DD"));
            self.columnPersonGridList = [
                { headerText: 'personId', key: 'personId', hidden: true },
                { headerText: 'コード', key: 'employeeCode', width: 100 },
                { headerText: '名称', key: 'employeeName', width: 150 }
            ];
            self.lstPerson = ko.observableArray([
                { personId: '999000000000000000000000000000000001', employeeCode: 'A0000001', employeeName: '従業員名 1' },
                { personId: '999000000000000000000000000000000002', employeeCode: 'A0000002', employeeName: '従業員名 2' },
                { personId: '999000000000000000000000000000000003', employeeCode: 'A0000003', employeeName: '従業員名 3' },
                { personId: '999000000000000000000000000000000004', employeeCode: 'A0000004', employeeName: '従業員名 4' },
                { personId: '999000000000000000000000000000000005', employeeCode: 'A0000005', employeeName: '従業員名 5' },
                { personId: '999000000000000000000000000000000006', employeeCode: 'A0000006', employeeName: '従業員名 6' },
                { personId: '999000000000000000000000000000000007', employeeCode: 'A0000007', employeeName: '従業員名 7' },
                { personId: '999000000000000000000000000000000008', employeeCode: 'A0000008', employeeName: '従業員名 8' },
                { personId: '999000000000000000000000000000000009', employeeCode: 'A0000009', employeeName: '従業員名 9' },
                { personId: '999000000000000000000000000000000010', employeeCode: 'A0000010', employeeName: '従業員名 0' }
            ]);
            self.selectedPersons = ko.observableArray([]);

        }

        exportPDF() {
            var self = this;
            if (self.selectedPersons().length > 0) {
                let query = {
                    lstPersonId: self.selectedPersons(),
                    startDate: moment(self.startDate()).utc().toISOString(),
                    endDate: moment(self.endDate()).utc().toISOString()
                };
                if (moment(self.startDate()) <= moment(self.endDate())) {
                    service.exportRetirementPaymentPDF(query)
                        .fail(function(res) {
                            nts.uk.ui.dialog.alert(res.message);
                        });
                } else {
                    nts.uk.ui.dialog.alert("範囲の指定が正しくありません。");
                }
            } else {
                nts.uk.ui.dialog.alert("社員選択が選択されていません。");
            }
        }
    }
}