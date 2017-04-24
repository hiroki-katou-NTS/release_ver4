module qet002.a.viewmodel {
    export class ScreenModel {
        targetYear: KnockoutObservable<number>;
        isLowerLimit: KnockoutObservable<boolean>;
        lowerLimitValue: KnockoutObservable<number>;
        isUpperLimit: KnockoutObservable<boolean>;
        upperLimitValue: KnockoutObservable<number>;
        japanYear: KnockoutObservable<string>;

        constructor() {
            var self = this;
            self.targetYear = ko.observable(2016);
            self.isLowerLimit = ko.observable(true);
            self.isUpperLimit = ko.observable(true);
            self.lowerLimitValue = ko.observable(null);
            self.japanYear = ko.observable('(' + nts.uk.time.yearInJapanEmpire('2016') + ')');
            self.upperLimitValue = ko.observable(null);           
            self.targetYear.subscribe(function(val: number){
               self.japanYear(""+nts.uk.time.yearInJapanEmpire(val)); 
            });
        }

        /**
         * Start screen.
         */
        public start(): JQueryPromise<void> {
            var dfd = $.Deferred<void>();
            dfd.resolve();
            return dfd.promise();
        }


        /**
         * Print Report
         */
       public printData(){  
       
           // Validate
            var hasError = false;
            if (this.targetYear() == null) {
                hasError = true;
                $('#target-year-input').ntsError('set', '未入力エラー');
            }
            if (this.isLowerLimit() == true) {
                if (this.lowerLimitValue() == null) {
                    hasError = true;
                    $('#lower-limit-input').ntsError('set', '未入力エラー');
                }
            }
            if (this.isUpperLimit() == true) {
                if (this.upperLimitValue() == null) {
                    hasError = true;
                    $('#upper-limit-input').ntsError('set', '未入力エラー');
                }
            }
            if ((this.isLowerLimit() == true) && (this.isUpperLimit() == true)) {
                if (this.lowerLimitValue() > this.upperLimitValue()) {
                    hasError = true;
                    $('#lower-limit-input').ntsError('set', '未入力エラー');
                }
            }
            if (this.isLowerLimit() == false){                             
                $('#lower-limit-input').ntsError('clear');           
            }
            if (this.isUpperLimit() == false){
                $('#upper-limit-input').ntsError('clear');           
            }
            if (hasError) {
                return;
            }
           
           //Print Report
           service.printService(this).done(function(data: any) {                
            }).fail(function(res) {
                nts.uk.ui.dialog.alert(res.message);
            })

        }
    }
    
    /**
     * Accumulated Payment Result Dto
     */
    export class AccumulatedPaymentResultViewModel {
        empDesignation: string;
        empCode: string;
        empName: string;
        taxAmount: number;
        socialInsuranceAmount: number;
        widthHoldingTaxAmount: number;
        enrollmentStatus: string;
        directionalStatus: string;
        amountAfterTaxDeduction: number;

        constructor(empDesignation: string, empCode: string, empName: string, taxAmount: number, socialInsuranceAmount: number,
            widthHoldingTaxAmount: number, enrollmentStatus: string, directionalStatus: string, amountAfterTaxDeduction: number) {
            var self = this;
            self.empDesignation = empDesignation;
            self.empCode = empCode;
            self.empName = empName;
            self.taxAmount = taxAmount;
            self.socialInsuranceAmount = socialInsuranceAmount;
            self.widthHoldingTaxAmount = widthHoldingTaxAmount;
            self.enrollmentStatus = enrollmentStatus;
            self.directionalStatus = directionalStatus;
            self.amountAfterTaxDeduction = amountAfterTaxDeduction;
        }

    }
}