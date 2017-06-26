module sample.datepicker.viewmodel {
    export class ScreenModel {
        dateString: KnockoutObservable<string>;
        date: KnockoutObservable<Date>;
        yearMonth: KnockoutObservable<number>;
        constructor() {
            var self = this;
            self.dateString = ko.observable('20000101');
            self.yearMonth = ko.observable("200002");
            
            // Define styles
            self.cssRangerYM = { 2000: [{ 1: "round-green" }, { 5: "round-yellow" }],
                                2002: [ 1, { 5: "round-gray" }]};
            self.cssRangerYMD = {
                2000: {1: [{ 11: "round-green" }, { 12: "round-orange" }, { 15: "rect-pink" }], 3: [{ 1: "round-green" }, { 2: "round-purple" }, 3 ]},
                2002: {1: [{ 11: "round-green" }, { 12: "round-green" }, { 15: "round-green" }], 3: [{ 1: "round-green" }, { 2: "round-green" }, { 3: "round-green" }]} 
            }
        }
    }
}