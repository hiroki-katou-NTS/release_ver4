module sample.datepicker.viewmodel {
    export class ScreenModel {
        dateString: KnockoutObservable<string>;
        date: KnockoutObservable<Date>;
        yearMonth: KnockoutObservable<string>;
        enable: KnockoutObservable<boolean>;
        constructor() {
            var self = this;
            self.dateString = ko.observable('20000101');
            self.yearMonth = ko.observable("200002");
            self.enable = ko.observable(true);
            // NOTE: Un-comment to see diffirent between Date and UTC Date 
            //self.date = ko.observable(new Date(2000,0,1));
            self.date = ko.observable(nts.uk.time.UTCDate(2000, 0, 1));
        }
    }

    class TestDto {
        code: string;
        name: string;
        date: Date;
        dateTime: Date;
        constructor(code, name, date, dateTime) {
            this.code = code;
            this.name = name;
            this.date = date;
            this.dateTime = moment.utc(dateTime).toDate();
        }
    }
}