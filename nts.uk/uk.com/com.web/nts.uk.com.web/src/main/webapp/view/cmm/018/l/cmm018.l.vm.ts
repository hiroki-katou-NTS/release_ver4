module nts.uk.com.view.cmm018.l {
    export module viewmodel {
        export class ScreenModel {
            date: KnockoutObservable<Date>;
            constructor() {
                var self = this;
                //self.date = ko.observable('20000101');
//                var currentDate = (new Date()).toISOString().split('T')[0];
//                self.date = ko.observable(currentDate);
                self.date =ko.observable(new Date())
            }
            //閉じるボタン
            closeDialog(){
                nts.uk.ui.windows.close();    
            }
            //Excel出力
            printExcel(){
                var self = this;
                service.saveExcel(self.date())
                .done(function(){})
                .fail(function(res: any){
                      nts.uk.ui.dialog.alertError({ messageId: res.messageId});  
                })
            }
        }
    }
}
