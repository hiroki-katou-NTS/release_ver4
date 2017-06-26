module qmm005.d {
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
        sel016: common.CheckBoxItem = new common.CheckBoxItem({ enable: true, checked: false, text: 'この処理区分を廃止する' });

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
            sel016: new nts.uk.ui.DirtyChecker(this.sel016.checked),
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
                    || this.sel015.isDirty()
                    || this.sel016.isDirty();
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
                this.sel016.reset();
            }
        }

        constructor() {
            let self = this;
            // processingNo
            self.index(nts.uk.ui.windows.getShared('dataRow').index());
            self.sel016.checked(nts.uk.ui.windows.getShared('dataRow').dispSet);
            // define all data
            let D_SEL_001_DATA: Array<common.SelectItem> = [];

            for (let i = 1; i < 31; i++) {
                D_SEL_001_DATA.push(new common.SelectItem({ index: i, label: i + '日' }));
            }

            D_SEL_001_DATA.push(new common.SelectItem({ index: 31, label: '末日' }));

            let D_SEL_002_DATA: Array<common.SelectItem> = [
                new common.SelectItem({ index: 0, label: '当月' }),
                new common.SelectItem({ index: -1, label: '前月' })
            ];

            let D_SEL_003_DATA: Array<common.SelectItem> = [];

            for (let i = 1; i < 31; i++) {
                D_SEL_003_DATA.push(new common.SelectItem({ index: i, label: i + '日' }));
            }

            D_SEL_003_DATA.push(new common.SelectItem({ index: 31, label: '末日' }));

            let D_SEL_004_DATA: Array<common.SelectItem> = [
                new common.SelectItem({ index: 0, label: '当月' }),
                new common.SelectItem({ index: -1, label: '前月' })
            ];

            let D_SEL_005_DATA: Array<common.SelectItem> = [];

            for (let i = 1; i < 31; i++) {
                D_SEL_005_DATA.push(new common.SelectItem({ index: i, label: i + '日' }));
            }

            D_SEL_005_DATA.push(new common.SelectItem({ index: 31, label: '末日' }));

            let D_SEL_006_DATA: Array<common.SelectItem> = [
                new common.SelectItem({ index: 0, label: '当月' }),
                new common.SelectItem({ index: -1, label: '前月' }),
            ];

            let D_SEL_007_DATA: Array<common.SelectItem> = [
                new common.SelectItem({ index: -2, label: '前々月' }),
                new common.SelectItem({ index: -1, label: '前月' }),
                new common.SelectItem({ index: 0, label: '当月' }),
                new common.SelectItem({ index: 1, label: '翌月' }),
                new common.SelectItem({ index: 2, label: '翌々月' })
            ];

            let D_SEL_008_DATA: Array<common.SelectItem> = [
                new common.SelectItem({ index: -1, label: '前年' }),
                new common.SelectItem({ index: 0, label: '当年' }),
                new common.SelectItem({ index: 1, label: '翌年' }),
                new common.SelectItem({ index: 2, label: '翌々年' })
            ];

            let D_SEL_009_DATA: Array<common.SelectItem> = [
                new common.SelectItem({ index: -1, label: '前月' }),
                new common.SelectItem({ index: 0, label: '当月' })
            ];

            for (let i = 1; i <= 12; i++) {
                D_SEL_009_DATA.push(new common.SelectItem({ index: i, label: i + '月' }));
            }

            let D_SEL_010_DATA: Array<common.SelectItem> = [];

            for (let i = 1; i < 31; i++) {
                D_SEL_010_DATA.push(new common.SelectItem({ index: i, label: i + '日' }));
            }

            D_SEL_010_DATA.push(new common.SelectItem({ index: 31, label: '末日' }));

            let D_SEL_011_DATA: Array<common.SelectItem> = [];

            for (let i = 1; i <= 12; i++) {
                D_SEL_011_DATA.push(new common.SelectItem({ index: i, label: i + '月' }));
            }

            let D_SEL_012_DATA: Array<common.SelectItem> = [];

            for (let i = 1; i < 31; i++) {
                D_SEL_012_DATA.push(new common.SelectItem({ index: i, label: i + '日' }));
            }

            D_SEL_012_DATA.push(new common.SelectItem({ index: 31, label: '末日' }));

            let D_SEL_013_DATA: Array<common.SelectItem> = [
                new common.SelectItem({ index: -1, label: '前年' }),
                new common.SelectItem({ index: 0, label: '当年' }),
                new common.SelectItem({ index: 1, label: '翌年' }),
                new common.SelectItem({ index: 2, label: '翌々年' }),
            ];

            let D_SEL_014_DATA: Array<common.SelectItem> = [];

            for (let i = 1; i <= 12; i++) {
                D_SEL_014_DATA.push(new common.SelectItem({ index: i, label: i + '月' }));
            }

            let D_SEL_015_DATA: Array<common.SelectItem> = [];

            for (let i = 1; i < 31; i++) {
                D_SEL_015_DATA.push(new common.SelectItem({ index: i, label: i + '日' }));
            }

            D_SEL_015_DATA.push(new common.SelectItem({ index: 31, label: '末日' }));

            // observable all props
            services.getData(self.index()).done(function(resp: Array<any>) {
                if (resp && resp.length == 2) {
                    self.inp001(nts.uk.ui.windows.getShared('dataRow').label());
                    if (resp[0]) {
                        self.sel001(resp[0].payStdDay || self.sel001());
                        self.sel002(resp[0].pickupStdMonAtr || self.sel002());
                        self.sel003(resp[0].pickupStdDay || self.sel003());
                        self.sel004(resp[0].accountDueMonAtr || self.sel004());
                        self.sel005(resp[0].accountDueDay || self.sel005());
                        self.sel006(resp[0].payslipPrintMonth || self.sel006());
                        self.sel007(resp[0].socialInsLevyMonAtr || self.sel007());
                    }

                    if (resp[1]) {
                        self.sel008(resp[1].socialInsStdYearAtr || self.sel008());
                        self.sel009(resp[1].socialInsStdMon || self.sel009());
                        self.sel010(resp[1].socialInsStdDay || self.sel010());
                        self.sel011(resp[1].empInsStdMon || self.sel011());
                        self.sel012(resp[1].empInsStdDay || self.sel012());
                        self.sel013(resp[1].incometaxStdYearAtr || self.sel013());
                        self.sel014(resp[1].incometaxStdMon || self.sel014());
                        self.sel015(resp[1].incometaxStdDay || self.sel015());
                    }

                    // reset all dirty data
                    self.dirty.reset();
                }
            });

            // observable all datas
            self.sel001Data(D_SEL_001_DATA);
            self.sel002Data(D_SEL_002_DATA);
            self.sel003Data(D_SEL_003_DATA);
            self.sel004Data(D_SEL_004_DATA);
            self.sel005Data(D_SEL_005_DATA);
            self.sel006Data(D_SEL_006_DATA);
            self.sel007Data(D_SEL_007_DATA);
            self.sel008Data(D_SEL_008_DATA);

            self.sel009Data(D_SEL_009_DATA);
            self.sel010Data(D_SEL_010_DATA);

            self.sel011Data(D_SEL_011_DATA);
            self.sel012Data(D_SEL_012_DATA);

            self.sel013Data(D_SEL_013_DATA);

            self.sel014Data(D_SEL_014_DATA);
            self.sel015Data(D_SEL_015_DATA);
        }

        toggleView() {
            $('.form-extent').toggleClass('hidden');
            if (!$('.form-extent').hasClass('hidden')) {
                nts.uk.ui.windows.getSelf().setHeight(window.top.innerHeight < 801 ? 620 : 660);
                $('#contents-area').css('padding-bottom', '0');
            } else {
                nts.uk.ui.windows.getSelf().setHeight(370);
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
                payBonusAtr: 0,
                sparePayAtr: 0,
                processingNo: self.index(),
                processingName: self.inp001(),
                dispSet: self.sel016.checked() ==  true ? 1 : 0,
                currentProcessingYm: parseInt(nts.uk.time.formatDate(new Date(stdYear, stdDate <= self.sel001() ? stdMonth + 1 : stdMonth, 1), 'yyyyMM')),
                bonusAtr: 0,
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
                incometaxStdDay: self.sel015()
            };
            services.updateData(data).done(function() {
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



