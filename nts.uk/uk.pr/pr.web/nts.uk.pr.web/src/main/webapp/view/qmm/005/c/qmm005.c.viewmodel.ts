module qmm005.c {
    export class ViewModel {
        index: KnockoutObservable<number> = ko.observable(0);
        inp001: KnockoutObservable<string> = ko.observable('');

        sel001: KnockoutObservable<number> = ko.observable(1);
        sel002: KnockoutObservable<number> = ko.observable(0);
        sel003: KnockoutObservable<number> = ko.observable(1);
        sel004: KnockoutObservable<number> = ko.observable(0);
        sel005: KnockoutObservable<number> = ko.observable(1);
        sel006: KnockoutObservable<number> = ko.observable(0);
        sel007: KnockoutObservable<number> = ko.observable(0);
        sel008: KnockoutObservable<number> = ko.observable(0);
        sel009: KnockoutObservable<number> = ko.observable(0);
        sel010: KnockoutObservable<number> = ko.observable(1);
        sel011: KnockoutObservable<number> = ko.observable(1);
        sel012: KnockoutObservable<number> = ko.observable(1);
        sel013: KnockoutObservable<number> = ko.observable(1);
        sel014: KnockoutObservable<number> = ko.observable(1);
        sel015: KnockoutObservable<number> = ko.observable(1);

        sel001Data: KnockoutObservableArray<common.SelectItem> = ko.observableArray([]);
        sel002Data: KnockoutObservableArray<common.SelectItem> = ko.observableArray([]);
        sel003Data: KnockoutObservableArray<common.SelectItem> = ko.observableArray([]);
        sel004Data: KnockoutObservableArray<common.SelectItem> = ko.observableArray([]);
        sel005Data: KnockoutObservableArray<common.SelectItem> = ko.observableArray([]);
        sel006Data: KnockoutObservableArray<common.SelectItem> = ko.observableArray([]);
        sel007Data: KnockoutObservableArray<common.SelectItem> = ko.observableArray([]);
        sel008Data: KnockoutObservableArray<common.SelectItem> = ko.observableArray([]);
        sel009Data: KnockoutObservableArray<common.SelectItem> = ko.observableArray([]);
        sel010Data: KnockoutObservableArray<common.SelectItem> = ko.observableArray([]);
        sel011Data: KnockoutObservableArray<common.SelectItem> = ko.observableArray([]);
        sel012Data: KnockoutObservableArray<common.SelectItem> = ko.observableArray([]);
        sel013Data: KnockoutObservableArray<common.SelectItem> = ko.observableArray([]);
        sel014Data: KnockoutObservableArray<common.SelectItem> = ko.observableArray([]);
        sel015Data: KnockoutObservableArray<common.SelectItem> = ko.observableArray([]);

        dirty = {
            inp001: new nts.uk.ui.DirtyChecker(this.inp001),
            sel001: new nts.uk.ui.DirtyChecker(this.sel001),
            sel002: new nts.uk.ui.DirtyChecker(this.sel002),
            sel003: new nts.uk.ui.DirtyChecker(this.sel003),
            sel004: new nts.uk.ui.DirtyChecker(this.sel004),
            sel005: new nts.uk.ui.DirtyChecker(this.sel005),
            sel006: new nts.uk.ui.DirtyChecker(this.sel006),
            sel007: new nts.uk.ui.DirtyChecker(this.sel007),
            sel008: new nts.uk.ui.DirtyChecker(this.sel008),
            sel009: new nts.uk.ui.DirtyChecker(this.sel009),
            sel010: new nts.uk.ui.DirtyChecker(this.sel010),
            sel011: new nts.uk.ui.DirtyChecker(this.sel011),
            sel012: new nts.uk.ui.DirtyChecker(this.sel012),
            sel013: new nts.uk.ui.DirtyChecker(this.sel013),
            sel014: new nts.uk.ui.DirtyChecker(this.sel014),
            sel015: new nts.uk.ui.DirtyChecker(this.sel015),
            isDirty: function() {
                return this.inp001.isDirty()
                    || this.sel001.isDirty()
                    || this.sel002.isDirty()
                    || this.sel003.isDirty()
                    || this.sel004.isDirty()
                    || this.sel005.isDirty()
                    || this.sel006.isDirty()
                    || this.sel007.isDirty()
                    || this.sel008.isDirty()
                    || this.sel009.isDirty()
                    || this.sel010.isDirty()
                    || this.sel011.isDirty()
                    || this.sel012.isDirty()
                    || this.sel013.isDirty()
                    || this.sel014.isDirty()
                    || this.sel015.isDirty();
            },
            reset: function() {
                this.inp001.reset();
                this.sel001.reset();
                this.sel002.reset();
                this.sel003.reset();
                this.sel004.reset();
                this.sel005.reset();
                this.sel006.reset();
                this.sel007.reset();
                this.sel008.reset();
                this.sel009.reset();
                this.sel010.reset();
                this.sel011.reset();
                this.sel012.reset();
                this.sel013.reset();
                this.sel014.reset();
                this.sel015.reset();
            }
        }

        constructor() {
            let self = this;
            // processingNo
            self.index(nts.uk.ui.windows.getShared('dataRow').index());

            // define all data
            let C_SEL_001_DATA: Array<common.SelectItem> = [];

            for (let i = 1; i < 31; i++) {
                C_SEL_001_DATA.push(new common.SelectItem({ index: i, label: i + '日' }));
            }

            C_SEL_001_DATA.push(new common.SelectItem({ index: 31, label: '末日' }));

            let C_SEL_002_DATA: Array<common.SelectItem> = [
                new common.SelectItem({ index: 0, label: '当月' }),
                new common.SelectItem({ index: -1, label: '前月' })
            ];

            let C_SEL_003_DATA: Array<common.SelectItem> = [];

            for (let i = 1; i < 31; i++) {
                C_SEL_003_DATA.push(new common.SelectItem({ index: i, label: i + '日' }));
            }

            C_SEL_003_DATA.push(new common.SelectItem({ index: 31, label: '末日' }));

            let C_SEL_004_DATA: Array<common.SelectItem> = [
                new common.SelectItem({ index: 0, label: '当月' }),
                new common.SelectItem({ index: -1, label: '前月' })
            ];

            let C_SEL_005_DATA: Array<common.SelectItem> = [];

            for (let i = 1; i < 31; i++) {
                C_SEL_005_DATA.push(new common.SelectItem({ index: i, label: i + '日' }));
            }

            C_SEL_005_DATA.push(new common.SelectItem({ index: 31, label: '末日' }));

            let C_SEL_006_DATA: Array<common.SelectItem> = [
                new common.SelectItem({ index: 0, label: '当月' }),
                new common.SelectItem({ index: -1, label: '前月' }),
            ];

            let C_SEL_007_DATA: Array<common.SelectItem> = [
                new common.SelectItem({ index: -2, label: '前々月' }),
                new common.SelectItem({ index: -1, label: '前月' }),
                new common.SelectItem({ index: 0, label: '当月' }),
                new common.SelectItem({ index: 1, label: '翌月' }),
                new common.SelectItem({ index: 2, label: '翌々月' })
            ];

            let C_SEL_008_DATA: Array<common.SelectItem> = [
                new common.SelectItem({ index: -1, label: '前年' }),
                new common.SelectItem({ index: 0, label: '当年' }),
                new common.SelectItem({ index: 1, label: '翌年' }),
                new common.SelectItem({ index: 2, label: '翌々年' })
            ];

            let C_SEL_009_DATA: Array<common.SelectItem> = [
                new common.SelectItem({ index: -1, label: '前月' }),
                new common.SelectItem({ index: 0, label: '当月' })
            ];

            for (let i = 1; i <= 12; i++) {
                C_SEL_009_DATA.push(new common.SelectItem({ index: i, label: i + '月' }));
            }

            let C_SEL_010_DATA: Array<common.SelectItem> = [];

            for (let i = 1; i < 31; i++) {
                C_SEL_010_DATA.push(new common.SelectItem({ index: i, label: i + '日' }));
            }

            C_SEL_010_DATA.push(new common.SelectItem({ index: 31, label: '末日' }));

            let C_SEL_011_DATA: Array<common.SelectItem> = [];

            for (let i = 1; i <= 12; i++) {
                C_SEL_011_DATA.push(new common.SelectItem({ index: i, label: i + '月' }));
            }

            let C_SEL_012_DATA: Array<common.SelectItem> = [];

            for (let i = 1; i < 31; i++) {
                C_SEL_012_DATA.push(new common.SelectItem({ index: i, label: i + '日' }));
            }

            C_SEL_012_DATA.push(new common.SelectItem({ index: 31, label: '末日' }));

            let C_SEL_013_DATA: Array<common.SelectItem> = [
                new common.SelectItem({ index: -1, label: '前年' }),
                new common.SelectItem({ index: 0, label: '当年' }),
                new common.SelectItem({ index: 1, label: '翌年' }),
                new common.SelectItem({ index: 2, label: '翌々年' }),
            ];

            let C_SEL_014_DATA: Array<common.SelectItem> = [];

            for (let i = 1; i <= 12; i++) {
                C_SEL_014_DATA.push(new common.SelectItem({ index: i, label: i + '月' }));
            }

            let C_SEL_015_DATA: Array<common.SelectItem> = [];

            for (let i = 1; i < 31; i++) {
                C_SEL_015_DATA.push(new common.SelectItem({ index: i, label: i + '日' }));
            }

            C_SEL_015_DATA.push(new common.SelectItem({ index: 31, label: '末日' }));

            // observable all datas
            self.sel001Data(C_SEL_001_DATA);
            self.sel002Data(C_SEL_002_DATA);
            self.sel003Data(C_SEL_003_DATA);
            self.sel004Data(C_SEL_004_DATA);
            self.sel005Data(C_SEL_005_DATA);
            self.sel006Data(C_SEL_006_DATA);
            self.sel007Data(C_SEL_007_DATA);
            self.sel008Data(C_SEL_008_DATA);

            self.sel009Data(C_SEL_009_DATA);
            self.sel010Data(C_SEL_010_DATA);

            self.sel011Data(C_SEL_011_DATA);
            self.sel012Data(C_SEL_012_DATA);

            self.sel013Data(C_SEL_013_DATA);

            self.sel014Data(C_SEL_014_DATA);
            self.sel015Data(C_SEL_015_DATA);

            // reset all dirty data
            self.dirty.reset();
        }

        toggleView() {
            $('.form-extent').toggleClass('hidden');
            if (!$('.form-extent').hasClass('hidden')) {
                nts.uk.ui.windows.getSelf().setHeight(window.top.innerHeight < 801 ? 610 : 650);
                $('#contents-area').css('padding-bottom', '0');
            } else {
                nts.uk.ui.windows.getSelf().setHeight(350);
                $('#contents-area').css('padding-bottom', '');
            }
        }

        validate($root) {
            let self = this;
            return $root.errors.isEmpty() && self.inp001().trim() != '';
        }

        saveData() {
            let self = this,
                date = new Date(),
                stdYear = date.getFullYear(),
                stdMonth = date.getMonth(),
                stdDate = date.getDate();

            // Chưa thấy đề cập tới các trường hợp ngày > số ngày trong tháng.
            var data = {
                processingNo: self.index(),
                processingName: self.inp001(),
                dispSet: 0, // default db design
                currentProcessingYm: parseInt(nts.uk.time.formatDate(new Date(stdYear, stdDate <= self.sel001() ? stdMonth + 1 : stdMonth, 1), 'yyyyMM')),
                bonusAtr: 0, // default db design
                bcurrentProcessingYm: parseInt(nts.uk.time.formatDate(new Date(stdYear, stdDate <= self.sel001() ? stdMonth + 1 : stdMonth, 1), 'yyyyMM')),
                payStdDay: self.sel001(),
                pickupStdMonAtr: self.sel002(),
                pickupStdDay: self.sel003(),
                accountDueMonAtr: self.sel004(),
                accountDueDay: self.sel005(),
                payslipPrintMonth: self.sel006(),
                socialInsuLevyMonAtr: self.sel007(),
                socialInsStdYearAtr: self.sel008(),
                socialInsStdMon: self.sel009(),
                socialInsStdDay: self.sel010(),
                empInsStdMon: self.sel011(),
                empInsStdDay: self.sel012(),
                incometaxStdYearAtr: self.sel013(),
                incometaxStdMon: self.sel014(),
                incometaxStdDay: self.sel015(),
                payDays: []
            };
            /// Initial data for salary and bonus for 12 months in year
            for (var month = 0; month < 12; month++) {
                // Salary
                data.payDays.push({
                    processingNo: data.processingNo,
                    payBonusAtr: 0,
                    processingYm: parseInt(nts.uk.time.formatDate(new Date(stdYear, month, 1), 'yyyyMM')),
                    sparePayAtr: 0,
                    payDate: new Date(stdYear, month, data.payStdDay),
                    stdDate: new Date(stdYear, month + data.pickupStdMonAtr, data.pickupStdDay),
                    accountingClosing: new Date(stdYear, month + data.accountDueMonAtr, data.accountDueDay),
                    socialInsLevyMon: parseInt(nts.uk.time.formatDate(new Date(stdYear, month + data.socialInsuLevyMonAtr, 1), 'yyyyMM')),
                    socialInsStdDate: new Date(stdYear + data.socialInsStdYearAtr, data.socialInsStdMon < 1 ? month + data.socialInsStdMon : data.socialInsStdMon - 1, data.socialInsStdDay),
                    incomeTaxStdDate: new Date(stdYear + data.incometaxStdYearAtr, data.incometaxStdMon - 1, data.incometaxStdDay),
                    neededWorkDay: 0, //Interim
                    empInsStdDate: new Date(stdYear, data.empInsStdMon - 1, data.empInsStdDay),
                    stmtOutputMon: parseInt(nts.uk.time.formatDate(new Date(stdYear, month + data.payslipPrintMonth, 1), 'yyyyMM'))
                });
                // Bonus
                /*data.payDays.push({
                    processingNo: data.processingNo,
                    payBonusAtr: 1,
                    processingYm: parseInt(nts.uk.time.formatDate(new Date(stdYear, month, 1), 'yyyyMM')),
                    sparePayAtr: 0,
                    payDate: new Date(stdYear, month, data.payStdDay),
                    stdDate: new Date(stdYear, month + data.pickupStdMonAtr, data.pickupStdDay),
                    accountingClosing: new Date(stdYear, month + data.accountDueMonAtr, data.accountDueDay),
                    socialInsLevyMon: parseInt(nts.uk.time.formatDate(new Date(stdYear, month + data.socialInsuLevyMonAtr, 1), 'yyyyMM')),
                    socialInsStdDate: new Date(stdYear + data.socialInsStdYearAtr, data.socialInsStdMon < 1 ? month + data.socialInsStdMon : data.socialInsStdMon - 1, data.socialInsStdDay),
                    incomeTaxStdDate: new Date(stdYear + data.incometaxStdYearAtr, data.incometaxStdMon - 1, data.incometaxStdDay),
                    neededWorkDay: 0, //Interim
                    empInsStdDate: new Date(stdYear, data.empInsStdMon - 1, data.empInsStdDay),
                    stmtOutputMon: parseInt(nts.uk.time.formatDate(new Date(stdYear, month + data.payslipPrintMonth, 1), 'yyyyMM'))
                });*/
            }
            services.insertData(data).done(function() {
                self.dirty.reset();
                self.closeDialog();
            });
        }

        closeDialog() {
            let self = this;
            if (self.dirty.isDirty()) {
                nts.uk.ui.dialog.confirm("変更された内容が登録されていません。よろしいですか。?")
                    .ifYes(function() {
                        nts.uk.ui.windows.close();
                    });
            } else {
                nts.uk.ui.windows.close();
            }
        }
    }
}



