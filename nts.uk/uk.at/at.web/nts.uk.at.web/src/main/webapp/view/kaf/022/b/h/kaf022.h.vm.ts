module nts.uk.at.view.kaf022.h.viewmodel {
    import getText = nts.uk.resource.getText;

    export class ScreenModelH {
        itemListB8: KnockoutObservableArray<ItemModel> = ko.observableArray([
            {code: 1, name: getText('KAF022_75')},
            {code: 0, name: getText('KAF022_82')}
        ]);
        itemListB242: KnockoutObservableArray<ItemModel> = ko.observableArray([
            {code: 1, name: getText('KAF022_75')},
            {code: 0, name: getText('KAF022_82')},
        ]);
        itemListH3: KnockoutObservableArray<ItemModel> = ko.observableArray([
            {code: 1, name: getText('KAF022_44')},
            {code: 0, name: getText('KAF022_396')}
        ]);

        // 時間年休
        annualVacationTime: KnockoutObservable<number>;

        // 60H超休
        superHoliday60H: KnockoutObservable<number>;

        //時間代休
        substituteLeaveTime: KnockoutObservable<number>;

        //介護
        nursing: KnockoutObservable<number>;

        // 子看護
        childNursing: KnockoutObservable<number>;

        // 時間特別休暇
        specialVacationTime: KnockoutObservable<number>;

        // 出勤前(1回目勤務)
        firstBeforeWork: KnockoutObservable<number>;

        // 出勤前(2回目勤務)
        secondBeforeWork: KnockoutObservable<number>;

        // 退勤後(1回目勤務)
        firstAfterWork: KnockoutObservable<number>;

        // 退勤後(2回目勤務)
        secondAfterWork: KnockoutObservable<number>;

        // 私用外出
        privateGoingOut: KnockoutObservable<number>;

        // 組合外出
        unionGoingOut: KnockoutObservable<number>;

        // 実績の時間帯へ反映する
        reflectActualTimeZone: KnockoutObservable<number>;

        constructor() {
            const self = this;
            self.annualVacationTime = ko.observable(0);
            self.superHoliday60H = ko.observable(0);
            self.substituteLeaveTime = ko.observable(0);
            self.nursing = ko.observable(0);
            self.childNursing = ko.observable(0);
            self.specialVacationTime = ko.observable(0);

            self.firstBeforeWork = ko.observable(0);
            self.secondBeforeWork = ko.observable(0);
            self.firstAfterWork = ko.observable(0);
            self.secondAfterWork = ko.observable(0);
            self.privateGoingOut = ko.observable(0);
            self.unionGoingOut = ko.observable(0);

            self.reflectActualTimeZone = ko.observable(1);

            $("#fixed-table-h1").ntsFixedTable({});
            $("#fixed-table-h2").ntsFixedTable({});
            $("#fixed-table-h3").ntsFixedTable({});
        }

        initData(allData: any): void {
            const self = this;
            const dataReflect = allData.timeLeaveApplicationReflect;
            if (dataReflect) {
                self.reflectActualTimeZone(dataReflect.reflectActualTimeZone || 0);
                if (dataReflect.destination) {
                    self.firstBeforeWork(dataReflect.destination.firstBeforeWork || 0);
                    self.secondBeforeWork(dataReflect.destination.secondBeforeWork || 0);
                    self.firstAfterWork(dataReflect.destination.firstAfterWork || 0);
                    self.secondAfterWork(dataReflect.destination.secondAfterWork || 0);
                    self.privateGoingOut(dataReflect.destination.privateGoingOut || 0);
                    self.unionGoingOut(dataReflect.destination.unionGoingOut || 0);
                }
                if (dataReflect.condition) {
                    self.annualVacationTime(dataReflect.condition.annualVacationTime || 0);
                    self.childNursing(dataReflect.condition.childNursing || 0);
                    self.nursing(dataReflect.condition.nursing || 0);
                    self.specialVacationTime(dataReflect.condition.specialVacationTime || 0);
                    self.substituteLeaveTime(dataReflect.condition.substituteLeaveTime || 0);
                    self.superHoliday60H(dataReflect.condition.superHoliday60H || 0);

                }
            }
        }

        collectData(): any {
            const self = this;
            return {
                reflectActualTimeZone: self.reflectActualTimeZone(),
                destination: {
                    firstBeforeWork: self.firstBeforeWork(),
                    secondBeforeWork: self.secondBeforeWork(),
                    firstAfterWork: self.firstAfterWork(),
                    secondAfterWork: self.secondAfterWork(),
                    privateGoingOut: self.privateGoingOut(),
                    unionGoingOut: self.unionGoingOut()
                },
                condition: {
                    superHoliday60H: self.superHoliday60H(),
                    nursing: self.nursing(),
                    childNursing: self.childNursing(),
                    substituteLeaveTime: self.substituteLeaveTime(),
                    annualVacationTime: self.annualVacationTime(),
                    specialVacationTime: self.specialVacationTime()
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