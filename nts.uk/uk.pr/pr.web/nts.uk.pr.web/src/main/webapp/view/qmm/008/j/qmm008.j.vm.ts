module nts.uk.pr.view.qmm008.j {
    import getShared = nts.uk.ui.windows.getShared;
    export module viewmodel {
       export class ScreenModel {
           
           dataList: KnockoutObservableArray<RowData>;
           officeCode: KnockoutObservable<string> = ko.observable(null);
           socialInsuranceName: KnockoutObservable<string> = ko.observable(null);
           startMonth: KnockoutObservable<string> = ko.observable(null);
           endMonth: KnockoutObservable<string> = ko.observable(null);
           displayStart: KnockoutObservable<string> = ko.observable(null);
           displayEnd: KnockoutObservable<string> = ko.observable(null);
           historyId: KnockoutObservable<string> = ko.observable(null);
           isEnableBtnPdf: KnockoutObservable<boolean> = ko.observable(false);
           constructor(){
               var self = this;
               self.dataList = ko.observableArray([]);
               let dataGetShare = getShared("QMM008_J_PARAMS");
               self.officeCode(dataGetShare.selectedOffice.socialInsuranceCode);
               self.socialInsuranceName(dataGetShare.selectedOffice.socialInsuranceName);
               self.displayStart(dataGetShare.selectedHistory.displayStart);
               self.displayEnd(dataGetShare.selectedHistory.displayEnd);
               self.historyId(dataGetShare.selectedHistory.historyId);
               self.startMonth(dataGetShare.selectedHistory.startMonth);
               let command = { historyId: self.historyId(),date : self.startMonth(),socialInsuranceCode: self.officeCode() };
               nts.uk.pr.view.qmm008.j.service.start(command).done(function(response) {
                    for (var i = 0; i < response.length; i++) {
                            self.dataList.push(new RowData(response[i]));
                     }
                   setTimeout(function () {
                       $(".nts-fixed-table").attr('tabindex', '5');
                       $(".nts-fixed-table").focus();
                       if(self.dataList().length > 10) {
                           if (/Edge/.test(navigator.userAgent)) {
                               $('#J3_1_container .scroll-header').addClass('edge_scroll_header');
                               $('#J3_1_container .scroll-header').addClass('edge_scroll_header');
                           } else {
                               $('#J3_1_container .scroll-header').addClass('ci_scroll_header');
                               $('#J3_1_container .scroll-header').addClass('ci_scroll_header');
                           }

                       }
                   }, 100);

               });

               $("#fixed-table").ntsFixedTable({ height: 363, width: 800 });
           }
           
            
           genNumber(itemNumber: any, decimalPart: any) {
               let option: any;
               if (nts.uk.text.isNullOrEmpty(decimalPart)) {
                   option = new nts.uk.ui.option.NumberEditorOption({ grouplength: 3, decimallength: 0 });

               } else {
                   option = new nts.uk.ui.option.NumberEditorOption({ grouplength: 3, decimallength: decimalPart });

               }
               return nts.uk.ntsNumber.formatNumber(itemNumber, option);
           }
           
           
           /**
            *  close dialog
            */

           private closeDialog(): void {
               nts.uk.ui.windows.close();
           }
           
           private update(): void {
               $('#J3_1_container .nts-input').trigger("validate");
               if (nts.uk.ui.errors.hasError()) {
                   return;
               }
               let self = this;
               let command = {
                   data: ko.toJS(self.dataList()),
                   historyId: self.historyId(),
                   socialInsuranceCode: self.officeCode()
               };
               nts.uk.pr.view.qmm008.j.service.update(command).done(function(response) {
                   nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                       setTimeout(function () {
                           $(".nts-fixed-table").attr('tabindex', '0');
                           $(".nts-fixed-table").focus();
                       }, 500);
                   });
               });
           }
           
           private countReview(): void {
               nts.uk.ui.errors.clearAll();
               let self = this;
               let command = { historyId: self.historyId(), date: self.startMonth(),socialInsuranceCode: self.officeCode() };
               nts.uk.pr.view.qmm008.j.service.count(command).done(function(response) {
                   self.dataList([]);
                   for (var i = 0; i < response.length; i++) {
                       self.dataList.push(new RowData(response[i]));
                   }
                   setTimeout(function () {
                       $(".nts-fixed-table").attr('tabindex', '5');
                       $(".nts-fixed-table").focus();
                   }, 500);
               });
            }
       }
    }
    
    interface IRowData {
        welfarePensionGrade: KnockoutObservable<string>,
        standardMonthlyFee: KnockoutObservable<string>,
        rewardMonthlyLowerLimit: KnockoutObservable<string>,
        rewardMonthlyUpperLimit: KnockoutObservable<string>,
        childCareContribution: KnockoutObservable<string>
    
    }
    
    export class RowData {
        welfarePensionGrade: KnockoutObservable<string>  = ko.observable(null);
        standardMonthlyFee: KnockoutObservable<string>  = ko.observable(null);
        rewardMonthlyLowerLimit: KnockoutObservable<string>  = ko.observable(null);
        rewardMonthlyUpperLimit: KnockoutObservable<string>  = ko.observable(null);
        childCareContribution: KnockoutObservable<string>  = ko.observable(null);
        
        constructor(parameter? :IRowData) {
            this.welfarePensionGrade(parameter.welfarePensionGrade);
            this.standardMonthlyFee(parameter.standardMonthlyFee);
            this.rewardMonthlyLowerLimit(parameter.rewardMonthlyLowerLimit);
            this.rewardMonthlyUpperLimit(parameter.rewardMonthlyUpperLimit);
            this.childCareContribution(parameter.childCareContribution);
            
        }
    }
    
}