module nts.uk.at.view.ksm005.f {

    import MonthlyPatternDto = service.model.MonthlyPatternDto;
    export module viewmodel {

        export class ScreenModel {
            columnMonthlyPatterns: KnockoutObservableArray<NtsGridListColumn>;
            lstMonthlyPattern: KnockoutObservableArray<MonthlyPatternDto>;
            selectMonthlyPattern: KnockoutObservable<string>;                 
            employeeId: string; 


           constructor() {
               var self = this;
               self.columnMonthlyPatterns = ko.observableArray([
                   { headerText: nts.uk.resource.getText("KSM005_13"), key: 'code', width: 100 },
                   { headerText: nts.uk.resource.getText("KSM005_14"), key: 'name', width: 150 }
               ]);
               var monthlyPatterns: MonthlyPatternDto[] = [];
               self.lstMonthlyPattern = ko.observableArray(monthlyPatterns);
               self.selectMonthlyPattern = ko.observable('');
               self.employeeId = nts.uk.ui.windows.getShared("employeeId");
               self.selectMonthlyPattern(nts.uk.ui.windows.getShared("monthlyPatternCode"));
           }
            /**
             * start page when init data
             */
            public startPage(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred();
                service.findAllMonthlyPattern().done(function(data) {
                    self.lstMonthlyPattern(data);
                    if (!self.selectMonthlyPattern() && data && data.length > 0) {
                        self.selectMonthlyPattern(data[0].code);
                    }
                    dfd.resolve(self);
                });
                return dfd.promise();
            }
            /**
             * call service add MonthlyPatternSetting
             */
            public saveMonthlyPatternSetting(): void{
                var self = this;
                nts.uk.ui.windows.setShared("monthlyPatternCode", self.selectMonthlyPattern());
                nts.uk.ui.windows.close();
            }
        
        }

    }
}