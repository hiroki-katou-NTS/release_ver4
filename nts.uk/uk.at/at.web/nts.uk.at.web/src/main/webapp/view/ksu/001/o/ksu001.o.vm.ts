module ksu001.o.viewmodel {

    export class ScreenModel {

        listWorkType: KnockoutObservableArray<IWorkType>;
        listWorkTime: KnockoutObservableArray<IWorkTime>;
        itemName: KnockoutObservable<string>;
        currentCode: KnockoutObservable<number>
        selectedWorkTypeCode: KnockoutObservable<string>;
        selectedWorkTimeCode: KnockoutObservable<string>;
        time1: KnockoutObservable<string>;
        time2: KnockoutObservable<string>;
        roundingRules: KnockoutObservableArray<any>;
        selectedRuleCode: any;

        constructor() {
            let self = this;
            self.listWorkType = ko.observableArray([]);
            self.listWorkTime = ko.observableArray([]);

            self.roundingRules = ko.observableArray([
                //KSU001_71
                { code: '1', name: nts.uk.resource.getText("リスト内検索") },
                //KSU001_72
                { code: '2', name: nts.uk.resource.getText("全件検索") }
            ]);
            self.selectedRuleCode = ko.observable(1);
            self.itemName = ko.observable('');
            self.currentCode = ko.observable(3);
            self.selectedWorkTypeCode = ko.observable('');
            self.selectedWorkTimeCode = ko.observable('');
            self.time1 = ko.observable('12:00');
            self.time2 = ko.observable('15:00');

            self.findWorkType();
            self.findWorkTime();
        }

        start() {
            let self = this;
            var dfd = $.Deferred();
            dfd.resolve();
            return dfd.promise();
        }

        /**
         * find data in DB WORK_TYPE
         */
        findWorkType(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            service.getWorkType().done(function(data: WorkType) {
                if (data) {
                    _.each(data, function(wT) {
                        self.listWorkType.push(new WorkType({
                            workTypeCode: wT.workTypeCode,
                            sortOrder: wT.sortOrder,
                            symbolicName: wT.symbolicName,
                            name: wT.name,
                            abbreviationName: wT.abbreviationName,
                            memo: wT.memo,
                            displayAtr: wT.displayAtr
                        }));
                    });
                    //                    self.selectedWorkTypeCode(self.listWorkType()[0].workTypeCode);
                }
                dfd.resolve();
            }).fail(function() {
                dfd.reject();
            });
            return dfd.promise();
        }

        /**
         * find data in DB WORK_TIME
         */
        findWorkTime(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            service.getWorkTime().done(function(data) {
                if (data) {
                    _.each(data, function(wT) {
                        self.listWorkTime.push(new WorkTime({
                            siftCd: wT.siftCd,
                            name: wT.name,
                            abName: wT.abName,
                            dailyWorkAtr: wT.dailyWorkAtr,
                            methodAtr: wT.methodAtr,
                            displayAtr: wT.dailyWorkAtr,
                            note: wT.note,
                        }));
                    });
                    //                    self.selectedWorkTimeCode(self.listWorkTime()[0].siftCd);
                }
                dfd.resolve();
            }).fail(function() {
                dfd.reject();
            });
            return dfd.promise();
        }
    }

    interface IWorkType {
        workTypeCode: string,
        sortOrder: number,
        symbolicName: string,
        name: string,
        abbreviationName: string,
        memo: string,
        displayAtr: number
    }

    class WorkType {
        workTypeCode: string;
        sortOrder: number;
        symbolicName: string;
        name: string;
        abbreviationName: string;
        memo: string;
        displayAtr: number;
        labelDisplay: string

        constructor(params: IWorkType) {
            this.workTypeCode = params.workTypeCode;
            this.sortOrder = params.sortOrder;
            this.symbolicName = params.symbolicName;
            this.name = params.name;
            this.abbreviationName = params.abbreviationName;
            this.memo = params.memo;
            this.displayAtr = params.displayAtr;
            this.labelDisplay = '  ' + this.workTypeCode + '  ' + this.abbreviationName + '  ' + this.name + ' ( ' + this.memo + ' )';
        }
    }

    interface IWorkTime {
        siftCd: string,
        name: string,
        abName: string,
        dailyWorkAtr: number,
        methodAtr: number,
        displayAtr: number,
        note: string
    }

    class WorkTime {
        siftCd: string;
        name: string;
        abName: string;
        dailyWorkAtr: number;
        methodAtr: number;
        displayAtr: number;
        note: string;
        labelDisplay: string;

        constructor(params: IWorkTime) {
            this.siftCd = params.siftCd;
            this.name = params.name;
            this.abName = params.abName;
            this.dailyWorkAtr = params.dailyWorkAtr;
            this.methodAtr = params.methodAtr;
            this.displayAtr = params.displayAtr;
            this.note = params.note;
            this.labelDisplay = '  ' + this.siftCd + '  ' + this.abName + '  ' + this.name + '  ' + 'timeZone1 + timeZone2 ' + '( ' + this.note + ' )';
        }
    }
}