import setShared = nts.uk.ui.windows.setShared;
import getShared = nts.uk.ui.windows.getShared;
module nts.uk.at.view.kmk004.b.viewmodel {
    
        export class ScreenModel {
            // object cal day set receive from kml004-A
            calDaySet: KnockoutObservable<any>;
            // check checkbox hidden
            check: KnockoutObservable<boolean>;
            // check_1
            yearHd: KnockoutObservable<boolean>;
            // check 2
            heavyHd: KnockoutObservable<boolean>;
            // check 3 
            specialHoliday: KnockoutObservable<boolean>;
            // check 4
            halfDay: KnockoutObservable<boolean>;
            share: KnockoutObservable<any>;
            constructor() {
                let self = this;
                self.check = ko.observable(true);
                self.yearHd = ko.observable(false);
                self.heavyHd = ko.observable(false);
                self.specialHoliday = ko.observable(false);
                self.halfDay = ko.observable(false);
                self.share = ko.observable(null);
                self.calDaySet = ko.observable(getShared("KML004A_DAY_SET"));
            }


            /**
             * Event on start page.
             */
            public startPage(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred();
                service.getAll().done(function(data) {
                    self.check(true);
                    if(self.check() == false){
                        nts.uk.ui.windows.getSelf().setHeight(400);    
                    }
                    self.halfDay(self.calDaySet().halfDay);
                    self.yearHd(self.calDaySet().yearHd);
                    self.specialHoliday(self.calDaySet().specialHoliday);
                    self.heavyHd(self.calDaySet().heavyHd);
                    dfd.resolve();
                });
                return dfd.promise();
            }

            /**
             * click register button, send cal day set to screen A 
             */
            public save(): void {
                var self = this;
                let halfDay = self.halfDay() == true ? 1 : 0;
                let yearHd = self.yearHd() == true ? 1 : 0;
                let specialHoliday = self.specialHoliday() == true ? 1 : 0;
                let heavyHd = self.heavyHd() == true ? 1 : 0;
                let calSet = new CalDaySet(self.calDaySet().categoryCode, halfDay, yearHd, specialHoliday, heavyHd);
                self.share(calSet);
                setShared('KML004B_DAY_SET', calSet);
                nts.uk.ui.windows.close();
            }

            /**
             * Event on click cancel button.
             */
            public cancel(): void {
                let self = this;
                setShared('KML004B_DAY_SET', null);
                nts.uk.ui.windows.close();
            }
        }
    export class CalDaySet{
        categoryCode: string;
        halfDay: number;
        yearHd: number;
        specialHoliday: number;
        heavyHd: number;
        constructor(categoryCode: string, halfDay: number, yearHd: number, specialHoliday: number, heavyHd: number){
            this.categoryCode = categoryCode;
            this.halfDay = halfDay;
            this.yearHd = yearHd;
            this.specialHoliday = specialHoliday; 
            this.heavyHd = heavyHd;
        }  
    }
}