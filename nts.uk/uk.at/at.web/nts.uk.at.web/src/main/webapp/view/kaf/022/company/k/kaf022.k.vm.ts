module nts.uk.at.view.kaf022.k.viewmodel {
    import text = nts.uk.resource.getText;

    export class ScreenModelK {
        itemListC27: KnockoutObservableArray<ItemModel> = ko.observableArray([
            {code: 1, name: text('KAF022_100')},
            {code: 0, name: text('KAF022_101')},
            {code: 2, name: text('KAF022_171')}
        ]);
        itemListC48: KnockoutObservableArray<ItemModel> = ko.observableArray([
            {code: 1, name: text('KAF022_420')},
            {code: 0, name: text('KAF022_421')}
        ]);
        itemListD15: KnockoutObservableArray<ItemModel> = ko.observableArray([
            {code: 0, name: text('KAF022_391')},
            {code: 1, name: text('KAF022_392')}
        ]);
        itemListD13: KnockoutObservableArray<ItemModel> = ko.observableArray([
            {code: 1, name: text('KAF022_389')},
            {code: 0, name: text('KAF022_390')}
        ]);
        itemListCC1: KnockoutObservableArray<ItemModel> = ko.observableArray([
            {code: 0, name: text('KAF022_75')},
            {code: 1, name: text('KAF022_82')},
        ]);
        itemListK13: KnockoutObservableArray<ItemModel> = ko.observableArray([
            {code: 1, name: text('KAF022_292')},
            {code: 0, name: text('KAF022_291')},
        ]);
        itemListK14: KnockoutObservableArray<ItemModel> = ko.observableArray([
            {code: 1, name: text('KAF022_272')},
            {code: 0, name: text('KAF022_273')},
        ]);

        reflectWorkHour: KnockoutObservable<number>;
        reflectAttendance: KnockoutObservable<number>;
        oneDayLeaveDeleteAttendance: KnockoutObservable<number>;

        simultaneousApplyRequired: KnockoutObservable<number>;
        allowanceForAbsence: KnockoutObservable<number>;

        reflectAttendanceAtr: KnockoutObservable<number>;

        texteditorD9: KnockoutObservable<string>;
        valueD10: KnockoutObservable<string>;
        enableD11: KnockoutObservable<boolean>;

        texteditorD12: KnockoutObservable<string>;
        valueD10_1: KnockoutObservable<string>;
        enableD11_1: KnockoutObservable<boolean>;

        constructor() {
            const self = this;

            self.oneDayLeaveDeleteAttendance = ko.observable(0);
            self.reflectAttendance = ko.observable(0);
            self.reflectWorkHour = ko.observable(0);
            
            self.simultaneousApplyRequired = ko.observable(0);
            self.allowanceForAbsence = ko.observable(0);

            self.reflectAttendanceAtr = ko.observable(0);

            self.texteditorD9 = ko.observable(null);
            self.valueD10 = ko.observable(null);
            self.enableD11 = ko.observable(false);

            self.texteditorD12 = ko.observable(null);
            self.valueD10_1 = ko.observable(null);
            self.enableD11_1 = ko.observable(false);

            $("#fixed-table-k1").ntsFixedTable({});
            $("#fixed-table-k2").ntsFixedTable({});
            $("#fixed-table-k3").ntsFixedTable({});
            $("#fixed-table-k4").ntsFixedTable({});
        }

        initData(allData: any): void {
            const self = this;
            if (allData.substituteWorkApplicationReflect) {
                self.reflectAttendanceAtr(allData.substituteWorkApplicationReflect.reflectAttendanceAtr || 0);
            }
            if (allData.substituteLeaveApplicationReflect) {
                self.reflectWorkHour(allData.substituteLeaveApplicationReflect.reflectWorkHour || 0);
                self.reflectAttendance(allData.substituteLeaveApplicationReflect.reflectAttendance || 0);
                self.oneDayLeaveDeleteAttendance(allData.substituteLeaveApplicationReflect.oneDayLeaveDeleteAttendance || 0);
            }
            if (allData.substituteHdWorkApplicationSetting) {
                const data = allData.substituteHdWorkApplicationSetting;
                self.simultaneousApplyRequired(data.simultaneousApplyRequired || 0);
                self.allowanceForAbsence(data.allowanceForAbsence || 0);

                self.texteditorD9(data.subHolidayComment || "");
                self.valueD10(data.subHolidayColor);
                self.enableD11(data.subHolidayBold);

                self.texteditorD9(data.subWorkComment || "");
                self.valueD10(data.subWorkColor);
                self.enableD11(data.subWorkBold);
            }
        }

        collectData(): any {
            const self = this;
            return {
                drawOutApplicationReflect: {
                    reflectAttendanceAtr: self.reflectAttendanceAtr()
                },
                suspenseApplicationReflect: {
                    reflectWorkHour: self.reflectWorkHour(),
                    reflectAttendance: self.reflectAttendance(),
                    oneDayLeaveDeleteAttendance: self.oneDayLeaveDeleteAttendance()
                },
                suspenseDrawOutApplicationSetting: {
                    simultaneousApplyRequired: self.simultaneousApplyRequired(),
                    allowanceForAbsence: self.allowanceForAbsence(),
                    subHolidayComment: self.texteditorD12(),
                    subHolidayColor: self.valueD10_1(),
                    subHolidayBold: self.enableD11_1(),
                    subWorkComment: self.texteditorD9(),
                    subWorkColor: self.valueD10(),
                    subWorkBold: self.enableD11()
                }
            };
        }
    }

    class ItemModel {
        code: number;
        name: string;

        constructor(code: number, name: string) {
            this.code = code;
            this.name = name;
        }
    }

}